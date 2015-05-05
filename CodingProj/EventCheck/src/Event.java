import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;


public class Event {

	public static ArrayList<String> MyEvent = new ArrayList<String>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Event eve = new Event();
		eve.EventInit();
		eve.sortEvent();
		eve.checkCMU();
		//eve.listEvent();
		eve.saveEvent();
	}

	public void EventInit()
	{
		try
		{
			// Open file
			//String current = new java.io.File( "." ).getCanonicalPath();
			//System.out.println("Current dir:"+current);

			FileInputStream fstream = new FileInputStream("events.txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));

			String strLine;

			//Read File Line By Line
			int index = 0;
			while ((strLine = br.readLine()) != null) 
			{

				//System.out.println("Processing Line + [" + strLine + "]");
				Event.MyEvent.add(strLine.trim());
				index++;
			}

			//Close the input stream			
			in.close();
		}
		catch (Exception e){
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void sortEvent()
	{
		//System.out.println("First Name   Last Name   Extension");			
		for (int i=0; i<Event.MyEvent.size(); i++)
		{
			for (int j=i+1; j<Event.MyEvent.size(); j++)
			{
				if (Integer.parseInt(Event.MyEvent.get(j).trim()) > Integer.parseInt(Event.MyEvent.get(i).trim()))
				{
					String myTemp = Event.MyEvent.get(i).trim();
					Event.MyEvent.add(i, Event.MyEvent.get(j).trim());
					Event.MyEvent.remove(i+1);					
					Event.MyEvent.add(j, myTemp);
					Event.MyEvent.remove(j+1);
					
				}
			}
		}
	}

	public void listEvent()
	{
		//System.out.println("First Name   Last Name   Extension");			
		for (int i=0; i<Event.MyEvent.size(); i++)
		{
			System.out.println(Event.MyEvent.get(i).trim());
		}
	}
	
	public void checkCMU()
	{
		boolean isCMU = false;
		for (int i=0; i<Event.MyEvent.size(); i++)
		{
			if (Event.MyEvent.get(i).trim().equals("1892"))
			{
				isCMU = true;
			}
		}
		
		if (!isCMU)
		{
			System.out.println("The	founding of CMU in 1892	was	NOT considered	a	world historic	event");
			Event.MyEvent.add("1892");
			this.sortEvent();
		}
		else
			System.out.println("The	founding of CMU in 1892	was	ALREADY considered a world historic	event");
	}

	public void saveEvent()
	{
		try {

			//			FileInputStream fstream = new FileInputStream("Contacts.txt");
			//			DataInputStream in = new DataInputStream(fstream);
			//			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));

			File file = new File("sorted_events.txt");

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			for (int i=0; i<Event.MyEvent.size(); i++)
			{
				String content = Event.MyEvent.get(i).trim() +"\n";
				bw.write(content);

			}
			bw.close();
			System.out.println("Total " + Event.MyEvent.size() + " event(s) are written to file sorted_events.txt");

		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void showFiveEvent()
	{
		for (int i=0; i<Event.MyEvent.size(); i++)
		{
			System.out.println(Event.MyEvent.get(i));
		}
	}

	public void showArray(Object[] showArray)
	{
		for (int i=0; i<showArray.length; i++)
		{
			System.out.println("Value at [" + (i+1) + "] is [" + String.valueOf(showArray[i]) + "]");
		}
	}



}
