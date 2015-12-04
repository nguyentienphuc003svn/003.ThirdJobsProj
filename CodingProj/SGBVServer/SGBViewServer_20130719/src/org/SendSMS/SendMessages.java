package org.SendSMS;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import vn.com.saigonbank.smswebservices.MTMsgServiceLocator;
import vn.com.saigonbank.smswebservices.MTMsgServiceSoap;
import vn.com.saigonbank.smswebservices.MTMsgServiceSoapStub;

import _42._1._168._192.SMSLocator;
import _42._1._168._192.SMSSoap;
import _42._1._168._192.SMSSoapStub;

public class SendMessages {
	public static boolean SendMessageFromWebSerVices(String mobile, String content)throws RemoteException {
		
		String mobile1 = mobile.substring(1,mobile.length());
		mobile1 = "84" + mobile1;
		
		System.out.println(mobile1);
		MTMsgServiceSoapStub stb = new MTMsgServiceSoapStub();
		MTMsgServiceLocator location;
		MTMsgServiceSoap  soapService;
		location = new MTMsgServiceLocator();
		try {
			soapService = (MTMsgServiceSoap) location.getMTMsgServiceSoap();
			soapService.sendEx(mobile1,content,"SGB");
			return true;
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	
	}
}
