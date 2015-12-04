package ccas;

public class SendViewServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String testString = "10002040201G21L100000100Admin0 0 3 0 3 0 0 3 0 0 0 0 0 0 9 000133 0231$2$1$2$3$3$0$0$0$0$0$0$20000$100000$50000$500000$0$0$VND$VND$VND$VND$VND$VND$100$0$300$166$0$0$0$0$0$0$0$0$0$0$88$0$0$0$20130926120000201309261200000$0$0$0$0$0$0105$0$2$3$0$0$0$0$0$0$0$0$";
		
		ViewUnpack myView = new ViewUnpack();
		
		new ViewUnpack();
		while (true)
		{
			myView.insert(testString);
			//myView.event(1);
			
			//myView.InvalidSess();
		}
		
	}

}

