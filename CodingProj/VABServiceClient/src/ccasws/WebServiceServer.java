
package ccasws;

import java.io.*;
import java.sql.SQLException;
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

import com.vab.iflex.gateway.casaservice.CasaTransfer;
import com.vab.iflex.gateway.paybillservice.DemoApp;

public class WebServiceServer extends Thread {
	private static int mytimeout = 20;

	public WebServiceServer() throws IOException {
	}

	public static void main(String args[]) throws Exception {
		DataStorage storage = new DataStorage();

		// å¼€å�¯TCPæœ�å�¡
		TCPServer tcpServer = new TCPServer(9997);
		tcpServer.startListen(storage);

		// å¼€å�¯UDPæœ�å�¡
		UDPServer udpServer = new UDPServer(9998);
		udpServer.startListen(storage);

		// å¼€å�¯ä¸�å�¡å¤�?ç�†æœ�å�¡
		BusinessHandler business = new BusinessHandler(storage);
		new Thread(business).start();

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
		// æ‰“å¼€é€‰æ‹©å™¨å¤�?ç�†
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

		System.out.println("The Web Service TCP 9997 is started");
		CLog.writeLog("The Web Service TCP 9997 is started");
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
		// æ‰“å¼€é€‰æ‹©å™¨å¤�?ç�†
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

		System.out.println("The Web Service UDP 9998 is started");
		CLog.writeLog("The Web Service UDP 9998 is started");

	}
}

/*******************************
 * @class: TCPTcpSelectorLoop
 * @brief: TCPé€‰æ‹©å™¨å¤�?ç�†
 * @details:
 *******************************/
class TcpSelectorLoop implements Runnable {
	private Selector selector;
	private ByteBuffer recvBuf;
	private DataStorage dataStorage;

	public static final String US = Character.toString((char) 0x1F);

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
					// å¤�?ç�†äº‹ä»¶. å�¯ä»¥ç�?¨å¤�çº¿ç¨‹æ�¥å¤�?ç�†.
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
			// è¿™æ˜¯ä¸€ä¸ªconnection acceptäº‹ä»¶, å¹¶ä¸�?è¿™ä¸ªäº‹ä»¶æ˜¯æ³¨å†Œåœ¨serversocketchannelä¸�ç��?.
			ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
			// æ�¥å�—ä¸€ä¸ªè¿�æ�¥.
			SocketChannel sc = ssc.accept();
			// å¯¹æ–°ç��?è¿�æ�¥ç��?channelæ³¨å†Œreadäº‹ä»¶
			sc.configureBlocking(false);
			sc.register(this.getSelector(), SelectionKey.OP_READ);
		} 
		else if (key.isReadable()) {
			// è¿™æ˜¯ä¸€ä¸ªreadäº‹ä»¶,å¹¶ä¸�?è¿™ä¸ªäº‹ä»¶æ˜¯æ³¨å†Œåœ¨socketchannelä¸�ç��?.
			SocketChannel socketChannel = (SocketChannel) key.channel();
			// æ¸…ç©ºç¼“å†²åŒº
			this.recvBuf.clear();
			// å†™æ•°æ�®åˆ°buffer

			socketChannel.configureBlocking(false);		
			int count = socketChannel.read(this.recvBuf);			
			System.out.println("\n\n------------------");
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
			CLog.writeLog("The String is [" + msg + "]");			
			dataStorage.push(msg);

			//--------Response
			System.out.println("\nStart to get the Response");
			Boolean needRes = true;
			int ResultCount = 0;
			String ResKey = msg.trim().split(US,-1)[1].trim();
			System.out.println("\nRes Check Key [" + ResKey + "]");
			while (needRes)
			{
				if (ViewTransParser.VABRes != null && ViewTransParser.VABRes.size() > 0)
				{
					if(ViewTransParser.VABRes.containsKey(ResKey))
					{
						String ResValue = ViewTransParser.VABRes.get(ResKey).trim();
						System.out.println("MSG ID " + ResKey + " will be response as " + ResValue);
						socketChannel.write(ByteBuffer.wrap(ResValue.getBytes(Charset.forName("UTF-8"))));

						needRes = false;
					}
				}
				else
				{
					ResultCount++;
					System.out.println("Cannot find the Respone for MSG ID " + ResKey);
					System.out.println("Waiting for checking the Response ........ time " + ResultCount);
										
					Thread.sleep(5*1000);
					
					if (ResultCount >= 10)
					{
						System.out.println("MSG ID " + ResKey + " will be response as [68]");
						socketChannel.write(ByteBuffer.wrap("68".getBytes(Charset.forName("UTF-8"))));
						needRes = false;
					}					
				}
			}
			ViewTransParser.VABRes.remove(ResKey);
			//--------End Response

			//			ByteBuffer bb1 = ByteBuffer.allocate(10000);
			//            String s = "AAAAAAAAAAAAAAAAA";
			//            byte[] array1 = new byte[bb1.limit()];
			//            array1 = s.getBytes();
			//            bb1.put(array1);
			//            bb1.flip();
			//            socketChannel.write(bb1);

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
				// å�‘é€�ä¸�å�¡å¤�?ç�†çº¿ç¨‹
				dataStorage.push(msgBody);
				// è®¾ç½®æ�¥æ�?¶æˆ�å�Ÿ
				msg = "00";
			} 
			else 
			{
				// è®¾ç½®CRC32æ ¡éªŒå¤±è´¥
				msg = "01";
			}
			 *////////-------------------------------------------------------------------------------

			//Response ve nguyen mau
			//socketChannel.write(ByteBuffer.wrap(msg.getBytes(Charset.forName("UTF-8"))));
		}
	}
}

/*******************************
 * @class: UDPSelectorLoop
 * @brief: UDPé€‰æ‹©å™¨å¤�?ç�†
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
			// é˜»å¡�,å�ªæœ‰å½“è‡³å°‘ä¸€ä¸ªæ³¨å†Œç��?äº‹ä»¶å�‘ç�?Ÿç��?æ—¶å€™æ‰�ä¼�ç»§ç»­.
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
				// å¤�?ç�†äº‹ä»¶. å�¯ä»¥ç�?¨å¤�çº¿ç¨‹æ�¥å¤�?ç�†.
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
			// è¿™æ˜¯ä¸€ä¸ªconnection acceptäº‹ä»¶, å¹¶ä¸�?è¿™ä¸ªäº‹ä»¶æ˜¯æ³¨å†Œåœ¨serversocketchannelä¸�ç��?.
			ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
			// æ�¥å�—ä¸€ä¸ªè¿�æ�¥.
			SocketChannel sc = ssc.accept();
			// å¯¹æ–°ç��?è¿�æ�¥ç��?channelæ³¨å†Œreadäº‹ä»¶
			sc.configureBlocking(false);
			sc.register(this.getSelector(), SelectionKey.OP_READ);
		} else if (key.isReadable()) {
			// è¿™æ˜¯ä¸€ä¸ªreadäº‹ä»¶,å¹¶ä¸�?è¿™ä¸ªäº‹ä»¶æ˜¯æ³¨å†Œåœ¨socketchannelä¸�ç��?.
			DatagramChannel datagramChannel = (DatagramChannel) key.channel();
			// æ¸…ç©ºç¼“å†²åŒº
			recvBuf.clear();
			// å†™æ•°æ�®åˆ°buffer
			SocketAddress sa = datagramChannel.receive(recvBuf);
			// æ��æ•°æ�®å†™å…¥é˜Ÿåˆ—ä¸­
			recvBuf.flip();
			String msg = Charset.forName("UTF-8").decode(recvBuf).toString();
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
 * @brief: ä¸�å�¡å¤�?ç�†ç±»
 * @details: è¿›è¡Œæ�¥æ–‡è§£æ��å’Œæ•°æ�®å¤�?ç�†
 *******************************/
class BusinessHandler implements Runnable {
	private DataStorage dataStorage;
	// çº¿ç¨‹æ± 
	private ExecutorService executorService;
	private int count = 1;

	BusinessHandler(DataStorage storage) {
		this.dataStorage = storage;
		// Runtimeç��?availableProcessor()æ–¹æ³•è¿�?å›�å½“å‰�ç³»ç»Ÿç��?CPUæ•°ç›®.
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

				System.out.println("The Origin WS Data from Switch is [" + data + "]");
				CLog.writeLog("The Origin WS Data from Switch is [" + data + "]");
				executorService.execute(new ViewTransParser(data.trim()));
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

	public static HashMap<String,String> VABRes = new HashMap<String,String>();	

	ViewTransParser(String mypackage) {
		this.mypackage = mypackage;
	}

	public void run() {
		try {
			unpackTrans();
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
		}
	}

	public boolean unpackTrans() {

		if (mypackage != null && mypackage.length() > 0)
		{
			System.out.println("The WS Data Transfer to Unpack SPLITTED into  [" + mypackage.split(US, -1).length + "] parts");
			CLog.writeLog("The Data WS Transfer to Unpack SPLITTED into  [" + mypackage.split(US, -1).length + "] parts");
		}

		System.out.println("The WS Data Transfer to Unpack is [" + mypackage + "]");
		CLog.writeLog("The Data WS Transfer to Unpack is [" + mypackage + "]");

		System.out.println("The Message Type is [" + mypackage.split(US, -1)[0] + "] Boolean is " + mypackage.split(US, -1)[0].trim().equals("103"));
		CLog.writeLog("The Message Type is [" + mypackage.split(US, -1)[0] + "] Boolean is " + mypackage.split(US, -1)[0].trim().equals("103"));

		if (mypackage.split(US, -1)[0].trim().equals("103") || mypackage.split(US, -1)[0].trim().equals("103")) {
			// 10002è®¾å¤‡ç�¶æ€�æ•°æ�®
			System.out.println("Can go to 103");
			CLog.writeLog("Can go to 103");

			parserTermStatus();
		}
		else if (mypackage.split(US, -1)[0].trim().equals("139") || mypackage.split(US, -1)[0].trim().equals("139")) {
			System.out.println("Can go to 139");
			CLog.writeLog("Can go to 139");

			VABGetList();
		}
		else if (mypackage.split(US, -1)[0].trim().equals("140") || mypackage.split(US, -1)[0].trim().equals("140")) {
			System.out.println("Can go to 140");
			CLog.writeLog("Can go to 140");

			VABQueryBill();
		}
		else if (mypackage.split(US, -1)[0].trim().equals("141") || mypackage.split(US, -1)[0].trim().equals("141")) {
			System.out.println("Can go to 141");
			CLog.writeLog("Can go to 141");

			VABPayBill();
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

		try 
		{
			String msgid = mypackBuf[0].trim();
			String traceno = mypackBuf[1].trim();
			String revbranch = mypackBuf[2].trim();
			String revacc = mypackBuf[3].trim();
			Double depositamt = Double.parseDouble(mypackBuf[4].trim().substring(0, mypackBuf[4].trim().length()-2));
			System.out.println("\n Deposit Amt: " + depositamt);
			String currency = mypackBuf[5].trim();

			String atmGLAccBr = mypackBuf[6].trim();			
			String atmGLAccNo = mypackBuf[7].trim();

			//Do the Deposit Txn
			CasaTransfer instance = new CasaTransfer("EBANKING", "http://192.168.31.200:7001/FCCGateway/CASAGatewayService", "600000");
			String[] out = instance.casaToCasaTransferGRG("IN07", atmGLAccBr, atmGLAccNo, 
					revbranch, revacc, depositamt, currency, "Deposit to Acc " + revacc, traceno);

			System.out.println("\n Status: " + out[0]);
			System.out.println("\n Trace No " + out[1]);
			System.out.println("\n" + out[2]);
			System.out.println("\n" + out[3]);
			System.out.println("\n" + out[4]);

			//Response
			//String depositresp = "<depositresponse><msgid>"+out[1]+"</msgid><status>"+out[0]+"</status></depositresponse>";
			String depositresp = "01";
			if (!out[0].trim().equals("FAILURE")) depositresp = "00";

			System.out.println("\nResponse to Switch " + out[0].trim() + " at " + depositresp);

			//String cmdRep =  ccasws.App.sendByTCP("127.0.0.1", 9997, 60*1000, depositresp);			
			//String	myresponse = app.getTcpRepResponse(cmdRep);

			if (ViewTransParser.VABRes != null)
			{
				ViewTransParser.VABRes.clear();
				ViewTransParser.VABRes.put(out[1].trim(), depositresp);
			}
		} 
		catch (Exception e) 
		{
			System.out.println("Has Error at 103 Exception");
			System.out.println(e.toString());

			CLog.writeLog("Has Error at 103 Exception");
			CLog.writeLog(e.toString());

			e.printStackTrace();
		}
	}



	private void VABQueryBill() {
		String mypackBuf[] = mypackage.split(US,-1);//split(US)

		System.out.println("In the Status is [" + mypackBuf.length + "] parts");
		CLog.writeLog("In the Status is [" + mypackBuf.length + "] parts");

		for (int i=0; i<mypackBuf.length; i++)
		{
			System.out.println("Value at [" + i + "] is [" + mypackBuf[i] + "]");
			CLog.writeLog("Value at [" + i + "] is [" + mypackBuf[i] + "]");
		}

		try 
		{
			String msgid = mypackBuf[0].trim();
			String traceno = mypackBuf[1].trim();

			String TenDichVu = mypackBuf[2].trim();
			String MaDichVu = mypackBuf[3].trim();

			String TenNhaCC = mypackBuf[4].trim();
			String MaNhaCC = mypackBuf[5].trim();

			String CustomerID = mypackBuf[6].trim();
			String CustomerTK = mypackBuf[7].trim();


			//Do the Query Bill Txn
			DemoApp myBill = new DemoApp();
			String myQueryBillResult = myBill.QueryBillCcas(TenDichVu, MaDichVu, TenNhaCC, MaNhaCC, CustomerID, CustomerTK).trim();			

			System.out.println("\n>>>>>>>Status Query Bill at Ccas: " + myQueryBillResult);

			//Response
			if (ViewTransParser.VABRes != null)
			{
				//ViewTransParser.VABRes.clear();
				ViewTransParser.VABRes.put(traceno, myQueryBillResult);
			}
		} 
		catch (Exception e) 
		{
			System.out.println("Has Error at 140 Exception");
			System.out.println(e.toString());

			CLog.writeLog("Has Error at 140 Exception");
			CLog.writeLog(e.toString());

			e.printStackTrace();
		}
	}



	private void VABPayBill() {
		String mypackBuf[] = mypackage.split(US,-1);//split(US)

		System.out.println("In the Status is [" + mypackBuf.length + "] parts");
		CLog.writeLog("In the Status is [" + mypackBuf.length + "] parts");

		for (int i=0; i<mypackBuf.length; i++)
		{
			System.out.println("Value at [" + i + "] is [" + mypackBuf[i] + "]");
			CLog.writeLog("Value at [" + i + "] is [" + mypackBuf[i] + "]");
		}

		try 
		{
			String msgid = mypackBuf[0].trim();
			String traceno = mypackBuf[1].trim();

			String TenDichVu = mypackBuf[2].trim();
			String MaDichVu = mypackBuf[3].trim();

			String TenNhaCC = mypackBuf[4].trim();
			String MaNhaCC = mypackBuf[5].trim();

			String CustomerID = mypackBuf[6].trim();
			String CustomerTK = mypackBuf[7].trim();


			//Do the Query Bill Txn
			DemoApp myBill = new DemoApp();
			String myQueryBillResult = myBill.QueryBillCcas(TenDichVu, MaDichVu, TenNhaCC, MaNhaCC, CustomerID, CustomerTK).trim();
			if(!myQueryBillResult.split(US,-1)[0].trim().equals("01")) myQueryBillResult = myBill.PayBillCcas().trim();
			else
			{
				System.out.println("Co loi Query Bill o Pay Bill");
				myQueryBillResult = "01" + US;
			}            
			System.out.println("\n>>>>>>>>Status Pay Bill at Ccas: " + myQueryBillResult);

			//Response
			try
			{
				if (ViewTransParser.VABRes != null)
				{
					//ViewTransParser.VABRes.clear();
					ViewTransParser.VABRes.put(traceno, myQueryBillResult);
				}
				else
				{
					System.out.println("\nResponse HS Co Loi");
				}
			}
			catch (Exception ex)
			{
				System.out.println("\nResponse HS Exception");
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
		} 
		catch (Exception e) 
		{
			System.out.println("Has Error at 141 Exception");
			System.out.println(e.toString());

			CLog.writeLog("Has Error at 141 Exception");
			CLog.writeLog(e.toString());

			e.printStackTrace();
		}
	}



	private void VABGetList() {
		String mypackBuf[] = mypackage.split(US,-1);//split(US)

		System.out.println("In the Status is [" + mypackBuf.length + "] parts");
		CLog.writeLog("In the Status is [" + mypackBuf.length + "] parts");

		for (int i=0; i<mypackBuf.length; i++)
		{
			System.out.println("Value at [" + i + "] is [" + mypackBuf[i] + "]");
			CLog.writeLog("Value at [" + i + "] is [" + mypackBuf[i] + "]");
		}

		try 
		{
			String msgid = mypackBuf[0].trim();
			String traceno = mypackBuf[1].trim();

			//Do the GetList Txn
			DemoApp myBill = new DemoApp();
			String myGetListResult = myBill.GetListCcas().trim();
			System.out.println("\n Status Pay Bill at Ccas: " + myGetListResult);

			//Response
			if (ViewTransParser.VABRes != null)
			{
				//ViewTransParser.VABRes.clear();
				ViewTransParser.VABRes.put(traceno, myGetListResult);
			}
		} 
		catch (Exception e) 
		{
			System.out.println("Has Error at 139 Exception");
			System.out.println(e.toString());

			CLog.writeLog("Has Error at 139 Exception");
			CLog.writeLog(e.toString());

			e.printStackTrace();
		}
	}



	private void parserAddCash() {
		String mypackBuf[] = mypackage.split(US);
		if (mypackBuf.length < 9)
			return ;

		try {
			String TermId = mypackBuf[1];
			String Mudt = App.transDateFormat(mypackBuf[8], "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss");

			int num = 1;
			if (mypackBuf[0].trim().indexOf("0008") >= 0)
				num = -1;
			boolean flag = false;
			int a = 0;
			for (int j = 0; j < mypackBuf[2].split("\\$").length; j++) {
				String loadcash = mypackBuf[2].split("\\$")[j];
				if (!loadcash.equals("3")) {
					a = a + 1;
				}
				if (!loadcash.equals("3")) {
					int Anum = num * Integer.parseInt("0" + mypackBuf[4].split("\\$")[j])
							* Integer.parseInt("0" + mypackBuf[6].split("\\$")[j]);
					String sqlForLoadCash = "insert into T_TML_LOADCASH values('"
							+ TermId + "','" + a + "','"
							+ mypackBuf[4].split("\\$")[j]
									+ "','" + "" //obInterface.getIdtp()
									+ mypackBuf[6].split("\\$")[j]
											+ "','" + Mudt + "','" + Anum + "','"
											+ mypackBuf[5].split("\\$")[j] + "')";
					mysql.executeUpdate(sqlForLoadCash);
					String sqlForCASHBOX = "update T_TML_CASHBOX set C_LOADTIME='" + Mudt
							+ "',I_DENO='"
							+ mypackBuf[4].split("\\$")[j]
									+ "',C_NOTETYPE='"
									+ mypackBuf[5].split("\\$")[j]
											+ "',I_remain='"
											+ mypackBuf[6].split("\\$")[j]
													+ "',I_loading='"
													+ mypackBuf[6].split("\\$")[j]
															+ "',I_REJECT='0',I_RETRACT='0' where C_TERMID ='"
															+ TermId + "' and C_TERMCASTTEEID='" + a + "'";
					flag = mysql.executeUpdate(sqlForCASHBOX);
				}
			}
			if (flag) {
				System.out.println("------Load cash is success!------");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// CLog.writeLog(e.toString());
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

		String TermId = mypackBuf[1];
		String CardNo = mypackBuf[2];
		String ErrCode = mypackBuf[3] + mypackBuf[4];
		String CTime = mypackBuf[5];
		try {
			String sqlForCardmerge = "insert into T_CARDMERGE values('" + TermId
					+ "','" + CTime + "','" + CardNo + "','" + ErrCode + "')";
			mysql.executeUpdate(sqlForCardmerge);
			// äº§ç�?Ÿè­¦å‘�äº‹ä»¶
			String sqlForEvent = "insert into T_TML_EVENT values('"
					+ TermId
					+ "','ATMC','0','','1','"
					+ App.transDateFormat(CTime,
							"yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss")
							+ "','Capture Card: " + CardNo + "','','','')";
			mysql.executeUpdate(sqlForEvent);

		} catch (Exception e) {
			e.printStackTrace();
			// CLog.writeLog(e.toString());
		}
	}

	private String spliteTermStatusSQL(String[] mypackBuf) { 
		String sqlForStatus = "insert into T_TML_STATUS values('";
		sqlForStatus += mypackBuf[3] + "','"; // ç»ˆç«¯å�·
		sqlForStatus += mypackBuf[4] + "','"; // ç»ˆç«¯ç�¶æ€�
		sqlForStatus += mypackBuf[48] + "','"; // æ•°æ�®é‡‡é›†æ—¶é—´
		sqlForStatus += mypackBuf[6] + "','"; // ä¸»æ�§è®¾å¤‡ç�¶æ€�
		sqlForStatus += mypackBuf[7] + "','"; // ä¸»æ�§è®¾å¤‡é�?™è¯¯ç �
		sqlForStatus += mypackBuf[8] + "','"; // å‡ºé’�æœºè�¯
		sqlForStatus += mypackBuf[9] + "','"; // å‡ºé’�æœºè�¯é�?™è¯¯ç �
		sqlForStatus += mypackBuf[10] + "','"; // å­˜æ¬¾æœºè�¯
		sqlForStatus += mypackBuf[11] + "','"; // å­˜æ¬¾æœºè�¯é�?™è¯¯ç �
		sqlForStatus += mypackBuf[12] + "','"; // å‡ºé’�é—¨
		sqlForStatus += mypackBuf[13] + "','"; // å‡ºé’�é—¨é�?™è¯¯ç �
		sqlForStatus += mypackBuf[14] + "','"; // å­˜æ¬¾é—¨
		sqlForStatus += mypackBuf[15] + "','"; // å­˜æ¬¾é—¨é�?™è¯¯ç �
		sqlForStatus += mypackBuf[16] + "','"; // å‡­æ�¡æ‰“å�°æœº
		sqlForStatus += mypackBuf[17] + "','"; // å‡­æ�¡æ‰“å�°æœºé�?™è¯¯ç �
		sqlForStatus += mypackBuf[18] + "','"; // æµ�æ°´æ‰“å�°æœº
		sqlForStatus += mypackBuf[19] + "','"; // æµ�æ°´æ‰“å�°æœºé�?™è¯¯ç �
		sqlForStatus += mypackBuf[20] + "','"; // å�‘ç¥¨æ‰“å�°æœº
		sqlForStatus += mypackBuf[21] + "','"; // å�‘ç¥¨æ‰“å�°æœºé�?™è¯¯ç �
		sqlForStatus += mypackBuf[22] + "','"; // UPS
		sqlForStatus += mypackBuf[23] + "','"; // UPSé�?™è¯¯ç �
		sqlForStatus += mypackBuf[24] + "','"; // ç£�å�¡è¯»å†™å™¨
		sqlForStatus += mypackBuf[25] + "','"; // ç£�å�¡è¯»å†™å™¨é�?™è¯¯ç �
		sqlForStatus += mypackBuf[26] + "','"; // ICå�¡è¯»å†™å™¨
		sqlForStatus += mypackBuf[27] + "','"; // ICå�¡è¯»å†™å™¨é�?™è¯¯ç �
		sqlForStatus += mypackBuf[28] + "','"; // ä¿�é™©é—¨
		sqlForStatus += mypackBuf[29] + "','"; // ä¿�é™©é—¨é�?™è¯¯ç �
		sqlForStatus += mypackBuf[30] + "','"; // æœºç®±é—¨
		sqlForStatus += mypackBuf[31] + "','"; // æœºç®±é—¨é�?™è¯¯ç �
		sqlForStatus += mypackBuf[32] + "','"; // ç¡¬ä»¶å� å¯†æœº
		sqlForStatus += mypackBuf[33] + "','"; // ç¡¬ä»¶å� å¯†æœºé�?™è¯¯ç �
		sqlForStatus += mypackBuf[34] + "','"; // ä¿¡å°�å­˜æ¬¾ç®±
		sqlForStatus += mypackBuf[35] + "','"; // ä¿¡å°�å­˜æ¬¾ç®±é�?™è¯¯ç �
		sqlForStatus += mypackBuf[36] + "','"; // ä¿¡å°�å­˜æ¬¾ä¿¡å°�æ•°
		sqlForStatus += mypackBuf[37] + "','"; // å­˜æ�˜æ‰“å�°æœº
		sqlForStatus += mypackBuf[38] + "','"; // å­˜æ�˜æ‰“å�°æœºé�?™è¯¯ç �
		sqlForStatus += "','"; // åºŸé’�ç®±
		sqlForStatus += "','"; // åºŸé’�ç®±é�?™è¯¯ç �
		sqlForStatus += mypackBuf[39] + "','"; // å��å�¡å¼ æ•°
		sqlForStatus += "0','"; // å½“å‰�ç�°é‡‘é‡‘é¢�
		sqlForStatus += mypackBuf[49] + "','"; // é’±ç®±é�?™è¯¯ç �
		sqlForStatus += mypackBuf[40].split("\\$").length + "','"; // é’±ç®±ä¸ªæ•°
		// ======é’�ç®±1=======
		sqlForStatus += mypackBuf[40].split("\\$")[0] + "','"; // é’�ç®±1ç�¶æ€�
		sqlForStatus += mypackBuf[43].split("\\$")[0] + "','"; // é’�ç®±1å¸�åˆ«
		sqlForStatus += mypackBuf[42].split("\\$")[0] + "','"; // é’�ç®±1é�¢é¢�,å�¯èƒ½ ä¸ºN
		sqlForStatus += mypackBuf[46].split("\\$")[0] + "','"; // é’�ç®±1å‰©ä½™å¼ æ•°
		sqlForStatus += mypackBuf[47] + "','"; // é’�ç®±1å� é’�æ—¶é—´
		sqlForStatus += "0','0','";// é’�ç®±1åºŸé’�å›�æ�?¶å¼ æ•°, é’�ç®±1å‡ºé’�å›�æ�?¶å¼ æ•°
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
		sqlForStatus += "0','0','"; // é’�ç®±2åºŸé’�å›�æ�?¶å¼ æ•°, é’�ç®±2å‡ºé’�å›�æ�?¶å¼ æ•°
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
		sqlForStatus += "0','0','"; // é’�ç®±3åºŸé’�å›�æ�?¶å¼ æ•°, é’�ç®±3å‡ºé’�å›�æ�?¶å¼ æ•°
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
		sqlForStatus += "0','0','"; // é’�ç®±4åºŸé’�å›�æ�?¶å¼ æ•°, é’�ç®±4å‡ºé’�å›�æ�?¶å¼ æ•°

		if (mypackBuf.length > 53) // æ‘�?åƒ�æœºç�¶æ€�(5.0æ�¥æ–‡æ‰�æœ‰è¯¥ç�¶æ€�)
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

	public boolean update(String mydatetime) {
		if (mydatetime == null || mydatetime.length() < 10)
			return false;

		long stime = System.currentTimeMillis();
		ObInterface obInterface = new ObInterface();

		try {

			try {
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

			try {
				System.out.println(App.getCurDateStr(19) + " Auto Scan...");
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
			} catch (Exception e) {
				e.printStackTrace();
				// CLog.writeLog(e.toString());
			}


		} catch (Exception e) {
			e.printStackTrace();
			// CLog.writeLog(e.toString());
			// CLog.writeLog(new
			// Exception().getStackTrace()[0].getClassName()+":"+new
			// Exception().getStackTrace()[0].getMethodName()+":"+new
			// Exception().getStackTrace()[0].getLineNumber());
			return false;
		} catch (Throwable e) {
			return false;
		} finally {
			//mysql.setAutoCommit(true);
			mysql.close();
			mysql.setDataSource(null);

			obInterface = null;			
			dataSource = null;
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


			Collection interfaceInfos = mysql
					.selectSql("select C_TERMID as tmid,c_time as opdt,c_code as uscd,c_msg as clnm,C_SOLVE_TIME as cuno,C_USERID as cdtp from T_TML_EVENT where (C_SOLVE_MSG <=' ' or C_SOLVE_MSG is null) and (C_USERID <'A' or C_USERID is null)");
			Iterator it = interfaceInfos.iterator();
			if (obInterface.getRowNum() > 0)
				System.out.println(App.getCurDateStr(19) + " EVENT "
						+ obInterface.getRowNum());
			// if (obInterface.getRetNum()>0)
			// System.out.println(App.getCurDateStr(19)+" EVENT:"+obInterface.getRetNum());
			ObInterface temp1;
			for (int i = 0; it.hasNext(); i++) {
				temp1 = (ObInterface) it.next();
				obInterface.setCunm("");
				obInterface.setIdtp("");
				obInterface.setCldt("");
				try {
					mysql.getRowSql("select C_ROLE as cunm,c_time as cldt,C_ADVICETYPE as idtp,I_SENDCOUNT as num,I_SENDINTERVAL as anum from T_ADVICEPOLICY where C_TERMID='"
							+ temp1.getTmid()
							+ "' and C_CODE='"
							+ temp1.getUscd() + "'");
				} catch (Exception e) {
					try {
						mysql.getRowSql("select C_ROLE as cunm,c_time as cldt,C_ADVICETYPE as idtp,I_SENDCOUNT as num,I_SENDINTERVAL as anum from T_ADVICEPOLICY where C_TERMID<=' ' and C_CODE='"
								+ temp1.getUscd() + "'");
					} catch (Exception ex) {
						continue;
					}
				}

				// CLog.writeLog("---DateTime:"+obInterface.getCldt());
				if (obInterface.getCldt().length() > 0
						&& obInterface.getCldt().split("-").length > 1) {
					if (App.getCurTimeStr(6).compareTo(
							obInterface.getCldt().split("-")[0]) < 0
							|| App.getCurTimeStr(6).compareTo(
									obInterface.getCldt().split("-")[1]) > 0)
						continue;
				}

				// E:email|P:SMS
				String myflag = "";
				if (obInterface.getIdtp().equals("11"))
					myflag = "PE";
				if (obInterface.getIdtp().equals("01"))
					myflag = "E";
				if (obInterface.getIdtp().equals("10"))
					myflag = "P";
				int mycount = obInterface.getNum();
				int curcount = 1 + Integer.parseInt("0"
						+ temp1.getCdtp().replaceAll("\\D", ""));
				temp1.setCdtp("" + curcount);
				// å�‘é€�é—´é��?
				int myInterval = App.getSecondInterval(App.getCurDateStr(8),
						App.getCurTimeStr(6), temp1.getOpdt().substring(0, 10)
						.replaceAll("[:/]", "").trim(), temp1.getOpdt()
						.substring(11).replaceAll("[:/]", "").trim());
				// CLog.writeLog("---Interval:"+myInterval+" s,"+obInterface.getAnum()*60+"S,from:"+temp1.getOpdt()+",count:"+curcount+"/"+mycount);
				if (myInterval <= (curcount - 1) * obInterface.getAnum() * 60)
					continue;
				if (obInterface.getCunm().length() <= 0
						|| obInterface.getIdtp().length() <= 0
						|| myflag.length() <= 0)
					continue;
				// System.out.println(obInterface.getCunm()+",len:"+obInterface.getCunm().split(";").length);
				for (int j = 0; j < obInterface.getCunm().split(";").length; j++) {
					try {
						mysql.getRow("P007", "usid='"
								+ obInterface.getCunm().split(";")[j]
										+ "' and usst='O'");
						if (myflag.equals("E"))
							obInterface.setUscd(obInterface.getTele());
						if (obInterface.getUscd().length() > 5)
							mysql.executeUpdate("insert into D012 values('"
									+ App.getCurDateStr(8)
									+ "','"
									+ App.getCurTimeStr(6)
									+ "','ATM','06','"
									+ ccasws.RandomStringUtils
									.randomAlphabetic(10) + "','"
									+ temp1.getUscd() + "','"
									+ myflag.substring(0, 1) + "','"
									+ temp1.getTmid() + ":" + temp1.getClnm()
									+ "',' ',' ','" + obInterface.getUscd()
									+ "',' ')");
						if (myflag.equals("PE")
								&& obInterface.getTele().length() > 5)
							mysql.executeUpdate("insert into D012 values('"
									+ App.getCurDateStr(8)
									+ "','"
									+ App.getCurTimeStr(6)
									+ "','ATM','06','"
									+ ccasws.RandomStringUtils
									.randomAlphabetic(10) + "','"
									+ temp1.getUscd() + "','E','"
									+ temp1.getTmid() + ":" + temp1.getClnm()
									+ "',' ',' ','" + obInterface.getTele()
									+ "',' ')");
					} catch (Exception ex) {
					}
				}
				if (curcount >= mycount)
					temp1.setCdtp("count:" + mycount);
				// CLog.writeLog("---Interval:"+myInterval+" s,"+obInterface.getAnum()*60+"S,from:"+temp1.getOpdt()+",count:"+curcount+"/"+mycount+"---");
				String sql = "update T_TML_EVENT set C_USERID='"
						+ temp1.getCdtp() + "' where c_time='"
						+ temp1.getOpdt() + "'";
				if (temp1.getUscd().length() > 0)
					sql += " and c_code='" + temp1.getUscd() + "'";
				if (temp1.getCuno().length() > 0)
					sql += " and C_SOLVE_TIME='" + temp1.getCuno() + "'";
				if (temp1.getTmid().length() > 0)
					sql += " and C_TERMID='" + temp1.getTmid() + "'";
				mysql.executeUpdate(sql);

			}

		} catch (Exception e) {
			e.printStackTrace();
			// CLog.writeLog(e.toString());
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
