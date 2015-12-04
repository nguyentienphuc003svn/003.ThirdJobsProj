package ccas;

import java.util.Iterator;
import java.util.Set;

public class ThreadMngr {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void StopAllThread()
	{
		System.out.println("Stop Thread");
		CLog.writeLogVS("Stop Thread");

		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		Iterator it = threadSet.iterator();
		System.out.println("Alive Thread >>> " + threadSet.size());
		CLog.writeLogVS("Alive Thread >>> " + threadSet.size());
		
		while(it.hasNext())
		{
			Thread t = (Thread)it.next();
			
			if (t.getName().equals("ViewServerThread")) 
			{	
				System.out.println("Stop ViewServerThread");
				CLog.writeLogVS("Stop ViewServerThread");
				t.interrupt();

			}
			if (t.getName().equals("GetDataThread")) 
			{	
				System.out.println("Stop GetDataThread");
				CLog.writeLogVS("Stop GetDataThread");
				t.interrupt();

			}
			if (t.getName().equals("OfflineCheckThread")) 
			{	
				System.out.println("Stop OfflineCheckThread");
				CLog.writeLogVS("Stop OfflineCheckThread");
				t.interrupt();

			}
			if (t.getName().equals("EventConvertThread")) 
			{	
				System.out.println("Stop EventConvertThread");
				CLog.writeLogVS("Stop EventConvertThread");
				t.interrupt();

			}
		}
		
		Set<Thread> threadSetAfter = Thread.getAllStackTraces().keySet();
		System.out.println("Alive Thread After >>> " + threadSetAfter.size());
		CLog.writeLogVS("Alive Thread After >>> " + threadSetAfter.size());
		
	}
	
	public void StartAllThread()
	{
		try
		{
			////////--------
			System.out.println("Restart ViewServerThread");
			CLog.writeLogVS("Restart ViewServerThread");
			Thread s = new Thread (new ViewServer());
			s.setName("ViewServerThread");
			s.start();

			////////--------
			System.out.println("Restart GetDataThread");
			CLog.writeLogVS("Restart GetDataThread");
			UDPServer	UDPserver	=	new	UDPServer();
			Thread t = new Thread (UDPserver);
			t.setName("GetDataThread");
			t.start();

			////////--------
			System.out.println("Restart OfflineCheckThread");
			CLog.writeLogVS("Restart OfflineCheckThread");
			Thread o = new Thread (new DaemonThread(20));
			o.setName("OfflineCheckThread");
			o.setDaemon(true);
			o.start();

			////////--------
			System.out.println("Restart EventConvertThread");
			CLog.writeLogVS("Restart EventConvertThread");
			Thread e = new Thread (new EventThread());
			e.setName("EventConvertThread");
			e.setDaemon(true);
			e.start();
		}
		catch(Exception e)
		{
			System.out.println(">>> Exception at Restart Threads");
			System.out.println(e.toString());
			System.out.println(e.getMessage());
			e.printStackTrace();
			
			CLog.writeLogVS(">>> Exception at Restart Threads");
			CLog.writeLogVS(e.toString());
			CLog.writeLogVS(e.getMessage());

		}

	}
	
	public boolean CheckAliveThread()
	{
		System.out.println("Checking Alive Thread");
		CLog.writeLogVS("Checking Alive Thread");
		
		boolean alive = true;

		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		Iterator it = threadSet.iterator();
		System.out.println("Alive Thread >>> " + threadSet.size());
		CLog.writeLogVS("Alive Thread >>> " + threadSet.size());
		
		int scount = 0;
		int tcount = 0;
		int ocount = 0;
		int ecount = 0;
		while(it.hasNext())
		{
			Thread t = (Thread)it.next();
			
			if (t.getName().equals("ViewServerThread")) 
			{	
				scount++;
				System.out.println("ViewServerThread Still Alive");
				CLog.writeLogVS("ViewServerThread Still Alive");

			}
			if (t.getName().equals("GetDataThread")) 
			{
				tcount++;
				System.out.println("GetDataThread Still Alive");
				CLog.writeLogVS("GetDataThread Still Alive");
		
			}
			if (t.getName().equals("OfflineCheckThread")) 
			{
				ocount++;
				System.out.println("OfflineCheckThread Still Alive");
				CLog.writeLogVS("OfflineCheckThread Still Alive");
		
			}
			if (t.getName().equals("EventConvertThread")) 
			{
				ecount++;
				System.out.println("EventConvertThread Still Alive");
				CLog.writeLogVS("EventConvertThread Still Alive");
		
			}
		}
		
		System.out.println("S >>> " + scount + " U >>> " + tcount + " O >>> " + ocount + " E >>> " + ecount);
		CLog.writeLogVS("S >>> " + scount + " U >>> " + tcount + " O >>> " + ocount + " E >>> " + ecount);
		
		if (scount <=0 || tcount <=0 || ocount <=0 || ecount<=0) alive = false;
		
		return alive;
	}


	
	
	
}
