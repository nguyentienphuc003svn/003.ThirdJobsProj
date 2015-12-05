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
		
		ViewTransParser wt = new ViewTransParser("AAAAAAAA");
		wt.run();

	}
}
