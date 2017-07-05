package com.hy.ly.test;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.hy.ly.dao.Dao;
import com.hy.ly.dao.DaoImpl;
import com.hy.ly.entity.Person;
import com.hy.ly.utils.DBUtils;

public class BlobTest {

	// 插入Blob数据到数据库中，因为Blob的数据无法用字符中数据进行插入
	@Test
	public void testAdd() throws FileNotFoundException {
		String sql = "INSERT INTO person(name,password,pic) VALUES(?,?,?)";
		Dao dao = new DaoImpl();

		InputStream in = new FileInputStream("hily.jpg");
		int row = dao.update(sql, "貂蝉", "123456", in);
		assertEquals(1, row);
	}

	@Test
	public void testQuery() throws SQLException, IOException {
		String sql = "SELECT * FROM person WHERE id=?";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			// 获取连接
			conn = DBUtils.getConnection();
			// 创建Statement对象
			pst = conn.prepareStatement(sql);
			pst.setObject(1, 19);
			
			// 执行插入sql语句
			rs = pst.executeQuery();
			// 处理Resultset结果集
			if (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String password = rs.getString("password");
				Blob picture=rs.getBlob("pic");
				System.out.println("id=" + id + " name=" + name + " password=" + password);
				if (picture != null) {
					InputStream in = picture.getBinaryStream();
					OutputStream out = new FileOutputStream("flow.jpg");

					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = in.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}
					out.close();
					in.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(rs, pst, conn);
		}
	}

}
