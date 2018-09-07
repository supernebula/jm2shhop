package cn.evshop.util;
import java.util.Properties;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

public class MailUtils {
	public static void sendMail(String email, String emialMsg) throws AddressException, MessagingException
	{
		// 1.创建一个程序与邮件服务器会话对象，Session
		
		
		Properties props = new Properties();
		
		// 设置邮件传输协议为SMTP
		props.setProperty("mail.transport.protocol", "SMTP");
		// 设置SMTP服务器地址
		props.setProperty("mail.host", "smtp.qq.com");
		props.setProperty("mail.smtp.auth", "true");
		
		// 创建验证器
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("","");
			}
		};
		
		Session session = Session.getInstance(props, auth);
		
		// 2. 创建一个Message, 她相当于是邮件正文
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("22257443@qq.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(email));
		message.setSubject("用户激活");
		message.setContent(emialMsg, "text/html;charset=utf-8");
	    Transport.send(message);
		
		
		
	}
}
