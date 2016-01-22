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
		
		ViewTransParser wt = new ViewTransParser("100020501013H68NL020000020admin id of ATM for feel view0 000 0 03330303030320$0$0$0$2$2$2$2$4$3$3$1$2$2$2$2$0$0$0$0$50000000$20000000$10000000$5000000$0$0$0$0$VND$VND$VND$VND$0$0$0$0$0$0$50$0$0$0$0$0$0$2$5$6$1$3$0$0$0$2$49$6$2015122314015120151224094552");
		wt.run();

	}
}
