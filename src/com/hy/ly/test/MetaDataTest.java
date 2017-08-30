package com.hy.ly.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.Test;

import com.hy.ly.utils.DBUtils;

public class MetaDataTest {

	// 结果集的原数据
	@Test
	public void testResultSetMetaData() {
		Connection conn = DBUtils.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql="SELECT id,name userName,password passWord FROM person WHERE id=9";
		try {
			pst=conn.prepareStatement(sql);
			rs=pst.executeQuery();
			
			ResultSetMetaData rsmd=rs.getMetaData();
			
			//获取列名
			for(int i=0;i<rsmd.getColumnCount();i++){
				System.out.println(rsmd.getColumnLabel(i+1));
				System.out.println(rsmd.getColumnName(i+1));
				System.out.println(rsmd.getColumnType(i+1));
			}
			//获取列个数
			System.out.println(rsmd.getColumnCount());
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(rs, pst, conn);
		}
	}

	// 数据库的原数据，DatabaseMetaData
	@Test
	public void testDatabaseMetaData() {
		Connection conn = DBUtils.getConnection();
		ResultSet rs = null;
		try {
			DatabaseMetaData dmd = conn.getMetaData();
			// 数据库版本号
			int version = dmd.getDatabaseMajorVersion();
			System.out.println(version);
			// 数据库用户名
			String user = dmd.getUserName();
			System.out.println(user);
			// 获取数据库中的所有的库名
			rs = dmd.getCatalogs();
			while (rs.next()) {
				System.out.println(rs.getString(1));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(rs, null, conn);
		}
	}

}
