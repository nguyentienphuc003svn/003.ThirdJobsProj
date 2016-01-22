package ccasws;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.zip.*;
import java.net.*;
import java.text.*;
import java.math.*;
import java.lang.reflect.*;
import sun.misc.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import gnu.io.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class App {
	static private final int dayType = 1;
	static private final int monthType = 2;
	static private final int yearType = 3;
	static private final int typeNum = 100;
	//static private final String myencode="GBK";
	static private String myencode="UTF-8";
	//static private final String myencode="ISO8859_1";
	static private String resourcefile="ApplicationResources";
	static private String osname="";
	static private ResourceBundle resourceBundle=null;
	static private Locale currentLocale=Locale.US;
	//static private Locale currentLocale=Locale.getDefault();
	static private		Cell		mycell = null;
	static private		Phrase	myphrase = null;

	public App() {
		osname=System.getProperty("os.name").toLowerCase();
	}

	public void setEncode() {
		this.myencode="UTF-8";
		CLog.writeLog("setEncode=["+this.myencode+"]");
	}
	public void setEncode(String myencode) {
		if	(myencode!=null && myencode.length()>1)		this.myencode=myencode;
		CLog.writeLog("setEncode=["+this.myencode+"]"+myencode);
	}
	public void setLocale(Locale currentLocale) {
		if	(!currentLocale.getLanguage().equals(this.currentLocale.getLanguage()) || !currentLocale.getCountry().equals(this.currentLocale.getCountry()))
				resourceBundle=null;
		this.currentLocale=currentLocale;
		//CLog.writeLog("getLanguage:Language=["+currentLocale.getLanguage()+"],Country=["+currentLocale.getCountry()+"]");
	}

	public void setResourcefile(String resourcefile) {
		this.resourcefile=resourcefile;
		//CLog.writeLog("getresourcefile=["+resourcefile+"]");
	}

	public static String getString(String key)
	{
		String ret=key;
		try {
			if	(resourceBundle==null)	resourceBundle= ResourceBundle.getBundle(resourcefile,currentLocale);
			//ResourceBundle= ResourceBundle.getBundle(resourcefile,currentLocale);			
			ret=resourceBundle.getString(key).trim();
			if	(getStringLength(ret)==ret.length())				ret+=" ";
			//CLog.writeLog("getLanguage:Language=["+resourceBundle.getLocale().getLanguage()+"],Country=["+resourceBundle.getLocale().getCountry()+"],resourcefile=["+resourcefile+"],key=["+key+"]"+ret);
		} catch ( Exception e ) {
			ret=key;
			if	(ret==null ||ret.length()<=0)	ret="";
			CLog.writeLog("error:getLanguage:Language=["+currentLocale.getLanguage()+"],Country=["+currentLocale.getCountry()+"],getresourcefile=["+resourcefile+"],key=["+key+"]"+ret);
		}
		
		return ret;
	}


	public static Phrase setMyPhrase(String str){
		myphrase = new Phrase(str);
		try {
			if	(str.length()!=getStringLength(str)){
					com.lowagie.text.Font chineseFont = new com.lowagie.text.Font(BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));
					myphrase = new Phrase(str, chineseFont);
			}
		} catch ( Exception e ) {
		}
		return	myphrase;
	}

	public static Cell setMyCell(Object objcell){
		String ret=null;
		if	(objcell instanceof String)			ret=(String)objcell;

		if	(objcell instanceof Integer)		ret=String.format("%d",((Integer)objcell).intValue());
		if	(objcell instanceof Long)				ret=String.format("%d",((Long)objcell).longValue());
		if	(objcell instanceof Double)			ret=String.format("%.f",((Double)objcell).doubleValue());
		if	(objcell instanceof Float)			ret=String.format("%.f",((Float)objcell).floatValue());
		if	(objcell instanceof Boolean){
			if	(((Boolean)objcell).booleanValue())	ret="true";	else	ret="false";
		}
		if	(objcell instanceof Date){
			ret=(new SimpleDateFormat("yyyy/MM/dd")).format((Date)objcell);
		}

		if	(ret==null)	ret=(String)objcell;


		return	setMyCell(ret);
	}
	public static Cell setMyCell(String str){
		return	setMyCell(str,com.lowagie.text.Element.ALIGN_CENTER,java.awt.Color.WHITE);
	}
	public static Cell setMyCell(Object objcell,java.awt.Color color){
		String ret=null;
		if	(objcell instanceof String)			ret=(String)objcell;

		if	(objcell instanceof Integer)		ret=String.format("%d",((Integer)objcell).intValue());
		if	(objcell instanceof Long)				ret=String.format("%d",((Long)objcell).longValue());
		if	(objcell instanceof Double)			ret=String.format("%.f",((Double)objcell).doubleValue());
		if	(objcell instanceof Float)			ret=String.format("%.f",((Float)objcell).floatValue());
		if	(ret==null)	ret=(String)objcell;
		return	setMyCell(ret,com.lowagie.text.Element.ALIGN_CENTER,color);
	}
	public static Cell setMyCell(String str,java.awt.Color color){
		return	setMyCell(str,com.lowagie.text.Element.ALIGN_CENTER,color);
	}
	public static Cell setMyCell(String str,int alignment){
		return	setMyCell(str,alignment,java.awt.Color.WHITE);
	}
	public static Cell setMyCell(Object objcell,int myalignment,java.awt.Color mycolor){
		String ret=null;
		if	(objcell instanceof String)			ret=(String)objcell;

		if	(objcell instanceof Integer)		ret=String.format("%d",((Integer)objcell).intValue());
		if	(objcell instanceof Long)				ret=String.format("%d",((Long)objcell).longValue());
		if	(objcell instanceof Double)			ret=String.format("%.f",((Double)objcell).doubleValue());
		if	(objcell instanceof Float)			ret=String.format("%.f",((Float)objcell).floatValue());
		if	(ret==null)	ret=(String)objcell;
		return	setMyCell(ret,myalignment,mycolor);
	}
	public static Cell setMyCell(String str,int alignment,java.awt.Color color){
		//if	(alignment==com.lowagie.text.Element.ALIGN_RIGHT )	str+=" ";
		//if	(alignment==com.lowagie.text.Element.ALIGN_LEFT)	str=" "+str;
		//com.lowagie.text.Font chineseFont = new com.lowagie.text.Font(BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED), 12, com.lowagie.text.Font.NORMAL, Color.BLACK);

		mycell = new Cell(str);
		try {
			if	(str.length()!=getStringLength(str)){
					com.lowagie.text.Font chineseFont = new com.lowagie.text.Font(BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));
					mycell = new Cell(new Paragraph(str, chineseFont));
			}
		} catch ( Exception e ) {
		}
		//垂直居中
		//mycell.setPadding(x);
		mycell.setUseAscender(true);
		mycell.setHorizontalAlignment(alignment);
		mycell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
		if	(color!=null)	mycell.setBackgroundColor(color);
		return	mycell;
	}


	//
	public Object	myinvoke(String classname,String methodname) throws Exception	{
		Object	arglist[] = new Object[]{};
		return myinvoke(classname,methodname,arglist);
	}

	//
	public Object	myinvoke(String classname,String methodname,Object[] arglist) throws Exception	{

		CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",begin...,classname=["+classname+"],methodname=["+methodname+"]");

		Class[] partypes = new Class[]{
				ccasws.ObInterface.class
		};

		Object	paramlist[] = new Object[]{};
		Object	retobj=null;
		Class		classlist[] = new Class []{};

		try {
			Class myclass = Class.forName(classname);
			//Method mymethod = myclass.getMethod(methodname);
			Method mymethod = myclass.getMethod(methodname,classlist);
			Constructor myconstructor=myclass.getConstructor(partypes);
			Object myobject = myconstructor.newInstance(paramlist);

			//
			retobj=mymethod.invoke(myobject,arglist);
		} catch ( Exception e ) {
			// TODO 自动生成 catch 块
			//e.printStackTrace();
			CLog.writeLog(e.toString());
			throw e;
		}

		return retobj;
	}

	//执行类的方法
	public Object	myinvoke(Object o,String methodname) throws Exception	{
		Object	arglist[] = new Object[]{};
		return myinvoke(o,methodname,arglist);
	}

	//执行类的方法
	public Object	myinvoke(Object o,String methodname,Object[] arglist) throws Exception{
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",begin...,classname=["+o.getClass().getName()+"],methodname=["+methodname+"]");
		Object	retobj=null;
		Class		c=o.getClass();
		int			max=0;
		Class		classlist[]=null;
		try {
			for	(int i=0;;i++){
				if	(arglist[i]==null)		break;
				max=i+1;
			}
	 	} catch ( Exception e ) {
			// TODO 自动生成 catch 块
			//e.printStackTrace();
			//CLog.writeLog(e.toString());
			//throw e;
		}

		try {
			classlist=new Class [max];
			//CLog.writeLog("max="+max+"|"+methodname);
			for	(int i=0;i<max;i++){
				classlist[i]=arglist[i].getClass();
			}

			/**/
			//Method mymethod=c.getMethod(methodname);
			Method mymethod=c.getMethod(methodname,classlist);
			//执行方法
			//retobj=mymethod.invoke(o,arglist);
			retobj=mymethod.invoke(o);
			classlist=null;
	 	} catch ( Exception e ) {
			// TODO 自动生成 catch 块
			//e.printStackTrace();
			//CLog.writeLog(methodname+","+e.toString());
			throw e;
		}

		//CLog.writeLog(methodname+"==="+retobj.toString());
		return retobj;
	}
	public static String typeToString(int i)
	{
		String ret="";
		switch(i)
		{
			case(1):ret="String";break;
			//case(2):ret="NUMERIC";break;
			case(2):ret="Double";break;
			case(3):ret="Double";break;
			case(4):ret="Int";break;
			case(5):ret="Int";break;
			case(6):ret="FLOAT";break;
			case(8):ret="DOUBLE";break;
			case(12):ret="String";break;
			//case(12):ret="VARCHAR";break;
			case(91):ret="DATE";break;
			default:ret="String";
		}
		return ret;
	}

	public static String transFirstUpperCase(String chi) throws Exception
	{
		String result = null;
		if	(chi==null || chi.trim().length()<=0)	return chi;
		try	{
			StringBuffer temp=new StringBuffer(chi);
			temp.setCharAt(0,chi.toUpperCase().charAt(0));
			result = new String(temp);
		}	catch	(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+" err...,Exception=["+e.toString()+"]");
			throw e;
		}

		return result;
	}


	public static String asc2hex(String str) {
		int len = str.length();
		String stmp = null;
		byte bt[] = new byte[len];
		bt=str.getBytes();
		return byte2hex(bt);
	}

	public static String hex2asc(String str) {
		byte bt[] = hex2byte(str);
		return new String(bt);
	}
	public static String byte2hex(byte[] b) {
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

	public static byte[] hex2byte(String str) { //字符串转二进制
		int len = str.length();
		String stmp = null;
		byte bt[] = new byte[len / 2];
		for (int n = 0; n < len / 2; n++) {
			stmp = str.substring(n * 2, n * 2 + 2);
			bt[n] = (byte) (java.lang.Integer.parseInt(stmp, 16));
		}
		return bt;
	}
	public static byte[] Xor(byte[] desc,byte[] sour,int len) {
		byte[] ret=new byte[len];
		ret=desc;
		try	{
			for (int i=0;i<len;i++)		ret[i]^=sour[i];
		}	catch	(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+" err1...,Exception=["+e.toString()+"],len="+len);
		}

		return ret;
	}

	public static String Xor(String desc,String sour,int len) {
		String ret=desc;
		try	{
			byte[] desc1=hex2byte(desc);
			byte[] sour1=hex2byte(sour);
			for (int i=0;i<len/2;i++)		desc1[i]^=sour1[i];
			ret=byte2hex(desc1);
		}	catch	(Exception e)	{
			//System.out.println(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+" err...,Exception=["+e.toString()+"]");
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+" err2...,Exception=["+e.toString()+"],len="+len);
		}
		return ret;
	}

	public static String transferKey(byte[] PINBlock,String key1) {
		return transferKey(byte2hex(PINBlock),key1,"stdkey");
	}
	public static String transferKey(String PINBlock,String key1) {
		if	(PINBlock==null || key1==null || (key1.length()!=6 && key1.length()!=16 && key1.length()!=32))	return PINBlock;

		return transferKey(PINBlock,key1,"stdkey");
	}


	public static String transferKey(String data,String key1,String key2) {
		if	(data==null || key1==null || key2==null || (key1.length()!=6 && key1.length()!=16 && key1.length()!=32) || (key2.length()!=6 && key2.length()!=16 && key2.length()!=32))	return data;
		String ret=data;
		try	{
			Des des1=new Des(key1);
			ret=des1.dec(data);
			des1.init(key2);
			ret=byte2hex(des1.createEncryptor(hex2byte(ret)));
			des1=null;
		}	catch	(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+" err...,Exception=["+e.toString()+"]");
		}

		return ret;
	}

	public static String getPIN(String PINBlock,String cdno){
		return getPIN("stdkey",PINBlock,cdno);
	}

	public static String getPIN(String deskey,byte[] PINBlock,String cdno){
		if	(deskey==null || (deskey.length()!=32 && deskey.length()!=16 && deskey.length()!=6) || PINBlock.length!=8 || cdno==null || cdno.length()<=13)
				//throw new Exception("get PIN error");
				return App.byte2hex(PINBlock);
		return getPIN(deskey,App.byte2hex(PINBlock),cdno);
	}

	public static String getPIN(String deskey,String PINBlock,String cdno){
		String ret=PINBlock;
		if	(deskey==null || (deskey.length()!=32 && deskey.length()!=16 && deskey.length()!=6) || PINBlock==null || PINBlock.length()!=16 || cdno==null || cdno.length()<=13)
				return ret;
		//CLog.writeLog("deskey="+deskey+",PINBlock="+PINBlock+",cdno="+cdno);
		try	{
			Des des1=new Des(deskey);
			ret=des1.dec(PINBlock);
			//CLog.writeLog("deskey="+deskey+",PINBlock="+PINBlock+",cdno="+cdno+",ret="+ret);
			ret=App.Xor(ret,"0000"+cdno.substring(cdno.length()-13,cdno.length()-1),16);
			ret=ret.substring(2).toUpperCase().replaceAll("F","").trim();
			des1=null;
		}	catch	(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+" err...,Exception=["+e.toString()+"]");
			//throw e;
		}
		return ret;
	}

	public static String getCVV(String pan,String expire,String servicecode,String CVKA,String CVKB) {
		if	(pan==null || expire==null || servicecode==null || CVKA==null || CVKB==null)	return "000";
		if	(CVKA.length()!=16 || CVKB.length()!=16)	return "000";
		String retstr=pan+expire+servicecode+"00000000000000000000000000000000000";
		String retstr1=retstr.substring(0,16);
		String retstr2=retstr.substring(16,32);
		Des des1=new Des(CVKA);
		Des des2=new Des(CVKB);

		//System.out.println("retstr1="+retstr1+",retstr2="+retstr2);
		//System.out.println("CVKA="+CVKA+",CVKB="+CVKB);
		//retstr=retstr1+","+retstr2;
		//retstr=byte2hex(des1.createEncryptor(hex2byte(retstr1)));
		retstr=byte2hex(des1.createEncryptor(des2.createDecryptorB(des1.createEncryptor(App.Xor(des1.createEncryptor(hex2byte(retstr1)),hex2byte(retstr2),8)))));
		//retstr=des1.enc(des2.dec(des1.enc(App.Xor(des1.enc(retstr1),retstr2,16))));
		retstr1=retstr.toUpperCase().replaceAll("[ABCDEF]","");
		retstr2=retstr.replaceAll("[0123456789]","").replaceAll("A","0").replaceAll("A","0").replaceAll("B","1").replaceAll("C","2").replaceAll("D","3").replaceAll("E","4").replaceAll("F","5");
		return (retstr1+retstr2).substring(0,3);
	}

	public static String getPVV(String pan,String PIN,String PVKIndex,String PVKA,String PVKB) {
		if	(pan==null || PIN==null || PVKIndex==null || PVKA==null || PVKB==null || PIN.length()<4)				return "0000";
		if	(PVKA.length()!=16 || PVKB.length()!=16 || pan.length()<=11 || PVKIndex.length()<1)	return "0000";
		if	(PIN.length()==16)	PIN=getPIN(PIN,pan);

		String retstr=pan.substring(pan.length()-12,pan.length()-1)+PVKIndex+PIN.substring(0,4);
		String retstr1="";
		String retstr2="";
		Des des1=new Des(PVKA);
		Des des2=new Des(PVKB);

		//System.out.println("retstr="+retstr+",lenth="+retstr.length());
		//System.out.println("PVKA="+PVKA+",PVKB="+PVKB);
		//retstr1=byte2hex(des1.createEncryptor(hex2byte(retstr)));
		//System.out.println("retstr1="+retstr1);
		retstr=byte2hex(des1.createEncryptor(des2.createDecryptorB(des1.createEncryptor(hex2byte(retstr)))));
		//System.out.println("retstr="+retstr);
		//retstr=des1.enc(des2.dec(des1.enc(App.Xor(des1.enc(retstr1),retstr2,16))));
		retstr1=retstr.toUpperCase().replaceAll("[ABCDEF]","");
		retstr2=retstr.replaceAll("[0123456789]","").replaceAll("A","0").replaceAll("A","0").replaceAll("B","1").replaceAll("C","2").replaceAll("D","3").replaceAll("E","4").replaceAll("F","5");
		return (retstr1+retstr2).substring(0,4);
	}




	public static String getPINBlock(String pan,String PIN) {

		return getPINBlock(pan,PIN,"stdkey");
	}

	public static String getPINBlock(String pan,String PIN,String PINkey) {
		//CLog.writeLog("pan="+pan+",PIN="+PIN+",PINkey="+PINkey);
		if	(pan==null || PIN==null || PINkey==null)	return "0000000000000000";
		if	((PINkey.length()!=32 && PINkey.length()!=16 && PINkey.length()!=6) || pan.length()<=12 ||  PIN.length()<4)	return "0000000000000000";
		if	(PIN.length()==16)	return PIN;
		String retstr1="0000"+pan.substring(pan.length()-13,pan.length()-1);
		String retstr2=(App.LFZero(2,PIN.length())+PIN+"FFFFFFFFFFFFFFFFFFFFFF").substring(0,16);
		Des des1=new Des(PINkey);

		//System.out.println("retstr1="+retstr1+",retstr2="+retstr2);
		//retstr=retstr1+","+retstr2;
		//retstr1=byte2hex(App.Xor(hex2byte(retstr1),hex2byte(retstr2),8));
		retstr1=byte2hex(des1.createEncryptor(App.Xor(hex2byte(retstr1),hex2byte(retstr2),8)));
		return retstr1;
	}

	//创建目录
	public static String Mkdir(String path) {
		String msg=null;
		java.io.File dir;

		//新建文件对象
		dir =new java.io.File(path);
		if	(dir == null) {
			msg = "error:null dir...";
			return msg;
		}
		if	(dir.isFile()) {
			msg = "error:such file " + dir.getAbsolutePath() + " exists...";
			return msg;
		}
		if	(!dir.exists()) {
			boolean result = dir.mkdirs();
			if	(result == false) {
				msg = "error:create dir " + dir.getAbsolutePath() + "fail...";
				return msg;
			}
			//如果成功创建目录，则无输出。
			//msg ="成功创建目录: " + dir.getAbsolutePath() + "";
			return msg;
		}else {
			//msg = "错误原因:目录" + dir.getAbsolutePath() + "已存在。";
		}
		return msg;
	}

	//删除目录下的所有文件
	public static String Rmdirfile(String path) {
		String msg=null;
		Mkdir(path);
		//建立当前目录中文件的File对象
		java.io.File dir=new File(path);
		//取得代表目录中所有文件的File对象数组
		java.io.File list[]=dir.listFiles();
		for(int i=0;i<list.length;i++){
			//删除文件
			if	(list[i].isFile())	list[i].delete();
		}

		return msg;
	}


	public static String[] getXmlfield(String xmlfile,String prid,String section) {
		//writeLog("xmlfile:"+xmlfile+",prid="+prid+",section="+section);
		//String[] xmlfield=new String[80];
		String[] xmlfield=null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream infile = new FileInputStream(xmlfile);
			Document doc = builder.parse(infile);
			doc.normalize();
			Element orders = doc.getDocumentElement();
			String nodeName;
			int i = 0,j = 0,k = 0;
			int num=0;
			NodeList order = orders.getElementsByTagName("prid");
			//writeLog("count order:"+order.getLength());

			for (i = 0; i < order.getLength(); i++) {

				nodeName = order.item(i).getAttributes().getNamedItem("value").getNodeValue();
				//writeLog("nodeName:"+nodeName);

				if (nodeName.equalsIgnoreCase(prid)) {
					for (j = 0;j < order.item(i).getChildNodes().getLength();j++) {
						if (order.item(i).getChildNodes().item(j).getNodeName().equalsIgnoreCase("keyword")){
							String sel = order.item(i).getChildNodes().item(j).getAttributes().getNamedItem("select").getNodeValue();
							if (sel.equalsIgnoreCase(section)) {
								//writeLog("sel:" + sel+"len="+order.item(i).getChildNodes().item(j).getChildNodes().getLength());
								xmlfield=new String[order.item(i).getChildNodes().item(j).getChildNodes().getLength()];
								for (k = 0;k < order.item(i).getChildNodes().item(j).getChildNodes().getLength(); k++) {
									if (order.item(i).getChildNodes().item(j).getChildNodes().item(k).getNodeName().equalsIgnoreCase("class")){
										String ret = order.item(i).getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("option").getNodeValue();
										//writeLog("ret:" + ret);
										String value = order.item(i).getChildNodes().item(j).getChildNodes().item(k).getTextContent();

										if (value == null) value = "";
										//writeLog("value:" + value);
										//writeLog(num+",xmlfield=" + value+",xmlvalue="+ret);
										xmlfield[num++]=value;
										xmlfield[num++]=ret;
										//if (cls.equalsIgnoreCase(value))	return ret;
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			CLog.writeLog(e.toString());
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+"," + e.getMessage() );
			return xmlfield;
		}
		return xmlfield;
	}


	/* 日志文件路径设置
	<?xml version="1.0" encoding="GBK"?>
	<item>
	<prid value ="clog">
				<keyword select ="log">
						<class option="d:\\ccaslog">path</class>
				</keyword>
		</prid>
	</item>

	*/
	public static String getXmlValue(String xmlfile,String prid,String section,String cls) {
		return	getXmlValue(xmlfile,prid,section,cls,cls);
	}

	public static String getXmlValue(String xmlfile,String prid,String section,String cls,String defaultpath) {
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+","+xmlfile+","+prid+","+section+","+cls+","+defaultpath);
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream infile = new FileInputStream(xmlfile);
			Document doc = builder.parse(infile);
			doc.normalize();
			Element orders = doc.getDocumentElement();
			String nodeName;
			int i = 0,j = 0,k = 0;
			NodeList order = orders.getElementsByTagName("prid");
			//writeLog("count order:"+order.getLength());

			for (i = 0; i < order.getLength(); i++) {

				nodeName = order.item(i).getAttributes().getNamedItem("value").getNodeValue();
				//writeLog("nodeName:"+nodeName);

				if (nodeName.equalsIgnoreCase(prid)) {
					for (j = 0;j < order.item(i).getChildNodes().getLength();j++) {
						if (order.item(i).getChildNodes().item(j).getNodeName().equalsIgnoreCase("keyword")){
							String sel = order.item(i).getChildNodes().item(j).getAttributes().getNamedItem("select").getNodeValue();
							//writeLog("sel:" + sel);
							if (sel.equalsIgnoreCase(section)) {
								for (k = 0;k < order.item(i).getChildNodes().item(j).getChildNodes().getLength(); k++) {
									if (order.item(i).getChildNodes().item(j).getChildNodes().item(k).getNodeName().equalsIgnoreCase("class")){
										String ret = order.item(i).getChildNodes().item(j).getChildNodes().item(k).getAttributes().getNamedItem("option").getNodeValue();
										//writeLog("ret:" + ret);
										String value = order.item(i).getChildNodes().item(j).getChildNodes().item(k).getTextContent();

										if (value == null) value = "";
										//writeLog("value:" + value);
										if (cls.equalsIgnoreCase(value)) {
											return ret;
										}
									}
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			CLog.writeLog(e.toString());
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+","+xmlfile+","+prid+","+section+","+e.getMessage() );
			return defaultpath;
		}
		return defaultpath;
	}

	public byte[]	GBK2UTF8(String chinese) {
		char c[] = chinese.toCharArray();
		byte[] fullByte = new byte[3 * c.length];
		String	ret="";
		for (int i = 0; i < c.length; i++) {
			int m = (int) c[i];
			String word = Integer.toBinaryString(m);
			StringBuffer sb = new StringBuffer();
			int len = 16 - word.length();
			for (int j = 0; j < len; j++) {
				sb.append("0");
			}
			sb.append(word);
			sb.insert(0, "1110");
			sb.insert(8, "10");
			sb.insert(16, "10");
			String s1 = sb.substring(0, 8);
			String s2 = sb.substring(8, 16);
			String s3 = sb.substring(16);
			byte b0 = Integer.valueOf(s1, 2).byteValue();
			byte b1 = Integer.valueOf(s2, 2).byteValue();
			byte b2 = Integer.valueOf(s3, 2).byteValue();
			byte[] bf = new byte[3];
			bf[0] = b0;
			fullByte[i * 3] = bf[0];
			bf[1] = b1;
			fullByte[i * 3 + 1] = bf[1];
			bf[2] = b2;
			fullByte[i * 3 + 2] = bf[2];
		}
		return fullByte;
	}

	/* 字符串格式转换myencode1--->myencode2 */
	public static String TransferEncode(String str,String myencode1,String myencode2) {
		String ret;
		if	(str == null) {
			return "";
		}
		try {
			ret = new String(str.trim().getBytes(myencode1),myencode2);
		} catch (Exception e) {
			return "";
		}
		return ret;
	}

	/* 字符串格式转换ISO8859_1 --> GBK */
	public static String ISO2GBK(String str) {
		String ret;
		if	(str == null) {
			return "";
		}
		try {
			ret = new String(str.trim().getBytes("ISO8859_1"), "GBK");
		} catch (Exception e) {
			return "";
		}
		return ret;
	}

	/* 字符串格式转换ISO8859_1 --> UTF8 */
	public static String ISO2UTF8(String str) {
		String ret;
		if	(str == null) {
			return "";
		}
		try {
			ret = new String(str.trim().getBytes("ISO8859_1"), "UTF-8");
		} catch (Exception e) {
			return "";
		}
		return ret;
	}

	/* 格式转换,数字对齐,左补0 如长度为6时,234 ---> 000234 */
	public static String LFZero(int len, long i) {
		String str = "" + Math.abs(i);
		if	(i>=0)		return LFZero(len,str);
				else			return "-"+LFZero(len-1,str);
		//String.format("%06d",i);
	}
	public static String LFZero(int len, int i) {
		return LFZero(len,(long)i);
	}

	/* 格式转换,数字对齐,左补0 如长度为6时,234 ---> 000234 */
	public static String LFZero(int len,String str) {
		if	(str==null)	str="";
		if	(str.length() >= len) {
			if	(str.charAt(0)!='-')	return str.substring(str.length()-len,str.length());
				else	return "-"+str.substring(str.length()-len+1,str.length());
		}
		String str0 = "";
		for (int k = 0; k < len - str.length(); k++) {
			str0 = str0 + "0";
		}
		String str1 = "";
		str1 = str0 + str;
		return str1;
	}

	/* for ccas 时间格式转换 94957-->094957 */
	public static String strTime(int i) {
		return LFZero(6, i);
	}

	/* 取字符串最后几位*/
	public static String strRight(String str, int lastNum) {
		int len;
		String toStr;
		if	(str == null) {
			return null;
		}
		toStr = str.trim();
		len = toStr.length();
		if	(len < lastNum) {
			return null;
		}
		toStr = toStr.substring(len - lastNum, len);
		return toStr;
	}

	/* 字符串金额转换为不带小数的字符串 123.1----000000012310*/
	public static String strAmount2Str(String strAmount) {
		String strRet;
		String strZero = "000000000000";
		if	(strAmount == null)		strAmount="0";
		int locb, loce;
		locb = strAmount.indexOf('.');
		loce = strAmount.lastIndexOf('.');
		if	(locb != loce) /*字符串金额含两个以上小数，非法 */
		{
			return null;
		}

		if	(locb > 0) {
			strAmount = strAmount.trim() + "00";
			strAmount = strAmount.substring(0, locb) +
						strAmount.substring(locb + 1, locb + 3);
		} else {
			strAmount = strAmount + "00";
		}
		int len = strAmount.length();

		strRet = strZero.substring(0, 12 - len) + strAmount;

		return strRet;
	}

	/* 是否为带小数的金额字串 123.9：是 123否，123.12.2否,122ds.22否*/
	public static boolean isDotAmountStr(String DotStrAmt) {
		int locb, loce;
		if	(DotStrAmt == null) {
			return false;
		}

		locb = DotStrAmt.indexOf('.');
		if	(locb < 0) {
			return false;
		}
		loce = DotStrAmt.lastIndexOf('.');
		if	(locb != loce) /*字符串金额含两个以上小数，非法 */
				{
			return false;
		}
		String tStr = DotStrAmt.replace('.', '0');
		return isNumberstr(tStr);
	}

	/* 不带小数的字符串转换为金额 000000012310----123.1*/
	public static double strAmount2D(String strAmount) {
		double damount = 0.0;
		if	(strAmount == null)		strAmount="0";
		if	(strAmount!=null && strAmount.length()>0)
		{
			damount = Double.parseDouble(strAmount.trim());
			damount = damount / 100.0;
		}
		return damount;
	}

	/* 不带小数的字符串转换为带小数的字符串 000000012310----123.10*/
	public static String strAmount2S(String strAmount) {
		if	(strAmount == null || strAmount.length() == 0) {
			strAmount = "0000";
		}
		long   longAmount=Long.parseLong(strAmount)/100;
		String symbol="";
		if	(longAmount < 0)		symbol="-";
		String strRet = "0000"+strAmount;
		return symbol+Long.toString(longAmount) + "." + strRet.substring(strRet.length()-2);


/*
		String strRet = LFZero(3, Integer.parseInt(strAmount.trim()));
		String symbol="";
		if	(Integer.parseInt(strAmount.trim()) < 0) {
			strRet = LFZero(3, -Integer.parseInt(strAmount.trim()));
			symbol="-";
			//return "-" + strRet.substring(0, strRet.length() - 2) + "." + strRet.substring(strRet.length() - 2, strRet.length());
		}
		return symbol+strRet.substring(0, strRet.length() - 2) + "." + strRet.substring(strRet.length() - 2, strRet.length());
*/
	}

	/* 金额转换为带小数的字符串 123.1----123.10*/
	public static String DAmount2S(double dAmount) {
		return strAmount2S(DAmount2Str(dAmount));
	}

	/* 金额转换为带小数的字符串 1234567890.12----1,234,567,890.12*/
	public static String transAmountFormat(double dAmount) {
		//return transAmountFormat(dAmount,2);
		return String.format("%1$,.2f",dAmount);
	}
	public static String transAmountFormat(double dAmount,String language) {
		if	(language!=null && language.length()==0 && currentLocale.getLanguage().toLowerCase().indexOf("vi")>=0)		return transAmountFormat(dAmount,0);
		if	(language!=null && language.toLowerCase().indexOf("704")>=0)	return transAmountFormat(dAmount,0);
		if	(language!=null && language.toLowerCase().indexOf("vnd")>=0)	return transAmountFormat(dAmount,0);
		if	(language!=null && language.toLowerCase().indexOf("vi")>=0)		return transAmountFormat(dAmount,0);
		return transAmountFormat(dAmount,2);
	}


	/* 金额转换为带小数的字符串 1234567890.12----1,234,567,890.12*/
	public static String transAmountFormat(double dAmount,int len) {
		return String.format("%1$,."+len+"f",dAmount);
		//return String.format("%1$,.%2$f",dAmount,len);
/*
		String str1=strAmount2S(DAmount2Str(Math.abs(dAmount)));
		String ret1=str1.substring(0,str1.length()-3);
		String ret2=str1.substring(str1.length()-3);
		str1=ret1;
		ret1="";
		for (int i=str1.length()%3; i<=str1.length(); i+=3) {
			if	(ret1.length()>0)			ret1+=",";
			if	(i>0)									ret1+=str1.substring((i-3)<0?0:i-3,i);
		}
		if	(round(dAmount,2) <= -0.005) ret1="-"+ret1;

		if	(len!=2)	return ret1;
		return ret1+ret2;
*/
	}


	/* 金额转换为带小数的字符串 1234567890.12----1,234,567,890.12*/
	public static String transAmountFormat(String StrAmount) {
		return transAmountFormat(strAmount2D(StrAmount));
	}

	public static String DAmount2Str(double dAmount) {
		if	(round(dAmount,2) <= -0.005) {
			//dAmount = -dAmount;
			return "-" + DAmount2StrPositive(dAmount).substring(1);
		}
		return DAmount2StrPositive(dAmount);
	}

	/* 金额转换为不带小数的字符串123.1----000000012310*/
	private static String DAmount2StrPositive(double dAmount) {
		String strAmount;
		String strRet;
		String strZero = "000000000000";
		dAmount=Math.abs(dAmount);

/*
		strAmount = Double.toString(round(dAmount,2));
		int loc = strAmount.indexOf('.');
		if	(loc > 0) {
			strAmount = strAmount + "00";
			strAmount = strAmount.substring(0, loc) +
						strAmount.substring(loc + 1, loc + 3);
		} else {
			strAmount = strAmount + "00";
		}
		int len = strAmount.length();

		strRet = strZero.substring(0, 12 - len) + strAmount;
*/
		BigDecimal b = new BigDecimal(Double.toString(100*round(dAmount,2)));
		strRet=b.toBigInteger().toString();
		return strRet;
	}

	/*日期转换为sql识别的datetime 20050322121212 --- 20050322 12:12:12 */
	public static String strdate2SQLtdate(String strdate) {
		if	(strdate==null || strdate.length() != 14) {
			return null;
		}
		String sqldate = strdate.substring(0, 8) + " " +
						 strdate.substring(8, 10) +
						 ":" + strdate.substring(10, 12) + ":" +
						 strdate.substring(12, 14);
		return sqldate;
	}

	/*------------------------------*/
	/***  测试日期是否合法  ***/
	public static boolean isDate(String strdate) {
		int year, mon, day;
		int hh = 0, mm = 0, ss = 0;
		int len;
		if	(strdate == null) {
			return false;
		}
		len = strdate.length();
		if	((len != 8 && len != 14) || (isNumberstr(strdate) == false)) {
			return false;
		}
		year = Integer.parseInt(strdate.substring(0, 4));
		mon = Integer.parseInt(strdate.substring(4, 6));
		day = Integer.parseInt(strdate.substring(6, 8));
		if	(len == 14) {
			hh = Integer.parseInt(strdate.substring(8, 10));
			mm = Integer.parseInt(strdate.substring(10, 12));
			ss = Integer.parseInt(strdate.substring(12, 14));
		}

		if	(mon >= 1 && mon <= 12 && day >= 1 && day <= dmaxDaysofMon(year, mon) &&
			hh < 24 && mm < 60 && ss < 60) {
			return (true);
		}

		return (false);
	}

	/*----------------------------*/
	/***  以数字测试字串  ***/
	public static boolean isNumberstr(String str) {
		if	(str == null)		return (false);
		int len = str.length();
		if	(len == 0) {
			return (false);
		}
		try {
			if	(len != str.getBytes(myencode).length) {
				return (false);
			}
		} catch (Exception e) {
			return (false);
		}

		for (int i = 0; i < len; i++) {
			if	((str.charAt(i) > '9') || (str.charAt(i) < '0')) {
				return (false);
			}
		}
		return (true);
	}

	/*-----------------------------*/
	/*** 找出某月的最大天数  ***/
	static int month_maxdayset[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31,30, 31};
	public static int dmaxDaysofMon(String yearmonth) {
		String year="0";
		String month="1";
		if	(yearmonth!=null && yearmonth.length()>=6){
			year=yearmonth.substring(0,4);
			month=yearmonth.substring(4,6);
		}

		return	dmaxDaysofMon(Integer.parseInt(year),Integer.parseInt(month));
	}

	public static int dmaxDaysofMon(String year, String month) {
		if	(year == null)		year="0";
		if	(month== null)		month="1";
		return	dmaxDaysofMon(Integer.parseInt(year),Integer.parseInt(month));
	}
	public static int dmaxDaysofMon(int year, String month) {
		if	(month== null)		month="1";
		return	dmaxDaysofMon(year,Integer.parseInt(month));
	}
	public static int dmaxDaysofMon(String year, int month) {
		if	(year == null)		year="0";
		return	dmaxDaysofMon(Integer.parseInt(year),month);
	}
	public static int dmaxDaysofMon(int year, int mon) {
		int days;

		days = month_maxdayset[mon];
		if	(mon == 2) {
			days += (maxDaysofYear(year) - 365);
		}
		return (days);
	}

	/*-------------------------------*/
	/*** 找出某年的最大天数  ***/
	public static int maxDaysofYear(String year){
		if	(year == null)		year="0";
		return maxDaysofYear(Integer.parseInt(year));
	}
	public static int maxDaysofYear(int year) {
		int days;

		if	((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) {
			days = 366;
		} else {
			days = 365;
		}
		return (days);
	}

	/*取8位当前日期 YYYYMMDD*/
	public static String getCurDate8Str() {
		String curtime = "";
		Date currTime = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd", Locale.US);
		curtime = sf.format(currTime);

		return curtime;
	}
	/*取当前日期*/
	public static String getCurDateStr() {
		return getCurDateStr(8);
	}

	/*取当前日期*/
	public static String getCurDateStr(int len) {
		//String dateFormat1 = "yyyyMMdd";
		String dateFormat2 = "";
		String retdate = getCurDate8Str();

		if	(len==21)			dateFormat2 = "yyyy/MM/dd HH:mm:ss.S";
		if	(len==19)			dateFormat2 = "yyyy/MM/dd HH:mm:ss";
		if	(len==11)			dateFormat2 = "yyyyMMdd-HH";
		if	(len==10)			dateFormat2 = "yyyy/MM/dd";
		if	(len==8)			dateFormat2 = "yyyyMMdd";
		if	(len==7)			dateFormat2 = "yyyy/MM";
		if	(len==6)			dateFormat2 = "yyyyMM";
		if	(len==4)			dateFormat2 = "yyyy";
		if	(len==2)			dateFormat2 = "yy";
		if	(dateFormat2.length()<=0)		return	retdate;

		//SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat1);
		SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormat2);
		try {
			retdate=sdf2.format(new Date());
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",Exception=" + e.toString());
			//e.printStackTrace();
		}
		return retdate;
	}

	/*取14位当前时间 YYYYMMDDHHmmss*/
	public static String getCurTime14Str() {
		String curtime = "";
		Date currTime = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
		curtime = sf.format(currTime);

		return curtime;
	}

	/*取6位当前时间 HHmmss*/
	public static String getCurTime6Str() {
		String curtime = "";
		Date currTime = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("HHmmss", Locale.US);
		curtime = sf.format(currTime);

		return curtime;
	}

	/*取当前时间*/
	public static String getCurTimeStr() {
		return getCurTimeStr(6);
	}

	/*取当前时间*/
	public static String getCurTimeStr(int len) {
		String curtime=getCurTime6Str();
		String dateFormat2 = "";
		if	(len==9)			dateFormat2 = "HHmmssS";
		if	(len==8)			dateFormat2 = "HH:mm:ss";
		if	(len==7)			dateFormat2 = "mmssS";
		if	(len==6)			dateFormat2 = "HHmmss";
		if	(len==5)			dateFormat2 = "ssS";
		if	(len==4)			dateFormat2 = "HHmm";
		if	(len==3)			dateFormat2 = "S";
		if	(len==2)			dateFormat2 = "HH";
		if	(dateFormat2.length()<=0)		return	curtime;


		Date currTime = new Date();
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat2, Locale.US);
		curtime = sf.format(currTime);

		return curtime;
	}

	/*取当前时间 YYYY年MM月DD日 HH:mm:ss*/
	public static String getCurTimeHZStr() {
		String curtime = "";
		Date currTime = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss",
				Locale.US);
		curtime = sf.format(currTime);

		return curtime;
	}

	/*时间格式转换 YYYYMMDDHHmmss --> YYYY年MM月DD日 HH:mm:ss*/
	public static String convert14CurTime2HZStr(String t) {
		if	(t == null || t.trim().length() != 14) {
			return "";
		}
		String curtime = "";
		curtime = t.trim().substring(0, 4) + "年" +
					trimLeftZero(t.trim().substring(4, 6)) + "月" +
					trimLeftZero(t.trim().substring(6, 8)) + "日";
		curtime = curtime + " " + t.trim().substring(8, 10) + ":" +
					t.trim().substring(10, 12) + ":" + t.trim().substring(12, 14);
		return curtime;
	}

	/* 去除左边0 如 000000123-->123 */
	public static String trimLeftZero(String str) {
		int len;
		String toStr;
		if	(str == null) {
			return null;
		}
		toStr = str.trim();
		len = toStr.length();
		if	(len == 0) {
			return null;
		}
		int i = 0;
		for (i = 0; i < len; i++) {
			if	(toStr.charAt(i) != '0') {
				break;
			}
		}
		toStr = toStr.substring(i, len);
		return toStr;
	}

	/* */
	public static String getFillStr(String Str,String FillStr) {
		String retStr="";
		if	(Str==null || FillStr==null) return retStr;
		for	(int i=0;i<Str.length();i++){
			retStr+=FillStr;
		}
		return retStr;
	}
	/* 左对齐，右补空格，总长为StrLen */
	public static String getFillBlankStrLen(String Str, int StrLen) {
		if	(Str == null) Str="";

		byte bStr[] = null;
		try {
			bStr = Str.getBytes(myencode);
		} catch (Exception e) {
			return null;
		}
		if	(bStr.length >= StrLen) {
			return subStringGBK(Str,0,StrLen);
		}
		int len = StrLen - bStr.length;
		for (int i = 0; i < len; i++) {
			Str = Str + new String(" ");
		}
		return Str;
	}

	/* 右对齐，左补FillStr，总长为StrLen */
	public static String getFillLeftStrLen(String Str,String FillStr,int StrLen) {
		if	(Str == null || FillStr == null || FillStr.length()<=0) {
			return Str;
		}
		byte bStr[] = null;
		try {
			bStr = Str.getBytes(myencode);
		} catch (Exception e) {
			return null;
		}
		if	(bStr.length >= StrLen) {
			return Str;
		}
		int len = (StrLen - bStr.length)/FillStr.length();
		for (int i = 0; i < len; i++) {
			Str = FillStr + Str;
		}
		return Str;
	}


	/* 右对齐，左补空格，总长为StrLen */
	public static String getFillLeftBlankStrLen(String Str, int StrLen) {
		return getFillLeftStrLen(Str," ",StrLen);
	}


	/*得到汉字串的真正的长度*/
	public static int getStringLength(String Str) {
		if	(Str == null)		return 0;
		byte bStr[] = null;
		try {
			bStr = Str.getBytes(myencode);
		} catch (Exception e) {
			return 0;
		}
		return bStr.length;
	}

	/* 可用于对含汉字字符串的截取 subStringGBK("长城",0,2) return "长"*/
	public static String subStringGBK(String Str, int start, int end) {
		if	(Str == null) {
			//return null;
			return "";
		}
		byte bStr[] = null;
		try {
			bStr = Str.getBytes(myencode);
			//bStr = Str.getBytes("ISO8859_1");
		} catch (Exception e) {
			//return null;
			return "";
		}
		if	(bStr.length <= start || start >= end) {
			//CLog.writeLog("len="+bStr.length+",start="+start+",end="+end);
			return "";
		}
		if	(end > bStr.length) {
			end = bStr.length;
		}
		int len = end - start;
		if	(len<=0)		return "";
		//if	(getStringLength(Str)==Str.length())		return Str.substring(start,end);
		byte[] bt = new byte[len];
		System.arraycopy(bStr, start, bt, 0, len);
		try {
			Str = new String(bt, myencode);
		} catch (Exception e) {
			//return null;
			return "";
		}

		return Str;
	}

	public static String getDayInterval(String day1, String time1,String day2,String time2) throws ParseException {
		return getDayInterval(day1,Integer.parseInt(time1),day2,Integer.parseInt(time2));
	}
	/*用于返回两个时间之间的间隔*/
	/*输入：String day1  格式 yyyyMMdd 表示年月日 */
	/*输入：int 	time1 格式 HHmmss 表示时分s不足6位处理时自动补零 */
	/*输入：String day2  格式 yyyyMMdd 表示年月日 */
	/*输入：int 	time2 格式 HHmmss 表示时分s不足6位处理时自动补零 */
	/*输出 String "dd天HH时mm分sss"*/
	public static String getDayInterval(String day1,int time1,String day2,int time2) throws ParseException {
		final String defDateFormat = "yyyyMMdd HHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(defDateFormat); ;
		GregorianCalendar gCal1 = new GregorianCalendar();
		gCal1.setTimeZone(TimeZone.getDefault());
		GregorianCalendar gCal2 = new GregorianCalendar();
		gCal2.setTimeZone(TimeZone.getDefault());
		String day = LFZero(2, gCal1.get(Calendar.YEAR)) +
					 LFZero(2, (gCal1.get(Calendar.MONTH) + 1)) +
					 LFZero(2, gCal1.get(Calendar.DAY_OF_MONTH));
		int time = gCal1.get(Calendar.HOUR_OF_DAY) * 10000 +
					 gCal1.get(Calendar.MINUTE) * 100 + gCal1.get(Calendar.SECOND);
		if	(day1 == null || day1.length() == 0 || day1.equals("0")) {
			day1 = day;
		}
		if	(day2 == null || day2.length() == 0 || day2.equals("0")) {
			day2 = day;
		}
		if	(time1 < 0) {
			time1 = time;
		}
		if	(time2 < 0) {
			time2 = time;
		}
		String firstDay = day1 + " " + strTime(time1);
		String secondDay = day2 + " " + strTime(time2);
		gCal1.setTimeInMillis(sdf.parse(firstDay).getTime());
		gCal2.setTimeInMillis(sdf.parse(secondDay).getTime());
		long ldate = Math.abs(gCal1.getTimeInMillis() - gCal2.getTimeInMillis());
		long dd = ldate / (1000 * 60 * 60 * 24);
		long hh = (ldate % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long mm = (ldate % (1000 * 60 * 60)) / (1000 * 60);
		long ss = (ldate % (1000 * 60)) / 1000;
		String dayInterval;
		if	(dd > 0) {
			dayInterval = dd + "天" + hh + "小时" + mm + "分钟" + ss + "s";
		} else if	(hh > 0) {
			dayInterval = hh + "小时" + mm + "分钟" + ss + "s";
		} else if	(mm > 0) {
			dayInterval = mm + "分钟" + ss + "s";
		} else if	(ss > 0) {
			dayInterval = ss + "s";
		} else {
			//dayInterval="";
			dayInterval = ss + "s";
		}
		return dayInterval;
	}

	public static int getSecondInterval(String day1, String time1,String day2,String time2) throws ParseException {
		return getSecondInterval(day1,Integer.parseInt(time1),day2,Integer.parseInt(time2));
	}
	/*用于返回两个时间之间的间隔*/
	/*输入：String day1  格式 yyyyMMdd 表示年月日 */
	/*输入：int 	time1 格式 HHmmss 表示时分s不足6位处理时自动补零 */
	/*输入：String day2  格式 yyyyMMdd 表示年月日 */
	/*输入：int 	time2 格式 HHmmss 表示时分s不足6位处理时自动补零 */
	/*输出 int 秒*/
	public static int getSecondInterval(String day1,int time1,String day2,int time2) throws ParseException {
		final String defDateFormat = "yyyyMMdd HHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(defDateFormat); ;
		GregorianCalendar gCal1 = new GregorianCalendar();
		gCal1.setTimeZone(TimeZone.getDefault());
		GregorianCalendar gCal2 = new GregorianCalendar();
		gCal2.setTimeZone(TimeZone.getDefault());
		String day = LFZero(2, gCal1.get(Calendar.YEAR)) +
					 LFZero(2, (gCal1.get(Calendar.MONTH) + 1)) +
					 LFZero(2, gCal1.get(Calendar.DAY_OF_MONTH));
		int time = gCal1.get(Calendar.HOUR_OF_DAY) * 10000 +
					 gCal1.get(Calendar.MINUTE) * 100 + gCal1.get(Calendar.SECOND);
		if	(day1 == null || day1.length() == 0 || day1.equals("0")) {
			day1 = day;
		}
		if	(day2 == null || day2.length() == 0 || day2.equals("0")) {
			day2 = day;
		}
		if	(time1 < 0) {
			time1 = time;
		}
		if	(time2 < 0) {
			time2 = time;
		}
		String firstDay = day1 + " " + strTime(time1);
		String secondDay = day2 + " " + strTime(time2);
		gCal1.setTimeInMillis(sdf.parse(firstDay).getTime());
		gCal2.setTimeInMillis(sdf.parse(secondDay).getTime());
		long ldate = Math.abs(gCal1.getTimeInMillis() - gCal2.getTimeInMillis());
		return (int)ldate/1000;
	}


	/*
	 * public static String  getAddDate(String date,int addDate) throws Exception
	 * 用于计算指定日期的增加（减少）
	 * 输入String date 指定日期（格式yyyyMMdd）
	 * 输入int	addDate 三位的整型（首位1表日，2表月，3表年 正数表增加，负数表减少）
	 * 输出String 增加后的日期（格式yyyyMMdd）
	 */
	public static String getAddDate(String date, int addDate) throws Exception {
		String dateFormat = "yyyy/MM/dd";
		if	(date.length()==8)			dateFormat = "yyyyMMdd";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		GregorianCalendar gCal = new GregorianCalendar();
		gCal.setTimeZone(TimeZone.getDefault());
		long time = sdf.parse(date).getTime();
		gCal.setTimeInMillis(time);
		int type = Math.abs(addDate / typeNum);
		int dateAdd = addDate % typeNum;

		if	(type == dayType) {
			gCal.add(Calendar.DAY_OF_MONTH, dateAdd);
		} else if	(type == monthType) {
			gCal.add(Calendar.MONTH, dateAdd);
		} else if	(type == yearType) {
			gCal.add(Calendar.YEAR, dateAdd);
		} else {
			return date;
		}

		return sdf.format(gCal.getTime());

	}

	public static java.sql.Date String2Date(String date){
		String dateFormat = "yyyy/MM/dd";
		if	(date==null || date.length()==0)			date="1899-12-31";

		if	(date.length()==21)			dateFormat = "yyyy/MM/dd HH:mm:ss.S";
		if	(date.length()==10)			dateFormat = "yyyy/MM/dd";
		if	(date.length()==8)			dateFormat = "yyyyMMdd";
		if	(date.length()==7)			dateFormat = "yyyy/MM";
		if	(date.length()==6)			dateFormat = "yyyyMM";
		if	(date.length()==4)			dateFormat = "yyyy";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			//return sdf.parse(date);
			return new java.sql.Date(sdf.parse(date).getTime());
		} catch (Exception e) {
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",Exception=" + e.toString());
			//e.printStackTrace();
			return null;
		}
	}

	public static String transDateFormat(String date){
		return transDateFormat(date,8);
	}
	public static String transDateFormat(int date,int len){
		return transDateFormat(""+date,8);
	}

	public static String transDateFormat(String date,int len){
		String dateFormat1 = "yyyy/MM/dd";
		String dateFormat2 = "yyyyMMdd";
		if	(date==null || date.length()<=1)
		{
			//date = getCurDate8Str();
			return date;
		}
		String retdate = date;
		if	(date.trim().length()==21)			dateFormat1 = "yyyy/MM/dd HH:mm:ss.S";
		if	(date.trim().length()==14)			dateFormat1 = "yyyyMMddHHmmss";
		if	(date.trim().length()==11)			dateFormat1 = "yyyyMMdd-HH";
		if	(date.trim().length()==10)			dateFormat1 = "yyyy/MM/dd";
		if	(date.trim().length()==8)				dateFormat1 = "yyyyMMdd";
		if	(date.trim().length()==7)				dateFormat1 = "yyyy/MM";
		if	(date.trim().length()==6)				dateFormat1 = "yyyyMM";
		if	(date.trim().length()==4)				dateFormat1 = "yyyy";

		if	(len==19)			dateFormat2 = "yyyy/MM/dd HH:mm:ss";
		if	(len==11)			dateFormat2 = "yyyyMMdd-HH";
		if	(len==10)			dateFormat2 = "yyyy/MM/dd";
		if	(len==8)			dateFormat2 = "yyyyMMdd";
		if	(len==7)			dateFormat2 = "yyyy/MM";
		if	(len==6)			dateFormat2 = "yyyyMM";
		if	(len==4)			dateFormat2 = "yyyy";
		if	(len>10 || len<4 || len==5)		return	date;

		return transDateFormat(date,dateFormat1,dateFormat2);
	}


	public static String transDateFormat(String date,String dateFormat1,String dateFormat2){
		if	(date==null || date.length()<=1)				return date;
		String retdate = date;
		SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat1);
		SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormat2);
		try {
			retdate=sdf2.format(sdf1.parse(date).getTime());
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",Exception=" + e.toString());
			return retdate;
			//e.printStackTrace();
		}
		return retdate;
	}

	  /**
	 * create by Lcwen 2010.08.10
	 * @param comm
	 * @param format
	 * @return  
	 */
	public static String transDateFormatFromComm(String comm, String format){
		String retDateTime = comm;
		try{
			if(comm != null || !comm.equals("")){
				String strYYYY = "";
				String strDD = "";
				String strMM = "";
				int len = 0;
				if((len = comm.indexOf("/")) != -1){
					strYYYY = comm.substring(0, len);
					comm = comm.substring(len + 1);
					if(comm.indexOf("/") != -1){
						strMM = comm.substring(0, comm.indexOf("/"));
						comm = comm.substring(comm.indexOf("/") + 1);
						if(comm.indexOf(" ") != -1){
							strDD = comm.substring(0, comm.indexOf(" "));
							Date dateTemp = new Date(Integer.parseInt(strYYYY) - 1900, Integer.parseInt(strMM) - 1, Integer.parseInt(strDD));
							SimpleDateFormat sdf = new SimpleDateFormat(format);
							String dateTimeTemp = sdf.format(dateTemp);
							retDateTime = dateTimeTemp + comm.substring(comm.indexOf(" "));
						}else{
							strDD = comm;
							retDateTime = strYYYY + "/" +strMM + "/" + strDD;
						}
					}else{ //如果输入的内容，并不是时间或者时间格式，则按照原来的内容输出
						retDateTime = strYYYY + "/" + comm;
					}
				}else{
					if((len = comm.indexOf("-")) != -1){
						strYYYY = comm.substring(0, len);
						comm = comm.substring(len + 1);
						if(comm.indexOf("-") != -1){
							strMM = comm.substring(0, comm.indexOf("-"));
							comm = comm.substring(comm.indexOf("-") + 1);
							if(comm.indexOf(" ") != -1){
								strDD = comm.substring(0, comm.indexOf(" "));
								Date dateTemp = new Date(Integer.parseInt(strYYYY) - 1900, Integer.parseInt(strMM) - 1, Integer.parseInt(strDD));
								SimpleDateFormat sdf = new SimpleDateFormat(format);
								String dateTimeTemp = sdf.format(dateTemp);
								retDateTime = dateTimeTemp + comm.substring(comm.indexOf(" "));
							}else{
								strDD = comm;
								retDateTime = strYYYY + "-" +strMM + "-" + strDD;
							}
						}else{ //如果输入的内容，并不是时间或者时间格式，则按照原来的内容输出
							retDateTime = strYYYY + "-" + comm;
						}
					}
				}
			}
		}catch(Exception e){
			retDateTime = "error";
		}
		return retDateTime;
	}


	public static String getDateHzStr(String date) throws Exception {
		if	(date == null || date.length() == 0) {
			date = getCurDate8Str();
		}
		if	(date.length() <= 5 && date.length()>0) {
			date = "20"+LFZero(6,Integer.parseInt(date));
		}
		String dateFormat = "yyyy/MM/dd";
		if	(date.length()==8)			dateFormat = "yyyyMMdd";
		if	(date.length()==7)			dateFormat = "yyyy/MM";
		if	(date.length()==6)			dateFormat = "yyyyMM";
		if	(date.length()==4)			dateFormat = "yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		GregorianCalendar gCal = new GregorianCalendar();
		gCal.setTimeZone(TimeZone.getDefault());
		long time = sdf.parse(date).getTime();
		gCal.setTimeInMillis(time);
		int year = gCal.get(Calendar.YEAR);
		int month = gCal.get(Calendar.MONTH) + 1;
		int day = gCal.get(Calendar.DAY_OF_MONTH);

		if	(date.length()!=7 && date.length()!=6)
					return year + "年" + month + "月" + day + "日";
				else
					return year + "年" + month + "月";
	}

	public static String getTimeHzStr(int time)throws Exception {
		return getTimeHzStr(LFZero(6,time));
	}

	public static String getTimeHzStr(String time) throws Exception {
		if	(time==null || time.length()==0) return "";
		if	(time.length() <= 5 && time.length()>0) {
			time = LFZero(6,Integer.parseInt(time));
		}

		return time.substring(0,2)+":"+time.substring(2,4)+":"+time.substring(4,6);
	}

	public static int getDiffStrDate(String date1, String date2){	//throws Exception
		if	(date1==null || date2==null || date1.length()<8 || date2.length()<8)		return -1;
		try {
			java.util.Date td1 = new Date();
			java.util.Date td2 = new Date();
			if	(date1!=null && date1.length()==8 )		td1 = new SimpleDateFormat("yyyyMMdd").parse(date1);
			if	(date1!=null && date1.length()==10)		td1 = new SimpleDateFormat("yyyy/MM/dd").parse(date1);

			if	(date2!=null && date2.length()==8 )		td2 = new SimpleDateFormat("yyyyMMdd").parse(date2);
			if	(date2!=null && date2.length()==10)		td2 = new SimpleDateFormat("yyyy/MM/dd").parse(date2);
			int iDay = (int) ((td1.getTime() - td2.getTime()) / 86400000);
			return iDay;
		} catch (ParseException e) {
			//e.printStackTrace();
			//throw e;
		}
		return 0;
	}

	/* 字符串格式转换 -->UTF-8  */
	public static String toUTF8(String in) {
		char temChr;
		int i;
		String rtStr = new String("");
		if	(in == null) {
			in = "";
		}
		for (i = 0; i < in.length(); i++) {
			temChr = in.charAt(i);
			rtStr = rtStr + "&#x" + Integer.toHexString(temChr) + ";";
		}
		return rtStr;
	}

	public static double round(double v) {
		return round(v,2);
	}
	/* 提供精确的小数位四舍五入处理 如：round(4.015,2) --> 4.12 */
	public static double round(double v, int scale) {

		if	(scale < 0) {

			throw new IllegalArgumentException(

					"The scale must be a positive integer or zero");

		}

		return	Math.round(v*Math.pow(10,scale))/Math.pow(10,scale);
/*
		BigDecimal b = new BigDecimal(Double.toString(v));

		BigDecimal one = new BigDecimal("1");

		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
*/
	}



	public static String getLastPort() {
		String lastPort="0";
		try	{
			CommPortIdentifier portID_ = null;
			Enumeration ports = CommPortIdentifier.getPortIdentifiers();

			while (ports.hasMoreElements()) {
				portID_ = (CommPortIdentifier) ports.nextElement();
				switch (portID_.getPortType()) {
				case CommPortIdentifier.PORT_SERIAL:
					lastPort=portID_.getName();
					break;
				default:
					break;
				}
			}

			portID_ = null;
			ports=null;
		}	catch(Exception e)	{
		}

		return lastPort.charAt(lastPort.length()-1)+"";
	}

	public static String getPortInfo() {
		String portInfo="";
		try	{
			CommPortIdentifier portID_ = null;
			java.util.List port1Vector = new Vector(32);
			java.util.List port2Vector = new Vector(32);
			Enumeration ports = CommPortIdentifier.getPortIdentifiers();

			while (ports.hasMoreElements()) {
				portID_ = (CommPortIdentifier) ports.nextElement();
				//portInfo+="\nCommPort : "+portID_.getName()+",type:"+portID_.getPortType()+",CurrentOwner:"+portID_.getCurrentOwner();
				switch (portID_.getPortType()) {
				case CommPortIdentifier.PORT_SERIAL:
					port1Vector.add(portID_.getName());
					break;
				case CommPortIdentifier.PORT_PARALLEL:
					port2Vector.add(portID_.getName());
					break;
				default:
					break;
				}
			}
			portInfo+="\nPORT_SERIAL   = " + port1Vector;
			portInfo+="\nPORT_PARALLEL = " + port2Vector;

			portID_ = null;
			ports=null;
		}	catch(Exception e)	{
		}

		return portInfo;
	}

	public static int getPortNum() {
		int portNum=0;
		CommPortIdentifier portID_ = null;
		java.util.List port1Vector = new Vector(32);
		java.util.List port2Vector = new Vector(32);
		Enumeration ports = CommPortIdentifier.getPortIdentifiers();

		while (ports.hasMoreElements()) {
			portID_ = (CommPortIdentifier) ports.nextElement();
			//portInfo+="\nCommPort : "+portID_.getName()+",type:"+portID_.getPortType()+",CurrentOwner:"+portID_.getCurrentOwner();
			switch (portID_.getPortType()) {
			case CommPortIdentifier.PORT_SERIAL:
				portNum++;
				port1Vector.add(portID_.getName());
				break;
			case CommPortIdentifier.PORT_PARALLEL:
				port2Vector.add(portID_.getName());
				break;
			default:
				break;
			}
		}
		portID_ = null;
		ports=null;

		return portNum;
	}

	public static String getJVMInfo() {
		String JVMInfo="";
		java.lang.Runtime rt =java.lang.Runtime.getRuntime();
		JVMInfo+="\nJVM Total Memory = " + rt.totalMemory()/(1024*1024)+" M";
		JVMInfo+="\nJVM Free  Memory = " + rt.freeMemory() /(1024*1024)+" M";
		rt = null;

		return JVMInfo;
	}

	public static String getAllLocalIP() { //throws Exception
		String LocalIP="";
		try {

/*
			Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();

			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
				InetAddress ip = (InetAddress) ni.getInetAddresses().nextElement();
				if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":")==-1) {
					LocalIP+=ip.getHostName()+":"+ip.getHostAddress()+"-\n";
				} else {
					LocalIP+=ip.getHostName()+":"+ip.getHostAddress()+"--\n";
				}
			}
*/

			Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements()){
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				//System.out.println(netInterface.getName());
				Enumeration addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()){
					ip = (InetAddress) addresses.nextElement();
					if	(LocalIP.length()>10)	LocalIP+="\n";
					if (ip != null && ip instanceof Inet4Address)	{
						//System.out.println("本机的IP = " + ip.getHostAddress());
						if	(!ip.getHostAddress().equals("127.0.0.1"))	LocalIP+=netInterface.getName()+"/"+netInterface.getDisplayName()+"\n"+(ip.getHostName().equals(ip.getHostAddress())?"":(ip.getHostName()+":"))+ip.getHostAddress();
					}
				}
			}

			//LocalIP+=InetAddress.getLocalHost().getHostName()+":"+InetAddress.getLocalHost().getHostAddress();
			ip = null;
			allNetInterfaces = null;
		}	catch (Exception ex) {
		}	finally	{
		}
		return LocalIP;
	}
	public static String grep(String pattern, String inStr) throws IOException {
		if	(pattern==null || pattern.length()<=0)	return inStr;
		StringBuffer		sb=new	StringBuffer();
		StringTokenizer tokens0=new StringTokenizer(inStr.trim(),"\n");
		String line = "";
		pattern=pattern.toLowerCase();
		for	(;tokens0.hasMoreTokens();){
			line=tokens0.nextToken().toLowerCase();
			if (line.indexOf(pattern) >= 0)
				sb.append(line).append("\n");
		}
		return sb.toString();
	}
	
	public static String grep(String pattern, StringBuffer inStr) throws IOException {
		if	(pattern==null || pattern.length()<=0)	return inStr.toString();
		StringBuffer		sb=new	StringBuffer();
		StringTokenizer tokens0=new StringTokenizer(inStr.toString().trim(),"\n");
		String line = "";
		pattern=pattern.toLowerCase();
		for	(;tokens0.hasMoreTokens();){
			line=tokens0.nextToken().toLowerCase();
System.out.println(line);			
			if (line.indexOf(pattern) >= 0)
				sb.append(line).append("\n");
		}
		return sb.toString();
	}
/*
	public static String grep(String pattern, String text) throws IOException {
		if	(pattern==null || pattern.length()<=0)	return text;
		String ret	="";
		String result[]=text.split("\n");

		for	(int i=0;i<result.length;i++)	ret+=result[i]+"\n";
		return ret;
	}
*/

	//java执行(调用)操作系统命令
	//public static synchronized String system(String cmd){	//throws IOException
	public static String system(String cmd){	//throws IOException
		return	system(cmd,1);
	}

	//java执行(调用)操作系统命令
	//public static synchronized String system(String cmd,int timeout){	//throws IOException
	public static String system(String cmd,int timeout){	//throws IOException
		Runtime					rt=Runtime.getRuntime();
		StringBuffer		sb=new	StringBuffer();
		String					inline="";

		try	{
			//if	(cmd.indexOf("|")>0 || cmd.indexOf(";")>0 || cmd.indexOf("*")>0 || cmd.indexOf("&")>0){
			if	(cmd.indexOf("|")>0 || cmd.indexOf(">")>0 || cmd.indexOf(";")>0 || cmd.indexOf("*")>0 || cmd.indexOf("nohup")>0 || cmd.indexOf("EOF")>0){
				try {
					inline=CLog.getPath()+File.separator+App.getCurTimeStr(7)+RandomStringUtils.randomAlphabetic(6);
					if	(osname.startsWith("windows")){
						inline+=".cmd";
						cmd="@"+cmd;
					}	else	{
						inline+=".sh";
						//if	(cmd.indexOf(";")<=0 && cmd.indexOf("&")<=0)	cmd+=" 2>&1";
					}
					FileOutputStream out = new FileOutputStream(inline,true);
					PrintWriter p = new PrintWriter(out);
					p.println(cmd);
					CLog.writeLog(inline+" command is:["+cmd+"]");
					if	(osname.startsWith("windows"))	p.println("@del \""+inline.substring(1)+"\"");	else	p.println("rm -f "+inline);
					p.close();
					out.close();
				} catch (Exception e) {
					CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+" err...,Exception=["+e.toString()+"]");
					return	"";
				}
				if	(osname.startsWith("linux"))	inline="sh "+inline+" &";
						else	inline="\""+inline.substring(1)+"\"";

				//return	system(inline,timeout);
				cmd=inline;
			}
			//else	{
				CLog.writeLog("system exec command:["+cmd+"],"+timeout);
				//if	(osname.startsWith("linux"))		cmd+=" 2>&1";
				Process					proc=rt.exec(cmd);
				BufferedReader	br=new	BufferedReader(new   InputStreamReader(proc.getInputStream(),myencode));
				if	(timeout>0)	Thread.sleep(timeout);
				while(null!=(inline=br.readLine())){
					sb.append(inline).append("\n");
				}
			//}
		}	catch(Exception e)	{
			CLog.writeLog("error,system exec command:"+cmd);
			CLog.writeLog(e.toString());
		}

		//CLog.writeLog("system exec command:"+cmd+",timeout:"+timeout+",result:"+sb.toString());
		return sb.toString();
	}

	//public static synchronized String run(String cmd) {	//throws IOException
	public static String run(String cmd) {	//throws IOException
		return	run(cmd,null);
	}
	//public static synchronized String run(String cmd,String workDirectory) {	//throws IOException
	public static String run(String cmd,String workDirectory) {	//throws IOException
		String[] mycmd=new String[1];
		StringTokenizer tokens0=new StringTokenizer(cmd," 	");
		if	(cmd.indexOf("|")>0 || cmd.indexOf(">")>0 || cmd.indexOf(";")>0 || cmd.indexOf("*")>0 || cmd.indexOf("&")>0 || cmd.indexOf("sh")>0 || cmd.indexOf("EOF")>0){
			return	system(cmd);
		}	else	{
			mycmd=new String[tokens0.countTokens()];
			for	(int i=0;tokens0.hasMoreTokens();i++)	mycmd[i]=tokens0.nextToken();
		}
		return	run(mycmd,workDirectory);
	}
/*
	public static synchronized String run(String cmd,String workDirectory) {	//throws IOException
		String[] mycmd;
		if	(cmd.indexOf("|")>0 || cmd.indexOf(";")>0 || cmd.indexOf("*")>0){
			return	system(cmd);
		}	else	{
			mycmd=cmd.split(" |	",0);
		}
		return	run(mycmd,workDirectory);
	}
*/
	//public static synchronized String run(String[] cmd) {	//throws IOException
	public static String run(String[] cmd) {	//throws IOException
		return	run(cmd,null);
	}

	//public static synchronized String run(String[] cmd,String workDirectory) {	//throws IOException
	public static String run(String[] cmd,String workDirectory) {	//throws IOException
		String line=null;
		String result="";
		String[] mycmd=cmd;
		//CLog.writeLog(cmd[0]);
		if	(cmd==null || cmd.length<=0)	return result;
		if	(osname.startsWith("windows")){
			mycmd=new String[cmd.length+2];
			mycmd[0]="cmd.exe";
			mycmd[1]="/c";
			for	(int i=0;i<cmd.length;i++)	mycmd[i+2]=cmd[i];
		}
		//CLog.writeLog("run command:"+mycmd.toString());
		try {
			ProcessBuilder builder = new ProcessBuilder(mycmd);

			//set working directory
			if (workDirectory!=null && workDirectory.trim().length()>0)		builder.directory(new File(workDirectory));

			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream is=process.getInputStream();
			//InputStreamReader isr = new  InputStreamReader(is,myencode);
			InputStreamReader isr = new  InputStreamReader(is,myencode);
			BufferedReader br = new  BufferedReader(isr);
			while  ((line = br.readLine()) !=  null ) {
				//CLog.writeLog(new String(re, myencode));
				//result+=new String(re, myencode);
				result+=line+"\n";
			}
			is.close();
			isr.close();
			br.close();
			process.destroy();
			process=null;
			builder=null;
			is=null;
			isr=null;
			line=null;
		} catch (Exception ex) {
			CLog.writeLog(ex.toString());
			//ex.printStackTrace();
		}

		return result;
	}

	public static String cat(String filename) {	//throws IOException
		String line=null;
		String result="";

		try {
			InputStreamReader isr =new InputStreamReader(new FileInputStream(filename),myencode);
			BufferedReader br = new  BufferedReader(isr);
			while  ((line = br.readLine()) !=  null ) {
				//CLog.writeLog(new String(re, myencode));
				//result+=new String(re, myencode);
				result+=line+"\n";
			}
			isr.close();
			br.close();
			isr=null;
			line=null;
		} catch (Exception ex) {
			CLog.writeLog(ex.toString());
			//ex.printStackTrace();
		}

		return result;
	}

	public void write(String filename,String context) {	//throws IOException
		try {
			FileOutputStream out = new FileOutputStream(filename,false);
			//Connect print stream to the output stream
			PrintWriter p = new PrintWriter(out);
			context = new String(context.getBytes("GBK"));
			p.println(context);
			p.close();
			out.close();
		} catch (Exception e) {
			CLog.writeLog(e.toString());
		}
	}

	public static String sendByTCP(String myip,int myports,String mytext) {	//throws IOException
		return	sendByTCP(myip,myports,0,mytext);
	}
	public static String sendByTCP(String myip,String myports,String mytext) {	//throws IOException
		return	sendByTCP(myip,Integer.parseInt("0"+myports),0,mytext);
	}
	public static String sendByTCP(String myip,String myports,String  timeout,String mytext) {	//throws IOException
		return	sendByTCP(myip,Integer.parseInt("0"+myports),Integer.parseInt("0"+timeout),mytext);
	}
	public static String sendByTCP(String myip,String myports,int timeout,String mytext) {	//throws IOException
		return	sendByTCP(myip,Integer.parseInt("0"+myports),timeout,mytext);
	}
	public static String sendByTCP(String myip,int myports,int timeout,String mytext) {	//throws IOException
		String myresponse = null;
		Socket mysocket=null;
		DataInputStream myin=null;
		DataOutputStream myout=null;
		
		try {
			mysocket = new Socket();
			mysocket.setReuseAddress(true);
			mysocket.connect(new InetSocketAddress(myip,myports));
			//CLog.writeLog("mysocket:" + mysocket);
			CLog.writeLog("mysocket.isConnected():" + mysocket.isConnected());
			if (mysocket.isConnected())	{
				myin = new DataInputStream(mysocket.getInputStream());
				myout = new DataOutputStream(mysocket.getOutputStream());
				mysocket.setSoTimeout(100); //timeout
				CLog.writeLog("timeout:" + mysocket.getSoTimeout());
				byte[] rcvByte = new byte[40960];
				try {
					//myin.read(rcvByte, 0,40960);
				}	catch (Exception ex) {
				}
				mysocket.setSoTimeout(timeout*1000); //timeout
				CLog.writeLog("timeout:" + mysocket.getSoTimeout());
				byte[] bdata=null;
				if	(mytext!=null && mytext.length()>0){
					//bdata=mytext.getBytes();
					CLog.writeLog("send:"+mytext);
					myout.write(mytext.getBytes(),0,mytext.getBytes().length);
				}
				int len = myin.read(rcvByte, 0,40960);
				if (len > 0) {
					bdata = new byte[len];
					System.arraycopy(rcvByte, 0,bdata, 0, len);
					myresponse=new String(bdata,myencode);
					CLog.writeLog("response:"+myresponse);
				}					
				myin.close();
				myout.close();
				mysocket.close();
			}
		}	catch (IOException e) {
			CLog.writeLog("new socket error1");
			CLog.writeLog("getMessage=" + e.getMessage());
			CLog.writeLog("Exception=" + e.toString());
		}	catch (Exception ex) {
			CLog.writeLog("new socket error2");
			CLog.writeLog("getMessage=" + ex.getMessage());
			CLog.writeLog("Exception=" + ex.toString());
		}	finally {
			CLog.writeLog("ip:"+myip+":"+myports);
			myin=null;
			myout=null;
			mysocket=null;
		}

		return	myresponse;
	}

	public static StringBuffer sendByTCP(String myip,int myports,int timeout,String mytext,int falg) {	//throws IOException
		StringBuffer myresponse = new StringBuffer("");
		Socket mysocket=null;
		DataInputStream myin=null;
		DataOutputStream myout=null;		
		try {
			mysocket = new Socket();
			mysocket.setReuseAddress(true);
			mysocket.connect(new InetSocketAddress(myip,myports));
			//CLog.writeLog("mysocket:" + mysocket);
			CLog.writeLog("mysocket.isConnected():" + mysocket.isConnected());
			if (mysocket.isConnected())	{
				myin = new DataInputStream(mysocket.getInputStream());
				myout = new DataOutputStream(mysocket.getOutputStream());
				mysocket.setSoTimeout(100); //timeout
				CLog.writeLog("timeout:" + mysocket.getSoTimeout());
				byte[] rcvByte = new byte[40960];
				try {
					//myin.read(rcvByte, 0,40960);
				}	catch (Exception ex) {
				}
				mysocket.setSoTimeout(timeout*1000); //timeout
				CLog.writeLog("timeout:" + mysocket.getSoTimeout());
				byte[] bdata=null;
				if	(mytext!=null && mytext.length()>0){
					//bdata=mytext.getBytes();
					CLog.writeLog("send:"+mytext);
					myout.write(mytext.getBytes(),0,mytext.getBytes().length);
				}
				int len = 0;
				int readLen = 10000;
				while((len = myin.read(rcvByte, 0,readLen)) > 0){
				
					bdata = new byte[len];
					System.arraycopy(rcvByte, 0 ,bdata, 0, len);
					String temp = new String(bdata,myencode);
					myresponse.append(temp);
//System.out.println(temp);					
//System.out.println("can read:"+myin.available());
//System.out.println("----------------------------------------------------------------------");
					if(myin.available() == 0)
						break;
				}
				myin.close();
				myout.close();
				mysocket.close();
			}
		}	catch (IOException e) {
			CLog.writeLog("new socket error1");
			CLog.writeLog("getMessage=" + e.getMessage());
			CLog.writeLog("Exception=" + e.toString());
		}	catch (Exception ex) {
			CLog.writeLog("new socket error2");
			CLog.writeLog("getMessage=" + ex.getMessage());
			CLog.writeLog("Exception=" + ex.toString());
		}	finally {
			CLog.writeLog("ip:"+myip+":"+myports);
			myin=null;
			myout=null;
			mysocket=null;
		}

		return	myresponse;
	}
	
	public static String base64encode(byte[] b) {
		BASE64Encoder enCoder = new BASE64Encoder();
		return  enCoder.encode(b);
	}

	public static byte[] base64decode(String s) {
		BASE64Decoder deCoder = new BASE64Decoder();
		try {
			return  deCoder.decodeBuffer(s);
		}	catch (Exception e) {
			CLog.writeLog("base64decode:["+s+"],["+e.getMessage()+"]");
			return  hex2byte(s);
		}
	}

	//压缩
	public static String compress(String str) throws IOException {
		try {
			ByteArrayOutputStream bos = null;
			GZIPOutputStream os = null;
			byte[] bs = null;
			try {
				bos = new ByteArrayOutputStream();
				os = new GZIPOutputStream(bos);
				os.write(str.getBytes());
				os.finish(); //这个在写入arrayOutputStream时一定要有，否则不能完全写入
				os.close();
				bos.close();
				bs = bos.toByteArray();
				return base64encode(bs);
				//return new String(bs, "iso-8859-1");
			} finally {
				bs = null;
				bos = null;
				os = null;
			}
		} catch (Exception ex) {
			return str;
		}
	}

	// 解压缩
	public static String uncompress(String str) throws IOException {
		ByteArrayInputStream bis = null;
		ByteArrayOutputStream bos = null;
		GZIPInputStream is = null;
		byte[] buf = null;
		try {
			//bis = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
			bis = new ByteArrayInputStream(base64decode(str));
			bos = new ByteArrayOutputStream();
			is = new GZIPInputStream(bis);
			buf = new byte[1024];
			int len;
			while ((len = is.read(buf)) != -1) {
				bos.write(buf, 0, len);
			}
			is.close();
			bis.close();
			bos.close();
			return new String(bos.toByteArray());
		} catch (Exception ex) {
			return str;
		} finally {
			bis = null;
			bos = null;
			is = null;
			buf = null;
		}
	}


	//银行卡的校验算法
	public static String getCardnoLastDigit(int intCardNo){
		return getCardnoLastDigit(""+intCardNo);
	}

	public static String getCardnoLastDigit(String strCardNo){
		if	(strCardNo==null || strCardNo.length()<=1)	return strCardNo;
		return getCardnoLastDigit(strCardNo,strCardNo.length());
	}

	//银行卡的校验算法
	public static String getCardnoLastDigit(String strCardNo,int intCardNoLen){
		if	(strCardNo==null || strCardNo.length()<=1 || strCardNo.length()<intCardNoLen)	return strCardNo;
		boolean 	bolStatus=true;
		int				intTotal=0,intDigits=0;
		for (int i=intCardNoLen; i > 0; i--) {
			if	(bolStatus) {
				intDigits = Integer.parseInt(strCardNo.substring(i - 1, i)) * 2;
				if (intDigits > 9)  intDigits -= 9;
				intTotal += intDigits;
				bolStatus = false;
			}	else {
				intTotal = intTotal + Integer.parseInt(strCardNo.substring(i - 1, i));
				bolStatus = true;
			}
		}
		intDigits = 10 - intTotal%10;

		return ""+intDigits%10;
	}

	public String telnetTest(String ip,String port) {
		String msg = null;
//		Socket mysocket = null;
		try {				
			new Socket().connect(new InetSocketAddress(ip, new Integer(port)),5);
			msg = "telent "+ip+" "+port+"\nSuccess!";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			msg = "telent "+ip+" "+port+"\nError!";
		}
		CLog.writeLog("App.telnetTest:"+msg);
		return msg;
	}
	public String getTcpRepResponse(String cmdRep){
		String rep = null;		
		try {
			rep = cmdRep.substring(cmdRep.indexOf("<responsecode>")+new String("<responsecode>").length(), cmdRep.indexOf("</responsecode>"));
		} catch (RuntimeException e) {
			return null;
		}		
		return rep;
	}
	public String getTcpRepResponse(StringBuffer cmdRep){
		String rep = null;		
		try {
			rep = cmdRep.substring(cmdRep.indexOf("<responsecode>")+new String("<responsecode>").length(), cmdRep.indexOf("</responsecode>"));
		} catch (RuntimeException e) {
			return null;
		}		
		return rep;
	}

	public String getTcpRepText(String cmdRep){
		String rep = null;		
		try {
			rep = cmdRep.substring(cmdRep.indexOf("<text>")+new String("<text>").length(), cmdRep.indexOf("</text>"));
		} catch (RuntimeException e) {
			return null;
		}		
		return rep;
	}
	public StringBuffer getTcpRepText(StringBuffer cmdRep){
		try {
//			rep = cmdRep.substring(cmdRep.indexOf("<responsecode>")+new String("<responsecode>").length(), cmdRep.indexOf("</responsecode>"));
			cmdRep.delete(0, cmdRep.indexOf("<text>")+new String("<text>").length());
			cmdRep.delete(cmdRep.indexOf("</text>"),cmdRep.length());
		} catch (RuntimeException e) {
System.out.println("public StringBuffer getTcpRepText(StringBuffer cmdRep) error");
			return cmdRep;
		}		
		return cmdRep;
	}
	
	/*
	 * forsentlog 发送日志
	 * author: zhaoyanmei
	 * time:2011-10-14
	 */
	   public  static StringBuffer sendByTCPForLog(String myip,int myports,int timeout,String mytext,int falg) { //throws IOException
		   StringBuffer myresponse = new StringBuffer("");
		   Socket mysocket=null;
		   DataInputStream myin=null;
		   DataOutputStream myout=null;
		   BufferedReader read=null;
		   try {
		    mysocket = new Socket();
		    mysocket.setReuseAddress(true);
		    mysocket.connect(new InetSocketAddress(myip,myports));
		    if (mysocket.isConnected()) {
		     read = new BufferedReader(new InputStreamReader(mysocket.getInputStream()));
		     myout = new DataOutputStream(mysocket.getOutputStream());
		     mysocket.setSoTimeout(timeout*1000); //timeout
		     CLog.writeLog("timeout:" + mysocket.getSoTimeout());
		     byte[] bdata=null;
		     if (mytext!=null && mytext.length()>0){
		      myout.write(mytext.getBytes(),0,mytext.getBytes().length);
		      
		     }
		     String temp = "";
		     while ((temp = read.readLine()) != null) {
		      myresponse.append(temp).append("\r\n");
		        }
		     read.close();
		     myout.close();
		     mysocket.close();
		    }
		   } catch (IOException e) {
		    CLog.writeLog("new socket error1");
		    CLog.writeLog("getMessage=" + e.getMessage());
		    CLog.writeLog("Exception=" + e.toString());
		   } catch (Exception ex) {
		    CLog.writeLog("new socket error2");
		    CLog.writeLog("getMessage=" + ex.getMessage());
		    CLog.writeLog("Exception=" + ex.toString());
		   } finally {
		    CLog.writeLog("ip:"+myip+":"+myports);
		    
		    try{
		     read.close();
		     myout.close();
		     mysocket.close();
		    }catch(Exception e){
		     
		    }
		    
		    myin=null;
		    myout=null;
		    mysocket=null;
		   }
		   return myresponse;
		  }

	
	
	public static void main(String[] args) {

		System.out.println(dmaxDaysofMon("20010105"));
		System.out.println("970408029001105+"+getCardnoLastDigit("970408029001105"));
		System.out.println("970408019001043+"+getCardnoLastDigit("970408019001043"));

		try		{
			String[] cmd={"java","-version"};
			//String[] cmd={"cmd.exe","/c","dir","/p"};
			String result= run(cmd,"d:/");
			System.out.println(result);

			System.out.println(getPortInfo());
			System.out.println(getAllLocalIP());
			System.out.println(getLastPort());
		}		catch(Exception e)		{
			e.printStackTrace();
		}		
		
		
/*
		try		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}		catch(Exception e)		{
			e.printStackTrace();
		}
		//Create application frame.
		//JStartWindow start = new JStartWindow();
		//start.showFrame("ccas start window");//显示主窗口
		//显示启动界面，3s钟后自动消失
		//start.start();
		MyMainFrame myframe = new MyMainFrame();
		//System.out.print("ok");
		//Show frame
		//myframe.validate();
		myframe.setVisible(true);
	}

		try {
			Class c = Class.forName("ccas.Clog");
			Method toolboxMain = c.getMethod("main", new Class[] {args.getClass()});
			toolboxMain.invoke(null, new Object[] {args} );
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"err...");
		}
*/

/*
		System.out.println("CVV="+App.getCVV("4123456789012345","8701","101","0123456789ABCDEF","FEDCBA9876543210"));
		System.out.println("PVV="+App.getPVV("46666555544441111","345612","2","0123456789ABCDEF","FEDCBA9876543210"));
*/
		System.out.println("PINBlock="+App.getPINBlock("4063151111111115","000000"));
		System.out.println("asc2hex="+App.asc2hex("1234567"));
		System.out.println("hex2asc="+App.hex2asc("31323334353637"));
	}	   
	   
}