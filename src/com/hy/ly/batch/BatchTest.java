package com.hy.ly.batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.junit.Test;

import com.hy.ly.utils.DBUtils;

public class BatchTest {

	// 向Oracle的Customer表中插入10W条记录，并查看怎么插入用时最短
	// 1. Statement
	// 2. PreparedStatement

	@Test
	public void testBatch() {
		Connection conn = DBUtils.getConnection();
		PreparedStatement pst = null;
		String sql = "insert into customers values(?,?,to_date(?,'yyyy-MM-dd'))";
		try {
			conn = DBUtils.getConnection();
			DBUtils.beginTx(conn);
			System.out.println(conn);
			pst = conn.prepareStatement(sql);
			long begin = System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				pst.setObject(1, i + 1);
				pst.setObject(2, "name_" + (i + 1));
				pst.setObject(3, "2017-10-10");
				//积累sql
				pst.addBatch();
				//积累到一定数量的时候再执行积累的sql
				if((i+1)%300==0){
					pst.executeBatch();
					pst.clearBatch();
				}
				//如果总数不是批量值的整数倍时再执行一次。
				if(10000%300!=0){
					pst.executeBatch();
					pst.clearBatch();
				}
			}
			long end = System.currentTimeMillis();
			System.out.println(end - begin);
			DBUtils.commit(conn);
		} catch (Exception e) {
			e.printStackTrace();
			DBUtils.rollback(conn);
		} finally {
			DBUtils.release(null, pst, conn);
		}
	}

	@Test
	public void testBatchWithPreparedStatement() {
		Connection conn = DBUtils.getConnection();
		PreparedStatement pst = null;
		String sql = "insert into customers values(?,?,to_date(?,'yyyy-MM-dd'))";
		try {
			conn = DBUtils.getConnection();
			DBUtils.beginTx(conn);
			System.out.println(conn);
			pst = conn.prepareStatement(sql);
			long begin = System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				pst.setObject(1, i + 1);
				pst.setObject(2, "name_" + (i + 1));
				pst.setObject(3, "2017-10-10");
				pst.executeUpdate();
			}
			long end = System.currentTimeMillis();
			System.out.println(end - begin);
			DBUtils.commit(conn);
		} catch (Exception e) {
			e.printStackTrace();
			DBUtils.rollback(conn);
		} finally {
			DBUtils.release(null, pst, conn);
		}
	}

	@Test
	public void testBatchWithStatement() {
		Connection conn = DBUtils.getConnection();
		Statement st = null;

		try {
			conn = DBUtils.getConnection();
			DBUtils.beginTx(conn);
			System.out.println(conn);
			st = conn.createStatement();
			long begin = System.currentTimeMillis();
			for (int i = 0; i < 10000; i++) {
				String sql = "insert into customers values(" + i + 1 + ",'name_" + i + 1
						+ "',to_date('2017-10-10','yyyy-MM-dd'))";
				st.executeUpdate(sql);
			}
			long end = System.currentTimeMillis();
			System.out.println(end - begin);
			DBUtils.commit(conn);
		} catch (Exception e) {
			e.printStackTrace();
			DBUtils.rollback(conn);
		} finally {
			DBUtils.release(null, st, conn);
		}
	}
}
