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

import com.licj.viewworldweb.model.Item;
import com.licj.viewworldweb.model.Rate;
import com.licj.viewworldweb.model.table.ItemTable;

public class FetchRate {
	public final static String TABLE_NAME = "rates";
	public final static String USER_ID_COLUMN = "userID";
	public final static String ITEM_ID_COLUMN = "itemID";
	public final static String RATING = "preference";
	public final static String TIMESTAMP = "timestamp";
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			LineNumberReader lineReader = new LineNumberReader(
					new FileReader("E:/12/ViewWorldWeb/src/main/java/resource/music_rate.csv"));
			String line = "";
			List<Rate> rateList = new ArrayList<Rate>();
			lineReader.readLine();// 去除第一行的标题
			while ((line = lineReader.readLine()) != null) {
				rateList.add(fillRate(line));
			}
			persist(rateList);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Rate fillRate(String line) {

		String[] ra = line.split(",");
		Rate rate = new Rate();
		rate.setUserId(Long.parseLong(ra[0]));
		rate.setItemId(Long.parseLong(ra[1]));
		rate.setPreference(Float.parseFloat(ra[2]));
		rate.setTimestamp(Integer.parseInt(ra[3]));
		return rate;
	}

	public static void persist(List<Rate> rates) {
		Connection conn = DBUtil.getJDBCConnection();
		PreparedStatement ps = null;
		String sql = "insert into " + TABLE_NAME + " ( " + USER_ID_COLUMN + ", " + ITEM_ID_COLUMN + ", " + RATING + ", "
				+ TIMESTAMP + ") values (?, ?, ?, ?)";
		try {
			conn.setAutoCommit(false);

			ps = conn.prepareStatement(sql);

			for (Rate rate : rates) {
				Item item = new ItemTable().get(rate.getItemId());
				if(item == null){
					continue;
				}
				ps.setLong(1, rate.getUserId());
				ps.setLong(2, rate.getItemId());
				ps.setFloat(3, rate.getPreference());
				ps.setInt(4, rate.getTimestamp());
				ps.addBatch();
				System.out.println("insert " + rate.getUserId() + "," + rate.getItemId() + "," + rate.getPreference()
						+ "," + rate.getTimestamp());

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
