package ccasws;

import javax.crypto.spec.*;
import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import sun.misc.*;

/**
 * DES加密的，文件中共有两个方法,加密、解密
 * 加密后的数据：Des.createEncryptor(Test);
 * 解密后的数据：Des.createDecryptor(Des.createEncryptor(Test));
 **/
public class Des {
	//"DES/ECB/Nopadding"
	private static final String Algorithm = "DES";	//定义 加密算法,可用 DES,DESede,Blowfish
	//private String inkey = "abcdefgh";
	//static final String strcommkey	=	App.getXmlValue(CLog.getConfigfile(),"ccasserver","deskey","comkey","");
	//static final String strpinkey		=	App.getXmlValue(CLog.getConfigfile(),"ccasserver","deskey","pinkey","");
	private static final byte[] stdcommkey = { 0x19, 0x70, (byte) 0x89, 0x20, 0x03, 0x02, 0x20, (byte) 0x89 };
	static final String strcommkey	=	"0123456789ABCDEF";
	static final String strpinkey		=	"0123456789ABCDEF";

	//private KeyGenerator keygen;
	private SecretKey deskey=null;
	private Cipher c=null;
	private byte[] cipherByte=null;
	private byte[] mydeskey=null;

	static{
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
	}

	/**
	* 初始化 DES 实例
	*/
	public Des() {
		if	(strpinkey.length()==16 || strpinkey.length()==32 || strpinkey.length()==48)	mydeskey=hex2byte(strpinkey);
				else											mydeskey=stdcommkey;
		init(mydeskey);
		//CLog.writeLog("mydeskey=[" + byte2hex(mydeskey) + "],comkey=[" + strcommkey + "]");
	}

	public Des(byte[] mykey) {
		init(mykey);
	}
	public Des(String typekey) {
		mydeskey=stdcommkey;
		if	(typekey.equals("deskey"))	mydeskey=hex2byte(strcommkey);
		if	(typekey.equals("pinkey"))	mydeskey=hex2byte(strpinkey);
		if	(typekey.equals("stdkey"))	mydeskey=stdcommkey;
		if	(mydeskey==null || typekey.length()==16 || typekey.length()==32 || typekey.length()==48)		mydeskey=hex2byte(typekey);

		init(mydeskey);
		//CLog.writeLog("mydeskey=[" + byte2hex(mydeskey) + "],comkey=[" + strcommkey + "]");
	}

	public void init() {
		init(stdcommkey);
	}

	public void init(String strkey) {
		//init(hex2byte(strkey));
		mydeskey=stdcommkey;
		if	(strkey.length()==16 || strkey.length()==32 || strkey.length()==48)			mydeskey=hex2byte(strkey);
		if	(strkey.equals("deskey"))	mydeskey=hex2byte(strcommkey);
		if	(strkey.equals("pinkey"))	mydeskey=hex2byte(strpinkey);
		init(mydeskey);
	}

	public void init(byte[] initkey) {
		try {
			//keygen = KeyGenerator.getInstance(Algorithm);
			//deskey = keygen.generateKey();
			//deskey = new SecretKeySpec(initkey, Algorithm);
			//deskey = new SecretKeySpec(pinkey, Algorithm);
			//c = Cipher.getInstance("DES");
			//c = Cipher.getInstance("DESede");
			//c = Cipher.getInstance("DES/ECB/Nopadding");
			if	(initkey.length==16){
				String myinitkey=byte2hex(initkey);
				myinitkey+=myinitkey.substring(0,16);
				initkey=hex2byte(myinitkey);
				myinitkey=null;
			}

			if	(initkey.length==24){
				deskey = new SecretKeySpec(initkey, "DESede");
				c = Cipher.getInstance("DESede/ECB/Nopadding");
			}	else	{
				deskey = new SecretKeySpec(initkey, Algorithm);
				c = Cipher.getInstance("DES/ECB/Nopadding");
			}
			//CLog.writeLog("initkey.length=[" + initkey.length + "]"+deskey.getEncoded().length);
			//c = Cipher.getInstance(Algorithm);

			//IvParameterSpec ivSpec = new IvParameterSpec (inkey.getBytes());
			//c.init (Cipher.ENCRYPT_MODE, deskey, ivSpec);

		}
/*
		catch (NoSuchProviderException ex ) {
			//CLog.writeLog("getMessage=" + ex.getMessage());
			//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",Exception=" + ex.toString());
			//ex.printStackTrace();
		}
*/

		catch (NoSuchAlgorithmException ex) {
			CLog.writeLog("getMessage=" + ex.getMessage());
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",Exception=" + ex.toString());
			//ex.printStackTrace();
		}
		catch (NoSuchPaddingException ex) {
			CLog.writeLog("getMessage=" + ex.getMessage());
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",Exception=" + ex.toString());
			//ex.printStackTrace();
		}
	}

	/**
	* 对 String 进行加密
	* @param str 要加密的数据
	* @return 返回加密后的 byte 数组
	*/
	public byte[] createEncryptor(String str) {

		int len = str.length();
		int maxPage = len / 8;
		maxPage += (len % 8) == 0 ? 0 : 1;
		try {
			c.init(Cipher.ENCRYPT_MODE,	deskey);
			byte[] aa = new byte[8 * maxPage];
			for (int i = 0; i < aa.length; i++)
				aa[i] = (byte) ' ';
			System.arraycopy(str.getBytes(), 0, aa, 0, str.getBytes().length);
			//cipherByte = c.doFinal(str.getBytes());
			cipherByte = c.doFinal(aa);
		}
		catch (java.security.InvalidKeyException ex) {
			CLog.writeLog("getMessage=" + ex.getMessage());
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",Exception=" + ex.toString());
			//ex.printStackTrace();
		}
		catch (javax.crypto.BadPaddingException ex) {
			CLog.writeLog("getMessage=" + ex.getMessage());
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",Exception=" + ex.toString());
			//ex.printStackTrace();
		}
		catch (javax.crypto.IllegalBlockSizeException ex) {
			CLog.writeLog("getMessage=" + ex.getMessage());
			CLog.writeLog("Exception0=" + ex.toString());
			//ex.printStackTrace();
		}
		//CLog.writeLog("enc len=" + cipherByte.length);
		return cipherByte;
	}

	public byte[] createEncryptor(byte[] bt) {
		int len = bt.length;
		int maxPage = len / 8;
		maxPage += (len % 8) == 0 ? 0 : 1;
		byte[] aa = new byte[8 * maxPage];
		for (int i = 0; i < aa.length; i++)
			aa[i] = (byte) 0x00;
		System.arraycopy(bt, 0, aa, 0, bt.length);
		//cipherByte = c.doFinal(str.getBytes());

		try {
			c.init(Cipher.ENCRYPT_MODE, deskey);
			//	cipherByte = c.doFinal(bt);
			cipherByte = c.doFinal(aa);
		}
		catch (java.security.InvalidKeyException ex) {
			CLog.writeLog("getMessage=" + ex.getMessage());
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",Exception=" + ex.toString());
			//ex.printStackTrace();
		}
		catch (javax.crypto.BadPaddingException ex) {
			CLog.writeLog("getMessage=" + ex.getMessage());
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",Exception=" + ex.toString());
			//ex.printStackTrace();
		}
		catch (javax.crypto.IllegalBlockSizeException ex) {
			CLog.writeLog("getMessage=" + ex.getMessage());
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",Exception=" + ex.toString());
			//ex.printStackTrace();
		}
		return cipherByte;
	}

	/**
	* 对 Byte 数组进行解密
	* @param buff 要解密的数据
	* @return 返回加密后的 String
	*/
	public String createDecryptor(byte[] buff) {

		//return (new String(createDecryptorB(buff)));
		return (byte2hex(createDecryptorB(buff)));
	}

	public byte[] createDecryptorB(byte[] buff) {
		cipherByte=null;
		try {
			//c = Cipher.getInstance("DESede","SunJCE");
			c.init(Cipher.DECRYPT_MODE, deskey);
			cipherByte = c.doFinal(buff);
			//String str = byte2hex(cipherByte);
			//CLog.writeLog("createDecryptor len=["+str.length()+"], str =" + str);
		}
		catch (java.security.InvalidKeyException ex) {
			CLog.writeLog("getMessage=" + ex.getMessage());
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",Exception=" + ex.toString());
			//ex.printStackTrace();
		}
		catch (javax.crypto.BadPaddingException ex) {
			CLog.writeLog("getMessage=" + ex.getMessage());
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",Exception=" + ex.toString());
			//ex.printStackTrace();
		}
		catch (javax.crypto.IllegalBlockSizeException ex) {
			CLog.writeLog("getMessage=" + ex.getMessage());
			CLog.writeLog("Exception2=" + ex.toString());
			CLog.writeLog("buff len=["+buff.length+"]...");
			//ex.printStackTrace();
		}
		return (cipherByte);
	}

	public String base64encode(byte[] b) {
		BASE64Encoder enCoder = new BASE64Encoder();
		return  enCoder.encode(b);
	}

	public byte[] base64decode(String s) {
		BASE64Decoder deCoder = new BASE64Decoder();
		try {
			if	(s.length()==12)	return  deCoder.decodeBuffer(s);
					else							return  createEncryptor("password error:"+s);
		}

		catch (Exception e) {
			CLog.writeLog("base64decode:["+s+"],["+e.getMessage()+"]");
			return  createEncryptor("password error:"+s);
		}
	}


	public String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) hs = hs + "0" + stmp;
			else hs = hs + stmp;
			//if (n<b.length-1)	hs=hs+":";
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

	public String dec(String str) {
		return createDecryptor(hex2byte(str));
	}

	public byte[] decB(String str) {
		return createDecryptorB(hex2byte(str));
	}

	public String enc(String str) {
		return byte2hex(createEncryptor(str));
	}

	public String base64dec(String str) {
		return createDecryptor(base64decode(str));
	}

	public byte[] base64decB(String str) {
		return createDecryptorB(base64decode(str));
	}

	public String base64enc(String str) {
		return base64encode(createEncryptor(str));
	}

	public String base64encH(String str) {
		return base64encode(createEncryptor(hex2byte(str)));
	}

	public static void main(String args[]) {
		Des odes = new Des("31323334353637383132333435363738");
		String stemp = "";
		for (int i = 0; i < 16; i++) {
			stemp = stemp + "1";
		}
		stemp = "abc12fgh";
		String tt = odes.enc(stemp);
		CLog.writeLog("" + tt);
		// tt = "F9087F2BECBC2EEF";

		String tt1 = odes.dec(tt);
		CLog.writeLog("plain=" + tt1 + ".");

		byte[] bt = {
				(byte) 0xd4, (byte) 0xbb, (byte) 0x2c, (byte) 0xe6, (byte) 0x8a, 0x76,
				(byte) 0x26, (byte) 0xd3, 0x26, 0x1b, (byte) 0x36, (byte) 0xe7, 0x6a,
				(byte) 0x2f, (byte) 0xba, 0x57};
		tt1 = odes.createDecryptor(bt);
		CLog.writeLog("plain=" + tt1.trim() + ".");
		/*
		encode oe = new encode();
		byte [] input = stemp.getBytes();
		byte [] output =	oe.decode(input);
		String ret = new String(output);
		CLog.writeLog("ret="+ret);

		*/
		Des des1=new Des("ABA285D349D97573ABA285D349D97573");
		tt=des1.dec("1fc2240fc24f6a891");
		tt1=App.Xor(tt,"0000408029001105",16).replaceAll("F","").replaceAll("f","").trim();
		System.out.println(tt+","+tt1);
		Des des=new Des("stdkey");
		des.init("stdkey");
		;
		String	tspw="111111";
		System.out.println(des.enc(tspw)+","+des.base64enc(tspw));
/*
		System.out.println("CVV="+App.getCVV("4123456789012345","8701","101","0123456789ABCDEF","FEDCBA9876543210"));
		System.out.println("PVV="+App.getPVV("46666555544441111","345612","2","0123456789ABCDEF","FEDCBA9876543210"));
		System.out.println("PINBlock="+App.getPINBlock("4123456789012345","111111","FEDCBA9876543210"));
*/
	}

}
