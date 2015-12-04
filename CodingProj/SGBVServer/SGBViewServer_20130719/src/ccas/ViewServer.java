package	ccas;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.nio.channels.ServerSocketChannel;

import org.jpos.iso.*;
import org.jpos.iso.channel.*;
import org.jpos.iso.packager.*;

import org.jpos.util.*;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import javax.swing.text.DateFormatter;

public class ViewServer extends	Thread	
{
	private	final static int TCPports=8887;
	private	static int mytimeout=20;
	private ServerSocket serverSocket;
	//线程池
	private ExecutorService executorService;
	//单个CPU线程池大小
	private final int POOL_SIZE=20;

	public ViewServer() throws IOException
	{
		serverSocket=new ServerSocket(TCPports);
		//Runtime的availableProcessor()方法返回当前系统的CPU数目.
		executorService=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*POOL_SIZE);
		System.out.println("CPU Number:"+Runtime.getRuntime().availableProcessors());
		System.out.println("TCP SocketServer running...Ports:"+TCPports);
	}

	public void run(){
		new	ViewUnpack();
		while(true){
			try {
				//接收客户连接,只要客户进行了连接,就会触发accept();从而建立连接
				Socket mysocket=serverSocket.accept();
				executorService.execute(new Handler(mysocket));
				
				////////--------
//				ThreadMngr tm = new ThreadMngr();
//				if (!tm.CheckAliveThread())
//				{
//					tm.StopAllThread();
//					tm.StartAllThread();
//				}
				////////--------


			} 
			catch (Exception e) 
			{
				CLog.writeLogVS("Exception at ViewServer run");
				CLog.writeLogVS(e.toString());
				CLog.writeLogVS(e.getMessage());
				
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[]) 
	{

		try 
		{
			if	(args.length>0)				mytimeout=Integer.parseInt(args[0]);
		} 
		catch (Exception e) {}

		try	
		{
			ServerSocketChannel server = ServerSocketChannel.open();
			server.socket().bind(new java.net.InetSocketAddress(TCPports));
			server.socket().close();

		} 
		catch (java.net.BindException e) 
		{
			System.out.println("Error:VIEW Server Already Started, Exiting..., Listen Port["+TCPports+"]");
			CLog.writeLogVS("Error:VIEW Server Already Started, Exiting..., Listen Port["+TCPports+"]");
			System.exit(1);
		} 
		catch (Exception e) 
		{
			CLog.writeLogVS(e.toString());
			CLog.writeLogVS(e.getMessage());

			e.printStackTrace();
			System.exit(1);
		}

		try {
			//new ViewServer().service();
			//ViewServer	TCPserver	=	new	ViewServer();
			//TCPserver.start();
			
			////////--------
			//new ViewServer().start();
			Thread s = new Thread (new ViewServer());
			s.setName("ViewServerThread");
			s.start();
			
			
			////////--------
			UDPServer	UDPserver	=	new	UDPServer();
			Thread t = new Thread (UDPserver);
			t.setName("GetDataThread");
			t.start();
			System.out.println("Auto Scan times: "+mytimeout+" Minutes, ViewServer running...\n");

			
			////////--------
			//DaemonThread daemonThread = new DaemonThread(mytimeout);
			//daemonThread.setDaemon(true);
			//daemonThread.start();
			Thread o = new Thread (new DaemonThread(mytimeout));
			o.setName("OfflineCheckThread");
			o.setDaemon(true);
			o.start();


			////////--------
			//EventThread EventThread = new EventThread();
			//EventThread.setDaemon(true);
			//EventThread.start();
			Thread e = new Thread (new EventThread());
			e.setName("EventConvertThread");
			e.setDaemon(true);
			e.start();
			
			
			////////--------
//			Thread InSess = new Thread (new InSessThread());
//			InSess.setName("InvalidSessionThread");
//			InSess.setDaemon(true);
//			InSess.start();
			
			
			////////--------
			boolean l_bIsDaemon = true;
			Timer l_oTimer = new Timer(l_bIsDaemon);
			InSessThread is = new InSessThread();
			l_oTimer.schedule(is, 1*60*1000, 3*60*1000);//1*60*1000, 3*60*1000


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class Handler implements Runnable{
	private Socket mysocket;
	public Handler(Socket mysocket){
		this.mysocket=mysocket;
	}
	private PrintWriter getWriter(Socket mysocket) throws IOException{
		OutputStream socketOut=mysocket.getOutputStream();
		return new PrintWriter(socketOut,true);
	}
	private BufferedReader getReader(Socket mysocket) throws IOException{
		InputStream socketIn=mysocket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));
	}
	public String echo(String msg){
		return "echo:"+msg;
	}
	public void run(){
		//new	ViewUnpack();
		try {
			BufferedReader br=getReader(mysocket);
			//PrintWriter pw=getWriter(mysocket);
			String msg=null;
			String mydata="";
			while((msg=br.readLine())!=null){
				//System.out.println(msg);
				//pw.println(echo(msg));
				mydata+=msg;
			}
			System.out.println(App.getCurDateStr(19)+" TCP:Receive Data...len:"+mydata.length()+",come from:"+mysocket.getInetAddress()+":"+mysocket.getPort());
			CLog.writeLogVS("TCP:Receive Data...len:"+mydata.length()+",come from:"+mysocket.getInetAddress()+":"+mysocket.getPort());
			new	ViewUnpack(mydata);
			//System.out.println("New connection accepted "+mysocket.getInetAddress()+":"+mysocket.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
			try {
				if(mysocket!=null)	mysocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}



class	UDPServer	extends	Thread	
{
	private	final static int UDPports=8888;
	DatagramSocket	ServerSocket;
	
	public	UDPServer()	
	{
		super("UDPServer");
		
		try		
		{
			ServerSocket	=	new	DatagramSocket(UDPports);
			ServerSocket.setReuseAddress(true);
			ServerSocket.setSoTimeout(1000*60);

			System.out.println("UDP Server Running ... Ports: " + UDPports + " Socket Time Out <StatusHandle> " + ServerSocket.getSoTimeout());
			CLog.writeLogVS("UDP Server Running ... Ports: " + UDPports + " Socket Time Out <StatusHandle> " + ServerSocket.getSoTimeout());
			
		}	
		catch	(SocketException	e)		
		{
			System.err.println("Exception: couldn't create Datagram Socket");
			CLog.writeLogVS("Exception: couldn't create Datagram Socket");
			CLog.writeLogVS(e.toString());
			CLog.writeLogVS(e.getMessage());
			
			System.exit(1);
		}
	}
	
	public	static	void	main(String[]	args)	
	{
		UDPServer	server	=	new	UDPServer();
		server.start();
	}
	
	public	void	run()		
	{
		if	(ServerSocket	==	null)			return;
		new	ViewUnpack();
		byte[]	bdata	=	new	byte[4*1024];
		
		while	(true)		
		{
			try		
			{
				DatagramPacket	mypacket=new	DatagramPacket(bdata,bdata.length);
				ServerSocket.receive(mypacket);
				CLog.writeLogVS("Socket time out <StatusHandle>: " + ServerSocket.getSoTimeout());

				System.out.println(App.getCurDateStr(19)+" UDP:Receive Data ... Len: " +mypacket.getLength()+ ", Come From: " +mypacket.getAddress()+ ":" +mypacket.getPort());
				CLog.writeLogVS(App.getCurDateStr(19)+" UDP:Receive Data ... Len: " +mypacket.getLength()+ ", Come From: " +mypacket.getAddress()+ ":" +mypacket.getPort());
				
				byte[]	mydata	=	new	byte[mypacket.getLength()];
				System.out.println("OK ! CAN CREATE ARRAY MYDATA");
				CLog.writeLogVS("OK ! CAN CREATE ARRAY MYDATA");
				
				System.arraycopy(mypacket.getData(),mypacket.getOffset(),mydata, 0,mypacket.getLength());
				System.out.println("OK ! CAN COPY MYPACKET TO MYDATA, LENGTH MYDATA IS: [" + mydata.length + "]");
				CLog.writeLogVS("OK ! CAN COPY MYPACKET TO MYDATA, LENGTH MYDATA IS: [" + mydata.length + "]");
				
				//Try to print the mydata
				if (mydata.length > 0)
				{
					System.out.println("OK ! THIS IS UR MYDATA ARRAY");
					CLog.writeLogVS("OK ! THIS IS UR MYDATA ARRAY");
					for (int m=0; m < mydata.length; m++)
					{
						//System.out.println("OK ! ELEMENT [" + m +"] is [" + mydata[m] + "]");
					}
				}
				
				System.out.println("OK ! CAN START TO INSERT");
				CLog.writeLogVS("OK ! CAN START TO INSERT");
				//CLog.writeLogVS(App.byte2hex(mydata));
				
				//----
				//new	ViewUnpack(new String(mydata));		
				
				Thread sh = new Thread (new StatusHandle(new String(mydata).trim()));
				sh.setName("StatusHandleThread");
				//sh.setDaemon(true);
				sh.start();
				//----
				
				////////--------
//				ThreadMngr tm = new ThreadMngr();
//				if (!tm.CheckAliveThread())
//				{
//					tm.StopAllThread();
//					tm.StartAllThread();
//				}
				////////--------

			}	
			catch	(Exception	e)			
			{
				CLog.writeLogVS("Exception at UDBServer run");
				CLog.writeLogVS(e.toString());
				CLog.writeLogVS(e.getMessage());
				
				e.printStackTrace();
			}
		}
	}
}



class ViewUnpack 	extends	Thread	{
	private static String path = System.getProperty("user.dir");
	private	static String xmlfile= "cmsbeans.xml";
	
//	public static XmlBeanFactory factory=null;
//	private static DataSource dataSource=null;
//	private static Mysql mysql=null;
	public XmlBeanFactory factory=null;
	public	DataSource dataSource=null;
	public	Mysql mysql=null;
	
	private	String mypackage=null;
	public static final String US = Character.toString((char) 0x1F);
	
//	static	
//	{
//		if	(factory==null)				factory=new XmlBeanFactory(new ClassPathResource(xmlfile));
//		if	(dataSource==null)		dataSource=(DataSource)factory.getBean("dataSource");
//	}

	public ViewUnpack()	{}

	public ViewUnpack(String mypackage)	throws Exception	{
		System.out.println("THE DB CONNECTION LOCATION IS [" + xmlfile + "]");
		insert(mypackage);
	}
	public ViewUnpack(String mydatetime,int myflag)	throws Exception	{
		update(mydatetime);
	}
	public ViewUnpack(int myflag)	throws Exception	{
		event(myflag);
	}

	public boolean insert(String mypackage)//Default no synchronized 
	{
		CLog.writeLogVS(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
		CLog.writeLogVS(mypackage.split(US, -1).length+",package["+mypackage+"]"+mypackage.length());
		
		if	(mypackage==null || mypackage.length()<10 || mypackage.split(US, -1).length<6)
		{
			CLog.writeLogVS("UR PKG IS NOT OK, PLS CHECK");
			CLog.writeLogVS("LENGTH = [" + mypackage.length() + "] ----- SPLITTED PART(S) [" + mypackage.split(US, -1).length + "]");
			return false;

		}
		System.out.println("LENGTH = [" + mypackage.length() + "] ----- SPLITTED PART(S) [" + mypackage.split(US, -1).length + "]");
		CLog.writeLogVS("LENGTH = [" + mypackage.length() + "] ----- SPLITTED PART(S) [" + mypackage.split(US, -1).length + "]");

		
		System.out.println("OK ! WE CAN GO TO INSERT METHOD");
		CLog.writeLogVS("OK ! WE CAN GO TO INSERT METHOD");
		for	(int j=0;j<mypackage.split(US, -1).length;j++)
		{
			System.out.println("==="+j+",["+mypackage.split(US, -1)[j]+"]");
			CLog.writeLogVS("==="+j+",["+mypackage.split(US, -1)[j]+"]");

		}
		
		long stime = System.currentTimeMillis();
		ObInterface obInterface=new ObInterface();
		mypackage+=US+"0"+US+"0"+US+"0";
		
		try 
		{
			CLog.writeLogVS("header="+mypackage.split(US, -1)[0]+"|"+mypackage.split(US, -1)[1]);

			try 
			{
				if	(factory==null) factory=new XmlBeanFactory(new ClassPathResource(xmlfile));
				if	(dataSource==null) dataSource=(DataSource)factory.getBean("dataSource");
				if	(mysql==null) mysql=new Mysql(dataSource);				 
				
//				dataSource=(DataSource)factory.getBean("dataSource");
//				mysql=new Mysql(dataSource);
				obInterface.setDataSource(dataSource);
			}	
			catch (Exception e)	
			{
				//dataSource=(DataSource)factory.getBean("dataSource");
				//mysql=new Mysql(dataSource);
				//obInterface.setDataSource(dataSource);
				
				CLog.writeLogVS("GET CONNECTION AT INSERT FAIL");
				CLog.writeLogVS(e.toString());
				e.printStackTrace();
				
				obInterface=null;
				mysql=null;
				
				return false;
			}
			mysql.setObInterface(obInterface);
			//设备状态数据
			
			//////////0002 MSG///
			if	(mypackage.split(US, -1)[0].trim().indexOf("0002")>=0)
			{
				//--------Time Declaration
				String TXNTime = "";
				String CLoadTime ="";
				
				//---------This try block for the cash load time and the transaction time
				try 
				{
					obInterface.setTmid(mypackage.split(US, -1)[3]);
					//obInterface.setMudt(mypackage.split(US, -1)[47]+"$ $ $ $ $ $");
					
					//----Java will take the current time
					//String field47 = mypackage.split(US, -1)[47].trim();
					//System.out.println("###THE POSITION 47 VALIE IS [" + field47 + "]");					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date resultdate = new Date(System.currentTimeMillis());
					System.out.println("### THE SYSTEM TIME IS [" + sdf.format(resultdate) + "]");
					
					obInterface.setMudt(sdf.format(resultdate));
					//obInterface.setMudt(mypackage.split(US, -1)[47]);
					//obInterface.setMudt(App.transDateFormat(mypackage.split(US, -1)[47],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss"));
					//-----------------------------
					
					//------------------
					CLoadTime = App.transDateFormat(mypackage.split(US, -1)[47],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss");
					TXNTime = App.transDateFormat(mypackage.split(US, -1)[48],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss");
					obInterface.setMudt(TXNTime.trim());
					
					System.out.println("THE STATUS REPORT TIME IS [" + obInterface.getMudt() + "] AND THE CASHLOAD TIME IS [" + CLoadTime + "]");
					CLog.writeLogVS("THE STATUS REPORT TIME IS [" + obInterface.getMudt() + "] AND THE CASHLOAD TIME IS [" + CLoadTime + "]");
					
					
				}
				catch (Exception pe)
				{
					System.out.println("Mudt ERROR");
					System.out.println(pe.toString());
					
					CLog.writeLogVS("Mudt ERROR");
					CLog.writeLogVS(pe.toString());
					
					
					pe.printStackTrace();
				}
				//------------------
				
				try 
				{
					//-----Get the current system time
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date resultdate = new Date(System.currentTimeMillis());
					//System.out.println("### THE SYSTEM TIME IS [" + sdf.format(resultdate) + "]");
					//-----End Get
					
					
					//--------Update for device status
					String mnstatus = mypackage.split(US, -1)[4];
					if (!mnstatus.equals("0") && !mnstatus.equals("1"))
					{
						mnstatus = "0";
					}
					
					
					//--------Update TML_STATUS
					mysql.delete("T_TML_STATUS","C_TERMID='"+obInterface.getTmid()+"'");
					
					CLog.writeLogVS("insert into T_TML_STATUS values('"+obInterface.getTmid()+"','"+mnstatus+"','"+obInterface.getMudt()+"','"+mypackage.split(US, -1)[6]+"','"+mypackage.split(US, -1)[7]+"','"+mypackage.split(US, -1)[8]+"','"+mypackage.split(US, -1)[9]+"','"+mypackage.split(US, -1)[10]+"','"+mypackage.split(US, -1)[11]+"','"+mypackage.split(US, -1)[12]+"','"+mypackage.split(US, -1)[13]+"','"+mypackage.split(US, -1)[14]+"','"+mypackage.split(US, -1)[15]+"','"+mypackage.split(US, -1)[16]+"','"+mypackage.split(US, -1)[17]+"','"+mypackage.split(US, -1)[18]+"','"+mypackage.split(US, -1)[19]+"','"+mypackage.split(US, -1)[20]+"','"+mypackage.split(US, -1)[21]+"','"+mypackage.split(US, -1)[22]+"','"+mypackage.split(US, -1)[23]+"','"+mypackage.split(US, -1)[24]+"','"+mypackage.split(US, -1)[25]+"','"+mypackage.split(US, -1)[26]+"','"+mypackage.split(US, -1)[27]+"','"+mypackage.split(US, -1)[28]+"','"+mypackage.split(US, -1)[29]+"','"+mypackage.split(US, -1)[30]+"','"+mypackage.split(US, -1)[31]+"','"+mypackage.split(US, -1)[32]+"','"+mypackage.split(US, -1)[33]+"','"+mypackage.split(US, -1)[34]+"','"+mypackage.split(US, -1)[35]+"','"+mypackage.split(US, -1)[36]+"','"+mypackage.split(US, -1)[37]+"','"+mypackage.split(US, -1)[38]+"','','','"+mypackage.split(US, -1)[39]+"','0','"+mypackage.split(US, -1)[49]+"','"+mypackage.split(US, -1)[40].split("\\$").length+"','"+mypackage.split(US, -1)[40].split("\\$")[0]+"','"+mypackage.split(US, -1)[43].split("\\$")[0]+"','"+mypackage.split(US, -1)[42].split("\\$")[0]+"','"+mypackage.split(US, -1)[46].split("\\$")[0]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[1]+"','"+mypackage.split(US, -1)[43].split("\\$")[1]+"','"+mypackage.split(US, -1)[42].split("\\$")[1]+"','"+mypackage.split(US, -1)[46].split("\\$")[1]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[2]+"','"+mypackage.split(US, -1)[43].split("\\$")[2]+"','"+mypackage.split(US, -1)[42].split("\\$")[2]+"','"+mypackage.split(US, -1)[46].split("\\$")[2]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[3]+"','"+mypackage.split(US, -1)[43].split("\\$")[3]+"','"+mypackage.split(US, -1)[42].split("\\$")[3]+"','"+mypackage.split(US, -1)[46].split("\\$")[3]+"','"+obInterface.getMudt()+"','0','0')");
					mysql.executeUpdate("insert into T_TML_STATUS values('"+obInterface.getTmid()+"','"+mnstatus+"','"+obInterface.getMudt()+"','"+mypackage.split(US, -1)[6]+"','"+mypackage.split(US, -1)[7]+"','"+mypackage.split(US, -1)[8]+"','"+mypackage.split(US, -1)[9]+"','"+mypackage.split(US, -1)[10]+"','"+mypackage.split(US, -1)[11]+"','"+mypackage.split(US, -1)[12]+"','"+mypackage.split(US, -1)[13]+"','"+mypackage.split(US, -1)[14]+"','"+mypackage.split(US, -1)[15]+"','"+mypackage.split(US, -1)[16]+"','"+mypackage.split(US, -1)[17]+"','"+mypackage.split(US, -1)[18]+"','"+mypackage.split(US, -1)[19]+"','"+mypackage.split(US, -1)[20]+"','"+mypackage.split(US, -1)[21]+"','"+mypackage.split(US, -1)[22]+"','"+mypackage.split(US, -1)[23]+"','"+mypackage.split(US, -1)[24]+"','"+mypackage.split(US, -1)[25]+"','"+mypackage.split(US, -1)[26]+"','"+mypackage.split(US, -1)[27]+"','"+mypackage.split(US, -1)[28]+"','"+mypackage.split(US, -1)[29]+"','"+mypackage.split(US, -1)[30]+"','"+mypackage.split(US, -1)[31]+"','"+mypackage.split(US, -1)[32]+"','"+mypackage.split(US, -1)[33]+"','"+mypackage.split(US, -1)[34]+"','"+mypackage.split(US, -1)[35]+"','"+mypackage.split(US, -1)[36]+"','"+mypackage.split(US, -1)[37]+"','"+mypackage.split(US, -1)[38]+"','','','"+mypackage.split(US, -1)[39]+"','0','"+mypackage.split(US, -1)[49]+"','"+mypackage.split(US, -1)[40].split("\\$").length+"','"+mypackage.split(US, -1)[40].split("\\$")[0]+"','"+mypackage.split(US, -1)[43].split("\\$")[0]+"','"+mypackage.split(US, -1)[42].split("\\$")[0]+"','"+mypackage.split(US, -1)[46].split("\\$")[0]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[1]+"','"+mypackage.split(US, -1)[43].split("\\$")[1]+"','"+mypackage.split(US, -1)[42].split("\\$")[1]+"','"+mypackage.split(US, -1)[46].split("\\$")[1]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[2]+"','"+mypackage.split(US, -1)[43].split("\\$")[2]+"','"+mypackage.split(US, -1)[42].split("\\$")[2]+"','"+mypackage.split(US, -1)[46].split("\\$")[2]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[3]+"','"+mypackage.split(US, -1)[43].split("\\$")[3]+"','"+mypackage.split(US, -1)[42].split("\\$")[3]+"','"+mypackage.split(US, -1)[46].split("\\$")[3]+"','"+obInterface.getMudt()+"','0','0')");
					//mysql.executeUpdate("insert into T_TML_STATUS values('"+obInterface.getTmid()+"','"+mnstatus+"','"+sdf.format(resultdate)+"','"+mypackage.split(US, -1)[6]+"','"+mypackage.split(US, -1)[7]+"','"+mypackage.split(US, -1)[8]+"','"+mypackage.split(US, -1)[9]+"','"+mypackage.split(US, -1)[10]+"','"+mypackage.split(US, -1)[11]+"','"+mypackage.split(US, -1)[12]+"','"+mypackage.split(US, -1)[13]+"','"+mypackage.split(US, -1)[14]+"','"+mypackage.split(US, -1)[15]+"','"+mypackage.split(US, -1)[16]+"','"+mypackage.split(US, -1)[17]+"','"+mypackage.split(US, -1)[18]+"','"+mypackage.split(US, -1)[19]+"','"+mypackage.split(US, -1)[20]+"','"+mypackage.split(US, -1)[21]+"','"+mypackage.split(US, -1)[22]+"','"+mypackage.split(US, -1)[23]+"','"+mypackage.split(US, -1)[24]+"','"+mypackage.split(US, -1)[25]+"','"+mypackage.split(US, -1)[26]+"','"+mypackage.split(US, -1)[27]+"','"+mypackage.split(US, -1)[28]+"','"+mypackage.split(US, -1)[29]+"','"+mypackage.split(US, -1)[30]+"','"+mypackage.split(US, -1)[31]+"','"+mypackage.split(US, -1)[32]+"','"+mypackage.split(US, -1)[33]+"','"+mypackage.split(US, -1)[34]+"','"+mypackage.split(US, -1)[35]+"','"+mypackage.split(US, -1)[36]+"','"+mypackage.split(US, -1)[37]+"','"+mypackage.split(US, -1)[38]+"','','','"+mypackage.split(US, -1)[39]+"','0','"+mypackage.split(US, -1)[49]+"','"+mypackage.split(US, -1)[40].split("\\$").length+"','"+mypackage.split(US, -1)[40].split("\\$")[0]+"','"+mypackage.split(US, -1)[43].split("\\$")[0]+"','"+mypackage.split(US, -1)[42].split("\\$")[0]+"','"+mypackage.split(US, -1)[46].split("\\$")[0]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[1]+"','"+mypackage.split(US, -1)[43].split("\\$")[1]+"','"+mypackage.split(US, -1)[42].split("\\$")[1]+"','"+mypackage.split(US, -1)[46].split("\\$")[1]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[2]+"','"+mypackage.split(US, -1)[43].split("\\$")[2]+"','"+mypackage.split(US, -1)[42].split("\\$")[2]+"','"+mypackage.split(US, -1)[46].split("\\$")[2]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[3]+"','"+mypackage.split(US, -1)[43].split("\\$")[3]+"','"+mypackage.split(US, -1)[42].split("\\$")[3]+"','"+mypackage.split(US, -1)[46].split("\\$")[3]+"','"+obInterface.getMudt()+"','0','0')");
					//mysql.executeUpdate("insert into T_TML_STATUS values('"+obInterface.getTmid()+"','"+mypackage.split(US, -1)[4]+"','"+mypackage.split(US, -1)[48]+"','"+mypackage.split(US, -1)[6]+"','"+mypackage.split(US, -1)[7]+"','"+mypackage.split(US, -1)[8]+"','"+mypackage.split(US, -1)[9]+"','"+mypackage.split(US, -1)[10]+"','"+mypackage.split(US, -1)[11]+"','"+mypackage.split(US, -1)[12]+"','"+mypackage.split(US, -1)[13]+"','"+mypackage.split(US, -1)[14]+"','"+mypackage.split(US, -1)[15]+"','"+mypackage.split(US, -1)[16]+"','"+mypackage.split(US, -1)[17]+"','"+mypackage.split(US, -1)[18]+"','"+mypackage.split(US, -1)[19]+"','"+mypackage.split(US, -1)[20]+"','"+mypackage.split(US, -1)[21]+"','"+mypackage.split(US, -1)[22]+"','"+mypackage.split(US, -1)[23]+"','"+mypackage.split(US, -1)[24]+"','"+mypackage.split(US, -1)[25]+"','"+mypackage.split(US, -1)[26]+"','"+mypackage.split(US, -1)[27]+"','"+mypackage.split(US, -1)[28]+"','"+mypackage.split(US, -1)[29]+"','"+mypackage.split(US, -1)[30]+"','"+mypackage.split(US, -1)[31]+"','"+mypackage.split(US, -1)[32]+"','"+mypackage.split(US, -1)[33]+"','"+mypackage.split(US, -1)[34]+"','"+mypackage.split(US, -1)[35]+"','"+mypackage.split(US, -1)[36]+"','"+mypackage.split(US, -1)[37]+"','"+mypackage.split(US, -1)[38]+"','','','"+mypackage.split(US, -1)[39]+"','0','"+mypackage.split(US, -1)[49]+"','"+mypackage.split(US, -1)[40].split("\\$").length+"','"+mypackage.split(US, -1)[40].split("\\$")[0]+"','"+mypackage.split(US, -1)[43].split("\\$")[0]+"','"+mypackage.split(US, -1)[42].split("\\$")[0]+"','"+mypackage.split(US, -1)[46].split("\\$")[0]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[1]+"','"+mypackage.split(US, -1)[43].split("\\$")[1]+"','"+mypackage.split(US, -1)[42].split("\\$")[1]+"','"+mypackage.split(US, -1)[46].split("\\$")[1]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[2]+"','"+mypackage.split(US, -1)[43].split("\\$")[2]+"','"+mypackage.split(US, -1)[42].split("\\$")[2]+"','"+mypackage.split(US, -1)[46].split("\\$")[2]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[3]+"','"+mypackage.split(US, -1)[43].split("\\$")[3]+"','"+mypackage.split(US, -1)[42].split("\\$")[3]+"','"+mypackage.split(US, -1)[46].split("\\$")[3]+"','"+obInterface.getMudt()+"','0','0')");
					
					
					//--------Update for each cashbox
					mysql.delete("T_TML_CASHBOX", (new StringBuilder("C_TERMID='")).append(obInterface.getTmid()).append("'").toString());
					for	(int j=0;j<mypackage.split(US, -1)[40].split("\\$").length;j++)
					{
						//Try to insert new DEFAULT cashbox
						System.out.println("OK ! CASHBOX ADDING IS [" + j + "]");
						CLog.writeLogVS("OK ! CASHBOX ADDING IS [" + j + "]");

						try
						{
							//if (obInterface.getBnum() == "N")
							//obInterface.setBnum() = 0;
							CLog.writeLogVS("MAX NO " + obInterface.getBnum());
							mysql.executeUpdate((new StringBuilder("insert into T_TML_CASHBOX values('")).append(obInterface.getTmid()).append("','").append(j + 1).append("','0','0','0','0','0','0','").append(obInterface.getBnum()).append("','0','0','0','0')").toString());
						}
						catch (Exception e) 
						{
							System.out.println("ERROR IS ADDING DEFAULT DATA INTO CASHBOX");
							System.out.println(e.toString());
							
							CLog.writeLogVS("ERROR IS ADDING DEFAULT DATA INTO CASHBOX");
							CLog.writeLogVS(e.toString());

							e.printStackTrace();

						}


						String Ireject = mypackage.split(US, -1)[51].split("\\$")[j];
						int numIreject = 0;
						if (!Ireject.equals("N"))
							numIreject = Integer.parseInt(mypackage.split(US, -1)[51].split("\\$")[j]);

						String Iretract = mypackage.split(US, -1)[52].split("\\$")[j];
						int numIretract = 0;
						if (!Iretract.equals("N"))
							numIretract = Integer.parseInt(mypackage.split(US, -1)[52].split("\\$")[j]);

						//////////////////H68
						int numIdeposit = 0;
						if (mypackage.split(US, -1)[45] != null && !mypackage.split(US, -1)[45].equals(""))
						{
							String Ideposit = mypackage.split(US, -1)[45].split("\\$")[j];
							if (!Ideposit.equals("N"))
								numIdeposit = Integer.parseInt(mypackage.split(US, -1)[45].split("\\$")[j]);
						}


						//sql="update T_TML_CASHBOX set I_DENO='"+mypackage.split(US, -1)[42].split("\\$")[j]+"',C_NOTETYPE='"+mypackage.split(US, -1)[43].split("\\$")[j]+"',I_remain='"+mypackage.split(US, -1)[46].split("\\$")[j]+"',I_loading='"+mypackage.split(US, -1)[44].split("\\$")[j]+"' where C_TERMID ='"+obInterface.getTmid()+"' and C_TERMCASTTEEID='"+(j+1)+"'";
						String sql="update T_TML_CASHBOX set I_DENO='"+mypackage.split(US, -1)[42].split("\\$")[j]+"',C_NOTETYPE='"+mypackage.split(US, -1)[43].split("\\$")[j]+"',I_remain='"+mypackage.split(US, -1)[46].split("\\$")[j]+"',I_retract='"+numIretract+"',I_reject='"+numIreject+"',I_deposit='"+numIdeposit+"',C_STATUS='"+mypackage.split(US, -1)[40].split("\\$")[j]+"'";
						
						//if	(Integer.parseInt("0"+mypackage.split(US, -1)[44].split("\\$")[j])>=Integer.parseInt("0"+mypackage.split(US, -1)[46].split("\\$")[j]))	sql+=",I_loading='"+mypackage.split(US, -1)[44].split("\\$")[j]+"'";
						sql+=",I_LOADING='"+mypackage.split(US, -1)[44].split("\\$")[j]+"'";
						
						//if	(obInterface.getMudt().length()>6)	sql+=",C_LOADTIME='"+App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"'";
						sql+=",C_LOADTIME='"+obInterface.getMudt()+"'";
						
						sql+=" where C_TERMID ='"+obInterface.getTmid()+"' and C_TERMCASTTEEID='"+(j+1)+"'";
						
						System.out.println("CASH BOX UPDATE SQL [" + sql + "]");
						CLog.writeLogVS("CASH BOX UPDATE SQL [" + sql + "]");
						
						mysql.executeUpdate(sql);
					}
					
					
					//--------MixedBox--------
					//mysql.delete("T_TML_MIXEDBOX", (new StringBuilder("C_TERMID='")).append(obInterface.getTmid()).append("'").toString());
					
					System.out.println("H68 DATA LENGTH [" + mypackage.split(US, -1).length + "] [" + mypackage.split(US).length +"]");
					CLog.writeLogVS("H68 DATA LENGTH [" + mypackage.split(US, -1).length + "] [" + mypackage.split(US).length +"]");

					if(mypackage.split(US, -1).length >= 57)//57
					{
						if (mypackage.split(US, -1)[53] != null && !mypackage.split(US, -1)[53].equals(""))
						{
							for	(int j=0;j<mypackage.split(US, -1)[53].split(" ").length;j++)
							{
								//-------------------
								String SecondTable = "T_TML_CASHSETT";
								String MixedLoadTime = "";
								Iterator SecondIT=mysql.select(SecondTable,"C_TERMID='" + obInterface.getTmid() + "' order by c_loadtime desc").iterator();
								ObInterface SecondTemp;
								
								System.out.println("Second IT boolean [" + SecondIT.hasNext() +"]");
								CLog.writeLogVS("Second IT boolean [" + SecondIT.hasNext() +"]");
								for	(int i=0;SecondIT.hasNext();i++)	
								{
									SecondTemp =(ObInterface)SecondIT.next();
									MixedLoadTime = String.valueOf(SecondTemp.get("C_LOADTIME"));
									MixedLoadTime = MixedLoadTime.trim();
									
									System.out.println("Second Date [" + i + " "+ MixedLoadTime +"]");
									CLog.writeLogVS("Second Date [" + i + " "+ MixedLoadTime +"]");
									
									break;
								}
								
								
								
								//-------------------
								//Try to insert new mixed box
								System.out.println("OK ! MIXED BOX ADDING IS [" + j + "]");
								CLog.writeLogVS("OK ! MIXED BOX ADDING IS [" + j + "]");

								try
								{
									String NoteDate = TXNTime;

									//if	(obInterface.getMudt().length()>6)	
									//	NoteDate = App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss");
									
									String sql = "insert into T_TML_MIXEDBOX values('" + obInterface.getTmid() + "', '" + (j + 1) + "','VND','0','0','0','"+NoteDate+"','0','0')";
									System.out.println("MIXED BOX INITIAL SQL [" + sql + "]");
									CLog.writeLogVS("MIXED BOX INITIAL SQL [" + sql + "]");
									mysql.executeUpdate(sql);
									//mysql.executeUpdate((new StringBuilder("insert into T_TML_MIXEDBOX values('")).append(obInterface.getTmid()).append("','").append(j + 1).append("','VND','0','0','0','0','0'").toString());
								}
								catch (Exception e) 
								{
									System.out.println("ERROR IS ADDING DEFAULT DATA INTO MIXED BOX");
									System.out.println(e.toString());
									
									CLog.writeLogVS("ERROR IS ADDING DEFAULT DATA INTO MIXED BOX");
									CLog.writeLogVS(e.toString());

									e.printStackTrace();

								}

								String INote = mypackage.split(US, -1)[53].trim();
								INote = INote.concat(" ");
								System.out.println("53 String: [" + INote + "]");

								INote = mypackage.split(US, -1)[53].split(" ")[j].trim();
								String ICurr = INote.split(":", -1)[0];
								String NoteType = ICurr.substring(0, 1);
								String NoteDeno = ICurr.substring(1);
								String NoteQuantity = INote.split(":", -1)[1];
								String NoteTotal = String.valueOf(Long.parseLong(NoteQuantity) * Long.parseLong(NoteDeno));
								String NoteDate = TXNTime;

								//if	(obInterface.getMudt().length()>6)	
								//	NoteDate = App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss");

								String sql = "update T_TML_MIXEDBOX set C_MONEYTYPE = '" + NoteType + "', C_DENO = '" + NoteDeno + "', C_QUANTITY = '" + NoteQuantity + "', C_LASTUPDATE = '" + NoteDate + "', C_TOTAL = '" + NoteTotal + "', C_LOADING = '" + MixedLoadTime + "' where C_TERMID ='"+obInterface.getTmid()+"' and C_SLOTID='"+(j+1)+"' and C_LASTUPDATE = (select max(c_lastupdate) from t_tml_mixedbox where C_TERMID = '" + obInterface.getTmid() + "')       ";
								System.out.println("NEW MIXED BOX SQL [" + sql + "]");
								CLog.writeLogVS("NEW MIXED BOX SQL [" + sql + "]");

								try
								{
									mysql.executeUpdate(sql);								
								}
								catch (Exception e) 
								{
									System.out.println("ERROR IS UPDATE DATA INTO MIXED BOX");
									System.out.println(e.toString());
									
									CLog.writeLogVS("ERROR IS UPDATE DATA INTO MIXED BOX");
									CLog.writeLogVS(e.toString());
									
									e.printStackTrace();

								}
							}//End For Loop
						}// End If 2 Case
					}//End If 1 Case
					

					
					//===================
					/////Add event for 10002/////
					String tablename = "T_TML_STATUS";
	                String myevent = "";
	                String mycode = "";
	                String mylevel = "99";
	                
	                /*
	                //ATM Status [4]
					if(mypackage.split(US, -1)[4].equals("0"))
					{
						myevent = "ATM Status is Normal";
						mycode = "A0100";
						mylevel = "00";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
					}				
					if(mypackage.split(US, -1)[4].equals("1"))
					{
						myevent = "System Maintainance Mode";
						mycode = "A0101";
						mylevel = "66";

						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");
					}
					if(mypackage.split(US, -1)[4].equals("2"))
					{
						myevent = "Device Warning";
						mycode = "A0102";
						mylevel = "66";

						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");
					}					

					
					//Journal Printer Status [18]
					if(mypackage.split(US, -1)[18].equals("0"))
					{
						myevent = "Journal Printer Normal";
						mycode = "A0200";
						mylevel = "00";

						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");
					}
					if(mypackage.split(US, -1)[18].equals("3") || mypackage.split(US, -1)[18].equals("9"))
					{
						myevent = "Journal Printer Malfunction";
						mycode = "A0201";

						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");
					}					
					if(mypackage.split(US, -1)[18].equals("5"))
					{
						myevent = "Journal Printer Less of Paper";
						mycode = "A0202";
						mylevel = "66";

						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");
					}
					if(mypackage.split(US, -1)[18].equals("6"))
					{
						myevent = "Journal Printer Out of Paper";
						mycode = "A0203";
						mylevel = "66";

						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");
					}

					
					//Receipt Printer Status [16]
					if(mypackage.split(US, -1)[16].equals("0"))
					{
						myevent = "Receipt Printer Normal";
						mycode = "A0300";
						mylevel = "00";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

					}
					if(mypackage.split(US, -1)[16].equals("3") || mypackage.split(US, -1)[16].equals("9"))
					{
						myevent = "Receipt Printer Malfunction";
						mycode = "A0301";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

					}
					if(mypackage.split(US, -1)[16].equals("5"))
					{
						myevent = "Receipt Printer Less of Paper";
						mycode = "A0302";
						mylevel = "66";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

					}
					if(mypackage.split(US, -1)[16].equals("6"))
					{
						myevent = "Receipt Printer Out of Paper";
						mycode = "A0303";
						mylevel = "66";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

					}

					
					//Card Reader Status [24]
					if(mypackage.split(US, -1)[24].equals("0"))
					{
						myevent = "Card Reader Normal";
						mycode = "A0400";
						mylevel = "00";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

					}
					if(mypackage.split(US, -1)[24].equals("9"))
					{
						myevent = "Card Reader Malfunction";
						mycode = "A0401";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

					}
					
					
					//Envelop Deposit Status [34]
					if(mypackage.split(US, -1)[34].equals("0"))
					{
						myevent = "Envelop Deposit Normal";
						mycode = "A0820";
						mylevel = "66";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

					}
					if(mypackage.split(US, -1)[34].equals("9"))
					{
						myevent = "Envelop Deposit Malfunction";
						mycode = "A0821";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

					}
					
					
					//Dispenser Status [8]
					if(mypackage.split(US, -1)[8].equals("0"))
					{
						myevent = "Cash Despenser Normal";
						mycode = "A0500";
						mylevel = "00";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

					}
					if(mypackage.split(US, -1)[8].equals("9"))
					{
						myevent = "Cash Despenser Malfunction";
						mycode = "A0501";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

					}
					
					
					//Cabinet door status [30]
					if(mypackage.split(US, -1)[30].equals("0"))
					{
						myevent = "Cabinet Door Normal";
						mycode = "A0700";
						mylevel = "66";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

					}
					if(mypackage.split(US, -1)[30].equals("9"))
					{
						myevent = "Cabinet Door Malfunction";
						mycode = "A0701";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

					}
					
					
					//Insurance door status [28]
					if(mypackage.split(US, -1)[28].equals("0"))
					{
						myevent = "Safe Door Normal";
						mycode = "A0600";
						mylevel = "00";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

					}
					if(mypackage.split(US, -1)[28].equals("9"))
					{
						myevent = "Safe Door Malfunction";
						mycode = "A0601";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

					}
					
					
					//Keyboard status [32]
					if(mypackage.split(US, -1)[32].equals("0"))
					{
						myevent = "Encrypt Machine (Keyboard) Normal";
						mycode = "A0800";
						mylevel = "00";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

					}
					if(mypackage.split(US, -1)[32].equals("9"))
					{
						myevent = "Encrypt Machine (Keyboard) Malfunction";
						mycode = "A0801";
						
						if (this.Valid2Add(obInterface.getTmid(), mycode))
						{
							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

						}
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

					}
					
					
					//CashBox Status for Each Box [40]
					for	(int j=0;j<mypackage.split(US, -1)[40].split("\\$").length;j++)
					{
						
//						if(mypackage.split(US, -1)[40].split("\\$")[j].equals("1"))
//						{
//							myevent = "Cash Shortage";
//							mycode = "A0811";
//							mylevel = "66";
//							
//							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
//
//						}
//						if(mypackage.split(US, -1)[40].split("\\$")[j].equals("2"))
//						{
//							myevent = "Out of Cash";
//							mycode = "A0812";
//							mylevel = "66";
//							
//							mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
//
//						}
						if(mypackage.split(US, -1)[40].split("\\$")[j].equals("9"))
						{
							myevent = "Cashbox Malfunction";
							mycode = "A0813";
							
							if (this.Valid2Add(obInterface.getTmid(), mycode))
							{
								mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
								//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

							}
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

						}						
					}
					*/
					
					////////--------Money Status for an ATM
					//Take the Amount Limit
					long lAmount = 0;

					//Collection	interfaceInfos=mysql.select("T_TML_TOTALAMTLIMIT","C_TERMID='"+obInterface.getTmid()+"'");
					Collection	interfaceInfos=mysql.select("T_TML_CASH_SHORT","C_TERMID='"+obInterface.getTmid()+"'");
					//Iterator it=interfaceInfos.iterator();

					if (interfaceInfos != null)
					{
						Iterator it=interfaceInfos.iterator();

						ObInterface temp1;
						for	(;it.hasNext();)
						{						
							temp1 =	(ObInterface)it.next();
							lAmount = Long.parseLong(temp1.getString("C_MONEY"));
						}
						System.out.println("THE NOTIFICATION AMOUNT IS [" + lAmount + "]");
						CLog.writeLogVS("THE NOTIFICATION AMOUNT IS [" + lAmount + "]");


						//Get the current amount in each ATM
						long cAmount = 0;
						for	(int j=0; j<mypackage.split(US, -1)[42].split("\\$").length; j++)
						{
							long boxAmount=0;
							long doNumber = 0;
							long reNumber = 0;

							if (!mypackage.split(US, -1)[42].split("\\$")[j].equals("N"))
							{
								doNumber = Long.parseLong(mypackage.split(US, -1)[42].split("\\$")[j]);
							}
							if (!mypackage.split(US, -1)[46].split("\\$")[j].equals("N"))
							{
								reNumber = Long.parseLong(mypackage.split(US, -1)[46].split("\\$")[j]);
							}


							//boxAmount = Long.parseLong(mypackage.split(US, -1)[42].split("\\$")[j]) * Long.parseLong(mypackage.split(US, -1)[46].split("\\$")[j]);
							boxAmount = doNumber * reNumber;
							//System.out.println("Domination [" + Long.parseLong(mypackage.split(US, -1)[42].split("\\$")[j]) + "] Remain Notes [" + Long.parseLong(mypackage.split(US, -1)[46].split("\\$")[j]) + "]");
							System.out.println("Domination [" + doNumber + "] Remain Notes [" + reNumber + "]");
							CLog.writeLogVS("Domination [" + doNumber + "] Remain Notes [" + reNumber + "]");



							//						for	(int n=0; n<mypackage.split(US, -1)[46].split("\\$").length; n++)
							//						{
							//							boxAmount = Long.parseLong(mypackage.split(US, -1)[42].split("\\$")[j]) * Long.parseLong(mypackage.split(US, -1)[46].split("\\$")[n]);
							//							System.out.println("Domination [" + Long.parseLong(mypackage.split(US, -1)[42].split("\\$")[j]) + "] Remain Notes [" + Long.parseLong(mypackage.split(US, -1)[46].split("\\$")[n]) + "]");
							//							break;
							//						}
							cAmount = cAmount + boxAmount;

						}

						//Summarry for Status
						//Case 1: Out Of Cash
						if (cAmount <= 0)
						{
							myevent = "-Out of Cash-0";
							mycode = "A0812";
							mylevel = "66";

							boolean Validable = this.Valid2Add(obInterface.getTmid(), mycode);
							CLog.writeLogVS("ValidCheck [" + Validable + "]        ");
							if (Validable)
							{
								CLog.writeLogVS("ValidCheck Inside [" + Validable + "]        ");
								mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
								//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

								//----
								Thread esms = new Thread (new SmsThread(obInterface.getTmid().trim(), mycode.trim()));
								esms.setName("EventSMSThread");
								esms.setDaemon(true);
								esms.start();
								//----

							}
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

						}
						//Case 2: Cash Shortage
						if(cAmount > 0 && cAmount < lAmount)
						{
							myevent = "-Cash Shortage-" + cAmount;
							mycode = "A0811";
							mylevel = "66";

							boolean Validable = this.Valid2Add(obInterface.getTmid(), mycode);
							CLog.writeLogVS("ValidCheck [" + Validable + "]        ");
							if (Validable)
							{
								CLog.writeLogVS("ValidCheck Inside [" + Validable + "]        ");
								//CLog.writeLogVS("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

								mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
								//mysql.executeUpdate("insert into T_TML_EVENT values('0','ATMC','0','1','0','0','0','','','')");
								
								//----
								Thread esms = new Thread (new SmsThread(obInterface.getTmid().trim(), mycode.trim()));
								esms.setName("EventSMSThread");
								esms.setDaemon(true);
								esms.start();
								//----
								
							}
							//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");

						}
					}//End for if t_tml_cash_short got value
					//===================================================================================
					
					
//					//Finally add for 10002
//					//obInterface.getMudt()
//					try
//					{
//						mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ obInterface.getMudt() +"','"+ myevent +"','','','')");
//
//					}
//					catch(Exception e)
//					{
//						e.printStackTrace();
//						CLog.writeLogVS(e.toString());
//						CLog.writeLogVS("EXCEPTION AT EVENT CODE [" + mycode + "] AND AT EVENT [" + myevent + "]");
//						
//					}
					
					
				} 
				catch (Exception e) 
				{
					CLog.writeLogVS("ERROR IN STATUS REPORT MESSAGE --- START DEVICE PART");
					CLog.writeLogVS(e.toString());
					
					e.printStackTrace();
					
				}
			}


			//10004/10008加钞/减钞报告
			/////////0008MSG///
			
			if	(mypackage.split(US, -1)[0].trim().indexOf("0004")>=0 || mypackage.split(US, -1)[0].trim().indexOf("0008")>=0)
			{
				String SettTermId = mypackage.split(US, -1)[1].trim();
				obInterface.setTmid(mypackage.split(US, -1)[1]);
				obInterface.setMudt(App.transDateFormat(mypackage.split(US, -1)[8],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss"));

				int	num=1;
				if	(mypackage.split(US, -1)[0].trim().indexOf("0008")>=0)	num=-1;
				//CLog.writeLogVS("c_loadtime="+obInterface.get("c_loadtime"));

				try 
				{
					try
					{
						//======= SETT for T_TML_CASHSETT ========
						System.out.println("################################");
						CLog.writeLogVS("################################");
						
						//-------Delete T_TML_CASHSETT data before insert new
						/*
						Iterator settIT = mysql.selectSql("select * from t_tml_cashsett where c_termid = '" + SettTermId + "'   ").iterator();

						//System.out.println("###SETTIT IS [" + settIT == null ? true : false + "]");
						System.out.println("###SETTIT IS [" + settIT.hasNext() + "]");

						if (settIT.hasNext())
						{
							mysql.delete("T_TML_CASHSETT", (new StringBuilder("C_TERMID='")).append(obInterface.getTmid()).append("'").toString());

						}
						*/
						//-------End Delete
						
						//-------Take the from date
						Iterator TakeFromDate=mysql.selectSql("select * from t_tml_cashsett where c_termid = '" + SettTermId + "' order by c_loadtime desc   ").iterator();
						ObInterface TakeFromDateTemp;
						String SettFromDate = "";
						if (TakeFromDate.hasNext())
						{
							TakeFromDateTemp =(ObInterface)TakeFromDate.next();
							SettFromDate = TakeFromDateTemp.getString("C_LOADTIME");
						}
						//-------This one to get the from date

						//-------Add CashLoad Data and Sett Default Value-------
						for	(int j=0;j<mypackage.split(US, -1)[2].split("\\$").length;j++)
						{
							System.out.println("###OK CAN GO TO ADD CASHLOAD OF SETT");
							CLog.writeLogVS("###OK CAN GO TO ADD CASHLOAD OF SETT");

							
							try
							{
								//Try to insert all of the cashload
								String iLoad = mypackage.split(US, -1)[6].split("\\$")[j];
								int numIload = 0;
								if (!iLoad.equals("N"))
									numIload = Integer.parseInt(mypackage.split(US, -1)[6].split("\\$")[j]);

								System.out.println("Deno AT SETT [" + mypackage.split(US, -1)[4].split("\\$")[j] +"]");
								String Deno = mypackage.split(US, -1)[4].split("\\$")[j];
								int numDeno = 0;
								if (!Deno.trim().equals("N"))
									numDeno = Integer.parseInt(mypackage.split(US, -1)[4].split("\\$")[j]);

								//Total amount of a deno is loaded
								obInterface.setAnum(num*Integer.parseInt("0"+numDeno)*Integer.parseInt("0"+numIload));

								//mysql.executeUpdate("insert into T_TML_CASHSEET values('"+obInterface.getTmid()+"','"+(j+1)+"','"+mypackage.split(US, -1)[4].split("\\$")[j]+"','"+obInterface.getIdtp()+numIload+"','"+obInterface.getMudt()+"','"+obInterface.getAnum()+"','"+mypackage.split(US, -1)[5].split("\\$")[j]+"')");
								//mysql.executeUpdate("insert into T_TML_CASHSETT values('"+obInterface.getTmid()+"','"+(j+1)+"','"+obInterface.getMudt()+"','"+mypackage.split(US, -1)[4].split("\\$")[j]+"','"+obInterface.getIdtp()+numIload+"','"+mypackage.split(US, -1)[5].split("\\$")[j]+"','"+obInterface.getAnum()+"','0','0','0','0','0','0')");
								//mysql.executeUpdate("insert into T_TML_CASHSETT values('"+obInterface.getTmid()+"','"+(j+1)+"','"+obInterface.getMudt()+"','"+numDeno+"','"+obInterface.getIdtp()+numIload+"','"+mypackage.split(US, -1)[5].split("\\$")[j]+"','"+obInterface.getAnum()+"','0','0','0','0','0','0','0')");
								String SettSQL = "insert into T_TML_CASHSETT values('"+obInterface.getTmid()+"','"+(j+1)+"','"+obInterface.getMudt()+"','"+numDeno+"','"+obInterface.getIdtp()+numIload+"','"+mypackage.split(US, -1)[5].split("\\$")[j]+"','"+obInterface.getAnum()+"','0','0','0','0','0','0','0')";
								System.out.println("DEFAULT SQL FOR SETT BOX [" + SettSQL + "]");
								CLog.writeLogVS("DEFAULT SQL FOR SETT BOX [" + SettSQL + "]");

								mysql.executeUpdate(SettSQL);
							}
							catch (Exception e)
							{
								System.out.println("### EXCEPTION O INITIAL SETT DATA");
								System.out.println(e.toString());
								
								CLog.writeLogVS("### EXCEPTION O INITIAL SETT DATA");
								CLog.writeLogVS(e.toString());
								
								e.printStackTrace();								
							}
							
						}
						//-------End adding default value

						//------- Coi coi T_TML_CASHBOX co du lieu khong --- Co thi lay qua --- Khong thi add default value
						Iterator CheckCashBoxIT=mysql.selectSql("select * from t_tml_cashbox where c_termid = '" + SettTermId + "' order by C_TERMCASTTEEID asc").iterator();
						ObInterface CheckCashBoxTemp;

						System.out.println("### CheckCashBoxIT IS [" + CheckCashBoxIT.hasNext() + "]");
						CLog.writeLogVS("### CheckCashBoxIT IS [" + CheckCashBoxIT.hasNext() + "]");

						if (CheckCashBoxIT.hasNext())
						{
							for	(int i=1;CheckCashBoxIT.hasNext();i++)	
							{
								CheckCashBoxTemp =(ObInterface)CheckCashBoxIT.next();
								
								try
								{

									String SettIloading = String.valueOf(CheckCashBoxTemp.getString("I_LOADING"));
									if (SettIloading.equals("N")) SettIloading = "0";
									System.out.println("Iloading = [" + SettIloading +"]");
									CLog.writeLogVS("Iloading = [" + SettIloading +"]");
									
									String SettRemain = CheckCashBoxTemp.getString("I_Remain");
									if (SettRemain.equals("N")) SettRemain = "0";									
									System.out.println("Remain = [" + SettRemain +"]");
									CLog.writeLogVS("Remain = [" + SettRemain +"]");									
									
									String SettReject = CheckCashBoxTemp.getString("I_REJECT");
									if (SettReject.equals("N")) SettReject = "0";	
									System.out.println("Reject = [" + SettReject +"]");
									CLog.writeLogVS("Reject = [" + SettReject +"]");
									
									String SettRetract = CheckCashBoxTemp.getString("I_RETRACT");
									if (SettRetract.equals("N")) SettRetract = "0";
									System.out.println("Retract = [" + SettRetract +"]");
									CLog.writeLogVS("Retract = [" + SettRetract +"]");
									
									String SettDeposit = CheckCashBoxTemp.getString("I_DEPOSIT");
									if (SettDeposit.equals("N")) SettDeposit = "0";
									System.out.println("Deposit = [" + SettDeposit +"]");
									CLog.writeLogVS("Deposit = [" + SettDeposit +"]");
									
									String SettBoxID = CheckCashBoxTemp.getString("C_TERMCASTTEEID").trim();

									int SettDispense = Integer.parseInt(SettIloading) + Integer.parseInt(SettDeposit) - Integer.parseInt(SettRemain) - Integer.parseInt(SettReject) - Integer.parseInt(SettRetract);
									//if (SettDispense < 0) SettDispense = (SettDispense) * (-1);
									if (SettDispense < 0) SettDispense = 0;   


									//mysql.executeUpdate("update T_TML_CASHSET set C_LOADTIME='"+obInterface.getMudt()+"',C_STATUS='"+mypackage.split(US, -1)[3].split("\\$")[j]+"',I_DENO='"+mypackage.split(US, -1)[4].split("\\$")[j]+"',C_NOTETYPE='"+mypackage.split(US, -1)[5].split("\\$")[j]+"',I_remain='"+mypackage.split(US, -1)[6].split("\\$")[j]+"',I_loading='"+numIload+"' where C_TERMID ='"+obInterface.getTmid()+"' and C_TERMCASTTEEID='"+(j+1)+"'");
									//mysql.executeUpdate("update T_TML_CASHSETT set C_LOADTIME_SET='"+CheckCashBoxTemp.getString("C_LOADTIME")+"',I_LOADING_SET='"+CheckCashBoxTemp.getString("I_LOADING")+"',I_DISPENSER='"+SettDispense+"',I_REMAIN='"+CheckCashBoxTemp.getString("I_REMAIN")+"',I_REJECT='"+CheckCashBoxTemp.getString("I_REJECT")+"',I_RETRACT='"+CheckCashBoxTemp.getString("I_RETRACT")+"' where C_TERMID ='"+obInterface.getTmid()+"' and C_LOADTIME = (select max(c_loadtime) from t_tml_cashsett) and C_TERMCASTTEEID='"+(i)+"'");//Decrease the casett ID from i= 1 to i
									//mysql.executeUpdate("update T_TML_CASHSETT set C_LOADTIME_SET='"+SettFromDate+"',I_LOADING_SET='"+CheckCashBoxTemp.getString("I_LOADING")+"',I_DISPENSER='"+SettDispense+"',I_REMAIN='"+CheckCashBoxTemp.getString("I_REMAIN")+"',I_REJECT='"+CheckCashBoxTemp.getString("I_REJECT")+"',I_RETRACT='"+CheckCashBoxTemp.getString("I_RETRACT")+"' where C_TERMID ='"+obInterface.getTmid()+"' and C_LOADTIME = (select max(c_loadtime) from t_tml_cashsett) and C_TERMCASTTEEID='"+(i)+"'");//Decrease the casett ID from i= 1 to i
									//mysql.executeUpdate("update T_TML_CASHSETT set C_LOADTIME_SET='"+SettFromDate+"',I_LOADING_SET='"+CheckCashBoxTemp.getString("I_LOADING")+"',I_DISPENSER='"+SettDispense+"',I_REMAIN='"+CheckCashBoxTemp.getString("I_REMAIN")+"',I_REJECT='"+CheckCashBoxTemp.getString("I_REJECT")+"',I_RETRACT='"+CheckCashBoxTemp.getString("I_RETRACT")+"',I_DEPOSIT='"+CheckCashBoxTemp.getString("I_DEPOSIT")+"' where C_TERMID ='"+obInterface.getTmid()+"' and C_LOADTIME = (select max(c_loadtime) from t_tml_cashsett) and C_TERMCASTTEEID='"+ SettBoxID +"'");//Reset the BoxID
									
									String updateSett = "update T_TML_CASHSETT set C_LOADTIME_SET='"+SettFromDate+"',I_LOADING_SET='"+SettIloading+"',I_DISPENSER='"+SettDispense+"',I_REMAIN='"+ SettRemain +"',I_REJECT='"+ SettReject +"',I_RETRACT='"+ SettRetract +"',I_DEPOSIT='"+ SettDeposit +"' where C_TERMID ='"+obInterface.getTmid()+"' and C_LOADTIME = (select max(c_loadtime) from t_tml_cashsett where C_TERMID = '" + obInterface.getTmid() + "') and C_TERMCASTTEEID='"+ SettBoxID +"'";
									System.out.println(updateSett);
									CLog.writeLogVS(updateSett);
									//mysql.executeUpdate("update T_TML_CASHSETT set C_LOADTIME_SET='"+SettFromDate+"',I_LOADING_SET='"+SettIloading+"',I_DISPENSER='"+SettDispense+"',I_REMAIN='"+ SettRemain +"',I_REJECT='"+ SettReject +"',I_RETRACT='"+ SettRetract +"',I_DEPOSIT='"+ SettDeposit +"' where C_TERMID ='"+obInterface.getTmid()+"' and C_LOADTIME = (select max(c_loadtime) from t_tml_cashsett) and C_TERMCASTTEEID='"+ SettBoxID +"'");//Reset the BoxID//
									mysql.executeUpdate(updateSett);//Reset the BoxID//

									
								}
								catch (Exception e)
								{
									System.out.println("### EXCEPTION AT SETT UPDATE DATA");
									System.out.println(e.toString());
									
									CLog.writeLogVS("### EXCEPTION AT SETT UPDATE DATA");
									CLog.writeLogVS(e.toString());									
									
									e.printStackTrace();
								}
							}
						}
					}
					catch(Exception SETTEX)
					{
						System.out.println("### EXCEPTION at SETT");
						System.out.println(SETTEX.toString());
						
						CLog.writeLogVS("### EXCEPTION at SETT");
						CLog.writeLogVS(SETTEX.toString());						
						
						SETTEX.printStackTrace();
					}
					
					//=======NORMAL CASH LOAD	
					CLog.writeLogVS(">>>>>>>>NORMAL CASH LOAD");

					CLog.writeLogVS("NUMBER OF CASHBOX AT LOAD CASH: "+ mypackage.split(US, -1)[2].split("\\$").length);
					
					mysql.delete("T_TML_CASHBOX", (new StringBuilder("C_TERMID='")).append(obInterface.getTmid()).append("'").toString());
					//mysql.delete("T_TML_MIXEDBOX", (new StringBuilder("C_TERMID='")).append(obInterface.getTmid()).append("'").toString());

					for	(int j=0;j<mypackage.split(US, -1)[2].split("\\$").length;j++)
					{
						 //Try to insert new DEFAULT cashbox
						 try
						 {
							 mysql.executeUpdate((new StringBuilder("insert into T_TML_CASHBOX values('")).append(obInterface.getTmid()).append("','").append(j + 1).append("','0','0','0','0','0','0','").append(obInterface.getBnum()).append("','0','0','0','0')").toString());
						 }
						 catch (Exception e) 
						 {
							 System.out.println("NORMAL LOADCASH: ERROR IS ADDING DEFAULT DATA INTO CASHBOX");
							 System.out.println(e.toString());
							 
							 CLog.writeLogVS("NORMAL LOADCASH: ERROR IS ADDING DEFAULT DATA INTO CASHBOX");
							 CLog.writeLogVS(e.toString());							 
							 
							 e.printStackTrace();

						 }
						 
						//Update cashload and cashbox 
						//obInterface.setAnum(num*Integer.parseInt("0"+mypackage.split(US, -1)[4].split("\\$")[j])*Integer.parseInt("0"+mypackage.split(US, -1)[6].split("\\$")[j]));
						CLog.writeLogVS("PROCESSING CASHBOX " + (j+1));
						
						String iLoad = mypackage.split(US, -1)[6].split("\\$")[j];
						int numIload = 0;
						if (!iLoad.equals("N"))
							numIload = Integer.parseInt(mypackage.split(US, -1)[6].split("\\$")[j]);
						CLog.writeLogVS("ILOADING [" + numIload + "]");
							
						String Deno = mypackage.split(US, -1)[4].split("\\$")[j];
						int numDeno = 0;
						if (!Deno.equals("N"))
							numDeno = Integer.parseInt(mypackage.split(US, -1)[4].split("\\$")[j]);
						CLog.writeLogVS("IDENO [" + numDeno + "]");
						
						//obInterface.setAnum(num*Integer.parseInt("0"+numDeno)*Integer.parseInt("0"+numIload));
						try
						{
							obInterface.setAnum(num*Integer.parseInt("0"+numDeno)*Integer.parseInt("0"+numIload));							
						}
						catch(Exception Anum)
						{
							CLog.writeLogVS("ERROR IN CALCULATE ANUM");
							CLog.writeLogVS(Anum.toString());
							
							Anum.printStackTrace();
							
						}
						

						CLog.writeLogVS("insert into T_TML_LOADCASH values('"+obInterface.getTmid()+"','"+(j+1)+"','"+mypackage.split(US, -1)[4].split("\\$")[j]+"','"+obInterface.getIdtp()+numIload+"','"+obInterface.getMudt()+"','"+obInterface.getAnum()+"','"+mypackage.split(US, -1)[5].split("\\$")[j]+"')");
						mysql.executeUpdate("insert into T_TML_LOADCASH values('"+obInterface.getTmid()+"','"+(j+1)+"','"+mypackage.split(US, -1)[4].split("\\$")[j]+"','"+obInterface.getIdtp()+numIload+"','"+obInterface.getMudt()+"','"+obInterface.getAnum()+"','"+mypackage.split(US, -1)[5].split("\\$")[j]+"')");
						
						CLog.writeLogVS("update T_TML_CASHBOX set C_LOADTIME='"+obInterface.getMudt()+"',C_STATUS='"+mypackage.split(US, -1)[3].split("\\$")[j]+"',I_DENO='"+mypackage.split(US, -1)[4].split("\\$")[j]+"',C_NOTETYPE='"+mypackage.split(US, -1)[5].split("\\$")[j]+"',I_remain='"+mypackage.split(US, -1)[6].split("\\$")[j]+"',I_loading='"+numIload+"' where C_TERMID ='"+obInterface.getTmid()+"' and C_TERMCASTTEEID='"+(j+1)+"'");
						mysql.executeUpdate("update T_TML_CASHBOX set C_LOADTIME='"+obInterface.getMudt()+"',C_STATUS='"+mypackage.split(US, -1)[3].split("\\$")[j]+"',I_DENO='"+mypackage.split(US, -1)[4].split("\\$")[j]+"',C_NOTETYPE='"+mypackage.split(US, -1)[5].split("\\$")[j]+"',I_remain='"+mypackage.split(US, -1)[6].split("\\$")[j]+"',I_loading='"+numIload+"' where C_TERMID ='"+obInterface.getTmid()+"' and C_TERMCASTTEEID='"+(j+1)+"'");
						//mysql.executeUpdate("update T_TML_CASHBOX set C_LOADTIME='"+obInterface.getMudt()+"',C_STATUS='"+mypackage.split(US, -1)[3].split("\\$")[j]+"',I_DENO='"+mypackage.split(US, -1)[4].split("\\$")[j]+"',C_NOTETYPE='"+mypackage.split(US, -1)[5].split("\\$")[j]+"',I_remain='"+mypackage.split(US, -1)[6].split("\\$")[j]+"',I_loading='"+numIload+"' where C_TERMID ='"+SettTermId+"' and C_TERMCASTTEEID='"+(j+1)+"'");

					}
					
					/*
					/////Add event/////
					String tablename = "T_TML_STATUS";
	                String myevent = "";
	                String mycode = "";
	                String mylevel = "99";
					
					//CashLoad
					myevent = "Cash Load";
					mycode = "A0900";
					mylevel = "66";
							
					if (this.Valid2Add(obInterface.getTmid(), mycode))
					{
						mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
						//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','"+ myevent +"','','','')");

					}
					//mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
					*/
					
				} 
				catch (Exception e) 
				{
					CLog.writeLogVS("Update cashbox at load cash FAIL ");
					CLog.writeLogVS(e.toString());
					
					e.printStackTrace();

				}
			}

//			if(mypackage.split(US, -1)[0].trim().indexOf("0004") >= 0 || mypackage.split(US, -1)[0].trim().indexOf("0008") >= 0)
//			{
//				obInterface.setTmid(mypackage.split(US, -1)[1]);
//				obInterface.setMudt(App.transDateFormat(mypackage.split(US, -1)[8], "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss"));
//
//				int num = 1;
//
//				if(mypackage.split(US, -1)[0].trim().indexOf("0008") >= 0)
//					num = -1;
//
//				try
//				{
//					for(int j = 0; j < mypackage.split(US, -1)[2].split("\\$").length; j++)
//					{
//						obInterface.setAnum(num * Integer.parseInt((new StringBuilder("0")).append(mypackage.split(US, -1)[4].split("\\$")[j]).toString()) * Integer.parseInt((new StringBuilder("0")).append(mypackage.split(US, -1)[6].split("\\$")[j]).toString()));
//						mysql.executeUpdate((new StringBuilder("insert into T_TML_LOADCASH values('")).append(obInterface.getTmid()).append("','").append(j + 1).append("','").append(mypackage.split(US, -1)[4].split("\\$")[j]).append("','").append(obInterface.getIdtp()).append(mypackage.split(US, -1)[6].split("\\$")[j]).append("','").append(obInterface.getMudt()).append("','").append(obInterface.getAnum()).append("','").append(mypackage.split(US, -1)[5].split("\\$")[j]).append("')").toString());
//						mysql.executeUpdate((new StringBuilder("update T_TML_CASHBOX set C_LOADTIME='")).append(obInterface.getMudt()).append("',I_DENO='").append(mypackage.split(US, -1)[4].split("\\$")[j]).append("',C_NOTETYPE='").append(mypackage.split(US, -1)[5].split("\\$")[j]).append("',I_remain='").append(mypackage.split(US, -1)[6].split("\\$")[j]).append("',I_loading='").append(mypackage.split(US, -1)[6].split("\\$")[j]).append("' where C_TERMID ='").append(obInterface.getTmid()).append("' and C_TERMCASTTEEID='").append(j + 1).append("'").toString());
//
//					}
//				}
//				catch(Exception e)
//				{
//					e.printStackTrace();
//					CLog.writeLogVS(e.toString());
//				}
//			}


			//10003吞卡报告
			if	(mypackage.split(US, -1)[0].trim().indexOf("0003")>=0)
			{
				CLog.writeLogVS("I AM CCP");
				
				obInterface.setTmid(mypackage.split(US, -1)[1]);
				obInterface.setCdno(mypackage.split(US, -1)[2]);
				obInterface.setCdno1(mypackage.split(US, -1)[3]+mypackage.split(US, -1)[4]);
				obInterface.setCdno2(mypackage.split(US, -1)[5]);
				//obInterface.set("c_termid",mypackage.split(US, -1)[1]);
				//obInterface.set("c_pan",mypackage.split(US, -1)[2]);
				//obInterface.set("c_reason",mypackage.split(US, -1)[3]+mypackage.split(US, -1)[4]);
				//obInterface.set("c_time",mypackage.split(US, -1)[5]);
				try {
					//mysql.insert("T_CARDMERGE");
					mysql.executeUpdate("insert into T_CARDMERGE values('"+obInterface.getTmid()+"','"+obInterface.getCdno2()+"','"+obInterface.getCdno()+"','"+obInterface.getCdno1()+"')");
					
					/////Add event/////
					mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','','1','99','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','Capture Card: "+obInterface.getCdno()+"','','','')");
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					CLog.writeLogVS(e.toString());
				}

			}

		}
		//This catch try to print out all the entries of the message package sent from ATMP
		catch (Exception e) 
		{
			e.printStackTrace();
			for	(int j=0;j<mypackage.split(US, -1).length;j++) CLog.writeLogVS("==="+j+",["+mypackage.split(US, -1)[j]+"]");
			
			CLog.writeLogVS(e.toString());
			return false;
		} 
		catch (Throwable e) 
		{
			CLog.writeLogVS(e.toString());
			CLog.writeLogVS(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
			return false;
		}	
		finally
		{
			for	(int j=0;j<mypackage.split(US, -1).length;j++) CLog.writeLogVS("==="+j+",["+mypackage.split(US, -1)[j]+"]");
			
			obInterface=null;
			mysql.close();
			mysql=null;
		}
		//Logger.log (evt);
		CLog.writeLogVS("===Total Processing Time For Insert Msg Package From Switch:"+(System.currentTimeMillis() - stime)+" ms===");
		return true;
	}

	public boolean update(String mydatetime) 
	{
		System.out.println("OK ! GO TO THE BEACH");
		
		CLog.writeLogVS(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
		CLog.writeLogVS("mydatetimeATUPDATE["+mydatetime+"]"+mydatetime.length());
		
		if	(mydatetime==null || mydatetime.length()<10 )			
			return false;

		System.out.println("OK ! REACH THE BEACH");
		long stime = System.currentTimeMillis();
		ObInterface obInterface=new ObInterface();

		try 
		{
			try 
			{
				if	(factory==null) factory=new XmlBeanFactory(new ClassPathResource(xmlfile));
				if	(dataSource==null) dataSource=(DataSource)factory.getBean("dataSource");
				if	(mysql==null) mysql=new Mysql(dataSource);				 
				
//				dataSource=(DataSource)factory.getBean("dataSource");
//				mysql=new Mysql(dataSource);
				obInterface.setDataSource(dataSource);
			} 
			catch (Exception e) 
			{
				//dataSource=(DataSource)factory.getBean("dataSource");
				//mysql=new Mysql(dataSource);
				//obInterface.setDataSource(dataSource);
				
				CLog.writeLogVS("GET CONNECTION AT UPDATE FAIL");
				CLog.writeLogVS(e.toString());
				e.printStackTrace();
				
				obInterface=null;
				mysql=null;
				
				return false;

			}
			mysql.setObInterface(obInterface);

			//-------case 3
			try 
			{
				System.out.println(App.getCurDateStr(19)+" Auto Scan...");
				
				Iterator it=mysql.selectSql("select c_termid as tmid,C_STATUS as cdst,C_REFRESH as cldt from T_TML_STATUS where C_STATUS<>'3' and C_REFRESH <='"+mydatetime+"' order by 1,2").iterator();
				ObInterface temp1;
				
				for	(int i=1;it.hasNext();i++)	
				{
					temp1			=(ObInterface)it.next();
					CLog.writeLogVS("Auto Scan:before:"+i+"/"+obInterface.getRowNum()+",termid="+temp1.getTmid()+",status="+temp1.getCdst()+",time="+temp1.getCldt());
					System.out.println("Auto Scan:before:"+i+"/"+obInterface.getRowNum()+",termid="+temp1.getTmid()+",status="+temp1.getCdst()+",time="+temp1.getCldt());
					
				}
				//if	(obInterface.getRowNum()>0)		mysql.executeUpdate("update T_TML_STATUS set C_STATUS='3',C_REFRESH='"+App.getCurTime14Str()+"' where C_STATUS<>'3' and C_REFRESH <='"+mydatetime+"'");
				if	(obInterface.getRowNum()>0)		
					mysql.executeUpdate("update T_TML_STATUS set C_STATUS='3' where C_STATUS<>'3' and C_REFRESH <='"+mydatetime+"'");
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				System.out.println(e.toString());
			}
			//--------end case 3
			
			//--------case 5
			try 
			{
				long dooos = Long.parseLong(mydatetime.trim());
				
				//int oosdt = Integer.parseInt(mydatetime.trim()) - 10000;
				//String oosdts = String.valueOf(oosdt);
				CLog.writeLogVS("MY 5 STRING IS [" + dooos + "] and Original is [" + mydatetime + "]");
				
				Iterator itoos = mysql.selectSql("select c_termid as tmid,C_STATUS as cdst,C_REFRESH as cldt from T_TML_STATUS where C_STATUS<>'5' order by 1,2").iterator();
				ObInterface temp2;
				
				for	(int i=1;itoos.hasNext();i++)	
				{
					temp2			=(ObInterface)itoos.next();
					CLog.writeLogVS("Auto Scan:before:"+i+"/"+obInterface.getRowNum()+",termid="+temp2.getTmid()+",status="+temp2.getCdst()+",time="+temp2.getCldt());
					//System.out.println("Auto Scan:before:"+i+"/"+obInterface.getRowNum()+",termid="+temp1.getTmid()+",status="+temp1.getCdst()+",time="+temp1.getCldt());
					
					long myrefreshtime = App.StringTwoDate(temp2.getCldt().trim());
					CLog.writeLogVS("C_REFRESH time [" +myrefreshtime+ " dooos [" +dooos+ " termid [" +temp2.getTmid()+ "");
					
					if (dooos - myrefreshtime >= 10000)
					{
						mysql.executeUpdate("update T_TML_STATUS set C_STATUS='5' where C_TERMID = '"+ temp2.getTmid() +"'");

					}
				}
				
				mysql.executeUpdate("update T_TML_STATUS set C_STATUS='0' where C_STATUS='2' OR C_STATUS='9'");
				
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				System.out.println(e.toString());
			}
			//-------- end case 5

			//mysql=null;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			CLog.writeLogVS(e.toString());
			CLog.writeLogVS(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
			return false;
		} 
		catch (Throwable e) 
		{
			return false;
		}	
		finally
		{
			obInterface=null;
			mysql.close();
			mysql=null;

		}
		//Logger.log (evt);
		CLog.writeLogVS("===Total Processing UPDATEEEEEE time:"+(System.currentTimeMillis() - stime)+" ms===");
		return true;
	}


	public boolean event(int mynum) 
	{
		System.out.println("OK! WE CAN GO TO D012 EVENT ADD");
		System.out.println(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
		CLog.writeLogVS(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());

		long stime = System.currentTimeMillis();
		ObInterface obInterface=new ObInterface();

		try 
		{
			try 
			{
				if	(factory==null) factory=new XmlBeanFactory(new ClassPathResource(xmlfile));
				if	(dataSource==null) dataSource=(DataSource)factory.getBean("dataSource");
				if	(mysql==null) mysql=new Mysql(dataSource);				 

//				dataSource=(DataSource)factory.getBean("dataSource");
//				mysql=new Mysql(dataSource);
				obInterface.setDataSource(dataSource);
			} 
			catch (Exception e) 
			{
				//dataSource=(DataSource)factory.getBean("dataSource");
				//mysql=new Mysql(dataSource);
				//obInterface.setDataSource(dataSource);
				
				CLog.writeLogVS("GET CONNECTION AT EVENT FAIL");
				CLog.writeLogVS(e.toString());
				e.printStackTrace();
				
				obInterface=null;
				mysql=null;
				
				return false;

			}			
			mysql.setObInterface(obInterface);
			
			//Select all the event in the t_tm_event
			Collection	interfaceInfos=mysql.selectSql("select C_TERMID as tmid,c_time as opdt,c_code as uscd,c_msg as clnm,C_SOLVE_TIME as cuno,C_USERID as cdtp from T_TML_EVENT where (C_SOLVE_MSG <=' ' or C_SOLVE_MSG is null) and (C_USERID <'A' or C_USERID is null)");
			Iterator		it=interfaceInfos.iterator();
			
			if	(obInterface.getRowNum()>0)	
				System.out.println(App.getCurDateStr(19)+" EVENT "+obInterface.getRowNum());
			
			ObInterface temp1;
			for	(int i=0;it.hasNext();i++)	
			{
				temp1 =(ObInterface)it.next();
				obInterface.setCunm("");
				obInterface.setIdtp("");
				obInterface.setCldt("");
				
				//Try to see this event got policy or not
				try 
				{
					mysql.getRowSql("select C_ROLE as cunm,c_time as cldt,C_ADVICETYPE as idtp,I_SENDCOUNT as num,I_SENDINTERVAL as anum from T_ADVICEPOLICY where C_TERMID='"+temp1.getTmid()+"' and C_CODE='"+temp1.getUscd()+"'");
				}	
				catch (Exception e) 
				{
//					System.out.println("CAN NOT FIND POLICY FOR EVENT AT 1");
//					System.out.println(e.toString());
//					e.printStackTrace();
					
					try 
					{
						mysql.getRowSql("select C_ROLE as cunm,c_time as cldt,C_ADVICETYPE as idtp,I_SENDCOUNT as num,I_SENDINTERVAL as anum from T_ADVICEPOLICY where C_TERMID<=' ' and C_CODE='"+temp1.getUscd()+"'");
					}	
					catch (Exception ex) 
					{
//						System.out.println("CAN NOT FIND POLICY FOR EVENT AT 2");
//						System.out.println(ex.toString());
//						ex.printStackTrace();
						
						continue;
					}
				}

				System.out.println("--- DateTime at EVENT: ["+ obInterface.getCldt()+ "]");
				if	(obInterface.getCldt().length()>0 && obInterface.getCldt().split("-").length>1)
				{
					if	(App.getCurTimeStr(6).compareTo(obInterface.getCldt().split("-")[0])<0 || App.getCurTimeStr(6).compareTo(obInterface.getCldt().split("-")[1])>0)	continue;
				}

				//E:email|P:SMS
				String	myflag="";
				if	(obInterface.getIdtp().equals("11"))		myflag="PE";
				if	(obInterface.getIdtp().equals("01"))		myflag="E";
				if	(obInterface.getIdtp().equals("10"))		myflag="P";
				
				int	mycount=obInterface.getNum();
				int	curcount=1+Integer.parseInt("0"+temp1.getCdtp().replaceAll("\\D",""));
				temp1.setCdtp(""+curcount);
				
				//发送间隔
				int	myInterval=App.getSecondInterval(App.getCurDateStr(8),App.getCurTimeStr(6),temp1.getOpdt().substring(0,10).replaceAll("[:/]","").trim(),temp1.getOpdt().substring(11).replaceAll("[:/]","").trim());
				
				System.out.println("---Interval:"+myInterval+" s,"+obInterface.getAnum()*60+"S,from:"+temp1.getOpdt()+",count:"+curcount+"/"+mycount);
				CLog.writeLogVS("---Interval:"+myInterval+" s,"+obInterface.getAnum()*60+"S,from:"+temp1.getOpdt()+",count:"+curcount+"/"+mycount);
				if	(myInterval<=(curcount-1)*obInterface.getAnum()*60)		
					continue;
				if	(obInterface.getCunm().length()<=0 || obInterface.getIdtp().length()<=0 || myflag.length()<=0)		
					continue;
				//System.out.println(obInterface.getCunm()+",len:"+obInterface.getCunm().split(";").length);
				
				for	(int j=0;j<obInterface.getCunm().split(";").length;j++)	
				{
					try 
					{
						mysql.getRow("P007","usid='"+obInterface.getCunm().split(";")[j]+"' and usst='O'");
						if	(myflag.equals("E"))	obInterface.setUscd(obInterface.getTele());
						System.out.println("Email Send or NOT [" + obInterface.getTele() + "]");
						System.out.println("MY FLAG IS [" + myflag + "]");
						if	(obInterface.getUscd().length()>5)	mysql.executeUpdate("insert into D012 values('"+App.getCurDateStr(8)+"','"+App.getCurTimeStr(6)+"','ATM','06','"+ccas.RandomStringUtils.randomAlphabetic(10)+"','"+temp1.getUscd()+"','"+myflag.substring(0,1)+"','"+temp1.getTmid()+":"+temp1.getClnm()+"',' ',' ','"+obInterface.getUscd()+"',' ')");
						if	(myflag.equals("PE") && obInterface.getTele().length()>5)	mysql.executeUpdate("insert into D012 values('"+App.getCurDateStr(8)+"','"+App.getCurTimeStr(6)+"','ATM','06','"+ccas.RandomStringUtils.randomAlphabetic(10)+"','"+temp1.getUscd()+"','E','"+temp1.getTmid()+":"+temp1.getClnm()+"',' ',' ','"+obInterface.getTele()+"',' ')");
					}	
					catch (Exception ex) 
					{}
				}
				
				if	(curcount>=mycount)			
					temp1.setCdtp("count:"+mycount);
				CLog.writeLogVS("---Interval:"+myInterval+" s,"+obInterface.getAnum()*60+"S,from:"+temp1.getOpdt()+",count:"+curcount+"/"+mycount+"---");
				
				String sql="update T_TML_EVENT set C_USERID='"+temp1.getCdtp()+"' where c_time='"+temp1.getOpdt()+"'";
				if	(temp1.getUscd().length()>0)	sql+=" and c_code='"+temp1.getUscd()+"'";
				if	(temp1.getCuno().length()>0)	sql+=" and C_SOLVE_TIME='"+temp1.getCuno()+"'";
				if	(temp1.getTmid().length()>0)	sql+=" and C_TERMID='"+temp1.getTmid()+"'";
				mysql.executeUpdate(sql);


			}
		} 
		catch (Exception e) 
		{
			System.out.println(e.toString());
			CLog.writeLogVS(e.toString());
			e.printStackTrace();
			CLog.writeLogVS(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
			return false;
		} 
		catch (Throwable e) 
		{
			return false;
		}	
		finally
		{
			obInterface=null;
			mysql.close();
			mysql=null;
		}
		//Logger.log (evt);
		System.out.println("===Total Processing time:"+(System.currentTimeMillis() - stime)+" ms===");
		CLog.writeLogVS("===Total Processing time FOR SMS and Email :"+(System.currentTimeMillis() - stime)+" ms===");
		return true;
	}
	
	
	public void InvalidSess() 
	{
		System.out.println("Let's Start to Clear Invalid Sess in Console");
		CLog.writeLogVS("Let's Start to Clear Invalid Sess in Console");

		ObInterface obInterface=new ObInterface();

		try 
		{
			try 
			{
				if	(factory==null) factory=new XmlBeanFactory(new ClassPathResource(xmlfile));
				if	(dataSource==null) dataSource=(DataSource)factory.getBean("dataSource");
				if	(mysql==null) mysql=new Mysql(dataSource);				 

//				dataSource=(DataSource)factory.getBean("dataSource");
//				mysql=new Mysql(dataSource);
				obInterface.setDataSource(dataSource);
			} 
			catch (Exception e) 
			{
				CLog.writeLogVS("GET CONNECTION AT INVALID CONN FAIL");
				CLog.writeLogVS(e.toString());
				e.printStackTrace();
				
				obInterface=null;
				mysql=null;
			}			
			mysql.setObInterface(obInterface);
			
			//Sellect all Invalid Sess
			String InvalidSessSQL = "select 'alter system kill session ''' ||sid|| ',' ||serial#|| ''' immediate;' as InSess from v$session where status='INACTIVE' and program is null and module is null     ";
			CLog.writeLogVS(InvalidSessSQL);			
			
			Collection	interfaceInfos=mysql.selectSql(InvalidSessSQL);
			CLog.writeLogVS("Invalid Sess BEFORE [" + interfaceInfos.size() + "]");			
			
			Iterator		it=interfaceInfos.iterator();
			ObInterface temp1;
			String SessTr = "";
			for	(int i=0;it.hasNext();i++)	
			{
				temp1 =(ObInterface)it.next();
				
				SessTr = temp1.getString("InSess").trim();
				CLog.writeLogVS("Invalid Sess [" + SessTr + "]");
				
				boolean sessbol = mysql.executeUpdate(SessTr);
				CLog.writeLogVS("Invalid Sess Execute [" + sessbol + "]");
			}
			
			Collection	InSessAfter=mysql.selectSql(InvalidSessSQL);
			CLog.writeLogVS("Invalid Sess AFTER [" + InSessAfter.size() + "]");			
				
		} 
		catch (Exception e) 
		{
			System.out.println(e.toString());
			CLog.writeLogVS(e.toString());
			e.printStackTrace();
			CLog.writeLogVS(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
		} 
		finally
		{
			obInterface=null;
			mysql.close();
			mysql=null;
		}
		
		CLog.writeLogVS("End Invalid Sess in Console");

	}
	
	public void InvalidSessRS() 
	{
		System.out.println("Let's Start to Clear Invalid Sess in Console");
		CLog.writeLogVS("Let's Start to Clear Invalid Sess in Console");

		ObInterface obInterface=new ObInterface();

		try 
		{
			try 
			{
				if	(factory==null) factory=new XmlBeanFactory(new ClassPathResource(xmlfile));
				if	(dataSource==null) dataSource=(DataSource)factory.getBean("dataSource");
				if	(mysql==null) mysql=new Mysql(dataSource);				 
				
//				dataSource=(DataSource)factory.getBean("dataSource");
//				mysql=new Mysql(dataSource);
				obInterface.setDataSource(dataSource);
			} 
			catch (Exception e) 
			{
				CLog.writeLogVS("GET CONNECTION AT INVALID CONN FAIL");
				CLog.writeLogVS(e.toString());
				e.printStackTrace();
				
				obInterface=null;
				mysql=null;
			}			
			mysql.setObInterface(obInterface);
			
			
			////////
			String InvalidSessSQL = "select 'alter system kill session ''' ||sid|| ',' ||serial#|| ''' immediate;' as InSess from v$session where status='INACTIVE' and program is null and module is null     ";
			CLog.writeLogVS(InvalidSessSQL);
			
			java.sql.ResultSet resMHB = mysql.getRowSqlRS(InvalidSessSQL);
			
			int bcount = 0;
			String SessTr = "";
			while (resMHB.next())
			{
				bcount++;
				
				SessTr = resMHB.getString("InSess").trim();
				CLog.writeLogVS("Invalid Sess [" + SessTr + "]");
				
				boolean sessbol = mysql.executeUpdate(SessTr);
				CLog.writeLogVS("Invalid Sess Execute [" + sessbol + "]");


			}
			//resMHB.close();
			resMHB = null;
			CLog.writeLogVS("Invalid Sess Count [" + bcount + "]");			
				
		} 
		catch (Exception e) 
		{
			System.out.println(e.toString());
			CLog.writeLogVS(e.toString());
			e.printStackTrace();
			CLog.writeLogVS(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
		} 
		finally
		{
			obInterface=null;
			mysql.close();
			mysql=null;
		}
		CLog.writeLogVS("End Invalid Sess in Console");
	}

	
	////////---------
	//////////--------
	
	
	//========SMS Boom Prevention Code - Written by K
	public boolean Valid2Add(String termID, String eventCode)
	{
		ObInterface obInterface=new ObInterface();

		try
		{
			try 
			{
				//dataSource=(DataSource)factory.getBean("dataSource");
				//mysql=new Mysql(dataSource);
				//obInterface.setDataSource(dataSource);
			}	
			catch (Exception e)	
			{
				//dataSource=(DataSource)factory.getBean("dataSource");
				//mysql=new Mysql(dataSource);
				//obInterface.setDataSource(dataSource);
				
				CLog.writeLogVS("GET CONNECTION AT SMS BOOM FAIL");
				CLog.writeLogVS(e.toString());
				e.printStackTrace();
				
				obInterface=null;
				mysql=null;
				
				return false;
				
			}
			mysql.setObInterface(obInterface);

			
			Collection	interfaceInfos = null;
			try
			{
				//interfaceInfos=mysql.select("T_TML_EVENT","C_TERMID='"+termID+"' and C_CODE='"+eventCode+"' order by C_TIME DESC");
				//Iterator it=interfaceInfos.iterator();
				
				String EventCheckSQL = "select * from T_TML_EVENT where C_TERMID = '"+termID+"' and C_CODE = '"+eventCode+"' order by C_TIME DESC   ";
				CLog.writeLogVS(EventCheckSQL);
				interfaceInfos=mysql.selectSql(EventCheckSQL);

			}
			catch(Exception ValidEx)
			{
				CLog.writeLogVS("T_TML_EVENT SQL ERROR");
				CLog.writeLogVS(ValidEx.toString());
				ValidEx.printStackTrace();
				
				return false;

			}

			if (interfaceInfos != null)
			{
				Iterator it=interfaceInfos.iterator();


				ObInterface temp1;
				String eTime = "";

				for	(;it.hasNext();)
				{						
					temp1 =	(ObInterface)it.next();
					eTime = temp1.getString("C_TIME");			
					//lAmount = Long.parseLong(temp1.getString("C_LIMITAMT"));
					//App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")
					System.out.println("DATETIME OF EVENT BEFORE PARSE [" +eTime+ "]");
					CLog.writeLogVS("DATETIME OF EVENT BEFORE PARSE [" +eTime+ "]");
					
					it = null;
					interfaceInfos = null;
					
					break;
				}

				if (eTime.equals("")) return true;

				SimpleDateFormat formatter;
				Date eDate = null, vDate = null, cDate = null;

				formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

				try
				{
					eDate = (Date)formatter.parse(eTime);
					System.out.println("LASTEST EVENT DATE IN DB: [" +eDate+ "]");
					CLog.writeLogVS("LASTEST EVENT DATE IN DB: [" +eDate+ "]");

					java.util.Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.HOUR, -3);//So gio thut lui		    
					//calendar.add(Calendar.HOUR,14);
					//calendar.add(Calendar.MINUTE,55);
					//vDate = (Date)formatter.parse((calendar.getTime().toString()));
					vDate = calendar.getTime();
					System.out.println("VALID DATA: [" +vDate+ "]");
					CLog.writeLogVS("VALID DATA: [" +vDate+ "]");
				}
				catch (Exception e)
				{
					System.out.println("ERROR TO CONVERT TO DATE");
					System.out.println(e.toString());
					e.printStackTrace();
					
					CLog.writeLogVS("ERROR TO CONVERT TO DATE");
					CLog.writeLogVS(e.toString());
					
					return false;
				}

				if (vDate.compareTo(eDate) >= 0)
				{
					System.out.println("OK! THIS EVENT IS 03 HRS LARGER THAN LASTEST EVENT");
					CLog.writeLogVS("OK! THIS EVENT IS 03 HRS LARGER THAN LASTEST EVENT");
					
					//Only add if time is from 8 to 22
					java.util.Calendar luckyTime = java.util.Calendar.getInstance();
					int luckyHour = luckyTime.get(java.util.Calendar.HOUR_OF_DAY);
					CLog.writeLogVS("The hour now is [" + luckyHour + "]");
					
					if (luckyHour >= 8 && luckyHour <= 22) return true;
					else return false;
					
					
				}
			}
		}
		catch(Exception eventE)
		{
			System.out.println("OK! CAN NOTTTTTTT ADD NEW EVENT");
			System.out.println(eventE.toString());
			eventE.printStackTrace();
			
			CLog.writeLogVS("OK! CAN NOTTTTTTT ADD NEW EVENT");
			CLog.writeLogVS(eventE.toString());
			
			
			return false;
			
		}
		finally
		{
			//-------Do NOT close the connection
			//obInterface=null;
			//mysql=null;
		}
		
		return false;
	}
	//========End of Function Valid
	
	
	//End of Class	
}


class DaemonThread extends Thread 
{
	
	//private	static int mytimeout=20;
	private	static int mytimeout=25;

	public DaemonThread(int mytimeout) 
	{
		System.out.println("Raise 1");
		this.mytimeout=mytimeout;
	}

	/*线程的run方法，它将和其他线程同时运行*/
	public void run() 
	{
		new	ViewUnpack();
		while (true) 
		{
			System.out.println("Raise 2");
			String	lastdatetime=App.getCurTime14Str();
			CLog.writeLogVS("------datetime:"+lastdatetime+",start sleep------");
			
			this.setSleep(1000*60*25);
			try	
			{
				CLog.writeLogVS("------datetime:"+lastdatetime+",start update------");
				new	ViewUnpack(lastdatetime,0);
				System.out.println("The Datetime that go to the beach: [" + lastdatetime + "]");
				
				////////--------
				ThreadMngr tm = new ThreadMngr();
				if (!tm.CheckAliveThread())
				{
					tm.StopAllThread();
					tm.StartAllThread();
				}
				////////--------

			} 
			catch (Exception e) 
			{
				CLog.writeLogVS("Exception at Offline run");
				CLog.writeLogVS(e.toString());
				CLog.writeLogVS(e.getMessage());
				
				e.printStackTrace();
			}
		}
	}

	public void setSleep(long sleeptime) 
	{
		try 
		{
			CLog.writeLogVS("------start sleep:"+sleeptime/1000.0+" Seconds----------");
			Thread.sleep(sleeptime);
			CLog.writeLogVS("------end   sleep:"+sleeptime/1000.0+" Seconds----------");
		} 
		catch (InterruptedException ex) 
		{
			ex.printStackTrace();
		}
	}
}


class EventThread extends Thread 
{
	private	static int mytimeout=25;

	public EventThread() {
		System.out.println("Check Event");
		CLog.writeLogVS("Check Event");
	}
	public EventThread(int mytimeout) {
		this.mytimeout=mytimeout;
		
		System.out.println("Check Event");
		CLog.writeLogVS("Check Event");
	}

	public void run() 
	{
		new	ViewUnpack();
		while (true) 
		{
			
			try	
			{
				Thread.sleep(1000*60*25);
				
				//--------
				new	ViewUnpack(0);
				//new	ViewUnpack(App.getCurTime14Str(),0);
				
				//SMS
				String[] args = {};
				ccas.SMSServer.main(args);
				//--------
				
			} 
			catch (Exception e) 
			{
				CLog.writeLogVS("Exception at Event run");
				CLog.writeLogVS(e.toString());
				CLog.writeLogVS(e.getMessage());
				
				e.printStackTrace();

			}
		}
	}
	
}



class InSessThread extends TimerTask 
{
	public InSessThread() 
	{
		System.out.println("Check Invalid Session");
		CLog.writeLogVS("Check Invalid Session");
	}

	public void run() 
	{
		new	ViewUnpack();
		try	
		{
			//--------
			ViewUnpack vu = new	ViewUnpack();
			vu.InvalidSess();
			//vu.InvalidSessRS();
			//--------

		} 
		catch (Exception e) 
		{
			CLog.writeLogVS("Exception at InSess run");
			CLog.writeLogVS(e.toString());
			CLog.writeLogVS(e.getMessage());

			e.printStackTrace();
		}
	}
}


