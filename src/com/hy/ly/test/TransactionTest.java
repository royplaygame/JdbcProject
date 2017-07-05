package com.hy.ly.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;

import com.hy.ly.dao.Dao;
import com.hy.ly.dao.DaoImpl;
import com.hy.ly.utils.DBUtils;

public class TransactionTest {

	/**
	 * liub给zhaoy 汇款500元 关于事务 1. 每一个事务是单独的连接，没法保证同一个事务。
	 */
	@Test
	public void testTransaction() {
		Dao dao = new DaoImpl();
		String sql = "update person set balance=balance-500 where id=11";
		dao.update(sql);
		// 在这个地方出现异常
		int k = 10 / 0;
		System.out.println(k);
		sql = "update person set balance=balance+500 where id=9";
		dao.update(sql);
	}

	@Test
	public void testTransactionOk() {
		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = DBUtils.getConnection();
			// 开始事务
			conn.setAutoCommit(false);
			String sql = "update person set balance=balance-500 where id=11";
			update(conn, sql);
			// 在这个地方出现异常
			int k = 10 / 0;
			System.out.println(k);
			sql = "update person set balance=balance+500 where id=9";
			update(conn, sql);
			// 提交事务
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				// 回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			DBUtils.release(null, pst, conn);
		}
	}

	private void update(Connection conn, String sql, Object... args) {
		// 获取连接
		PreparedStatement pst = null;
		try {
			// 获取PreparedStatement
			pst = conn.prepareStatement(sql);
			// 给变量赋值
			for (int i = 0; i < args.length; i++) {
				pst.setObject(i + 1, args[i]);
			}
			// 执行sql语句
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.release(null, pst, conn);
		}
	}

	// 事务模版
	public void templateTransaction() {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			// 开始事务
			conn.setAutoCommit(false);

			// 执行操作

			// 提交事务
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				// 回滚事务
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			DBUtils.release(null, null, conn);
		}
	}

}
