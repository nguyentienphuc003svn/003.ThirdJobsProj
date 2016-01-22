package ccasws;

//	jSMSEngine API.
//	An open-source API package for sending and receiving SMS via a GSM device.
//	Copyright (C) 2002-2006, Thanasis Delenikas, Athens/GREECE
//		Web Site: http://www.jsmsengine.org
//
//	jSMSEngine is a package which can be used in order to add SMS processing
//		capabilities in an application. jSMSEngine is written in Java. It allows you
//		to communicate with a compatible mobile phone or GSM Modem, and
//		send / receive SMS messages.
//
//	jSMSEngine is distributed under the LGPL license.
//
//	This library is free software; you can redistribute it and/or
//		modify it under the terms of the GNU Lesser General Public
//		License as published by the Free Software Foundation; either
//		version 2.1 of the License, or (at your option) any later version.
//	This library is distributed in the hope that it will be useful,
//		but WITHOUT ANY WARRANTY; without even the implied warranty of
//		MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//		Lesser General Public License for more details.
//	You should have received a copy of the GNU Lesser General Public
//		License along with this library; if not, write to the Free Software
//		Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//

//
//	SendMessage.java - Sample application.
//
//		This application shows you the basic procedure needed for sending
//		an SMS messages from your GSM device.
//

//	Include the necessary package.
import java.io.InputStream;
import java.util.Properties;

import org.jsmsengine.CMessage;
import org.jsmsengine.COutgoingMessage;
import org.jsmsengine.CService;



public class SmsMessage {
	private Properties props = null;
	private String comport = "COM1";
	private	static CService srv = null;
	private	COutgoingMessage msg=null;
	private	boolean 	retboolean=false;
	private	long stime = System.currentTimeMillis();
/*
*/
	public static final int MAX_SMS_LEN_7BIT = 160;
	public static final int MAX_SMS_LEN_8BIT = 140;
	public static final int MAX_SMS_LEN_UNICODE = 70;

	//public static final int MESSAGE_ENCODING_7BIT = 1;
	//public static final int MESSAGE_ENCODING_8BIT = 2;
	//public static final int MESSAGE_ENCODING_UNICODE = 3;
	public static int myMessage_Encoding = 0;

	public SmsMessage(){
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
		initialize();
	}

	private void initialize(){
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
		try {
			if	(srv!=null){
				if (!srv.getConnected()){
					//System.out.println("SMS connect...");
					//srv.disconnect();
					srv.setSimPin("0000");
					srv.connect();
					System.out.println(ccasws.App.getCurDateStr(19)+"	Signal Level  : "+ srv.getDeviceInfo().getSignalLevel() + "%");
					srv.setSmscNumber("");
					//System.out.println("SMS connect finish");
				}
				return;
			}
			InputStream inputStream = SmsMessage.class.getResourceAsStream("smsserver.properties");
			System.out.println("inputStream=="+inputStream);
			props = new Properties();
			props.load(inputStream);
			comport=props.getProperty("comportwindows");
			if	(System.getProperty("os.name").startsWith("Linux"))		comport=props.getProperty("comportlinux");
			System.out.println(comport+","+Integer.parseInt(props.getProperty("baudrate"))+","+props.getProperty("manufacturer")+","+props.getProperty("model"));
			CLog.writeLog(comport+","+Integer.parseInt(props.getProperty("baudrate"))+","+props.getProperty("manufacturer")+","+props.getProperty("model"));

			CLog.writeLog("init CService...");
			srv = new CService(comport, Integer.parseInt(props.getProperty("baudrate")), props.getProperty("manufacturer"),props.getProperty("model"));
			//Initialize service.
			
			inputStream.close();
			//srv.disconnect();
			srv.setSimPin("0000");
			//if (!srv.getConnected())	srv.connect();
			System.out.println("SMS first connect...");
			srv.connect();
			
			System.out.println("SMS first connect finish");
			CLog.writeLog("Mobile Device Information: ");
			CLog.writeLog("	Manufacturer  : "+ srv.getDeviceInfo().getManufacturer());
			CLog.writeLog("	Model         : "+ srv.getDeviceInfo().getModel());
			CLog.writeLog("	Serial No     : "+ srv.getDeviceInfo().getSerialNo());
			CLog.writeLog("	IMSI          : "+ srv.getDeviceInfo().getImsi());
			CLog.writeLog("	S/W Version   : "+ srv.getDeviceInfo().getSwVersion());
			CLog.writeLog("	Battery Level : "+ srv.getDeviceInfo().getBatteryLevel() + "%");
			CLog.writeLog("	Signal Level  : "+ srv.getDeviceInfo().getSignalLevel() + "%");
			CLog.writeLog("SendMessage(): sample application.");
			CLog.writeLog("	Using " + srv._name + " " + srv._version);
			System.out.println(ccasws.App.getCurDateStr(19)+"	Signal Level  : "+ srv.getDeviceInfo().getSignalLevel() + "%");

			srv.setSmscNumber("");
			CLog.writeLog("init CService suceess");
		} catch (Throwable e) {
			CLog.writeLog("Exception="+e.toString());
		}	finally	{
			props=null;
		}
	}

	private CService getSrv() {
		return srv;
	}

	public void disconnect() {
		if	(srv==null)	return;
		//System.out.println("SMS disconnect...");
		if (srv.getConnected())		srv.disconnect();
	}
	public boolean sendMessages(String mobilePhoneNum, String message)	throws Exception {
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
		//int status;
		stime = System.currentTimeMillis();
		String	temp="";
		
		initialize();
		
		//System.out.println(ccas.App.getCurDateStr(19)+"	sending SMS:"+mobilePhoneNum);
		//System.out.println(ccas.App.getCurDateStr(19)+"	sending SMS:"+mobilePhoneNum+"["+message+"]...");

		//CLog.writeLog();
		int maxLen=65,maxMessageLength=MAX_SMS_LEN_UNICODE;
		myMessage_Encoding = CMessage.MESSAGE_ENCODING_UNICODE;
		if	(message.length()==App.getStringLength(message)){
			//System.out.println("ENCODING is MESSAGE_ENCODING_7BIT,len="+message.length());
			myMessage_Encoding = CMessage.MESSAGE_ENCODING_7BIT;
			maxMessageLength=MAX_SMS_LEN_7BIT;
		}
		maxLen=maxMessageLength-5;
		try {
			for	(int i=0;i<=message.length()/maxLen;i++){
				int	startlen=maxLen*i;
				int	endlen=maxLen*(i+1);
				if	(endlen>=message.length())		endlen=message.length();
				if	(message.length()<maxMessageLength)		endlen=message.length();
				if	(endlen==startlen)	break;
				if	(message.length()>=maxMessageLength)	temp=(i+1)+"/"+(1+message.length()/maxLen)+")";
				retboolean=false;
				temp+=message.substring(startlen,endlen);
				msg = new COutgoingMessage(mobilePhoneNum,temp);
				//System.out.println(ccas.App.getCurDateStr(19)+"	length="+temp.length()+",msg["+temp+"],mobile="+mobilePhoneNum);
				//MESSAGE_ENCODING_7BIT/MESSAGE_ENCODING_8BIT
				msg.setMessageEncoding(myMessage_Encoding);
				retboolean=srv.sendMessage(msg);
				System.out.println("·¢ËÍ³É¹¦¡£¡£¡£¡£");
				if	(message.length()<maxMessageLength)		break;
			}

			return retboolean;

		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println(ccas.App.getCurDateStr(19)+"	length="+temp.length()+",msg["+temp+"],mobile="+mobilePhoneNum);
			retboolean=false;
		}	finally	{
			if	(retboolean){
				CLog.writeLog("mobile:"+mobilePhoneNum+",length=["+message.length()+"],Processing time:"+(System.currentTimeMillis() - stime)+" ms");
				System.out.println(ccasws.App.getCurDateStr(19)+"	mobile:"+mobilePhoneNum+",length=["+message.length()+"],Processing time:"+(System.currentTimeMillis() - stime)+" ms");
			}
		}
		return false;
	}

	public static void main(String[] args) {
		try {
			SmsMessage sendMessage = new SmsMessage();
			//sendMessage.sendMessages("15814515001","test1 for SMS,send time is "+App.getCurDateStr(21));
			//sendMessage.sendMessages("13580501218","test2 for SMS,send time is "+App.getCurDateStr(21));
			sendMessage.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ;
	}
}
