package com.hy.ly.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.hy.ly.utils.DBUtils;

public class DaoImpl implements Dao {

	@Override
	public int update(String sql, Object... args) {
		// 获取连接
		Connection conn = DBUtils.getConnection();
		PreparedStatement pst = null;
		int row = 0;
		try {
			// 获取PreparedStatement
			pst = conn.prepareStatement(sql);
			// 给变量赋值
			for (int i = 0; i < args.length; i++) {
				pst.setObject(i + 1, args[i]);
			}
			// 执行sql语句
			row = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtils.release(null, pst, conn);
		}
		return row;
	}

	@Override
	public <T> T get(Class<T> clazz, String sql, Object... args) {
		T entity = null;
		// 获取连接
		Connection conn = DBUtils.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		Map<String, Object> values = new HashMap<String, Object>();
		try {
			// 获取PreparedStatement
			pst = conn.prepareStatement(sql);
			// 给变量赋值
			for (int i = 0; i < args.length; i++) {
				pst.setObject(i + 1, args[i]);
			}
			// 执行sql语句
			rs = pst.executeQuery();
			// 获取ResultSetMetaData对象
			rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			if (rs.next()) {
				// 把一条记录对象先入到Map中,columnLable为key,对应的value为Map的值
				for (int j = 0; j < count; j++) {
					String columnLable = rsmd.getColumnLabel(j + 1);
					Object value = rs.getObject(columnLable);
					values.put(columnLable, value);
				}

				try {
					// 通过反射生成一个对象
					entity = clazz.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}

				// 遍历Map集合把所的属性值赋值给对象
				for (Map.Entry<String, Object> entry : values.entrySet()) {
					String fieldName = entry.getKey();
					Object fieldValue = entry.getValue();

					try {
						BeanUtils.setProperty(entity, fieldName, fieldValue);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.release(rs, pst, conn);
		}
		return entity;
	}

	@Override
	public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {
		T entity = null;
		// 获取Connection
		Connection conn = DBUtils.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		Map<String, Object> values = new HashMap<String, Object>();
		List<T> list = new ArrayList<T>();

		try {
			pst = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				pst.setObject(i + 1, args[i]);
			}
			// 执行
			rs = pst.executeQuery();
			rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			while (rs.next()) {
				// 把一条结果放入到一个Map中，key为：columnLable,Value为：列对应的值
				for (int j = 0; j < count; j++) {
					String columnLabel = rsmd.getColumnLabel(j + 1);
					Object value = rs.getObject(columnLabel);
					values.put(columnLabel, value);
				}

				try {
					// 获取一个实体对象
					entity = clazz.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}

				// 遍历Map给对象属性赋值
				for (Map.Entry<String, Object> entry : values.entrySet()) {
					String name = entry.getKey();
					Object value = entry.getValue();
					try {
						BeanUtils.setProperty(entity, name, value);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				list.add(entity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtils.release(rs, pst, conn);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> E getForValue(String sql, Object... args) {
		// 获取连接
		Connection conn = DBUtils.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			// 获取PreparedStatement
			pst = conn.prepareStatement(sql);
			// 给变量赋值
			for (int i = 0; i < args.length; i++) {
				pst.setObject(i + 1, args[i]);
			}
			// 执行sql语句,获取的结果集只有一列，且只有一行。
			rs = pst.executeQuery();
			if (rs.next()) {
				return (E) rs.getObject(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.release(rs, pst, conn);
		}
		return null;
	}

}
