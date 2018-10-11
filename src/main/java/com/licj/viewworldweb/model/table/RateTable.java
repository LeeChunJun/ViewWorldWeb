package com.licj.viewworldweb.model.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.licj.viewworldweb.model.Item;
import com.licj.viewworldweb.model.ItemList;
import com.licj.viewworldweb.model.Rate;

public class RateTable extends BaseDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(RateTable.class);

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
			LOGGER.info("sql->" + sql);

			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}

			LOGGER.info("total->" + total);

		} catch (SQLException e) {
			LOGGER.error("getTotal() error!", e);
		}
		return total;
	}

	public void constructItemsFromResultSet(ResultSet rs, ItemList items) {
		try {
			Item item = new Item();
			item.setId(rs.getLong(ItemTable.ID_COLUMN));
			item.setName(rs.getString(ItemTable.NAME_COLUMN));
			item.setPublish_time(rs.getString(ItemTable.PUBLISH_TIME_COLUMN));
			String tags = rs.getString(ItemTable.TAGS_COLUMN);
			if (tags != null) {
				item.setTags(Arrays.asList(tags.split(",")));
			}
			Float score = rs.getFloat(RATING);
			items.add(item, score);
		} catch (SQLException e) {
			LOGGER.error("constructItemsFromResultSet() error!", e);
		}
	}

	public void add(Rate rate) {

		String sql = "insert into " + TABLE_NAME + " values(?,?,?,?)";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setLong(1, rate.getUserId());
			ps.setLong(2, rate.getItemId());
			ps.setFloat(3, rate.getPreference());
			ps.setInt(4, rate.getTimestamp());

			ps.execute();
		} catch (SQLException e) {
			LOGGER.error("add() error!", e);
		}
	}

	public void delete(long userId, long itemId) {
		try (Connection c = getConnection(); Statement s = c.createStatement();) {

			String sql = "delete from " + TABLE_NAME + " where " + USER_ID_COLUMN + " = " + userId + " and "
					+ ITEM_ID_COLUMN + " = " + itemId;
			LOGGER.info("sql->" + sql);

			s.execute(sql);
		} catch (SQLException e) {
			LOGGER.error("delete() error!", e);
		}
	}

	public void update(Rate rate) {

		String sql = "update " + TABLE_NAME + " set " + USER_ID_COLUMN + " =?, " + ITEM_ID_COLUMN + " =?, " + RATING
				+ " =?, " + TIMESTAMP + " =? where " + USER_ID_COLUMN + " =?" + " and " + ITEM_ID_COLUMN + "=?";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setLong(1, rate.getUserId());
			ps.setLong(2, rate.getItemId());
			ps.setFloat(3, rate.getPreference());
			ps.setInt(4, rate.getTimestamp());
			ps.setLong(5, rate.getUserId());
			ps.setLong(6, rate.getItemId());

			ps.execute();
		} catch (SQLException e) {
			LOGGER.error("update() error!", e);
		}
	}

	public ItemList getItemsByUserID(String userID) {

		String sql = "select * from " + TABLE_NAME + " r, " + ItemTable.TABLE_NAME + " i" + " where " + "r."
				+ ITEM_ID_COLUMN + " = i." + ItemTable.ID_COLUMN + " and " + "r." + USER_ID_COLUMN + " = " + userID;
		LOGGER.info("sql->" + sql);
		ItemList items = new ItemList();
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				constructItemsFromResultSet(rs, items);
			}
		} catch (SQLException e) {
			LOGGER.error("getItemsByUserID() error!", e);
		}
		return items;
	}
	
	@Deprecated
	public float getAVGPreferByItemID(String itemID){
		float average = 0.0f;
		
		String sql = "select AVG(" + RATING + ") from " + TABLE_NAME + " where " + ITEM_ID_COLUMN + " =?";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			
			ps.setString(1, itemID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				average = rs.getFloat(1);
			}

			LOGGER.info("average->" + average);
		} catch (SQLException e) {
			LOGGER.error("getAVGPreferByItemID() error!", e);
		}
		
		return average;
	}
	
	@Deprecated
	public int getRatesNumByItemID(String itemID){
		int num = 0;
		
		String sql = "select count(*) from " + TABLE_NAME + " where " + ITEM_ID_COLUMN + " =?";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			
			ps.setString(1, itemID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				num = rs.getInt(1);
			}

			LOGGER.info("num->" + num);
		} catch (SQLException e) {
			LOGGER.error("getRatesNumByItemID() error!", e);
		}
		
		return num;
	}
	
	public boolean hasRatedByUserID(String userID){
		boolean hasRated = true;
		
		String sql = "select count(*) from rates where userID = '" + userID + "'";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if(rs.getInt(1) == 0){
					hasRated = false;
				}
			}

		} catch (SQLException e) {
			LOGGER.error("hasRatedByUserID() error!", e);
		}
		
		return hasRated;
	}
	
	public List<Long> getMostRateItems(){
		List<Long> items = new ArrayList<>();
		
		String sql = "select itemID,count(userID) num from rates group by itemID order by num desc";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				items.add(rs.getLong(1));
			}

		} catch (SQLException e) {
			LOGGER.error("getMostRateItems() error!", e);
		}
		
		return items;
	}

}
