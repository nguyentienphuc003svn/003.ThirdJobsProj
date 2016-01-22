package ccasws;

import java.io.*;
import java.util.*;
import java.net.*;
import java.text.*;
import java.math.*;
import java.lang.reflect.*;
import org.springframework.transaction.*;
import javax.sql.DataSource;

public class BaseInterface implements Cloneable,java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public Object clone() throws CloneNotSupportedException	{
		Object result = super.clone();
		//((ObInterface)result).setDataSource(this.dataSource);
		return result;
	}

	//属性
	private	PlatformTransactionManager tmanager=null;					//事务管理
	private	TransactionStatus tstatus=null;										//事务状态
	private	DataSource dataSource=null;												//数据源
	private	String drvname = "";															//数据库名称
	private	String apname = "cms";														//应用程序名称
	private	String sql = "";
	private	String table = "";
	private	String tablename="";
	private	String where = "";
	private	String dml = "";
	private	String errMsg = "";

	private	int retNum = 0;

	//private int pageSize = 20;
	private int pageSize = 200;

	private int maxPage = 0;

	private int pageNo = 1;

	private int serialNo = 0;

	private int rowNum = 0;

	private Map userMap=new HashMap();
	//private Map userMap=new TreeMap();


	public	void resetMap() {
		userMap=new HashMap();
	}

	//getter
	public	Map	getMap() {
		return this.userMap;
	}

	//setter
	public	void	setMap(Map userMap) {
		this.userMap=userMap;
	}
	public	void	setMap(javax.servlet.http.HttpServletRequest request) {
		try	{
			String name=null;
			Enumeration  pNames=request.getParameterNames();
			while(pNames.hasMoreElements()){
				name=(String)pNames.nextElement();
				//CLog.writeLog(name+"="+request.getParameter(name));
				set(name,request.getParameter(name));
			}
		}	catch(Exception e){
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",request=["+request+"]");
			//out.print(e.toString());
		}

	}
	
	public String	toWhere(String myname) {
		return toWhere(myname,getString(myname),"like");
	}
	public String	toWhere(String myname,String myvalue) {
		return toWhere(myname,myvalue,"like");
	}
	public String	toWhere(String myname,String myvalue,String myoperator) {
		String	ret="";
		if	(myvalue!=null && myvalue.length()>0){
			if	(myoperator!=null && myoperator.equalsIgnoreCase("like"))	ret=myname.toUpperCase()+" like '%"+myvalue+"%'";
				else	ret=myname.toUpperCase()+myoperator+"'"+myvalue+"'";
		}
		return ret;
	}

	public String	toWhere(String myname,int myvalue) {
		return toWhere(myname,myvalue,"=");
	}
	public String	toWhere(String myname,int myvalue,String myoperator) {
		String	ret="";
		//if	(myvalue>0)	ret=myname.toUpperCase()+myoperator+"'"+myvalue+"'";
		if	(myvalue>0)	ret=myname.toUpperCase()+myoperator+myvalue;
		return ret;
	}

	public String	toWhere(String myname,double myvalue) {
		return toWhere(myname,myvalue,"=");
	}
	public String	toWhere(String myname,double myvalue,String myoperator) {
		String	ret="";
		if	(myvalue>0.0)	ret=myname.toUpperCase()+myoperator+"'"+myvalue+"'";
		return ret;
	}

	public String	toWhere(String myname,float myvalue) {
		return toWhere(myname,myvalue,"=");
	}
	public String	toWhere(String myname,float myvalue,String myoperator) {
		String	ret="";
		if	(myvalue>0.0)	ret=myname.toUpperCase()+myoperator+"'"+myvalue+"'";
		return ret;
	}

	public String getWhere(String ... args) {
		String ret="";
		String ret1="";
		for	(String argsname:args){
			if	(argsname!=null && argsname.trim().length()>0){
				if	(argsname.trim().toLowerCase().indexOf("order")<0){
					if	(ret.length()>0)		ret+=" and ";
					ret+=argsname.trim();
				}	else	ret1=" "+argsname.trim();
			}
		}
		ret+=ret1;
		return ret;
	}


	public String setWhere(String ... args) {
		String ret="";
		String ret1="";
		for	(String argsname:args){
			if	(argsname!=null && argsname.trim().length()>0){
				if	(argsname.trim().toLowerCase().indexOf("order")<0){
					if	(ret.length()>0)		ret+=" and ";
					ret+=argsname.trim();
				}	else	ret1=" "+argsname.trim();
			}
		}
		ret+=ret1;
		this.where=ret;
		return ret;
	}


	public void addWhere(String ... args) {
		String ret=this.where;
		String ret1="";
		for	(String argsname:args){
			if	(argsname!=null && argsname.trim().length()>0){
				if	(argsname.trim().toLowerCase().indexOf("order")<0){
					if	(ret.length()>0)		ret+=" and ";
					ret+=argsname.trim();
				}	else	ret1=" "+argsname.trim();
			}
		}
		ret+=ret1;
		this.where=ret;
	}



	//getter
	public	Object	get(String myname) {
		Object	retobj=getFieldValue(myname);
		if	(retobj instanceof String)	return	TransEncoding((String)retobj);
		if	(retobj==null)	retobj="";
		return retobj;
	}

	public	String	getString(String myname) {
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+","+myname+"=["+getFieldValue(myname)+"]");
		Object retobj = get(myname);
		if	(retobj instanceof String)	return	TransEncoding((String)retobj);
		if	(retobj instanceof Integer)	return	String.format("%d",((Integer)retobj).intValue());
		if	(retobj instanceof Long)		return	String.format("%d",((Long)retobj).longValue());
		if	(retobj instanceof Double)	return	String.format("%.f",((Double)retobj).doubleValue());
		if	(retobj instanceof Float)		return	String.format("%.f",((Float)retobj).floatValue());
		if	(retobj instanceof Boolean){
			if	(((Boolean)retobj).booleanValue())	return	"true";	else	return	"false";
		}
		if	(retobj instanceof Date){
			return	(new SimpleDateFormat("yyyy/MM/dd")).format((Date)retobj);
		}
		return TransEncoding((String)retobj);
	}
	public	int	getInt(String myname) {
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+","+myname+"=["+getFieldValue(myname)+"]");
		Object retobj = get(myname);
		try	{
			if	(retobj instanceof String)	return	Integer.parseInt(parsetoInt((String)retobj,"0"));
			if	(retobj instanceof Integer)	return	((Integer)retobj).intValue();
			if	(retobj instanceof Long)		return	(int)((Long)retobj).longValue();
			if	(retobj instanceof Double)	return	(int)((Double)retobj).doubleValue();
			if	(retobj instanceof Float)		return	(int)((Float)retobj).floatValue();
			if	(retobj instanceof Boolean){
				if	(((Boolean)retobj).booleanValue())	return	1;	else	return	0;
			}
			if	(retobj instanceof Date){
				return	Integer.parseInt((new SimpleDateFormat("yyyyMMdd")).format((Date)retobj));
			}
			return Integer.parseInt(parsetoInt((String)retobj,"0"));

		}	catch	(Exception e)	{
		}
		return 0;

	}

	public	long	getLong(String myname) {
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+","+myname+"=["+getFieldValue(myname)+"]");
		Object retobj = get(myname);
		try	{
			if	(retobj instanceof String)	return	Long.parseLong(parsetoInt((String)retobj,"0"));
			if	(retobj instanceof Integer)	return	((Integer)retobj).intValue();
			if	(retobj instanceof Long)		return	((Long)retobj).longValue();
			if	(retobj instanceof Double)	return	(long)((Double)retobj).doubleValue();
			if	(retobj instanceof Float)		return	(long)((Float)retobj).floatValue();
			if	(retobj instanceof Boolean){
				if	(((Boolean)retobj).booleanValue())	return	1;	else	return	0;
			}
			if	(retobj instanceof Date){
				return	Long.parseLong((new SimpleDateFormat("yyyyMMdd")).format((Date)retobj));
			}
			return Long.parseLong(parsetoInt((String)retobj,"0"));
		}	catch	(Exception e)	{
		}
		return 0L;
	}

	public	double	getDouble(String myname) {
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+","+myname+"=["+getFieldValue(myname)+"]");
		Object retobj = get(myname);
		try	{
			if	(retobj instanceof String)	return	Double.parseDouble(parsetoInt((String)retobj,"0"));
			if	(retobj instanceof Integer)	return	((Integer)retobj).intValue();
			if	(retobj instanceof Long)		return	((Long)retobj).longValue();
			if	(retobj instanceof Double)	return	((Double)retobj).doubleValue();
			if	(retobj instanceof Float)		return	((Float)retobj).floatValue();
			if	(retobj instanceof Boolean){
				if	(((Boolean)retobj).booleanValue())	return	1;	else	return	0;
			}
			if	(retobj instanceof Date){
				return	Integer.parseInt((new SimpleDateFormat("yyyyMMdd")).format((Date)retobj));
			}
			return Double.parseDouble(parsetoInt((String)retobj,"0"));
		}	catch	(Exception e)	{
		}
		return 0.0;
	}

	public	float	getFloat(String myname) {
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+","+myname+"=["+getFieldValue(myname)+"]");
		Object retobj = get(myname);
		try	{
			if	(retobj instanceof String)	return	Float.parseFloat(parsetoInt((String)retobj,"0"));
			if	(retobj instanceof Integer)	return	((Integer)retobj).intValue();
			if	(retobj instanceof Long)		return	((Long)retobj).longValue();
			if	(retobj instanceof Double)	return	(float)((Double)retobj).doubleValue();
			if	(retobj instanceof Float)		return	((Float)retobj).floatValue();
			if	(retobj instanceof Boolean){
				if	(((Boolean)retobj).booleanValue())	return	1;	else	return	0;
			}
			if	(retobj instanceof Date){
				return	Integer.parseInt((new SimpleDateFormat("yyyyMMdd")).format((Date)retobj));
			}

			return Float.parseFloat(parsetoInt((String)retobj,"0"));
		}	catch	(Exception e)	{
		}
		return (float)0.0;
	}

	private	String	parsetoInt(String mydefault,String myvalue) {
		String	ret=mydefault;
		if	(ret==null || ret.length()<=0)		ret=myvalue;
		return ret;
	}


	public	String	getDefault(String myname,String myvalue) {
		String	ret=getString(myname);
		if	(ret==null || ret.length()<=0)		ret=myvalue;
		return ret;
	}


	//setter
	public	void	set(String myname,Object myobjvalue) {
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+","+myname+"=["+getFieldValue(myname)+"]");
		if	(myobjvalue instanceof String)			set(myname,(String)myobjvalue);
		if	(myobjvalue instanceof Integer)			set(myname,(Integer)myobjvalue);
		if	(myobjvalue instanceof Long)				set(myname,(Long)myobjvalue);
		if	(myobjvalue instanceof Double)			set(myname,(Double)myobjvalue);
		if	(myobjvalue instanceof Float)				set(myname,(Float)myobjvalue);
		if	(myobjvalue instanceof Boolean)			set(myname,(Boolean)myobjvalue);
		if	(myobjvalue instanceof java.sql.Date)				set(myname,(java.sql.Date)myobjvalue);



/*
		if	(myobjvalue instanceof Integer)	return	String.format("%d",((Integer)myobjvalue).intValue());
		if	(myobjvalue instanceof Long)		return	String.format("%d",((Long)myobjvalue).longValue());
		if	(myobjvalue instanceof Double)	return	String.format("%.f",((Double)myobjvalue).doubleValue());
		if	(myobjvalue instanceof Float)		return	String.format("%.f",((Float)myobjvalue).floatValue());
		if	(myobjvalue instanceof Boolean){
			if	(((Boolean)myobjvalue).booleanValue())	return	"true";	else	return	"false";
		}
		if	(myobjvalue instanceof Date){
			return	(new SimpleDateFormat("yyyy/MM/dd")).format((Date)myobjvalue);
		}
*/
	}
	public	void	set(String myname,String myvalue) {
		try	{
			setFieldByName(myname,myvalue);
		}	catch	(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",name=["+myname+"]");
		}
	}


	public	void	set(String myname,int myvalue) {
		try	{
			setFieldByName(myname,myvalue);
		}	catch	(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",name=["+myname+"]");
		}
	}

	public	void	set(String myname,long myvalue) {
		try	{
			setFieldByName(myname,myvalue);
		}	catch	(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",name=["+myname+"]");
		}
	}

	public	void	set(String myname,double myvalue) {
		try	{
			setFieldByName(myname,myvalue);
		}	catch	(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",name=["+myname+"]");
		}
	}

	public	void	set(String myname,float myvalue) {
		try	{
			setFieldByName(myname,myvalue);
		}	catch	(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",name=["+myname+"]");
		}
	}
	public	void	set(String myname,Boolean myvalue) {
		try	{
			setFieldByName(myname,myvalue);
		}	catch	(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",name=["+myname+"]");
		}
	}
	public	void	set(String myname,java.sql.Date myvalue) {
		try	{
			setFieldByName(myname,myvalue);
		}	catch	(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",name=["+myname+"]");
		}
	}
	public	void	set(String myname,byte[] myvalue) {
		try	{
			setFieldByName(myname,myvalue);
		}	catch	(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",name=["+myname+"]");
		}
	}
	public String setDefault(String myname,String myvalue) {
		try	{
			String ret=getString(myname);
			if	(ret!=null && ret.length()<=0 && myvalue!=null && myvalue.length()>0){
				set(myname,myvalue);
				ret=myvalue;
			}
			return ret;
		}	catch	(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",name=["+myname+"]");
		}
		return myvalue;
	}

	public int setDefault(String myname,int myvalue) {
		try	{
			int ret=getInt(myname);
			if	(ret==0 && myvalue!=0){
				set(myname,myvalue);
				ret=myvalue;
			}
			return ret;
		}	catch	(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",name=["+myname+"]");
		}
		return myvalue;
	}

	public double setDefault(String myname,double myvalue) {
		try	{
			double ret=getInt(myname);
			if	(ret==0.0 && myvalue!=0.0){
				set(myname,myvalue);
				ret=myvalue;
			}
			return ret;
		}	catch	(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",name=["+myname+"]");
		}
		return myvalue;
	}

	public float setDefault(String myname,float myvalue) {
		try	{
			float ret=getInt(myname);
			if	(ret==0.0 && myvalue!=0.0){
				set(myname,myvalue);
				ret=myvalue;
			}
			return ret;
		}	catch	(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",name=["+myname+"]");
		}
		return myvalue;
	}


	public String TransEncoding(String c) {
		if (c == null)			c = "";
/*
		try	{
			return new String(c.trim().getBytes("ISO8859_1"),"GBK");
		} catch (Exception e) {	}
*/
		return c.trim();
	}
/*
	public String transFirstUpperCase(String chi) {
		String result = null;
		StringBuffer temp = null;
		try {
			temp = new StringBuffer(chi);
			temp.setCharAt(0, chi.toUpperCase().charAt(0));
			result = new String(temp);
		} catch (Exception e) {
			CLog.writeLog("transFirstUpperCase [" + e.toString()+"]");
		}
		return result;
	}
*/


	public String transFirstUpperCase(String str) {
		if	(str==null || str.length()<=0)	return str;
		if	(str.length()==1)	return str.toUpperCase();
		return str.toUpperCase().charAt(0)+str.substring(1);
	}

	public Object getFieldValue(String name)	{	//throws Exception
		Object	arglist[] = new Object[]{};
		Object	retobj=null;
		Class		c=this.getClass();
		int			max=0;
		Class		classlist[]=null;
		String methodname = "get" + transFirstUpperCase(name.trim());
		try {
			for	(int i=0;;i++){
				if	(arglist[i]==null)		break;
				max=i+1;
			}
		}	catch	(Exception	e)	{
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
			retobj=mymethod.invoke(this);
			classlist=null;
		}	catch	(Exception	e)	{
			// TODO 自动生成 catch 块
			//e.printStackTrace();
			//CLog.writeLog(methodname+","+e.toString());
			retobj=userMap.get(name.toLowerCase());
			//CLog.writeLog(name.toUpperCase()+"=["+retobj.toString()+"]");
			//throw e;
		}

		//CLog.writeLog(methodname+"==="+retobj.toString());
		return retobj;
	}



	public void doSetValue(String name, Class[] parameterTypes, Object[] value) throws Exception {
		try {
			String setMethodName = "set" + transFirstUpperCase(name.trim());
			Method setMethod = this.getClass().getMethod(setMethodName,parameterTypes);
			setMethod.invoke(this, value);
		}	catch	(Exception	e)	{
			//CLog.writeLog("name=["+name+"],parameterTypes=["+parameterTypes[0].getName()+"],value=["+value);
			//CLog.writeLog("ObInterface::doSetValue() [" + e.toString() + "]");
			userMap.put(name.trim().toLowerCase(),value[0]);
			//CLog.writeLog(name.toUpperCase()+"=["+value[0].toString()+"]");
			//throw e;
		}
	}
	public void setFieldValue(String name, Object value) throws Exception {
		setFieldValue(name.trim(),value.toString());
	}

	public void setFieldValue(String name, String value) throws Exception {
		//CLog.writeLog("setFieldName-string [" + name + "][" + value + "]");
		if	(value==null)	value=" ";
		try {
			Class myclass = this.getClass();
			Method mymethod = myclass.getMethod("get"+transFirstUpperCase(name.trim()),new Class []{});
			Constructor myconstructor=myclass.getConstructor(new Class[]{});
			Object myobject = myconstructor.newInstance(new Object[]{});
			//执行方法
			Object retobj=mymethod.invoke(myobject,new Object[]{});

			Class[] parameterTypes = new Class[] { retobj.getClass() };
			Object[] setParam=null;
			String paramTypename = parameterTypes[0].getName();
			//CLog.writeLog("parameterTypes[" + parameterTypes + "],paramTypename[" + paramTypename + "]");
			if (paramTypename.indexOf("Int") >= 0 || paramTypename.indexOf("int") >= 0) {
				int fieldiValue;
				if (value.toString().trim().length()!=0)
					fieldiValue= new Integer(value.toString().trim());
				else
					fieldiValue= new Integer(0);
				setParam = new Object[] { fieldiValue };
				setFieldByName(name,fieldiValue);
			} else if (paramTypename.indexOf("String") >= 0 || paramTypename.indexOf("string") >= 0) {
				//String fieldiValue = value.toString();
				String fieldiValue = new String(value.toString().trim().getBytes("ISO8859_1"),"GBK");
				setParam = new Object[] { fieldiValue };
				setFieldByName(name,fieldiValue);
			} else if (paramTypename.indexOf("Double") >= 0 || paramTypename.indexOf("double") >= 0) {
				double fieldiValue;
				if (value.toString().trim().length()!=0)
					fieldiValue = new Double(new Double(value.toString().trim()).doubleValue());
				else
					fieldiValue = new Double(0);
				setParam = new Object[] { fieldiValue };
				setFieldByName(name,fieldiValue);
			} else if (paramTypename.indexOf("Float") >= 0 || paramTypename.indexOf("float") >= 0) {
				float fieldiValue ;
				if (value.toString().trim().length()!=0)
					fieldiValue = new Float(new Float(value.toString().trim()).floatValue());
				else
					fieldiValue = new Float(0);
				setParam = new Object[] { fieldiValue };
				setFieldByName(name,fieldiValue);
			} else if (paramTypename.indexOf("Byte") >= 0 || paramTypename.indexOf("bye") >= 0) {
				byte fieldiValue = new Byte(value.toString());
				setParam = new Object[] { fieldiValue };
				setFieldByName(name,fieldiValue);
			} else if (paramTypename.indexOf("Short") >= 0 || paramTypename.indexOf("short") >= 0) {
				short fieldiValue = new Short(value.toString().trim());
				setParam = new Object[] { fieldiValue };
				setFieldByName(name,fieldiValue);
			} else if (paramTypename.indexOf("Long") >= 0 || paramTypename.indexOf("long") >= 0) {
				long fieldiValue = new Long(value.toString().trim());
				setParam = new Object[] { fieldiValue };
				setFieldByName(name,fieldiValue);
			} else if (paramTypename.indexOf("Char") >= 0 || paramTypename.indexOf("char") >= 0) {
				Character fieldiValue = new Character(value.toString().charAt(0));
				setParam = new Object[] { fieldiValue };
				setFieldByName(name,fieldiValue);
			} else if (paramTypename.indexOf("Boolean") >= 0 || paramTypename.indexOf("boolean") >= 0) {
				boolean fieldiValue = new Boolean(value.toString());
				setParam = new Object[] { fieldiValue };
				setFieldByName(name,fieldiValue);
			} else {
				throw new Exception(("set:no such type " + paramTypename).toString());
			}

		}	catch	(Exception	e)	{
			// TODO 自动生成 catch 块
			//e.printStackTrace();
			CLog.writeLog(e.toString());
			throw e;
		}
	}

	public void setFieldByName(String name, String value) throws Exception {
		//CLog.writeLog("setFieldName-string [" + name + "][" + value + "]");
		if	(value==null)	value=" ";
		Class[] paramTypename = new Class[] { value.getClass() };
		Object[] obValue = new Object[] { value };
		doSetValue(name, paramTypename, obValue);
	}

	public void setFieldByName(String name, int value) throws Exception {
		//CLog.writeLog("setFieldName-int [" + name + "][" + value + "]");
		Class[] paramTypename = new Class[] { Integer.TYPE };
		Object[] obValue = new Object[] { new Integer(value) };
		doSetValue(name, paramTypename, obValue);
	}

	public void setFieldByName(String name, long value) throws Exception {
		//CLog.writeLog("setFieldName-int [" + name + "][" + value + "]");
		Class[] paramTypename = new Class[] { Long.TYPE };
		Object[] obValue = new Object[] { new Long(value) };
		doSetValue(name, paramTypename, obValue);
	}

	public void setFieldByName(String name, short value) throws Exception {
		//CLog.writeLog("setFieldName-int [" + name + "][" + value + "]");
		Class[] paramTypename = new Class[] { Short.TYPE };
		Object[] obValue = new Object[] { new Short(value) };
		doSetValue(name, paramTypename, obValue);
	}
	public void setFieldByName(String name, double value) throws Exception {
		//CLog.writeLog("setFieldName-double [" + name + "][" + value + "]");
		Class[] paramTypename = new Class[] { Double.TYPE };
		Object[] obValue = new Object[] { new Double(value) };
		doSetValue(name, paramTypename, obValue);
	}
	public void setFieldByName(String name, float value) throws Exception {
		//CLog.writeLog("setFieldName-double [" + name + "][" + value + "]");
		Class[] paramTypename = new Class[] { Float.TYPE };
		Object[] obValue = new Object[] { new Float(value) };
		doSetValue(name, paramTypename, obValue);
	}
	public void setFieldByName(String name, boolean value) throws Exception {
		//CLog.writeLog("setFieldName-double [" + name + "][" + value + "]");
		Class[] paramTypename = new Class[] { Boolean.TYPE };
		Object[] obValue = new Object[] { new Boolean(value) };
		doSetValue(name, paramTypename, obValue);
	}

	public void setFieldByName(String name, java.sql.Date value) throws Exception {
		if	(value!=null)	setFieldByName(name, value.toString());
		else setFieldByName(name,(new java.sql.Date(new java.util.Date().getTime())).toString());
	}
	public void setFieldByName(String name, byte[] value) throws Exception {
		//CLog.writeLog("setFieldName-double [" + name + "][" + value + "]");
		Class[] paramTypename = new Class[] { Byte.TYPE };
		Object[] obValue = new Object[] {value};
		doSetValue(name, paramTypename, obValue);
	}
	public void deleteFieldByName(String name){
		userMap.remove(name);
	}

	public PlatformTransactionManager getTmanager() {
		return this.tmanager;
	}

	public void setTmanager(PlatformTransactionManager tmanager) {
		this.tmanager = tmanager;
	}

	public TransactionStatus getTstatus() {
		return this.tstatus;
	}

	public void setTstatus(TransactionStatus tstatus) {
		this.tstatus = tstatus;
	}

	public DataSource getDataSource() {
		return this.dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	//getter方法
	public String getTable() {
		return this.table.trim();
	}

	//setter方法
	public void setTable(String c) {
		this.table = TransEncoding(c);
	}
	//getter方法
	public String getTablename() {
		return this.tablename.trim();
	}

	//setter方法
	public void setTablename(String c) {
		this.tablename = TransEncoding(c);
	}

	public String getWhere() {
		return this.where.trim();
	}

	public void setWhere(String c) {
		this.where = TransEncoding(c);
	}

	public String getDrvname() {
		return this.drvname.trim();
	}

	public void setDrvname(String c) {
		this.drvname = TransEncoding(c);
	}

	public String getApname() {
		return this.apname.trim();
	}

	public void setApname(String c) {
		this.apname = TransEncoding(c);
	}

	public String getSql() {
		return this.sql.trim();
	}

	public void setSql(String c) {
		this.sql = TransEncoding(c);
	}
	public String getDml() {
		return this.dml.trim();
	}

	public void setDml(String c) {
		this.dml = TransEncoding(c);
	}

	public String getErrMsg() {
		return this.errMsg.trim();
	}

	public void setErrMsg(String c) {
		this.errMsg = TransEncoding(c);
	}

	public int getRetNum() {
		return this.retNum;
	}

	public void setRetNum(int c) {
		this.retNum = c;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int c) {
		this.pageSize = c;
	}

	public int getMaxPage() {
		return this.maxPage;
	}

	public void setMaxPage(int c) {
		this.maxPage = c;
	}

	public int getPageNo() {
		return this.pageNo;
	}

	public void setPageNo(int c) {
		this.pageNo = c;
	}

	public int getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(int c) {
		this.serialNo = c;
	}

	public int getRowNum() {
		return this.rowNum;
	}

	public void setRowNum(int c) {
		this.rowNum = c;
	}





}
