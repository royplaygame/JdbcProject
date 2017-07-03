package com.hy.ly.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import org.junit.Test;

import com.hy.ly.entity.Person;
import com.hy.ly.utils.DBUtils;

public class JDBCTest {

	@Test
	public void testPreparedStatement() {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean flag = false;
		String sql = "select * from person where name=? and password=?";
		try {
			// 获取连接
			conn = DBUtils.getConnection();
			// 创建Statement对象
			pst = conn.prepareStatement(sql);
			pst.setString(1, "lvbu");
			//pst.setString(1, "lvbu' or 1=1 or 2='2");
			pst.setString(2, "123456");
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
		System.out.println(flag);
	}

	// sql注入漏洞，如何避免，用PrepredStatement
	@Test
	public void login() {
		// 获取用户名称
		Person p = loginPerson();
		String sql = "select * from person where name='" + p.getName() + "' and password='" + p.getPassword() + "'";
		System.out.println(sql);
		// 查询当前帐号
		boolean flag = DBUtils.getResult(sql);
		System.out.println(flag);
	}

	private Person loginPerson() {
		Person p = new Person();
		p.setName("lvbu' or 1=1 or 2='2");
		p.setPassword("21231313");
		return p;
	}

	@Test
	public void testGetPerson() {
		// 获取用户名称
		Person p = searchPerson();
		String sql = "select * from person where name='" + p.getName() + "'";
		// 查询当前帐号
		DBUtils.getResult(sql);
	}

	private Person searchPerson() {
		Scanner s = new Scanner(System.in);
		Person p = new Person();
		System.out.println("请输入用户帐号");
		p.setName(s.next());
		s.close();
		return p;
	}

	@Test
	public void testAddNewPerson() {
		Person p = getPersonFromConsole();
		addNewPerson(p);
	}
	@Test
	public void testAddNewPersonPrepared() {
		Person p = getPersonFromConsole();
		addNewPersonPrepared(p);
	}

	// 控制台输入person信息
	private Person getPersonFromConsole() {
		Scanner s = new Scanner(System.in);
		Person p = new Person();
		System.out.println("请输入用户帐号");
		p.setName(s.next());
		System.out.println("请输入用户密码");
		p.setPassword(s.next());
		s.close();
		return p;
	}

	public void addNewPerson(Person p) {
		String sql = "INSERT INTO person(name,password) VALUES('" + p.getName() + "','" + p.getPassword() + "')";
		System.out.println(sql);
		DBUtils.update(sql);
	}
	
	public void addNewPersonPrepared(Person p) {
		String sql = "INSERT INTO person(name,password) VALUES(?,?)";
		System.out.println(sql);
		DBUtils.update(sql,p.getName(),p.getPassword());
	}

	
}
