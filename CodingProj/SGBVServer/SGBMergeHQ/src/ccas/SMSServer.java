package ccas;


import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.nio.channels.ServerSocketChannel;
import sun.misc.*;

import org.SendSMS.SendMessages;
import org.smslib.OutboundMessage;
import org.smslib.Library;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.*;
import java.io.IOException;

import org.smslib.AGateway;
import org.smslib.GatewayException;
import org.smslib.IOutboundMessageNotification;
import org.smslib.Library;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;
import ccas.SendMessage.OutboundNotification;

import javax.sql.DataSource;

public class SMSServer{
	static	private	XmlBeanFactory factory=null;
	static	private	DataSource dataSource=null;
	static	private ObInterface obInterface=new ObInterface();
	static	private	int serverPort=5044;
	static	private	final String xmlfile="cmsbeans.xml";
	static	private	Mysql mysql=null;
	//static	private	SmsMessage sendMessage = null;
	static	private	int		rownum = 0;
	static	private	String	sql=null;
	static	private	String	tsus=null;

	//////////////////////////////////////////
	//static private SendMessage sendMessage = null;


	public static void run() throws Exception	
	{
		obInterface.setDataSource(dataSource);
		mysql=new Mysql(dataSource);

		for	(;;)
		{
			try	
			{
				if	(send()<=0) 
				{
					//Thread.sleep(1000*60*20);
				}
			} 
			catch (Throwable e) {
				CLog.writeLogVS("Exception="+e.toString());
			}
			finally
			{
				obInterface=null;
				//mysql.close();
				if (dataSource.getConnection() != null) dataSource.getConnection().close();
				mysql=null;
			}
		}
	}


	public static int sendSGB()	{

		obInterface.setDataSource(dataSource);
		mysql=new Mysql(dataSource);	
		try	{			

			mysql.setObInterface(obInterface);
			Collection	interfaceInfos=mysql.select("D012","flag='P' and bstp>'00' and spct>'0' order by 1,2");
			Iterator		it=interfaceInfos.iterator();
			rownum = obInterface.getRowNum();

			System.out.println("ROWNUM AT SEND()[" + rownum + "]");
			if	(rownum<=0)			
				return rownum;
			/*
			if	(sendMessage==null)	{
				sendMessage = new SendMessage();//////SmsMessage}
				//sendMessage.sendMessages();

			}*/
			ObInterface temp1;
			rownum=0;
			for	(;it.hasNext();){

				temp1		=	(ObInterface)it.next();
				try	
				{
					System.out.println("UserName: " +temp1.getSpct() + "Message" +temp1.getClnm());					
					if	(temp1.getSpct().length()<9){
						try	{														
							System.out.println("Name At D012 [" + temp1.getSpct()+ "]");
							Collection  interfaceInfo = mysql.select("p007","usid =" +"'" + temp1.getSpct()+ "'" );							
							Iterator iterator = interfaceInfo.iterator();
							ObInterface temp2;
							for	(;iterator.hasNext();)	{																				
								temp2	=	(ObInterface)iterator.next();	
								System.out.println("UserName [" + temp2.getUsid()+ "]");
								System.out.println("Name [" + temp2.getUsnm()+ "]");
								System.out.println("USTP [" + temp2.getUstp()+ "]");
								System.out.println("USBR [" + temp2.getUsbr()+ "]");
								System.out.println("Phone [" + temp2.getUscd()+ "]");
								System.out.println("USGP [" + temp2.getUsgp()+ "]");
								System.out.println("CVCD [" + temp2.getCvcd()+ "]");
								System.out.println("USPW [" + temp2.getUspw()+ "]");
								System.out.println("UserName [" + temp2.getUsid()+ "]");


								try{																															
									if(!org.SendSMS.SendMessages.SendMessageFromWebSerVices(temp2.getUscd(),temp1.getClnm()))continue;
									System.out.println("Mobile: " +temp2.getUscd() + "Message : " + temp1.getClnm());
								}catch(Exception e){
									System.out.println("not found device " + e.getMessage());
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
						if(!org.SendSMS.SendMessages.SendMessageFromWebSerVices(temp1.getSpct(),temp1.getClnm()))continue;
						System.out.println("Mobile " +temp1.getSpct() + "Message : " + temp1.getClnm());
					}

				} catch (Throwable e) {
					CLog.writeLog("Exception="+e.toString()+",mobile["+temp1.getSpct()+"]");
					//sendMessage.disconnect();
					continue;
				}
				mysql.setObInterface(temp1);							
				tsus=temp1.getTsus();
				if	(temp1.getTsus().length()<=0)	tsus=" ";
				sql="update d012 set bstp='00',tsus='"+tsus+"',comm='"+mysql.getCurDateStr(19)+"' where tsdt='"+temp1.getTsdt()+"' and tsrf='"+temp1.getTsrf()+"' and prid='"+temp1.getPrid()+"' and bstp='"+temp1.getBstp()+"' and cuno='"+temp1.getCuno()+"' and flag='"+temp1.getFlag()+"'";				
				mysql.executeUpdate(sql);															

				CLog.writeLog("sql="+sql);
				CLog.writeLog("Mobile="+temp1.getSpct()+",SMS="+temp1.getClnm());				
				rownum++;
			}
			return rownum;
		}	catch (Throwable e) {

			/*
			if	(sendMessage!=null)	
				sendMessage =null;
			 */
		}	finally	{
			/*
			if	(sendMessage!=null)
				sendMessage=null;*/
		}

		return rownum;

	}

	public static int send()	{

		try	{
			mysql.setObInterface(obInterface);
			
			Collection	interfaceInfos=mysql.select("D012","flag='P' and bstp>'00' and spct>'0' order by 1,2");
			Iterator		it=interfaceInfos.iterator();
			rownum = obInterface.getRowNum();

			System.out.println("ROWNUM AT SEND()[" + rownum + "]");
			CLog.writeLogVS("ROWNUM AT SEND()[" + rownum + "]");
			if	(rownum<=0)			
				return rownum;
			
			/*
			if	(sendMessage==null)	{
				sendMessage = new SendMessage();//////SmsMessage}
				//sendMessage.sendMessages();

			}*/
			
			ObInterface temp1;
			rownum=0;
			for	(;it.hasNext();){

				temp1		=	(ObInterface)it.next();
				try	
				{
					System.out.println("UserName: " +temp1.getSpct() + "Message" +temp1.getClnm());
					CLog.writeLogVS("UserName: " +temp1.getSpct() + "Message" +temp1.getClnm());
					
					if	(temp1.getSpct().length()<9){
						try	{														
							System.out.println("Name At D012 [" + temp1.getSpct()+ "]");
							CLog.writeLogVS("Name At D012 [" + temp1.getSpct()+ "]");
							
							Collection  interfaceInfo = mysql.select("p007","usid =" +"'" + temp1.getSpct()+ "'" );							
							Iterator iterator = interfaceInfo.iterator();
							ObInterface temp2;
							for	(;iterator.hasNext();)	{																				
								temp2	=	(ObInterface)iterator.next();	
								CLog.writeLogVS("UserName [" + temp2.getUsid()+ "]");
								CLog.writeLogVS("Name [" + temp2.getUsnm()+ "]");
								CLog.writeLogVS("USTP [" + temp2.getUstp()+ "]");
								CLog.writeLogVS("USBR [" + temp2.getUsbr()+ "]");
								CLog.writeLogVS("Phone [" + temp2.getUscd()+ "]");
								CLog.writeLogVS("USGP [" + temp2.getUsgp()+ "]");
								CLog.writeLogVS("CVCD [" + temp2.getCvcd()+ "]");
								CLog.writeLogVS("USPW [" + temp2.getUspw()+ "]");
								CLog.writeLogVS("UserName [" + temp2.getUsid()+ "]");


								try{																															
									if(!org.SendSMS.SendMessages.SendMessageFromWebSerVices(temp2.getUscd(),temp1.getClnm())) continue;
									CLog.writeLogVS("Successful send to Mobile: " +temp2.getUscd() + "Message : " + temp1.getClnm());
								}catch(Exception e){
									e.printStackTrace();
									
									System.out.println("Cannot send the SMS " + e.getMessage());
									CLog.writeLogVS("Cannot send the SMS " + e.getMessage());
									
									continue;
								}								
							}												
						}
						catch (Throwable e) {
							CLog.writeLogVS("Exception="+e.toString()+",mobile["+temp1.getSpct()+"]");					
							CLog.writeLogVS("kok co viec" + e.getMessage() + e.getStackTrace());
							e.getStackTrace();
							
							continue;
						}
					}
					else{
						if(!org.SendSMS.SendMessages.SendMessageFromWebSerVices(temp1.getSpct(),temp1.getClnm())) continue;
						CLog.writeLogVS("Successful send to Mobile " +temp1.getSpct() + "Message : " + temp1.getClnm());
					}

				} catch (Throwable e) {
					CLog.writeLogVS("Exception="+e.toString()+",mobile["+temp1.getSpct()+"]");
					//sendMessage.disconnect();
					continue;
				}
				
				mysql.setObInterface(temp1);							
				tsus=temp1.getTsus();
				if	(temp1.getTsus().length()<=0)	tsus=" ";
				sql="update d012 set bstp='00',tsus='"+tsus+"',comm='"+mysql.getCurDateStr(19)+"' where tsdt='"+temp1.getTsdt()+"' and tsrf='"+temp1.getTsrf()+"' and prid='"+temp1.getPrid()+"' and bstp='"+temp1.getBstp()+"' and cuno='"+temp1.getCuno()+"' and flag='"+temp1.getFlag()+"'";				
				mysql.executeUpdate(sql);															

				CLog.writeLogVS("sql="+sql);
				CLog.writeLogVS("Mobile="+temp1.getSpct()+",SMS="+temp1.getClnm());				
				rownum++;
			}
			
			return rownum;
		}	
		catch (Throwable e) {

			/*
			if	(sendMessage!=null)	
				sendMessage =null;
			 */
		}	
		finally	{
			/*
			if	(sendMessage!=null)
				sendMessage=null;*/
		}

		return rownum;

	}



	public static void mainSGB(String[] args) throws Exception 
	{

		try	
		{

			if	(factory==null)				
				factory = new XmlBeanFactory(new ClassPathResource(xmlfile));
			if	(dataSource==null)		
				dataSource=(DataSource)factory.getBean("dataSource");


			CLog.writeLogVS("SMS Server Starting..., Listen Port["+serverPort+"]");
			System.out.println("SMS Server Starting..., Listen Port["+serverPort+"]");
			CLog.writeLogVS("logfile["+CLog.getLogfile()+"]");
			System.out.println("logfile["+CLog.getLogfile()+"]");
			System.out.println("SMS Server is Starting Sucess");
			CLog.writeLogVS("SMS Server is Starting Sucess");

			//run();
			SMSServer sv = new SMSServer();
			sv.run();
		} 
		catch (java.net.BindException e) 
		{
			System.out.println("Error:SMS Server Already Started, Exiting..., Listen Port["+serverPort+"]");
			CLog.writeLogVS("Error:SMS Server Already Started, Exiting..., Listen Port["+serverPort+"]");

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Start the main of SMS Server");
		CLog.writeLogVS("Start the main of SMS Server");

		if	(App.getPortNum()<=0)
		{
			System.out.println("Error: No Serial Port, Exiting...");
			CLog.writeLogVS("Error: No Serial Port, Exiting...");
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
			CLog.writeLogVS("factory=="+factory);
			//factory = new XmlBeanFactory(new ClassPathResource(""));
			if	(dataSource==null)		dataSource=(DataSource)factory.getBean("dataSource");

			//System.out.println(App.getPortInfo());
			System.out.println("SMS Server Starting..., Listen Port["+serverPort+"]");
			CLog.writeLogVS("SMS Server Starting..., Listen Port["+serverPort+"]");
			System.out.println("logfile["+CLog.getLogfile()+"]");
			CLog.writeLogVS("logfile["+CLog.getLogfile()+"]");

			run();

		} catch (java.net.BindException e) {
			System.out.println("Error:SMS Server Already Started, Exiting..., Listen Port["+serverPort+"]");
			CLog.writeLogVS("Error:SMS Server Already Started, Exiting..., Listen Port["+serverPort+"]");
		} catch (Exception e) {
			e.printStackTrace();

			System.out.println(e.toString());
			CLog.writeLogVS(e.toString());
		}
	}



}

