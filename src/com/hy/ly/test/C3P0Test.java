package com.hy.ly.test;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0Test {

	//
	@Test
	public void testC3P0WithConfigXML() throws SQLException {
		DataSource dataSource = new ComboPooledDataSource("helloc3p0");
		System.out.println(dataSource.getConnection());
	}

	// 添加jar c3p0-0.9.5.2.jar，依赖mchange-commons-java-0.2.11.jar
	@Test
	public void testC3P0DataSource() throws Exception {

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass("com.mysql.jdbc.Driver"); // loads the jdbc driver
		cpds.setJdbcUrl("jdbc:mysql://10.2.2.28:3306/test");
		cpds.setUser("root");
		cpds.setPassword("root");

		Connection conn = cpds.getConnection();
		System.out.println(conn);
	}
}
