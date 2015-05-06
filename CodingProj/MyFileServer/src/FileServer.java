import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class FileServer extends UnicastRemoteObject implements FileServerInt {

	private String file="";

	protected FileServer() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setFile(String f){
		file=f;
	}

	public boolean login(FileClientInt c) throws RemoteException{
		/*
		 *
		 * Change to byte --> Sending The File to Client...
		 * 
		 */
		try{
			File f1=new File("C:\\FileReceiveFromClient.txt");			 
			FileInputStream in=new FileInputStream(f1);			 				 
			byte [] mydata=new byte[1024*1024];						
			int mylen=in.read(mydata);
			while(mylen>0){
				c.sendData(f1.getName(), mydata, mylen);	 
				mylen=in.read(mydata);				 
			}
		}
		catch(Exception e){
			e.printStackTrace();

		}
		return true;
	}

	//Receive file from Client
	public boolean receiveData(String filename, byte[] data, int len) throws RemoteException{
		try{
			File f=new File("C:\\FileReceiveFromClient.txt");
			f.createNewFile();
			FileOutputStream out=new FileOutputStream(f,true);
			out.write(data,0,len);
			out.flush();
			out.close();

			System.out.println("\nServer has finised receive the fie OriginClientFile.txt from Client");
			System.out.println("The fie OriginClientFile.txt from Client has renamed as FileReceiveFromClient.txt");
			System.out.println("\n\nThe content of FileReceiveFromClient.txt as");

			//
			System.out.println("-----------------");
			BufferedReader br = new BufferedReader(new FileReader("C:\\FileReceiveFromClient.txt")); 
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			System.out.println("-----------------");

			//
			System.out.println("The content of FileReceiveFromClient.txt now modified and update in the Server");
			FileWriter fw = new FileWriter("C:\\FileReceiveFromClient.txt",true); //the true will append the new data
			fw.write("\nServer has modifed the FileReceiveFromClient.txt file");//appends the string to the file
			fw.close();



		}
		catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
}
