import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


/**
 * 
 * @author <Your Name>
 * This is the application to implement the Vignere
 *
 */

public class Encrypt {

	public static final String MYNAME = "AuthorName";	
	public static BufferedWriter out;

	public String mode = "";
	public static String theKey = "";
	public static String inputFile = "";
	public static String outputFile = "";

	public static void main(String[] args) {

		//		for (String str: args)
		//			System.out.println(str);

		//Switch command
		if (args == null || args.length<=0)
		{
			new Encrypt().usageMode();
		}
		else
		{
			if (args[0].equals("") || (!args[0].equals("-e") && !args[0].equals("-d")))
			{
				new Encrypt().usageMode();
			}
			else
			{
				//Command to encrypt: Encrypt -e abcdef < in.txt
				if (args[0].equals("-e"))
				{
					Encrypt.theKey = args[1].trim();
					Encrypt.inputFile = args[3].trim();

					new Encrypt().encryptMode();
				}

				//Command to decrypt: Encrypt -d abcdef < indecrypt.txt > out.txt
				if (args[0].equals("-d"))
				{
					Encrypt.theKey = args[1].trim();
					Encrypt.inputFile = args[3].trim();
					Encrypt.outputFile = args[5].trim();

					new Encrypt().decryptMode();
				}
			}
		}
	}

	/*
	 * This method to encrypt the original text
	 */
	static String encrypt(String text, String key) {
		String cipher = "";
		text = text.toUpperCase();
		key = key.toUpperCase();

		for (int i = 0, j = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c < 'A' || c > 'Z') continue;

			cipher += ((c + key.charAt(j) - 2 * 'A') % 26 + 'A');
			cipher += " ";
			j = ++j % key.length();
		}
		return cipher;
	}

	/*
	 * This method to decrypt the cipher text
	 */ 
	static String decrypt(String text, String key) {
		String ori = "";
		text = text.toUpperCase().trim();
		key = key.toUpperCase();

		//Change the cipher number string to cipher letter string
		String ciohertext = "";
		for(int i=0; i<text.split(" ", -1).length; i++)
		{

			if (!text.split(" ")[i].equals("")) 
				ciohertext += (char)Integer.parseInt(text.split(" ")[i]);
		}

		text = ciohertext;
		for (int i = 0, j = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c < 'A' || c > 'Z') continue;
			ori += (char)((c - key.charAt(j) + 26) % 26 + 'A');
			j = ++j % key.length();
		}
		return ori;
	}

	/*
	 * The usage Mode
	 * 
	 */
	public void usageMode()
	{
		System.out.println("Encryption program by " + MYNAME);
		System.out.println("usage: java Encrypt [-e, -d] < inputFile > outputFile");
	}

	/*
	 * The encrypt Mode
	 * Read the original text in the in.txt to encrypt and display on the console
	 */
	public void encryptMode()
	{
		try
		{

			//Open the file that is the first
			//Command line parameter
			FileInputStream fstream = new FileInputStream(Encrypt.inputFile);

			//Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));

			String strLine;
			String InsertCmd = "";

			//Write out to out.txt
			//out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("out.txt"), "UTF16"));

			//Read File Line By Line
			while ((strLine = br.readLine()) != null)
			{

				if (strLine.length() <= 0) continue;

				System.out.println("Origin Text is  + [" + strLine + "]");
				System.out.println("Encrypted Text is  + [" + Encrypt.encrypt(strLine, Encrypt.theKey).trim() + "]");
				System.out.println("-------------------\n");

				//InsertCmd = "";
				// Print the content to file out.txt
				//System.out.println (InsertCmd);
				//out.write(InsertCmd);
				//out.newLine();
			}

			//Close the input stream
			in.close();
			//out.close();

		}
		catch (Exception e){

			//Catch exception if any
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * The decrypt Mode
	 * Read the cipher text in the in.txt 
	 *    Decrypt to original text and show to the Console
	 *    Save the decryptext text to out.txt	 
	 */
	public void decryptMode()
	{
		try
		{

			//Open the file that is the first
			//Command line parameter
			FileInputStream fstream = new FileInputStream(Encrypt.inputFile);

			//Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));

			String strLine;
			String InsertCmd = "";

			//Write out to out.txt
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Encrypt.outputFile), "UTF8"));

			//Read File Line By Line
			while ((strLine = br.readLine()) != null)
			{

				if (strLine.length() <= 0) continue;

				System.out.println("Cipher Text is  + [" + strLine + "]");
				System.out.println("Decrypted Text is  + [" + Encrypt.decrypt(strLine, Encrypt.theKey).trim() + "]");
				System.out.println("-------------------\n");

				InsertCmd = Encrypt.decrypt(strLine, Encrypt.theKey).trim();
				// Print the content to file out.txt
				//System.out.println (InsertCmd);
				out.write(InsertCmd);
				out.newLine();
			}

			//Close the input stream
			in.close();
			out.close();

		}
		catch (Exception e){

			//Catch exception if any
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
