package com.hy.ly.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.Test;

import com.hy.ly.entity.Person;
import com.hy.ly.utils.DBUtils;

public class CommonsDBUtilsTest {
	
	@SuppressWarnings("rawtypes")
	class myResultSetHandler implements ResultSetHandler{

		@Override
		public Object handle(ResultSet resultSet) throws SQLException {
			System.out.println("handler");
			//return "hp.ly";
			//List list=new ArrayList();
			while(resultSet.next()){
				
			}
			
			return null;
		}
		
	}

	//更新方法
	@Test
	public void testQueryRunnerUpdate() throws SQLException {
		Connection conn = null;
		// 1. 创建QueryRunner实现类的对象
		QueryRunner qr = new QueryRunner();
		// 2. 使用update方法
		String sql = "UPDATE person SET password=? WHERE id=?";

		try {
			conn = DBUtils.getConnection();
			qr.update(conn, sql, "123456",11);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(null, null, conn);
		}
	}
	
	//添加方法
	@Test
	public void testQueryRunnerAdd() throws SQLException {
		Connection conn = null;
		// 1. 创建QueryRunner实现类的对象
		QueryRunner qr = new QueryRunner();
		// 2. 使用update方法
		String sql = "INSERT INTO person(name,password) VALUES(?,?)";
		
		try {
			conn = DBUtils.getConnection();
			qr.update(conn, sql, "zhangfei","111111");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(null, null, conn);
		}
	}
	
	//删除方法
	@Test
	public void testQueryRunnerDelete() throws SQLException {
		Connection conn = null;
		// 1. 创建QueryRunner实现类的对象
		QueryRunner qr = new QueryRunner();
		// 2. 使用update方法
		String sql = "DELETE FROM person WHERE id=?";
		
		try {
			conn = DBUtils.getConnection();
			qr.update(conn, sql, 19);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(null, null, conn);
		}
	}
	
	//查询方法
	@SuppressWarnings("unchecked")
	@Test
	public void testQuery() throws SQLException{
		Connection conn = null;
		// 1. 创建QueryRunner实现类的对象
		QueryRunner qr = new QueryRunner();
		// 2. 使用update方法
		String sql = "SELECT * FROM person";
		
		try {
			conn = DBUtils.getConnection();
			Object obj=qr.query(conn, sql, new myResultSetHandler());
			System.out.println(obj);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(null, null, conn);
		}
	}
	//查询方法
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testQueryBeanHandler() throws SQLException{
		Connection conn = null;
		// 1. 创建QueryRunner实现类的对象
		QueryRunner qr = new QueryRunner();
		// 2. 使用update方法
		String sql = "SELECT * FROM person where id=?";
		
		try {
			conn = DBUtils.getConnection();
			Person obj=qr.query(conn, sql, new BeanHandler(Person.class), 11);
			System.out.println(obj);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(null, null, conn);
		}
	}
	
	//查询方法
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testQueryBeanListHandler() throws SQLException{
		Connection conn = null;
		// 1. 创建QueryRunner实现类的对象
		QueryRunner qr = new QueryRunner();
		// 2. 使用update方法
		String sql = "SELECT * FROM person";
		
		try {
			conn = DBUtils.getConnection();
			List<Person> list=qr.query(conn, sql, new BeanListHandler(Person.class));
			for(Person p:list){
				System.out.println(p);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(null, null, conn);
		}
	}
	//查询方法
	@Test
	public void testQueryMapHandler() throws SQLException{
		Connection conn = null;
		// 1. 创建QueryRunner实现类的对象
		QueryRunner qr = new QueryRunner();
		// 2. 使用update方法
		String sql = "SELECT * FROM person";
		
		try {
			conn = DBUtils.getConnection();
			Map<String,Object> persons=qr.query(conn, sql, new MapHandler());
				System.out.println(persons);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(null, null, conn);
		}
	}
	//查询方法
	@Test
	public void testQueryMapListHandler() throws SQLException{
		Connection conn = null;
		// 1. 创建QueryRunner实现类的对象
		QueryRunner qr = new QueryRunner();
		// 2. 使用update方法
		String sql = "SELECT * FROM person";
		
		try {
			conn = DBUtils.getConnection();
			List<Map<String,Object>> persons=qr.query(conn, sql, new MapListHandler());
			System.out.println(persons);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.release(null, null, conn);
		}
	}

}
