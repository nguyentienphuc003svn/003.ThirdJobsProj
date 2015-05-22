import java.util.LinkedList;
import java.util.Queue;


public class MyObj {

	public Queue<String> myQueue = new LinkedList<String>();
	public static Object lock = new Object();

	public void addQueue(String value)
	{
		synchronized(lock) 
		{
			try
			{					    	
				myQueue.add(value);
				lock.wait();
			}
			catch (Exception e) {}
		}
	}


	public void removeQueue()
	{
		synchronized(lock) 
		{
			try
			{					    	
				myQueue.remove();
				lock.wait();
			}
			catch (Exception e) {}
		}
	}
}
