
import java.net.*;
import java.util.HashMap;
import java.io.*;

public class Server implements Runnable
{  
	public static ServerThread clients[] = new ServerThread[50];
	public static HashMap<String,Integer> IPAddress = new HashMap<String,Integer>(); 
	private ServerSocket server = null;
	private Thread thread = null;
	public static int clientCount = 0;

	public Server(int port)
	{  
		try
		{  
			System.out.println("Binding to port " + port + ", please wait  ...");
			server = new ServerSocket(port);  
			System.out.println("Server started: " + server);
			start(); 
		}
		catch(IOException ioe)
		{System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());}
	}

	public void run()
	{  
		while (thread != null)
		{  
			try
			{  
				System.out.println("Waiting for a client ..."); 
				addThread(server.accept()); 
			}
			catch(IOException ioe)
			{System.out.println("Server accept error: " + ioe); stop();}
		}
	}

	public void start()
	{  
		if (thread == null)
		{  
			thread = new Thread(this); 
			thread.start();
		}
	}
	
	public void stop()
	{  
		if (thread != null)
		{  
			thread.stop(); 
			thread = null;
		}
	}

	private int findClient(int ID)
	{  
		for (int i = 0; i < clientCount; i++)
			if (clients[i].getID() == ID) return i;

		return -1;
	}

	public synchronized void handle(int ID, String input)
	{  
		System.out.println(ID + ": " + input);
		
		if (input.equals(".bye"))
		{  
			clients[findClient(ID)].send(".bye");
			remove(ID); 
		}
		else
			for (int i = 0; i < clientCount; i++)
				clients[i].send(ID + ": " + input);   
	}

	public synchronized void remove(int ID)
	{  
		int pos = findClient(ID);

		if (pos >= 0)
		{  
			ServerThread toTerminate = clients[pos];
			System.out.println("Removing client thread " + ID + " at " + pos);

			if (pos < clientCount-1)
				for (int i = pos+1; i < clientCount; i++)
					clients[i-1] = clients[i];

			clientCount--;

			try
			{  
				toTerminate.close(); 
			}
			catch(IOException ioe)
			{  
				System.out.println("Error closing thread: " + ioe); 
			}

			toTerminate.stop(); 
		}
	}

	private void addThread(Socket socket)
	{  
		if (clientCount < clients.length)
		{  
			System.out.println("Client accepted: " + socket);
			clients[clientCount] = new ServerThread(this, socket);

			try
			{  
				clients[clientCount].open(); 
				clients[clientCount].start();  
				clientCount++; 
			}
			catch(IOException ioe)
			{System.out.println("Error opening thread: " + ioe); } 
		}
		else
			System.out.println("Client refused: maximum " + clients.length + " reached.");
		
		//Check if more than 5 client for an IP
		//System.out.println("Checking for IP SAME");
		String SocketIP = ((InetSocketAddress)socket.getRemoteSocketAddress()).getAddress().toString().trim();
		//System.out.println("IP " + SocketIP);
		if (IPAddress.keySet().size()<=0)
		{
			IPAddress.put(SocketIP, 1);
		}
		else
		{
			boolean Addable = false;
			for (String IPKey : IPAddress.keySet())
			{
				if (IPKey.trim().equals(SocketIP)) Addable = true; 
			}
			
			if (Addable) IPAddress.replace(SocketIP, IPAddress.get(SocketIP)+1);
			else IPAddress.put(SocketIP, 1);
		}
		
		for (String IPKey : IPAddress.keySet())
		{
			if (IPAddress.get(IPKey) >=5)
			{
				try{
				System.out.println("\n\n>>>There are more than 5 connection from IP " + IPKey + " >>> Server Quit SOON");
				server.close();
				thread.stop();
				}
				catch(Exception e)
				{}
			}
		}
	}
	
	public int getTotalClient()
	{
		return this.clientCount;
	}

	public static void main(String args[])
	{  
		Server server = null;
		server = new Server(4000);
	}
}
