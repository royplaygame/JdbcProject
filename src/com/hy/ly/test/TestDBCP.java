package com.hy.ly.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.Test;

public class TestDBCP {

	// 1. 加载properties配置文件,配置文件中的key的值需要来自BasicDataSource类的属性
	// 2.
	// 调用BasicDataSourceFactory.createDataSource(properties)的方法，创建DataSource实例
	// 3. DataSource实例获取连接
	@Test
	public void testDBCPFactory() throws Exception {
		Properties properties = new Properties();
		properties.load(TestDBCP.class.getClassLoader().getResourceAsStream("dbcp.properties"));
		DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);
		System.out.println(dataSource.getConnection());
	}

	// 使用DBCP连接池
	// 1. 加入jar包commons-dbcp2-2.1.1.jar，依赖commons-logging-1.2.jar,commons-pool2-2.3.jar
	// 2. 创建数据库连接池BasicDataSource
	// 3。 设置必须的属性
	// 4. 获取Connection连接
	@SuppressWarnings("resource")
	@Test
	public void testDBCP() throws SQLException {
		// 创建DBCP数据源实例
		final BasicDataSource dataSource = new BasicDataSource();

		// 为数据源指定必要的属性
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		dataSource.setUrl("jdbc:mysql://10.2.2.28:3306/test");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");

		// 可选的属性
		// 1).指定数据库连接池中初始化连接的个数
		dataSource.setInitialSize(5);
		// 2).指定数据库连接池中最大连接的个数，同一时刻可以向数据库申请的连接数
		dataSource.setMaxTotal(5);
		// 3).在数据库连接池中保持的最小空闲连接的个数
		dataSource.setMinIdle(3);
		// 4).在数据库连接池中保持的最大空闲连接的个数
		dataSource.setMaxIdle(5);
		// 5).等待数据库分配置连接的最长时间，单位毫秒，超出时间抛出异常
		dataSource.setMaxWaitMillis(1000 * 5);

		// 获取conn
		Connection conn = dataSource.getConnection();
		System.out.println(conn);
		System.out.println(conn.getClass());

		Connection conn2 = dataSource.getConnection();
		System.out.println(conn2);

		Connection conn3 = dataSource.getConnection();
		System.out.println(conn3);

		Connection conn4 = dataSource.getConnection();
		System.out.println(conn4);

		Connection conn5 = dataSource.getConnection();
		System.out.println(">>>>>>>>" + conn5);

		/*Connection conn6 = dataSource.getConnection();
		System.out.println(conn6);*/

		new Thread() {
			public void run() {
				Connection conn;
				try {
					conn = dataSource.getConnection();
					System.out.println(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		//等待3秒，关闭conn5
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		conn5.close();
	}

}
