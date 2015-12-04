package ccas;

// SendMessage.java - Sample application.
//
// This application shows you the basic procedure for sending messages.
// You will find how to send synchronous and asynchronous messages.
//
// For asynchronous dispatch, the example application sets a callback
// notification, to see what's happened with messages.



import java.io.InputStream;

import org.smslib.AGateway;
import org.smslib.IOutboundMessageNotification;
import org.smslib.Library;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;

import java.util.Properties;


public class SendMessage
{
	private Properties props = null;
	private String comport = "COM1";
	
	/* this method to start server */
	public boolean sendMessages() throws Exception
	{
		
		//System.out.println("phone: " + phone + "  -msg: " +mobile);	
		OutboundNotification outboundNotification  = null;		
		outboundNotification = new OutboundNotification();
		SerialModemGateway gateway = null;
		
		
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("smsserver.properties");
		props = new Properties();
		props.load(inputStream);
		comport=props.getProperty("comportwindows");
		if	(System.getProperty("os.name").startsWith("Linux"))		
			comport=props.getProperty("comportlinux");
		
		System.out.print("port: " + comport);	
		
		gateway = new SerialModemGateway("modem.com1",comport,9600, "6310i", "");
		
		gateway.setInbound(true);
		gateway.setOutbound(true);
		gateway.setSimPin("0000");		
		gateway.setSmscNumber("");
		Service.getInstance().setOutboundMessageNotification(outboundNotification);
		Service.getInstance().addGateway(gateway);
		
		Service.getInstance().startService();
		System.out.println();
		System.out.println("Modem Information:");
		System.out.println("  Manufacturer: " + gateway.getManufacturer());
		System.out.println("  Model: " + gateway.getModel());
		System.out.println("  Serial No: " + gateway.getSerialNo());
		System.out.println("  SIM IMSI: " + gateway.getImsi());
		System.out.println("  Signal Level: " + gateway.getSignalLevel() + " dBm");
		System.out.println("  Battery Level: " + gateway.getBatteryLevel() + "%");
		System.out.println();
		//OutboundMessage msg = null;
		//msg = new OutboundMessage(mobilePhone, message);
		//boolean flag = false;		
		//flag = Service.getInstance().sendMessage(msg);		
		//Service.getInstance().stopService();

		
		//System.in.read();
		
		return true;
	}
	public  boolean send(String phone, String mesage)throws Exception{
		OutboundMessage msg = new OutboundMessage(phone, mesage);
		return Service.getInstance().sendMessage(msg);
	}
	
	public static void disconected(){
		try{
			Service.getInstance().stopService();
		}catch(Exception ex){
			
		}
	}
	public class OutboundNotification implements IOutboundMessageNotification
	{
		public void process(AGateway gateway, OutboundMessage msg)
		{
			System.out.println("Outbound handler called from Gateway: " + gateway.getGatewayId());
			System.out.println(msg);
			
		}
	}
	
	public static void main(String args[])
	{
			
		SendMessage app = new SendMessage();
		try
		{
			
			app.sendMessages();
			app.send("0988566151","alo1");
			//app.sendMessages("0988566151","alo2");
			
			
			
			//sendMessages("0988566151","test ets aad af sdf ");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.print("aaaaaaaaaaaa" );
		}
		
	}
}
