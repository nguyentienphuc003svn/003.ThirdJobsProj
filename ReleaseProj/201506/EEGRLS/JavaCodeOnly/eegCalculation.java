import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;


public class eegCalculation {

	
	public static int count = 0 ; 
	public static double num = 0.0;
	public static double sum = 0.0; 
	public static double average = 0.0; 
	public static double min = Double.MAX_VALUE; //min value is initially set to the largest positive named constant 
	public static double max = -Double.MAX_VALUE; //max value is initially set to the largest negative named constant

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		eegCalculation eeg = new eegCalculation();
		eeg.eegInit();

	}

	public void eegInit()
	{
		try
		{
			// Open file
			//String current = new java.io.File( "." ).getCanonicalPath();
			//System.out.println("Current dir:"+current);

			FileInputStream fstream = new FileInputStream("eeg.txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));

			String strLine;

			//Read File Line By Line
			int index = 0;
			while ((strLine = br.readLine()) != null) 
			{
				if (strLine.split(",",-1).length <= 0) continue;

				//System.out.println("Processing Line + [" + strLine + "]");
				if (strLine.split(",",-1).length == 1) 
				{	
					continue;
				}

				if (strLine.split(",",-1).length > 1)
				{
					// read a number from fileInput and place it in num
					num = Double.parseDouble(strLine.split(",",-1)[1]);
					
					// increment count here
					count++;
					
					// update sum by adding num to it
					sum = sum + num;
					
					// update max
					if (num > max) max = num;
					
					// similarly update min
					if (num < min) min = num;
					
					// the averate
					average = sum/count;
					
					System.out.printf("Count is %4d \t Num is %8.2f \n", count, num);
					
				}
			}
			
			//Print max and min
			System.out.printf("Max is %8.2f \t Min is %8.2f \n", max, min);
			
			//Print sum and average
			System.out.printf("Sum is %8.2f \t Average is %8.2f \n", sum, average);

			//Close the input stream			
			in.close();
		}
		catch (Exception e){
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}



}
