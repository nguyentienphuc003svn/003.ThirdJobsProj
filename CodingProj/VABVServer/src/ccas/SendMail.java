package ccas;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendMail{
	//������Email��ַ
	private static String senderEmail = "���㱦 service@86ecp.com";
	//smtp���ͷ���������
	private static String smtpServerName = "mail.86ecp.com";
	//smtp���ͷ�������¼��,һ������Ҫ���Ϻ�׺@86ecp.com
	private static String smtpLoginName = "smtp@86ecp.com";
	//smtp���ͷ���������
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
			Properties props = new Properties(); //Ҳ����Properties props = System.getProperties();
			props.put("mail.smtp.host",smtpServerName); //�洢�����ʼ�����������Ϣ
			props.put("mail.smtp.auth","true");		//ͬʱͨ����֤
			Session mailSession = Session.getInstance(props,null); //���������½�һ���ʼ��Ự
			mailSession.setDebug(true);

			MimeMessage mimeMessage = new MimeMessage(mailSession); //���ʼ��Ự�½�һ����Ϣ����

			//�����ʼ�
			StringTokenizer tokens;
			String emailNick=null;
			tokens=new StringTokenizer(senderEmail.trim()," ");
			if	(tokens.countTokens()==2)		emailNick=tokens.nextToken();
			senderEmail=tokens.nextToken();

			InternetAddress from = new InternetAddress(senderEmail,emailNick);
			mimeMessage.setFrom(from); //���÷�����

			tokens=new StringTokenizer(tto.trim()," ");
			emailNick=null;
			if	(tokens.countTokens()==2)		emailNick=tokens.nextToken();
			tto=tokens.nextToken();

			InternetAddress to = new InternetAddress(tto,emailNick);
			mimeMessage.setRecipient(Message.RecipientType.TO, to); //�����ռ���,���������������ΪTO
			mimeMessage.setSubject(ttitle); //��������
			mimeMessage.setContentLanguage(contentLanguage);
			mimeMessage.setText(tcontent); //�����ż�����
			mimeMessage.setSentDate(new Date()); //���÷���ʱ��

			//�����ʼ�
			mimeMessage.saveChanges(); //�洢�ʼ���Ϣ
			Transport transport = mailSession.getTransport("smtp");
			tokens=new StringTokenizer(smtpLoginName.trim(),"@");
			if	(tokens.hasMoreTokens())		smtpLoginName=tokens.nextToken();
			transport.connect((String) props.get("mail.smtp.host"),smtpLoginName,smtpPassword); //��smtp��ʽ��¼����
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients()); //�����ʼ�,���еڶ�����������������õ��ռ��˵�ַ
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


