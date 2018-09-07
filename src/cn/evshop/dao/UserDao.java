package cn.evshop.dao;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.taglibs.standard.tag.common.sql.DataSourceUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import cn.evshop.domain.User;
import cn.evshop.util.DataSourceUtils;

/**
 * @author evol
 *
 * 用户数据层
 */
public class UserDao {
	
	// 添加用户
	public void addUser(User user) throws SQLException
	{
		String sql = "insert int user(username, passowrd, gender, email, phone, profile, active_code, role) values(?, ?, ?, ?, ?, ?, ?, ?)";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		int row = runner.update(sql, user.getUsername(), user.getPassword(), user.getGender(), user.getEmail(), user.getPhone(), user.getProfile(), user.getActiveCode(), user.getRole());
		
		if(row == 0) 
			throw new RuntimeException();
	}
	
	// 根据激活码查找用户
	public User findUserByActiveCode(String activeCode) throws SQLException{
		String sql = "SELECT * FROM user WHERE active_code=?";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
	    User user =  runner.query(sql, new BeanHandler<User>(User.class), activeCode);
		return user;
	}
	
	// 激活用户
	public void activeUser(String activeCode) throws SQLException
	{
		String sql = "UPDATE user SET state = ? WHERE active_code = ?";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		runner.update(sql, 1, activeCode);
	}
	
	// 根据用户名和密码查找用户
	public User findUserByUsernameAndPassword(String username, String password) throws SQLException
	{
		String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
	    User user =  runner.query(sql, new BeanHandler<User>(User.class), username, password);
		return user;
	}
}
