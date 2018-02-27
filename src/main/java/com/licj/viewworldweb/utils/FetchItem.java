package com.licj.viewworldweb.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.licj.viewworldweb.model.Item;

public class FetchItem {
	public static final String output = "E:/12/ViewWorldWeb/src/main/java/resource/";
	
	public static final String TABLE_NAME = "items";
	public static final String ID_COLUMN = "id";
	public static final String SONG_NAME_COLUMN = "song_name";
	public static final String SINGER_NAME_COLUMN = "singer_name";
	public static final String PIC_URL_COLUMN = "pic_url";
	public static final String PUBLISH_TIME_COLUMN = "publish_time";
	public static final String LYRIC_COLUMN = "lyric";
	public static final String TAGS_COLUMN = "tags";

	public static void main(String[] args) throws IOException {
		
//		try {
//			LineNumberReader lineReader = new LineNumberReader(
//					new FileReader("E:/12/ViewWorldWeb/src/main/java/resource/music_item.csv"));
//			String line = "";
//			List<Item> itemList = new ArrayList<Item>();
//			lineReader.readLine();// 去除第一行的标题
//			while ((line = lineReader.readLine()) != null) {
//				itemList.add(fillItem(line));
//			}
//			persist(itemList);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try (Stream<String> stream = Files.lines(Paths.get(output + "music_item.csv"))) {
			List<Item> items = stream.skip(1).map(line -> {
				Item item = null;
				try {
					item = fillItem(line);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return item;
			}).filter(item -> item != null).collect(Collectors.toList());
			persist(items);
		}
		
	}

	private static Item fillItem(String line) throws UnsupportedEncodingException {
		Item item = new Item();
		String[] mu = line.split(",");

		if(mu.length != 7 || mu[0] == null || mu[0] == ""){
			return null;			
		} else {
			System.out.println(mu[0]+"|"+mu.length);
		}
		
		item.setId(Long.parseLong(mu[0]));
		item.setSong_name(mu[1]);
		item.setSinger_name(mu[2]);
		item.setPic_url(mu[3]);
		item.setPublish_time(mu[4]);
		if (mu[5] != null || mu[5] != "") {
			mu[5] = new String(Base64.getDecoder().decode(mu[5]), "UTF-8");
			item.setLyric(mu[5]);
		} else {
			item.setLyric("");
		}
		String tags = mu[6];
		if (tags != null) {
			item.setTags(Arrays.asList(tags.split(":")));
		}
		return item;
	}

	private static void persist(List<Item> items) {

		Connection conn = DBUtil.getJDBCConnection();
		PreparedStatement ps = null;
		String sql = "insert into " + TABLE_NAME + " ( " + ID_COLUMN + ", " + SONG_NAME_COLUMN + ", "
				+ SINGER_NAME_COLUMN + ", " + PIC_URL_COLUMN + ", " + PUBLISH_TIME_COLUMN + ", " + LYRIC_COLUMN + ", "
				+ TAGS_COLUMN + ") values (?, ?, ?, ?, ?, ?, ?)";
		try {
			conn.setAutoCommit(false);

			ps = conn.prepareStatement(sql);

			for (Item item : items) {
				ps.setLong(1, item.getId());
				ps.setString(2, item.getSong_name());
				ps.setString(3, item.getSinger_name());
				ps.setString(4, item.getPic_url());
				ps.setString(5, item.getPublish_time());
				ps.setString(6, item.getLyric());
				ps.setString(7, StringUtil.connectString(item.getTags(), ", "));
				ps.addBatch();
				System.out.println("insert " + item.getId());
			}

			ps.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
