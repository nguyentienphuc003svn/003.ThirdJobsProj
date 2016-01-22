package ccasws;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.lang.reflect.*;

import javax.naming.*;
import javax.sql.*;

import org.logicalcobwebs.proxool.ProxoolDataSource;


/**
 *
 * JDBC通用操作类
 *
 * 关于数据库的主要操作有：获取数据库连接；数据库查询、插入、修改、删除；
 * 断开数据库连接。这些数据库操作对于操作不同的数据表应竤刀际峭骋坏模?
 * 因此，数据库的JDBC操作是可以做成一个通用类，这样就能达到重用目的。
 *
 */

public class Mysql {
	//通过JDBC-ODBC与mssqlsever2000相联
	//public static final String dbdriver="sun.jdbc.odbc.JdbcOdbcDriver";
	//通过JDBC-ODBC与mssqlsever2000相联,数据库连接URL
	//public static String dburl="jdbc:odbc:cl-sysodbc;User=;Password=";

	//通过JDBC与mssqlsever2000相联
	//public static final String dbdriver="com.microsoft.jdbc.sqlserver.SQLServerDriver";
	//通过JDBC与mssqlsever2000相联,数据库连接URL
	//public static String dburl="jdbc:microsoft:sqlserver://192.168.1.199:1433;DatabaseName=chenglian;User=sa;Password=111111";

	//通过JDBC与informix相联
	//public static final String dbdriver="com.informix.jdbc.IfxDriver";
	//通过JDBC与informix相联,数据库连接URL
	//public static final	String dburl="jdbc:informix-sqli://192.168.1.5:5050/obts:INFORMIXSERVER=obts;User=informix;Password=informix";
	boolean			retboolean=false;
	boolean			initboolean=false;

	static private boolean			logboolean=true;
	//static private DataSource dataSource=null;
	private DataSource dataSource=null;
	static private final String defaultjndi="java:comp/env/jdbc/mydb";
	private String myjndi=defaultjndi;
	static private String myencode="GBK";
	static public String dbtype=null;
	static public String dbname=null;
	static public String dburl=null;
	static public String productname=null;
	private Connection conn=null;
	private Statement stmt=null;
	private PreparedStatement prepstmt=null;
	private PreparedStatement prepstmt2=null;
	private int i=0,retNum;
	private String	sql="";
	private String	where="";
	private	ObInterface obInterface=null;
	private	ResultSetMetaData meta=null;
	private	ResultSet rst=null;
	private	long stime=System.currentTimeMillis();
	private	DatabaseMetaData dbMetaData=null;
	private	String [] indexcolname=null;							//索引名称
	private	int		indexcolnum=0;											//索引的数量,缺省为0
	private	int		colnum=0;														//字段数量,缺省为0

	public static ProxoolDataSource pxDS;

	static	{
	}


	/**
	 * 以创建Statement 初始化Mysql
	 */
	public Mysql()	{
		//init();
	}

	public Mysql(String VAB, Boolean flag){
		init();
	}

	public void setEncode() {
		this.myencode="GBK";
		CLog.writeLog("setEncode=["+this.myencode+"]");
	}
	public void setEncode(String myencode) {
		if	(myencode!=null && myencode.length()>1)		this.myencode=myencode;
		CLog.writeLog("setEncode=["+this.myencode+"]"+myencode);
	}

	/**
	 * 以创建PreparedStatement 初始化Mysql
	 */
	public Mysql(String myjndi)	{

		try	{
			getDataSource(myjndi);
		}	
		catch (Exception e)	{
			System.out.println("Mysql init2 error: "+e);
			CLog.writeLog("Mysql init2 error: "+e);

			e.printStackTrace();
		}
	}

	public Mysql(ObInterface obInterface)	{
		this.obInterface=obInterface;
	}
	public Mysql(DataSource dataSource)	{
		this.dataSource=dataSource;

		try	
		{
			if	(dataSource != null)
			{
				////Reset Password
				try
				{
					if (pxDS == null)
					{
						pxDS = (ProxoolDataSource)dataSource;
						//System.out.println("Origin Password: [" + pxDS.getPassword() + "]");

						//pxDS.setUser(EncryptUser);
						//pxDS.setPassword(this.DsReverse(pxDS.getPassword().trim()));					
						pxDS.setPassword(VABDes.hexStringToString(new VABDes("31323334353637383132333435363738").dec(pxDS.getPassword().trim()), 2));
						System.out.println("Connect User: [" + pxDS.getUser() + "]");
						//System.out.println("Connect Password: [" + pxDS.getPassword() + "]");
						CLog.writeLog("Connect User: [" + pxDS.getUser() + "]");
						//CLog.writeLog("Connect Pass: [" + pxDS.getPassword() + "]");						

						//if	(conn==null) System.out.println("No Connection is Created");
						//else System.out.println("Connection is Created OK");
					}
				}
				catch(Exception connEE)
				{
					System.out.println("Error at Connecting");
					System.out.println(connEE.toString());
					connEE.printStackTrace();

				}
				////

				//conn=dataSource.getConnection();
				conn=pxDS.getConnection();
			}

			if	(conn==null) 
			{
				System.out.println("No Connection is Created");
				throw	new Exception();
			}
			else System.out.println("Connection is Created OK");

		}	
		catch (Exception e)	
		{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",DataSource:"+dataSource+")Exception=" + e.toString());
			//CLog.writeLog("setDataSource("+dataSource+") error: "+e);
		}
		//CLog.writeLog("getDataSource="+dataSource+",conn="+conn);
	}


	public synchronized void init()	{
		//if	(conn!=null)			return;
		stime=System.currentTimeMillis();

		try	
		{
			if	(conn==null)
			{
				if	(dataSource==null)		
				{	
					dataSource=getDataSource();
				}

				/*
				////Reset Password
				try
				{
					pxDS = (ProxoolDataSource)dataSource;
					//pxDS.setUser(EncryptUser);
					//pxDS.setPassword(this.DsReverse(pxDS.getPassword().trim()));
					pxDS.setPassword(VABDes.hexStringToString(new VABDes("31323334353637383132333435363738").dec(pxDS.getPassword().trim()), 2));
					System.out.println("Connect User: [" + pxDS.getUser() + "]");
					//System.out.println("Connect Password: [" + pxDS.getPassword() + "]");
					CLog.writeLog("Connect User: [" + pxDS.getUser() + "]");
					//CLog.writeLog("Connect Pass: [" + pxDS.getPassword() + "]");						

					//if	(conn==null) System.out.println("No Connection is Created");
					//else System.out.println("Connection is Created OK");
				}
				catch(Exception connEE)
				{
					System.out.println("Error at Connecting");
					System.out.println(connEE.toString());
					connEE.printStackTrace();

				}
				////
				 * 				 * 
				 */

				//conn=dataSource.getConnection();
				conn=pxDS.getConnection();

			}

			if	(conn==null)					
				throw	new Exception();

			//getDirectConn();
			if	(stmt != null)				{stmt.close();stmt=null;}
			if	(rst != null)					{rst.close();rst=null;}
			//stmt=conn.createStatement();
			if	(dbtype==null || dbtype.length()<=0){
				dbMetaData=conn.getMetaData();
				dbtype=dbMetaData.getDatabaseProductName().trim().toLowerCase();
				if	(conn.getCatalog()!=null)	dbname=conn.getCatalog().trim();
				dburl=dbMetaData.getURL();
				if	(dbname==null){
					if	(dbtype!=null && dbtype.toLowerCase().startsWith("oracle"))			dbname=dbMetaData.getURL().split("\\:")[dbMetaData.getURL().split("\\:").length-1].trim();
				}
				/*
				 */
				//CLog.writeLog("getURL()="+dbMetaData.getURL()+",getCatalogTerm()="+dbMetaData.getCatalogTerm());

				productname=dbMetaData.getDatabaseProductVersion();
				CLog.writeLog("Jdbc-Url:"+dburl);
				CLog.writeLog("DataSource init successful,DriverName="+dbMetaData.getDriverName()+","+dbMetaData.getDriverVersion());
				CLog.writeLog("Product Version="+dbMetaData.getDatabaseProductVersion()+",dbtype="+dbtype+",dbname="+dbname);
				dbMetaData=null;
			}
		}	catch	(SQLException e)	{
			close();
			this.dataSource=null;
			initboolean=true;
			CLog.writeLog("Mysql init1 error: "+e);
			CLog.writeLog(",errorcode="+e.getErrorCode());
			CLog.writeLog(",getMessage="+e.getMessage());
			CLog.writeLog(",Exception="+e.toString());
		}	catch (Exception e)	{
			close();
			this.dataSource=null;
			initboolean=true;
			CLog.writeLog("Mysql init1 error: "+e);
		}
	}

	public String getProductName()	 throws Exception	{
		return productname;
	}

	public String getDBType()	 throws Exception	{
		return dbtype;
		//CLog.writeLog("DBType="+dbtype);
	}
	public String getDBName()	 throws Exception	{
		return dbname;
		//CLog.writeLog("DBname="+dbname);
	}
	public String getDBUrl()	 throws Exception	{
		return dburl;
		//CLog.writeLog("dburl="+dburl);
	}
	public void setDBName(String dbname) {
		this.dbname=dbname;
	}

	public void setLog(boolean logboolean) {
		this.logboolean=logboolean;
	}

	//设置接口信息
	public void setObInterface(ObInterface obInterface) {	//throws Exception	{
		this.obInterface=obInterface;
	}
	//得到接口信息
	public ObInterface getObInterface(){	// throws Exception	{
		return this.obInterface;
	}

	public String getJndi()	 throws Exception	{
		return this.myjndi;
	}

	public void setJndi(String myjndi)	 throws Exception	{
		this.myjndi=myjndi;
	}

	public void resetDataSource() {	//throws Exception	{
		resetDataSource(defaultjndi);
	}
	public void resetDataSource(String myjndi) {	//throws Exception	{
		CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",jndi:"+myjndi);
		this.dataSource=null;
		getDataSource(myjndi);
	}

	public DataSource getDataSource()	{	 //throws Exception
		//return getDataSource("java:comp/env/jdbc/mydb");
		return getDataSource(myjndi);
		//CLog.writeLog("getDataSource");
	}
	private synchronized DataSource getDataSource(String myjndi)	{	 //throws Exception
		CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",jndi:"+myjndi);
		this.myjndi=myjndi;
		try	{
			if	(dataSource == null){
				Context ctx=new InitialContext();
				if	(ctx == null)				throw new Exception("Boom - No Context");
				//initboolean=false;

				//DataSource dataSource =	(DataSource) ctx.lookup(datasource);
				this.dataSource =	(DataSource) ctx.lookup(myjndi);

				ctx.close();
				ctx=null;
				initboolean=true;
				//dbtype=null;
			}

		}	catch (Exception e)	{
			CLog.writeLog("getDataSource("+myjndi+") error: "+e);
		}
		return this.dataSource;
		//CLog.writeLog("getDataSource");
	}
	/*
	private void getDirectConn() throws Exception	{
		try	{
			Class.forName(dbdriver).newInstance();
			conn=DriverManager.getConnection(dburl);
		}	catch (Exception e)	{
			CLog.writeLog("getDataSource() error: "+e);
		}
	}
	 */

	public Connection getConnection()	{
		return conn;
	}

	public void prepareStatement(String sql) throws SQLException	{
		prepstmt=conn.prepareStatement(sql);
	}

	public void setString(int index, String value) throws SQLException	{
		prepstmt.setString(index, value);
	}

	public void setInt(int index, int value) throws SQLException	{
		prepstmt.setInt(index, value);
	}

	public void setBoolean(int index, boolean value) throws SQLException	{
		prepstmt.setBoolean(index, value);
	}

	public void setDate(int index, java.sql.Date value) throws SQLException	{
		prepstmt.setDate(index, value);
	}

	public void setLong(int index, long value) throws SQLException	{
		prepstmt.setLong(index, value);
	}

	public void setFloat(int index, float value) throws SQLException	{
		prepstmt.setFloat(index, value);
	}

	public void setDouble(int index, double value) throws SQLException	{
		prepstmt.setDouble(index, value);
	}

	public void setBinaryStream(int index, InputStream in, int length) throws	SQLException	{
		prepstmt.setBinaryStream(index, in, length);
	}

	public void clearParameters() throws SQLException	{
		prepstmt.clearParameters();
	}

	public PreparedStatement getPreparedStatement()	 throws Exception	{
		return prepstmt;
	}

	public Statement getStatement() throws Exception	{
		return stmt;
	}

	/**
	 * 执行Statement查询语句
	 * @param sql
	 * @return
	 * @throws SQLException
	public ResultSet executeQuery(String sql) throws SQLException	{

		if	(stmt != null)				{stmt.close();stmt=null;}
		if	(stmt==null)	stmt=conn.createStatement();
		ResultSet retSet=stmt.executeQuery(sql);
		stmt.close();
		stmt=null;
		return retSet;
	}
	 */

	/**
	 * 执行PreparedStatement查询语句
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery() throws SQLException	{
		if	(prepstmt != null)	{
			return prepstmt.executeQuery();
		} else
			return null;
	}

	/**
	 * 执行Statement更改语句
	 * @param sql
	 * @throws SQLException
	 */
	public boolean executeUpdate(String sql) {
		if	(conn==null)			init();
		//if	(stmt==null)			init();
		try {
			//System.out.println("============"+sql);
			prepstmt=conn.prepareStatement(sql);
			retboolean=executeUpdate();
			this.Commit();
			//System.out.println("======retboolean======"+retboolean);
		}	catch ( Exception e ) {
			if	(!retboolean)	CLog.writeLog("error,sql=["+sql+"]");
		}	finally	{
			if	(logboolean)		CLog.writeLog("sql=["+sql+"]");
			if	(logboolean)	CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+","+retboolean);
			close();
		}
		if	(logboolean)	CLog.writeLog("==Processing time:"+(System.currentTimeMillis() - stime)+" ms==");
		return retboolean;
	}

	/**
	 * 执行PreparedStatement更改语句
	 * @throws SQLException
	 */
	public boolean executeUpdate() {
		int i=0;
		try	{
			retboolean=false;
			if	(prepstmt==null)		return false;
			if	(prepstmt.executeUpdate()>0)				retboolean=true;
		}	catch	(SQLException e)	{
			CLog.writeLog("e="+e);
			CLog.writeLog(",errorcode="+e.getErrorCode());
			CLog.writeLog(",getMessage="+e.getMessage());
			CLog.writeLog(",Exception="+e.toString());
			//e.printStackTrace();
		}	catch(Exception e)	{
			CLog.writeLog(e.toString());
		}	finally	{
			if	(logboolean)	CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
			close();
		}
		return retboolean;
	}

	/**
	 * 关闭连接
	 */
	public void close(){
		try	{
			if	(meta != null)	{
				//meta.close();
				meta=null;
			}
			if	(dbMetaData != null)	{
				//dbMetaData.close();
				dbMetaData=null;
			}
			if	(rst != null)	{
				rst.close();
				rst=null;
			}
			if	(stmt != null)	{
				stmt.close();
				stmt=null;
			}
			if	(prepstmt != null)	{
				prepstmt.close();
				prepstmt=null;
			}
			if	(prepstmt2!= null)	{
				prepstmt2.close();
				prepstmt2=null;
			}
			if	(conn != null && conn.getAutoCommit())	{
				//CLog.writeLog("conn.getAutoCommit="+conn.getAutoCommit());
				conn.close();
				conn=null;
			}
			//dataSource=null;
			//obInterface=null;
			indexcolname=null;
			where=null;
			sql=null;
		}	catch (Exception e)	{
			CLog.writeLog("Mysql close error: "+e);
		}
		//CLog.writeLog("Mysql close success");
		if	(logboolean)	CLog.writeLog("==Processing time:"+(System.currentTimeMillis() - stime)+" ms==");
	}

	public void	setAutoCommit(boolean flag)	{
		try	
		{
			conn.setAutoCommit(flag);
		}	
		catch	(SQLException e)	
		{
			e.printStackTrace();
			CLog.writeLog("Mysql setAutoCommit error: "+e);
		}
	}
	
	public boolean	getAutoCommit(){
		try	{
			retboolean=conn.getAutoCommit();
		}	catch	(SQLException e)	{
			CLog.writeLog("Mysql getAutoCommit error: "+e);
		}
		return retboolean;
	}
	public boolean Commit(){
		try	{
			//CLog.writeLog("Mysql Commit");
			conn.commit();
			return true;
		}	catch	(SQLException e)	{
			CLog.writeLog("Mysql Commit error: "+e);
		}
		return false;
	}

	public boolean Rollback(){
		try	{
			conn.rollback();
			CLog.writeLog("Mysql rollback");
			return true;
		}	catch(SQLException e){
			CLog.writeLog("Mysql Rollback error: "+e);
		}
		return false;
	}

	String typeToName(ResultSetMetaData meta,int num){
		String ret=null;
		try	{
			ret=typeToName(meta.getColumnTypeName(num),meta.getColumnType(num),meta.getColumnDisplaySize(num),meta.getPrecision(num),meta.getScale(num));
		}	catch(SQLException e){
		}
		return	ret;
	}

	String typeToName(String name,int i,int j,int m,int n){
		//i-getColumnType,j-getColumnDisplaySize,m-getPrecision,n-getScale
		String ret=name.trim();
		switch(i)
		{
		case(1):ret+="("+j+")";break;
		//case(2):ret="NUMERIC";break;
		case(2):ret="DECIMAL("+m+","+n+")";
		if	(n==0 ||m==0)	ret="INTEGER";
		break;
		case(3):ret="DECIMAL("+m+","+n+")";break;
		case(4):ret="INTEGER";break;
		case(5):ret="INTEGER";break;
		case(6):ret="DECIMAL("+m+","+n+")";break;
		case(8):ret="DECIMAL("+m+","+n+")";break;
		//case(12):ret+="("+j+")";break;
		//case(12):ret="VARCHAR";break;
		case(12):if	(j>255)	ret="VARCHAR("+j+")";	else	ret="CHAR("+j+")";break;
		case(91):ret="CHAR("+j+")";break;
		//case(91):ret="Date";break;
		default:ret+="("+j+")";break;
		}
		/*
	if	(3==i && 11>=j)	ret="Float";
	if	(2==i && 22==j && 0==m)	ret="Int";
	if	(2==i && 22==j && 0==n)	ret="Int";
	if	(2==i && 22==j && 4==n)	ret="Float";
		 */
		return ret;
	}

	//toHistory
	public boolean	toHistory(String tablename,String history)	{
		return	toHistory(tablename,history,null);
	}
	//toHistory
	public boolean	toHistory(String tablename,String history,String where)	{
		if	(tablename==null || tablename.length()==0)		return false;
		try {
			if	(where!=null && where.trim().length()>0 && where.trim().toLowerCase().indexOf("where")<0)		where=" WHERE "+where;
			if	(where==null)		where="";

			if	(conn==null)			init();
			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt!= null)		{prepstmt.close();prepstmt=null;}
			prepstmt=conn.prepareStatement("INSERT INTO "+history+" SELECT * FROM "+tablename.toUpperCase()+where);
			if	(prepstmt.executeUpdate()<=0)			return false;
			if	(prepstmt!= null)		{prepstmt.close();prepstmt=null;}
			prepstmt=conn.prepareStatement("DELETE FROM "+tablename.toUpperCase()+where);
			if	(prepstmt.executeUpdate()<=0)	return false;
		}	catch	(SQLException e)	{
			return false;
		}
		return true;
	}

	//get table name list
	public String	getTableList(String where)	{
		return getTableList(dbname,where);
	}
	//get table name list
	public String	getTableList(String dbname,String where)	{
		String	result="";

		try	{
			if	(conn==null)					init();
			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}


			dbMetaData=conn.getMetaData();
			String [] tabletype={"TABLE"};
			rst=dbMetaData.getTables(dbname,null,null,tabletype);
			//rst=dbMetaData.getTables(null,"%", "t%",tabletype);
			//CLog.writeLog("dbname=["+dbname+"],where["+where+"]");

			String	mytable=null;
			while(rst.next())	{
				//CLog.writeLog(rst.getString(1)+","+rst.getString(2)+","+rst.getString(3)+","+rst.getString(4)+","+rst.getString(5));
				//1-null,2-数据库名,3-表名,4-类型
				//mytable=TransFirstUpperCase(rst.getString(app.getString("label.CustomerName")));
				//if	(dbname!=null && dbname.length()>1 && rst.getString(1)!=null && !rst.getString(1).trim().equalsIgnoreCase(dbname))					continue;
				//if	(dbname!=null && dbname.length()>1 && rst.getString(2)!=null && !rst.getString(2).trim().equalsIgnoreCase(dbname))					continue;
				if	(dbname!=null && dbname.length()>1 && !(rst.getString(1)!=null && rst.getString(1).trim().equalsIgnoreCase(dbname) || rst.getString(2)!=null && rst.getString(2).trim().equalsIgnoreCase(dbname)))					continue;
				//----------------------剔除杂七杂八的表名------------------------
				if	(rst.getString(3).length()>22)					continue;
				//----------------------剔除杂七杂八的表名------------------------
				mytable=rst.getString(3).trim().toUpperCase();
				if	(where!=null && where.length()>0){
					for	(int i=0;i<where.split("\\,").length;i++){
						String	mywhere=where.split("\\,")[i];
						if	(mywhere!=null && mywhere.trim().length()>0 && mytable.indexOf(mywhere.trim().toUpperCase())>=0){
							if	(result!=null && result.length()>0)			result+=",";
							result+=mytable;
							break;
						}
					}
				}	else	{
					if	(result!=null && result.length()>0)			result+=",";
					result+=mytable;
				}
				//CLog.writeLog(rst.getString(1)+","+rst.getString(2)+","+rst.getString(3)+","+rst.getString(4)+","+rst.getString(5));
			}
		}	catch	(SQLException e)	{
			CLog.writeLog("e="+e);
			CLog.writeLog(",errorcode="+e.getErrorCode());
			CLog.writeLog(",getMessage="+e.getMessage());
			CLog.writeLog(",Exception="+e.toString());
		}


		return result;

	}


	//get table
	public String	getTable(String tablename)	{
		return	getTable(tablename,null);
	}
	//get table
	public String	getTable(String tablename,String filename)	{
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",tablename=["+tablename+"]...");
		String	mysemicolon=";";
		String	result="";
		String	indexname=null;
		if	(tablename==null || tablename.length()<=0)	return result;
		if	(dbtype!=null && dbtype.toLowerCase().indexOf("server")>=0)		mysemicolon="";
		tablename=tablename.toUpperCase();
		try	{
			if	(conn==null)					init();
			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			prepstmt=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase());
			rst=prepstmt.executeQuery();

			meta=rst.getMetaData();
			colnum=meta.getColumnCount();
			result+="prompt Dropping "+tablename+"...\n";
			result+="drop		table	"+tablename+mysemicolon+"\n";
			result+="create	table	"+tablename+"\n";
			result+="(\n";
			for	(i=1;i<=colnum;i++){
				if	(i>1)	result+=",\n";
				result+="	"+meta.getColumnName(i)+"	"+typeToName(meta,i);
				if	(meta.isNullable(i)==0)	result+=" not null";
				//result+="|"+meta.isNullable(i)+","+meta.getColumnTypeName(i)+","+meta.getColumnType(i)+","+meta.getColumnDisplaySize(i)+","+meta.getPrecision(i)+","+meta.getScale(i)+"|";
			}
			result+="\n)"+mysemicolon+"\n";


			dbMetaData=conn.getMetaData();
			String indexlist="";
			String unique="unique";
			/*
			if	(rst != null)		{rst.close();rst=null;}
			rst= dbMetaData.getIndexInfo(null,null,tablename,true,false);
			for	(int j=0;rst.next();){
				if	(rst.getString("INDEX_NAME")==null)	continue;
				//if	(indexname!=null && !rst.getString("INDEX_NAME").equals(indexname))	break;
				if	(indexname!=null && !rst.getString("INDEX_NAME").equals(indexname)){
					result+="create "+unique+" index "+indexname+" on "+tablename+" ("+indexlist+");\n";
					indexlist="";
					j=0;
				}
				indexname=rst.getString("INDEX_NAME");
				if	(j>0)	indexlist+=",";
				indexlist+=rst.getString("COLUMN_NAME").toUpperCase();
				//sql+=indexcolname[j]+" = ? ";
				indexcolnum=j+1;
				j++;
			}
			result+="create "+unique+" index "+indexname+" on "+tablename+" ("+indexlist+")"+mysemicolon+"\n";
			 */
			if	(rst != null)					{rst.close();rst=null;}
			rst= dbMetaData.getIndexInfo(null,null,tablename,false,false);
			indexlist="";
			unique="";
			indexname=null;
			for	(int j=0;rst.next();){
				if	(rst.getString("INDEX_NAME")==null)	continue;
				//if	(indexname!=null && !rst.getString("INDEX_NAME").equals(indexname))	break;
				if	(indexname!=null && !rst.getString("INDEX_NAME").equals(indexname)){
					result+="create "+unique+" index "+indexname+" on "+tablename+" ("+indexlist+")"+mysemicolon+"\n";
					indexlist="";
					j=0;
				}
				if	(rst.getBoolean("NON_UNIQUE"))	unique="";	else	unique="unique";
				indexname=rst.getString("INDEX_NAME");
				if	(j>0)	indexlist+=",";
				indexlist+=rst.getString("COLUMN_NAME").toUpperCase();
				//sql+=indexcolname[j]+" = ? ";
				indexcolnum=j+1;
				j++;
			}
			if	(indexname!=null)	result+="create "+unique+" index "+indexname+" on "+tablename+" ("+indexlist+")"+mysemicolon+"\n";

			//PRIMARY KEY
			if	(rst != null)					{rst.close();rst=null;}
			rst= dbMetaData.getPrimaryKeys(null, null,tablename);

			indexlist="";
			unique="";
			indexname=null;
			indexcolnum=rst.getMetaData().getColumnCount();
			for	(;rst.next();){
				if	(!rst.getString("TABLE_NAME").equalsIgnoreCase(tablename))	continue;
				if	(indexlist.length()>0)	indexlist+=",";
				indexlist+=rst.getString("COLUMN_NAME");
				indexname=rst.getString("PK_NAME");
				indexcolnum=rst.getShort("KEY_SEQ");
			}
			if	(indexlist.length()>0)	result+=indexname+" PRIMARY KEY ("+indexlist+")"+mysemicolon+"\n";

			//FOREIGN KEY
			if	(rst != null)					{rst.close();rst=null;}
			rst= dbMetaData.getExportedKeys(null, null,tablename);

			indexlist="";
			unique="";
			indexname=null;
			String pk_name=null;
			for	(;rst.next();){
				if	(!rst.getString("PKTABLE_NAME").equalsIgnoreCase(tablename))	continue;
				if	(indexlist.length()>0)	indexlist+=",";
				indexlist+=rst.getString("PKCOLUMN_NAME").trim();
				indexname=rst.getString("FKTABLE_NAME").trim();
				if	(unique.length()>0)	unique+=",";
				unique+=rst.getString("FKCOLUMN_NAME").trim();
				pk_name=rst.getString("PK_NAME").trim();
			}
			if	(indexlist.length()>0)	result+="CONSTRAINT "+pk_name+" FOREIGN KEY ("+indexlist+") REFERENCES "+indexname+" ("+unique+") "+mysemicolon+"\n";

			if	(dbtype!=null && dbtype.toLowerCase().indexOf("server")>=0)		result+="\ngo\n";	else	result+="\ncommit;\n";
		}	catch	(SQLException e)	{
			CLog.writeLog("e="+e);
			CLog.writeLog(",errorcode="+e.getErrorCode());
			CLog.writeLog(",getMessage="+e.getMessage());
			CLog.writeLog(",Exception="+e.toString());
			//e.printStackTrace();
		}	catch	(Exception e)	{
			CLog.writeLog(e.toString());
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",tablename=["+tablename+"]");
			close();
		}
		try	{
			FileOutputStream fos = new FileOutputStream(filename,true);
			PrintWriter pw =new PrintWriter(new OutputStreamWriter(fos,myencode));
			//PrintWriter pw = new PrintWriter(fos);
			pw.println(result);
			pw.close();
			fos.close();
			pw=null;
			fos=null;
		}	catch	(Exception e)	{
		}

		if	(logboolean)	CLog.writeLog("==Processing time:"+(System.currentTimeMillis() - stime)+" ms==");
		return result;
	}


	//get table Data
	public String	getTableData(String tablename)	{
		return	getTableData(tablename,null,null,null);
	}
	//get table Data
	public String	getTableData(String tablename,String filename)	{
		return	getTableData(tablename,null,filename,null);
	}
	//get table Data
	public String	getTableData(String tablename,String where,String filename)	{
		return	getTableData(tablename,where,filename,null);
	}
	//get table data
	public String	getTableData(String tablename,String where,String filename,String separator)	{
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",tablename=["+tablename+"],where=["+where+"],filename=["+filename+"],separator="+separator);
		String	Data="";

		if	(tablename==null || tablename.length()<=0)	return Data;
		if	(separator==null)														separator="|";
		tablename=tablename.toUpperCase();
		FileOutputStream fos=null;
		PrintWriter pw=null;
		try	{
			fos = new FileOutputStream(filename,true);
			pw =new PrintWriter(new OutputStreamWriter(fos,myencode));
			//pw = new PrintWriter(fos);
		}	catch	(Exception e)	{
			fos=null;
			pw=null;
		}
		int j=0;
		try	{
			if	(conn==null)					init();
			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			if	(where!=null && where.trim().length()>0 && where.trim().toLowerCase().indexOf("where")<0)		where=" WHERE "+where;
			if	(where==null)		where="";
			prepstmt=conn.prepareStatement("SELECT * FROM "+tablename);
			rst=prepstmt.executeQuery();

			meta=rst.getMetaData();
			colnum=meta.getColumnCount();

			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt2 != null)		{prepstmt2.close();prepstmt2=null;}
			prepstmt2=conn.prepareStatement("SELECT * FROM "+tablename+where);
			rst=prepstmt2.executeQuery();

			//rst.next();
			String	myvalue="";
			//for	(j=0;j<obInterface.getPageSize()&&rst.next();j++){
			for	(j=0;rst.next();j++){
				for	(i=1;i<=colnum;i++){
					//if	(j==0)	CLog.writeLog("i=["+i+"],"+meta.getColumnTypeName(i)+","+j);

					if	(i>1)	Data+=separator;
					if	(meta.getColumnTypeName(i).toLowerCase().indexOf("char")>=0){
						myvalue=rst.getString(i);
						if	(myvalue==null)			Data+=myvalue;
						else	{
							myvalue=myvalue.trim();
							if	(myvalue.length()<=0)	myvalue=" ";
							//Data+="'"+myvalue+"'";
							Data+=myvalue;
						}
						continue;
					}
					//		else	Data+=rst.getString(i);
					if	(meta.getColumnTypeName(i).equalsIgnoreCase("number")){
						//if	(meta.getPrecision(i)==0 || meta.getScale(i)==0)	{Data+=rst.getInt(i);continue;}
						if	(meta.getScale(i)>=1)	{Data+=rst.getDouble(i);continue;}
						Data+=rst.getString(i);
						continue;
					}
					Data+=rst.getString(i);
				}

				//Data+="\n";

				if	(pw!=null){
					pw.println(Data);
					Data="";
				}	else	Data+="\n";
			}

		}	catch	(SQLException e)	{
			CLog.writeLog("e="+e);
			CLog.writeLog(",errorcode="+e.getErrorCode());
			CLog.writeLog(",getMessage="+e.getMessage());
			CLog.writeLog(",Exception="+e.toString());
			//e.printStackTrace();
			CLog.writeLog("i=["+i+"],"+j);
		}	catch	(Exception e)	{
			CLog.writeLog(e.toString());
			CLog.writeLog("i=["+i+"],"+j);
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",tablename=["+tablename+"],where=["+where+"],"+colnum);
			close();
		}

		if	(pw!=null){
			try {
				pw.println(Data);
				pw.close();
				fos.close();
				Data=null;
			}	catch	(Exception e)	{
			}
		}
		if	(logboolean)	CLog.writeLog("==Processing time:"+(System.currentTimeMillis() - stime)+" ms==");
		return Data;
	}


	//get table result
	public String	getTableResult(String tablename)	{
		return	getTableResult(tablename,null,null);
	}
	//get table result
	public String	getTableResult(String tablename,String filename)	{
		return	getTableResult(tablename,null,filename);
	}
	public String	getTableResult(String tablename,String where,String filename)	{
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",tablename=["+tablename+"]...");
		String	mysemicolon=";";
		String	result="";
		String	insertinto=null;
		if	(tablename==null || tablename.length()<=0)	return result;
		if	(dbtype!=null && dbtype.toLowerCase().indexOf("server")>=0)		mysemicolon="";
		tablename=tablename.toUpperCase();
		FileOutputStream fos=null;
		PrintWriter pw=null;
		try	{
			fos = new FileOutputStream(filename,true);
			pw =new PrintWriter(new OutputStreamWriter(fos,myencode));
			//pw = new PrintWriter(fos);
		}	catch	(Exception e)	{
			fos=null;
			pw=null;
		}
		//result="set feedback off\n";
		//result+="set define off\n";
		int j=0;
		try	{
			if	(conn==null)					init();
			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			if	(where!=null && where.trim().length()>0 && where.trim().toLowerCase().indexOf("where")<0)		where=" WHERE "+where;
			if	(where==null)		where="";
			prepstmt=conn.prepareStatement("SELECT * FROM "+tablename);
			rst=prepstmt.executeQuery();

			meta=rst.getMetaData();
			colnum=meta.getColumnCount();
			/*
			insertinto="insert into "+tablename+" (";
			for	(i=1;i<=colnum;i++){
				if	(i>1)	insertinto+=", ";
				insertinto+=meta.getColumnName(i);
			}
			insertinto+=")\nvalues (";*/
			insertinto="insert into "+tablename+" values (";


			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt2 != null)		{prepstmt2.close();prepstmt2=null;}
			prepstmt2=conn.prepareStatement("SELECT * FROM "+tablename+where);
			rst=prepstmt2.executeQuery();
			result+="prompt Loading "+tablename+"...\n";
			//rst.next();
			String	myvalue="";
			//for	(j=0;j<obInterface.getPageSize()&&rst.next();j++){
			for	(j=0;rst.next();j++){
				result+=insertinto;
				for	(i=1;i<=colnum;i++){
					if	(i>1)	result+=",";
					if	(meta.getColumnTypeName(i).toLowerCase().indexOf("char")>=0){
						myvalue=rst.getString(i);
						if	(myvalue==null)			result+=myvalue;
						else	{
							myvalue=myvalue.trim();
							if	(myvalue.length()<=0)	myvalue=" ";
							result+="'"+myvalue+"'";
						}
						continue;
					}
					//		else	result+=rst.getString(i);
					if	(meta.getColumnTypeName(i).equalsIgnoreCase("number")){
						//if	(meta.getPrecision(i)==0 || meta.getScale(i)==0)	{result+=rst.getInt(i);continue;}
						if	(meta.getScale(i)>=1)	{result+=rst.getDouble(i);continue;}
						result+=rst.getString(i);
						continue;
					}
					result+=rst.getString(i);
				}

				result+=")"+mysemicolon+"\n";
				if	((j%200)==199){
					if	(dbtype!=null && dbtype.toLowerCase().indexOf("server")>=0)		result+="\ngo\n";	else	result+="\ncommit;\n";
				}
				if	(pw!=null){
					pw.println(result);
					result="";
				}
			}

			if	(dbtype!=null && dbtype.toLowerCase().indexOf("server")>=0)		result+="\ngo\n";	else	result+="\ncommit;\n";
			result+="prompt "+j+" records loaded into "+tablename+"\n";
			//result+="set feedback on\n";
			//result+="set define on\n";

		}	catch	(SQLException e)	{
			CLog.writeLog("e="+e);
			CLog.writeLog(",errorcode="+e.getErrorCode());
			CLog.writeLog(",getMessage="+e.getMessage());
			CLog.writeLog(",Exception="+e.toString());
			//e.printStackTrace();
			CLog.writeLog("i=["+i+"],"+j);
		}	catch	(Exception e)	{
			CLog.writeLog(e.toString());
			CLog.writeLog("i=["+i+"],"+j);
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",tablename=["+tablename+"]"+colnum);
			close();
		}

		if	(pw!=null){
			try {
				pw.println(result);
				pw.close();
				fos.close();
				result=null;
			}	catch	(Exception e)	{
			}
		}
		if	(logboolean)	CLog.writeLog("==Processing time:"+(System.currentTimeMillis() - stime)+" ms==");
		return result;
	}

	//单一查询
	public boolean	existTable(String tablename)	{
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",tablename=["+tablename+"]...");
		try	{
			if	(conn==null)					init();
			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			prepstmt=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase());
			rst=prepstmt.executeQuery();
			retboolean=true;
			return retboolean;
		}	catch	(SQLException e)	{
			retboolean=false;
			return retboolean;
		}	catch	(Exception e)	{
			retboolean=false;
			return retboolean;
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",tablename=["+tablename+"]"+retboolean);
			close();
		}
	}


	private boolean	getValueFromMetaRst(ResultSetMetaData meta,ResultSet rst) throws Exception	{
		this.meta=meta;
		if	(this.rst != null)	this.rst.close();
		this.rst=rst;
		return getValueFromMetaRst();
	}


	//
	private boolean	getValueFromMetaRst() throws SQLException	{
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",conum=["+meta.getColumnCount()+"]...");
		try		{
			meta=rst.getMetaData();
			colnum=meta.getColumnCount();
			if	(!rst.next())		throw new SQLException("No ResultSet");

			for	(int j=1;j<colnum+1;j++)	{
				try{
					//CLog.writeLog("========colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]"+getVarType(obInterface,meta.getColumnName(j)));
					//CLog.writeLog("setFieldByName("+meta.getColumnName(j).toLowerCase()+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
					//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("Date"))

					if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("date")>=0)
					{
						obInterface.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
						continue;
					}
					//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("String"))
					if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("string")>=0)
					{
						obInterface.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getString(meta.getColumnName(j)));
						continue;
					}
					//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("int"))
					if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("int")>=0)
					{
						obInterface.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getInt(meta.getColumnName(j)));
						continue;
					}
					//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("double"))
					if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("double")>=0)
					{
						obInterface.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDouble(meta.getColumnName(j)));
						continue;
					}
					//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("float"))
					if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("float")>=0)
					{
						obInterface.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getFloat(meta.getColumnName(j)));
						continue;
					}


					/*
					 */
					if	(meta.getColumnType(j)==911)
					{
						obInterface.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
						continue;
					}
					if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("String"))
					{
						obInterface.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getString(meta.getColumnName(j)));
						continue;
					}
					if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("int"))
					{
						obInterface.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getInt(meta.getColumnName(j)));
						//obInterface.setFieldValue(meta.getColumnName(j),""+rst.getInt(meta.getColumnName(j)));
						continue;
					}
					if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("double"))
					{
						obInterface.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDouble(meta.getColumnName(j)));
						continue;
					}
					if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("float"))
					{
						obInterface.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getFloat(meta.getColumnName(j)));
						continue;
					}

				}	catch	(Exception e)	{
					CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]");
					CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
					CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
				}
			}
		}	catch	(SQLException e)	{
			CLog.writeLog("e="+e);
			CLog.writeLog(",errorcode="+e.getErrorCode());
			CLog.writeLog(",getMessage="+e.getMessage());
			CLog.writeLog(",Exception="+e.toString());
			//e.printStackTrace();
			CLog.writeLog("sql=["+sql+"]"+colnum);
			CLog.writeLog("where="+where);
			//throw e;
			return false;
			//}	catch(Exception e)	{
			//	CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			//throw new SQLException();
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+",conum=["+meta.getColumnCount()+"]...");
			close();
		}
		return true;
	}


	//单一查询
	public boolean	getRowSql(String sql) throws Exception	{
		//if	(mysql==null)		mysql=new Mysql(sql); else		mysql.prepareStatement(sql);
		if	(sql==null || sql.length()<=0)		return	false;

		try {
			if	(conn==null)		{
				init();
			}

			if	(prepstmt != null)		{
				prepstmt.close();prepstmt=null;
			}
			if	(rst != null)					{rst.close();rst=null;}
			//prepstmt=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			prepstmt=conn.prepareStatement(sql);
			rst=prepstmt.executeQuery();
			meta=rst.getMetaData();
			retboolean=getValueFromMetaRst();
		}	catch	(SQLException e)	{
			CLog.writeLog("e="+e);
			CLog.writeLog(",errorcode="+e.getErrorCode());
			CLog.writeLog(",getMessage="+e.getMessage());
			CLog.writeLog(",Exception="+e.toString());
			//e.printStackTrace();
			throw e;
		}	catch(Exception e)	{
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			throw e;
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+sql);
			close();
		}
		if	(logboolean)	CLog.writeLog("==Processing time:"+(System.currentTimeMillis() - stime)+" ms==");
		if	(!retboolean)		throw	new Exception();
		return retboolean;
	}

	//单一查询
	public boolean		getRow(String tablename) throws Exception	{
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",tablename=["+tablename+"]...");
		indexcolname=new String[30];
		if	(tablename==null || tablename.length()==0)		return false;
		try		{
			if	(conn==null)			init();

			//stmt=conn.createStatement();
			//meta=stmt.executeQuery("SELECT * FROM	"+tablename.toUpperCase()).getMetaData();

			if	(prepstmt2!= null)		{prepstmt2.close();prepstmt2=null;}
			if	(rst != null)					{rst.close();rst=null;}
			prepstmt2=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase());
			rst=prepstmt2.executeQuery();
			meta=rst.getMetaData();
			colnum=meta.getColumnCount();

			//if	(dbMetaData != null)	{dbMetaData=null;}
			//if	(meta != null)				{meta=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			if	(stmt != null)				{stmt.close();stmt=null;}
			if	(rst != null)					{rst.close();rst=null;}

			dbMetaData=conn.getMetaData();

			//CLog.writeLog(sql="SELECT * FROM "+tablename.toUpperCase()+" WHERE ");
			//	System.out.println("***"+new Date());
			rst= dbMetaData.getIndexInfo(null,null,tablename.toUpperCase(),true,false);
			//	System.out.println("***"+new Date());
			sql="";

			String indexname=null;
			String symbolstr="";
			indexcolnum=0;
			for	(int j=0;rst.next();){
				CLog.writeLog(j+",colnum["+rst.getInt("ORDINAL_POSITION")+"],INDEX_NAME["+rst.getString("INDEX_NAME")+"],COLUMN_NAME["+rst.getString("COLUMN_NAME"));
				//if	(j>0 && rst.getInt("ORDINAL_POSITION")==1)	break;

				if	(rst.getBoolean("NON_UNIQUE"))	continue;
				if	(rst.getString("INDEX_NAME")==null)	continue;
				if	(indexname!=null && !rst.getString("INDEX_NAME").equals(indexname))	break;
				indexname=rst.getString("INDEX_NAME");
				if	(j>0)	sql+=" AND ";
				//indexcolname[j]=rst.getString("COLUMN_NAME").toLowerCase();
				indexcolname[j]=rst.getString("COLUMN_NAME");
				if	(getVarType(obInterface,indexcolname[j]).toLowerCase().indexOf("string")>=0){
					symbolstr="'";
					/**/
					for	(int m=1;m<colnum+1;m++){
						//if	(meta.getColumnName(m).toLowerCase().equals(indexcolname[j]) && meta.getColumnTypeName(m).toUpperCase().equals("INTEGER"))	symbolstr="";
						//CLog.writeLog("colnum["+m+"/"+colnum+"],INDEX_NAME["+meta.getColumnName(m)+"/"+meta.getColumnName(m).toLowerCase()+"],type["+meta.getColumnTypeName(m)+"]");
						if	(meta.getColumnName(m).toLowerCase().equals(indexcolname[j].toLowerCase()))	{
							if	(meta.getColumnTypeName(m).toLowerCase().indexOf("char")<0)	symbolstr="";
							//CLog.writeLog("colnum["+m+"/"+colnum+"],INDEX_NAME["+meta.getColumnName(m)+"],type["+meta.getColumnTypeName(m)+"]");
							break;
						}
					}
				}	else	symbolstr="";
				//String myvalue=myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j].toLowerCase())).toString();
				//String myvalue=obInterface.getFieldValue(indexcolname[j].toLowerCase()).toString();
				String myvalue=obInterface.getString(indexcolname[j].toLowerCase());
				if	(myvalue==null || myvalue.length()<=0)	myvalue=" ";
				sql+=indexcolname[j]+" = "+symbolstr+myvalue+symbolstr;
				indexcolnum=j+1;
				j++;
				//sql+=myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j].toLowerCase()));
			}

			//PRIMARY KEY
			if	(indexcolnum==0){
				if	(rst != null)					{rst.close();rst=null;}
				rst= dbMetaData.getPrimaryKeys(null, null,tablename);

				for	(int j=0;rst.next();){
					if	(!rst.getString("TABLE_NAME").equalsIgnoreCase(tablename))	continue;

					indexname=rst.getString("COLUMN_NAME");
					if	(getVarType(obInterface,indexname).toLowerCase().indexOf("string")>=0)	symbolstr="'";	else	symbolstr="";
					if	(j>0)	sql+=" AND ";
					String myvalue=obInterface.getString(indexname.toLowerCase());
					if	(myvalue==null || myvalue.length()<=0)	myvalue=" ";
					sql+=indexname+" = "+symbolstr+myvalue+symbolstr;
					indexcolnum=rst.getShort("KEY_SEQ");
				}
			}


			if	(rst != null)		{rst.close();rst=null;}

			try	{
				if	(rst != null)					{rst.close();rst=null;}
				if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
				//prepstmt=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase()+" WHERE "+sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				//CLog.writeLog("sql="+"SELECT * FROM "+tablename.toUpperCase()+" WHERE "+sql);
				prepstmt=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase()+" WHERE "+sql);
				rst=prepstmt.executeQuery();
				retboolean=getValueFromMetaRst();
			}	catch	(SQLException e)	{
				if	(rst != null)					{rst.close();rst=null;}
				if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
				if	(conn==null)			init();
				prepstmt=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase()+"H WHERE "+sql);
				//CLog.writeLog("sql="+"SELECT * FROM "+tablename.toUpperCase()+"H WHERE "+sql);
				rst=prepstmt.executeQuery();
				getValueFromMetaRst();
				tablename+="H";
			}
		}	catch	(SQLException e)	{
			CLog.writeLog("e="+e);
			CLog.writeLog(",errorcode="+e.getErrorCode());
			CLog.writeLog(",getMessage="+e.getMessage());
			CLog.writeLog(",Exception="+e.toString());
			//e.printStackTrace();
			CLog.writeLog("sql=["+sql+"]"+colnum);
			CLog.writeLog("where="+where);
			throw e;
		}	catch(Exception e)	{
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			throw e;
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+tablename);
			close();
		}
		if	(logboolean)	CLog.writeLog("==Processing time:"+(System.currentTimeMillis() - stime)+" ms==");
		if	(!retboolean)		throw	new Exception();
		return retboolean;
	}


	//单一查询
	public boolean		getRow(String tablename,String where) throws Exception	{
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+",tablename=["+tablename+"],where=["+where+"...");
		if	(where!=null && where.trim().length()>0 && where.trim().toLowerCase().indexOf("where")<0)		where=" WHERE "+where;
		if	(tablename==null || tablename.length()==0)		return false;
		try {
			if	(conn==null)			init();
			//prepstmt=conn.prepareStatement("SELECT * FROM	"+tablename);
			//rst=prepstmt.executeQuery();
			//if	(rst.next())				meta=rst.getMetaData();
			//stmt=conn.createStatement();
			//rst=stmt.executeQuery("SELECT * FROM	"+tablename);
			//meta=rst.getMetaData();
			//meta=executeQuery("SELECT * FROM	"+tablename.toUpperCase()).getMetaData();
			//colnum=meta.getColumnCount();

			if	(prepstmt2!= null)		{prepstmt2.close();prepstmt2=null;}
			if	(rst != null)					{rst.close();rst=null;}
			prepstmt2=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase());
			rst=prepstmt2.executeQuery();
			meta=rst.getMetaData();
			colnum=meta.getColumnCount();


			try	{
				if	(rst != null)					{rst.close();rst=null;}
				if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
				prepstmt=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase()+" "+where);
				rst=prepstmt.executeQuery();
				retboolean=getValueFromMetaRst();
			}	catch	(SQLException e)	{
				if	(rst != null)					{rst.close();rst=null;}
				if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
				if	(conn==null)			init();
				prepstmt=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase()+"H "+where);
				rst=prepstmt.executeQuery();
				getValueFromMetaRst();
				tablename+="H";
			}
		}	catch	(SQLException e)	{
			CLog.writeLog("e="+e);
			CLog.writeLog(",errorcode="+e.getErrorCode());
			CLog.writeLog(",getMessage="+e.getMessage());
			CLog.writeLog(",Exception="+e.toString());
			//e.printStackTrace();
			CLog.writeLog("sql=["+sql+"]");
			CLog.writeLog("where="+where);
			throw e;
		}	catch(Exception e)	{
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			throw e;
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+tablename+":"+where);
			close();
		}
		return retboolean;
	}

	//批量查询
	public Collection	select2(String tablename)	{
		return select2(tablename,obInterface.getWhere());
	}

	//批量查询
	public Collection	select2(String tablename,String where) {
		CLog.writeLog("where["+where+"]");
		String	orderby="";
		String	defaultorderby=" ORDER BY 1,2";
		indexcolname=new String[30];
		if	(where!=null && where.length()>0 && where.trim().toLowerCase().indexOf("order")!=0){
			if	(where!=null && where.trim().length()>0 && (where.trim().toLowerCase().indexOf("where")<0 || where.trim().toLowerCase().indexOf("where")>9))		where=" WHERE "+where;
		}
		if	(where!=null && where.length()>0 && where.trim().toLowerCase().indexOf("order")==0){
			orderby=" "+where;
			where="";
		}
		if	(where!=null && where.length()>0 && where.trim().toLowerCase().indexOf("order")>0){
			orderby=where.substring(where.toLowerCase().indexOf("order"));
			where=where.substring(0,where.toLowerCase().indexOf("order"));
		}
		where=" "+where;

		Date date=new Date();
		Calendar  cal = Calendar.getInstance();
		cal.getTime();
		//date.getTime();

		SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd");
		Date begin = new Date();

		begin.setDate(begin.getDate()-7);
		SimpleDateFormat  formate = new SimpleDateFormat("yyyy/MM/dd");
		String begintime=formate.format(begin);



		//and C_TIME >= '2011/11/03 0' and C_TIME <= '2011/11/10AA'
		if("".equals(where.trim())){
			where=where+"where C_TIME >='"+begintime+"' and C_TIME <='"+df.format(date)+"'";			
		}

		Collection ret=new ArrayList();
		if	(tablename==null || tablename.length()==0)		return ret;
		try		{
			if	(conn==null)			init();

			if	(prepstmt2!= null)		{prepstmt2.close();prepstmt2=null;}
			if	(rst != null)					{rst.close();rst=null;}
			prepstmt2=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase());
			rst=prepstmt2.executeQuery();
			meta=rst.getMetaData();
			colnum=meta.getColumnCount();


			try	{
				if	(prepstmt != null)		{prepstmt.close();stmt=null;}
				if	(rst != null)					{rst.close();rst=null;}

				prepstmt=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase()+"H");
				rst=prepstmt.executeQuery();
				//meta=rst.getMetaData();

				if	(colnum!=rst.getMetaData().getColumnCount()) throw new SQLException();
				//executeQuery("select * from	"+tablename.toUpperCase()+"H");
				sql="SELECT COUNT(*) FROM "+tablename.toUpperCase()+" "+where+" UNION ALL  SELECT COUNT(*) FROM "+tablename.trim().toUpperCase()+"H "+where;;
			}	catch	(SQLException e)	{
				sql="SELECT COUNT(*) FROM "+tablename.toUpperCase()+" "+where;
			}
			//CLog.writeLog("sql["+sql+"]");
			//prepstmt=conn.prepareStatement(sql);
			//rst=prepstmt.executeQuery();

			//stmt=conn.createStatement();
			//rst=stmt.executeQuery(sql);
			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			prepstmt=conn.prepareStatement(sql);
			rst=prepstmt.executeQuery();



			retNum=0;
			for	(;rst.next();)				retNum+=rst.getInt(1);
			//if	(rst.next())				retNum+=rst.getInt(1);
			//if	(rst.next())				retNum+=rst.getInt(1);
			//CLog.writeLog("sql["+sql+"]"+retNum);

			obInterface.setRetNum(retNum);
			int maxPage=retNum/obInterface.getPageSize();
			maxPage+= (retNum%obInterface.getPageSize())==0?0:1;
			int curPage=obInterface.getPageNo();
			int pageSize=obInterface.getPageSize();

			if	((curPage < 1) || (curPage > maxPage)) curPage=1;
			if	(curPage>maxPage)		maxPage=curPage;

			obInterface.setPageNo(curPage);
			obInterface.setMaxPage(maxPage);
			obInterface.setRowNum(0);
			if	(retNum<=0)	return ret;

			//prepstmt=conn.prepareStatement("SELECT * FROM	"+tablename.toUpperCase());
			//rst=prepstmt.executeQuery();
			//if	(rst.next())	meta=rst.getMetaData();

			//stmt=conn.createStatement();
			//meta=stmt.executeQuery("SELECT * FROM	"+tablename.toUpperCase()).getMetaData();


			if	(colnum==1)	defaultorderby=" ORDER BY 1";
			if	(orderby.length()<=0)	orderby=defaultorderby;
			/*
			if	(orderby.length()<=0){
				orderby+=" ORDER BY ";
				try	{
						dbMetaData=conn.getMetaData();
						if	(rst != null)		{rst.close();rst=null;}
						rst= dbMetaData.getIndexInfo(null,null,tablename.toUpperCase(),true,false);
						String indexname=null;
						for	(int j=0;rst.next();){
							//CLog.writeLog("colnum["+rst.getInt("ORDINAL_POSITION")+"]"+rst.getString("INDEX_NAME"));
							//if	(j>0 && rst.getInt("ORDINAL_POSITION")==1)	break;
							//if	(j>0)	sql+=",";
							//sql+=rst.getString("COLUMN_NAME");

							//CLog.writeLog(j+",colnum["+rst.getInt("ORDINAL_POSITION")+"],INDEX_NAME["+rst.getString("INDEX_NAME")+"],COLUMN_NAME["+rst.getString("COLUMN_NAME"));
							//if	(j>0 && rst.getInt("ORDINAL_POSITION")==1)	break;

							if	(rst.getString("INDEX_NAME")==null)	continue;
							if	(indexname!=null && !rst.getString("INDEX_NAME").equals(indexname))	break;
							indexname=rst.getString("INDEX_NAME");
							indexcolname[j]=rst.getString("COLUMN_NAME").toLowerCase();
							if	(j>0)	orderby+=",";
							//orderby+=indexcolname[j];
							orderby+=(j+1);
							j++;
							//sql+=myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j].toLowerCase()));


						}
						//CLog.writeLog("sql=["+sql+"],indexcolnum=["+indexcolnum+"],tablename="+tablename);
				}	catch(Exception e)	{
						CLog.writeLog(e.toString());
						CLog.writeLog("sql=["+sql+"],indexcolnum=["+indexcolnum+"],tablename="+tablename);
				}	finally	{
						if	(j>=colnum)	orderby=defaultorderby;
				}
			}
			 */
			try	{
				if	(rst != null)					{rst.close();rst=null;}
				if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
				prepstmt=conn.prepareStatement("select * from	"+tablename.toUpperCase()+"H");
				rst=prepstmt.executeQuery();
				if	(colnum!=rst.getMetaData().getColumnCount()) throw new SQLException();
				sql="SELECT * FROM "+tablename.toUpperCase()+" "+where+" UNION ALL "+"SELECT * FROM "+tablename.trim().toUpperCase()+"H "+where+orderby;
				tablename+="H";
			}	catch	(SQLException e)	{
				sql="SELECT * FROM "+tablename.toUpperCase()+" "+where+orderby;
			}

			//CLog.writeLog("sql=["+sql+"],retNum="+retNum+",indexname="+indexcolnum);
			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			prepstmt=conn.prepareStatement(sql);
			rst=prepstmt.executeQuery();

			for	(i=0;i<(curPage-1)*pageSize;i++)		rst.next();

			for	(i=(curPage-1)*pageSize;(i<=curPage*pageSize-1)&&(i<=retNum-1);i++)			{
				rst.next();
				ObInterface temp=new ObInterface();
				temp.setSerialNo(i+1);
				temp.setRetNum(retNum);

				for	(int j=1;j<colnum+1;j++)
				{
					//CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]");
					//CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
					//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("Date"))
					try{
						if	(meta.getColumnType(j)==91)	{
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
							continue;
						}
						/*
						if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("date")>=0){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
							continue;
						}*/

						if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("String")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getString(meta.getColumnName(j)));
							continue;
						}
						if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("int")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getInt(meta.getColumnName(j)));
							continue;
						}
						if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("double")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDouble(meta.getColumnName(j)));
							continue;
						}
						if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("float")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getFloat(meta.getColumnName(j)));
							continue;
						}
					}	catch	(Exception e)	{
						CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]");
						CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
						CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
					}
				}

				ret.add(temp);
			}
			obInterface.setRowNum(i-(curPage-1)*pageSize);

		}	catch(Exception e)	{
			CLog.writeLog("sql=["+sql+"]");
			CLog.writeLog("where="+where);
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			//throw e;
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+tablename+"("+obInterface.getRowNum()+"/"+retNum+"),"+where.trim()+","+orderby.trim());
			orderby=null;
			defaultorderby=null;
			close();
		}

		return ret;
	}


	//getwhere
	public String	getWhere(String tablename)	{
		String ret="";
		if	(tablename==null || tablename.length()==0)		return ret;
		try {
			if	(conn==null)			init();
			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt2!= null)		{prepstmt2.close();prepstmt2=null;}
			prepstmt2=conn.prepareStatement("SELECT * FROM	"+tablename.toUpperCase());
			rst=prepstmt2.executeQuery();
			meta=rst.getMetaData();

			colnum=meta.getColumnCount();

			for	(int j=1;j<colnum+1;j++){
				if	(ret.length()>1)	ret+=" AND ";
				//ret+=meta.getColumnName(j)+"='"+obInterface.getFieldValue(meta.getColumnName(j).toLowerCase())+"'";
				ret+=meta.getColumnName(j)+"='"+obInterface.get(meta.getColumnName(j).toLowerCase())+"'";
			}
		}	catch(Exception e)	{
			CLog.writeLog("getWhere=["+ret+"],"+colnum);
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			//throw e;
			return "";
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+tablename+":"+colnum);
			close();
		}
		return ret.trim();
	}

	//getwhere
	public String	getWhereByIndex(String tablename)	{
		String ret="";
		indexcolnum=0;
		if	(tablename==null || tablename.length()==0)		return ret;
		try {
			if	(conn==null)			init();
			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt2!= null)		{prepstmt2.close();prepstmt2=null;}
			prepstmt2=conn.prepareStatement("SELECT * FROM	"+tablename.toUpperCase());
			rst=prepstmt2.executeQuery();
			meta=rst.getMetaData();

			colnum=meta.getColumnCount();
			indexcolname=new String[colnum];

			dbMetaData=conn.getMetaData();
			if	(rst != null)		{rst.close();rst=null;}
			rst= dbMetaData.getIndexInfo(null,null,tablename.toUpperCase(),true,false);


			String indexname=null;
			String symbolstr="";
			for	(int j=0;rst.next();){

				//CLog.writeLog(j+",colnum["+rst.getInt("ORDINAL_POSITION")+"],INDEX_NAME["+rst.getString("INDEX_NAME")+"],COLUMN_NAME["+rst.getString("COLUMN_NAME")+"]");
				//if	(j>0 && rst.getInt("ORDINAL_POSITION")==1)	break;

				if	(rst.getBoolean("NON_UNIQUE"))	continue;
				if	(rst.getString("INDEX_NAME")==null)	continue;
				if	(indexname!=null && !rst.getString("INDEX_NAME").equals(indexname))	break;
				indexname=rst.getString("INDEX_NAME");
				indexcolname[j]=rst.getString("COLUMN_NAME");
				//CLog.writeLog("getWhere=["+ret+"],"+indexcolname[j]);
				//if	(getVarType(obInterface,indexcolname[j]).toLowerCase().indexOf("string")>=0)	symbolstr="'";	else	symbolstr="";
				if	(getVarType(obInterface,indexcolname[j]).toLowerCase().indexOf("string")>=0){
					symbolstr="'";
					/**/
					for	(int m=1;m<colnum+1;m++){
						//if	(meta.getColumnName(m).toLowerCase().equals(indexcolname[j]) && meta.getColumnTypeName(m).toUpperCase().equals("INTEGER"))	symbolstr="";
						if	(meta.getColumnName(m).toLowerCase().equals(indexcolname[j].toLowerCase()))	{
							if	(meta.getColumnTypeName(m).toLowerCase().indexOf("char")<0)	symbolstr="";
							//CLog.writeLog("colnum["+m+"/"+colnum+"],INDEX_NAME["+meta.getColumnName(m)+"],type["+meta.getColumnTypeName(m)+"]");
							break;
						}
					}
				}	else	symbolstr="";

				//sql+=indexcolname[j]+" = "+symbolstr+myvalue+symbolstr;
				if	(ret.length()>1)	ret+=" AND ";
				//ret+=indexcolname[j]+"="+symbolstr+obInterface.getFieldValue(indexcolname[j].toLowerCase())+symbolstr;
				ret+=indexcolname[j]+"="+symbolstr+obInterface.get(indexcolname[j].toLowerCase())+symbolstr;
				indexcolnum=j+1;
				j++;
			}

			//PRIMARY KEY
			if	(indexcolnum==0){
				if	(rst != null)					{rst.close();rst=null;}
				rst= dbMetaData.getPrimaryKeys(null, null,tablename);

				for	(int j=0;rst.next();){
					if	(!rst.getString("TABLE_NAME").equalsIgnoreCase(tablename))	continue;

					indexname=rst.getString("COLUMN_NAME");
					if	(getVarType(obInterface,indexname).toLowerCase().indexOf("string")>=0)	symbolstr="'";	else	symbolstr="";
					if	(j>0)	sql+=" AND ";
					String myvalue=obInterface.getString(indexname.toLowerCase());
					if	(myvalue==null || myvalue.length()<=0)	myvalue=" ";
					sql+=indexname+" = "+symbolstr+myvalue+symbolstr;					indexcolnum=rst.getShort("KEY_SEQ");
				}
			}

		}	catch(Exception e)	{
			CLog.writeLog("getWhere=["+ret+"],"+indexcolnum);
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			//throw e;
			return "";
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+tablename+":"+indexcolnum);
			close();
		}
		if	(indexcolnum==0)		return	getWhere(tablename);
		return ret.trim();
	}

	//insert
	public boolean	insert(String tablename)	{
		retboolean=false;
		if	(tablename==null || tablename.length()==0)		return false;
		try {
			if	(conn==null)			init();
			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt2!= null)		{prepstmt2.close();prepstmt2=null;}
			prepstmt2=conn.prepareStatement("SELECT * FROM	"+tablename.toUpperCase());
			rst=prepstmt2.executeQuery();
			meta=rst.getMetaData();

			//stmt=conn.createStatement();
			//meta=stmt.executeQuery("SELECT * FROM	"+tablename.toUpperCase()).getMetaData();
			colnum=meta.getColumnCount();

			//CLog.writeLog("colnum="+colnum);
			sql="INSERT INTO "+tablename.toUpperCase()+" VALUES(";
			for	(int j=0;j<colnum;j++){
				if	(j==0)	sql+="?";	else	sql+=",?";
			}
			sql+=")";
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			if	(rst != null)					{rst.close();rst=null;}
			prepstmt=conn.prepareStatement(sql);
			for	(int j=1;j<colnum+1;j++)
			{
				//where=meta.getColumnName(j)+"="+myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase()));
				//CLog.writeLog(j+"=["+where+"]");
				//CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]");
				//CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
				//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("Date"))
				try{
					if	(meta.getColumnType(j)==91)
					{
						//prepstmt.setDate(j,App.String2Date((String)myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase()))));
						//prepstmt.setDate(j,App.String2Date(myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())).toString()));
						//prepstmt.setDate(j,App.String2Date(obInterface.getFieldValue(meta.getColumnName(j)).toString()));
						prepstmt.setDate(j,App.String2Date(obInterface.get(meta.getColumnName(j)).toString()));
						continue;
						//temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
					}
					if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("String"))
					{
						//prepstmt.setString(j,(String)myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())));
						//prepstmt.setString(j,myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())).toString());
						//String myvalue=myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())).toString();
						//String myvalue=obInterface.getFieldValue(meta.getColumnName(j).toLowerCase()).toString();
						String myvalue=obInterface.getString(meta.getColumnName(j).toLowerCase());
						if	(myvalue==null || myvalue.length()<=0)	myvalue=" ";
						prepstmt.setString(j,App.subStringGBK(myvalue,0,meta.getColumnDisplaySize(j)));
						continue;
						//temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getString(meta.getColumnName(j)));
					}
					//if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("int")>=0)
					if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("int"))
					{
						try	{
							//prepstmt.setInt(j,(Integer)myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())));
							prepstmt.setInt(j,obInterface.getInt(meta.getColumnName(j).toLowerCase()));
						}	catch	(Exception e)	{
							//prepstmt.setInt(j,Integer.parseInt("0"+myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())).toString()));
							prepstmt.setInt(j,obInterface.getInt(meta.getColumnName(j).toLowerCase()));
						}
						continue;
						//temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getInt(meta.getColumnName(j)));
					}
					if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("double"))
					{
						//prepstmt.setDouble(j,(Double)myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())));
						prepstmt.setDouble(j,obInterface.getDouble(meta.getColumnName(j).toLowerCase()));
						continue;
						//temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDouble(meta.getColumnName(j)));
					}
					if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("float"))
					{
						//prepstmt.setFloat(j,(Float)myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())));
						prepstmt.setFloat(j,obInterface.getFloat(meta.getColumnName(j).toLowerCase()));
						continue;
						//temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDouble(meta.getColumnName(j)));
					}
				}	catch	(Exception e)	{
					CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]");
					CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
					CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
				}
			}
			//prepstmt.executeUpdate();
			//return executeUpdate();
			retboolean=executeUpdate();
		}	catch(Exception e)	{
			CLog.writeLog("sql=["+sql+"]");
			CLog.writeLog("insert err! ");
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			//throw e;
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+tablename+":"+retboolean);
			close();
		}
		return retboolean;
	}

	//update
	public boolean	update(String tablename)	{
		return update(tablename,null);
	}

	//update
	public boolean	update(String tablename,String where)	{
		indexcolname=new String[30];
		indexcolnum=0;
		retboolean=false;
		if	(tablename==null || tablename.length()==0)		return false;
		try {
			if	(conn==null)			init();
			if	(prepstmt2!= null)		{prepstmt2.close();prepstmt2=null;}
			if	(rst != null)		{rst.close();rst=null;}
			prepstmt2=conn.prepareStatement("SELECT * FROM	"+tablename.toUpperCase());
			rst=prepstmt2.executeQuery();
			meta=rst.getMetaData();

			//stmt=conn.createStatement();
			//rst=stmt.executeQuery("SELECT * FROM	"+tablename.toUpperCase());
			//meta=rst.getMetaData();
			//meta=stmt.executeQuery("SELECT * FROM	"+tablename.toUpperCase()).getMetaData();
			colnum=meta.getColumnCount();
			indexcolname=new String[colnum];
			//CLog.writeLog("colnum="+colnum);
			sql="UPDATE "+tablename.toUpperCase()+" SET ";
			for	(int j=1;j<colnum+1;j++){
				if	(j==1)	sql+=meta.getColumnName(j)+"=?";	else	sql+=","+meta.getColumnName(j)+"=?";
			}

			dbMetaData=conn.getMetaData();
			if	(rst != null)		{rst.close();rst=null;}
			rst= dbMetaData.getIndexInfo(null,null,tablename.toUpperCase(),true,false);

			//if	(where!=null && where.trim().length()>0 && where.trim().toLowerCase().indexOf("where")<0)		where=" WHERE "+where;
			if	(where!=null && where.trim().length()>0){
				if	(where.trim().toLowerCase().indexOf("where")<0)		where=" WHERE "+where.replaceAll("%20"," ");
				sql+=where.replaceAll("%20"," ");
				//CLog.writeLog("sql=["+sql+"]"+",where=["+where+"]");
			}	else	{
				sql+=" WHERE ";
				String indexname=null;
				String symbolstr="";
				for	(int j=0;rst.next();){
					//CLog.writeLog(j+",colnum["+rst.getInt("ORDINAL_POSITION")+"],INDEX_NAME["+rst.getString("INDEX_NAME")+"],COLUMN_NAME["+rst.getString("COLUMN_NAME"));
					//if	(j>0 && rst.getInt("ORDINAL_POSITION")==1)	break;

					if	(rst.getBoolean("NON_UNIQUE"))	continue;
					if	(rst.getString("INDEX_NAME")==null)	continue;
					if	(indexname!=null && !rst.getString("INDEX_NAME").equals(indexname))	break;
					indexname=rst.getString("INDEX_NAME");
					if	(j>0)	sql+=" AND ";
					indexcolname[j]=rst.getString("COLUMN_NAME");
					//if	(getVarType(obInterface,indexcolname[j]).toLowerCase().indexOf("string")>=0)	symbolstr="'";	else	symbolstr="";
					if	(getVarType(obInterface,indexcolname[j]).toLowerCase().indexOf("string")>=0){
						symbolstr="'";
						/**/
						for	(int m=1;m<colnum+1;m++){
							//if	(meta.getColumnName(m).toLowerCase().equals(indexcolname[j]) && meta.getColumnTypeName(m).toUpperCase().equals("INTEGER"))	symbolstr="";
							if	(meta.getColumnName(m).toLowerCase().equals(indexcolname[j].toLowerCase()))	{
								if	(meta.getColumnTypeName(m).toLowerCase().indexOf("char")<0)	symbolstr="";
								//CLog.writeLog("colnum["+m+"/"+colnum+"],INDEX_NAME["+meta.getColumnName(m)+"],type["+meta.getColumnTypeName(m)+"]");
								break;
							}
						}
					}	else	symbolstr="";
					//String myvalue=myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j].toLowerCase())).toString();
					//String myvalue=obInterface.getFieldValue(indexcolname[j].toLowerCase()).toString();
					String myvalue=obInterface.getString(indexcolname[j].toLowerCase());
					if	(myvalue==null || myvalue.length()<=0)	myvalue=" ";
					sql+=indexcolname[j]+" = "+symbolstr+myvalue+symbolstr;
					indexcolnum=j+1;
					j++;
					//sql+=myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j].toLowerCase()));
				}
				//CLog.writeLog("sql=["+sql+"]"+",where=["+where+"]");
				//if	(indexcolnum==0)			sql+=getWhere(tablename);

				//PRIMARY KEY
				if	(indexcolnum==0){
					if	(rst != null)					{rst.close();rst=null;}
					rst= dbMetaData.getPrimaryKeys(null, null,tablename);

					for	(int j=0;rst.next();){
						if	(!rst.getString("TABLE_NAME").equalsIgnoreCase(tablename))	continue;

						indexname=rst.getString("COLUMN_NAME");
						if	(getVarType(obInterface,indexname).toLowerCase().indexOf("string")>=0)	symbolstr="'";	else	symbolstr="";
						if	(j>0)	sql+=" AND ";
						String myvalue=obInterface.getString(indexname.toLowerCase());
						if	(myvalue==null || myvalue.length()<=0)	myvalue=" ";
						sql+=indexname+" = "+symbolstr+myvalue+symbolstr;
						indexcolnum=rst.getShort("KEY_SEQ");
					}
				}


			}
			//CLog.writeLog("sql=["+sql+"]"+indexcolnum+"/"+colnum);

			prepstmt=conn.prepareStatement(sql);

			for	(int j=1;j<colnum+1;j++)
			{
				//CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"],value["+obInterface.getFieldValue(meta.getColumnName(j).toLowerCase())+"]");
				//CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]"+myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())));
				//CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
				//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("Date"))
				try{
					if	(meta.getColumnType(j)==91)
					{
						//prepstmt.setDate(j,App.String2Date((String)myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase()))));
						//prepstmt.setDate(j,App.String2Date(myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())).toString()));
						//prepstmt.setDate(j,App.String2Date(obInterface.getFieldValue(meta.getColumnName(j)).toString()));
						prepstmt.setDate(j,App.String2Date(obInterface.get(meta.getColumnName(j)).toString()));
						continue;
						//temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
					}
					if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("String"))
					{
						//prepstmt.setString(j,(String)myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())));
						//prepstmt.setString(j,myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())).toString());
						//String myvalue=myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())).toString();
						//String myvalue=obInterface.getFieldValue(meta.getColumnName(j).toLowerCase()).toString();
						String myvalue=obInterface.getString(meta.getColumnName(j).toLowerCase());
						if	(myvalue==null || myvalue.length()<=0)	myvalue=" ";

						prepstmt.setString(j,App.subStringGBK(myvalue,0,meta.getColumnDisplaySize(j)));
						//if	(App.getStringLength(myvalue)!=myvalue.length())		prepstmt.setString(j,App.subStringGBK(myvalue,0,meta.getColumnDisplaySize(j)));
						//		else	prepstmt.setString(j,myvalue.substring(0,(myvalue.length()>meta.getColumnDisplaySize(j)?meta.getColumnDisplaySize(j):myvalue.length())));
						continue;
						//temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getString(meta.getColumnName(j)));
					}
					if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("int"))
					{
						try	{
							//prepstmt.setInt(j,(Integer)myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())));
							prepstmt.setInt(j,obInterface.getInt(meta.getColumnName(j).toLowerCase()));
						}	catch	(Exception e)	{
							//prepstmt.setInt(j,Integer.parseInt("0"+myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())).toString()));
							prepstmt.setInt(j,obInterface.getInt(meta.getColumnName(j).toLowerCase()));
						}
						continue;
						//temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getInt(meta.getColumnName(j)));
					}
					if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("double"))
					{
						//prepstmt.setDouble(j,(Double)myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())));
						prepstmt.setDouble(j,obInterface.getDouble(meta.getColumnName(j).toLowerCase()));
						continue;
						//temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDouble(meta.getColumnName(j)));
					}
					if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("float"))
					{
						//prepstmt.setFloat(j,(Float)myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())));
						prepstmt.setFloat(j,obInterface.getFloat(meta.getColumnName(j).toLowerCase()));
						continue;
						//temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDouble(meta.getColumnName(j)));
					}
				}	catch	(Exception e)	{
				}
			}

			/*
			where="";
			for	(int j=1;j<indexcolnum+1;j++){
				if	(j>1)	where+=" AND ";
				where+=indexcolname[j-1]+"="+myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase()));
				//CLog.writeLog((j+colnum)+","+indexcolname[j-1]+"=["+myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1]))+"]"+getVarType(obInterface,indexcolname[j-1]).toLowerCase());
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("string")>=0)
				{
					//prepstmt.setString((j+colnum),((String)myinvoke(obInterface,"get"+App.transFirstUpperCase(.toLowerCase()))));
					prepstmt.setString((j+colnum),myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())).toString());
					continue;
				}
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("int")>=0)
				{
					//prepstmt.setInt(j+colnum,(Integer)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())));
					try	{
						prepstmt.setInt(j+colnum,(Integer)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())));
					}	catch	(Exception e)	{
						prepstmt.setInt(j+colnum,Integer.parseInt("0"+myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())).toString()));
					}
					continue;
				}
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("double")>=0)
				{
					prepstmt.setDouble(j+colnum,(Double)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())));
					continue;
				}
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("date")>=0)
				{
					//prepstmt.setDate(j+colnum,App.String2Date((String)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase()))));
					prepstmt.setDate(j+colnum,App.String2Date(myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())).toString()));
					continue;
				}
			}*/
			retboolean=executeUpdate();
			//prepstmt.executeUpdate();
		}	catch(Exception e)	{
			CLog.writeLog("sql=["+sql+"]"+indexcolnum+"/"+colnum);
			CLog.writeLog("where=["+where+"]"+indexcolnum+"/"+colnum);
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			//throw e;
			return retboolean;
		}	finally	{
			CLog.writeLog(retboolean+":"+new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+tablename+":"+where+",["+indexcolnum+"/"+colnum+"]");
			//CLog.writeLog("sql=["+sql+"]"+indexcolnum+"/"+colnum);
			close();
		}

		return retboolean;
	}


	//update
	public boolean	update1(String tablename)	{
		indexcolname=new String[30];
		if	(tablename==null || tablename.length()==0)		return false;
		try {
			if	(conn==null)			init();
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			if	(rst != null)					{rst.close();rst=null;}
			prepstmt=conn.prepareStatement("SELECT * FROM	"+tablename.toUpperCase());
			rst=prepstmt.executeQuery();
			meta=rst.getMetaData();

			//stmt=conn.createStatement();
			//rst=stmt.executeQuery("SELECT * FROM	"+tablename.toUpperCase());
			//meta=rst.getMetaData();
			//meta=stmt.executeQuery("SELECT * FROM	"+tablename.toUpperCase()).getMetaData();
			colnum=meta.getColumnCount();

			//CLog.writeLog("colnum="+colnum);
			sql="UPDATE "+tablename.toUpperCase()+" SET ";
			for	(int j=1;j<colnum+1;j++){
				if	(j==1)	sql+=meta.getColumnName(j)+" = ? ";	else	sql+=", "+meta.getColumnName(j)+" = ? ";
			}

			dbMetaData=conn.getMetaData();
			if	(rst != null)		{rst.close();rst=null;}
			rst= dbMetaData.getIndexInfo(null,null,tablename.toUpperCase(),true,false);

			sql+=" WHERE ";
			String indexname=null;
			indexcolnum=0;
			for	(int j=0;rst.next();){
				//CLog.writeLog(j+",colnum["+rst.getInt("ORDINAL_POSITION")+"],INDEX_NAME["+rst.getString("INDEX_NAME")+"],COLUMN_NAME["+rst.getString("COLUMN_NAME"));
				//if	(j>0 && rst.getInt("ORDINAL_POSITION")==1)	break;

				if	(rst.getBoolean("NON_UNIQUE"))	continue;
				if	(rst.getString("INDEX_NAME")==null)	continue;
				if	(indexname!=null && !rst.getString("INDEX_NAME").equals(indexname))	break;
				indexname=rst.getString("INDEX_NAME");
				if	(j>0)	sql+=" AND ";
				indexcolname[j]=rst.getString("COLUMN_NAME");
				sql+=indexcolname[j]+" = ? ";
				indexcolnum=j+1;
				j++;
				//sql+=myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j].toLowerCase()));
			}

			//PRIMARY KEY
			if	(indexcolnum==0){
				if	(rst != null)					{rst.close();rst=null;}
				rst= dbMetaData.getPrimaryKeys(null, null,tablename);

				for	(int j=0;rst.next();){
					if	(!rst.getString("TABLE_NAME").equalsIgnoreCase(tablename))	continue;

					indexname=rst.getString("COLUMN_NAME");
					if	(j>0)	sql+=" AND ";
					sql+=indexname+" = ? ";
					indexcolnum=rst.getShort("KEY_SEQ");
				}
			}

			//CLog.writeLog("sql=["+sql+"]"+indexcolnum);
			//if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			prepstmt=conn.prepareStatement(sql);

			for	(int j=1;j<colnum+1;j++)
			{
				//CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]"+myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())));
				//CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
				//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("Date"))
				try{
					if	(meta.getColumnType(j)==91)
					{
						//prepstmt.setDate(j,App.String2Date((String)myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase()))));
						//prepstmt.setDate(j,App.String2Date(myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())).toString()));
						//prepstmt.setDate(j,App.String2Date(obInterface.getFieldValue(meta.getColumnName(j)).toString()));
						prepstmt.setDate(j,App.String2Date(obInterface.get(meta.getColumnName(j)).toString()));
						continue;
						//temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
					}
					if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("String"))
					{
						//prepstmt.setString(j,(String)myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())));
						//prepstmt.setString(j,myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())).toString());
						prepstmt.setString(j,obInterface.getString(meta.getColumnName(j).toLowerCase()));
						continue;
						//temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getString(meta.getColumnName(j)));
					}
					if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("int"))
					{
						//prepstmt.setInt(j,(Integer)myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())));
						try	{
							//prepstmt.setInt(j,(Integer)myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())));
							prepstmt.setInt(j,obInterface.getInt(meta.getColumnName(j).toLowerCase()));
						}	catch	(Exception e)	{
							//prepstmt.setInt(j,Integer.parseInt("0"+myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())).toString()));
							prepstmt.setInt(j,obInterface.getInt(meta.getColumnName(j).toLowerCase()));
						}
						continue;
						//temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getInt(meta.getColumnName(j)));
					}
					if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("double"))
					{
						//prepstmt.setDouble(j,(Double)myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())));
						prepstmt.setDouble(j,obInterface.getDouble(meta.getColumnName(j).toLowerCase()));
						continue;
						//temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDouble(meta.getColumnName(j)));
					}
					if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("float"))
					{
						//prepstmt.setFloat(j,(Float)myinvoke(obInterface,"get"+App.transFirstUpperCase(meta.getColumnName(j).toLowerCase())));
						prepstmt.setFloat(j,obInterface.getFloat(meta.getColumnName(j).toLowerCase()));
						continue;
						//temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDouble(meta.getColumnName(j)));
					}
				}	catch	(Exception e)	{
				}
			}

			where="";
			for	(int j=1;j<indexcolnum+1;j++){
				if	(j>1)	where+=" AND ";
				//where+=indexcolname[j-1]+"="+myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase()));
				where+=indexcolname[j-1]+"="+obInterface.getString(indexcolname[j-1]);
				//CLog.writeLog((j+colnum)+","+indexcolname[j-1]+"=["+myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1]))+"]"+getVarType(obInterface,indexcolname[j-1]).toLowerCase());
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("string")>=0)
				{
					//prepstmt.setString((j+colnum),((String)myinvoke(obInterface,"get"+App.transFirstUpperCase(.toLowerCase()))));
					//prepstmt.setString((j+colnum),myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())).toString());
					prepstmt.setString((j+colnum),obInterface.getString(indexcolname[j-1]));
					continue;
				}
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("int")>=0)
				{
					//prepstmt.setInt(j+colnum,(Integer)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())));
					try	{
						//prepstmt.setInt(j+colnum,(Integer)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())));
						prepstmt.setInt(j+colnum,obInterface.getInt(indexcolname[j-1]));
					}	catch	(Exception e)	{
						//prepstmt.setInt(j+colnum,Integer.parseInt("0"+myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())).toString()));
						prepstmt.setInt(j+colnum,obInterface.getInt(indexcolname[j-1]));
					}
					continue;
				}
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("double")>=0)
				{
					//prepstmt.setDouble(j+colnum,(Double)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())));
					prepstmt.setDouble(j+colnum,obInterface.getDouble(indexcolname[j-1]));
					continue;
				}
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("date")>=0)
				{
					//prepstmt.setDate(j+colnum,App.String2Date((String)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase()))));
					//prepstmt.setDate(j+colnum,App.String2Date(myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())).toString()));
					//prepstmt.setDate(j+colnum,App.String2Date(obInterface.getFieldValue(indexcolname[j-1]).toString()));
					prepstmt.setDate(j+colnum,App.String2Date(obInterface.get(indexcolname[j-1]).toString()));
					continue;
				}
			}
			return	executeUpdate();
			//prepstmt.executeUpdate();
		}	catch(Exception e)	{
			CLog.writeLog("sql=["+sql+"]"+colnum);
			CLog.writeLog("where=["+where+"]"+colnum);
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			//throw e;
			return false;
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
			CLog.writeLog("sql=["+sql+"]");
			CLog.writeLog("where="+where);
			close();
		}

		//return true;
	}

	//删除
	public boolean	delete1(String tablename)	{
		indexcolname=new String[30];
		if	(tablename==null || tablename.length()==0)		return false;
		try {
			if	(conn==null)			init();

			dbMetaData=conn.getMetaData();
			if	(rst != null)		{rst.close();rst=null;}
			rst= dbMetaData.getIndexInfo(null,null,tablename.toUpperCase(),true,false);

			sql="DELETE FROM "+tablename.toUpperCase()+" WHERE ";
			String indexname=null;
			indexcolnum=0;
			for	(int j=0;rst.next();){
				//CLog.writeLog(j+",colnum["+rst.getInt("ORDINAL_POSITION")+"],INDEX_NAME["+rst.getString("INDEX_NAME")+"],COLUMN_NAME["+rst.getString("COLUMN_NAME"));
				//if	(j>0 && rst.getInt("ORDINAL_POSITION")==1)	break;

				if	(rst.getBoolean("NON_UNIQUE"))	continue;
				if	(rst.getString("INDEX_NAME")==null)	continue;
				if	(indexname!=null && !rst.getString("INDEX_NAME").equals(indexname))	break;
				indexname=rst.getString("INDEX_NAME");
				if	(j>1)	sql+=" AND ";
				//indexcolname[j]=rst.getString("COLUMN_NAME").toLowerCase();
				indexcolname[j]=rst.getString("COLUMN_NAME");
				sql+=indexcolname[j]+" = ? ";
				indexcolnum=j+1;
				j++;
				//sql+=myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j].toLowerCase()));
			}

			//PRIMARY KEY
			if	(indexcolnum==0){
				if	(rst != null)					{rst.close();rst=null;}
				rst= dbMetaData.getPrimaryKeys(null, null,tablename);

				for	(int j=0;rst.next();){
					if	(!rst.getString("TABLE_NAME").equalsIgnoreCase(tablename))	continue;

					indexname=rst.getString("COLUMN_NAME");
					if	(j>0)	sql+=" AND ";
					sql+=indexname+" = ? ";
					indexcolnum=rst.getShort("KEY_SEQ");
				}
			}


			//CLog.writeLog("sql=["+sql+"]"+indexcolnum);
			//if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			prepstmt=conn.prepareStatement(sql);
			where="";
			for	(int j=1;j<indexcolnum+1;j++){
				if	(j>1)	where+=" AND ";
				//where+=indexcolname[j-1]+"="+myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase()));
				where+=indexcolname[j-1]+"="+obInterface.getString(indexcolname[j-1]);
				//CLog.writeLog(j+","+indexcolname[j-1]+"=["+myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1]))+"]"+getVarType(obInterface,indexcolname[j-1]).toLowerCase());
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("string")>=0)
				{
					//prepstmt.setString(j,((String)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase()))));
					//prepstmt.setString(j,myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())).toString());
					prepstmt.setString(j,obInterface.getString(indexcolname[j-1]));
					continue;
				}
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("int")>=0)
				{
					try	{
						//prepstmt.setInt(j,(Integer)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())));
						prepstmt.setInt(j,obInterface.getInt(indexcolname[j-1]));
					}	catch	(Exception e)	{
						//prepstmt.setInt(j,Integer.parseInt("0"+myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())).toString()));
						prepstmt.setInt(j,obInterface.getInt(indexcolname[j-1]));
					}
					continue;
				}
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("double")>=0)
				{
					//prepstmt.setDouble(j,(Double)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())));
					prepstmt.setDouble(j,obInterface.getDouble(indexcolname[j-1]));
					continue;
				}
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("float")>=0)
				{
					//prepstmt.setFloat(j,(Float)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())));
					prepstmt.setFloat(j,obInterface.getFloat(indexcolname[j-1]));
					continue;
				}
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("date")>=0)
				{
					//prepstmt.setDate(j,App.String2Date((String)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase()))));
					//prepstmt.setDate(j,App.String2Date(myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())).toString()));
					//prepstmt.setDate(j,App.String2Date(obInterface.getFieldValue(indexcolname[j-1]).toString()));
					prepstmt.setDate(j,App.String2Date(obInterface.get(indexcolname[j-1]).toString()));
					continue;
				}
			}

			//CLog.writeLog("sql===[delete from "+tablename+" WHERE "+where);
			return executeUpdate();
		}	catch(Exception e)	{
			CLog.writeLog("sql=["+sql+"]");
			CLog.writeLog("where="+where);
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			//throw e;
			return false;
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
			close();
		}
	}


	//删除
	public boolean	delete(String tablename)	{
		indexcolname=new String[30];
		indexcolnum=0;
		if	(tablename==null || tablename.length()==0)		return false;
		try {
			if	(conn==null)			init();

			//if	(rst != null)		{rst.close();rst=null;}
			//prepstmt=conn.prepareStatement("SELECT * FROM	"+tablename.toUpperCase());
			//rst=prepstmt.executeQuery();
			//meta=rst.getMetaData();
			//colnum=meta.getColumnCount();

			if	(prepstmt2!= null)		{prepstmt2.close();prepstmt2=null;}
			if	(rst != null)					{rst.close();rst=null;}
			prepstmt2=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase());
			rst=prepstmt2.executeQuery();
			meta=rst.getMetaData();
			colnum=meta.getColumnCount();


			dbMetaData=conn.getMetaData();
			if	(rst != null)		{rst.close();rst=null;}
			rst= dbMetaData.getIndexInfo(null,null,tablename.toUpperCase(),true,false);

			sql="DELETE FROM "+tablename.toUpperCase()+" WHERE ";
			String indexname=null;
			String symbolstr="";
			for	(int j=0;rst.next();){
				//CLog.writeLog(j+",colnum["+rst.getInt("ORDINAL_POSITION")+"],INDEX_NAME["+rst.getString("INDEX_NAME")+"],COLUMN_NAME["+rst.getString("COLUMN_NAME"));
				//if	(j>0 && rst.getInt("ORDINAL_POSITION")==1)	break;

				if	(rst.getBoolean("NON_UNIQUE"))	continue;
				if	(rst.getString("INDEX_NAME")==null)	continue;
				if	(indexname!=null && !rst.getString("INDEX_NAME").equals(indexname))	break;
				indexname=rst.getString("INDEX_NAME");
				if	(j>0)	sql+=" AND ";
				//indexcolname[j]=rst.getString("COLUMN_NAME").toLowerCase();
				indexcolname[j]=rst.getString("COLUMN_NAME");
				//if	(getVarType(obInterface,indexcolname[j]).toLowerCase().indexOf("string")>=0)	symbolstr="'";	else	symbolstr="";
				if	(getVarType(obInterface,indexcolname[j]).toLowerCase().indexOf("string")>=0){
					symbolstr="'";
					/**/
					for	(int m=1;m<colnum+1;m++){
						//if	(meta.getColumnName(m).toLowerCase().equals(indexcolname[j]) && meta.getColumnTypeName(m).toUpperCase().equals("INTEGER"))	symbolstr="";
						if	(meta.getColumnName(m).toLowerCase().equals(indexcolname[j].toLowerCase()))	{
							if	(meta.getColumnTypeName(m).toLowerCase().indexOf("char")<0)	symbolstr="";
							//CLog.writeLog("colnum["+m+"/"+colnum+"],INDEX_NAME["+meta.getColumnName(m)+"],type["+meta.getColumnTypeName(m)+"]");
							break;
						}
					}
				}	else	symbolstr="";
				//String myvalue=myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j].toLowerCase())).toString();
				//String myvalue=obInterface.getFieldValue(indexcolname[j].toLowerCase()).toString();
				String myvalue=obInterface.getString(indexcolname[j].toLowerCase());
				if	(myvalue==null || myvalue.length()<=0)	myvalue=" ";
				sql+=indexcolname[j]+" = "+symbolstr+myvalue+symbolstr;

				//sql+=indexcolname[j]+" = ? ";
				indexcolnum=j+1;
				j++;
				//sql+=myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j].toLowerCase()));
			}

			//PRIMARY KEY
			if	(indexcolnum==0){
				if	(rst != null)					{rst.close();rst=null;}
				rst= dbMetaData.getPrimaryKeys(null, null,tablename);

				for	(int j=0;rst.next();){
					if	(!rst.getString("TABLE_NAME").equalsIgnoreCase(tablename))	continue;

					indexname=rst.getString("COLUMN_NAME");
					if	(getVarType(obInterface,indexname).toLowerCase().indexOf("string")>=0)	symbolstr="'";	else	symbolstr="";
					if	(j>0)	sql+=" AND ";
					String myvalue=obInterface.getString(indexname.toLowerCase());
					if	(myvalue==null || myvalue.length()<=0)	myvalue=" ";
					sql+=indexname+" = "+symbolstr+myvalue+symbolstr;
					indexcolnum=rst.getShort("KEY_SEQ");
				}
			}
			if	(indexcolnum==0)		return	delete(tablename,getWhere(tablename));

			//CLog.writeLog("sql=["+sql+"]"+indexcolnum);
			//prepstmt=conn.prepareStatement(sql);
			where="";
			for	(int j=1;j<indexcolnum+1;j++){
				//if	(j>1)	where+=" AND ";
				//where+=indexcolname[j-1]+"="+myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase()));


				//CLog.writeLog(j+","+indexcolname[j-1]+"=["+myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1]))+"]"+getVarType(obInterface,indexcolname[j-1]).toLowerCase());
				/*
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("string")>=0)
				{
					//prepstmt.setString(j,((String)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase()))));
					prepstmt.setString(j,myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())).toString());
					continue;
				}
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("int")>=0)
				{
					//prepstmt.setInt(j,(Integer)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())));
					try	{
						prepstmt.setInt(j,(Integer)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())));
					}	catch	(Exception e)	{
						prepstmt.setInt(j,Integer.parseInt("0"+myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())).toString()));
					}
					continue;
				}
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("double")>=0)
				{
					prepstmt.setDouble(j,(Double)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())));
					continue;
				}
				if	(getVarType(obInterface,indexcolname[j-1]).toLowerCase().indexOf("date")>=0)
				{
					//prepstmt.setDate(j,App.String2Date((String)myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase()))));
					prepstmt.setDate(j,App.String2Date(myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j-1].toLowerCase())).toString()));
					continue;
				}*/
			}

			//CLog.writeLog("sql===[delete from "+tablename+" WHERE "+where);
			//return executeUpdate("DELETE FROM "+tablename.toUpperCase()+" WHERE "+where);
			//CLog.writeLog("sql=["+sql+"]"+indexcolnum);
			return executeUpdate(sql);
		}	catch(Exception e)	{
			CLog.writeLog("sql=["+sql+"]");
			CLog.writeLog("where="+where);
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			//throw e;
			return false;
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+tablename+":"+sql);
			close();
		}

	}

	//删除
	public boolean	delete(String tablename,String where)	{
		indexcolname=new String[30];
		if	(tablename==null || tablename.length()==0)		return false;
		try {
			if	(where==null || where.trim().length()==0)		return delete(tablename);
			if	(conn==null)			init();
			if	(where!=null && where.trim().length()>0 && where.trim().toLowerCase().indexOf("where")<0)		where=" WHERE "+where;
			sql="DELETE FROM "+tablename.toUpperCase()+" "+where;

			return executeUpdate(sql);
		}	catch(Exception e)	{
			CLog.writeLog("sql=["+sql+"]");
			CLog.writeLog("where="+where);
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			//throw e;
			return false;
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+tablename+":"+where);
			close();
		}
	}



	public Collection selectSql()  {
		return selectSql(obInterface.getSql().trim());
	}

	public Collection selectSql(String sql)  {
		Collection ret=new ArrayList();
		//安全检查
		if	(sql.toLowerCase().indexOf("select")<0)		return ret;
		//if	(sql.toLowerCase().indexOf("insert")>0 && sql.toLowerCase().indexOf("insert")<20)		return ret;
		//if	(sql.toLowerCase().indexOf("delete")>0 && sql.toLowerCase().indexOf("delete")<20)		return ret;
		//if	(sql.toLowerCase().indexOf("update")>0 && sql.toLowerCase().indexOf("update")<20)		return ret;

		if	(conn==null)			init();
		//if	(mysql==null)		mysql=new Mysql(sql); else		mysql.prepareStatement(sql);

		//int retNum=0;
		retNum=0;

		String result[]=sql.trim().replaceAll("	"," ").split("(?i) from ");
		String tempsql="";
		String tempfrom="";
		boolean normalflag=false;

		//if	(sql.toLowerCase().indexOf("distinct")<0 && sql.toLowerCase().indexOf("unique")<0 && sql.toLowerCase().indexOf("count")<0 && sql.toLowerCase().indexOf("sum")<0){
		if	(result[0].toLowerCase().indexOf("distinct")<0 && result[0].toLowerCase().indexOf("unique")<0 && result[0].toLowerCase().indexOf("count")<0 && result[0].toLowerCase().indexOf("sum")<0)	normalflag=true;
		if	(sql.trim().replaceAll("	"," ").split("(?i) from ").length!=sql.trim().replaceAll("	"," ").split("(?i) union ").length)	normalflag=false;
		if	(normalflag){
			for	(int i=1;i<result.length;i++){
				tempfrom=result[i];
				int tempnum=result[i].toLowerCase().indexOf(" union ");
				if	(tempnum>0)	tempfrom=result[i].substring(0,tempnum);
				tempnum=result[i].toLowerCase().indexOf(" order ");
				if	(tempnum>0)	tempfrom=result[i].substring(0,tempnum);
				tempnum=result[i].toLowerCase().indexOf(" group ");
				if	(tempnum>0)	tempfrom=result[i].substring(0,tempnum);
				//CLog.writeLog(i+",tempfrom=["+tempfrom+"]"+result[i]);
				if	(i>1)	tempsql+=" UNION ALL ";
				tempsql+="SELECT COUNT(*) FROM "+tempfrom;
				//	System.out.println("temsql "+tempsql);
			}

			CLog.writeLog("tempsql=["+tempsql+"]"+result.length);

			try {
				if	(rst != null)					{rst.close();rst=null;}
				if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
				prepstmt=conn.prepareStatement(tempsql);
				rst=prepstmt.executeQuery();
				for	(;rst.next();)				retNum+=rst.getInt(1);
			}	catch(Exception e)	{
			}
		}
		try {
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			if	(rst != null)					{rst.close();rst=null;}

			if	(normalflag){
				prepstmt=conn.prepareStatement(sql);
				rst=prepstmt.executeQuery();
				meta=rst.getMetaData();
				colnum=meta.getColumnCount();
			}	else	{
				prepstmt=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				rst=prepstmt.executeQuery();
				meta=rst.getMetaData();
				colnum=meta.getColumnCount();
				rst.last();//jdbc 2.0
				retNum=rst.getRow();
				CLog.writeLog("jdbc 2.0:retNum=["+retNum+"]"+colnum);
				rst.beforeFirst();		//将Cursor移到初始状态
			}

			obInterface.setRetNum(retNum);
			int maxPage=retNum / obInterface.getPageSize();
			maxPage += (retNum % obInterface.getPageSize()) == 0 ? 0 : 1;
			//CLog.writeLog("PageNo=["+obInterface.getPageNo()+"]"+obInterface.getPageSize());
			int curPage=obInterface.getPageNo();
			int pageSize=obInterface.getPageSize();

			if	((curPage < 1) || (curPage > maxPage))								curPage=1;
			if	(curPage>maxPage)		maxPage=curPage;

			//if	(sql.toLowerCase().indexOf("distinct")<0 && sql.toLowerCase().indexOf("unique")<0)		obInterface.setPageNo(curPage);
			if	(result[0].toLowerCase().indexOf("distinct")<0 && result[0].toLowerCase().indexOf("unique")<0)		obInterface.setPageNo(curPage);
			obInterface.setMaxPage(maxPage);
			obInterface.setRowNum(0);
			if	(retNum<=0){
				CLog.writeLog("retNum=["+retNum+"]"+colnum);
				return ret;
			}
			int i=0;
			for (i=0; i < (curPage - 1) * pageSize; i++)					rst.next();
			//CLog.writeLog("colnum="+colnum);
			for (i=(curPage-1)*pageSize;(i<=curPage*pageSize-1)&&(i<=retNum -1);i++){
				rst.next();
				ObInterface temp=new ObInterface();
				temp.setSerialNo(i+1);
				temp.setRetNum(retNum);
				//CLog.writeLog("i="+i+",sql2==table==["+sql2+"]");
				for	(int j=1;j<colnum+1;j++)
				{
					//CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]");
					//CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
					//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("Date"))
					try{
						//if	(meta.getColumnType(j)==91)
						if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("date")>=0){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
							continue;
						}
						//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("String"))
						if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("string")>=0)	{
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getString(meta.getColumnName(j)));
							continue;
						}
						//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("int"))
						if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("int")>=0)	{
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getInt(meta.getColumnName(j)));
							continue;
						}
						//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("double"))
						if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("double")>=0)	{
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDouble(meta.getColumnName(j)));
							continue;
						}
						//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("float"))
						if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("float")>=0)	{
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getFloat(meta.getColumnName(j)));
							continue;
						}
					}	catch	(Exception e)	{
						CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]");
						CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
						CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
					}
				}

				ret.add(temp);
				temp=null;
			}
			obInterface.setRowNum(i-(curPage-1)*pageSize);

		}	catch	(SQLException e)	{
			CLog.writeLog("e="+e);
			CLog.writeLog(",errorcode="+e.getErrorCode());
			CLog.writeLog(",getMessage="+e.getMessage());
			CLog.writeLog(",Exception="+e.toString());
			//e.printStackTrace();
			CLog.writeLog("sql=["+sql+"]");
			//CLog.writeLog("where="+where);
			//throw e;
		}	catch(Exception e)	{
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			//throw e;
		}	finally	{
			if	(logboolean)	CLog.writeLog("sql=["+sql+"]");
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+retNum);
			if	(logboolean)	CLog.writeLog("==Processing time:"+(System.currentTimeMillis() - stime)+" ms==");
			close();
		}
		return ret;

	}



	//得到某变量类型
	private String	getVarType(Object o,String varname) throws Exception	{
		String	vartype="string";
		Class		classlist[]=null;
		Class		c=o.getClass();
		try {
			Method mymethod=c.getMethod("get"+App.transFirstUpperCase(varname.toLowerCase()),classlist);
			vartype=mymethod.getReturnType().getName();
			//CLog.writeLog(varname+",type="+vartype);
			return vartype;
		}	catch ( Exception e ) {
			// TODO 自动生成 catch 块
			//e.printStackTrace();
			//CLog.writeLog(varname+","+e.toString());
			//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+sql+":"+retNum);
			//CLog.writeLog(varname+",type="+vartype);
			//throw e;
			return vartype;
		}
	}

	/*

	//执行类的方法
	public Object	myinvoke(Object o,String methodname) throws Exception	{
		Object	arglist[]=new Object[]{};
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
		}	catch ( Exception e ) {
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

			//Method mymethod=c.getMethod(methodname);
			Method mymethod=c.getMethod(methodname,classlist);
			//执行方法
			//retobj=mymethod.invoke(o,arglist);
			retobj=mymethod.invoke(o);
			classlist=null;
		}	catch ( Exception e ) {
			// TODO 自动生成 catch 块
			//e.printStackTrace();
			//CLog.writeLog(methodname+","+e.toString());
			throw e;
		}

		//CLog.writeLog(methodname+"==="+retobj.toString());
		return retobj;
	}
	 */
	private String typeToString(int i,int j,int m,int n){
		//i-getColumnType,j-getColumnDisplaySize,m-getPrecision,n-getScale
		String ret="String";
		switch(i)
		{
		case(1):ret="String";break;
		//case(2):ret="NUMERIC";break;
		case(2):ret="Double";break;
		case(3):ret="Double";break;
		case(4):ret="Int";break;
		case(5):ret="Int";break;
		case(6):ret="FLOAT";break;
		case(8):ret="Double";break;
		case(12):ret="String";break;
		//case(12):ret="VARCHAR";break;
		case(91):ret="String";break;
		//case(91):ret="Date";break;
		default:ret="String";
		}

		if	(3==i && 11>=j)	ret="Float";
		if	(2==i && 22==j && 0==m)	ret="Int";
		if	(2==i && 22==j && 0==n)	ret="Int";
		if	(2==i && 22==j && 4==n)	ret="Float";
		return ret;
	}


	/*Get DataBase Time*/
	public int getCurTime(int len) {
		return Integer.parseInt("0"+getCurTimeStr(len));
	}

	/*Get DataBase Time*/
	public int getCurTime6() {
		return Integer.parseInt("0"+getCurTimeStr(6));
	}

	/*Get DataBase Time*/
	public String getCurTime6Str() {
		return getCurTimeStr(6);
	}

	public String getCurTimeStr() {
		return getCurTimeStr(6);
	}

	/*Get DataBase Time*/
	public String getCurTimeStr(int len) {
		String curdate=App.getCurTimeStr(len);

		if	(dbtype!=null && dbtype.toLowerCase().startsWith("oracle"))			curdate=getCurTimeStrForOracle(len);
		if	(dbtype!=null && dbtype.toLowerCase().startsWith("mysql"))			curdate=getCurTimeStrForMysql(len);
		if	(dbtype!=null && dbtype.toLowerCase().indexOf("server")>=0)			curdate=getCurDateStrForSybase(len);
		if	(dbtype!=null && dbtype.toLowerCase().startsWith("informix"))		curdate=getCurTimeStrForInformix(len);
		if	(dbtype!=null && dbtype.toLowerCase().startsWith("db2"))				curdate=getCurTimeStrForDb2(len);
		if	(dbtype!=null && dbtype.length()<=0)														curdate=App.getCurTimeStr(len);

		return curdate;
	}


	/*Get DataBase Time for oracle*/
	public String getCurTimeStrForOracle(int len) {
		String sqlStr = "SELECT TO_CHAR(SYSDATE,'HH24MISS') AS APMG FROM DUAL";
		obInterface.setApmg(App.getCurTimeStr(len));
		if	(len==9)			sqlStr = "SELECT SUBSTR(TO_CHAR(SYSTIMESTAMP,'HH24MISSFF'),0,"+len+") AS APMG FROM DUAL";
		if	(len==8)			sqlStr = "SELECT TO_CHAR(SYSDATE,'HH24:MI:SS') AS APMG FROM DUAL";
		if	(len==7)			sqlStr = "SELECT SUBSTR(TO_CHAR(SYSTIMESTAMP,'MISSFF'),0,"+len+") AS APMG FROM DUAL";
		if	(len==6)			sqlStr = "SELECT TO_CHAR(SYSDATE,'HH24MISS') AS APMG FROM DUAL";
		if	(len==5)			sqlStr = "SELECT SUBSTR(TO_CHAR(SYSTIMESTAMP,'SSFF'),0,"+len+") AS APMG FROM DUAL";
		if	(len==4)			sqlStr = "SELECT TO_CHAR(SYSDATE,'MISS') AS APMG FROM DUAL";
		if	(len==3)			sqlStr = "SELECT SUBSTR(TO_CHAR(SYSTIMESTAMP,'FF'),0,"+len+") AS APMG FROM DUAL";
		if	(len==2)			sqlStr = "SELECT TO_CHAR(SYSDATE,'HH24') AS APMG FROM DUAL";

		try	{getRowSql(sqlStr);}	catch (Exception e)	{obInterface.setApmg(App.getCurTimeStr(len));};
		dbtype="oracle";

		return obInterface.getApmg();
	}

	/*Get DataBase Time for mysql*/
	public String getCurTimeStrForMysql(int len) {
		String sqlStr = "SELECT DATE_FORMAT(sysdate(),'%H:%i:%s') AS APMG;";
		obInterface.setApmg(App.getCurTimeStr(len));
		if	(len==9)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%H%i%s%f') AS APMG;";
		if	(len==8)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%H:%i:%s') AS APMG;";
		if	(len==7)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%i%s%f') AS APMG;";
		if	(len==6)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%H%i%s') AS APMG;";
		if	(len==5)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%s%f') AS APMG;";
		if	(len==4)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%i%s') AS APMG;";
		if	(len==3)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%f') AS APMG;";
		if	(len==2)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%H') AS APMG;";

		try	{getRowSql(sqlStr);}	catch (Exception e)	{obInterface.setApmg(App.getCurTimeStr(len));};
		dbtype="mysql";

		return obInterface.getApmg();
	}


	/*Get DataBase Time for informix*/
	public String getCurTimeStrForInformix(int len) {
		//select to_date(current, 'YYYY-MM-DD HH24:MI:SS') from sysmaster
		//SELECT TODAY from sysmaster
		String sqlStr = "SELECT DATE_FORMAT(sysdate(),'%H:%i:%s') AS APMG;";
		obInterface.setApmg(App.getCurTimeStr(len));
		if	(len==9)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%H%i%s%f') AS APMG;";
		if	(len==8)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%H:%i:%s') AS APMG;";
		if	(len==7)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%i%s%f') AS APMG;";
		if	(len==6)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%H%i%s') AS APMG;";
		if	(len==5)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%s%f') AS APMG;";
		if	(len==4)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%i%s') AS APMG;";
		if	(len==3)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%f') AS APMG;";
		if	(len==2)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%H') AS APMG;";

		try	{getRowSql(sqlStr);}	catch (Exception e)	{obInterface.setApmg(App.getCurTimeStr(len));};
		dbtype="informix";

		return obInterface.getApmg();
	}

	/*Get DataBase Time for DB2*/
	public String getCurTimeStrForDb2(int len) {
		String sqlStr = "SELECT DATE_FORMAT(sysdate(),'%H:%i:%s') AS APMG FROM SYSIBM.SYSDUMMY1;";
		obInterface.setApmg(App.getCurTimeStr(len));
		if	(len==9)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%H%i%s%f') AS APMG FROM SYSIBM.SYSDUMMY1;";
		if	(len==8)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%H:%i:%s') AS APMG FROM SYSIBM.SYSDUMMY1;";
		if	(len==7)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%i%s%f') AS APMG FROM SYSIBM.SYSDUMMY1;";
		if	(len==6)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%H%i%s') AS APMG FROM SYSIBM.SYSDUMMY1;";
		if	(len==5)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%s%f') AS APMG FROM SYSIBM.SYSDUMMY1;";
		if	(len==4)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%i%s') AS APMG FROM SYSIBM.SYSDUMMY1;";
		if	(len==3)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%f') AS APMG FROM SYSIBM.SYSDUMMY1;";
		if	(len==2)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%H') AS APMG FROM SYSIBM.SYSDUMMY1;";

		try	{getRowSql(sqlStr);}	catch (Exception e)	{obInterface.setApmg(App.getCurTimeStr(len));};
		dbtype="db2";

		return obInterface.getApmg();
	}


	/*Get DataBase Date YYYYMMDD*/
	public String getCurDate8Str() {
		return getCurDateStr(8);
	}
	/*Get DataBase Date*/
	public String getCurDateStr() {
		return getCurDateStr(8);
	}

	/*Get DataBase Date*/
	public String getCurDateStr(int len) {
		String curdate=App.getCurDateStr(len);

		if	(dbtype!=null && dbtype.toLowerCase().startsWith("oracle"))			curdate=getCurDateStrForOracle(len);
		if	(dbtype!=null && dbtype.toLowerCase().startsWith("mysql"))			curdate=getCurDateStrForMysql(len);
		if	(dbtype!=null && dbtype.toLowerCase().indexOf("server")>=0)			curdate=getCurDateStrForSybase(len);
		if	(dbtype!=null && dbtype.toLowerCase().startsWith("informix"))		curdate=getCurDateStrForInformix(len);
		if	(dbtype!=null && dbtype.toLowerCase().startsWith("db2"))				curdate=getCurDateStrForDb2(len);
		//if	(dbtype!=null && dbtype.length()<=0)														curdate=App.getCurDateStr(len);
		curdate=curdate.replaceAll("[-/]","/");

		return curdate;
	}

	/*Get DataBase Date for oracle*/
	public String getCurDateStrForOracle(int len) {
		String sqlStr = "SELECT TO_CHAR(SYSDATE,'YYYYMMDD') AS APMG FROM DUAL";
		obInterface.setApmg(App.getCurDateStr(len));
		if	(len==21)			sqlStr = "SELECT SUBSTR(TO_CHAR(SYSTIMESTAMP,'YYYY-MM-DD HH24:MI:SSFF'),0,"+len+") AS APMG FROM DUAL";
		if	(len==19)			sqlStr = "SELECT TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS') AS APMG FROM DUAL";
		if	(len==10)			sqlStr = "SELECT TO_CHAR(SYSDATE,'YYYY-MM-DD') AS APMG FROM DUAL";
		if	(len==8)			sqlStr = "SELECT TO_CHAR(SYSDATE,'YYYYMMDD') AS APMG FROM DUAL";
		if	(len==7)			sqlStr = "SELECT TO_CHAR(SYSDATE,'YYYY-MM') AS APMG FROM DUAL";
		if	(len==6)			sqlStr = "SELECT TO_CHAR(SYSDATE,'YYYYMM') AS APMG FROM DUAL";
		if	(len==4)			sqlStr = "SELECT TO_CHAR(SYSDATE,'YYYY') AS APMG FROM DUAL";
		if	(len==2)			sqlStr = "SELECT TO_CHAR(SYSDATE,'YY') AS APMG FROM DUAL";

		try	{getRowSql(sqlStr);}	catch (Exception e)	{obInterface.setApmg(App.getCurDateStr(len));};
		dbtype="oracle";

		return obInterface.getApmg();
	}

	/*Get DataBase Date for sybase*/
	public String getCurDateStrForSybase(int len) {
		String sqlStr = "select datepart(hh,getdate())*10000 + datepart(mi,getdate())*100 + datepart(ss,getdate()) AS APMG";
		obInterface.setApmg(App.getCurDateStr(len));

		if	(len==21)			sqlStr = "SELECT rtrim(convert(char,getdate(),105))+' '+rtrim(convert(char,getdate(),108))+(convert(CHAR(3),datepart(Ms,getdate()))) AS APMG";
		if	(len==19)			sqlStr = "SELECT rtrim(convert(char,getdate(),105))+' '+(convert(char,getdate(),108)) AS APMG";
		if	(len==10)			sqlStr = "SELECT convert(char,getdate(),111) AS APMG";
		if	(len==8)			sqlStr = "SELECT convert(char,getdate(),112) AS APMG";
		if	(len==7)			sqlStr = "SELECT CONVERT(VARCHAR(7),getdate(),111) AS APMG";
		if	(len==6)			sqlStr = "SELECT datepart(yy,getdate())*100+datepart(mm,getdate()) AS APMG";
		if	(len==4)			sqlStr = "SELECT datepart(yy,getdate()) AS APMG";
		if	(len==2)			sqlStr = "SELECT datepart(yy,getdate())%100 AS APMG";

		try	{getRowSql(sqlStr);}	catch (Exception e)	{obInterface.setApmg(App.getCurDateStr(len));};
		dbtype="server";

		return obInterface.getApmg();
	}



	/*Get DataBase Date for mysql*/
	public String getCurDateStrForMysql(int len) {
		String sqlStr = "SELECT DATE_FORMAT(sysdate(),'%H%i%s');";
		obInterface.setApmg(App.getCurDateStr(len));

		if	(len==21)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%Y-%m-%d %H:%i:%s%f') AS APMG;";
		if	(len==19)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%Y-%m-%d %H:%i:%s') AS APMG;";
		if	(len==10)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%Y-%m-%d') AS APMG;";
		if	(len==8)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%Y%m%d') AS APMG;";
		if	(len==7)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%Y-%m') AS APMG;";
		if	(len==6)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%Y%m') AS APMG;";
		if	(len==4)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%Y') AS APMG;";
		if	(len==2)			sqlStr = "SELECT DATE_FORMAT(sysdate(),'%y') AS APMG;";

		try	{getRowSql(sqlStr);}	catch (Exception e)	{obInterface.setApmg(App.getCurDateStr(len));};
		dbtype="mysql";

		return obInterface.getApmg();
	}

	/*Get DataBase Date for informix*/
	public String getCurDateStrForInformix(int len) {
		String sqlStr = "SELECT TO_DATE(CURRENT,'YYYYMMDD') AS APMG FROM P001";
		obInterface.setApmg(App.getCurDateStr(len));

		if	(len==19)			sqlStr = "SELECT TO_DATE(CURRENT,'YYYY-MM-DD HH24:MI:SS') AS APMG FROM P001";
		if	(len==10)			sqlStr = "SELECT TO_DATE(CURRENT,'YYYY-MM-DD') AS APMG FROM P001";
		if	(len==8)			sqlStr = "SELECT TO_DATE(CURRENT,'YYYYMMDD') AS APMG FROM P001";
		if	(len==7)			sqlStr = "SELECT TO_DATE(CURRENT,'YYYY-MM') AS APMG FROM P001";
		if	(len==6)			sqlStr = "SELECT TO_DATE(CURRENT,'YYYYMM') AS APMG FROM P001";
		if	(len==4)			sqlStr = "SELECT TO_DATE(CURRENT,'YYYY') AS APMG FROM P001";
		if	(len==2)			sqlStr = "SELECT TO_DATE(CURRENT,'YY') AS APMG FROM P001";

		try	{getRowSql(sqlStr);}	catch (Exception e)	{obInterface.setApmg(App.getCurDateStr(len));};
		dbtype="informix";

		return obInterface.getApmg();
	}


	/*Get DataBase Date for DB2*/
	public String getCurDateStrForDb2(int len) {
		String sqlStr = "SELECT TO_DATE(CURRENT,'YYYYMMDD') AS APMG FROM SYSIBM.SYSDUMMY1 ";
		obInterface.setApmg(App.getCurDateStr(len));

		if	(len==19)			sqlStr = "SELECT TO_DATE(CURRENT,'YYYY-MM-DD HH24:MI:SS') AS APMG FROM SYSIBM.SYSDUMMY1 ";
		if	(len==10)			sqlStr = "SELECT TO_DATE(CURRENT,'YYYY-MM-DD') AS APMG FROM SYSIBM.SYSDUMMY1 ";
		if	(len==8)			sqlStr = "SELECT TO_DATE(CURRENT,'YYYYMMDD') AS APMG FROM SYSIBM.SYSDUMMY1 ";
		if	(len==7)			sqlStr = "SELECT TO_DATE(CURRENT,'YYYY-MM') AS APMG FROM SYSIBM.SYSDUMMY1 ";
		if	(len==6)			sqlStr = "SELECT TO_DATE(CURRENT,'YYYYMM') AS APMG FROM SYSIBM.SYSDUMMY1 ";
		if	(len==4)			sqlStr = "SELECT TO_DATE(CURRENT,'YYYY') AS APMG FROM SYSIBM.SYSDUMMY1 ";
		if	(len==2)			sqlStr = "SELECT TO_DATE(CURRENT,'YY') AS APMG FROM SYSIBM.SYSDUMMY1 ";

		try	{getRowSql(sqlStr);}	catch (Exception e)	{obInterface.setApmg(App.getCurDateStr(len));};
		dbtype="db2";

		return obInterface.getApmg();
	}



	//批量查询
	public Collection	select(String tablename)	{
		return select(tablename,obInterface.getWhere());
	}

	//批量查询
	public Collection	select(String tablename,String where) {
		CLog.writeLog("where["+where+"]");
		String	orderby="";
		String	defaultorderby=" ORDER BY 1,2";
		indexcolname=new String[30];
		if	(where!=null && where.length()>0 && where.trim().toLowerCase().indexOf("order")!=0){
			if	(where!=null && where.trim().length()>0 && (where.trim().toLowerCase().indexOf("where")<0 || where.trim().toLowerCase().indexOf("where")>9))		where=" WHERE "+where;
		}
		else if	(where!=null && where.length()>0 && where.trim().toLowerCase().indexOf("order")==0){
			orderby=" "+where;
			where="";
		}
		if	(where!=null && where.length()>0 && where.trim().toLowerCase().indexOf("order")>0){
			orderby=where.substring(where.toLowerCase().indexOf("order"));
			where=where.substring(0,where.toLowerCase().indexOf("order"));
		}
		where=" "+where;

		Collection ret=new ArrayList();
		if	(tablename==null || tablename.length()==0)		return ret;
		try		{
			if	(conn==null)			init();

			if	(prepstmt2!= null)		{prepstmt2.close();prepstmt2=null;}
			if	(rst != null)					{rst.close();rst=null;}
			prepstmt2=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase());
			rst=prepstmt2.executeQuery();
			meta=rst.getMetaData();
			colnum=meta.getColumnCount();


			try	{
				if	(prepstmt != null)		{prepstmt.close();stmt=null;}
				if	(rst != null)					{rst.close();rst=null;}

				prepstmt=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase()+"H");
				rst=prepstmt.executeQuery();
				//meta=rst.getMetaData();

				if	(colnum!=rst.getMetaData().getColumnCount()) throw new SQLException();
				//executeQuery("select * from	"+tablename.toUpperCase()+"H");
				sql="SELECT COUNT(*) FROM "+tablename.toUpperCase()+" "+where+" UNION ALL  SELECT COUNT(*) FROM "+tablename.trim().toUpperCase()+"H "+where;;
			}	catch	(SQLException e)	{
				sql="SELECT COUNT(*) FROM "+tablename.toUpperCase()+" "+where;
			}
			//CLog.writeLog("sql["+sql+"]");
			//prepstmt=conn.prepareStatement(sql);
			//rst=prepstmt.executeQuery();

			//stmt=conn.createStatement();
			//rst=stmt.executeQuery(sql);
			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			prepstmt=conn.prepareStatement(sql);
			rst=prepstmt.executeQuery();



			retNum=0;
			for	(;rst.next();)				retNum+=rst.getInt(1);
			//if	(rst.next())				retNum+=rst.getInt(1);
			//if	(rst.next())				retNum+=rst.getInt(1);
			//CLog.writeLog("sql["+sql+"]"+retNum);

			obInterface.setRetNum(retNum);
			int maxPage=retNum/obInterface.getPageSize();
			maxPage+= (retNum%obInterface.getPageSize())==0?0:1;
			int curPage=obInterface.getPageNo();
			int pageSize=obInterface.getPageSize();

			if	((curPage < 1) || (curPage > maxPage)) curPage=1;
			if	(curPage>maxPage)		maxPage=curPage;

			obInterface.setPageNo(curPage);
			obInterface.setMaxPage(maxPage);
			obInterface.setRowNum(0);
			if	(retNum<=0)	return ret;

			//prepstmt=conn.prepareStatement("SELECT * FROM	"+tablename.toUpperCase());
			//rst=prepstmt.executeQuery();
			//if	(rst.next())	meta=rst.getMetaData();

			//stmt=conn.createStatement();
			//meta=stmt.executeQuery("SELECT * FROM	"+tablename.toUpperCase()).getMetaData();


			if	(colnum==1)	defaultorderby=" ORDER BY 1";
			if	(orderby.length()<=0)	orderby=defaultorderby;
			/*
			if	(orderby.length()<=0){
				orderby+=" ORDER BY ";
				try	{
						dbMetaData=conn.getMetaData();
						if	(rst != null)		{rst.close();rst=null;}
						rst= dbMetaData.getIndexInfo(null,null,tablename.toUpperCase(),true,false);
						String indexname=null;
						for	(int j=0;rst.next();){
							//CLog.writeLog("colnum["+rst.getInt("ORDINAL_POSITION")+"]"+rst.getString("INDEX_NAME"));
							//if	(j>0 && rst.getInt("ORDINAL_POSITION")==1)	break;
							//if	(j>0)	sql+=",";
							//sql+=rst.getString("COLUMN_NAME");

							//CLog.writeLog(j+",colnum["+rst.getInt("ORDINAL_POSITION")+"],INDEX_NAME["+rst.getString("INDEX_NAME")+"],COLUMN_NAME["+rst.getString("COLUMN_NAME"));
							//if	(j>0 && rst.getInt("ORDINAL_POSITION")==1)	break;

							if	(rst.getString("INDEX_NAME")==null)	continue;
							if	(indexname!=null && !rst.getString("INDEX_NAME").equals(indexname))	break;
							indexname=rst.getString("INDEX_NAME");
							indexcolname[j]=rst.getString("COLUMN_NAME").toLowerCase();
							if	(j>0)	orderby+=",";
							//orderby+=indexcolname[j];
							orderby+=(j+1);
							j++;
							//sql+=myinvoke(obInterface,"get"+App.transFirstUpperCase(indexcolname[j].toLowerCase()));


						}
						//CLog.writeLog("sql=["+sql+"],indexcolnum=["+indexcolnum+"],tablename="+tablename);
				}	catch(Exception e)	{
						CLog.writeLog(e.toString());
						CLog.writeLog("sql=["+sql+"],indexcolnum=["+indexcolnum+"],tablename="+tablename);
				}	finally	{
						if	(j>=colnum)	orderby=defaultorderby;
				}
			}
			 */
			try	{
				if	(rst != null)					{rst.close();rst=null;}
				if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
				prepstmt=conn.prepareStatement("select * from	"+tablename.toUpperCase()+"H");
				rst=prepstmt.executeQuery();
				if	(colnum!=rst.getMetaData().getColumnCount()) throw new SQLException();
				sql="SELECT * FROM "+tablename.toUpperCase()+" "+where+" UNION ALL "+"SELECT * FROM "+tablename.trim().toUpperCase()+"H "+where+orderby;
				tablename+="H";
			}	catch	(SQLException e)	{
				sql="SELECT * FROM "+tablename.toUpperCase()+" "+where+orderby;
			}

			//CLog.writeLog("sql=["+sql+"],retNum="+retNum+",indexname="+indexcolnum);
			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			prepstmt=conn.prepareStatement(sql);
			rst=prepstmt.executeQuery();

			for	(i=0;i<(curPage-1)*pageSize;i++)		rst.next();

			for	(i=(curPage-1)*pageSize;(i<=curPage*pageSize-1)&&(i<=retNum-1);i++)			{
				rst.next();
				ObInterface temp=new ObInterface();
				temp.setSerialNo(i+1);
				temp.setRetNum(retNum);

				for	(int j=1;j<colnum+1;j++)
				{
					//CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]");
					//CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
					//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("Date"))
					try{
						if	(meta.getColumnType(j)==91)	{
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
							continue;
						}
						/*
						if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("date")>=0){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
							continue;
						}*/

						if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("String")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getString(meta.getColumnName(j)));
							continue;
						}
						else if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("int")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getInt(meta.getColumnName(j)));
							continue;
						}
						else if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("double")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDouble(meta.getColumnName(j)));
							continue;
						}
						else if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("float")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getFloat(meta.getColumnName(j)));
							continue;
						}
					}	catch	(Exception e)	{
						CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]");
						CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
						CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
					}
				}

				ret.add(temp);
			}
			obInterface.setRowNum(i-(curPage-1)*pageSize);

		}	catch(Exception e)	{
			CLog.writeLog("sql=["+sql+"]");
			CLog.writeLog("where="+where);
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			//throw e;
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+tablename+"("+obInterface.getRowNum()+"/"+retNum+"),"+where.trim()+","+orderby.trim());
			orderby=null;
			defaultorderby=null;
			close();
		}

		return ret;
	}


	//批量查询
	public Collection	select1(String tablename)	{
		return select1(tablename,obInterface.getWhere());
	}

	public Collection	select1(String tablename,String where) {
		//System.out.println("tableName=="+tablename+"   where==="+where);
		String term=where;
		//CLog.writeLog("where["+where+"]");
		String	orderby="";
		String	defaultorderby=" ORDER BY 1,2";
		indexcolname=new String[30];
		if	(where!=null && where.length()>0 && where.trim().toLowerCase().indexOf("order")!=0){
			if	(where!=null && where.trim().length()>0 && (where.trim().toLowerCase().indexOf("where")<0 || where.trim().toLowerCase().indexOf("where")>9))		where=" WHERE "+where;
		}
		else if	(where!=null && where.length()>0 && where.trim().toLowerCase().indexOf("order")==0){
			orderby=" "+where;
			where="";
		}
		if	(where!=null && where.length()>0 && where.trim().toLowerCase().indexOf("order")>0){
			orderby=where.substring(where.toLowerCase().indexOf("order"));
			where=where.substring(0,where.toLowerCase().indexOf("order"));
		}
		where=" "+where;
		Collection ret=new ArrayList();

		if	(tablename==null || tablename.length()==0)		return ret;
		try		{
			if	(conn==null)			init();

			if	(prepstmt2!= null)		{prepstmt2.close();prepstmt2=null;}
			if	(rst != null)					{rst.close();rst=null;}
			prepstmt2=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase());
			rst=prepstmt2.executeQuery();
			meta=rst.getMetaData();
			colnum=meta.getColumnCount();



			try	{
				if	(prepstmt != null)		{prepstmt.close();stmt=null;}
				if	(rst != null)					{rst.close();rst=null;}

				prepstmt=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase()+"H");
				rst=prepstmt.executeQuery();


				if	(colnum!=rst.getMetaData().getColumnCount()) throw new SQLException();
				//executeQuery("select * from	"+tablename.toUpperCase()+"H");
				sql="SELECT COUNT(*) FROM "+tablename.toUpperCase()+" "+where+" UNION ALL  SELECT COUNT(*) FROM "+tablename.trim().toUpperCase()+"H "+where;;
			}	catch	(SQLException e)	{
				sql="SELECT COUNT(*) FROM "+tablename.toUpperCase()+" "+where;
			}

			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			//System.out.println("page sql = "+sql);
			prepstmt=conn.prepareStatement(sql);
			rst=prepstmt.executeQuery();



			retNum=0;
			for	(;rst.next();)				retNum+=rst.getInt(1);


			obInterface.setRetNum(retNum);
			int maxPage=retNum/obInterface.getPageSize();
			maxPage+= (retNum%obInterface.getPageSize())==0?0:1;
			int curPage=obInterface.getPageNo();
			int pageSize=obInterface.getPageSize();

			if	((curPage < 1) || (curPage > maxPage)) curPage=1;

			if	(curPage>maxPage)		maxPage=curPage;
			//System.out.println("curPage = "+curPage);
			obInterface.setPageNo(curPage);
			obInterface.setMaxPage(maxPage);
			obInterface.setRowNum(0);
			if	(retNum<=0)	return ret;
			if	(colnum==1)	defaultorderby=" ORDER BY 1";
			if	(orderby.length()<=0)	orderby=defaultorderby;

			try	{
				if	(rst != null)					{rst.close();rst=null;}
				if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
				prepstmt=conn.prepareStatement("select * from	"+tablename.toUpperCase()+"H");
				rst=prepstmt.executeQuery();
				if	(colnum!=rst.getMetaData().getColumnCount()) throw new SQLException();
				sql="SELECT * FROM "+tablename.toUpperCase()+" "+where+" UNION ALL "+"SELECT * FROM "+tablename.trim().toUpperCase()+"H "+where+orderby;
				tablename+="H";

			}	catch	(SQLException e)	{
				sql="SELECT * FROM "+tablename.toUpperCase()+" "+where+orderby;
			}
			//	System.out.println("sql1111==="+sql);
			int M=(curPage-1)*pageSize;
			int N=curPage*pageSize-1;
			int count=M+N;

			if(obInterface.getBstp().toLowerCase().equals("noreply")){				
				sql="SELECT * FROM (SELECT ROWNUM r,t1.* From "+tablename+" t1 where rownum <= "+(curPage*pageSize)+" and "+term+") t2  where t2.r > "+(curPage-1)*pageSize;
			}

			if(tablename.equals( "T_TXNLOG" )){
				sql="select * from (SELECT ROWNUM r,t2.* FROM (SELECT * From T_TXNLOG t1 where 1=1 and "+term+") t2  where rownum <= "+(curPage*pageSize)+ ")t3 where t3.r>"+(curPage-1)*pageSize;
			}
			else if(tablename.equals( "T_TXNLOG" ) && !obInterface.getOpdt().equals( "" )){
				sql="SELECT * FROM (SELECT ROWNUM r,t1.* From "+tablename+" t1 where rownum <= "+(curPage*pageSize)+" and "+term+") t2  where t2.r > "+(curPage-1)*pageSize;				
			}
			else if(tablename.equals( "T_TML_EVENT" )){
				sql="SELECT * FROM (SELECT ROWNUM r,t1.* From "+tablename+" t1 where "+term+" and rownum <= "+(curPage*pageSize)+") t2  where t2.r > "+(curPage-1)*pageSize;				
			}
			else if(tablename.equals( "C001" )){
				sql="SELECT * FROM (SELECT ROWNUM r,t1.* From "+tablename+" t1 where rownum <= "+(curPage*pageSize)+" and "+term+") t2  where t2.r > "+(curPage-1)*pageSize;				
			}			
			else if(tablename.equals( "F001" )){
				sql="SELECT * FROM (SELECT ROWNUM r,t1.* From "+tablename+" t1 where rownum <= "+(curPage*pageSize)+" and "+term+") t2  where t2.r > "+(curPage-1)*pageSize;				
			}
			else if(tablename.equals( "V001" )){
				sql="SELECT * FROM (SELECT ROWNUM r,t1.* From "+tablename+" t1 where rownum <= "+(curPage*pageSize)+" and "+term+") t2  where t2.r > "+(curPage-1)*pageSize;				
			}
			else if(tablename.equals( "V_V001" )){
				sql="SELECT * FROM (SELECT ROWNUM r,t1.* From "+tablename+" t1 where rownum <= "+(curPage*pageSize)+" and "+term+") t2  where t2.r > "+(curPage-1)*pageSize;				
			}
			//System.out.println("tablename is "+tablename);
			else if(tablename.equals( "V_V002" )){
				sql="SELECT * FROM (SELECT ROWNUM r,t1.* From "+tablename+" t1 where rownum <= "+(curPage*pageSize)+" and "+term+") t2  where t2.r > "+(curPage-1)*pageSize;				
			}
			else if(tablename.equals( "V_V003" )){
				sql="SELECT * FROM (SELECT ROWNUM r,t1.* From "+tablename+" t1 where rownum <= "+(curPage*pageSize)+" and "+term+") t2  where t2.r > "+(curPage-1)*pageSize;				
			}
			else if(tablename.equals( "V_V004" )){
				sql="SELECT * FROM (SELECT ROWNUM r,t1.* From "+tablename+" t1 where rownum <= "+(curPage*pageSize)+" and "+term+") t2  where t2.r > "+(curPage-1)*pageSize;				
			}
			else if(tablename.equals( "V_V005" )){
				sql="SELECT * FROM (SELECT ROWNUM r,t1.* From "+tablename+" t1 where rownum <= "+(curPage*pageSize)+" and "+term+") t2  where t2.r > "+(curPage-1)*pageSize;				
			}
			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			prepstmt=conn.prepareStatement(sql);
			rst=prepstmt.executeQuery();			
			//	System.out.println(sql);

			//for	(i=0;i<(curPage-1)*pageSize;i++)		rst.next();
			//System.out.println("i=="+(curPage-1)*pageSize+"  i1<"+(curPage*pageSize-1)+"    i2<"+(retNum-1));
			//System.out.println("row=="+(curPage*pageSize-(curPage-1)*pageSize));
			//for	(i=(curPage-1)*pageSize;(i<=curPage*pageSize-1)&&(i<=retNum-1);i++)			{
			int aa=1;
			for	(i=0;i<=curPage*pageSize-(curPage-1)*pageSize;i++)			{
				rst.next();
				ObInterface temp=new ObInterface();
				temp.setSerialNo(M+aa);
				aa++;
				temp.setRetNum(retNum);
				//System.out.println("SerialNo=="+temp.getSerialNo()+"   retNum==="+temp.getRetNum());

				for	(int j=1;j<colnum+1;j++)
				{

					CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]");
					CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
					//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("Date"))
					try{
						if	(meta.getColumnType(j)==91)	{
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
							continue;
						}
						/*
						if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("date")>=0){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
							continue;
						}*/

						if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("String")){

							//System.out.println("meta.getColumnName(j).toLowerCase()==="+meta.getColumnName(j).toLowerCase());							
							//System.out.println("rst.getString(meta.getColumnName(j)====="+rst.getString(meta.getColumnName(j)));
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getString(meta.getColumnName(j)));
							continue;
						}
						else if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("int")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getInt(meta.getColumnName(j)));
							continue;
						}
						else if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("double")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDouble(meta.getColumnName(j)));
							continue;
						}
						else if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("float")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getFloat(meta.getColumnName(j)));
							continue;
						}
					}	catch	(Exception e)	{

						CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]");
						CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
						CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
					}
				}

				ret.add(temp);

			}
			obInterface.setRowNum(i-(curPage-1)*pageSize);

		}	catch(Exception e)	{
			CLog.writeLog("sql=["+sql+"]");
			CLog.writeLog("where="+where);
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			//throw e;  
		}	finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+tablename+"("+obInterface.getRowNum()+"/"+retNum+"),"+where.trim()+","+orderby.trim());
			orderby=null;
			defaultorderby=null;
			close();
		}
		return ret;
	}
	public Collection selectMoniTrans(String tablename, String where){
		String term=where;
		CLog.writeLog("where["+where+"]");
		String	orderby="";
		String	defaultorderby=" ORDER BY 1,2";
		String sql="";
		Collection ret=new ArrayList();

		//转换where 条件
		if	(where!=null && where.length()>0 && where.trim().toLowerCase().indexOf("order")!=0){
			if	(where!=null && where.trim().length()>0 && (where.trim().toLowerCase().indexOf("where")<0 || where.trim().toLowerCase().indexOf("where")>9))		where=" WHERE "+where;
		}
		else if	(where!=null && where.length()>0 && where.trim().toLowerCase().indexOf("order")==0){
			orderby=" "+where;
			where="";
		}
		//增加健壮性
		if	(tablename==null || tablename.length()==0)		return ret;
		try{
			retNum=0;
			try	{

				//获取连接
				if	(conn==null)			init();

				if	(prepstmt2!= null)		{prepstmt2.close();prepstmt2=null;}
				if	(rst != null)					{rst.close();rst=null;}
				prepstmt2=conn.prepareStatement("SELECT * FROM "+tablename.toUpperCase());
				rst=prepstmt2.executeQuery();
				//获取列集合
				meta=rst.getMetaData();
				colnum=meta.getColumnCount();

				if	(prepstmt != null){
					prepstmt.close();stmt=null;
				}
				if	(rst != null){
					rst.close();rst=null;
				}
				sql="SELECT COUNT(*) FROM "+tablename.toUpperCase();
			}	catch	(SQLException e)	{
				sql="SELECT COUNT(*) FROM "+tablename.toUpperCase();
			}

			prepstmt=conn.prepareStatement(sql);
			rst=prepstmt.executeQuery();
			for	(;rst.next();)	{			
				retNum+=rst.getInt(1);
			}
			obInterface.setRetNum(retNum);
			int maxPage=retNum/obInterface.getPageSize();
			maxPage+= (retNum%obInterface.getPageSize())==0?0:1;
			int curPage=obInterface.getPageNo();
			int pageSize=obInterface.getPageSize();

			if	((curPage < 1) || (curPage > maxPage)) curPage=1;

			if	(curPage>maxPage)		maxPage=curPage;
			obInterface.setPageNo(curPage);
			obInterface.setMaxPage(maxPage);
			obInterface.setRowNum(0);
			if	(retNum<=0)	return ret;
			if	(colnum==1)	defaultorderby=" ORDER BY 1";
			if	(orderby.length()<=0)	orderby=defaultorderby;

			if(tablename.equals( "T_TXNLOG" )){
				sql="select * from (SELECT ROWNUM r,t2.* FROM (SELECT * From T_TXNLOG t1 where 1=1  and "+term+") t2  where rownum <= "+(curPage*pageSize)+ ")t3 where t3.r>"+(curPage-1)*pageSize;
			}

			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			prepstmt=conn.prepareStatement(sql);
			rst=prepstmt.executeQuery();


			int M=(curPage-1)*pageSize;
			int N=curPage*pageSize-1;
			int count=M+N;

			//  处理结果集
			int aa=1;
			for	(i=0;i<=curPage*pageSize-(curPage-1)*pageSize;i++)			{
				rst.next();
				ObInterface temp=new ObInterface();
				temp.setSerialNo(M+aa);
				aa++;
				temp.setRetNum(retNum);

				for	(int j=1;j<colnum+1;j++)
				{
					try{
						if	(meta.getColumnType(j)==91)	{
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
							continue;
						}
						/*
						if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("date")>=0){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
							continue;
						}*/

						if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("String")){

							//System.out.println("meta.getColumnName(j).toLowerCase()==="+meta.getColumnName(j).toLowerCase());							
							//System.out.println("rst.getString(meta.getColumnName(j)====="+rst.getString(meta.getColumnName(j)));
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getString(meta.getColumnName(j)));
							continue;
						}
						else if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("int")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getInt(meta.getColumnName(j)));
							continue;
						}
						else if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("double")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDouble(meta.getColumnName(j)));
							continue;
						}
						else if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("float")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getFloat(meta.getColumnName(j)));
							continue;
						}

					}	catch	(Exception e)	{
						//e.printStackTrace();
						CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]");
						CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
						CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
					}
				}
				ret.add(temp);

			}

			obInterface.setRowNum(i-(curPage-1)*pageSize);

		}catch(Exception e){
			//e.printStackTrace();
		}finally	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+tablename+"("+obInterface.getRowNum()+"/"+retNum+"),"+where.trim()+","+orderby.trim());
			orderby=null;
			defaultorderby=null;
			close();
		}

		return ret;
	} 
	//select for card system
	public Collection	select3(String sql,String cout) {
		//System.out.println("tableName=="+tablename+"   where==="+where);
		String mysql = sql;
		Collection ret=new ArrayList();
		//	System.out.println(new java.util.Date());
		try		{
			if	(conn==null)			init();

			if	(prepstmt2!= null)		{prepstmt2.close();prepstmt2=null;}
			if	(rst != null)					{rst.close();rst=null;}
			prepstmt2=conn.prepareStatement(sql);
			rst=prepstmt2.executeQuery();
			meta=rst.getMetaData();
			colnum=meta.getColumnCount();

			if	(colnum!=rst.getMetaData().getColumnCount()) throw new SQLException();

			//sql="SELECT COUNT(*) FROM ("+cout+")" ;

			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}

			//	System.out.println("page sql = "+cout);
			prepstmt=conn.prepareStatement(cout);
			rst=prepstmt.executeQuery();

			retNum=0;
			for	(;rst.next();)				retNum+=rst.getInt(1);

			obInterface.setRetNum(retNum);
			int maxPage=retNum/obInterface.getPageSize();
			maxPage+= (retNum%obInterface.getPageSize())==0?0:1;
			int curPage=obInterface.getPageNo();
			int pageSize=obInterface.getPageSize();

			if	((curPage < 1) || (curPage > maxPage)) curPage=1;

			if	(curPage>maxPage)		maxPage=curPage;
			//System.out.println("curPage = "+curPage);
			obInterface.setPageNo(curPage);
			obInterface.setMaxPage(maxPage);
			obInterface.setRowNum(0);
			if	(retNum<=0)	return ret;

			int M=(curPage-1)*pageSize;
			int N=curPage*pageSize-1;
			int count=M+N;


			mysql="SELECT * FROM ( "+ mysql +" and rownum <= "+(curPage*pageSize)+" ) t  where t.r > "+(curPage-1)*pageSize;


			//dispose of the result set
			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			//System.out.println(mysql);

			prepstmt=conn.prepareStatement(mysql);

			rst=prepstmt.executeQuery();			

			//	System.out.println(new java.util.Date());
			int aa=1;
			for	(i=0;i<=curPage*pageSize-(curPage-1)*pageSize;i++)			{
				rst.next();
				ObInterface temp=new ObInterface();
				temp.setSerialNo(M+aa);
				aa++;
				temp.setRetNum(retNum);
				//System.out.println("SerialNo=="+temp.getSerialNo()+"   retNum==="+temp.getRetNum());

				for	(int j=1;j<colnum+1;j++)
				{

					CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]");
					CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
					//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("Date"))
					try{
						if	(meta.getColumnType(j)==91)	{
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
							continue;
						}
						/*
						if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("date")>=0){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
							continue;
						}*/

						if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("String")){

							//System.out.println("meta.getColumnName(j).toLowerCase()==="+meta.getColumnName(j).toLowerCase());							
							//System.out.println("rst.getString(meta.getColumnName(j)====="+rst.getString(meta.getColumnName(j)));
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getString(meta.getColumnName(j)));
							continue;
						}
						else if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("int")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getInt(meta.getColumnName(j)));
							continue;
						}
						else if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("double")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDouble(meta.getColumnName(j)));
							continue;
						}
						else if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("float")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getFloat(meta.getColumnName(j)));
							continue;
						}
					}	catch	(Exception e)	{

						CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]");
						CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
						CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
					}
				}

				ret.add(temp);

			}
			obInterface.setRowNum(i-(curPage-1)*pageSize);

		}	catch(Exception e)	{
			CLog.writeLog("sql=["+sql+"]");
			CLog.writeLog("where="+where);
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			//throw e;  
		}	finally	{
			//	CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+tablename+"("+obInterface.getRowNum()+"/"+retNum+"),"+where.trim()+","+orderby.trim());
			close();
		}
		return ret;
	}
	public Collection	select4(String sql,String cout) {
		//System.out.println("tableName=="+tablename+"   where==="+where);
		String mysql = sql;
		Collection ret=new ArrayList();
		//	System.out.println(new java.util.Date());
		try		{
			if	(conn==null)			init();

			if	(prepstmt2!= null)		{prepstmt2.close();prepstmt2=null;}
			if	(rst != null)					{rst.close();rst=null;}
			prepstmt2=conn.prepareStatement(sql);
			rst=prepstmt2.executeQuery();
			meta=rst.getMetaData();
			colnum=meta.getColumnCount();

			if	(colnum!=rst.getMetaData().getColumnCount()) throw new SQLException();

			//sql="SELECT COUNT(*) FROM ("+cout+")" ;

			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}

			//	System.out.println("page sql = "+cout);
			prepstmt=conn.prepareStatement(cout);
			rst=prepstmt.executeQuery();

			retNum=0;
			for	(;rst.next();)				retNum+=rst.getInt(1);

			obInterface.setRetNum(retNum);
			int maxPage=retNum/obInterface.getPageSize();
			maxPage+= (retNum%obInterface.getPageSize())==0?0:1;
			int curPage=obInterface.getPageNo();
			int pageSize=obInterface.getPageSize();

			if	((curPage < 1) || (curPage > maxPage)) curPage=1;

			if	(curPage>maxPage)		maxPage=curPage;
			//System.out.println("curPage = "+curPage);
			obInterface.setPageNo(curPage);
			obInterface.setMaxPage(maxPage);
			obInterface.setRowNum(0);
			if	(retNum<=0)	return ret;

			int M=(curPage-1)*pageSize;
			int N=curPage*pageSize-1;
			int count=M+N;


			mysql="SELECT * FROM ( "+ mysql +" and rownum <= "+(curPage*pageSize)+" ORDER BY c_date DESC,c_time DESC,i_feelsn DESC) t  where t.r > "+(curPage-1)*pageSize;


			//dispose of the result set
			if	(rst != null)					{rst.close();rst=null;}
			if	(prepstmt != null)		{prepstmt.close();prepstmt=null;}
			//System.out.println(mysql);

			prepstmt=conn.prepareStatement(mysql);

			rst=prepstmt.executeQuery();			

			//	System.out.println(new java.util.Date());
			int aa=1;
			for	(i=0;i<=curPage*pageSize-(curPage-1)*pageSize;i++)			{
				rst.next();
				ObInterface temp=new ObInterface();
				temp.setSerialNo(M+aa);
				aa++;
				temp.setRetNum(retNum);
				//System.out.println("SerialNo=="+temp.getSerialNo()+"   retNum==="+temp.getRetNum());

				for	(int j=1;j<colnum+1;j++)
				{

					CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]");
					CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
					//if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("Date"))
					try{
						if	(meta.getColumnType(j)==91)	{
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
							continue;
						}
						/*
						if	(getVarType(obInterface,meta.getColumnName(j)).toLowerCase().indexOf("date")>=0){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDate(meta.getColumnName(j)));
							continue;
						}*/

						if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("String")){

							//System.out.println("meta.getColumnName(j).toLowerCase()==="+meta.getColumnName(j).toLowerCase());							
							//System.out.println("rst.getString(meta.getColumnName(j)====="+rst.getString(meta.getColumnName(j)));
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getString(meta.getColumnName(j)));
							continue;
						}
						else if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("int")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getInt(meta.getColumnName(j)));
							continue;
						}
						else if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("double")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getDouble(meta.getColumnName(j)));
							continue;
						}
						else if	(typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j)).equalsIgnoreCase("float")){
							temp.setFieldByName(meta.getColumnName(j).toLowerCase(),rst.getFloat(meta.getColumnName(j)));
							continue;
						}
					}	catch	(Exception e)	{

						CLog.writeLog("colnum="+colnum+",j="+j+",type["+typeToString(meta.getColumnType(j),meta.getColumnDisplaySize(j),meta.getPrecision(j),meta.getScale(j))+"]"+"colname["+meta.getColumnName(j)+"]");
						CLog.writeLog("setFieldByName("+meta.getColumnName(j)+","+rst.getString(meta.getColumnName(j))+"),["+meta.getColumnType(j)+","+meta.getColumnDisplaySize(j)+","+meta.getPrecision(j)+","+meta.getScale(j)+"]");
						CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber());
					}
				}

				ret.add(temp);

			}
			obInterface.setRowNum(i-(curPage-1)*pageSize);

		}	catch(Exception e)	{
			CLog.writeLog("sql=["+sql+"]");
			CLog.writeLog("where="+where);
			CLog.writeLog(e.toString());
			//Rollback();
			//e.printStackTrace();
			//throw e;  
		}	finally	{
			//	CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+tablename+"("+obInterface.getRowNum()+"/"+retNum+"),"+where.trim()+","+orderby.trim());
			close();
		}
		return ret;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}

