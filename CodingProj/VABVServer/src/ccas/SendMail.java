package ccas;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendMail{
	//发件人Email地址
	private static String senderEmail = "结算宝 service@86ecp.com";
	//smtp发送服务器名称
	private static String smtpServerName = "mail.86ecp.com";
	//smtp发送服务器登录名,一定不能要加上后缀@86ecp.com
	private static String smtpLoginName = "smtp@86ecp.com";
	//smtp发送服务器密码
	private static String smtpPassword = "smtpsmtpsmtpsmtpsmtp";
	private static String [] contentLanguage = {"text/html; charset=GBK"};
/**/
	public SendMail(String senderEmail) {
		this.senderEmail =senderEmail;
	}

	public void setSenderEmail(String senderEmail)	{
		this.senderEmail =senderEmail;
	}

	public void setSmtpServerName(String smtpServerName)	{
		this.smtpServerName =smtpServerName;
	}

	public void setSmtpLoginName(String smtpLoginName)	{
		this.smtpLoginName =smtpLoginName;
	}

	public void setSmtpPassword(String smtpPassword)	{
		this.smtpPassword =smtpPassword;
	}

	public boolean send(String tto,String ttitle,String tcontent){
		try {
			Properties props = new Properties(); //也可用Properties props = System.getProperties();
			props.put("mail.smtp.host",smtpServerName); //存储发送邮件服务器的信息
			props.put("mail.smtp.auth","true");		//同时通过验证
			Session mailSession = Session.getInstance(props,null); //根据属性新建一个邮件会话
			mailSession.setDebug(true);

			MimeMessage mimeMessage = new MimeMessage(mailSession); //由邮件会话新建一个消息对象

			//设置邮件
			StringTokenizer tokens;
			String emailNick=null;
			tokens=new StringTokenizer(senderEmail.trim()," ");
			if	(tokens.countTokens()==2)		emailNick=tokens.nextToken();
			senderEmail=tokens.nextToken();

			InternetAddress from = new InternetAddress(senderEmail,emailNick);
			mimeMessage.setFrom(from); //设置发件人

			tokens=new StringTokenizer(tto.trim()," ");
			emailNick=null;
			if	(tokens.countTokens()==2)		emailNick=tokens.nextToken();
			tto=tokens.nextToken();

			InternetAddress to = new InternetAddress(tto,emailNick);
			mimeMessage.setRecipient(Message.RecipientType.TO, to); //设置收件人,并设置其接收类型为TO
			mimeMessage.setSubject(ttitle); //设置主题
			mimeMessage.setContentLanguage(contentLanguage);
			mimeMessage.setText(tcontent); //设置信件内容
			mimeMessage.setSentDate(new Date()); //设置发信时间

			//发送邮件
			mimeMessage.saveChanges(); //存储邮件信息
			Transport transport = mailSession.getTransport("smtp");
			tokens=new StringTokenizer(smtpLoginName.trim(),"@");
			if	(tokens.hasMoreTokens())		smtpLoginName=tokens.nextToken();
			transport.connect((String) props.get("mail.smtp.host"),smtpLoginName,smtpPassword); //以smtp方式登录邮箱
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients()); //发送邮件,其中第二个参数是所有已设好的收件人地址
			transport.close();
			CLog.writeLog("send email["+tto+"]success!!");
			return true;
		}	catch(Exception e)		{
			CLog.writeLog("send email to["+tto+"]fail!!");
			CLog.writeLog("send email err,getMessage=" + e.getMessage());
			CLog.writeLog("send email err,Exception=" + e.toString());
			return false;
		}
	}
}


