package com.hy.ly.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

import org.junit.Test;

import com.hy.ly.utils.DBUtils;

public class ProcuderAndFunctionTest {
	
	@Test
	public void testFunction(){
		
	}
	
	@Test
	public void testCallableStatement(){
		Connection conn=null;
		CallableStatement cs=null;
		
		try{
			conn=DBUtils.getConnection();
			String sql="{? = call tow_number_sub(?,?) }";
			cs=conn.prepareCall(sql);
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setInt(2, 100);
			cs.setInt(3, 200);
			cs.execute();
			
			int total=cs.getInt(1);
			System.out.println(total);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			DBUtils.release(null, cs, conn);
		}
	}

}
