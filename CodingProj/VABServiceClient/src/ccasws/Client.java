package ccasws;

import java.io.*;
import java.net.*;
import java.util.Comparator;
import java.util.zip.*;
import sun.misc.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
//import java.util.concurrent.locks.Lock;

public class Client {
	private Socket mysocket = null;
	private DataInputStream in;
	private DataOutputStream out;
	private byte[] buf = null;
	//prior 优先级
	private int prior = 20;
	private boolean connect =false;
	private int timeout = 98000;
	private String name="client";
	/*
	private static Client instance = null;

	public static synchronized Client getInstance(String host, int port) {
		if (instance == null) {
			instance = new Client(host, port);
		}
		return instance;
	}

	public static synchronized void closeInstance() {
		if (instance != null) {
			instance.close();
		}
		instance = null;
	}
*/
	public Client() {
		//prior = 20;
		connect =false;
	}

	public Client(String host, int port) {
		//prior = 20;
		name="client";
		Connect(host,port);
	}

	public Client(DataInputStream in,DataOutputStream out) {
		//prior = 20;
		connect =true;
		this.in=in;
		this.out=out;
		name="server";
	}

	public void setTimeout(int timeout) throws Exception	{
		this.timeout=timeout;
		if	(mysocket!=null)		mysocket.setSoTimeout(timeout); //超时时间
	}

	public	boolean Connect(String host, int port) {
		if	(mysocket!=null && mysocket.isConnected()){
			connect = true;
			return connect;
		}
		try {
			//mysocket = new Socket("192.168.1.5", 52005);
			long mtime=new java.util.Date().getTime();
			//mysocket = new Socket(host, port);
			mysocket = new Socket();
			mysocket.setReuseAddress(true);
			mysocket.connect(new InetSocketAddress(host, port));
			if (mysocket.isConnected())
			{
				connect = true;
				//prior = 10;
				mtime = new java.util.Date().getTime() - mtime;
				///CLog.writeLog("socket time=[" + mtime/1000+"."+mtime % 1000 + "second" + "]");
				in = new DataInputStream(mysocket.getInputStream());
				out = new DataOutputStream(mysocket.getOutputStream());
				mysocket.setSoTimeout(timeout); //超时时间
				///CLog.writeLog("timeout:" + mysocket.getSoTimeout());
			}
		}	catch (IOException e) {
			connect=false;
			mysocket=null;
			CLog.writeLog("new client error1");
			CLog.writeLog("getMessage=" + e.getMessage());
			CLog.writeLog("Exception=" + e.toString());
			//e.printStackTrace();
		}	catch (Exception e) {
			connect=false;
			mysocket=null;
			CLog.writeLog("new client error2");
			CLog.writeLog("getMessage=" + e.getMessage());
			CLog.writeLog("Exception=" + e.toString());
			//e.printStackTrace();
		}
		return connect;
	}

	private byte[] stoLH(short n) {
		byte[] b = new byte[4];
		b[1] = (byte) (n & 0xff);
		b[0] = (byte) (n >> 8 & 0xff);
		return b;
	}

	private byte[] ltoLH(short n) {
		byte[] b = new byte[4];
		b[3] = (byte) (n & 0xff);
		b[2] = (byte) (n >> 8 & 0xff);
		b[1] = (byte) (n >> 16 & 0xff);
		b[0] = (byte) (n >> 24 & 0xff);
		return b;
	}

//将2位byte16进制数，转为short [0x01] [0x23] -- > 123
	private short toShort(byte[] bt) {
		short st = 0;
		if (bt.length < 2) {
			return st;
		}
		st = (short) (bt[0] << 8);
		st = (short) (st + (short) (bt[1]));
		return st;
	}

	private byte[] toLH(float f) {
		return toLH(Float.floatToRawIntBits(f));
	}

	public void close() {
		connect =false;
		prior = 20;
		try {
			if	(in!=null)				in.close();
			if	(out!=null)				out.close();
			if	(mysocket!=null)	mysocket.close();
		}	catch (IOException e) {}
		finally{
			in=null;
			out=null;
			mysocket=null;
			//instance = null;
		}
	}

	protected void finalize() {
		//CLog.writeLog("finalize");
	}

	public boolean send(String data) {
		byte[] bdata=null;
		try {
			bdata = data.getBytes();
			out.write(bdata, 0, bdata.length);
		}	catch (Exception e) {
			CLog.writeLog("des err");
			CLog.writeLog("getMessage=" + e.getMessage());
			CLog.writeLog("Exception=" + e.toString());
			//e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean send(byte[] data) {
		try {
			out.write(data, 0, data.length);
		}	catch (Exception e) {
			CLog.writeLog("des err");
			CLog.writeLog("getMessage=" + e.getMessage());
			CLog.writeLog("Exception=" + e.toString());
			//e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean sendData(String data) {
		byte[] bdata=null;
		byte	 flag=0x00;
		/*	byte [] tt = jZiplib(data);
		String look = byte2hex(tt);
		CLog.writeLog("look" + look);
		byte[] bdata = hex2byte(look);
		short len = (short) bdata.length;
		*/

		//CLog.writeLog("xmlData:[" + data.length() + "][" + data + "]");
		try {
			//不进行DES加解密
			//Des des = new Des("deskey");
			//bdata = des.createEncryptor(zip(data));
			if	(data.length()>8000){
				bdata = zip(data);
				flag=(byte)(flag|0x40);
			}	else	{
				bdata = data.getBytes();
			}
		}	catch (Exception e) {
			CLog.writeLog("des err");
			CLog.writeLog("getMessage=" + e.getMessage());
			CLog.writeLog("Exception=" + e.toString());
			//e.printStackTrace();
			return false;
		}

		short len = (short)(bdata.length+1);

		//CLog.writeLog("sendData len="+len+"+4");
		buf = new byte[4 + len];
		//short id = 32000;
		int id = (len*(len-5)*(len+8)+10000-2315)%9999;
		byte[] temp = null;
		try {
			temp = stoLH(len);
			System.arraycopy(temp, 0, buf, 0, temp.length);
			//CLog.writeLog("aaaaaaaaaaaaaaaa");
			temp = stoLH((short)id);
			System.arraycopy(temp, 0, buf, 2, temp.length);
			//CLog.writeLog("bbbbbbbbbbbbbbbbbb");
			//temp = hex2byte(look);
			//temp = bdata;
			//System.arraycopy(temp, 0, buf, 4, temp.length);
			buf[4] = flag;
			System.arraycopy(bdata, 0, buf, 5, bdata.length);

			//CLog.writeLog("ccccccccccccccccccc");
			out.write(buf, 0, buf.length);
		}	catch (Exception e) {
			connect =false;
			CLog.writeLog(name+":sendData err,"+e.toString());
			//e.printStackTrace();
			return false;
		}
		return true;
	}


	public boolean sendData(byte[] data) {
		byte[] bdata=data;
		byte	 flag=0x00;


		short len = (short)(bdata.length+1);

		//CLog.writeLog("sendData len="+len+"+4");
		buf = new byte[4 + len];
		//short id = 32000;
		int id = (len*(len-5)*(len+8)+10000-2315)%9999;
		byte[] temp = null;
		try {
			temp = stoLH(len);
			System.arraycopy(temp, 0, buf, 0, temp.length);
			//CLog.writeLog("aaaaaaaaaaaaaaaa");
			temp = stoLH((short)id);
			System.arraycopy(temp, 0, buf, 2, temp.length);
			//CLog.writeLog("bbbbbbbbbbbbbbbbbb");
			//temp = hex2byte(look);
			//temp = bdata;
			//System.arraycopy(temp, 0, buf, 4, temp.length);
			buf[4] = flag;
			System.arraycopy(bdata, 0, buf, 5, bdata.length);

			//CLog.writeLog("ccccccccccccccccccc");
			out.write(buf, 0, buf.length);
		}	catch (Exception e) {
			connect =false;
			CLog.writeLog(name+":sendData err,"+e.toString());
			//e.printStackTrace();
			return false;
		}
		return true;
	}


	public String read(String name) {
		byte[] rcvByte = new byte[40960];
		byte[] copyTo = null;
		int		 len = 0;
		String retStr="";
		try {
			//CLog.writeLog("正在监听readData:"+name);
			len = in.read(rcvByte, 0, 40960);
			if (len < 0) {
				connect =false;
				CLog.writeLog("readData:"+name+", read len["+len+"]< 0 err");
				return retStr;
			}
			copyTo = new byte[len];
			System.arraycopy(rcvByte, 0, copyTo, 0, len);
			retStr = new String(rcvByte);
		}	catch (Exception e) {
			connect =false;
			CLog.writeLog(e.toString());
			CLog.writeLog("readData:"+name+",err:" + e.getMessage());
		}

		return retStr;
	}

	public String readData(String name) {
		byte[] rcvByte = new byte[40960];
		byte[] copyTo = null;
		byte[] bbt = null;
		int		 len = 0;
		byte	 flag=0x00;
		String retStr=null;
		try {
			//CLog.writeLog("正在监听readData:"+name);
			len = in.read(rcvByte, 0, 40960);
			if (len < 0) {
				connect =false;
				CLog.writeLog("readData:"+name+", read len["+len+"]< 0 err");
				return null;
			}
			//if (len<=4 || (len-4)%8!=0) 			CLog.writeLog("err:readData:"+name+",len=" + len);
			if (len<=4) 			CLog.writeLog("err:readData:"+name+",len=" + len);
			flag=rcvByte[4];
			copyTo = new byte[len - 5];
			//byte[] btemp = new byte[2];
			//System.arraycopy(rcvByte, 0, btemp, 0, 2);
			//CLog.writeLog("rcvlen = " + this.toShort(btemp)+",len="+len);

			//System.arraycopy(rcvByte, 2, btemp, 0, 2);
			//CLog.writeLog("rcvNum = " + this.toShort(btemp));

			System.arraycopy(rcvByte, 5, copyTo, 0, len - 5);
		}	catch (Exception e) {
			connect =false;
			CLog.writeLog(e.toString());
			CLog.writeLog("readData:"+name+",err:" + e.getMessage());
			return null;
		}

		try {
			//不进行DES加解密
			if	((flag&0x80)==0x80){
				Des odes = new Des("deskey");
				bbt = odes.createDecryptorB(copyTo);
			}
			bbt = new byte[len - 5];
			System.arraycopy(copyTo, 0, bbt, 0, len - 5);
			//CLog.writeLog("plain=" + tt1.trim()+".");
			//byte[] bbt = tt1.getBytes();
		}	catch (Exception e) {
			CLog.writeLog("decryptor err:" + e.getMessage());
			//e.printStackTrace();
			return null;
		}

		if	(bbt==null)	return null;

		int i = 0;
		for (i = bbt.length - 1; i > 0; i--) {
			if (bbt[i] != 0x00) {
				break;
			}
		}
		i++;
		//CLog.writeLog("zip len=" + i);

		byte[] lastbt = new byte[i];
		System.arraycopy(bbt, 0, lastbt, 0, i);
		//String retStr = junZiplib(lastbt);
		if	((flag&0x40)==0x40){
			retStr = unzip(lastbt);
			//CLog.writeLog("readData:"+name+",readData=" + retStr);
		}	else	{
			retStr = new String(lastbt);
		}

		return retStr;
	}

	public byte[] readDataByte(String name) {
		byte[] rcvByte = new byte[40960];
		byte[] copyTo = null;
		byte[] bbt = null;
		int		 len = 0;
		byte	 flag=0x00;
		try {
			//CLog.writeLog("正在监听readData:"+name);
			len = in.read(rcvByte, 0, 40960);
			if (len < 0) {
				connect =false;
				CLog.writeLog("readData:"+name+", read len["+len+"]< 0 err");
				return null;
			}
			//if (len<=4 || (len-4)%8!=0) 			CLog.writeLog("err:readData:"+name+",len=" + len);
			if (len<=4) 			CLog.writeLog("err:readData:"+name+",len=" + len);
			flag=rcvByte[4];
			copyTo = new byte[len - 5];
			//byte[] btemp = new byte[2];
			//System.arraycopy(rcvByte, 0, btemp, 0, 2);
			//CLog.writeLog("rcvlen = " + this.toShort(btemp)+",len="+len);

			//System.arraycopy(rcvByte, 2, btemp, 0, 2);
			//CLog.writeLog("rcvNum = " + this.toShort(btemp));

			System.arraycopy(rcvByte, 5, copyTo, 0, len - 5);
		}	catch (Exception e) {
			connect =false;
			CLog.writeLog(e.toString());
			CLog.writeLog("readData:"+name+",err:" + e.getMessage());
			return null;
		}

		try {
			//不进行DES加解密
			if	((flag&0x80)==0x80){
				Des odes = new Des("deskey");
				bbt = odes.createDecryptorB(copyTo);
			}
			bbt = new byte[len - 5];
			System.arraycopy(copyTo, 0, bbt, 0, len - 5);
			//CLog.writeLog("plain=" + tt1.trim()+".");
			//byte[] bbt = tt1.getBytes();
		}	catch (Exception e) {
			CLog.writeLog("decryptor err:" + e.getMessage());
			//e.printStackTrace();
			return null;
		}

		if	(bbt==null)	return null;

		int i = 0;
		for (i = bbt.length - 1; i > 0; i--) {
			if (bbt[i] != 0x00) {
				break;
			}
		}
		i++;
		//CLog.writeLog("zip len=" + i);

		byte[] lastbt = new byte[i];
		System.arraycopy(bbt, 0, lastbt, 0, i);

		return lastbt;
	}

	public byte[] zip(String src) {
		byte[] data = null;
		byte[] output = new byte[10240];
		try {
			Deflater compresser = new Deflater();
			compresser.setInput(src.getBytes("UTF-8"));
			compresser.finish();
			int olen = compresser.deflate(output);

			data = new byte[olen];
			System.arraycopy(output, 0, data, 0,olen);
			///CLog.writeLog("zip finish...ilen="+src.length()+",olen="+data.length);
		}
		catch (Exception e) {
			//e.printStackTrace();
			CLog.writeLog("getMessage=" + e.getMessage());
			CLog.writeLog("Exception=" + e.toString());
			CLog.writeLog("Zip error,src=" + src);
			return null;
		}
		return data;
	}


	public String unzip(byte[] output) {
		String outputString;
		try {
			byte[] result = new byte[40960];
			//Decompress the bytes
			Inflater decompresser = new Inflater();
			decompresser.setInput(output, 0, output.length);
			int resultLength = decompresser.inflate(result);
			decompresser.end();

			//Decode the bytes into a String
			outputString = new String(result, 0, resultLength, "UTF-8");
			///CLog.writeLog("unzip finish...ilen="+output.length+",olen="+outputString.length());
		}
		catch (Exception e) {
			CLog.writeLog("Exception=" + e.toString());
			//e.printStackTrace();
			CLog.writeLog("unzip error");
			return null;
		}
		return outputString;
	}


	public String byte2hex(byte[] b) { //二行制转字符串
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			}
			else {
				hs = hs + stmp;
			}
			if (n < b.length - 1) {
				hs = hs + "";
			}
		}
		return hs.toUpperCase();
	}

	public byte[] hex2byte(String str) { //字符串转二进制
		int len = str.length();
		String stmp = null;
		byte bt[] = new byte[len / 2];
		for (int n = 0; n < len / 2; n++) {
			stmp = str.substring(n * 2, n * 2 + 2);
			bt[n] = (byte) (java.lang.Integer.parseInt(stmp, 16));
		}
		return bt;
	}

	public void setPrior(int prior)	{
		this.prior = prior;
	}
	public int getPrior()	{
		return this.prior;
	}
	public boolean getConnect()	{
		return connect;
	}
	public static void main(String[] args) {

		Client cli = new Client("192.168.1.5", 52005);
		String stemp = "";
		for (int i = 0; i < 100; i++) {
			stemp = stemp + "1";
		}
		byte[] tt = null;

		if (cli.sendData(stemp) == false) {
			CLog.writeLog("sendData error");
			return;
		}
		String retStr = cli.readData("client");
		if (retStr == null) {
			CLog.writeLog("read from server error !");
			return;
		}
		//CLog.writeLog("plain=" + tt1.trim()+".");

		CLog.writeLog("src = :" + retStr + ".");
		cli.close();

		BASE64Encoder enc1 = new BASE64Encoder();

		//passwdenc = enc1.encodeBuffer(passwd.getBytes());
		String passwdenc = enc1.encode("abcdef合速度g10232".getBytes());

		CLog.writeLog(passwdenc);
		BASE64Decoder enc2 = new BASE64Decoder();
		String tstr = null;
		try {
			tstr = new String(enc2.decodeBuffer(passwdenc));
		}
		catch (Exception e) {}
		CLog.writeLog(tstr);

	}
}

class ClientPool	{
	//static final int 	serverPort	=	52002;
	//static final String serverIP	=	"192.168.1.5";
/*
<prid value ="ccasserver">
	<keyword select ="host">
		<class option="192.168.1.5">ip</class>
	</keyword>
</prid>
*/
	static final String serverIP	=	App.getXmlValue(CLog.getConfigfile(),"ccasserver","host","ip","");
	static final int 	serverPort	=	Integer.parseInt(App.getXmlValue(CLog.getConfigfile(),"ccasserver","host","port","52002"));
	private static ClientPool instance = null;
	private PriorityBlockingQueue pbQueue = null;
	private ClientCollator clientCollator = null;
	private int count =20;		//socket缓冲池数量,缺省20个

	public static synchronized ClientPool getInstance() {
		if (instance == null) {
			instance = new ClientPool();
		}
		return instance;
	}

	public static synchronized ClientPool getInstance(int poolNumber) {
		if (instance == null) {
			instance = new ClientPool(poolNumber);
		}
		return instance;
	}

	private ClientPool()
	{
		initialize(20);
		CLog.writeLog("ccasServer ip:"+this.getServerIP());
	}

	private ClientPool(int poolNumber) //throws Exception
	{
		initialize(poolNumber);
	}

	public void initialize(int poolNumber) //throws Exception
	{
		try	{
			count=poolNumber;
			clientCollator = new ClientCollator();
			pbQueue = new PriorityBlockingQueue(count+1,clientCollator);
			for (int i=0;i<count;i++)
			{
				pbQueue.put(new	Client());
			}
		}	catch(Exception e)	{
			CLog.writeLog("ClientPool Exception="+ e.getMessage());
			CLog.writeLog("ClientPool Exception=" + e.toString());
			//e.printStackTrace();
			//throw e;
		}
	}

	public PriorityBlockingQueue getConnectQueue()	{
		return pbQueue;
	}

	public int getServerPort()	{
		return serverPort;
	}
	public String getServerIP()	{
		return serverIP;
	}
	public static synchronized void closeInstance() {
		CLog.writeLog("closeInstance" );
		if (instance != null) {
			instance.close();
		}
		instance = null;
	}

	public void close()	{
		CLog.writeLog("ClientPool close" );
		try {
				for (int i =0;i<pbQueue.size();i++)
				{
					Client client = (Client) pbQueue.take();
					if (client.getConnect()==true)
					{
						client.close();
						client = null;
					}
				}
		}
		catch (Exception e) {}
		finally{
			instance = null;
		}
	}

}

class ClientCollator implements Comparator	{
	public int compare(Object o1,Object o2)// throws ClassCastException
	{
		if (o1 instanceof Client && o2 instanceof Client)
		{
			Client client1 = (Client) o1;
			Client client2 = (Client) o2;

			if (client1.getPrior()>client2.getPrior())
				return 1;
			else if (client1.getPrior()==client2.getPrior())
				return 0;
			else
				return -1;
		}
		else {
			ClassCastException cce = new ClassCastException("比较时应输入Client类");
			throw cce;
		}
	}

}

class Mutex implements java.util.concurrent.locks.Lock, java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Our internal helper class
	private static class Sync extends AbstractQueuedSynchronizer {
		private static final long serialVersionUID = 1L;
		// Report whether in locked state
		protected boolean isHeldExclusively() {
		return getState() == 1;
		}

		// Acquire the lock if state is zero
		public boolean tryAcquire(int acquires) {
		assert acquires == 1; // Otherwise unused
		return compareAndSetState(0, 1);
		}

		// Release the lock by setting state to zero
		protected boolean tryRelease(int releases) {
		assert releases == 1; // Otherwise unused
		if (getState() == 0) throw new IllegalMonitorStateException();
		setState(0);
		return true;
		}

		// Provide a Condition
		Condition newCondition() { return new ConditionObject(); }

		// Deserialize properly
		private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		s.defaultReadObject();
		setState(0); // reset to unlocked state
		}
	}

	// The sync object does all the hard work. We just forward to it.
	private final Sync sync = new Sync();

	public void acquire()						{ sync.acquire(1); }
	public void lock()							{ sync.acquire(1); }
	public boolean tryLock()				{ return sync.tryAcquire(1); }
	public void release()						{ sync.release(1); }
	public void unlock()						{ sync.release(1); }
	public Condition newCondition()	{ return sync.newCondition(); }
	public boolean isLocked()				{ return sync.isHeldExclusively(); }
	public boolean hasQueuedThreads() { return sync.hasQueuedThreads(); }
	public void lockInterruptibly() throws InterruptedException {
		sync.acquireInterruptibly(1);
	}
	public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireNanos(1, unit.toNanos(timeout));
	}
}