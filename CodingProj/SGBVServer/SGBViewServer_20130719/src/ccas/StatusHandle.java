package ccas;

public class StatusHandle extends Thread{
	
	public String data;
	
	public StatusHandle (String data)	{
		this.data = data.trim();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void run()
	{
		try
		{
			CLog.writeLogVS(">>>Start the Thread for Status");
			ViewUnpack vu = new ViewUnpack();
			vu.insert(this.data.trim());
			CLog.writeLogVS(">>>Do the Thread for Status Successful<<<");			
		}
		catch(Exception e)
		{
			CLog.writeLogVS(">>>One Status Thread is Fail");
			CLog.writeLogVS(">>>Data:" + this.data.trim());
			CLog.writeLogVS(e.toString());
			CLog.writeLogVS(e.getMessage());
			
			//Thread.currentThread().interrupt();
		}
	}

}
