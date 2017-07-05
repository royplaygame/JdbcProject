package com.hy.ly.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.hy.ly.dao.Dao;
import com.hy.ly.dao.DaoImpl;
import com.hy.ly.entity.Person;


public class DaoImplTest {

	@Test
	public void testUpdate() {
		String sql = "UPDATE person SET password=?";
		Dao dao=new DaoImpl();
		int row=dao.update(sql, "000000");
		assertNotSame(0, row);
	}
	@Test
	public void testAdd() {
		String sql = "INSERT INTO person(name,password) VALUES(?,?)";
		Dao dao=new DaoImpl();
		int row=dao.update(sql, "貂蝉","123456");
		assertEquals(1, row);
	}
	@Test
	public void testDelete() {
		String sql = "DELETE FROM person WHERE id=?";
		Dao dao=new DaoImpl();
		int row=dao.update(sql, 10);
		assertEquals(1, row);
	}

	@Test
	public void testGet() {
		String sql="SELECT * FROM person WHERE id=?";
		Dao dao=new DaoImpl();
		Person p=dao.get(Person.class, sql, 9);
		System.out.println(p);
	}

	@Test
	public void testGetForList() {
		String sql="SELECT * FROM person";
		Dao dao=new DaoImpl();
		List<Person> list=dao.getForList(Person.class, sql);
		System.out.println(list.size());
		for(Person p:list){
			System.out.println(p);
		}
	}

	@Test
	public void testGetForValue() {
		String sql="SELECT COUNT(*) FROM person";
		Dao dao=new DaoImpl();
		long row=dao.getForValue(sql);
		System.out.println(row);
	}
	@Test
	public void testGetForValue2() {
		String sql="SELECT name FROM person WHERE id=?";
		Dao dao=new DaoImpl();
		String name=dao.getForValue(sql,11);
		System.out.println(name);
	}

}
