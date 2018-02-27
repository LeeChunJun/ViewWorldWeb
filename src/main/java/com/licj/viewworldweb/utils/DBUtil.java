package com.licj.viewworldweb.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

	public static Connection getJDBCConnection() {
		//声明Connection对象
        Connection connection = null;
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库名music_all_db
        String url = "jdbc:mysql://localhost:3306/music_all_db";
        //MySQL配置时的用户名
        String user = "root";
        //MySQL配置时的密码
        String password = "destiny";
        //遍历查询结果集
        try {
            //加载驱动程序
            Class.forName(driver);
            //getConnection()方法，连接MySQL数据库！！
            if (connection == null){
            	connection = DriverManager.getConnection(url,user,password);
            }
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return connection;
	}

}
