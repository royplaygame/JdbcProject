package com.hy.ly.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

public class DBUtilDaoImpl<T> implements DBUtilDao<T> {

	private QueryRunner qr = null;
	private Class<T> type = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DBUtilDaoImpl() {
		qr = new QueryRunner();
		// type=DBUtilDaoImpl.class.getGenericSuperclass();
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		type = (Class) params[0];
	}

	@Override
	public void batch(Connection conn, String sql, Object[]... args) throws SQLException {
		qr.batch(conn, sql, args);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <E> E getForValue(Connection conn, String sql, Object... args) throws SQLException {
		return (E) qr.query(conn, sql, new ScalarHandler(), args);
	}

	@Override
	public List<T> getForList(Connection conn, String sql, Object... args) throws SQLException {
		return qr.query(conn, sql, new BeanListHandler<>(type), args);
	}

	@Override
	public T get(Connection conn, String sql, Object... args) throws SQLException {
		return qr.query(conn, sql, new BeanHandler<>(type), args);
	}

	@Override
	public void update(Connection conn, String sql, Object... args) throws SQLException {
		qr.update(conn, sql, args);
	}

}
