package com.licj.viewworldweb.recommender;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;


public class ItemJDBCDataModel implements ItemDataModel{
	public final static String PERFERENCETABLE = "rates";
	public final static String USERID_COLUMN = "userID";
	public final static String ITEMID_COLUMN = "itemID";
	public final static String PERFERENCE_COLUMN = "preference";
	public final static String TIMESTAMP_COLUMN = "timestamp";
	
	//驱动程序名
	public final static String driver = "com.mysql.jdbc.Driver";
    //URL指向要访问的数据库名mydata
	public final static String url = "jdbc:mysql://localhost:3306/music_all_db";
    //MySQL配置时的用户名
	public final static String user = "root";
    //MySQL配置时的密码
	public final static String password = "destiny";

	public BasicDataSource dataSource = new BasicDataSource();
	public JDBCDataModel dataModel = null;

	public ItemJDBCDataModel() {
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		dataSource.setTestWhileIdle(true);
		dataSource.setMaxActive(20);
		dataSource.setInitialSize(0);
		dataSource.setMaxIdle(20);
		dataSource.setMinIdle(0);
		dataSource.setMaxWait(15000);
	}
	
	public JDBCDataModel getJDBCDataModel() {
		if (dataModel == null) {
			dataModel = new MySQLJDBCDataModel(dataSource, PERFERENCETABLE, USERID_COLUMN, ITEMID_COLUMN,
					PERFERENCE_COLUMN, TIMESTAMP_COLUMN);
		}
		return dataModel;
	}

	@Override
	public DataModel getDataModel() {
		return getJDBCDataModel();
	}

}
