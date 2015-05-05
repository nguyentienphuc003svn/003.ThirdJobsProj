import java.rmi.*;
 
public interface FileServerInt extends Remote{
 
		public boolean login(FileClientInt c) throws RemoteException;
		public boolean receiveData(String filename, byte[] data, int len) throws RemoteException;

}
