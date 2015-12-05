package ccas;


import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.channels.ServerSocketChannel;
import sun.misc.*;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.*;
import javax.sql.DataSource;

public class SMSServer{
	static	private	XmlBeanFactory factory=null;
	static	private	DataSource dataSource=null;
	static	private ObInterface obInterface=new ObInterface();
	static	private	int serverPort=5044;
	static	private	final String xmlfile="cmsbeans.xml";
	static	private	Mysql mysql=null;
	static	private	SmsMessage sendMessage = null;
	static	private	int		rownum = 0;
	static	private	String	sql=null;
	static	private	String	tsus=null;

/*
*/
	public static void run()	{
		//System.out.println("进入run()");
		//SmsMessage msg=new SmsMessage();
		obInterface.setDataSource(dataSource);
		mysql=new Mysql(dataSource);
		//mysql.setObInterface(obInterface);
		if	(sendMessage==null)	sendMessage = new SmsMessage();
		for	(;;){
			try	{
				if	(send()<=0)		Thread.sleep(20*1000);
			} catch (Throwable e) {
				CLog.writeLog("Exception="+e.toString());
			}
		}
		
		//send();
		//pbQueue=null;
	}


	public static int send()	{
		try	{
			//System.out.println("进入send()。。。");
			//System.out.println("obInterface==="+obInterface);
			
			mysql.setObInterface(obInterface);
			
			//System.out.println("mysql=="+mysql);			
			//boolean con = mysql.getRowSql( "select * from p007" );			
			//System.out.println("con==="+con);
			
			//Collection	interfaceInfos=mysql.select("D012","flag='P' and bstp>'00' and spct>'0' order by 1,2");
			Collection interfaceInfos=mysql.selectSql( "select * from d012 t1,p007 t2 where t1.flag='P' and t1.bstp>'00' and t1.spct>'0' and t1.spct=t2.usid order by 1,2" );
			
			Iterator		it=interfaceInfos.iterator();
			//System.out.println("it=="+interfaceInfos.size());
			rownum = obInterface.getRowNum();
			if	(rownum<=0)			return rownum;
			if	(sendMessage==null)	sendMessage = new SmsMessage();
			ObInterface temp1;
			rownum=0;
			
			for	(;it.hasNext();){
				temp1		=	(ObInterface)it.next();
				try	{
					if	(temp1.getUscd().length()<6)	continue;
					//Long.parseLong(temp1.getSpct());
					//System.out.println("信息==="+temp1.getClnm()+"    号码==="+temp1.getUscd());
					if	(!sendMessage.sendMessages(temp1.getUscd(),temp1.getClnm()))	continue;
				} catch (Throwable e) {
					CLog.writeLog("Exception="+e.toString()+",mobile["+temp1.getUscd()+"]");
					//sendMessage.disconnect();
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
				CLog.writeLog("sql="+sql);
				CLog.writeLog("Mobile="+temp1.getSpct()+",SMS="+temp1.getClnm());
				rownum++;
			}

			return rownum;
		}	catch (Throwable e) {
			if	(sendMessage!=null)	sendMessage.disconnect();
			sendMessage = null;
		}	finally	{
			if	(sendMessage!=null)	sendMessage.disconnect();
			sendMessage = null;
		}

		return rownum;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("进入。。。。。");
		if	(App.getPortNum()<=0){
			System.out.println("Error:No Serial Port,Exiting...");
			return;
		}
		try	{
			//ServerSocket ssk = new ServerSocket(serverPort);
			ServerSocketChannel server = ServerSocketChannel.open();
			server.socket().bind(new java.net.InetSocketAddress(serverPort));
			//server.socket().close();
			System.setProperty("javax.xml.parsers.DocumentBuilderFactory", 
		       "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl"); 
			System.setProperty("javax.xml.parsers.SAXParserFactory", 
		       "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl"); 
			System.out.println("xmlfile=="+xmlfile);
			if	(factory==null)				factory = new XmlBeanFactory(new ClassPathResource(xmlfile));
			System.out.println("factory=="+factory);
			//factory = new XmlBeanFactory(new ClassPathResource(""));
			if	(dataSource==null)		dataSource=(DataSource)factory.getBean("dataSource");

			//System.out.println(App.getPortInfo());
			System.out.println("SMS Server Starting..., Listen Port["+serverPort+"]");
			CLog.writeLog("SMS Server Starting..., Listen Port["+serverPort+"]");
			System.out.println("logfile["+CLog.getLogfile()+"]");
			CLog.writeLog("logfile["+CLog.getLogfile()+"]");
			run();

		} catch (java.net.BindException e) {
			System.out.println("Error:SMS Server Already Started, Exiting..., Listen Port["+serverPort+"]");
			CLog.writeLog("Error:SMS Server Already Started, Exiting..., Listen Port["+serverPort+"]");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

}







