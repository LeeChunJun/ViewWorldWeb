package com.licj.viewworldweb.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.licj.viewworldweb.utils.MusicCrawler.PlayDetailList;

public class MusicFetcher {
	private static final Logger logger = Logger.getLogger(MusicFetcher.class);

	public static final String StoreDir = "D:/12-licj/eclipse-workspace/ViewWorldWeb/src/main/java/resource/";

	public static void main(String[] args) {
		MusicFetcher musicFetcher = new MusicFetcher();
		Gson gson = new Gson();
		// 读取存储的结果
		try (InputStream inputStream = new FileInputStream(StoreDir + "pDetailList.json");
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charsets.UTF_8));) {

			List<PlayDetailList> pDlist = gson.fromJson(reader, new TypeToken<List<PlayDetailList>>() {
			}.getType());

			// 获取items列表
			musicFetcher.fetchItems(pDlist);
			// 获取users列表
			musicFetcher.fetchUsers(pDlist);
			// 获取rates列表
			musicFetcher.fetchRates(pDlist);

		} catch (IOException e) {
			logger.error("music fetcher data error!!!", e);
		}
	}

	public void fetchUsers(List<PlayDetailList> pDlist) {
		int[] index = { 1 };
		List<User> result = pDlist.parallelStream().map(playDetailList -> {

			List<String> tags = playDetailList.getThemes().parallelStream().map(theme -> {
				return theme.getDataCat();
			}).collect(Collectors.toList());

			User user = new User();
			user.setId(playDetailList.getPlayList().getUsrUrl().split("=")[1]);
			user.setName(playDetailList.getPlayList().getUsrName());
			user.setEmail("user" + user.getId() + "@gmail.com");
			user.setPassword("user" + user.getId());
			user.setPhone(getUserPhone(index[0]++));
			user.setTags(tags);

			return user;
		}).collect(Collectors.toList());
		List<User> returnList = result.stream().filter(distinctByKey(b -> b.getId())).collect(Collectors.toList());

		user2CSV(returnList);
		user2DB(returnList);
	}

	public static String getUserPhone(int index) {
		String phone = "";
		if (index < 10) {
			phone = "1827083700" + index;
		} else if (index >= 10 && index < 100) {
			phone = "182708370" + index;
		} else if (index >= 100 && index < 1000) {
			phone = "18270837" + index;
		} else if (index >= 1000 && index < 10000) {
			phone = "1827083" + index;
		} else if (index >= 10000 && index < 100000) {
			phone = "182708" + index;
		}
		return phone;
	}

	public void user2CSV(List<User> returnList) {
		// 保存最后的结果
		try (OutputStream outStream = new FileOutputStream(StoreDir + "neteasy_users.csv", false);
				Writer outWriter = new BufferedWriter(new OutputStreamWriter(outStream, Charsets.UTF_8));) {
			for (User user : returnList) {
				outWriter.write(user.toString());
			}
		} catch (IOException e) {
			logger.error("user2CSV error!!!", e);
		}
		System.out.println("user2CSV complete.");
	}

	public void user2DB(List<User> returnList) {

		String sql = "insert into users values(?,?,?,?,?,?)";
		logger.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			c.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			// 删除原有表内容
			Statement s = c.createStatement();
			s.execute("delete from users");
			for (User user : returnList) {
				ps.setLong(1, Long.parseLong(user.getId()));
				ps.setString(2, user.getName());
				ps.setString(3, user.getEmail());
				ps.setString(4, user.getPassword());
				ps.setString(5, user.getPhone());
				ps.setString(6, StringUtil.connectString(user.getTags(), ", "));
				ps.addBatch();
			}
			ps.executeBatch();
			c.commit();// 提交JDBC事务
			c.setAutoCommit(true);// 恢复JDBC事务的默认提交方式

		} catch (SQLException e) {
			logger.error("user2DB() error!", e);
		}
		System.out.println("user2DB complete.");
	}

	public void fetchRates(List<PlayDetailList> pDlist) {
		Stream<Rate> result = pDlist.parallelStream().flatMap(playDetailList -> {

			List<Rate> rates = playDetailList.getSongs().stream().map(song -> {
				Rate rate = new Rate();
				rate.setItemId(song.getSongUrl().split("=")[1]);
				rate.setUserId(playDetailList.getPlayList().getUsrUrl().split("=")[1]);
				rate.setTimestamp(dateToStamp(playDetailList.getPubDate()));
				rate.setPreference(playDetailList.getFarvirate());
				return rate;
			}).collect(Collectors.toList());

			return rates.stream();
		});
		List<Rate> returnList = result.parallel().filter(distinctByKey(b -> b.getUserId() + b.getItemId()))
				.collect(Collectors.toList());

		Float maxValue = Float
				.parseFloat(returnList.stream().min(Comparator.comparing(Rate::getPreference)).get().getPreference());
		returnList = returnList.parallelStream().map(rate -> {
			rate.setPreference(String.valueOf((Float.parseFloat(rate.getPreference()) / maxValue) * 5.0));
			return rate;
		}).collect(Collectors.toList());

		rate2CSV(returnList);
		rate2DB(returnList);
	}

	public static String dateToStamp(String s) {
		String res = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = simpleDateFormat.parse(s);
			long ts = date.getTime() / 1000;
			res = String.valueOf(ts);
		} catch (ParseException e) {
			logger.error("ParseException!!!", e);
		}
		return res;
	}

	public void rate2CSV(List<Rate> returnList) {
		// 保存最后的结果
		try (OutputStream outStream = new FileOutputStream(StoreDir + "neteasy_rates.csv", false);
				Writer outWriter = new BufferedWriter(new OutputStreamWriter(outStream, Charsets.UTF_8));) {
			for (Rate rate : returnList) {
				outWriter.write(rate.toString());
			}
		} catch (IOException e) {
			logger.error("rate2CSV error!!!", e);
		}
		System.out.println("rate2CSV complete.");
	}

	public void rate2DB(List<Rate> returnList) {

		String sql = "insert into rates values(?,?,?,?)";
		logger.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			c.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			// 删除原有表内容
			Statement s = c.createStatement();
			s.execute("delete from rates");
			for (Rate rate : returnList) {
				ps.setLong(1, Long.parseLong(rate.getUserId()));
				ps.setLong(2, Long.parseLong(rate.getItemId()));
				ps.setFloat(3, Float.parseFloat(rate.getPreference()));
				ps.setInt(4, Integer.parseInt(rate.getTimestamp()));
				ps.addBatch();
			}
			ps.executeBatch();
			c.commit();// 提交JDBC事务
			c.setAutoCommit(true);// 恢复JDBC事务的默认提交方式

		} catch (SQLException e) {
			logger.error("rate2DB() error!", e);
		}
		System.out.println("rate2DB complete.");
	}

	public void fetchItems(List<PlayDetailList> pDlist) {
		Stream<Item> result = pDlist.parallelStream().flatMap(playDetailList -> {

			List<String> tags = playDetailList.getThemes().parallelStream().map(theme -> {
				return theme.getDataCat();
			}).collect(Collectors.toList());

			List<Item> songs = playDetailList.getSongs().stream().map(song -> {
				Item item = new Item();
				item.setTags(tags);
				item.setPublished_year(playDetailList.getPubDate());
				item.setId(song.getSongUrl().split("=")[1]);
				item.setName(song.getSongName());
				return item;
			}).collect(Collectors.toList());

			return songs.stream();
		});
		List<Item> returnList = result.parallel().filter(distinctByKey(b -> b.getId())).collect(Collectors.toList());

		item2CSV(returnList);
		item2DB(returnList);
	}

	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public void item2CSV(List<Item> returnList) {
		// 保存最后的结果
		try (OutputStream outStream = new FileOutputStream(StoreDir + "neteasy_items.csv", false);
				Writer outWriter = new BufferedWriter(new OutputStreamWriter(outStream, Charsets.UTF_8));) {
			for (Item item : returnList) {
				outWriter.write(item.toString());
			}
		} catch (IOException e) {
			logger.error("item2CSV error!!!", e);
		}
		System.out.println("item2CSV complete.");
	}

	public void item2DB(List<Item> returnList) {

		String sql = "insert into items values(?,?,?,?)";
		logger.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			c.setAutoCommit(false);// 更改JDBC事务的默认提交方式
			// 删除原有表内容
			Statement s = c.createStatement();
			s.execute("delete from items");
			for (Item item : returnList) {
				ps.setLong(1, Long.parseLong(item.getId()));
				ps.setString(2, item.getName());
				ps.setString(3, item.getPublished_year());
				ps.setString(4, StringUtil.connectString(item.getTags(), ", "));
				ps.addBatch();
			}
			ps.executeBatch();
			c.commit();// 提交JDBC事务
			c.setAutoCommit(true);// 恢复JDBC事务的默认提交方式

		} catch (SQLException e) {
			logger.error("item2DB() error!", e);
		}
		System.out.println("item2DB complete.");
	}

	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/neteasydb?characterEncoding=UTF-8", "root",
					"destiny");
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException!!!", e);
		} catch (SQLException e) {
			logger.error("SQLException!!!", e);
		}
		return conn;
	}

	class Item {
		public String id;
		public String name;
		public String published_year;
		public List<String> tags = new ArrayList<>();

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPublished_year() {
			return published_year;
		}

		public void setPublished_year(String published_year) {
			this.published_year = published_year;
		}

		public List<String> getTags() {
			return tags;
		}

		public void setTags(List<String> tags) {
			this.tags = tags;
		}

		@Override
		public String toString() {
			return this.id + "," + this.name + "," + this.published_year + ","
					+ StringUtil.connectString(this.tags, "|") + "\n";
		}

	}

	class Rate {
		public String userId;
		public String itemId;
		public String preference;
		public String timestamp;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getItemId() {
			return itemId;
		}

		public void setItemId(String itemId) {
			this.itemId = itemId;
		}

		public String getPreference() {
			return preference;
		}

		public void setPreference(String preference) {
			this.preference = preference;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		@Override
		public String toString() {
			return this.userId + "," + this.itemId + "," + this.preference + "," + this.timestamp + "\n";
		}
	}

	class User {
		public String id;
		public String name;
		public String email;
		public String password;
		public String phone;
		public List<String> tags;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public List<String> getTags() {
			return tags;
		}

		public void setTags(List<String> tags) {
			this.tags = tags;
		}

		@Override
		public String toString() {
			return this.id + "," + this.name + "," + this.email + "," + this.password + "," + this.phone + ","
					+ StringUtil.connectString(this.tags, "|") + "\n";
		}
	}
}
