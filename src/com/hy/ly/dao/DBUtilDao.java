package com.hy.ly.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DBUtilDao<T> {

	// 批处理方法
	void batch(Connection conn, String sql, Object[]... args) throws SQLException;

	// 返回一个值
	<E> E getForValue(Connection conn, String sql, Object... args) throws SQLException;

	// 获取一个对象列表
	List<T> getForList(Connection conn, String sql, Object... args) throws SQLException;

	// 获取一个对象
	T get(Connection conn, String sql, Object... args) throws SQLException;

	// insert、update、delete 操作
	void update(Connection conn, String sql, Object... args) throws SQLException;

}
