package cn.evshop.service;

import java.sql.SQLException;
import cn.evshop.dao.UserDao;
import cn.evshop.domain.User;
import cn.evshop.exception.ActiveUserException;
import cn.evshop.exception.LoginException;
import cn.evshop.exception.RegisterException;
import cn.evshop.util.MailUtils;

public class UserSerivce {
	
	private UserDao userDao = new UserDao();
	
	public void register(User user) throws RegisterException
	{	
		try {
			userDao.addUser(user);
			String emailMsg = "感谢您注册网上书城，点击\"\n" + "<a href='http://localhost:8080/bookstore/activeUser?activeCode=" + user.getActiveCode() + "'>&nbsp;激活&nbsp;</a>后使用。<br />为保障您的账户安全，请在24小时内完成激活操作";
			MailUtils.sendMail(user.getEmail(), emailMsg);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RegisterException("注册失败");
		}
	}
	
	// 激活用户
	public void activeUser(String activeCode) throws ActiveUserException
	{
		try {
			User user = userDao.findUserByActiveCode(activeCode);
			
			//判断激活码是否过期，24小时内激活有效
			// 得到注册时间
			java.util.Date regTime = user.getRegTime();
			
			// 2.判断是否超时
			long time = System.currentTimeMillis() - regTime.getTime();
			if(time /1000 / 60 / 60 > 24)
				throw new ActiveUserException("激活码过期");
			// 激活用户，就是修改用户的state状态
			userDao.activeUser(activeCode);
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ActiveUserException("激活用户失败");
		}
	}
	
	// 登录操作
	public User login(String username, String password) throws LoginException
	{
		try {
			//根据登录时表单输入的用户名和密码，查找用户
			User user = userDao.findUserByUsernameAndPassword(username, password);
			//如果用户找到，还需要确定用户是否为激活用户
			if(user != null)
			{
				if (user.getStatus() == 1) {
					return user;
				}
				throw new LoginException("用户未激活");
				
			}
			throw new LoginException("用户名或密码错误");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new LoginException("登录失败");
		}
		

		
		

	}
}
