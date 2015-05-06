import java.io.File;
import java.io.FileInputStream;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;
 
 
public class StartFileClient {
 
	public static void main(String[] args) {
		try{						
			
			FileServerInt server=(FileServerInt)Naming.lookup("rmi://127.0.0.1/abc");
			FileClient c=new FileClient("diff");
			
			//Send from Client to Server
			//c.changeByte(server);
			StartFileClient.changeByte();
			
			//Send from Server to Client			
			server.login(c);
			
			//System.out.println("Listening.....");			
			Scanner s=new Scanner(System.in);			
			while(true){
				String line=s.nextLine();
				//System.out.println(">>>" + line);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//Change to byte --> Sending The File to Server...
	public static boolean changeByte() throws RemoteException{
		
		try{
			
			FileServerInt server=(FileServerInt)Naming.lookup("rmi://127.0.0.1/abc");
			
			File f1=new File("c:\\OriginClientFile.txt");			 
			FileInputStream in=new FileInputStream(f1);			 				 
			byte [] mydata=new byte[1024*1024];						
			int mylen=in.read(mydata);
			while(mylen>0){
				server.receiveData(f1.getName(), mydata, mylen);	 
				mylen=in.read(mydata);				 
			}
		}
		catch(Exception e){
			e.printStackTrace();

		}
		return true;
	} 
}
