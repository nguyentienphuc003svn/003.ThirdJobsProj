import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class RemoveThread implements Runnable {

	public MyObj objone = new MyObj();

	public RemoveThread(MyObj objone)
	{
		this.objone = objone;
	}

	public void run()
	{
		System.out.println("Thread remove is started OK");

		while(true)
		{
			if (objone.myQueue.isEmpty() || objone.myQueue.size() <=0)
			{
				try{
					System.out.println("No element to remove. Sleep for 17 sec");
					Thread.sleep(1000*17);
				}
				catch (Exception e) 
				{
					System.out.println("Error on thread remove");
					e.getMessage();
					e.toString();
					e.printStackTrace();
				}

			}
			else
			{
				objone.myQueue.remove();
			}
			
			Queue<String> myTempQueue = new LinkedList<String>(objone.myQueue);
			Iterator<String> it = myTempQueue.iterator();			
			while(it.hasNext())
			{
				System.out.print(it.next() + ">>>");
			}
			System.out.println("\n");
		}
	}
}
