package com.hy.ly.dao;

import java.util.List;

public interface Dao {

	int update(String sql, Object... args);

	<T> T get(Class<T> clazz, String sql, Object... args);

	<T> List<T> getForList(Class<T> clazz, String sql, Object... args);

	<E> E getForValue(String sql, Object... args);
}
