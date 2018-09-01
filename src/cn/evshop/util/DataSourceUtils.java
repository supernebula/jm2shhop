package cn.evshop.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSourceUtils {
	private static DataSource dataSource = new ComboPooledDataSource();
	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
	
	public static DataSource getDataSource()
	{
		return dataSource;
	}
	
	/**
	 * 当DBUtils需要手动控制事务时，调用该方法获得一个连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException
	{
		Connection con = threadLocal.get();
		if(con == null)
		{
			con = dataSource.getConnection();
			threadLocal.set(con);
		}
		return con;
	}
	
	/**
	 * 开启事务
	 * 
	 * @throws SQLException
	 */
	public static void startTranscation() throws SQLException{
		Connection conn = getConnection();
		if(conn != null)
			conn.setAutoCommit(false);
	}
	
	/**
	 * 从ThreadLocal中释放并且关闭Connection,并结束事务
	 * 
	 * @throws SQLException
	 */
	public static void releaseAndCloseConnection() throws SQLException{
		Connection conn = getConnection();
		if(conn != null)
		{
			conn.commit();
			threadLocal.remove();
			conn.close();
		}
	}
	
	/**
	 * 事务回滚
	 * 
	 * @throws SQLException
	 */
	public static void rollback() throws SQLException{
		Connection con = getConnection();
		if(con != null)
		{
			con.rollback();
		}
	}
	

}
