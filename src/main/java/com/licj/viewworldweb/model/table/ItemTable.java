package com.licj.viewworldweb.model.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.licj.viewworldweb.model.Item;
import com.licj.viewworldweb.utils.L;
import com.licj.viewworldweb.utils.StringUtil;

public class ItemTable extends BaseDAO {
	public static final String TABLE_NAME = "items";
	public static final String ID_COLUMN = "id";
	public static final String SONG_NAME_COLUMN = "song_name";
	public static final String SINGER_NAME_COLUMN = "singer_name";
	public static final String PIC_URL_COLUMN = "pic_url";
	public static final String PUBLISH_TIME_COLUMN = "publish_time";
	public static final String LYRIC_COLUMN = "lyric";
	public static final String TAGS_COLUMN = "tags";

	public ItemTable() {
		super();
	}
	
	public int getAttrTotal(String attr){
		int total = 0;
		try (Connection c = getConnection(); Statement s = c.createStatement();) {

			String sql = "select count(1) from " + TABLE_NAME + " where " + SONG_NAME_COLUMN + " like '%"+ attr +"%' or " + SINGER_NAME_COLUMN + " like '%"+ attr +"%' or " + TAGS_COLUMN + " like '%"+ attr +"%'";
			L.i("sql->" + sql);

			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}

			L.i("total->" + total);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	public int getTotal() {
		int total = 0;
		try (Connection c = getConnection(); Statement s = c.createStatement();) {
			
			long current_timestamp = new Date().getTime();
			long old_timestamp = 0;
			String sql_pre_timestamp = "select timestamp from line_total where id = 1";
			String sql_pre_sum = "select sum from line_total where id = 1";
			ResultSet rs_pre_timestamp = s.executeQuery(sql_pre_timestamp);
			if (rs_pre_timestamp.next()) {
				old_timestamp = rs_pre_timestamp.getLong(1);
				ResultSet rs_pre_sum = s.executeQuery(sql_pre_sum);
				if((current_timestamp - old_timestamp) <= 86400000){
					rs_pre_sum.next();
					total = rs_pre_sum.getInt(1);
					return total;
				}
			}

			String sql = "select count(1) from " + TABLE_NAME;
			L.i("sql->" + sql);
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}
			L.i("total->" + total);

			String sql_aft_sum = "insert into line_total values(1,'item'," + current_timestamp + "," + total + ")";
			L.i("sql_aft_sum->" + sql_aft_sum);
			s.execute(sql_aft_sum);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return total;
	}

	public Item constructItemFromResultSet(ResultSet rs) {
		try {
			Item item = new Item();
			item.setId(rs.getLong(ID_COLUMN));
			item.setSong_name(rs.getString(SONG_NAME_COLUMN));
			item.setSinger_name(rs.getString(SINGER_NAME_COLUMN));
			item.setPic_url(rs.getString(PIC_URL_COLUMN));
			item.setPublish_time(rs.getString(PUBLISH_TIME_COLUMN));
			String lyric = rs.getString(LYRIC_COLUMN);
			if (lyric != null) {
				item.setLyric(rs.getString(LYRIC_COLUMN));
			}
			String tags = rs.getString(TAGS_COLUMN);
			if (tags != null) {
				item.setTags(Arrays.asList(tags.split(",")));
			}
			return item;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void add(Item item) {

		String sql = "insert into " + TABLE_NAME + " values(?,?,?,?,?,?,?)";
		L.i("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setLong(1, item.getId());
			ps.setString(2, item.getSong_name());
			ps.setString(3, item.getSinger_name());
			ps.setString(4, item.getPic_url());
			ps.setString(5, item.getPublish_time());
			ps.setString(6, item.getLyric());
			ps.setString(7, StringUtil.connectString(item.getTags(), ", "));

			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(long id) {
		try (Connection c = getConnection(); Statement s = c.createStatement();) {

			String sql = "delete from " + TABLE_NAME + " where " + ID_COLUMN + " = " + id;
			L.i("sql->" + sql);

			s.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(Item item) {

		String sql = "update " + TABLE_NAME + " set " + ID_COLUMN + " =?, " + SONG_NAME_COLUMN + " =?, " + SINGER_NAME_COLUMN
				+ " =?, " + PIC_URL_COLUMN + " =?, " + PUBLISH_TIME_COLUMN + " =?, " + LYRIC_COLUMN + " =?, " + TAGS_COLUMN + " =? where " + ID_COLUMN + " =?";
		L.i("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setLong(1, item.getId());
			ps.setString(2, item.getSong_name());
			ps.setString(3, item.getSinger_name());
			ps.setString(4, item.getPic_url());
			ps.setString(5, item.getPublish_time());
			ps.setString(6, item.getLyric());
			ps.setString(7, StringUtil.connectString(item.getTags(), ", "));
			ps.setLong(8, item.getId());

			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Item get(long id) {
		Item item = null;
		try (Connection c = getConnection(); Statement s = c.createStatement();) {

			String sql = "select * from " + TABLE_NAME + " where " + ID_COLUMN + " = " + id;
			L.i("sql->" + sql);

			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				item = constructItemFromResultSet(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return item;
	}

	public List<Item> getItems(Collection<String> itemIDs) {
		List<Item> items = new ArrayList<Item>();
		String itemIDString = StringUtil.connectString(itemIDs, ", ");

		String sql = "select * from " + TABLE_NAME + " where " + ID_COLUMN + " in ( " + itemIDString + " )";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Item item = constructItemFromResultSet(rs);
				if (item != null) {
					items.add(item);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	public Map<String, Item> getItemMap(Collection<String> itemIDs) {
		Map<String, Item> items = new HashMap<String, Item>();
		String itemIDString = StringUtil.connectString(itemIDs, ", ");

		String sql = "select * from " + TABLE_NAME + " where " + ID_COLUMN + " in ( " + itemIDString + " )";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Item item = constructItemFromResultSet(rs);
				if (item != null) {
					items.put(String.valueOf(item.getId()), item);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	public List<Item> list() {
		return list(0, Short.MAX_VALUE);
	}

	public List<Item> list(int start, int count) {
		List<Item> items = new ArrayList<>();

		String sql = "select * from " + TABLE_NAME + " order by " + ID_COLUMN + " asc limit ?,? ";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setInt(1, start);
			ps.setInt(2, count);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Item item = constructItemFromResultSet(rs);
				if (item != null) {
					items.add(item);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}
	
	public List<Item> listByAttr(int start, int count, String attr) {
		List<Item> items = new ArrayList<>();

		String sql = "select * from " + TABLE_NAME + " where " + SONG_NAME_COLUMN + " like '%"+ attr +"%' or " + SINGER_NAME_COLUMN + " like '%"+ attr +"%' or " + TAGS_COLUMN + " like '%"+ attr +"%' order by " + ID_COLUMN + " asc limit ?,? ";
		L.i("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setInt(1, start);
			ps.setInt(2, count);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Item item = constructItemFromResultSet(rs);
				if (item != null) {
					items.add(item);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

}
