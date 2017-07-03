package com.hy.ly.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
	public static int update(String sql) {
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
	
	// 更新的方法
	public static int update(String sql,Object ...args) {
		Connection conn = null;
		PreparedStatement pst = null;
		int row = 0;
		try {
			// 获取连接
			conn = DBUtils.getConnection();
			// 创建Statement对象
			pst = conn.prepareStatement(sql);
			//替换变量到pst中
			for(int i=0;i<args.length;i++){
				pst.setObject(i+1, args[i]);
			}
			// 执行插入sql语句
			row = pst.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(null, pst, conn);
		}
		return row;
	}

	// 查询的方法
	public static boolean getResult(String sql) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		boolean flag=false;
		//String sql = "SELECT * FROM person WHERE id=10";
		try {
			// 获取连接
			conn = DBUtils.getConnection();
			// 创建Statement对象
			st = conn.createStatement();
			// 执行插入sql语句
			rs = st.executeQuery(sql);
			// 处理Resultset结果集
			if (rs.next()) {
				flag=true;
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String password=rs.getString("password");
				System.out.println("id=" + id + " name=" + name+" password="+password);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(rs, st, conn);
		}
		return flag;
	}
}
