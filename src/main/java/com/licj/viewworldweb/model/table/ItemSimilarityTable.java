package com.licj.viewworldweb.model.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.licj.viewworldweb.model.ItemSimilarity;

public class ItemSimilarityTable extends BaseDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemSimilarityTable.class);

	public final static String TABLE_NAME = "item_similarities";
	public final static String ITEM_ID_1 = "itemID1";
	public final static String ITEM_ID_2 = "itemID2";
	public final static String SIMILARITY = "similarity";// item之间的relevance值

	public ItemSimilarityTable() {
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

	public void add(ItemSimilarity itemSimilarity) {

		String sql = "insert into " + TABLE_NAME + " values(?,?,?)";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setLong(1, itemSimilarity.getItemID1());
			ps.setLong(2, itemSimilarity.getItemID2());
			ps.setDouble(3, itemSimilarity.getSimilarity());

			ps.execute();
		} catch (SQLException e) {
			LOGGER.error("add() error!", e);
		}
	}

	public void delete(long id1, long id2) {
		try (Connection c = getConnection(); Statement s = c.createStatement();) {

			String sql = "delete from " + TABLE_NAME + " where " + ITEM_ID_1 + " = " + id1 + " and " + ITEM_ID_2 + " = "
					+ id2;
			LOGGER.info("sql->" + sql);

			s.execute(sql);
		} catch (SQLException e) {
			LOGGER.error("delete() error!", e);
		}
	}

	public void update(ItemSimilarity itemSimilarity) {

		String sql = "update " + TABLE_NAME + " set " + ITEM_ID_1 + " =?, " + ITEM_ID_2 + " =?, " + SIMILARITY
				+ " =? where " + ITEM_ID_1 + " =? and " + ITEM_ID_2 + " =?";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setLong(1, itemSimilarity.getItemID1());
			ps.setLong(2, itemSimilarity.getItemID2());
			ps.setDouble(3, itemSimilarity.getSimilarity());
			ps.setLong(4, itemSimilarity.getItemID1());
			ps.setLong(5, itemSimilarity.getItemID2());

			ps.execute();
		} catch (SQLException e) {
			LOGGER.error("update() error!", e);
		}
	}

	public GenericItemSimilarity.ItemItemSimilarity constructItemSimilarityFromResultSet(ResultSet rs) {
		try {
			long item1 = rs.getLong(ITEM_ID_1);
			long item2 = rs.getLong(ITEM_ID_2);
			double rel = rs.getDouble(SIMILARITY);
			GenericItemSimilarity.ItemItemSimilarity similarity = new GenericItemSimilarity.ItemItemSimilarity(item1,
					item2, rel);
			return similarity;
		} catch (SQLException e) {
			LOGGER.error("constructItemSimilarityFromResultSet() error!", e);
		}
		return null;
	}

	public List<GenericItemSimilarity.ItemItemSimilarity> getAllItemSimilarities() {
		List<GenericItemSimilarity.ItemItemSimilarity> similarities = new ArrayList<GenericItemSimilarity.ItemItemSimilarity>();

		String sql = "select * from " + TABLE_NAME;
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				GenericItemSimilarity.ItemItemSimilarity similarity = constructItemSimilarityFromResultSet(rs);
				if (similarity != null) {
					similarities.add(similarity);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return similarities;
	}

}
