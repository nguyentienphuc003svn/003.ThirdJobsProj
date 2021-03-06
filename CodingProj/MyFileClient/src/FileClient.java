import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
 
public class FileClient  extends UnicastRemoteObject implements FileClientInt {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	
	public  FileClient(String n) throws RemoteException {
		super();
		name=n;
	}
 
	public String getName() throws RemoteException{
		return name;
	}
    
	//Receive file from Server
	public boolean sendData(String filename, byte[] data, int len) throws RemoteException{
        try{
        	File f=new File("C:\\ServerEditedReceived.txt");
        	f.createNewFile();
        	FileOutputStream out=new FileOutputStream(f,true);
        	out.write(data,0,len);
        	out.flush();
        	out.close();
        	
			System.out.println("Server has sent an update file called ServerEditedReceived.txt");
			System.out.println("\n\nThe content of ServerEditedReceived.txt as");

			//
			System.out.println("-----------------");
			BufferedReader br = new BufferedReader(new FileReader("C:\\ServerEditedReceived.txt")); 
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			System.out.println("-----------------");
        }
        catch(Exception e){
        	e.printStackTrace();
        }
		return true;
	}
	
	//Change to byte --> Sending The File to Server...
	public boolean changeByte(FileServerInt c) throws RemoteException{
		
		try{
			File f1=new File("c:\\OriginClient.txt");			 
			FileInputStream in=new FileInputStream(f1);			 				 
			byte [] mydata=new byte[1024*1024];						
			int mylen=in.read(mydata);
			while(mylen>0){
				c.receiveData(f1.getName(), mydata, mylen);	 
				mylen=in.read(mydata);				 
			}
		}
		catch(Exception e){
			e.printStackTrace();

		}

		return true;
	}
}
