package ccas;

public class SmsThread extends Thread{
	
	String termid = "";
	String code = "";
	
	public SmsThread (String termid, String code)
	{
		this.termid = termid.trim();
		this.code = code.trim();
	}
	
	public void run()
	{
		try
		{
			CLog.writeLogVS(">>>Start the Thread for Event and SMS");
			
			//Event
			ViewUnpack vu = new ViewUnpack();
			vu.event(0);
			CLog.writeLogVS(">>>Start the Thread for Event");
			
			//SMS
			String[] args = {};
			ccas.SMSServer.main(args);
			CLog.writeLogVS(">>>Start the Thread for SMS");
		
			CLog.writeLogVS(">>>Do the Thread for Event and SMS Successful<<<");			
		}
		catch(Exception e)
		{
			CLog.writeLogVS(">>>One Event Thread is Fail");
			CLog.writeLogVS(">>>TermID: " + this.termid.trim() + " Code " + this.code.trim());
			CLog.writeLogVS(e.toString());
			CLog.writeLogVS(e.getMessage());
			
			Thread.currentThread().interrupt();
		}
	}


}

