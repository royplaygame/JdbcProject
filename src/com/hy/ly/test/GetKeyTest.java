package com.hy.ly.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

import com.hy.ly.utils.DBUtils;
import com.mysql.jdbc.Statement;

public class GetKeyTest {

	// 获取数据库自动生成的主键
	@Test
	public void testGetKeyValue() {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "INSERT INTO person(name,password) VALUES(?,?)";
		try {
			conn = DBUtils.getConnection();
			//pst = conn.prepareStatement(sql);
			
			//需要使用重载的方法prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst =conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setObject(1, "东方红");
			pst.setObject(2, "123456");
			pst.executeUpdate();

			//获取返回的结果集合pst.getGeneratedKeys();
			rs=pst.getGeneratedKeys();
			if(rs.next()){
				System.out.println(rs.getObject(1));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.release(rs, pst, conn);
		}
	}
}
