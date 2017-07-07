package com.hy.ly.test;

import java.util.Properties;

import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DruidTest {

	@Test
	public void testDruidDataSource() throws Exception {

		Properties properties = new Properties();
		properties.load(DruidTest.class.getClassLoader().getResourceAsStream("druid.properties"));
		DruidDataSource ds = null;
		ds = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
		
		System.out.println(ds.getConnection());
		System.out.println(ds.getClass());
	}
}
