import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class AddThread implements Runnable {

	public MyObj objone = new MyObj();

	public AddThread(MyObj objone)
	{
		this.objone = objone;
	}

	public void run()
	{
		System.out.println("Thread add is started OK");
		
		Random rand = new Random();
		int randomNum = 0;

		while(true)
		{
			randomNum = rand.nextInt((50 - 1) + 1) + 1;

			if (objone.myQueue.size() == 10)
			{
				try{
					System.out.println("There are 10 obj in queue. Sleep for 17 sec");
					Thread.sleep(1000*17);
				}
				catch (Exception e) 
				{
					System.out.println("Error on thread add");
					e.getMessage();
					e.toString();
					e.printStackTrace();
				}

			}
			else
			{
				objone.myQueue.add(String.valueOf(randomNum));
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
