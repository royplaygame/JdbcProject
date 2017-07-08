package com.hy.ly.test;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.util.List;

import org.junit.Test;

import com.hy.ly.dao.PersonDao;
import com.hy.ly.entity.Person;
import com.hy.ly.utils.DBUtils;

public class PersonDaoTest {

	PersonDao personDao = new PersonDao();

	@Test
	public void testBatch() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetForValue() {
		Connection conn = null;

		try {
			conn = DBUtils.getConnection();
			//String sql = "select count(*) from person";
			String sql = "select name from person where id=11";
			String name = personDao.getForValue(conn, sql);
			System.out.println(name);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.release(null, null, conn);
		}
	}

	@Test
	public void testGetForList() {
		Connection conn = null;

		try {
			conn = DBUtils.getConnection();
			String sql = "select * from person";
			List<Person> list = personDao.getForList(conn, sql);
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.release(null, null, conn);
		}
	}

	@Test
	public void testGet() {
		Connection conn = null;

		try {
			conn = DBUtils.getConnection();
			String sql = "select * from person where id=?";
			Person p = personDao.get(conn, sql, 11);
			System.out.println(p);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.release(null, null, conn);
		}
	}

	@Test
	public void testUpdate() {
		Connection conn=null;
		try{
			conn=DBUtils.getConnection();
			String sql="INSERT INTO person(name,password,balance) VALUES(?,?,?)";
			personDao.update(conn, sql, "sunquan","111111",5000);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			DBUtils.release(null, null, conn);
		}
	}

}
