
public class TestThread {

	public static void main(String[] args) {
		
		MyObj objone = new MyObj();
		
		Thread addThread = new Thread(new AddThread(objone));
		addThread.start();
		
		Thread removeThread = new Thread(new RemoveThread(objone));
		removeThread.start();

	}

}
