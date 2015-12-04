/*******************************
 * @file: JdbcUtils_JNDI.java
 * @author: Jeremy Lee
 * @copyright: GRGBanking Equipment Co., Ltd
 * @version: 1.0
 * @date: 2015/4/9
 * @brief: æ•°æ�®åº“è¿�æ�¥æ± æ“�ä½œ
 * @details: ä½¿ç”¨springæ¡†æ�¶ä¸­ç�„æ•°æ�®åº“è¿�æ�¥æ± æœºåˆ¶
 *           ç¨‹åº�è¿�è¡Œæ—¶ä»�cmsbeans.xmlä¸­è¯»å�–æ•°æ�®åº“ç�„é…�ç½®ä¿¡æ�¯
 * @history
 *******************************/

package ccas;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.*;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @ClassName: JdbcUtils_JNDI
 * @Description: æ•°æ�®åº“è¿�æ�¥å·¥å…·ç±»
 *
 */ 
public class JdbcUtils_JNDI {
	private static String xmlfile = "cmsbeans.xml";
	private static XmlBeanFactory factory = null;
	private static DataSource ds = null;

	//åœ¨é�™æ€�ä»£ç �å�—ä¸­åˆ›å»ºæ•°æ�®åº“è¿�æ�¥æ± 
	static{
		try{
			if (factory == null)
				factory = new XmlBeanFactory(new ClassPathResource(xmlfile));
			if (ds == null)
				ds = (DataSource) factory.getBean("dataSource");
		}catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * @Method: getConnection
	 * @Description: ä»�æ•°æ�®æº�ä¸­è�·å�–æ•°æ�®åº“è¿�æ�¥
	 * @return Connection
	 * @throws SQLException
	 */ 
	public static synchronized Connection getConnection() throws SQLException{
		//ä»�æ•°æ�®æº�ä¸­è�·å�–æ•°æ�®åº“è¿�æ�¥
		return ds.getConnection();
	}

	/**
	 * @Method: release
	 * @Description: é‡�æ”¾èµ„æº�ï¼Œ
	 * é‡�æ”¾ç�„èµ„æº�åŒ…æ‹¬Connectionæ•°æ�®åº“è¿�æ�¥å¯¹è±¡ï¼Œè´Ÿè´£æ‰§è¡ŒSQLå‘½ä»¤ç�„Statementå¯¹è±¡ï¼Œå­˜å‚¨æŸ¥è¯¢ç»“æ�œç�„ResultSetå¯¹è±¡
	 * @param conn
	 * @param st
	 * @param rs
	 */ 
	public static void release(Connection conn,Statement st,ResultSet rs){
		if(rs!=null){
			try{
				//å…³é—­å­˜å‚¨æŸ¥è¯¢ç»“æ�œç�„ResultSetå¯¹è±¡
				rs.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
			rs = null;
		}
		if(st!=null){
			try{
				//å…³é—­è´Ÿè´£æ‰§è¡ŒSQLå‘½ä»¤ç�„Statementå¯¹è±¡
				st.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(conn!=null){
			try{
				//å°†Connectionè¿�æ�¥å¯¹è±¡è¿˜ç»™æ•°æ�®åº“è¿�æ�¥æ± 
				conn.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

class JDBCDBConn{
	private static JdbcUtils_JNDI	jdbcUtils = null;
	private Connection conn = null;
	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public PreparedStatement getPrepstatement() {
		return prepstatement;
	}

	public void setPrepstatement(PreparedStatement prepstatement) {
		this.prepstatement = prepstatement;
	}

	public ResultSet getRestSet() {
		return restSet;
	}

	public void setRestSet(ResultSet restSet) {
		this.restSet = restSet;
	}

	private PreparedStatement prepstatement=null;
	private ResultSet restSet = null;

	public JDBCDBConn() throws SQLException {
		if (jdbcUtils == null) jdbcUtils = new JdbcUtils_JNDI();
		conn = jdbcUtils.getConnection();
	}

	/**
	 * è�·å�–PreparedStatement
	 * 
	 * @param sql
	 * @throws SQLException
	 */
	private void getPreparedStatement(String sql) throws SQLException {
		prepstatement = conn.prepareStatement(sql);
	}

	/**
	 * ç”¨äº�æŸ¥è¯¢ï¼Œè¿”å›�ç»“æ�œé›†
	 * 
	 * @param sql
	 *            sqlè¯­å�¥
	 * @return ç»“æ�œé›†
	 * @throws SQLException
	 */
	public ResultSet executeQuery() throws SQLException	{
		if	(prepstatement != null)	{
			return prepstatement.executeQuery();
		} else
			return null;
	}

	/**
	 * ç”¨äº�å¢�åˆ æ”¹
	 * 
	 * @param sql
	 *            sqlè¯­å�¥
	 * @return å½±å“�è¡Œæ•°
	 * @throws SQLException
	 */
	public boolean executeUpdate(String sql) throws SQLException {

		try {
			getPreparedStatement(sql);
			if (prepstatement.executeUpdate() >= 0) {
				return true;
			}
		} catch (SQLException e) {
			CLog.writeLog("e="+e);
			CLog.writeLog(",errorcode="+e.getErrorCode());
			CLog.writeLog(",getMessage="+e.getMessage());
			CLog.writeLog(",Exception="+e.toString());
			throw new SQLException(e);
		}
		return false;
	}

	public List SgbSelect(String sql) throws SQLException 
	{
		ArrayList list = new ArrayList();

		try {
			getPreparedStatement(sql);

			ResultSet rs = prepstatement.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columns = md.getColumnCount();

			while (rs.next()){
				HashMap row = new HashMap(columns);
				for(int i=1; i<=columns; ++i){           
					row.put(md.getColumnName(i),rs.getObject(i));
				}
				list.add(row);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(",getMessage="+e.getMessage());			
			
			CLog.writeLogVS("e="+e);
			CLog.writeLogVS(",getMessage="+e.getMessage());
			CLog.writeLogVS(",Exception="+e.toString());
			
			throw new SQLException(e);
		}
		
		return list;
	}

	public void	setAutoCommit(boolean flag)	{
		try	{
			conn.setAutoCommit(flag);
		} catch(SQLException e) {
			CLog.writeLog("Mysql setAutoCommit error: "+e);
		}
	}
	public boolean	getAutoCommit(){
		boolean retboolean = false;
		try	{
			retboolean=conn.getAutoCommit();
		} catch(SQLException e) {
			CLog.writeLog("Mysql getAutoCommit error: "+e);
		}
		return retboolean;
	}
	public boolean Commit(){
		try	{
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

	/**
	 * é‡�æ”¾èµ„æº�
	 */
	public void free(Connection conn,Statement st,ResultSet rs)
	{
		jdbcUtils.release(conn, st, rs);
	}

	public void free()
	{
		free(conn, prepstatement, restSet);
	}
}
