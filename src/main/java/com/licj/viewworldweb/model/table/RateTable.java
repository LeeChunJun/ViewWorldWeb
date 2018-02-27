package com.licj.viewworldweb.model.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.licj.viewworldweb.model.Item;
import com.licj.viewworldweb.model.ItemList;
import com.licj.viewworldweb.model.Rate;
import com.licj.viewworldweb.utils.L;

public class RateTable extends BaseDAO {

	public final static String TABLE_NAME = "rates";
	public final static String USER_ID_COLUMN = "userID";
	public final static String ITEM_ID_COLUMN = "itemID";
	public final static String RATING = "preference";
	public final static String TIMESTAMP = "timestamp";

	public RateTable() {
		super();
	}

	public int getTotal() {
		int total = 0;
		try (Connection c = getConnection(); Statement s = c.createStatement();) {

			String sql = "select count(*) from " + TABLE_NAME;
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

	public void constructItemsFromResultSet(ResultSet rs, ItemList items) {
		try {
			Item item = new Item();
			item.setId(rs.getLong(ItemTable.ID_COLUMN));
			item.setSong_name(rs.getString(ItemTable.SONG_NAME_COLUMN));
			item.setSinger_name(rs.getString(ItemTable.SINGER_NAME_COLUMN));
			item.setPic_url(rs.getString(ItemTable.PIC_URL_COLUMN));
			item.setPublish_time(rs.getString(ItemTable.PUBLISH_TIME_COLUMN));
			String lyric = rs.getString(ItemTable.LYRIC_COLUMN);
			if (lyric != null) {
				item.setLyric(rs.getString(ItemTable.LYRIC_COLUMN));
			}
			String tags = rs.getString(ItemTable.TAGS_COLUMN);
			if (tags != null) {
				item.setTags(Arrays.asList(tags.split(",")));
			}
			Float score = rs.getFloat(RATING);
			items.add(item, score);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void add(Rate rate) {

		String sql = "insert into " + TABLE_NAME + " values(?,?,?,?)";
		L.i("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setLong(1, rate.getUserId());
			ps.setLong(2, rate.getItemId());
			ps.setFloat(3, rate.getPreference());
			ps.setInt(4, rate.getTimestamp());

			ps.execute();
		} catch (SQLException e) {
			L.e("sql->" + sql);
			e.printStackTrace();
		}
	}

	public void delete(long userId, long itemId) {
		try (Connection c = getConnection(); Statement s = c.createStatement();) {

			String sql = "delete from " + TABLE_NAME + " where " + USER_ID_COLUMN + " = " + userId + " and "
					+ ITEM_ID_COLUMN + " = " + itemId;
			L.i("sql->" + sql);

			s.execute(sql);
		} catch (SQLException e) {
			L.e("sql->delete from " + TABLE_NAME);
			e.printStackTrace();
		}
	}

	public void update(Rate rate) {

		String sql = "update " + TABLE_NAME + " set " + USER_ID_COLUMN + " =?, " + ITEM_ID_COLUMN + " =?, " + RATING
				+ " =?, " + TIMESTAMP + " =? where " + USER_ID_COLUMN + " =?" + " and " + ITEM_ID_COLUMN + "=?";
		L.i("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setLong(1, rate.getUserId());
			ps.setLong(2, rate.getItemId());
			ps.setFloat(3, rate.getPreference());
			ps.setInt(4, rate.getTimestamp());
			ps.setLong(5, rate.getUserId());
			ps.setLong(6, rate.getItemId());

			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ItemList getItemsByUserID(String userID) {

		String sql = "select * from " + TABLE_NAME + " r, " + ItemTable.TABLE_NAME + " i" + " where " + "r."
				+ ITEM_ID_COLUMN + " = i." + ItemTable.ID_COLUMN + " and " + "r." + USER_ID_COLUMN + " = " + userID;
		L.i("sql->" + sql);
		ItemList items = new ItemList();
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				constructItemsFromResultSet(rs, items);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}
	
	public float getAVGPreferByItemID(String itemID){
		float average = 0.0f;
		
		String sql = "select AVG(" + RATING + ") from " + TABLE_NAME + " where " + ITEM_ID_COLUMN + " =?";
		L.i("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			
			ps.setString(1, itemID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				average = rs.getFloat(1);
			}

			L.i("average->" + average);
		} catch (SQLException e) {
			L.e("sql->" + sql);
			e.printStackTrace();
		}
		
		return average;
	}
	
	public int getRatesNumByItemID(String itemID){
		int num = 0;
		
		String sql = "select count(*) from " + TABLE_NAME + " where " + ITEM_ID_COLUMN + " =?";
		L.i("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			
			ps.setString(1, itemID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				num = rs.getInt(1);
			}

			L.i("average->" + num);
		} catch (SQLException e) {
			L.e("sql->" + sql);
			e.printStackTrace();
		}
		
		return num;
	}
	
	public boolean hasRatedByUserID(String userID){
		boolean hasRated = true;
		
		String sql = "select count(*) from rates where userID = '" + userID + "'";
		L.i("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if(rs.getInt(1) == 0){
					hasRated = false;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return hasRated;
	}
	
	public List<Long> getMostRateItems(){
		List<Long> items = new ArrayList<>();
		
		String sql = "select itemID,count(userID) num from rates group by itemID order by num desc";
		L.i("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				items.add(rs.getLong(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return items;
	}

}
