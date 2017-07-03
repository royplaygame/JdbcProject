package com.hy.ly;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

import com.hy.ly.utils.DBUtils;
import com.mysql.jdbc.Driver;

public class DriverTest {
	// 查询方法
	@Test
	public void testResultSet() {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM person WHERE id=10";
		try {
			// 获取连接
			conn = this.getConn();
			// 创建Statement对象
			st = conn.createStatement();
			// 执行插入sql语句
			rs = st.executeQuery(sql);
			// 处理Resultset结果集
			if(rs.next()){
				int id=rs.getInt("id");
				String name=rs.getString("name");
				System.out.println("id="+id+" name="+name);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(rs, st, conn);
		}

	}

	// 通用的update方法，包括增加、修改、删除
	public void update(String sql) {
		Connection conn = null;
		Statement st = null;
		try {
			// 获取连接
			conn = this.getConn();
			// 创建Statement对象
			st = conn.createStatement();
			// 执行插入sql语句
			int row = st.executeUpdate(sql);
			System.out.println(row);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(null, st, conn);
		}

	}

	// 向数据表中插入一条数据
	@Test
	public void testStatement() {
		Connection conn = null;
		Statement st = null;
		try {
			String sql = "INSERT INTO person(name) VALUES('张飞')";
			// 获取连接
			conn = this.getConn();
			// 创建Statement对象
			st = conn.createStatement();

			// 执行插入sql语句
			int row = st.executeUpdate(sql);
			System.out.println(row);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// 关闭连接
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	public Connection getConn() {
		// 读取类路径下的jdbc.properties文件
		Properties properties = new Properties();
		try {
			properties.load(DriverTest.class.getClassLoader().getResourceAsStream("jdbc.properties"));
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

	// DriverManager的驱动管理类，

	public Connection testDriverManager() throws Exception {
		String driverClass = null;
		String url = null;
		String user = null;
		String password = null;

		// 读取类路径下的jdbc.properties文件
		InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(in);
		driverClass = properties.getProperty("driver");
		url = properties.getProperty("url");
		user = properties.getProperty("user");
		password = properties.getProperty("password");

		// 加载数据的驱动,注册驱动程序
		Class.forName(driverClass);
		Properties info = new Properties();
		info.setProperty("user", user);
		info.setProperty("password", password);

		Connection conn = DriverManager.getConnection(url, user, password);

		return conn;
	}

	// 用数据库厂商提供的Driver接口来获取数据库连接，
	@Test
	public void driverTest() throws SQLException {

		// 创建实现类的对象
		Driver driver = new Driver();

		// 连接数据库的基本信息
		String url = "jdbc:mysql://192.168.56.102:3306/test";
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "luoyang");

		// 调用Driver接口的connect(url,info)方法，获取连接
		Connection conn = driver.connect(url, info);
		System.out.println(conn);
	}

	/**
	 * 编写一个通用的方法，在不修改源程序的情况下，可以获取任意数据库的连接
	 * 1.把数据库的驱动全类名、url、user、password放到一个配置文件中来配置。 通过文件的形式实现和各数据库的解耦
	 * 
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * 
	 */

	public Connection getConnection() throws Exception {
		String driverClass = null;
		String url = null;
		String user = null;
		String password = null;

		// 读取类路径下的jdbc.properties文件
		InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(in);
		driverClass = properties.getProperty("driver");
		url = properties.getProperty("url");
		user = properties.getProperty("user");
		password = properties.getProperty("password");

		// 反射调用，生成driver对象
		Driver driver = (Driver) Class.forName(driverClass).newInstance();
		Properties info = new Properties();
		info.setProperty("user", user);
		info.setProperty("password", password);

		Connection conn = driver.connect(url, info);

		return conn;
	}

	@Test
	public void connectionTest() throws Exception {
		System.out.println(this.getConnection());
		System.out.println(this.testDriverManager());
		System.out.println(this.getConn());
	}
}
