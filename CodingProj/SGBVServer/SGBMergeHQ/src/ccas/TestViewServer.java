package ccas;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class TestViewServer {

	public static void main(String[] args) {
		ViewUnpack vu = new ViewUnpack();		
		vu.event(500);
		
//		ObInterface obInterface = new ObInterface();
//		Mysql ms = new Mysql(obInterface);
//		ms.setObInterface(obInterface);
//				
//		try
//		{
//			//ms.init();
//			
//			
//			
////			Properties prop = new Properties();
////			InputStream input = new FileInputStream("VSContext.xml");
////			prop.load(input);
//			
//
//			VABDes odes = new VABDes("31323334353637383132333435363738");
//	        String strDes = VABDes.hexStringToString(odes.dec("50F92C854FB1DB3E"), 2);
//	        System.out.println((new StringBuilder(" strDes ")).append(strDes).toString());	        
//	        System.out.println(VABDes.hexStringToString(new VABDes("31323334353637383132333435363738").dec("50F92C854FB1DB3E"), 2));
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}

	}
}
