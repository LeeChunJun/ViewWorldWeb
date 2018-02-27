package com.licj.viewworldweb.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.licj.viewworldweb.model.User;

public class FetchUser {
	public final static String TABLE_NAME = "users";
	public final static String ID_COLUMN = "id";
	public final static String NAME_COLUMN = "name";
	public final static String EMAIL_COLUMN = "email";
	public final static String PASSWORD_COLUMN = "password";
	public final static String PHONE_COLUMN = "phone";
	public final static String TAGS_COLUMN = "tags";

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			LineNumberReader lineReader = new LineNumberReader(
					new FileReader("E:/12/ViewWorldWeb/src/main/java/resource/music_user.csv"));
			String line = "";
			User user = null;
			List<User> userList = new ArrayList<User>();
			lineReader.readLine();// 去除第一行的标题
			while ((line = lineReader.readLine()) != null) {
				if ((user = fillUsers(line)) != null) {
					userList.add(user);
				}
			}
			persist(userList);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static User fillUsers(String line) {
		User user = new User();
		String[] ur = line.split(",");

		user.setId(Long.parseLong(ur[0]));
		user.setName(ur[1]);
		user.setEmail(ur[2]);
		user.setPassword(ur[3]);
		user.setPhone(ur[4]);
		List<String> tags = new ArrayList<>();
		tags.add(String.valueOf(ur[5]));
		user.setTags(tags);

		return user;
	}
	
	public static void persist(List<User> users) {
		Connection conn = DBUtil.getJDBCConnection();
		PreparedStatement ps = null;
		String sql = "insert into " + TABLE_NAME + " ( " + ID_COLUMN + ", " + NAME_COLUMN + ", " + EMAIL_COLUMN + ", "
				+ PASSWORD_COLUMN + ", " + PHONE_COLUMN + ", " + TAGS_COLUMN + ") values (?, ?, ?, ?, ?, ?)";
		try {
			conn.setAutoCommit(false);

			ps = conn.prepareStatement(sql);

			for (User user : users) {
				ps.setLong(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getEmail());
				ps.setString(4, user.getPassword());
				ps.setString(5, user.getPhone());
				ps.setString(6, StringUtil.connectString(user.getTags(), ", "));
				ps.addBatch();
				System.out.println("insert " + user.getId() + "," + user.getName() + "," + user.getEmail() + ","
						+ user.getPassword() + "," + user.getPhone() + "," + user.getTags());
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
