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

import com.licj.viewworldweb.model.User;
import com.licj.viewworldweb.utils.*;

public class UserTable extends BaseDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserTable.class);
	
	public final static String TABLE_NAME = "users";
	public final static String ID_COLUMN = "id";
	public final static String NAME_COLUMN = "name";
	public final static String EMAIL_COLUMN = "email";
	public final static String PASSWORD_COLUMN = "password";
	public final static String PHONE_COLUMN = "phone";
	public final static String TAGS_COLUMN = "tags";

	public UserTable() {
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

	public User constructUserFromResultSet(ResultSet rs) {
		try {
			User user = new User();
			user.setId(rs.getLong(ID_COLUMN));
			user.setName(rs.getString(NAME_COLUMN));
			user.setEmail(rs.getString(EMAIL_COLUMN));
			user.setPassword(rs.getString(PASSWORD_COLUMN));
			user.setPhone(rs.getString(PHONE_COLUMN));
			String tags = rs.getString(TAGS_COLUMN);
			if (tags != null) {
				user.setTags(Arrays.asList(tags.split(",")));
			}
			return user;
		} catch (SQLException e) {
			LOGGER.error("constructUserFromResultSet() error!", e);
		}
		return null;
	}

	public void add(User user) {

		String sql = "insert into " + TABLE_NAME + " values(?,?,?,?,?,?)";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			ps.setLong(1, user.getId());
			ps.setString(2, user.getName());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getPassword());
			ps.setString(5, user.getPhone());
			ps.setString(6, StringUtil.connectString(user.getTags(), ", "));
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

	public void update(User user) {

		String sql = "update " + TABLE_NAME + " set " + ID_COLUMN + " =?, " + NAME_COLUMN + " =?, " + EMAIL_COLUMN
				+ " =?, " + PASSWORD_COLUMN + " =?, " + PHONE_COLUMN + " =?, " + TAGS_COLUMN + " =? where " + ID_COLUMN
				+ " =?";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setLong(1, user.getId());
			ps.setString(2, user.getName());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getPassword());
			ps.setString(5, user.getPhone());
			ps.setString(6, StringUtil.connectString(user.getTags(), ", "));
			ps.setLong(7, user.getId());

			ps.execute();
		} catch (SQLException e) {
			LOGGER.error("update() error!", e);
		}
	}

	public User getUserByID(String userID) {

		String sql = "select * from " + TABLE_NAME + " where " + ID_COLUMN + " = " + userID;
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				User user = constructUserFromResultSet(rs);
				return user;
			}
		} catch (SQLException e) {
			LOGGER.error("getUserByID() error!", e);
		}
		return null;
	}
	
	public boolean hasUserByEmail(String email) {

		String sql = "select " + ID_COLUMN + " from " + TABLE_NAME + " where " + EMAIL_COLUMN + " =  '" + email + "'";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			LOGGER.error("hasUserByEmail() error!", e);
		}
		return false;
	}
	
	public boolean hasUserByPhone(String phone) {

		String sql = "select " + ID_COLUMN + " from " + TABLE_NAME + " where " + PHONE_COLUMN + " =  '" + phone + "'";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			LOGGER.error("hasUserByPhone() error!", e);
		}
		return false;
	}
	
	public String getPasswordByAccount(String account, boolean isPhone) {
		String password = "";
		String sql = "";

		if(isPhone){
			sql = "select " + PASSWORD_COLUMN + " from " + TABLE_NAME + " where " + PHONE_COLUMN + " =  '" + account + "'";
		} else {
			sql = "select " + PASSWORD_COLUMN + " from " + TABLE_NAME + " where " + EMAIL_COLUMN + " =  '" + account + "'";
		}
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString(PASSWORD_COLUMN);
			}
		} catch (SQLException e) {
			LOGGER.error("getPasswordByAccount() error!", e);
		}
		return password;
	}
	
	public String getIDByAccount(String account, boolean isPhone) {
		String userID = "";
		String sql = "";

		if(isPhone){
			sql = "select " + ID_COLUMN + " from " + TABLE_NAME + " where " + PHONE_COLUMN + " =  '" + account + "'";
		} else {
			sql = "select " + ID_COLUMN + " from " + TABLE_NAME + " where " + EMAIL_COLUMN + " =  '" + account + "'";
		}
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString(ID_COLUMN);
			}
		} catch (SQLException e) {
			LOGGER.error("getIDByAccount() error!", e);
		}
		return userID;
	}

	public User getUserByEmail(String email) {

		String sql = "select * from " + TABLE_NAME + " where " + EMAIL_COLUMN + " =  '" + email + "'";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				User user = constructUserFromResultSet(rs);
				return user;
			}
		} catch (SQLException e) {
			LOGGER.error("getUserByEmail() error!", e);
		}
		return null;
	}
	
	public User getUserByPhone(String phone) {

		String sql = "select * from " + TABLE_NAME + " where " + PHONE_COLUMN + " =  '" + phone + "'";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				User user = constructUserFromResultSet(rs);
				return user;
			}
		} catch (SQLException e) {
			LOGGER.error("getUserByPhone() error!", e);
		}
		return null;
	}

	public List<User> list() {
		return list(0, Short.MAX_VALUE);
	}

	public List<User> list(int start, int count) {
		List<User> users = new ArrayList<>();

		String sql = "select * from " + TABLE_NAME + " limit ?,? ";
		LOGGER.info("sql->" + sql);
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

			ps.setInt(1, start);
			ps.setInt(2, count);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				User user = constructUserFromResultSet(rs);
				if (user != null) {
					users.add(user);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("list() error!", e);
		}
		return users;
	}

}
