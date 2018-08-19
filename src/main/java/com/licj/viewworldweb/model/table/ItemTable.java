package com.licj.viewworldweb.model.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.licj.viewworldweb.model.Item;
import com.licj.viewworldweb.utils.StringUtil;

public class ItemTable extends BaseDAO {
	private static final Logger LOGGER = Logger.getLogger(ItemTable.class);

	public static final String TABLE_NAME = "items";
	public static final String ID_COLUMN = "id";
	public static final String NAME_COLUMN = "name";
	public static final String PUBLISH_TIME_COLUMN = "published_year";
	public static final String TAGS_COLUMN = "tags";

	public ItemTable() {
		super();
	}

	public int getAttrTotal(String attr) {
		int total = 0;
		try (Connection c = getConnection(); Statement s = c.createStatement();) {

			String sql = "select count(1) from " + TABLE_NAME + " where " + NAME_COLUMN + " like '%" + attr + "%' or "
					+ TAGS_COLUMN + " like '%" + attr + "%'";
			LOGGER.info("sql->" + sql);

			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				total = rs.getInt(1);
			}

			LOGGER.info("total->" + total);

		} catch (SQLException e) {
			LOGGER.error("getAttrTotal() error!", e);
		}
		return total;
	}

	public int getTotal() {
		int total = 0;
		try (Connection c = getConnection(); Statement s = c.createStatement();) {

			String sql = "select count(1) from " + TABLE_NAME;
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

	public Item constructItemFromResultSet(ResultSet rs) {
		try {
			Item item = new Item();
			item.setId(rs.getLong(ID_COLUMN));
			item.setName(rs.getString(NAME_COLUMN));
			item.setPublish_time(rs.getString(PUBLISH_TIME_COLUMN));
			String tags = rs.getString(TAGS_COLUMN);
			if (tags != null) {
				item.setTags(Arrays.asList(tags.split(",")));
			}
			return item;
		} catch (SQLException e) {
			LOGGER.error("constructItemFromResultSet() error!", e);
		}
		return null;
	}

	public void add(Item item) {

		String sql = "insert into " + TABLE_NAME + " values(?,?,?,?)";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setLong(1, item.getId());
			ps.setString(2, item.getName());
			ps.setString(3, item.getPublish_time());
			ps.setString(4, StringUtil.connectString(item.getTags(), ", "));

			ps.execute();
		} catch (SQLException e) {
			LOGGER.error("add() error!", e);
		}
	}

	public void delete(long id) {
		try (Connection c = getConnection(); Statement s = c.createStatement();) {

			String sql = "delete from " + TABLE_NAME + " where " + ID_COLUMN + " = " + id;
			LOGGER.info("sql->" + sql);

			s.execute(sql);
		} catch (SQLException e) {
			LOGGER.error("delete() error!", e);
		}
	}

	public void update(Item item) {

		String sql = "update " + TABLE_NAME + " set " + ID_COLUMN + " =?, " + NAME_COLUMN + " =?, "
				+ PUBLISH_TIME_COLUMN + " =?, " + TAGS_COLUMN + " =? where " + ID_COLUMN + " =?";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setLong(1, item.getId());
			ps.setString(2, item.getName());
			ps.setString(3, item.getPublish_time());
			ps.setString(4, StringUtil.connectString(item.getTags(), ", "));
			ps.setLong(5, item.getId());

			ps.execute();
		} catch (SQLException e) {
			LOGGER.error("update() error!", e);
		}
	}

	public Item get(long id) {
		Item item = null;
		try (Connection c = getConnection(); Statement s = c.createStatement();) {

			String sql = "select * from " + TABLE_NAME + " where " + ID_COLUMN + " = " + id;
			LOGGER.info("sql->" + sql);

			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				item = constructItemFromResultSet(rs);
			}
		} catch (SQLException e) {
			LOGGER.error("get() error!", e);
		}
		return item;
	}

	public List<Item> getItems(Collection<String> itemIDs) {
		List<Item> items = new ArrayList<Item>();
		String itemIDString = StringUtil.connectString(itemIDs, ", ");

		String sql = "select * from " + TABLE_NAME + " where " + ID_COLUMN + " in ( " + itemIDString + " )";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Item item = constructItemFromResultSet(rs);
				if (item != null) {
					items.add(item);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("getItems() error!", e);
		}
		return items;
	}

	public Map<String, Item> getItemMap(Collection<String> itemIDs) {
		Map<String, Item> items = new HashMap<String, Item>();
		String itemIDString = StringUtil.connectString(itemIDs, ", ");

		String sql = "select * from " + TABLE_NAME + " where " + ID_COLUMN + " in ( " + itemIDString + " )";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Item item = constructItemFromResultSet(rs);
				if (item != null) {
					items.put(String.valueOf(item.getId()), item);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("getItemMap() error!", e);
		}
		return items;
	}

	public List<Item> list() {
		return list(0, Short.MAX_VALUE);
	}

	public List<Item> list(int start, int count) {
		List<Item> items = new ArrayList<>();

		String sql = "select * from " + TABLE_NAME + " limit ?,? ";
		LOGGER.info("sql->" + sql);
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
			LOGGER.error("list() error!", e);
		}
		return items;
	}

	public List<Item> listByAttr(int start, int count, String attr) {
		List<Item> items = new ArrayList<>();

		String sql = "select * from " + TABLE_NAME + " where " + NAME_COLUMN + " like '%" + attr + "%' or "
				+ TAGS_COLUMN + " like '%" + attr + "%' limit ?,? ";
		LOGGER.info("sql->" + sql);
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
			LOGGER.error("listByAttr() error!", e);
		}
		return items;
	}

	public List<Long> getMostSimilarItems(String itemID) {
		// 1、获取与当前传入item中的tag有关联的itemIDs
		List<String> items = new ArrayList<>();
		String sql = "select id from items where tags in (select tags from items where id = '" + itemID + "')";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				items.add(String.valueOf(rs.getLong(1)));
			}

		} catch (SQLException e) {
			LOGGER.error("getMostSimilarItems() error!", e);
		}
		// 2、根据itemIDs找到items
		List<Item> itemAlls = getItems(items);
		// 3、获取当前传入itemID的item
		Item currentItem = get(Long.parseLong(itemID));
		// 4、比较两个item的相似度
		Map<Long, Double> mapAlls = new HashMap<>();
		itemAlls.stream().forEach(item -> {
			mapAlls.put(item.getId(), currentItem.relevance(item));
		});
		Map<Long, Double> result = mapAlls.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

		List<Long> resultList = new ArrayList<>(result.keySet());
		return resultList;
	}

}
