package ccas;


import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.channels.ServerSocketChannel;
import sun.misc.*;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.*;
import javax.sql.DataSource;

public class SMSServer3{
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
		//pbQueue=null;
	}


	public static int send()	{
		try	{
			mysql.setObInterface(obInterface);
			Collection	interfaceInfos=mysql.select("D012","flag='P' and bstp>'00' and spct>'0' order by 1,2");
			Iterator		it=interfaceInfos.iterator();
			rownum = obInterface.getRowNum();
			if	(rownum<=0)			return rownum;
			if	(sendMessage==null)	sendMessage = new SmsMessage();
			ObInterface temp1;
			rownum=0;
			for	(;it.hasNext();){
				temp1		=	(ObInterface)it.next();
				try	{
					/*
					if	(temp1.getSpct().length()<6)	continue;
					//Long.parseLong(temp1.getSpct());
					if	(!sendMessage.sendMessages(temp1.getSpct(),temp1.getClnm()))	continue;
					*/if	(temp1.getSpct().length()<6){
						try	{														
							Collection  interfaceInfo = mysql.select("p007","usid =" +"'" + temp1.getSpct()+ "'" );							
							Iterator iterator = interfaceInfo.iterator();
							ObInterface temp2;
							for	(;iterator.hasNext();)	{																				
								temp2	=	(ObInterface)iterator.next();												
								try{																															
									if(!sendMessage.sendMessages(temp2.getUscd(),temp1.getClnm()))continue;
									//System.out.println("Mobile: " +temp2.getUscd() + "Message : " + temp1.getClnm());
								}catch(Exception e){
									//System.out.println("not found device " + e.getMessage());
									continue;
								}								
							}												
						}catch (Throwable e) {
							CLog.writeLog("Exception="+e.toString()+",mobile["+temp1.getSpct()+"]");					
							System.out.print("kok co viec" + e.getMessage() + e.getStackTrace());
							e.getStackTrace();
							continue;
						}
					}else{
						 if(!sendMessage.sendMessages(temp1.getSpct(),temp1.getClnm())) continue;
						 System.out.println("Mobile " +temp1.getSpct() + "Message : " + temp1.getClnm());
					}
					
				} catch (Throwable e) {
					CLog.writeLog("Exception="+e.toString()+",mobile["+temp1.getSpct()+"]");
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
		if	(App.getPortNum()<=0){
			System.out.println("Error:No Serial Port,Exiting...");
			return;
		}
		try	{
			//ServerSocket ssk = new ServerSocket(serverPort);
			ServerSocketChannel server = ServerSocketChannel.open();
			server.socket().bind(new java.net.InetSocketAddress(serverPort));
			//server.socket().close();

			if	(factory==null)				factory = new XmlBeanFactory(new ClassPathResource(xmlfile));
			if	(dataSource==null)		dataSource=(DataSource)factory.getBean("dataSource");

			System.out.println(App.getPortInfo());
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







