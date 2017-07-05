package com.hy.ly.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;

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
	public static int update(String sql, Object... args) {
		Connection conn = null;
		PreparedStatement pst = null;
		int row = 0;
		try {
			// 获取连接
			conn = DBUtils.getConnection();
			// 创建Statement对象
			pst = conn.prepareStatement(sql);
			// 替换变量到pst中
			for (int i = 0; i < args.length; i++) {
				pst.setObject(i + 1, args[i]);
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
		boolean flag = false;
		// String sql = "SELECT * FROM person WHERE id=10";
		try {
			// 获取连接
			conn = DBUtils.getConnection();
			// 创建Statement对象
			st = conn.createStatement();
			// 执行插入sql语句
			rs = st.executeQuery(sql);
			// 处理Resultset结果集
			if (rs.next()) {
				flag = true;
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String password = rs.getString("password");
				System.out.println("id=" + id + " name=" + name + " password=" + password);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(rs, st, conn);
		}
		return flag;
	}

	// 查询的方法
	public static boolean getResult(String sql, Object... args) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			// 获取连接
			conn = DBUtils.getConnection();
			// 创建Statement对象
			pst = conn.prepareStatement(sql);
			// 替换参数
			for (int i = 0; i < args.length; i++) {
				pst.setObject(i + 1, args[i]);
			}
			// 执行插入sql语句
			rs = pst.executeQuery();
			// 处理Resultset结果集
			if (rs.next()) {
				flag = true;
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String password = rs.getString("password");
				System.out.println("id=" + id + " name=" + name + " password=" + password);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(rs, pst, conn);
		}
		return flag;
	}

	// 查询方法
	public static <T> T getObject(Class<T> clazz,String sql, Object... args) {
		T entity = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd=null;
		try {
			// 获取连接
			conn = DBUtils.getConnection();
			// 创建Statement对象
			pst = conn.prepareStatement(sql);
			// 替换参数
			for (int i = 0; i < args.length; i++) {
				pst.setObject(i + 1, args[i]);
			}
			// 执行插入sql语句
			rs = pst.executeQuery();
			// 处理Resultset结果集
			if (rs.next()) {
				/**
				 * 1. 先利用结果进行查询得到结果集
				 * 2. 利用反射创建实体对象
				 * 3. 获取结果集的别名
				 * 4. 再获取结果集的值，结合3得到一个Map,键是列的别名，值是列的值，
				 * 5. 再利用反射为2的实体对象赋值，属性为Map的key,值为Map的值.
				 *
				 */
				//反射创建对象
				entity=clazz.newInstance();
				
				//通过解析sql来判断查询了那些列,再给这些列赋值
				rsmd=rs.getMetaData();
				
				//再获取结果集的值,封装到Map
				int count=rsmd.getColumnCount();
				Map<String,Object> map=new HashMap<String,Object>();
				for(int i=0;i<count;i++){
					String columnLabel=rsmd.getColumnLabel(i+1);
					Object value=rs.getObject(columnLabel);
					map.put(columnLabel,value);
				}
				//创建一个对象
				entity=clazz.newInstance();
				
				//利用反射，再把map中的值赋值给实体对象
				for(Map.Entry<String, Object> entry:map.entrySet()){
					String fieldName = entry.getKey();
					Object fieldValue = entry.getValue();
					//需要用到commons-beanutils-1.9.2.jar、commons-logging-1.2.jar、commons-collections-3.2.jar
					BeanUtils.setProperty(entity, fieldName, fieldValue);
					
					//通过spring的ReflectionUtils来给属性赋值spring-core-4.3.3.RELEASE.jar
					/*Field field=ReflectionUtils.findField(clazz, fieldName);
					ReflectionUtils.makeAccessible(field);
					ReflectionUtils.setField(field, entity, fieldValue);*/
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(rs, pst, conn);
		}
		return entity;
	}
}
