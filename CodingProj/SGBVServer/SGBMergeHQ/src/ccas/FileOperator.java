package ccas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class FileOperator {
	private String filePath; //文件所在的实际物理路径
	public  BufferedReader bufread;
	public ObInterface obInterface;
	public ObInterface getObInterface() {
		return obInterface;
	}
	public void setObInterface(ObInterface obInterface) {
		this.obInterface = obInterface;
	}
	public FileOperator(){
		
	}
	public FileOperator(String filePath){
		this.filePath = filePath;
	}
	
	
	public StringBuffer readFile(String path){
		
		StringBuffer bf = new StringBuffer();
		try {
			if(fileExist(path)){
				FileReader fileReader = new FileReader(path);
				bufread = new BufferedReader(fileReader);
				//bufread = new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
				String read ="";
				while((read = bufread.readLine())!= null){
					String test = read.toString();
					if(test.contains("\"")){
						test = test.replace("\"", "\\\"");
					}
					bf.append(test);
					bf.append("\\n");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bf;
	}
	public StringBuffer readFile1(String path){
		
		StringBuffer bf = new StringBuffer();
		try {
			if(fileExist(path)){
				FileReader fileReader = new FileReader(path);
				bufread = new BufferedReader(fileReader);
				//bufread = new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
				String read ="";
				while((read = bufread.readLine())!= null){
					String test = read.toString();
					byte[] bytes = test.getBytes();
					test = new String(bytes,"GBK");
					if(test.contains("\"")){
						test = test.replace("\"", "\\\"");
					}
					bf.append(test);
					bf.append("\\n");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bf;
	}
	public  boolean writeFile(String bf,String path){
		boolean flag = false;
		try {
			FileWriter fw = null;
			fw = new   FileWriter(path);
			fw.write(bf);   
			fw.close();
			flag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = false;
		} 
		return flag;
	}
	public  boolean fileExist(String path) {
			 File file = new File(path);
			 if(file.exists()){
				 return true;
			 }else {
				 return false;
		     }

		  }
	public List<ObInterface> parseRoute(String path){
		List<ObInterface> list = new ArrayList<ObInterface>();
		StringBuffer bf = new StringBuffer();
		try {
			if(fileExist(path)){
				FileReader fileReader = new FileReader(path);
				bufread = new BufferedReader(fileReader);
				String read ="";
				while((read = bufread.readLine())!= null){
					
					int t = read.indexOf("#");
					if(read.equals("") || t == 0){
						//System.out.println(read);
						continue;
					}else{
						String[] re = read.split("$");
						ObInterface ob = new ObInterface();
						String s=read; 
					    //找到$所在的位置
					    String temp = s.substring(0,s.indexOf("$"));
					    String[] my = temp.split("[+]");
					    for(int i=0;i<my.length;i++){
				    		try {
				    			ob.setFieldByName("route"+i, my[i]);
				    		} catch (Exception e) {
				    			// TODO Auto-generated catch block
				    			e.printStackTrace();
				    		}
					    }
					    String temp1 = s.substring(s.indexOf("$")+1,s.length());
					    try {
							ob.setFieldByName("ChannelRoute", temp1);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						list.add(ob);
					}
				}
				 bufread.close();
				 fileReader.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public List<ObInterface> parseRouteSelect(String path,String channelId){
		List<ObInterface> list = new ArrayList<ObInterface>();
		List<ObInterface> listNew = new ArrayList<ObInterface>();
		StringBuffer bf = new StringBuffer();
		try {
			if(fileExist(path)){
				FileReader fileReader = new FileReader(path);
				bufread = new BufferedReader(fileReader);
				String read ="";
				while((read = bufread.readLine())!= null){
					//不读取注释的内容
					int t = read.indexOf("#");
					if(read.equals("") || t == 0){
						continue;
					}else{
						String[] re = read.split("$");
						ObInterface ob = new ObInterface();
						String s=read; 
					    //找到$所在的位置
					    String temp = s.substring(0,s.indexOf("$"));
					    String[] my = temp.split("[+]");
					    for(int i=0;i<my.length;i++){
					    	
				    		try {
				    			ob.setFieldByName("route"+i, my[i]);
				    		} catch (Exception e) {
				    			// TODO Auto-generated catch block
				    			e.printStackTrace();
				    		}
					    }
					   
					    String temp1 = s.substring(s.indexOf("$")+1,s.length());
					    try {
							ob.setFieldByName("ChannelRoute", temp1);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						list.add(ob);
						
						
					}
				}
				for(int j=0;j<list.size();j++){
					if(list.get(j).getString("route3") != null && list.get(j).getString("route3").length()>0){
						String c_Id = list.get(j).getString("route3");
						if(c_Id.equals(channelId)){
							listNew.add(list.get(j));
						}
					}else{
						String c_Id = list.get(j).getString("route2");
						if(c_Id.equals(channelId)){
							listNew.add(list.get(j));
						}
					}
					
				}
				 bufread.close();
				 fileReader.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listNew;
	}
	public boolean updateRoute(String path,String oldStr,String newStr){
		boolean flag = false;
		StringBuffer bf = new StringBuffer();
		try {
			if(fileExist(path)){
				FileReader fileReader = new FileReader(path);
				bufread = new BufferedReader(fileReader);
				   
				String read ="";
				while((read = bufread.readLine())!= null){
					if(read.trim().equals(oldStr.trim())){
						bf.append(newStr);
					}else{
						bf.append(read);
					}
					bf.append("\n");
				}
				 FileWriter fw = new FileWriter(path);
				 BufferedWriter   bw=new   BufferedWriter(fw);  
				 bw.write(bf.toString().toCharArray());     
				 bw.flush();     
				 bw.close();     
				 fw.close();  
				 bufread.close();
				 fileReader.close(); 
				 
			}
			flag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	public boolean deleteRoute(String path,String oldStr){
		boolean flag = false;
		StringBuffer bf = new StringBuffer();
		try {
			if(fileExist(path)){
				FileReader fileReader = new FileReader(path);
				bufread = new BufferedReader(fileReader);
				   
				String read ="";
				while((read = bufread.readLine())!= null){
					if(read.trim().equals(oldStr.trim())){
						continue;
					}else{
						bf.append(read);
					}
					bf.append("\n");
				}
				 FileWriter fw = new FileWriter(path);
				 BufferedWriter   bw=new   BufferedWriter(fw);  
				 bw.write(bf.toString().toCharArray());     
				 bw.flush();     
				 bw.close();     
				 fw.close();  
				 bufread.close();
				 fileReader.close(); 
				 
			}
			flag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	public boolean insertRoute(String path,String newStr){
		boolean flag = false;
		StringBuffer bf = new StringBuffer();
		try {
			if(fileExist(path)){
				FileReader fileReader = new FileReader(path);
				bufread = new BufferedReader(fileReader);
				   
				String read ="";
				 bf.append(newStr);
				 bf.append("\n");
				while((read = bufread.readLine())!= null){
					bf.append(read);
					bf.append("\n");
				}
				 FileWriter fw = new FileWriter(path);
				 BufferedWriter   bw=new   BufferedWriter(fw);  
				 bw.write(bf.toString().toCharArray());     
				 bw.flush();     
				 bw.close();     
				 fw.close();  
				 bufread.close();
				 fileReader.close(); 
				 
			}
			flag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	/**
	 * @author zymei
	 * @param newFilePath 是不带文件名的，templatePath是需要文件名的路径
	 * 创建文件
	 * */
	public boolean createFile(String newFilePath,String templatePath,String destFileName){
			
			File file = new File(newFilePath,destFileName);
		    if (file.exists()) {
		     System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
		     return false;
		    }
		    if (destFileName.endsWith(File.separator)) {
		     System.out.println("创建单个文件" + destFileName + "失败，目标不能是目录！");
		     return false;
		    }
		    // 创建目标文件
		    try {
		    	if (file.createNewFile()) {
		    		System.out.println("创建单个文件" + destFileName + "成功！");
		    		//读取模板信息
		    		//String readPath = "D:"+File.separator+"template"+File.separator+"cuplnk.conf";
		    		
		    		String readPath = templatePath;
		    		//写到原来的文件中
		    		String writePath = newFilePath + File.separator + destFileName;
		    		int  bytesum  =  0;  
		            int  byteread  =  0; 
		            File readfile = new File(readPath);
		            if(readfile.exists()){
		            	InputStream  inStream  =  new  FileInputStream(readPath);  //读入原文件  
		                FileOutputStream  fs  =  new  FileOutputStream(writePath);  
		                byte[]  buffer  =  new  byte[1444];  
		                int  length;  
		                while  (  (byteread  =  inStream.read(buffer))  !=  -1)  {  
		                   bytesum  +=  byteread;  //字节数  文件大小  
		                   fs.write(buffer,  0,  byteread);  
		                }  
			            inStream.close();  
			    		return true;
		            }else{
		            	return false;
		            }
					
		    	} else {
		    		System.out.println("创建单个文件" + destFileName + "失败！");
		    		return false;
		    	}
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    		System.out.println("创建单个文件" + destFileName + "失败！");
	    		return false;
	    	}
		
		
	}
	public  boolean createDir(String destDirName) { 
		 File dir = new File(destDirName); 
		 if(dir.exists()) { 
		 System.out.println("创建目录" + destDirName + "失败，目标目录已存在！"); 
		 return false; 
		 } 
		 if(!destDirName.endsWith(File.separator)) 
		 destDirName = destDirName + File.separator; 
		 // 创建单个目录  
		 if(dir.mkdirs()) { 
		 System.out.println("创建目录" + destDirName + "成功！"); 
		 return true; 
		 } else { 
		 System.out.println("创建目录" + destDirName + "成功！"); 
		 return false; 
		 } 
	} 
	public  boolean createSingleFile(String newFilePath,String destFileName){
		boolean flag = false;
		File file = new File(newFilePath,destFileName);
	    if (file.exists()) {
	     System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
	     return false;
	    }
	    if (destFileName.endsWith(File.separator)) {
	     System.out.println("创建单个文件" + destFileName + "失败，目标不能是目录！");
	     return false;
	    }
	    // 创建目标文件
	    try {
	    	if (file.createNewFile()) {
	    		System.out.println("创建单个文件" + destFileName + "成功！");
	    	} else {
	    		System.out.println("创建单个文件" + destFileName + "失败！");
	    		return false;
	    	}
    	} catch (IOException e) {
    		e.printStackTrace();
    		System.out.println("创建单个文件" + destFileName + "失败！");
    		return false;
    	}
		return flag;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FileOperator op = new FileOperator();
		String path = "D:"+File.separator+"cfg"+File.separator+"xml"+File.separator+"33";
		op.createDir(path);
	    
	}
}
