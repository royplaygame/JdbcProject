package com.hy.ly.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtils {

	// 获取连接
	public static Connection getConnection() {
		// 读取类路径下的jdbc.properties文件
		Properties properties = new Properties();
		try {
			properties.load(DBUtils.class.getClassLoader().getResourceAsStream("jdbc.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 加载数据的驱动,注册驱动程序
		try {
			Class.forName(properties.getProperty("driver"));

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(properties.getProperty("url"), properties);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	// 释放数据库资源
	public static void release(ResultSet rs, Statement st, Connection conn) {
		// 关闭ResultSet
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 关闭statement
		try {
			if (st != null) {
				st.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 关闭conn
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 更新的方法

	public int update(String sql) {
		Connection conn = null;
		Statement st = null;
		int row = 0;
		try {
			// 获取连接
			conn = DBUtils.getConnection();
			// 创建Statement对象
			st = conn.createStatement();
			// 执行插入sql语句
			row = st.executeUpdate(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(null, st, conn);
		}
		return row;
	}

	// 查询的方法
	public ResultSet getResult() {
		return null;
	}
}
