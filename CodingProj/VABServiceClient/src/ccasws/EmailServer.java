package ccasws;

import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.channels.ServerSocketChannel;
import sun.misc.*;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.*;
import javax.sql.DataSource;

public class EmailServer{
	static	private	XmlBeanFactory factory=null;
	static	private	DataSource dataSource=null;
	static	private ObInterface obInterface=new ObInterface();
	//5044SMS
	//5045Email
	static	private	int serverPort=5045;
	static	private	org.apache.commons.mail.HtmlEmail myemail = null;
	static	private	final String xmlfile="cmsbeans.xml";
	static	private	Mysql mysql=null;
	static	private	int		rownum = 0;
	static	private	int		mysleeptime=10;
	static	private	String	sql=null;
	static	private	String	tsus=null;

/**/
	public static void run()	{
		obInterface.setDataSource(dataSource);
		mysql=new Mysql(dataSource);
		mysql.setObInterface(obInterface);
		try	{
			mysql.getRow("P019","pmtp='CMS' and pmky='SMTP' and pmcd='detail'");
		}	catch(Exception e)	{
			System.out.println("error to read parameter");
			return;
		}
		System.out.println("Scan Interval: "+mysleeptime+" Minutes, Ready to Send Email...");
		for	(;;){
			try	{
				//if	(send()<=0)		Thread.sleep(100*1000);
				send();
				Thread.sleep(mysleeptime*1000);
			} catch (Throwable e) {
				CLog.writeLog("Exception="+e.toString());
			}
		}
		//pbQueue=null;
	}


	public static int send()	{
		try	{
			mysql.setObInterface(obInterface);


			Collection	interfaceInfos=mysql.select("D012","flag='E' and bstp>'00' and spct>'0' order by 1,2");
			Iterator		it=interfaceInfos.iterator();
			rownum = obInterface.getRowNum();
			//System.out.println(App.getCurDateStr(19)+" Email:"+obInterface.getRowNum());
			if	(rownum<=0)			return rownum;
			System.out.println(App.getCurDateStr(19)+" Email: "+obInterface.getRowNum());
			//if	(myemail==null)	myemail = new org.apache.commons.mail.HtmlEmail();;
			myemail = new org.apache.commons.mail.HtmlEmail();;

			myemail.setHostName(obInterface.getPmv1());
			myemail.setAuthentication(obInterface.getPmv3(),obInterface.getPmv4());
			myemail.setFrom(obInterface.getPmv2(),obInterface.getPmv5());
			myemail.setSubject(obInterface.getPmnm());
			ObInterface temp1;
			rownum=0;
			for	(int i=1;it.hasNext();i++){
				temp1		=	(ObInterface)it.next();
				try	{
					//System.out.println(App.getCurDateStr(19)+"-Email: "+i+"/"+obInterface.getRowNum());
					myemail.addTo(temp1.getSpct(),temp1.getCunm());
					myemail.setHtmlMsg(temp1.getClnm());
					myemail.send();

				}	catch (org.apache.commons.mail.EmailException ex) {
					CLog.writeLog("EmailException="+ex.toString());
					continue;
				}	catch (Throwable e) {
					CLog.writeLog("Throwable="+e.toString());
					continue;
				}

				mysql.setObInterface(temp1);
				//temp1.setComm(mqsql.getCurDateStr(19));
				//temp1.setBstp("00");
				tsus=temp1.getTsus();
				if	(temp1.getTsus().length()<=0)		tsus=" ";
				sql="update d012 set bstp='00',tsus='"+tsus+"',comm='"+mysql.getCurDateStr(19)+"' where tsdt='"+temp1.getTsdt()+"' and tsrf='"+temp1.getTsrf()+"' and prid='"+temp1.getPrid()+"' and bstp='"+temp1.getBstp()+"' and cuno='"+temp1.getCuno()+"' and flag='"+temp1.getFlag()+"'";
				//if	(temp1.getTsus().length()<=0)		sql+=" and (tsus is null or tsus=' ')";	else	sql+=" and tsus='"+temp1.getTsus()+"'";
				mysql.executeUpdate(sql);
				//CLog.writeLog("sql="+sql);
				CLog.writeLog("Email="+temp1.getSpct()+",Content="+temp1.getClnm());
				System.out.println(ccasws.App.getCurDateStr(19)+" Email="+temp1.getSpct()+",Content="+temp1.getClnm());
				rownum++;
			}

			return rownum;
		}	catch (org.apache.commons.mail.EmailException ex) {
			CLog.writeLog("EmailException="+ex.toString());
		}	catch (Throwable e) {
			CLog.writeLog("Throwable="+e.toString());
		}	finally	{
			myemail = null;
		}

		return rownum;
	}

	public static void main(String[] args) throws Exception {

		try {
			if	(args.length>0)				mysleeptime=Integer.parseInt(args[0]);
			if	(mysleeptime<1)				mysleeptime=10;
		} catch (Exception e) {
			mysleeptime=10;
		}

		try	{
			//ServerSocket ssk = new ServerSocket(serverPort);
			ServerSocketChannel server = ServerSocketChannel.open();
			server.socket().bind(new java.net.InetSocketAddress(serverPort));
			//server.socket().close();

			if	(factory==null)				factory = new XmlBeanFactory(new ClassPathResource(xmlfile));
			if	(dataSource==null)		dataSource=(DataSource)factory.getBean("dataSource");

			//System.out.println(App.getPortInfo());
			System.out.println("Email Server Starting..., Listen Port["+serverPort+"]");
			CLog.writeLog("Email Server Starting..., Listen Port["+serverPort+"]");
			System.out.println("logfile["+CLog.getLogfile()+"]");
			CLog.writeLog("logfile["+CLog.getLogfile()+"]");
			run();

		} catch (java.net.BindException e) {
			System.out.println("Error:Email Server Already Started, Exiting..., Listen Port["+serverPort+"]");
			CLog.writeLog("Error:Email Server Already Started, Exiting..., Listen Port["+serverPort+"]");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}







