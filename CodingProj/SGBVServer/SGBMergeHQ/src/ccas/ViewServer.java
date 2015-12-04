/*******************************
 * @file: ViewServer.java
 * @author: Jeremy Lee
 * @copyright: GRGBanking Equipment Co., Ltd
 * @version: 1.0
 * @date: 2015/4/9
 * @brief: ç›‘æ�§æœ�å�¡ç«¯å¤„ç�†ï¼Œå®�ç�°äº¤æ˜“æ�¥æ–‡å¤„ç�†å’Œäº‹ä»¶å¤„ç�†
 * @details: 
 *
 * @history
 *******************************/
package ccas;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.CRC32;

import javax.sql.DataSource;
import javax.naming.*;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/*******************************
 * @class: ViewServer
 * @brief:
 * @details:
 *******************************/
public class ViewServer extends Thread {
	private static int mytimeout = 20;

	public ViewServer() throws IOException {
	}

	public static void main(String args[]) throws Exception {
		DataStorage storage = new DataStorage();

		// å¼€å�¯TCPæœ�å�¡
		TCPServer tcpServer = new TCPServer(8887);
		tcpServer.startListen(storage);

		// å¼€å�¯UDPæœ�å�¡
		UDPServer udpServer = new UDPServer(8888);
		udpServer.startListen(storage);

		// å¼€å�¯ä¸�å�¡å¤„ç�†æœ�å�¡
		BusinessHandler business = new BusinessHandler(storage);
		new Thread(business).start();

		DaemonThread daemonThread = new DaemonThread(mytimeout);
		// è®¾ç½®ä¸ºå®ˆæ�¤çº¿ç¨‹
		daemonThread.setDaemon(true);
		daemonThread.start();

		EventThread EventThread = new EventThread();
		// è®¾ç½®ä¸ºå®ˆæ�¤çº¿ç¨‹
		EventThread.setDaemon(true);
		EventThread.start();
	}
}

/*******************************
 * @class: TCPServer
 * @brief: TCPå��è®®æœ�å�¡ç«¯
 * @details:
 *******************************/
class TCPServer extends Thread {
	private static TcpSelectorLoop selectors;
	private ServerSocketChannel srvSocketChannel;
	private ServerSocket srvSocket;
	private static int PORT;

	TCPServer(int port) {
		PORT = port;
	}

	/**
	 * Summary: Parameters: Param1: Param2: Return: true is successfully
	 **/
	public void startListen(DataStorage storage) throws Exception {
		// æ‰“å¼€é€‰æ‹©å™¨å¤„ç�†
		selectors = new TcpSelectorLoop(storage);
		// æ‰“å¼€æœ�å�¡é€�é�“
		srvSocketChannel = ServerSocketChannel.open();
		// å°†é€�é�“è®¾ç½®ä¸ºé��é˜»å¡�
		srvSocketChannel.configureBlocking(false);
		// ç»‘å®�æœ�å�¡ç«¯ç«¯å�£
		srvSocket = srvSocketChannel.socket();
		srvSocket.bind(new InetSocketAddress(PORT));
		// æ³¨å†Œè¿�æ�¥äº‹ä»¶
		srvSocketChannel.register(selectors.getSelector(),
				SelectionKey.OP_ACCEPT);
		// å¼€å�¯é€‰æ‹©å™¨çº¿ç¨‹
		new Thread(selectors).start();

		System.out.println("The TCP 8887 is started");
		CLog.writeLogVS("The TCP 8887 is started");
	}
}

/*******************************
 * @class: UDPServer
 * @brief: UDPå��è®®æœ�å�¡ç«¯
 * @details:
 *******************************/
class UDPServer extends Thread {
	private static UDPSelectorLoop selectors;
	private DatagramChannel datagramChannel;
	private DatagramSocket datagramSocket;
	private static int PORT;

	UDPServer(int port) {
		PORT = port;
	}

	/**
	 * Summary: Parameters: Param1: Param2: Return: true is successfully
	 **/
	public void startListen(DataStorage storage) throws Exception {
		// æ‰“å¼€é€‰æ‹©å™¨å¤„ç�†
		selectors = new UDPSelectorLoop(storage);
		// æ‰“å¼€æœ�å�¡é€�é�“
		datagramChannel = DatagramChannel.open();
		// å°†é€�é�“è®¾ç½®ä¸ºé��é˜»å¡�
		datagramChannel.configureBlocking(false);
		// ç»‘å®�æœ�å�¡ç«¯ç«¯å�£
		datagramSocket = datagramChannel.socket();
		datagramSocket.bind(new InetSocketAddress(PORT));
		// æ³¨å†Œè¿�æ�¥äº‹ä»¶
		datagramChannel.register(selectors.getSelector(), SelectionKey.OP_READ);
		// å¼€å�¯é€‰æ‹©å™¨çº¿ç¨‹
		new Thread(selectors).start();

		System.out.println("The UDP 8888 is started");
		CLog.writeLogVS("The UDP 8888 is started");

	}
}

/*******************************
 * @class: TCPTcpSelectorLoop
 * @brief: TCPé€‰æ‹©å™¨å¤„ç�†
 * @details:
 *******************************/
class TcpSelectorLoop implements Runnable {
	private Selector selector;
	private ByteBuffer recvBuf;
	private DataStorage dataStorage;

	public TcpSelectorLoop(DataStorage storage) throws Exception {
		this.selector = Selector.open();
		this.recvBuf = ByteBuffer.allocate(1024);
		this.dataStorage = storage;
	}

	public Selector getSelector() {
		return this.selector;
	}

	public void run() {
		while (true) {
			try {
				this.selector.select();
				Set<SelectionKey> selectKeys = this.selector.selectedKeys();
				Iterator<SelectionKey> it = selectKeys.iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					it.remove();
					// å¤„ç�†äº‹ä»¶. å�¯ä»¥ç”¨å¤�çº¿ç¨‹æ�¥å¤„ç�†.
					this.dispatch(key);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void dispatch(SelectionKey key) throws IOException,
	InterruptedException {
		if (key.isAcceptable()) {
			// è¿™æ˜¯ä¸€ä¸ªconnection acceptäº‹ä»¶, å¹¶ä¸”è¿™ä¸ªäº‹ä»¶æ˜¯æ³¨å†Œåœ¨serversocketchannelä¸�ç�„.
			ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
			// æ�¥å�—ä¸€ä¸ªè¿�æ�¥.
			SocketChannel sc = ssc.accept();
			// å¯¹æ–°ç�„è¿�æ�¥ç�„channelæ³¨å†Œreadäº‹ä»¶
			sc.configureBlocking(false);
			sc.register(this.getSelector(), SelectionKey.OP_READ);
		} 
		else if (key.isReadable()) {
			// è¿™æ˜¯ä¸€ä¸ªreadäº‹ä»¶,å¹¶ä¸”è¿™ä¸ªäº‹ä»¶æ˜¯æ³¨å†Œåœ¨socketchannelä¸�ç�„.
			SocketChannel socketChannel = (SocketChannel) key.channel();
			// æ¸…ç©ºç¼“å†²åŒº
			this.recvBuf.clear();
			// å†™æ•°æ�®åˆ°buffer

			socketChannel.configureBlocking(false);		
			int count = socketChannel.read(this.recvBuf);
			System.out.println("Scoket Status " + socketChannel.isConnected() + " Blocking " + socketChannel.isBlocking() + " Count " + count);
			CLog.writeLog("Scoket Status " + socketChannel.isConnected() + " Blocking " + socketChannel.isBlocking() + " Count " + count);

			if (count < 0) {
				// å®¢æˆ·ç«¯å·²ç»�æ–­å¼€è¿�æ�¥.
				System.out.println("close socket");
				socketChannel.close();
				key.cancel();
				return;
			}

			// æ��æ•°æ�®å†™å…¥é˜Ÿåˆ—ä¸­
			recvBuf.flip();
			String msg = Charset.forName("UTF-8").decode(recvBuf).toString();
			System.out.println("The String is [" + msg + "]");
			CLog.writeLogVS("The String is [" + msg + "]");			
			dataStorage.push(msg);


			/*////////-------------------------------------------------------------------------------
			if (msg.length() <= 16) return;

			//è�·å�–æ�¥æ–‡ä½“å’ŒCRC32æ ¡éªŒç �
			int    msgLen = msg.length();
			String msgBody = msg.substring(8, msgLen-8);
			String msgCRC32 = msg.substring(msgLen-8);

			// æ ¡éªŒCRC32
			CRC32 crc32 = new CRC32(); 
			crc32.update(msgBody.getBytes());
			String newCRC32 = Long.toHexString(crc32.getValue());
			if (msgCRC32.equalsIgnoreCase(newCRC32))
			{
				// å�‘é€�ä¸�å�¡å¤„ç�†çº¿ç¨‹
				dataStorage.push(msgBody);
				// è®¾ç½®æ�¥æ”¶æˆ�å�Ÿ
				msg = "00";
			} 
			else 
			{
				// è®¾ç½®CRC32æ ¡éªŒå¤±è´¥
				msg = "01";
			}
			 *////////-------------------------------------------------------------------------------

			// å�‘å®¢æˆ·ç«¯å“�åº”
			socketChannel.write(ByteBuffer.wrap(msg.getBytes(Charset.forName("UTF-8"))));
		}
	}
}

/*******************************
 * @class: UDPSelectorLoop
 * @brief: UDPé€‰æ‹©å™¨å¤„ç�†
 * @details:
 *******************************/
class UDPSelectorLoop implements Runnable {
	private Selector selector;
	private ByteBuffer recvBuf;
	private DataStorage dataStorage;

	public UDPSelectorLoop(DataStorage storage) throws Exception {
		this.selector = Selector.open();
		this.recvBuf = ByteBuffer.allocate(1024);
		this.dataStorage = storage;
	}

	public Selector getSelector() {
		return this.selector;
	}

	public void run() {
		while (true) {
			// é˜»å¡�,å�ªæœ‰å½“è‡³å°‘ä¸€ä¸ªæ³¨å†Œç�„äº‹ä»¶å�‘ç”Ÿç�„æ—¶å€™æ‰�ä¼�ç»§ç»­.
			try {
				this.selector.select();
			} catch (Exception e) {
				e.printStackTrace();
			}

			Set<SelectionKey> selectKeys = this.selector.selectedKeys();
			Iterator<SelectionKey> it = selectKeys.iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				it.remove();
				// å¤„ç�†äº‹ä»¶. å�¯ä»¥ç”¨å¤�çº¿ç¨‹æ�¥å¤„ç�†.
				try {
					this.dispatch(key);
				} catch (IOException e) {
					// e.printStackTrace();
					// åˆ é™¤äº‹ä»¶
					key.cancel();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void dispatch(SelectionKey key) throws IOException,
	InterruptedException {
		if (key.isAcceptable()) {
			// è¿™æ˜¯ä¸€ä¸ªconnection acceptäº‹ä»¶, å¹¶ä¸”è¿™ä¸ªäº‹ä»¶æ˜¯æ³¨å†Œåœ¨serversocketchannelä¸�ç�„.
			ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
			// æ�¥å�—ä¸€ä¸ªè¿�æ�¥.
			SocketChannel sc = ssc.accept();
			// å¯¹æ–°ç�„è¿�æ�¥ç�„channelæ³¨å†Œreadäº‹ä»¶
			sc.configureBlocking(false);
			sc.register(this.getSelector(), SelectionKey.OP_READ);
		} else if (key.isReadable()) {
			// è¿™æ˜¯ä¸€ä¸ªreadäº‹ä»¶,å¹¶ä¸”è¿™ä¸ªäº‹ä»¶æ˜¯æ³¨å†Œåœ¨socketchannelä¸�ç�„.
			DatagramChannel datagramChannel = (DatagramChannel) key.channel();
			// æ¸…ç©ºç¼“å†²åŒº
			recvBuf.clear();
			// å†™æ•°æ�®åˆ°buffer
			SocketAddress sa = datagramChannel.receive(recvBuf);
			// æ��æ•°æ�®å†™å…¥é˜Ÿåˆ—ä¸­
			recvBuf.flip();
			String msg = Charset.forName("UTF-8").decode(recvBuf).toString();
			System.out.println("The String is [" + msg + "]");
			CLog.writeLogVS("The String is [" + msg + "]");			
			dataStorage.push(msg);
		}
	}
}

/*******************************
 * @class: DataStorage
 * @brief: æ•°æ�®ä»“åº“
 * @details:
 *******************************/
class DataStorage {
	BlockingQueue<String> queues = new LinkedBlockingQueue<String>(50000);

	public void push(String p) throws InterruptedException {
		queues.put(p);
	}

	public String pop() throws InterruptedException {
		return queues.take();
	}

	public int getSize() throws InterruptedException {
		return queues.size();
	}
}

/*******************************
 * @class: BusinessHandler
 * @brief: ä¸�å�¡å¤„ç�†ç±»
 * @details: è¿›è¡Œæ�¥æ–‡è§£æ��å’Œæ•°æ�®å¤„ç�†
 *******************************/
class BusinessHandler implements Runnable {
	private DataStorage dataStorage;
	// çº¿ç¨‹æ± 
	private ExecutorService executorService;
	private int count = 1;

	BusinessHandler(DataStorage storage) {
		this.dataStorage = storage;
		// Runtimeç�„availableProcessor()æ–¹æ³•è¿”å›�å½“å‰�ç³»ç»Ÿç�„CPUæ•°ç›®.
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors());
		System.out.println("CPU Number:"
				+ Runtime.getRuntime().availableProcessors());
	}

	public void run() {
		String data;
		while (true) {
			try {
				data = null;
				data = this.dataStorage.pop();
				// System.out.println("Client received [" + data + "] ["
				// + this.dataStorage.getSize() + "]");

				System.out.println("The Origin Data from ATM is [" + data + "]");
				CLog.writeLogVS("The Origin Data from ATM is [" + data + "]");
				executorService.execute(new ViewTransParser(data));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

class ViewTransParser implements Runnable {
	private String mypackage = null;
	public static final String US = Character.toString((char) 0x1F);
	private JDBCDBConn mysql = null;

	ViewTransParser(String mypackage) {
		this.mypackage = mypackage;
	}

	public void run() {
		try {
			if (mysql == null) {
				mysql = new JDBCDBConn();
			}
			unpackTrans();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (mysql != null) {
				mysql.free();
				mysql = null;
			}
		}
	}

	public boolean unpackTrans() {

		if (mypackage != null && mypackage.length() > 0)
		{
			System.out.println("The Data Transfer to Unpack SPLITTED into  [" + mypackage.split(US, -1).length + "] parts");
			CLog.writeLogVS("The Data Transfer to Unpack SPLITTED into  [" + mypackage.split(US, -1).length + "] parts");
		}

		if (mypackage == null || mypackage.length() < 10 || mypackage.split(US, -1).length < 6)
			return false;
		// mypackage += US + "0" + US + "0" + US + "0";

		System.out.println("The Data Transfer to Unpack is [" + mypackage + "]");
		CLog.writeLogVS("The Data Transfer to Unpack is [" + mypackage + "]");

		System.out.println("The Message Type is [" + mypackage.split(US, -1)[0] + "] Boolean is " + mypackage.split(US, -1)[0].trim().equals("10002"));
		CLog.writeLogVS("The Message Type is [" + mypackage.split(US, -1)[0] + "] Boolean is " + mypackage.split(US, -1)[0].trim().equals("10002"));

		if (mypackage.split(US, -1)[0].trim().equals("0002") || mypackage.split(US, -1)[0].trim().equals("10002")) {
			// 10002è®¾å¤‡ç�¶æ€�æ•°æ�®
			System.out.println("Can go to 10002");
			CLog.writeLog("Can go to 10002");

			parserTermStatus();
		} 
		else if (mypackage.split(US, -1)[0].trim().equals("0004") 
				|| mypackage.split(US, -1)[0].trim().equals("10004") 
				|| mypackage.split(US, -1)[0].trim().equals("10008") 
				|| mypackage.split(US, -1)[0].trim().equals("0008")        ) {
			// 10004/10008å� é’�/å‡�é’�æ�¥å‘�
			parserAddCash();
		} 
		else if (mypackage.split(US, -1)[0].trim().equals("0003") || mypackage.split(US, -1)[0].trim().equals("10003")        ) {
			// 10003å��å�¡æ�¥å‘�
			parserCaptureCard();
		}
		return true;
	}

	/*
	 * è§£æ��è®¾å¤‡ç�¶æ€�åŒ…äº¤æ˜“
	 */
	private void parserTermStatus() {
		String mypackBuf[] = mypackage.split(US,-1);//split(US)

		System.out.println("In the Status is [" + mypackBuf.length + "] parts");
		CLog.writeLog("In the Status is [" + mypackBuf.length + "] parts");

		for (int i=0; i<mypackBuf.length; i++)
		{
			System.out.println("Value at [" + i + "] is [" + mypackBuf[i] + "]");
			CLog.writeLog("Value at [" + i + "] is [" + mypackBuf[i] + "]");
		}


		if (mypackBuf.length < 45) return;//50

		System.out.println("In the Status 2 is [" + mypackBuf.length + "] parts");
		CLog.writeLog("In the Status 2 is [" + mypackBuf.length + "] parts");

		long stime = System.currentTimeMillis();
		ObInterface obInterface=new ObInterface();
		mypackage+=US+"0"+US+"0"+US+"0";

		try 
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
				System.out.println("### THE Console SYSTEM TIME IS [" + sdf.format(resultdate) + "]");

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
				//mysql.delete("T_TML_STATUS","C_TERMID='"+obInterface.getTmid()+"'");
				mysql.executeUpdate("delete from T_TML_STATUS where C_TERMID='" + obInterface.getTmid().trim() + "'");

				CLog.writeLogVS("insert into T_TML_STATUS values('"+obInterface.getTmid()+"','"+mnstatus+"','"+obInterface.getMudt()+"','"+mypackage.split(US, -1)[6]+"','"+mypackage.split(US, -1)[7]+"','"+mypackage.split(US, -1)[8]+"','"+mypackage.split(US, -1)[9]+"','"+mypackage.split(US, -1)[10]+"','"+mypackage.split(US, -1)[11]+"','"+mypackage.split(US, -1)[12]+"','"+mypackage.split(US, -1)[13]+"','"+mypackage.split(US, -1)[14]+"','"+mypackage.split(US, -1)[15]+"','"+mypackage.split(US, -1)[16]+"','"+mypackage.split(US, -1)[17]+"','"+mypackage.split(US, -1)[18]+"','"+mypackage.split(US, -1)[19]+"','"+mypackage.split(US, -1)[20]+"','"+mypackage.split(US, -1)[21]+"','"+mypackage.split(US, -1)[22]+"','"+mypackage.split(US, -1)[23]+"','"+mypackage.split(US, -1)[24]+"','"+mypackage.split(US, -1)[25]+"','"+mypackage.split(US, -1)[26]+"','"+mypackage.split(US, -1)[27]+"','"+mypackage.split(US, -1)[28]+"','"+mypackage.split(US, -1)[29]+"','"+mypackage.split(US, -1)[30]+"','"+mypackage.split(US, -1)[31]+"','"+mypackage.split(US, -1)[32]+"','"+mypackage.split(US, -1)[33]+"','"+mypackage.split(US, -1)[34]+"','"+mypackage.split(US, -1)[35]+"','"+mypackage.split(US, -1)[36]+"','"+mypackage.split(US, -1)[37]+"','"+mypackage.split(US, -1)[38]+"','','','"+mypackage.split(US, -1)[39]+"','0','"+mypackage.split(US, -1)[49]+"','"+mypackage.split(US, -1)[40].split("\\$").length+"','"+mypackage.split(US, -1)[40].split("\\$")[0]+"','"+mypackage.split(US, -1)[43].split("\\$")[0]+"','"+mypackage.split(US, -1)[42].split("\\$")[0]+"','"+mypackage.split(US, -1)[46].split("\\$")[0]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[1]+"','"+mypackage.split(US, -1)[43].split("\\$")[1]+"','"+mypackage.split(US, -1)[42].split("\\$")[1]+"','"+mypackage.split(US, -1)[46].split("\\$")[1]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[2]+"','"+mypackage.split(US, -1)[43].split("\\$")[2]+"','"+mypackage.split(US, -1)[42].split("\\$")[2]+"','"+mypackage.split(US, -1)[46].split("\\$")[2]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[3]+"','"+mypackage.split(US, -1)[43].split("\\$")[3]+"','"+mypackage.split(US, -1)[42].split("\\$")[3]+"','"+mypackage.split(US, -1)[46].split("\\$")[3]+"','"+obInterface.getMudt()+"','0','0')");
				mysql.executeUpdate("insert into T_TML_STATUS values('"+obInterface.getTmid()+"','"+mnstatus+"','"+obInterface.getMudt()+"','"+mypackage.split(US, -1)[6]+"','"+mypackage.split(US, -1)[7]+"','"+mypackage.split(US, -1)[8]+"','"+mypackage.split(US, -1)[9]+"','"+mypackage.split(US, -1)[10]+"','"+mypackage.split(US, -1)[11]+"','"+mypackage.split(US, -1)[12]+"','"+mypackage.split(US, -1)[13]+"','"+mypackage.split(US, -1)[14]+"','"+mypackage.split(US, -1)[15]+"','"+mypackage.split(US, -1)[16]+"','"+mypackage.split(US, -1)[17]+"','"+mypackage.split(US, -1)[18]+"','"+mypackage.split(US, -1)[19]+"','"+mypackage.split(US, -1)[20]+"','"+mypackage.split(US, -1)[21]+"','"+mypackage.split(US, -1)[22]+"','"+mypackage.split(US, -1)[23]+"','"+mypackage.split(US, -1)[24]+"','"+mypackage.split(US, -1)[25]+"','"+mypackage.split(US, -1)[26]+"','"+mypackage.split(US, -1)[27]+"','"+mypackage.split(US, -1)[28]+"','"+mypackage.split(US, -1)[29]+"','"+mypackage.split(US, -1)[30]+"','"+mypackage.split(US, -1)[31]+"','"+mypackage.split(US, -1)[32]+"','"+mypackage.split(US, -1)[33]+"','"+mypackage.split(US, -1)[34]+"','"+mypackage.split(US, -1)[35]+"','"+mypackage.split(US, -1)[36]+"','"+mypackage.split(US, -1)[37]+"','"+mypackage.split(US, -1)[38]+"','','','"+mypackage.split(US, -1)[39]+"','0','"+mypackage.split(US, -1)[49]+"','"+mypackage.split(US, -1)[40].split("\\$").length+"','"+mypackage.split(US, -1)[40].split("\\$")[0]+"','"+mypackage.split(US, -1)[43].split("\\$")[0]+"','"+mypackage.split(US, -1)[42].split("\\$")[0]+"','"+mypackage.split(US, -1)[46].split("\\$")[0]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[1]+"','"+mypackage.split(US, -1)[43].split("\\$")[1]+"','"+mypackage.split(US, -1)[42].split("\\$")[1]+"','"+mypackage.split(US, -1)[46].split("\\$")[1]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[2]+"','"+mypackage.split(US, -1)[43].split("\\$")[2]+"','"+mypackage.split(US, -1)[42].split("\\$")[2]+"','"+mypackage.split(US, -1)[46].split("\\$")[2]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[3]+"','"+mypackage.split(US, -1)[43].split("\\$")[3]+"','"+mypackage.split(US, -1)[42].split("\\$")[3]+"','"+mypackage.split(US, -1)[46].split("\\$")[3]+"','"+obInterface.getMudt()+"','0','0')");
				//mysql.executeUpdate("insert into T_TML_STATUS values('"+obInterface.getTmid()+"','"+mnstatus+"','"+sdf.format(resultdate)+"','"+mypackage.split(US, -1)[6]+"','"+mypackage.split(US, -1)[7]+"','"+mypackage.split(US, -1)[8]+"','"+mypackage.split(US, -1)[9]+"','"+mypackage.split(US, -1)[10]+"','"+mypackage.split(US, -1)[11]+"','"+mypackage.split(US, -1)[12]+"','"+mypackage.split(US, -1)[13]+"','"+mypackage.split(US, -1)[14]+"','"+mypackage.split(US, -1)[15]+"','"+mypackage.split(US, -1)[16]+"','"+mypackage.split(US, -1)[17]+"','"+mypackage.split(US, -1)[18]+"','"+mypackage.split(US, -1)[19]+"','"+mypackage.split(US, -1)[20]+"','"+mypackage.split(US, -1)[21]+"','"+mypackage.split(US, -1)[22]+"','"+mypackage.split(US, -1)[23]+"','"+mypackage.split(US, -1)[24]+"','"+mypackage.split(US, -1)[25]+"','"+mypackage.split(US, -1)[26]+"','"+mypackage.split(US, -1)[27]+"','"+mypackage.split(US, -1)[28]+"','"+mypackage.split(US, -1)[29]+"','"+mypackage.split(US, -1)[30]+"','"+mypackage.split(US, -1)[31]+"','"+mypackage.split(US, -1)[32]+"','"+mypackage.split(US, -1)[33]+"','"+mypackage.split(US, -1)[34]+"','"+mypackage.split(US, -1)[35]+"','"+mypackage.split(US, -1)[36]+"','"+mypackage.split(US, -1)[37]+"','"+mypackage.split(US, -1)[38]+"','','','"+mypackage.split(US, -1)[39]+"','0','"+mypackage.split(US, -1)[49]+"','"+mypackage.split(US, -1)[40].split("\\$").length+"','"+mypackage.split(US, -1)[40].split("\\$")[0]+"','"+mypackage.split(US, -1)[43].split("\\$")[0]+"','"+mypackage.split(US, -1)[42].split("\\$")[0]+"','"+mypackage.split(US, -1)[46].split("\\$")[0]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[1]+"','"+mypackage.split(US, -1)[43].split("\\$")[1]+"','"+mypackage.split(US, -1)[42].split("\\$")[1]+"','"+mypackage.split(US, -1)[46].split("\\$")[1]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[2]+"','"+mypackage.split(US, -1)[43].split("\\$")[2]+"','"+mypackage.split(US, -1)[42].split("\\$")[2]+"','"+mypackage.split(US, -1)[46].split("\\$")[2]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[3]+"','"+mypackage.split(US, -1)[43].split("\\$")[3]+"','"+mypackage.split(US, -1)[42].split("\\$")[3]+"','"+mypackage.split(US, -1)[46].split("\\$")[3]+"','"+obInterface.getMudt()+"','0','0')");
				//mysql.executeUpdate("insert into T_TML_STATUS values('"+obInterface.getTmid()+"','"+mypackage.split(US, -1)[4]+"','"+mypackage.split(US, -1)[48]+"','"+mypackage.split(US, -1)[6]+"','"+mypackage.split(US, -1)[7]+"','"+mypackage.split(US, -1)[8]+"','"+mypackage.split(US, -1)[9]+"','"+mypackage.split(US, -1)[10]+"','"+mypackage.split(US, -1)[11]+"','"+mypackage.split(US, -1)[12]+"','"+mypackage.split(US, -1)[13]+"','"+mypackage.split(US, -1)[14]+"','"+mypackage.split(US, -1)[15]+"','"+mypackage.split(US, -1)[16]+"','"+mypackage.split(US, -1)[17]+"','"+mypackage.split(US, -1)[18]+"','"+mypackage.split(US, -1)[19]+"','"+mypackage.split(US, -1)[20]+"','"+mypackage.split(US, -1)[21]+"','"+mypackage.split(US, -1)[22]+"','"+mypackage.split(US, -1)[23]+"','"+mypackage.split(US, -1)[24]+"','"+mypackage.split(US, -1)[25]+"','"+mypackage.split(US, -1)[26]+"','"+mypackage.split(US, -1)[27]+"','"+mypackage.split(US, -1)[28]+"','"+mypackage.split(US, -1)[29]+"','"+mypackage.split(US, -1)[30]+"','"+mypackage.split(US, -1)[31]+"','"+mypackage.split(US, -1)[32]+"','"+mypackage.split(US, -1)[33]+"','"+mypackage.split(US, -1)[34]+"','"+mypackage.split(US, -1)[35]+"','"+mypackage.split(US, -1)[36]+"','"+mypackage.split(US, -1)[37]+"','"+mypackage.split(US, -1)[38]+"','','','"+mypackage.split(US, -1)[39]+"','0','"+mypackage.split(US, -1)[49]+"','"+mypackage.split(US, -1)[40].split("\\$").length+"','"+mypackage.split(US, -1)[40].split("\\$")[0]+"','"+mypackage.split(US, -1)[43].split("\\$")[0]+"','"+mypackage.split(US, -1)[42].split("\\$")[0]+"','"+mypackage.split(US, -1)[46].split("\\$")[0]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[1]+"','"+mypackage.split(US, -1)[43].split("\\$")[1]+"','"+mypackage.split(US, -1)[42].split("\\$")[1]+"','"+mypackage.split(US, -1)[46].split("\\$")[1]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[2]+"','"+mypackage.split(US, -1)[43].split("\\$")[2]+"','"+mypackage.split(US, -1)[42].split("\\$")[2]+"','"+mypackage.split(US, -1)[46].split("\\$")[2]+"','"+obInterface.getMudt()+"','0','0','"+mypackage.split(US, -1)[40].split("\\$")[3]+"','"+mypackage.split(US, -1)[43].split("\\$")[3]+"','"+mypackage.split(US, -1)[42].split("\\$")[3]+"','"+mypackage.split(US, -1)[46].split("\\$")[3]+"','"+obInterface.getMudt()+"','0','0')");


				//--------Update for each cashbox
				//mysql.delete("T_TML_CASHBOX", (new StringBuilder("C_TERMID='")).append(obInterface.getTmid()).append("'").toString());
				mysql.executeUpdate("delete from T_TML_CASHBOX where C_TERMID='" + obInterface.getTmid().trim() + "'");

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
							//hm
							Iterator SecondIT=mysql.SgbSelect("select * from " + SecondTable+ " where C_TERMID= '" + obInterface.getTmid() + "' order by c_loadtime desc").iterator();
							ObInterface SecondTemp;

							System.out.println("Second IT boolean [" + SecondIT.hasNext() +"]");
							CLog.writeLogVS("Second IT boolean [" + SecondIT.hasNext() +"]");

							for	(int i=0;SecondIT.hasNext();i++)	
							{
								//SecondTemp =(ObInterface)SecondIT.next();
								//MixedLoadTime = String.valueOf(SecondTemp.get("C_LOADTIME"));
								//MixedLoadTime = MixedLoadTime.trim();
								
								HashMap row = (HashMap)SecondIT.next();
								MixedLoadTime = String.valueOf(row.get("C_LOADTIME")).trim();

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

//					if(mypackage.split(US, -1)[40].split("\\$")[j].equals("1"))
//					{
//						myevent = "Cash Shortage";
//						mycode = "A0811";
//						mylevel = "66";
//						
//						mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
//
//					}
//					if(mypackage.split(US, -1)[40].split("\\$")[j].equals("2"))
//					{
//						myevent = "Out of Cash";
//						mycode = "A0812";
//						mylevel = "66";
//						
//						mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ App.transDateFormat(obInterface.getMudt(),"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss") +"','"+ myevent +"','','','')");
//
//					}
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
				//hm
				Collection	interfaceInfos=mysql.SgbSelect("select * from T_TML_CASH_SHORT where C_TERMID= '"+obInterface.getTmid().trim()+"'");
				//Iterator it=interfaceInfos.iterator();

				if (interfaceInfos != null)
				{
					Iterator it=interfaceInfos.iterator();

					ObInterface temp1;
					for	(;it.hasNext();)
					{						
						//temp1 =	(ObInterface)it.next();						
						//lAmount = Long.parseLong(temp1.getString("C_MONEY"));
						
						HashMap row = (HashMap)it.next();
						lAmount = Long.parseLong(String.valueOf(row.get("C_MONEY")).trim());
						
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
					System.out.println("The Remain Amount in ATM is " + cAmount);
					CLog.writeLogVS("The Remain Amount in ATM is " + cAmount);
					if (cAmount <= 0)//<=0
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


				//				//Finally add for 10002
				//				//obInterface.getMudt()
				//				try
				//				{
				//					mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','"+ mycode +"','1','"+ mylevel +"','"+ obInterface.getMudt() +"','"+ myevent +"','','','')");
				//
				//				}
				//				catch(Exception e)
				//				{
				//					e.printStackTrace();
				//					CLog.writeLogVS(e.toString());
				//					CLog.writeLogVS("EXCEPTION AT EVENT CODE [" + mycode + "] AND AT EVENT [" + myevent + "]");
				//					
				//				}


			} 
			catch (Exception e) 
			{
				CLog.writeLogVS("ERROR IN STATUS REPORT MESSAGE --- START DEVICE PART");
				CLog.writeLogVS(e.toString());

				e.printStackTrace();

			}



		} 
		catch (Exception e) 
		{
			System.out.println("Has Error at 10002 Exception");
			System.out.println(e.toString());

			CLog.writeLogVS("Has Error at 10002 Exception");
			CLog.writeLogVS(e.toString());

			e.printStackTrace();
		}
	}

	//========SMS Boom Prevention Code - Written by K
	public boolean Valid2Add(String termID, String eventCode)
	{
		ObInterface obInterface=new ObInterface();

		try
		{
			Collection	interfaceInfos = null;

			try
			{
				//interfaceInfos=mysql.select("T_TML_EVENT","C_TERMID='"+termID+"' and C_CODE='"+eventCode+"' order by C_TIME DESC");
				//Iterator it=interfaceInfos.iterator();

				String EventCheckSQL = "select * from T_TML_EVENT where C_TERMID = '"+termID+"' and C_CODE = '"+eventCode+"' order by C_TIME DESC   ";
				CLog.writeLogVS(EventCheckSQL);
				//hm
				interfaceInfos=mysql.SgbSelect(EventCheckSQL);

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
					//temp1 =	(ObInterface)it.next();
					//eTime = temp1.getString("C_TIME");

					HashMap row = (HashMap)it.next();
					eTime = String.valueOf(row.get("C_TIME")).trim();				
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


	private void parserAddCash() {

		String mypackBuf[] = mypackage.split(US,-1);//split(US)
		System.out.println("In the Cashload is [" + mypackBuf.length + "] parts");
		CLog.writeLog("In the Cashload is [" + mypackBuf.length + "] parts");

		for (int i=0; i<mypackBuf.length; i++)
		{
			System.out.println("Value at [" + i + "] is [" + mypackBuf[i] + "]");
			CLog.writeLog("Value at [" + i + "] is [" + mypackBuf[i] + "]");
		}

		String SettTermId = mypackage.split(US, -1)[1].trim();

		ObInterface obInterface=new ObInterface();
		obInterface.setTmid(mypackage.split(US, -1)[1]);
		obInterface.setMudt(App.transDateFormat(mypackage.split(US, -1)[8],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss"));

		int	num=1;
		if	(mypackage.split(US, -1)[0].trim().indexOf("0008")>=0)	num=-1;

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
				Iterator TakeFromDate=mysql.SgbSelect("select * from t_tml_cashsett where c_termid = '" + SettTermId + "' order by c_loadtime desc   ").iterator();
				ObInterface TakeFromDateTemp;
				String SettFromDate = "";
				if (TakeFromDate.hasNext())
				{
					//TakeFromDateTemp =(ObInterface)TakeFromDate.next();
					//SettFromDate = TakeFromDateTemp.getString("C_LOADTIME");
					
					HashMap row = (HashMap)TakeFromDate.next();
					SettFromDate = String.valueOf(row.get("C_LOADTIME")).trim();
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
						System.out.println("Successful Add Cashload SETT Default Data at " + obInterface.getMudt());
						CLog.writeLogVS("Successful Add Cashload SETT Default Data at " + obInterface.getMudt());

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
				Iterator CheckCashBoxIT=mysql.SgbSelect("select * from t_tml_cashbox where c_termid = '" + SettTermId + "' order by C_TERMCASTTEEID asc").iterator();
				ObInterface CheckCashBoxTemp;

				System.out.println("### CheckCashBoxIT IS [" + CheckCashBoxIT.hasNext() + "]");
				CLog.writeLogVS("### CheckCashBoxIT IS [" + CheckCashBoxIT.hasNext() + "]");

				if (CheckCashBoxIT.hasNext())
				{
					for	(int i=1;CheckCashBoxIT.hasNext();i++)	
					{
						//CheckCashBoxTemp =(ObInterface)CheckCashBoxIT.next();
						HashMap row = (HashMap)CheckCashBoxIT.next();

						try
						{

							String SettIloading = String.valueOf(row.get("I_LOADING")).trim();
							if (SettIloading.equals("N")) SettIloading = "0";
							System.out.println("Iloading = [" + SettIloading +"]");
							CLog.writeLogVS("Iloading = [" + SettIloading +"]");

							String SettRemain = String.valueOf(row.get("I_Remain")).trim();
							if (SettRemain.equals("N")) SettRemain = "0";									
							System.out.println("Remain = [" + SettRemain +"]");
							CLog.writeLogVS("Remain = [" + SettRemain +"]");									

							String SettReject = String.valueOf(row.get("I_REJECT")).trim();
							if (SettReject.equals("N")) SettReject = "0";	
							System.out.println("Reject = [" + SettReject +"]");
							CLog.writeLogVS("Reject = [" + SettReject +"]");

							String SettRetract = String.valueOf(row.get("I_RETRACT")).trim();
							if (SettRetract.equals("N")) SettRetract = "0";
							System.out.println("Retract = [" + SettRetract +"]");
							CLog.writeLogVS("Retract = [" + SettRetract +"]");

							String SettDeposit = String.valueOf(row.get("I_DEPOSIT")).trim();
							if (SettDeposit.equals("N")) SettDeposit = "0";
							System.out.println("Deposit = [" + SettDeposit +"]");
							CLog.writeLogVS("Deposit = [" + SettDeposit +"]");

							String SettBoxID = String.valueOf(row.get("C_TERMCASTTEEID")).trim();

							int SettDispense = Integer.parseInt(SettIloading) + Integer.parseInt(SettDeposit) - Integer.parseInt(SettRemain) - Integer.parseInt(SettReject) - Integer.parseInt(SettRetract);
							//if (SettDispense < 0) SettDispense = (SettDispense) * (-1);
							if (SettDispense < 0) SettDispense = 0;   


							//mysql.executeUpdate("update T_TML_CASHSET set C_LOADTIME='"+obInterface.getMudt()+"',C_STATUS='"+mypackage.split(US, -1)[3].split("\\$")[j]+"',I_DENO='"+mypackage.split(US, -1)[4].split("\\$")[j]+"',C_NOTETYPE='"+mypackage.split(US, -1)[5].split("\\$")[j]+"',I_remain='"+mypackage.split(US, -1)[6].split("\\$")[j]+"',I_loading='"+numIload+"' where C_TERMID ='"+obInterface.getTmid()+"' and C_TERMCASTTEEID='"+(j+1)+"'");
							//mysql.executeUpdate("update T_TML_CASHSETT set C_LOADTIME_SET='"+row.get("C_LOADTIME")+"',I_LOADING_SET='"+row.get("I_LOADING")+"',I_DISPENSER='"+SettDispense+"',I_REMAIN='"+row.get("I_REMAIN")+"',I_REJECT='"+row.get("I_REJECT")+"',I_RETRACT='"+row.get("I_RETRACT")+"' where C_TERMID ='"+obInterface.getTmid()+"' and C_LOADTIME = (select max(c_loadtime) from t_tml_cashsett) and C_TERMCASTTEEID='"+(i)+"'");//Decrease the casett ID from i= 1 to i
							//mysql.executeUpdate("update T_TML_CASHSETT set C_LOADTIME_SET='"+SettFromDate+"',I_LOADING_SET='"+row.get("I_LOADING")+"',I_DISPENSER='"+SettDispense+"',I_REMAIN='"+row.get("I_REMAIN")+"',I_REJECT='"+row.get("I_REJECT")+"',I_RETRACT='"+row.get("I_RETRACT")+"' where C_TERMID ='"+obInterface.getTmid()+"' and C_LOADTIME = (select max(c_loadtime) from t_tml_cashsett) and C_TERMCASTTEEID='"+(i)+"'");//Decrease the casett ID from i= 1 to i
							//mysql.executeUpdate("update T_TML_CASHSETT set C_LOADTIME_SET='"+SettFromDate+"',I_LOADING_SET='"+row.get("I_LOADING")+"',I_DISPENSER='"+SettDispense+"',I_REMAIN='"+row.get("I_REMAIN")+"',I_REJECT='"+row.get("I_REJECT")+"',I_RETRACT='"+row.get("I_RETRACT")+"',I_DEPOSIT='"+row.get("I_DEPOSIT")+"' where C_TERMID ='"+obInterface.getTmid()+"' and C_LOADTIME = (select max(c_loadtime) from t_tml_cashsett) and C_TERMCASTTEEID='"+ SettBoxID +"'");//Reset the BoxID

							String updateSett = "update T_TML_CASHSETT set C_LOADTIME_SET='"+SettFromDate+"',I_LOADING_SET='"+SettIloading+"',I_DISPENSER='"+SettDispense+"',I_REMAIN='"+ SettRemain +"',I_REJECT='"+ SettReject +"',I_RETRACT='"+ SettRetract +"',I_DEPOSIT='"+ SettDeposit +"' where C_TERMID ='"+obInterface.getTmid()+"' and C_LOADTIME = (select max(c_loadtime) from t_tml_cashsett where C_TERMID = '" + obInterface.getTmid() + "') and C_TERMCASTTEEID='"+ SettBoxID +"'";
							System.out.println(updateSett);
							CLog.writeLogVS(updateSett);
							//mysql.executeUpdate("update T_TML_CASHSETT set C_LOADTIME_SET='"+SettFromDate+"',I_LOADING_SET='"+SettIloading+"',I_DISPENSER='"+SettDispense+"',I_REMAIN='"+ SettRemain +"',I_REJECT='"+ SettReject +"',I_RETRACT='"+ SettRetract +"',I_DEPOSIT='"+ SettDeposit +"' where C_TERMID ='"+obInterface.getTmid()+"' and C_LOADTIME = (select max(c_loadtime) from t_tml_cashsett) and C_TERMCASTTEEID='"+ SettBoxID +"'");//Reset the BoxID//
							mysql.executeUpdate(updateSett);//Reset the BoxID//
							CLog.writeLogVS("Successful Update AT SETT UPDATE DATA");


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

			mysql.executeUpdate("delete T_TML_CASHBOX where C_TERMID = '" + obInterface.getTmid() + "' ");
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

				CLog.writeLogVS("Update cashbox and load cash NORMAL OK");

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

	private void parserCaptureCard() {
		String mypackBuf[] = mypackage.split(US, -1);

		System.out.println("In the Status is [" + mypackBuf.length + "] parts");
		CLog.writeLog("In the Status is [" + mypackBuf.length + "] parts");

		for (int i=0; i<mypackBuf.length; i++)
		{
			System.out.println("Value at [" + i + "] is [" + mypackBuf[i] + "]");
			CLog.writeLog("Value at [" + i + "] is [" + mypackBuf[i] + "]");
		}


		if (mypackBuf.length < 6 )
			return ;

		CLog.writeLogVS("I AM CCP");

		ObInterface obInterface=new ObInterface();
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
			CLog.writeLogVS("Successfull add a Capture Card for ATM " + obInterface.getTmid());

			/////Add event/////
			mysql.executeUpdate("insert into T_TML_EVENT values('"+obInterface.getTmid()+"','ATMC','','1','99','"+App.transDateFormat(mypackage.split(US, -1)[5],"yyyyMMddHHmmss","yyyy/MM/dd HH:mm:ss")+"','Capture Card: "+obInterface.getCdno()+"','','','')");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			CLog.writeLogVS(e.toString());
		}
	}

	private String spliteTermStatusSQL(String[] mypackBuf) { 
		String sqlForStatus = "insert into T_TML_STATUS values('";
		sqlForStatus += mypackBuf[3] + "','"; // ç»ˆç«¯å�·
		sqlForStatus += mypackBuf[4] + "','"; // ç»ˆç«¯ç�¶æ€�
		sqlForStatus += mypackBuf[48] + "','"; // æ•°æ�®é‡‡é›†æ—¶é—´
		sqlForStatus += mypackBuf[6] + "','"; // ä¸»æ�§è®¾å¤‡ç�¶æ€�
		sqlForStatus += mypackBuf[7] + "','"; // ä¸»æ�§è®¾å¤‡é”™è¯¯ç �
		sqlForStatus += mypackBuf[8] + "','"; // å‡ºé’�æœºè�¯
		sqlForStatus += mypackBuf[9] + "','"; // å‡ºé’�æœºè�¯é”™è¯¯ç �
		sqlForStatus += mypackBuf[10] + "','"; // å­˜æ¬¾æœºè�¯
		sqlForStatus += mypackBuf[11] + "','"; // å­˜æ¬¾æœºè�¯é”™è¯¯ç �
		sqlForStatus += mypackBuf[12] + "','"; // å‡ºé’�é—¨
		sqlForStatus += mypackBuf[13] + "','"; // å‡ºé’�é—¨é”™è¯¯ç �
		sqlForStatus += mypackBuf[14] + "','"; // å­˜æ¬¾é—¨
		sqlForStatus += mypackBuf[15] + "','"; // å­˜æ¬¾é—¨é”™è¯¯ç �
		sqlForStatus += mypackBuf[16] + "','"; // å‡­æ�¡æ‰“å�°æœº
		sqlForStatus += mypackBuf[17] + "','"; // å‡­æ�¡æ‰“å�°æœºé”™è¯¯ç �
		sqlForStatus += mypackBuf[18] + "','"; // æµ�æ°´æ‰“å�°æœº
		sqlForStatus += mypackBuf[19] + "','"; // æµ�æ°´æ‰“å�°æœºé”™è¯¯ç �
		sqlForStatus += mypackBuf[20] + "','"; // å�‘ç¥¨æ‰“å�°æœº
		sqlForStatus += mypackBuf[21] + "','"; // å�‘ç¥¨æ‰“å�°æœºé”™è¯¯ç �
		sqlForStatus += mypackBuf[22] + "','"; // UPS
		sqlForStatus += mypackBuf[23] + "','"; // UPSé”™è¯¯ç �
		sqlForStatus += mypackBuf[24] + "','"; // ç£�å�¡è¯»å†™å™¨
		sqlForStatus += mypackBuf[25] + "','"; // ç£�å�¡è¯»å†™å™¨é”™è¯¯ç �
		sqlForStatus += mypackBuf[26] + "','"; // ICå�¡è¯»å†™å™¨
		sqlForStatus += mypackBuf[27] + "','"; // ICå�¡è¯»å†™å™¨é”™è¯¯ç �
		sqlForStatus += mypackBuf[28] + "','"; // ä¿�é™©é—¨
		sqlForStatus += mypackBuf[29] + "','"; // ä¿�é™©é—¨é”™è¯¯ç �
		sqlForStatus += mypackBuf[30] + "','"; // æœºç®±é—¨
		sqlForStatus += mypackBuf[31] + "','"; // æœºç®±é—¨é”™è¯¯ç �
		sqlForStatus += mypackBuf[32] + "','"; // ç¡¬ä»¶å� å¯†æœº
		sqlForStatus += mypackBuf[33] + "','"; // ç¡¬ä»¶å� å¯†æœºé”™è¯¯ç �
		sqlForStatus += mypackBuf[34] + "','"; // ä¿¡å°�å­˜æ¬¾ç®±
		sqlForStatus += mypackBuf[35] + "','"; // ä¿¡å°�å­˜æ¬¾ç®±é”™è¯¯ç �
		sqlForStatus += mypackBuf[36] + "','"; // ä¿¡å°�å­˜æ¬¾ä¿¡å°�æ•°
		sqlForStatus += mypackBuf[37] + "','"; // å­˜æ�˜æ‰“å�°æœº
		sqlForStatus += mypackBuf[38] + "','"; // å­˜æ�˜æ‰“å�°æœºé”™è¯¯ç �
		sqlForStatus += "','"; // åºŸé’�ç®±
		sqlForStatus += "','"; // åºŸé’�ç®±é”™è¯¯ç �
		sqlForStatus += mypackBuf[39] + "','"; // å��å�¡å¼ æ•°
		sqlForStatus += "0','"; // å½“å‰�ç�°é‡‘é‡‘é¢�
		sqlForStatus += mypackBuf[49] + "','"; // é’±ç®±é”™è¯¯ç �
		sqlForStatus += mypackBuf[40].split("\\$").length + "','"; // é’±ç®±ä¸ªæ•°
		// ======é’�ç®±1=======
		sqlForStatus += mypackBuf[40].split("\\$")[0] + "','"; // é’�ç®±1ç�¶æ€�
		sqlForStatus += mypackBuf[43].split("\\$")[0] + "','"; // é’�ç®±1å¸�åˆ«
		sqlForStatus += mypackBuf[42].split("\\$")[0] + "','"; // é’�ç®±1é�¢é¢�,å�¯èƒ½ ä¸ºN
		sqlForStatus += mypackBuf[46].split("\\$")[0] + "','"; // é’�ç®±1å‰©ä½™å¼ æ•°
		sqlForStatus += mypackBuf[47] + "','"; // é’�ç®±1å� é’�æ—¶é—´
		sqlForStatus += "0','0','";// é’�ç®±1åºŸé’�å›�æ”¶å¼ æ•°, é’�ç®±1å‡ºé’�å›�æ”¶å¼ æ•°
		// ======é’�ç®±2=======
		sqlForStatus += mypackBuf[40].split("\\$")[1] + "','"; // é’�ç®±2ç�¶æ€�
		sqlForStatus += mypackBuf[43].split("\\$")[1] + "','"; // é’�ç®±2å¸�åˆ«
		if ((mypackBuf[42].split("\\$")[1]).equals("N")) { // é’�ç®±2é�¢é¢�,å�¯èƒ½ ä¸ºN
			sqlForStatus += "0','";
		} else {
			sqlForStatus += mypackBuf[42].split("\\$")[1] + "','";
		}
		if ((mypackBuf[46].split("\\$")[1]).equals("N")) { // é’�ç®±2å‰©ä½™å¼ æ•°
			sqlForStatus += "0','";
		} else {
			sqlForStatus += mypackBuf[46].split("\\$")[1] + "','";
		}
		sqlForStatus += mypackBuf[47] + "','"; // é’�ç®±2å� é’�æ—¶é—´
		sqlForStatus += "0','0','"; // é’�ç®±2åºŸé’�å›�æ”¶å¼ æ•°, é’�ç®±2å‡ºé’�å›�æ”¶å¼ æ•°
		// ======é’�ç®±3=======
		sqlForStatus += mypackBuf[40].split("\\$")[2] + "','"; // é’�ç®±3ç�¶æ€�
		sqlForStatus += mypackBuf[43].split("\\$")[2] + "','"; // é’�ç®±3å¸�åˆ«
		if ((mypackBuf[42].split("\\$")[2]).equals("N")) { // é’�ç®±3é�¢é¢�,å�¯èƒ½ ä¸ºN
			sqlForStatus += "0','";
		} else {
			sqlForStatus += mypackBuf[42].split("\\$")[2] + "','";
		}
		if ((mypackBuf[46].split("\\$")[2]).equals("N")) { // é’�ç®±3å‰©ä½™å¼ æ•°
			sqlForStatus += "0','";
		} else {
			sqlForStatus += mypackBuf[46].split("\\$")[2] + "','";
		}
		sqlForStatus += mypackBuf[47] + "','"; // é’�ç®±3å� é’�æ—¶é—´
		sqlForStatus += "0','0','"; // é’�ç®±3åºŸé’�å›�æ”¶å¼ æ•°, é’�ç®±3å‡ºé’�å›�æ”¶å¼ æ•°
		// ======é’�ç®±4=======
		sqlForStatus += mypackBuf[40].split("\\$")[3] + "','"; // é’�ç®±4ç�¶æ€�
		sqlForStatus += mypackBuf[43].split("\\$")[3] + "','"; // é’�ç®±4å¸�åˆ«
		if ((mypackBuf[42].split("\\$")[3]).equals("N")) { // é’�ç®±4é�¢é¢�,å�¯èƒ½ ä¸ºN
			sqlForStatus += "0','";
		} else {
			sqlForStatus += mypackBuf[42].split("\\$")[3] + "','";
		}
		if ((mypackBuf[46].split("\\$")[3]).equals("N")) { // é’�ç®±4å‰©ä½™å¼ æ•°
			sqlForStatus += "0','";
		} else {
			sqlForStatus += mypackBuf[46].split("\\$")[3] + "','";
		}
		sqlForStatus += mypackBuf[47] + "','"; // é’�ç®±4å� é’�æ—¶é—´
		sqlForStatus += "0','0','"; // é’�ç®±4åºŸé’�å›�æ”¶å¼ æ•°, é’�ç®±4å‡ºé’�å›�æ”¶å¼ æ•°

		if (mypackBuf.length > 53) // æ‘„åƒ�æœºç�¶æ€�(5.0æ�¥æ–‡æ‰�æœ‰è¯¥ç�¶æ€�)
		{
			sqlForStatus += mypackBuf[52]+"')";
		} else {
			sqlForStatus += "3')";  
		}

		return sqlForStatus;
	}

}

/**********************************************************
 * @class ViewUnpack
 * @author Administrator
 *
 *********************************************************/
class ViewUnpack extends Thread {
	private static String xmlfile = "cmsbeans.xml";
	public static XmlBeanFactory factory = null;
	static private DataSource dataSource = null;
	static private Context ctx = null;
	private String mypackage = null;
	public static final String US = Character.toString((char) 0x1F);
	static private Mysql mysql = null;

	static {
		if (factory == null)
			factory = new XmlBeanFactory(new ClassPathResource(xmlfile));

		if (dataSource == null)
		{
			dataSource = (DataSource) factory.getBean("dataSource");
		}
	}

	public ViewUnpack() {
	}

	public ViewUnpack(String mydatetime, int myflag) throws Exception {
		update(mydatetime);
	}

	public ViewUnpack(int myflag) throws Exception {
		event(myflag);
	}

	public boolean update(String mydatetime) 
	{
		if (mydatetime == null || mydatetime.length() < 10)
			return false;

		long stime = System.currentTimeMillis();
		ObInterface obInterface = new ObInterface();

		try 
		{

			//Try to get Conn-----------------------------------------------------
			try 
			{
				if (dataSource == null) {
					dataSource = (DataSource) factory.getBean("dataSource");
				}
				if (mysql == null) {
					mysql = new Mysql(dataSource);
				}
				obInterface.setDataSource(dataSource);
			} 
			catch (Exception e) 
			{
				System.out.println("Cannot get conn at update");
				CLog.writeLog("Cannot get conn at update");

				e.printStackTrace();

				//				if (dataSource == null) {
				//					dataSource = (DataSource) factory.getBean("dataSource");
				//				}				
				//				if (mysql == null) {
				//					mysql = new Mysql(dataSource);
				//				}
				//				obInterface.setDataSource(dataSource);
			}
			mysql.setObInterface(obInterface);
			//End try to get Conn-----------------------------------------------------


			//Try to update 3 5 -----------------------------------------------------			
			try 
			{
				System.out.println(App.getCurDateStr(19) + " Auto Scan...");


				//-------case 3
				Iterator it = mysql
						.selectSql(
								"select c_termid as tmid,C_STATUS as cdst,C_REFRESH as cldt from T_TML_STATUS where C_STATUS<>'3' and C_REFRESH <='"
										+ mydatetime + "' order by 1,2")
										.iterator();
				ObInterface temp1;
				for (int i = 1; it.hasNext(); i++) {
					temp1 = (ObInterface) it.next();
					// CLog.writeLog("Auto Scan:before:"+i+"/"+obInterface.getRowNum()+",termid="+temp1.getTmid()+",status="+temp1.getCdst()+",time="+temp1.getCldt());
				}
				// if (obInterface.getRowNum()>0)
				// mysql.executeUpdate("update T_TML_STATUS set C_STATUS='3',C_REFRESH='"+App.getCurTime14Str()+"' where C_STATUS<>'3' and C_REFRESH <='"+mydatetime+"'");
				if (obInterface.getRowNum() > 0)
					mysql.executeUpdate("update T_TML_STATUS set C_STATUS='3' where C_STATUS<>'3' and C_REFRESH <='"
							+ mydatetime + "'");
				//--------case 3 End

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
			} 
			catch (Exception e) {
				e.printStackTrace();
				// CLog.writeLog(e.toString());
			}
			//End try to update 3 5 -----------------------------------------------------			

		} 
		catch (Exception e) {
			e.printStackTrace();
			CLog.writeLogVS(e.toString());
			// CLog.writeLog(new
			// Exception().getStackTrace()[0].getClassName()+":"+new
			// Exception().getStackTrace()[0].getMethodName()+":"+new
			// Exception().getStackTrace()[0].getLineNumber());
			return false;
		} 
		catch (Throwable e) {
			return false;
		} 
		finally {
			obInterface = null;
			mysql.setAutoCommit(true);
			mysql.close();
			mysql.setDataSource(null);
			mysql = null;
		}
		// Logger.log (evt);
		// CLog.writeLog("===Total Processing time:"+(System.currentTimeMillis()
		// - stime)+" ms===");
		return true;

	}

	public boolean event(int mynum) {
		// CLog.writeLog(new
		// Exception().getStackTrace()[0].getClassName()+":"+new
		// Exception().getStackTrace()[0].getMethodName()+":"+new
		// Exception().getStackTrace()[0].getLineNumber());

		long stime = System.currentTimeMillis();
		ObInterface obInterface = new ObInterface();

		try {

			try 
			{
				if (dataSource == null)
				{
					dataSource = (DataSource) factory.getBean("dataSource");					
				}

				if (mysql == null) {
					System.out.println("Event Connect");
					CLog.writeLog("Event Connect");

					mysql = new Mysql(dataSource);
				}
				obInterface.setDataSource(dataSource);
			}
			catch (Exception e) 
			{
				System.out.println("Cannot get conn at event");
				CLog.writeLog("Cannot get conn at event");				
				e.printStackTrace();


				//				if (dataSource == null) {
				//					dataSource = (DataSource) factory.getBean("dataSource");
				//				}
				//				if (mysql == null) {
				//					mysql = new Mysql(dataSource);
				//				}
				//				obInterface.setDataSource(dataSource);
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

				//·¢ËÍ¼ä¸ô
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

		} catch (Exception e) {
			e.printStackTrace();
			CLog.writeLogVS(e.toString());
			// CLog.writeLog(new
			// Exception().getStackTrace()[0].getClassName()+":"+new
			// Exception().getStackTrace()[0].getMethodName()+":"+new
			// Exception().getStackTrace()[0].getLineNumber());
			return false;
		} catch (Throwable e) {
			return false;
		} finally {
			obInterface = null;
			mysql.close();
			mysql.setDataSource(null);
			mysql = null;
		}
		// Logger.log (evt);
		// CLog.writeLog("===Total Processing time:"+(System.currentTimeMillis()
		// - stime)+" ms===");
		return true;
	}

}

class DaemonThread extends Thread {
	private static int mytimeout = 25;

	public DaemonThread(int mytimeout) {
		this.mytimeout = mytimeout;
	}

	/* çº¿ç¨‹ç�„runæ–¹æ³•ï¼Œå®ƒå°†å’Œå…¶ä»–çº¿ç¨‹å�Œæ—¶è¿�è¡Œ */
	public void run() {
		// new ViewUnpack();
		while (true) {
			String lastdatetime = App.getCurTime14Str();
			// CLog.writeLog("------datetime:"+lastdatetime+",start sleep------");
			this.setSleep(1000 * 60 * mytimeout);
			try {
				// CLog.writeLog("------datetime:"+lastdatetime+",start update------");
				new ViewUnpack(lastdatetime, 0);
			} catch (Exception e) {
			}
		}
	}

	public void setSleep(long sleeptime) {
		try {
			// CLog.writeLog("------start sleep:"+sleeptime/1000.0+" Seconds----------");
			Thread.sleep(sleeptime);
			// CLog.writeLog("------end   sleep:"+sleeptime/1000.0+" Seconds----------");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}

class EventThread extends Thread {
	private static int mytimeout = 25;

	public EventThread() {
		System.out.println("Polocy Server: " + mytimeout + " Seconds ...\n");
	}

	public EventThread(int mytimeout) {
		this.mytimeout = mytimeout;
		System.out.println("Polocy Server: " + mytimeout + " Seconds ...\n");
	}

	/* çº¿ç¨‹ç�„runæ–¹æ³•ï¼Œå®ƒå°†å’Œå…¶ä»–çº¿ç¨‹å�Œæ—¶è¿�è¡Œ */
	public void run() {
		// new ViewUnpack();
		while (true) {
			this.setSleep(1000 * 60 * mytimeout);
			try {
				
				//--------
				new	ViewUnpack(0);
				//new	ViewUnpack(App.getCurTime14Str(),0);
				
				//SMS
				String[] args = {};
				ccas.SMSServer.main(args);
				//--------
				
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}

	public void setSleep(long sleeptime) {
		try {
			Thread.sleep(sleeptime);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}
