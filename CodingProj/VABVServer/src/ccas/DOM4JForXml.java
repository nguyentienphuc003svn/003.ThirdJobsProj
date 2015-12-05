package ccas;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


/** 
  * ����DOM4J��ʽ 
  * �˷�������Ҫ������
  * 1�����ҽڵ�
  * 2������XML�ڵ�
  * 3��ɾ���ڵ�
  * @author zymei 
  * 
  */ 
public class DOM4JForXml{ 
	private Document document;
	private String filePath; //�ļ����ڵ�ʵ������·��
	private ObInterface obInterface;
	private CLog cLog;
	List<ObInterface> listob = new ArrayList<ObInterface>();
	//��ʼ��Listob
	public DOM4JForXml(){
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<ObInterface> getListob() {
			return listob;
	}
	public void setListob(List<ObInterface> listob) {
			this.listob = listob;
	}
	//��ʼ��setgetObInterface
	public ObInterface getObInterface() {
		return obInterface;
	}
	public void setObInterface(ObInterface obInterface) {
		this.obInterface = obInterface;
	}
	//��ʼ��
	public DOM4JForXml(String filepath){
		   this.document = null;
		   this.filePath = filepath;
		   cLog = new CLog();
	 }
  /**
  * ��ȡ�Ѵ��ڵ�XML�ĵ�
  * @return
  */
  public Document getXmlFile() {
     if (fileExist()) {
    	 SAXReader reader = new SAXReader(); 
      try {
    	  this.document = reader.read(new File(filePath));
      } catch (DocumentException e) {
    	  e.printStackTrace();
      }finally{
       reader = null;
      }
      } else {
    	  this.document = null;
      }
     return this.document;
  }
  
  /**
   * ����XML�ļ�
   * @param document: XML�ļ���
   */
  private boolean saveXMLFile(Document document) {
     try {
	     OutputFormat format = OutputFormat.createPrettyPrint();
	     format.setEncoding("utf-8");
	     XMLWriter writer = new XMLWriter(new FileWriter(new File(filePath)),format);
	     writer.write(document);   
	     writer.close();
	     return true;
     } catch (Exception e) {   
    	 e.printStackTrace();
    	 return false;
     }
  }
  /**
  * �ж�XML�ļ��Ƿ����. 
  * @param fileName
  * @return
  */
  public boolean fileExist() {
     java.io.File objFile = new java.io.File(this.filePath);
     if (objFile.exists()) {
      return true;
     } else {
      return false;
     }

  }
  /** 
   * ����XML�ļ�
   * @param rootName:���ڵ�����
   */
   public boolean createXMLFile(String rootName) {
	   boolean flag = false;
	   if(!fileExist()){
    	  try{
    		  this.document = DocumentHelper.createDocument();
    	      this.document.addElement(rootName);
    	      flag = saveXMLFile(this.document); 
    	  }catch(Exception e){
    		  e.printStackTrace();
    		  System.out.println("�����ļ�ʧ��");
    		  return false;
    	  }
      }
      if(flag){
    	  return true;
      }else{
    	  return false;
      }
   }
   /**
    * ʵ��ҵ������սڵ���������һ��νڵ�
    * ��Ҫ�ж������������������ӽ���
    * @param parentNode ���ڵ����� root�µ�һ��ڵ�
    * @param childnode ��Ҫ���ӵĽڵ�����
    * @param ob ��Ҫ���ӵĽڵ��е�ֵ
    * @author zymei
    * */
   public boolean addStateXML(String parentNode,String childNode,ObInterface ob){
	   boolean flag = false;
	   if(document != null){
	       Element element = document.getRootElement().element(parentNode);  
	       //createElement
	       Element parentElement = element.addElement(childNode);  
	       parentElement.setAttributeValue("table", "t_TxnState");
	       if(childNode.equals("t_TxnState_field")){
	    	   Element child1 = parentElement.addElement("m_iStateID");
	           Element child2 = parentElement.addElement("m_iChannelID");
	           Element child3 = parentElement.addElement("m_iReqRes");
	           Element child4 = parentElement.addElement("m_iStateNum");
	           Element child5 = parentElement.addElement("m_iStateType");
	           Element child6 = parentElement.addElement("m_iPara0");
	           Element child7 = parentElement.addElement("m_iPara1");
	           if(!ob.getString("m_iPara2").equals("")){
	        	   	Element child8 = parentElement.addElement("m_iPara2");
		         	  child8.setText(ob.getString("m_iPara2"));  
		           }
	           Element child9 = parentElement.addElement("m_acMemo");
	           child1.setText(ob.getString("m_iStateID")); 
	           child2.setText(ob.getString("m_iChannelID")); 
	           child3.setText(ob.getString("m_iReqRes")); 
	           child4.setText(ob.getString("m_iStateNum")); 
	           child5.setText(ob.getString("m_iStateType")); 
	           child6.setText(ob.getString("m_iPara0")); 
	           child7.setText(ob.getString("m_iPara1")); 
	           child9.setText(ob.getString("m_acMemo")); 
	       }
	       flag = saveXMLFile(document);
	   }
	 	  return flag;
	 }
   /**
    * ʵ�ְ��սڵ���������һ��νڵ�
    * ��Ҫ�ж������������������ӽ���
    * @param parentNode ���ڵ����� root�µ�һ��ڵ�
    * @param childnode ��Ҫ���ӵĽڵ�����
    * @param ob ��Ҫ���ӵĽڵ��е�ֵ
    * @author zymei
    * */
   public boolean addXML(String parentNode,String childNode,ObInterface ob){
	   boolean flag = false;
	   if(document != null){
	       Element element = document.getRootElement().element(parentNode);  
	       //createElement
	       Element parentElement = element.addElement(childNode);  
	       if(childNode.equals("tChannel_field")){
	    	   parentElement.addAttribute("table", "tChannel");
	    	   Element child1 = parentElement.addElement("m_iChannelType");
	           Element child2 = parentElement.addElement("m_acChanelName");
	           child1.setText(ob.getString("m_iChannelType")); 
	           child2.setText(ob.getString("m_acChanelName")); 
	           if(ob.getString("m_iHaveTerm")!= null && ob.getString("m_iHaveTerm").equals("1")){
	         	  Element child3 = parentElement.addElement("m_iHaveTerm");
	         	  child3.setText(ob.getString("m_iHaveTerm"));  
	           }
	       }else if(childNode.equals("tTxn_field")){
	    	   parentElement.addAttribute("table", "tTxn");
	    	   Element child1 = parentElement.addElement("m_iTxnId");
	           Element child2 = parentElement.addElement("m_cTxnType");
	           Element child3 = parentElement.addElement("m_iTxnSwitch");
	           Element child4 = parentElement.addElement("m_iRevsFlag");
	           Element child5 = parentElement.addElement("m_iLogFlag");
	           Element child6 = parentElement.addElement("m_iRecvTxnId");
	           Element child7 = parentElement.addElement("m_iNeedMac");
	           Element child8 = parentElement.addElement("m_iReconType");
	           Element child9 = parentElement.addElement("m_acTxnName");
	
	           child1.setText(ob.getString("m_iTxnId")); 
	           child2.setText(ob.getString("m_cTxnType")); 
	           child3.setText(ob.getString("m_iTxnSwitch")); 
	           child4.setText(ob.getString("m_iRevsFlag")); 
	           child5.setText(ob.getString("m_iLogFlag")); 
	           child6.setText(ob.getString("m_iRecvTxnId")); 
	           child7.setText(ob.getString("m_iNeedMac")); 
	           child8.setText(ob.getString("m_iReconType")); 
	           child9.setText(ob.getString("m_acTxnName")); 
	           
	           if(ob.getString("m_cAIFlag")!= null && ob.getString("m_cAIFlag").length()>0){
	        	   Element child10 = parentElement.addElement("m_cAIFlag");
	        	   child10.setText(ob.getString("m_cAIFlag")); 
	           }
	           if(ob.getString("m_cAIData")!= null && ob.getString("m_cAIData").length()>0){
	        	   Element child11 = parentElement.addElement("m_cAIData");
	        	   child11.setText(ob.getString("m_cAIData")); 
	           }
	           if(ob.getString("m_cAIData2")!= null && ob.getString("m_cAIData2").length()>0){
	        	   Element child12 = parentElement.addElement("m_cAIData2");
	        	   child12.setText(ob.getString("m_cAIData2")); 
	           }
	       }else if(childNode.equals("t_TxnStateMap_field")){
	    	   parentElement.addAttribute("table", "t_TxnStateMap");
	    	  //ҵ����״̬������state.xml
	    	   Element child1 = parentElement.addElement("m_iTxnID");
	           Element child2 = parentElement.addElement("m_acMemo"); 
	           Element child3 = parentElement.addElement("m_iStateID");
	           child1.setText(ob.getString("m_iTxnId")); 
	           child2.setText(ob.getString("txnName")); 
	           child3.setText(ob.getString("m_iStateID")); 
	       }
	       flag = saveXMLFile(document);
	   }
	 	  return flag;
	 }
   /** 
    * ��������xml�е���Ϣ
    * @param parentNode ���ڵ�д��һ��ڵ� 
    * @param childnode  ֵ����Ӧ�Ľڵ�
    * @param value Ϊstate�ı�ʶ��numΪ״̬��������� 
    */ 
  public boolean updateStateXml(String parentNode,String childNode,ObInterface ob,String ruleId,String num,String stateId){
	  boolean flag = false;
	  parentNode = "t_TxnState_T";
	  childNode = "t_TxnState_field";
	  if(document != null){
		  if(ob != null && !ruleId.equals("") && !num.equals("")&& !stateId.equals("")){
			  Element element = document.getRootElement().element(parentNode);  
			  List<Element> elementList = element.elements();
			  for(int i=0;i<elementList.size();i++){   
			    	Element elt = (Element) elementList.get(i);  
			    	String text = elt.element("m_iStateID").getText();
			    	String stateNum = elt.element("m_iStateNum").getText();
			    	String sId = elt.element("m_iStateType").getText();

			    	if((text.endsWith(ruleId)) && (stateNum.equals(num)) && (stateId.equals(sId))){
			    		elt.element("m_iStateID").setText(ob.getString("m_iStateID"));
			    		elt.element("m_iChannelID").setText(ob.getString("m_iChannelID"));
			    		elt.element("m_iReqRes").setText(ob.getString("m_iReqRes"));
			    		elt.element("m_iStateNum").setText(ob.getString("m_iStateNum"));
			    		elt.element("m_iStateType").setText(ob.getString("m_iStateType"));
			    		elt.element("m_iPara0").setText(ob.getString("m_iPara0"));
			    		elt.element("m_iPara1").setText(ob.getString("m_iPara1"));
			    		//�޸�param2��ʾ			
			    		if(elt.element("m_iPara2")!= null){
			    			if(ob.getString("m_iPara2").length()>0){
			    				elt.element("m_iPara2").setText(ob.getString("m_iPara2"));
			    			}else{
			    				elt.remove(elt.element("m_iPara2"));
			    			}
			    			
			    		}else{
			    				if(ob.getString("m_iPara2").length()>0){
				    				Element m_cAIFlag = elt.addElement("m_iPara2");
				    				m_cAIFlag.setText(ob.getString("m_iPara2"));
				    			}
			    		}
			    		elt.element("m_acMemo").setText(ob.getString("m_acMemo"));
			    		flag = saveXMLFile(document); 
			    	}
			    }
		  }else{
			  flag = false;
		  }
	  }
	  return flag;
	  
  }
  /** 
   * ��������xml�е���Ϣ
   * @param parentNode ���ڵ�д��һ��ڵ� 
   * @param childnode  ֵ����Ӧ�Ľڵ�
   * @param fileName 
   */ 
  public boolean updateXml(String parentNode,String childNode,ObInterface ob,String value){ 
	   try{
		   Element element = document.getRootElement().element(parentNode);  
		   List<Element> elementList = element.elements();
		   //�޸�����
		   if(childNode.equals("tChannel_field")){
			    for(int i=0;i<elementList.size();i++){   
			    	Element elt = (Element) elementList.get(i);  
			    	Node node = elt.selectSingleNode("m_iChannelType");   
			    	String text = node.getText();
			    	if(text.endsWith(value)){
			    		node.getParent().element("m_iChannelType").setText(ob.getString("m_iChannelType"));
			    		node.getParent().element("m_acChanelName").setText(ob.getString("m_acChanelName"));
			    		
			    		//�޸�ǰ���ն˱�ʾ			    		
			    		if(node.getParent().element("m_iHaveTerm")!= null){
			    			if(ob.getString("m_iHaveTerm").length()>0){
			    				node.getParent().element("m_iHaveTerm").setText(ob.getString("m_iHaveTerm"));
			    			}else{
			    				node.getParent().remove(node.getParent().element("m_iHaveTerm"));
			    			}
			    			
			    		}else{
			    				if(ob.getString("m_iHaveTerm").length()>0){
				    				Element m_cAIFlag = node.getParent().addElement("m_iHaveTerm");
				    				m_cAIFlag.setText(ob.getString("m_iHaveTerm"));
				    			}
			    		}
			    		
			    	}
			    }
		   }else if(childNode.equals("tTxn_field")){
			   //�޸Ľ���
			   for(int i=0;i<elementList.size();i++){   
			    	Element elt = (Element) elementList.get(i);  
			    	Node node = elt.selectSingleNode("m_iTxnId");   
			    	String text = node.getText();
			    	if(text.endsWith(value)){
			    		node.getParent().element("m_iTxnId").setText(ob.getString("m_iTxnId"));
			    		node.getParent().element("m_cTxnType").setText(ob.getString("m_cTxnType"));
			    		node.getParent().element("m_iTxnSwitch").setText(ob.getString("m_iTxnSwitch"));
			    		node.getParent().element("m_iRevsFlag").setText(ob.getString("m_iRevsFlag"));
			    		node.getParent().element("m_iLogFlag").setText(ob.getString("m_iLogFlag"));
			    		node.getParent().element("m_iRecvTxnId").setText(ob.getString("m_iRecvTxnId"));
			    		node.getParent().element("m_iNeedMac").setText(ob.getString("m_iNeedMac"));
			    		node.getParent().element("m_iReconType").setText(ob.getString("m_iReconType"));
			    		node.getParent().element("m_acTxnName").setText(ob.getString("m_acTxnName"));
			    		// �޸�ʱ��3�����
			    		//1 xml ����ԭ����ֵ����Ҫ�޸�
			    		//2 xml ��û��ԭ����ֵ ��Ҫ����
			    		//3 xml ����ԭ����ֵ��������Ҫɾ��
			    		//��������־
			    		if(node.getParent().element("m_cAIFlag")!= null){
			    			if(ob.getString("m_cAIFlag").length()>0){
			    				node.getParent().element("m_cAIFlag").setText(ob.getString("m_cAIFlag"));
			    			}else{
			    				node.getParent().remove(node.getParent().element("m_cAIFlag"));
			    			}
			    			
			    		}else{
			    				if(ob.getString("m_cAIFlag").length()>0){
				    				Element m_cAIFlag = node.getParent().addElement("m_cAIFlag");
				    				m_cAIFlag.setText(ob.getString("m_cAIFlag"));
				    			}
			    		}
			    		//������������
			    		if(node.getParent().element("m_cAIData")!= null){
			    			if(ob.getString("m_cAIData").length()>0){
			    				node.getParent().element("m_cAIData").setText(ob.getString("m_cAIData"));
			    			}else{
			    				node.getParent().remove(node.getParent().element("m_cAIData"));
			    			}
			    			
			    		}else{
			    				if(ob.getString("m_cAIData").length()>0){
				    				Element m_cAIFlag = node.getParent().addElement("m_cAIData");
				    				m_cAIFlag.setText(ob.getString("m_cAIData"));
				    			}
			    		}
			    		//������������
			    		if(node.getParent().element("m_cAIData2")!= null){
			    			if(ob.getString("m_cAIData2").length()>0){
			    				node.getParent().element("m_cAIData2").setText(ob.getString("m_cAIData2"));
			    			}else{
			    				node.getParent().remove(node.getParent().element("m_cAIData2"));
			    			}
			    			
			    		}else{
			    				if(ob.getString("m_cAIData2").length()>0){
				    				Element m_cAIFlag = node.getParent().addElement("m_cAIData2");
				    				m_cAIFlag.setText(ob.getString("m_cAIData2"));
				    			}
			    		}
			    	}
			    }
		   }else if(childNode.equals("t_TxnStateMap_field")){
			   for(int i=0;i<elementList.size();i++){   
			    	Element elt = (Element) elementList.get(i);  
			    	Node node = elt.selectSingleNode("m_iTxnID");   
			    	String text = node.getText();
			    	if(text.endsWith(value)){
			    		node.getParent().element("m_iTxnID").setText(ob.getString("m_iTxnId"));
			    		node.getParent().element("m_acMemo").setText(ob.getString("txnName"));
			    		node.getParent().element("m_iStateID").setText(ob.getString("m_iStateID"));
			    	}
			    }
		   }
		 boolean flag = saveXMLFile(document); 
		 return flag;
	   }catch(Exception e){
		   e.printStackTrace();
		   return false;
	   }
  } 
/**
 * @author zymei
 * ɾ��ָ���ڵ��е�ֵ
 * @param parentNode root�ڵ��µ�һ��ڵ�
 * @param childNode ��Ҫɾ���ĵڶ���ڵ�
 * @param deleteNode ��Ҫɾ���Ľڵ��µ�Ԫ��
 * @param value  ������ڵ�ڵ�����Ԫ�ص�Ψһֵ
 * @return boolean
 * */  

  public boolean deleteXML(String parentNode,String childNode,String deleteNode,String value){
	  //parentNode = tChannel childNode=tChannel_field deleteNode =m_iChannelType value=m_iChannelType=25
	  boolean flag = false;
	  List<Element> elements = document.getRootElement().element(parentNode).elements(childNode);
      for(int i =0;i<elements.size();i++){
    	  String text =  elements.get(i).element(deleteNode).getText();
    	 if(text.equals(value)){
    		 flag = document.getRootElement().element(parentNode).remove(elements.get(i));
    	 }
      }
      boolean flag1 = saveXMLFile(document);
      flag = flag & flag1;
	  return flag;
  }
  public boolean deletePtl(String nodeName,String filedId){
	  //parentNode = tChannel childNode=tChannel_field deleteNode =m_iChannelType value=m_iChannelType=25
	  boolean flag = false;
	  	if(document != null){
	  		 List<Element> childList = document.selectNodes(nodeName); 
			  try{
				  for (int i=0;i<childList.size();i++) {
					  	Iterator child =  childList.get(i).elementIterator();
					  	if(childList.get(i).attributeValue("id").equals(filedId)){
					  		childList.get(i).getParent().remove(childList.get(i));
					  	}
				   }
			  }catch(Exception e){
				  e.printStackTrace();
			  }
	       flag = saveXMLFile(document);
	  	}
	  return flag;
  }
  public boolean checkPtlId(String nodeName,String filedId){
	  //parentNode = tChannel childNode=tChannel_field deleteNode =m_iChannelType value=m_iChannelType=25
	  boolean flag = false;
	  if(document != null){
		  List<Element> childList = document.selectNodes(nodeName); 
		  try{
			  for (int i=0;i<childList.size();i++) {
				  	Iterator child =  childList.get(i).elementIterator();
				  	if(childList.get(i).attributeValue("id").equals(filedId)){
				  		flag = true;
				  		break;
				  	}
			   }
		  }catch(Exception e){
			  e.printStackTrace();
		  }
	  }
	return flag; 
  }
  /**
   * @author zymei
   * ɾ��ָ���ڵ��е�ֵ
   * @param parentNode root�ڵ��µ�һ��ڵ�
   * @param childNode ��Ҫɾ���ĵڶ���ڵ�
   * @param deleteNode ��Ҫɾ���Ľڵ��µ�Ԫ��
   * @param value  ������ڵ�ڵ�����Ԫ�ص�Ψһֵ
   * @return boolean
   * */
  public boolean deleteStateXML(String parentNode,String childNode,String rule,String num,String stateId){
	  //parentNode = tChannel childNode=tChannel_field deleteNode =m_iChannelType value=m_iChannelType=25
	  boolean flag = false;
	  List<Element> elements = document.getRootElement().element(parentNode).elements(childNode);
      for(int i =0;i<elements.size();i++){
    	  String c_rule =  elements.get(i).element("m_iStateID").getText();
    	  String c_num =  elements.get(i).element("m_iStateNum").getText();
    	  String c_stateid =  elements.get(i).element("m_iStateType").getText();
    	 if((rule.equals(c_rule)) && (c_num.equals(num))&& (c_stateid.equals(stateId))){
    		 flag = document.getRootElement().element(parentNode).remove(elements.get(i));
    	 }
      }
      boolean flag1 = saveXMLFile(document);
      flag = flag & flag1;
	  return flag;
  }
  /**
   * ����Ԫ�ؽڵ���������ֵ����ȡ��ֵ����Ԫ���е�������Ϣ
   * */
  public List<ObInterface> getEleInfo(String parentNode,String childNode,String getNode,String value,String channlelId){
	  //parentNode = tChannel childNode=tChannel_field deleteNode =m_iChannelType value=m_iChannelType=25
	  if(document != null){
		 List<Element> elements = document.getRootElement().element(parentNode).elements(childNode);
		
		 try {
			 if(!value.equals("")&&channlelId.equals("")){
				 for(int i =0;i<elements.size();i++){
					 ObInterface ob = new ObInterface();
			    	 String text =  elements.get(i).element(getNode).getText();
			    	 if(text.equals(value)){
			    		 List<Element> list = elements.get(i).elements();
			    		 for(int j=0;j<list.size();j++){
			    			 String getName = list.get(j).getName();
			    			 String getText = list.get(j).getText();
							 ob.setFieldByName(getName, getText);
			    		 }
			    		 listob.add(ob);
			    	 }
			      }
			 }else if(value.equals("")&&!channlelId.equals("")){
				 //����������������
				 for(int i =0;i<elements.size();i++){
					 ObInterface ob = new ObInterface();
					 if(elements.get(i).element("m_cAIData") != null ){
						 String m_cAIData =  elements.get(i).element("m_cAIData").getText();
				    	 if(m_cAIData.contains(channlelId)){
				    		 List<Element> list = elements.get(i).elements();
				    		 for(int j=0;j<list.size();j++){
				    			 String getName = list.get(j).getName();
				    			 String getText = list.get(j).getText();
								 ob.setFieldByName(getName, getText);
				    		 }
				    		 listob.add(ob);
				    	 }
					 }
			      }
		 }else if(!value.equals("")&&!channlelId.equals("")){
				//�������������ͽ���ID���ϲ��Ҳ���
				 for(int i =0;i<elements.size();i++){
					 ObInterface ob = new ObInterface();
			    	 if(elements.get(i).element("m_cAIData")!= null ){
			    		 String text =  elements.get(i).element(getNode).getText();
			    		 String m_cAIData =  elements.get(i).element("m_cAIData").getText();
			    		 if(text.equals(value) && m_cAIData.contains(channlelId)){
				    		 List<Element> list = elements.get(i).elements();
				    		 for(int j=0;j<list.size();j++){
				    			 String getName = list.get(j).getName();
				    			 String getText = list.get(j).getText();
								 ob.setFieldByName(getName, getText);
				    		 }
				    		 listob.add(ob);
				    	 }
			    	 }
			    	 
			      }
			 }
			 
		  } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }
	  return listob;
  }
  
  public ObInterface getSingleEleInfo(String parentNode,String childNode,String getNode,String value){
	  ObInterface ob = new ObInterface();
	  if(document != null){
		  List<Element> elements = document.getRootElement().element(parentNode).elements(childNode);
		  try {
			  for(int i =0;i<elements.size();i++){
				 
		    	  String text =  elements.get(i).element(getNode).getText();
		    	 if(text.equals(value)){
		    		 List<Element> list = elements.get(i).elements();
		    		 for(int j=0;j<list.size();j++){
		    			 String getName = list.get(j).getName();
		    			 String getText = list.get(j).getText();
		    			 ob.setFieldByName(getName, getText);
		    		 }
		    	 }
		      }
		  } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
	  return ob;
  }
  
  /**
   * ���սڵ�˳���ѯ�����еĽڵ��������Ϣ
   * @param nodeName=/ManipulateConf/tChannel/tChannel_field
   * */
  public List searchAllXML(String nodeName){
	  	  listob = new ArrayList<ObInterface>();
	  	if(document != null){
	  		 
	  		 Element root = document.getRootElement();
			  //�˴�Ϊnodename
			  List<Element> childList = document.selectNodes(nodeName); 
			  try{
				  for (int i=0;i<childList.size();i++) {
					  	Iterator child =  childList.get(i).elementIterator();
					  	ObInterface ob= new ObInterface();
					  	for (;child.hasNext();){
					  		Element childElement =  (Element) child.next();
					  		String name = childElement.getName();
					  		String value = childElement.getText();
					  		ob.setFieldByName(name, value);
					  	}
				       listob.add(ob);
				   }
			  }catch(Exception e){
				  e.printStackTrace();
			  }
	  	}
	  	// ��sort��������	
	  	Collections.sort(listob, new Comparator<ObInterface>() {
			public int compare(ObInterface o1, ObInterface o2) {
				// TODO Auto-generated method stub
				return  (o1.getString("m_iTxnId")).compareTo((o2.getString("m_iTxnId")));
			}
		 });
		return listob;
  }
  /**
   * ���սڵ�˳���ѯ�����еĽڵ��������Ϣ
   * @param nodeName=/ManipulateConf/tChannel/tChannel_field
   * */
  public List searchAllXMLPTL(String nodeName){
	  	  listob = new ArrayList<ObInterface>();
	  	if(document != null){
	  		 
	  		 Element root = document.getRootElement();
			  //�˴�Ϊnodename
			  List<Element> childList = document.selectNodes(nodeName); 
			  try{
				  for (int i=0;i<childList.size();i++) {
					  	Iterator child =  childList.get(i).elementIterator();
					  	
					  	ObInterface ob= new ObInterface();
					  	ob.setFieldByName("fieldId",childList.get(i).attributeValue("id"));
					  	for (;child.hasNext();){
					  			
					  		Element childElement =  (Element) child.next();
					  		String name = childElement.getName();
					  		String value = childElement.getText();
					  		ob.setFieldByName(name, value);
					  	}
				       listob.add(ob);
				   }
			  }catch(Exception e){
				  e.printStackTrace();
			  }
	  	}
	  	// ��sort��������	
	  	Collections.sort(listob, new Comparator<ObInterface>() {
			public int compare(ObInterface o1, ObInterface o2) {
				// TODO Auto-generated method stub
				return  (o1.getString("m_iTxnId")).compareTo((o2.getString("m_iTxnId")));
			}
		 });
		return listob;
  }
  /**
   * ������������ѯ�����������Ϣ
   * @param nodeName=/ManipulateConf/tChannel/tChannel_field
   * */
  public ObInterface selectPtl(String nodeName,String filedId){
	  ObInterface ob= new ObInterface();	
	  if(document != null){
	  		 Element root = document.getRootElement();
			  //�˴�Ϊnodename
			  List<Element> childList = document.selectNodes(nodeName); 
			  try{
				  for (int i=0;i<childList.size();i++) {
					  	Iterator child =  childList.get(i).elementIterator();
					  	if(childList.get(i).attributeValue("id").equals(filedId)){
						  	ob.setFieldByName("fieldId",filedId);
						  	for (;child.hasNext();){
						  			
						  		Element childElement =  (Element) child.next();
						  		String name = childElement.getName();
						  		
						  		String value = childElement.getText();
						  		if(name.equals("name")){
						  			ob.setFieldByName("FieldName", value);
						  		}else{
						  			ob.setFieldByName(name, value);
						  		}
						  	}
						  	break;
					  	}
				   }
			  }catch(Exception e){
				  e.printStackTrace();
			  }
	  	}
	  	
		return ob;
  }
  /**
   * @author zymei
   * ���սڵ��ȡ�����ֵ��ȡ���ڵ��������Ϣ
   * 
   * */  
  public List getXML(String nodeName){
	  List<ObInterface> list = new ArrayList<ObInterface>();
	  return list;
  }
  /**
   * ʵ�ְ��սڵ����������еĽڵ��Ӧ��ֵ
   * @param parentNode ���ڵ����� root�µ�һ��ڵ�
   * @param childnode �ӽڵ����� root �µڶ��νڵ�
   * @param nodeName �����νڵ�
   * @return ��ȡ���ҽڵ�����ֵ��List
   * @author zymei
   * */
  public List getElementNum(String parentNode,String childNode,String nodeName){
	 
	  List<String> textList = new ArrayList<String>();
	  List<Element> list = document.getRootElement().element(parentNode).elements(childNode);
	  for(int i=0;i<list.size();i++){
		  String text =  list.get(i).element(nodeName).getText();
		  textList.add(text);
		  System.out.println(text);
	  }
	  return textList;
  }
  /**
   * ���Ҵ����Ϣ�������ƺ���ϢID
   * @author zymei
   * */
  public List<ObInterface> getMessageList(){
	  List<ObInterface> obList = new ArrayList<ObInterface>();
	  if(document != null){

		  List<Element> list = document.getRootElement().elements("op_rule");
		  List<Element> flist = new ArrayList();
		  for(int i=0;i<list.size();i++){
			  String messageName = list.get(i).element("name").getText();
			  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
			 // document.getElementsByTagName("tag");
			  Element  ele = list.get(i).element("rule");
			  ObInterface ob= new ObInterface();
			  int k = 0;
			  if(ele != null){
				  flist = ele.elements("field");
				  if(flist.size()>0){
					  for(int j=0;j<flist.size();j++){
							 Element pack = flist.get(j).element("pack");
							 if(pack != null){
								 try {
									//�����1
									ob.setFieldByName("packFlag", "1");
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							 }else {
								 try {
										//�����1
										ob.setFieldByName("packFlag", "0");
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							 }
						 }
				  }else{
					  	//����δ�趨����
						try {
							ob.setFieldByName("packFlag", "2");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				  }
				  
			  }
			 
			  try {
				ob.setFieldByName("messageName", messageName);
				ob.setFieldByName("messageId", messageId);
			  } catch (Exception e) {
				// TODO Auto-generated catch block
				  e.printStackTrace();
			  }
			  obList.add(ob);
		  }
	  }
	  return obList;
	
  }

  /**
   * ���Ҵ��������б�
   * @author zymei
   * */
  public List<ObInterface> getTransProList(){
	  
	  List<ObInterface> obList = new ArrayList<ObInterface>();
	  if(document != null){
		  List<Element> list = document.getRootElement().element("tMsg").elements("tMsg_field");
		  for(int i=0;i<list.size();i++){
			  String m_acMsgType = list.get(i).element("m_acMsgType").getText();
			  String m_acMsgCode = list.get(i).element("m_acMsgCode").getText();
			  String m_iTxnId = list.get(i).element("m_iTxnId").getText();
			  String m_iChannelType = list.get(i).element("m_iChannelType").getText();
			  String m_iReqResp = list.get(i).element("m_iReqResp").getText();
			  String m_iStartEnd = list.get(i).element("m_iStartEnd").getText();
			  String m_iMsgId = list.get(i).element("m_iMsgId").getText();
			  String m_iInterMsgId = list.get(i).element("m_iInterMsgId").getText();
			  String m_iPackFlag = list.get(i).element("m_iPackFlag").getText();
			  String m_iComment = list.get(i).element("m_iComment").getText();
			  String m_acAdd1="";
			  String m_acAdd2="";
			  if(list.get(i).element("m_acAdd1")!=null){
				  m_acAdd1 = list.get(i).element("m_acAdd1").getText();
			  }
			  if(list.get(i).element("m_acAdd2")!=null){
				  m_acAdd2 = list.get(i).element("m_acAdd2").getText();
			  }
			  ObInterface ob= new ObInterface();
			  try {
				ob.setFieldByName("m_acMsgType", m_acMsgType);
				ob.setFieldByName("m_acMsgCode", m_acMsgCode);
				ob.setFieldByName("m_iTxnId", m_iTxnId);
				ob.setFieldByName("m_iChannelType", m_iChannelType);
				ob.setFieldByName("m_iReqResp", m_iReqResp);
				ob.setFieldByName("m_iStartEnd", m_iStartEnd);
				ob.setFieldByName("m_iMsgId", m_iMsgId);
				ob.setFieldByName("m_iInterMsgId", m_iInterMsgId);
				ob.setFieldByName("m_iPackFlag", m_iPackFlag);
				ob.setFieldByName("m_iComment", m_iComment);
				ob.setFieldByName("m_acAdd1", m_acAdd1);
				ob.setFieldByName("m_acAdd2", m_acAdd2);
			  } catch (Exception e) {
				// TODO Auto-generated catch block
				  e.printStackTrace();
			  }
			  obList.add(ob);
		  }
	  }
	  return obList;
	
  }
  
  
  /**
   * ���ҽ����Ϣ�������ƺ���ϢID
   * @author zymei
   * String parentNode,String childNode,String childNodeid
   * */
  public List<ObInterface> getUnpackMessageList(){
	  List<ObInterface> obList = new ArrayList<ObInterface>();
	  if(document != null){
		  List<Element> list = document.getRootElement().elements("op_rule");
		  try {
			  for(int i=0;i<list.size();i++){
				  String messageName = list.get(i).element("name").getText();
				  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
				  String fieldId="";
				  String fieldType="";
				  String fieldValue="";
				  List<Element> fieldList = list.get(i).element("msg_type").elements("field");
				  ObInterface ob= new ObInterface();
				  for(int k=0;k<fieldList.size();k++){
						  fieldId = fieldList.get(k).attributeValue("id");
						  fieldType = fieldList.get(k).attributeValue("type");
						  fieldValue = fieldList.get(k).attributeValue("value");
						  ob.setFieldByName("fieldId"+(k+1), fieldId);
						  ob.setFieldByName("fieldType"+(k+1), fieldType);
						  ob.setFieldByName("fieldValue"+(k+1), fieldValue);
						  
				  }
				  
					ob.setFieldByName("unmessageName", messageName);
					ob.setFieldByName("unmessageId", messageId);
				  
				  obList.add(ob);
			  }
		  } catch (Exception e) {
				// TODO Auto-generated catch block
				  e.printStackTrace();
			  }
	  }
	  return obList;
	
  }
  /**
   * ������Ϣ����IDɾ����Ӧ��op_rule����
   * @author zymei
   * String parentNode,String childNode,String childNodeid
   * */
  public boolean deleteMessage(String msgid){
	  boolean flag = false;
	  boolean flag1 = false;
	  List<ObInterface> obList = new ArrayList<ObInterface>();
	  if(document != null){
		  List<Element> list = document.getRootElement().elements("op_rule");
		  for(int i=0;i<list.size();i++){
			  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
			  if(messageId.equals(msgid)){
				  flag = document.getRootElement().remove(list.get(i));
			  }
		  }
		  flag1 = saveXMLFile(document);
	  }
	  return flag & flag1;
  }
  /**
   * �޸���Ϣ����
   * @author zymei
   * @param msgidOld ԭֵ
   * @param msgidnew ��ֵ
   * @param msgname ������
   * */
  public boolean updateMessage(String msgidold,String msgidnew,String msgname){
	  boolean flag = false;
	  List<ObInterface> obList = new ArrayList<ObInterface>();
	  if(document != null){
		  try{
			  List<Element> list = document.getRootElement().elements("op_rule");
			  for(int i=0;i<list.size();i++){
				  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
				  if(messageId.equals(msgidnew)){
					//  list.get(i).element("msg_type").element("msg_id").setText(msgidnew);
					  list.get(i).element("name").setText(msgname);
					  flag = true;
				  }
			  }
			  if(flag){
				  saveXMLFile(document);
			  }
		  }catch (Exception e ){
			  e.printStackTrace();
			  flag = false;
		  }
	  }
	  return flag;
  }
  /**
   * �޸Ľ����Ϣ����
   * @author zymei
   * @param msgidOld ԭֵ
   * @param msgidnew ��ֵ
   * @param msgname ������
   * @param ob ��װ�����н����ص���Ϣ����
   * */
  public boolean updateUnpackMessage(String msgidold,String msgidnew,String msgname,ObInterface ob){
	  boolean flag = false;
	  if(document != null){
		  try{
			  List<ObInterface> obList = new ArrayList<ObInterface>();
			  List<Element> list = document.getRootElement().elements("op_rule");
			  
			  for(int i=0;i<list.size();i++){
				  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
				  if(messageId.equals(msgidold)){
					  list.get(i).element("msg_type").element("msg_id").setText(msgidnew);
					  list.get(i).element("name").setText(msgname);
					  
					  	//�Ƿ���ҪfieldId1 
						 if(list.get(i).element("msg_type").element("field")!= null){
				    			boolean u = false;
				    			if(list.get(i).element("msg_type").element("field") != null){
				    				
				    					List<Element> fieldList = list.get(i).element("msg_type").elements("field");
				    					for(int h=0;h<fieldList.size();h++){
				    						u = list.get(i).element("msg_type").remove(list.get(i).element("msg_type").element("field"));
				    					}
				    			}else {
				    				u = true;
				    			}
				    			
				    			if(u){
				    				if(ob.getString("fieldId1").trim().length()>0){
				    					Element field1 = list.get(i).element("msg_type").addElement("field");
				    					field1.addAttribute("id",ob.getString("fieldId1"));
				    					System.out.println(ob.getString("fieldId1")+"ttejlajfl");
				    					field1.addAttribute("type",ob.getString("fieldType1"));
				    					System.out.println(ob.getString("fieldType1")+"ttejlajfl");
				    					field1.addAttribute("value",ob.getString("fieldValue1"));
				    					System.out.println(ob.getString("fieldValue1")+"ttejlajfl");
				    				}
				    				if(ob.getString("fieldId2").trim().length()>0){
				    					Element field1 = list.get(i).element("msg_type").addElement("field");
				    					field1.addAttribute("id",ob.getString("fieldId2"));
				    					field1.addAttribute("type",ob.getString("fieldType2"));
				    					field1.addAttribute("value",ob.getString("fieldValue2"));
				    				}
				    			}
						 }
					 flag = true; 
				  }
			  }
			  System.out.println(flag);
			  if(flag){
				  flag = saveXMLFile(document);
			  }
		  }catch(Exception e){
			  e.printStackTrace();
			  flag = false;
		  }
	  }
	  return flag;
  }
  /**
   * ������Ϣ����ID����ȡ������ID�������ֵ
   * @author zymei
   * @param msgid �����ڵ���Ϣ����id
   * */
  public List<ObInterface> getFieldList(String msgid){
	  if(document != null){
		  List<Element> list = document.getRootElement().elements("op_rule");
		  for(int i=0;i<list.size();i++){
			  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
			  if(messageId.equals(msgid)){
				 List<Element> fieldList = list.get(i).element("rule").elements("field");
				 for(int j=0;j<fieldList.size();j++){
					 try {
						obInterface = new ObInterface();
						String fieldIndex="";
						String fieldName="";
						String defaultValue="";
						String valueLen="";
						String needMac="";
						String innerId="";
						fieldName = fieldList.get(j).element("name").getText();
						fieldIndex =fieldList.get(j).attributeValue("id");
						
						Element pack =  fieldList.get(j).element("pack");
						if(pack.element("default_value") != null){
							defaultValue = pack.element("default_value").getText();
						}
						if(pack.element("value_len") != null){
							valueLen = pack.element("value_len").getText();
						}
						if(fieldList.get(j).element("need_mac") != null){
							needMac = fieldList.get(j).element("need_mac").getText();
						}
						List<Element> innerFieldList = pack.elements("interfield");
						if(innerFieldList.size()>0){
							StringBuffer bf = new StringBuffer();
							for(int k=0;k<innerFieldList.size();k++){
								innerId = innerFieldList.get(k).element("id").getText();
								bf.append(innerId);
								if(k!=innerFieldList.size()-1){
									bf.append(",");
								}
								
							}
							obInterface.setFieldByName("innerField", bf.toString());
						}
						
						obInterface.setFieldByName("fieldindex", fieldIndex);
						obInterface.setFieldByName("fieldName", fieldName);
						obInterface.setFieldByName("defaultValue", defaultValue);
						obInterface.setFieldByName("valueLen", valueLen);
						obInterface.setFieldByName("needMac", needMac);
						listob.add(obInterface);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			  }
		  }
	  }
	  return listob;
  }
  //���մ�����ʶѰ�Ҹ��Ե���Ϣ
  public ObInterface getMsgInfo(String msgid,String msgname,String packFlag){
	  ObInterface ob = new ObInterface();
		  if(document !=null){
			  try{
				  Element root = document.getRootElement();
				//  Element tMsgState = root.element("op_rule");
				  List<Element> list = new ArrayList<Element>();
				  list = root.elements("op_rule");
				  
				  for(int i=0;i<list.size();i++){
					  String name = list.get(i).element("name").getText();
					  String stateNum1 = list.get(i).element("msg_type").element("msg_id").getText();
					  
					  if((name.trim().equals(msgname.trim())) && (msgid.trim().equals(stateNum1.trim()))){
						 //���
						 ob.setFieldByName("s_messageId",msgid);
						 ob.setFieldByName("s_messageName",msgname);
						 
						 //���
						 ob.setFieldByName("unmessageId",msgid);
						 ob.setFieldByName("unmessageName",msgname);
						 
						 if(list.get(i).element("msg_type").element("field") != null){
							 List<Element> list2 = new ArrayList<Element>();
							 list2 = list.get(i).element("msg_type").elements("field");
							 for(int j=0;j<list2.size();j++){
								 ob.setFieldByName("fieldId"+(j+1),list2.get(j).attributeValue("id"));
								 ob.setFieldByName("fieldType"+(j+1),list2.get(j).attributeValue("type"));
								 ob.setFieldByName("fieldValue"+(j+1),list2.get(j).attributeValue("value"));
							 }
						 }
					  }
				  }
				 // saveXMLFile(document);
			  }catch(Exception e){
				  e.printStackTrace();
			  } 
		  }
		  return ob;
	  
  }
  /**
   * ���
   * ������Ϣ����ID����ȡ������ID�������ֵ
   * @author zymei
   * @param msgid �����ڵ���Ϣ����id
   * */
  public List<ObInterface> getUnpackFieldList(String msgid){
	  if(document != null){
		  List<Element> list = document.getRootElement().elements("op_rule");
		  for(int i=0;i<list.size();i++){
			  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
			  if(messageId.equals(msgid)){
				 List<Element> fieldList = list.get(i).element("rule").elements("field");
				 for(int j=0;j<fieldList.size();j++){
					 try {
						obInterface = new ObInterface();
						String fieldIndex="";
						String fieldName="";
						String defaultValue="";
						String valueLen="";
						String needMac="";
						String innerId="";
						fieldName = fieldList.get(j).element("name").getText();
						fieldIndex =fieldList.get(j).attributeValue("id");
						
						Element unpack =  fieldList.get(j).element("unpack");
//						if(pack.element("default_value") != null){
//							defaultValue = pack.element("default_value").getText();
//						}
//						if(pack.element("value_len") != null){
//							valueLen = pack.element("value_len").getText();
//						}
						if(fieldList.get(j).element("need_mac") != null){
							needMac = fieldList.get(j).element("need_mac").getText();
						}
						List<Element> innerFieldList = unpack.elements("interfield");
						if(innerFieldList.size()>0){
							StringBuffer bf = new StringBuffer();
							for(int k=0;k<innerFieldList.size();k++){
								innerId = innerFieldList.get(k).element("id").getText();
								bf.append(innerId);
								if(k!=innerFieldList.size()-1){
									bf.append(",");
								}
								
							}
							obInterface.setFieldByName("innerField", bf.toString());
						}
						
						obInterface.setFieldByName("fieldindex", fieldIndex);
						obInterface.setFieldByName("fieldName", fieldName);
//						obInterface.setFieldByName("defaultValue", defaultValue);
//						obInterface.setFieldByName("valueLen", valueLen);
						obInterface.setFieldByName("needMac", needMac);
						listob.add(obInterface);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			  }
		  }
		  
	  }
	  return listob;
  }
  
  /**
   * ������Ϣ����ID�����´���������ֵ
   * @author zymei
   * @param msgid �����ڵ���Ϣ����id
   * */
  public boolean updateField(String msgid,ObInterface ob){
	  boolean flag = false;
	  if(document !=null){
		  try{
			  String indexOld=ob.getString("fieldIndex");  
			  List<Element> list = document.getRootElement().elements("op_rule");
			  for(int i=0;i<list.size();i++){
				  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
				  //�ҵ���ͬ��msgid
				  if(messageId.equals(msgid)){
					 List<Element> fieldList = list.get(i).element("rule").elements("field");
					 String idindex ="";
					// Iterator<Element> fieldID = list.get(i).element("rule").elementIterator("field");
					
					// while(fieldID.hasNext()){
					 for(int j=0;j<fieldList.size();j++){
					 	 idindex = fieldList.get(j).attributeValue("id");
						// System.out.println(idindex+"---------");
					//	 System.out.println(indexOld+"---------");
						 //��õ�������id == ���ҵ�ID
						 if(idindex.equals(indexOld)){
							 
							 fieldList.get(j).element("name").setText(ob.getString("fieldName"));
							 //�ڲ�����
							 fieldList.get(j).element("pack").element("interfield").element("id").setText(ob.getString("innerField"));
							 
							 //�Ƿ���ҪmacУ��
							 if(fieldList.get(j).element("need_mac")!= null){
					    			if(ob.getString("needMac").trim().length()>0){
					    				fieldList.get(j).element("need_mac").setText(ob.getString("needMac"));
					    			}else{
					    				fieldList.get(j).remove(fieldList.get(j).element("need_mac"));
					    			}
					    			
					    		}else{
					    				if(ob.getString("needMac").trim().length()>0){
						    				Element needMac = fieldList.get(j).addElement("need_mac");
						    				needMac.setText(ob.getString("needMac"));
						    			}
					    		}
							 //����
							 if(fieldList.get(j).element("pack").element("value_len")!= null){
								 	System.out.println(ob.getString("valueLen").length()+"---");
					    			if(ob.getString("valueLen").trim().length()>0){
					    				fieldList.get(j).element("pack").element("value_len").setText(ob.getString("valueLen"));
					    			}else{
					    				boolean falg = fieldList.get(j).element("pack").remove(fieldList.get(j).element("pack").element("value_len"));
					    				System.out.println(flag);
					    			}
					    			
					    		}else{
					    				if(ob.getString("valueLen").trim().length()>0){
						    				Element needMac = fieldList.get(j).element("pack").addElement("value_len");
						    				needMac.setText(ob.getString("valueLen"));
						    			}
					    		}
							 //Ĭ��ֵ
							 if(fieldList.get(j).element("pack").element("default_value")!= null){
					    			if(ob.getString("defaultValue").trim().length()>0){
					    				fieldList.get(j).element("pack").element("default_value").setText(ob.getString("defaultValue"));
					    			}else{
					    				fieldList.get(j).element("pack").remove(fieldList.get(j).element("pack").element("default_value"));
					    			}
					    			
					    		}else{
					    				if(ob.getString("defaultValue").trim().length()>0){
						    				Element needMac = fieldList.get(j).element("pack").addElement("default_value");
						    				needMac.setText(ob.getString("defaultValue"));
						    			}
					    		}
						 }
					 }
					
					 
					 
				  }
			  }
			  flag = saveXMLFile(document);
		  }catch(Exception e){
			  e.printStackTrace();
			  flag = false;
		  }
	  }
	  return flag;
  }
  /**
   * ������Ϣ����ID�����µ�������������ֵ
   * @author zymei
   * @param msgid �����ڵ���Ϣ����id
   * */
  public boolean s_updateField(String msgid,ObInterface ob){
	  boolean flag = false;
	  if(document != null){
		  try{
			  String indexOld=ob.getString("s_fieldIndex");  
			  List<Element> list = document.getRootElement().elements("op_rule");
			  for(int i=0;i<list.size();i++){
				  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
				  //�ҵ���ͬ��msgid
				  if(messageId.equals(msgid)){
					 List<Element> fieldList = list.get(i).element("rule").elements("field");
					 String idindex ="";
					// Iterator<Element> fieldID = list.get(i).element("rule").elementIterator("field");
					
					// while(fieldID.hasNext()){
					 for(int j=0;j<fieldList.size();j++){
					 	 idindex = fieldList.get(j).attributeValue("id");
						// System.out.println(idindex+"---------");
					//	 System.out.println(indexOld+"---------");
						 //��õ�������id == ���ҵ�ID
						 if(idindex.equals(indexOld)){
							 
							 fieldList.get(j).element("name").setText(ob.getString("s_fieldName"));
							 //�ڲ�����
							 fieldList.get(j).element("pack").element("interfield").element("id").setText(ob.getString("s_innerField"));
							 
							 //�Ƿ���ҪmacУ��
							 if(fieldList.get(j).element("need_mac")!= null){
					    			if(ob.getString("s_needMac").trim().length()>0){
					    				fieldList.get(j).element("need_mac").setText(ob.getString("s_needMac"));
					    			}else{
					    				fieldList.get(j).remove(fieldList.get(j).element("need_mac"));
					    			}
					    			
					    		}else{
					    				if(ob.getString("s_needMac").trim().length()>0){
						    				Element needMac = fieldList.get(j).addElement("need_mac");
						    				needMac.setText(ob.getString("s_needMac"));
						    			}
					    		}
							 //����
							 if(fieldList.get(j).element("pack").element("value_len")!= null){
								 	System.out.println(ob.getString("s_valueLen").length()+"---");
					    			if(ob.getString("s_valueLen").trim().length()>0){
					    				fieldList.get(j).element("pack").element("value_len").setText(ob.getString("s_valueLen"));
					    			}else{
					    				boolean falg = fieldList.get(j).element("pack").remove(fieldList.get(j).element("pack").element("value_len"));
					    				System.out.println(flag);
					    			}
					    			
					    		}else{
					    				if(ob.getString("s_valueLen").trim().length()>0){
						    				Element needMac = fieldList.get(j).element("pack").addElement("value_len");
						    				needMac.setText(ob.getString("s_valueLen"));
						    			}
					    		}
							 //Ĭ��ֵ
							 if(fieldList.get(j).element("pack").element("default_value")!= null){
					    			if(ob.getString("s_defaultValue").trim().length()>0){
					    				fieldList.get(j).element("pack").element("default_value").setText(ob.getString("s_defaultValue"));
					    			}else{
					    				fieldList.get(j).element("pack").remove(fieldList.get(j).element("pack").element("default_value"));
					    			}
					    			
					    		}else{
					    				if(ob.getString("s_defaultValue").trim().length()>0){
						    				Element needMac = fieldList.get(j).element("pack").addElement("default_value");
						    				needMac.setText(ob.getString("s_defaultValue"));
						    			}
					    		}
						 }
					 }
					
					 
					 
				  }
			  }
			  flag = saveXMLFile(document);
		  }catch(Exception e){
			  e.printStackTrace();
			  flag = false;
		  } 
	  }
	  return flag;
  }
  /**
   * ������Ϣ����ID�����´���������ֵ
   * @author zymei
   * @param msgid �����ڵ���Ϣ����id
   * */
  public boolean updateUnpackField(String msgid,ObInterface ob){
	  boolean flag = false;
	  if(document != null){
		  try{
			  String indexOld=ob.getString("unfieldIndex");  
			  List<Element> list = document.getRootElement().elements("op_rule");
			  for(int i=0;i<list.size();i++){
				  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
				  //�ҵ���ͬ��msgid
				  if(messageId.equals(msgid)){
					 List<Element> fieldList = list.get(i).element("rule").elements("field");
					 String idindex ="";
					 for(int j=0;j<fieldList.size();j++){
					 	 idindex = fieldList.get(j).attributeValue("id");
						 //��õ�������id == ���ҵ�ID
						 if(idindex.equals(indexOld)){
							 fieldList.get(j).element("name").setText(ob.getString("unfieldName"));
							 //�ڲ�����
							 if(ob.getString("uninnerField").trim().length()>0){
								 //ɾ��ԭ���ڲ���
								boolean u = fieldList.get(j).remove(fieldList.get(j).element("unpack"));
								 //�������޸ĵ��ڲ���
								if(u){
									fieldList.get(j).addElement("unpack");
									String[] inner = ob.getString("uninnerField").split(",");
									 for(int k=0;k<inner.length;k++){
										 System.out.println(inner[k]);
										 Element interfield = fieldList.get(j).element("unpack").addElement("interfield");
										 Element id = interfield.addElement("id");
										 id.setText(inner[k]);
									 }
								}
							 }
							 
							 //�Ƿ���ҪmacУ��
							 if(fieldList.get(j).element("need_mac")!= null){
					    			if(ob.getString("unneedMac").trim().length()>0){
					    				fieldList.get(j).element("need_mac").setText(ob.getString("unneedMac"));
					    			}else{
					    				fieldList.get(j).remove(fieldList.get(j).element("need_mac"));
					    			}
					    			
					    		}else{
					    				if(ob.getString("unneedMac").trim().length()>0){
						    				Element needMac = fieldList.get(j).addElement("need_mac");
						    				needMac.setText(ob.getString("unneedMac"));
						    			}
					    		}
							}
					 }
					
					 
					 
				  }
			  }
			  flag = saveXMLFile(document);
		  }catch(Exception e){
			  e.printStackTrace();
			  flag = false;
		  }
		  
	  }
	  return flag;
  }
  
  /**
   * ������Ϣ����ID��ɾ���������ֵ
   * @author zymei
   * @param msgid �����ڵ���Ϣ����id
   * */
  public boolean deleteFieldById(String msgid,ObInterface ob){
	  boolean flag = false;
	  if(document != null){
		  try{
			  String indexOld=ob.getString("fieldIndex");  
			  System.out.println(indexOld+"indexOld");
			  List<Element> list = document.getRootElement().elements("op_rule");
			  for(int i=0;i<list.size();i++){
				  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
				  //�ҵ���ͬ��msgid
				  if(messageId.equals(msgid)){
					 List<Element> fieldList = list.get(i).element("rule").elements("field");
					 String idindex ="";
					 for(int j=0;j<fieldList.size();j++){
					 	 idindex = fieldList.get(j).attributeValue("id");
						 //��õ�������id == ���ҵ�ID
						 if(idindex.equals(indexOld)){
							 //ɾ����field
							 flag = list.get(i).element("rule").remove(fieldList.get(j));
						 }
					 }
				  }
			  }
			  flag = saveXMLFile(document);
		  }catch(Exception e){
			  e.printStackTrace();
			  flag = false;
		  } 
	  }
	  return flag;
  }
  /**
   * ������Ϣ����ID��ɾ�������������ֵ
   * @author zymei
   * @param msgid �����ڵ���Ϣ����id
   * */
  public boolean s_deleteFieldById(String msgid,ObInterface ob){
	  boolean flag = false;
	  if(document != null){
		  try{
			  String indexOld=ob.getString("s_fieldIndex");  
			  System.out.println(indexOld+"indexOld");
			  List<Element> list = document.getRootElement().elements("op_rule");
			  for(int i=0;i<list.size();i++){
				  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
				  //�ҵ���ͬ��msgid
				  if(messageId.equals(msgid)){
					 List<Element> fieldList = list.get(i).element("rule").elements("field");
					 String idindex ="";
					 for(int j=0;j<fieldList.size();j++){
					 	 idindex = fieldList.get(j).attributeValue("id");
						 //��õ�������id == ���ҵ�ID
						 if(idindex.equals(indexOld)){
							 //ɾ����field
							 flag = list.get(i).element("rule").remove(fieldList.get(j));
						 }
					 }
				  }
			  }
			  flag = saveXMLFile(document);
		  }catch(Exception e){
			  e.printStackTrace();
			  flag = false;
		  }
	  }
	  return flag;
	  
  }
  /**
   * ������Ϣ����ID��ɾ���������ֵ
   * @author zymei
   * @param msgid �����ڵ���Ϣ����id
   * */
  public boolean deleteUnpackFieldById(String msgid,ObInterface ob){
	  boolean flag = false;
	  boolean flag1 = false;
	  if(document != null){
		  try{
			  String indexOld=ob.getString("unfieldIndex");  
			  System.out.println(indexOld+"indexOld");
			  List<Element> list = document.getRootElement().elements("op_rule");
			  for(int i=0;i<list.size();i++){
				  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
				  //�ҵ���ͬ��msgid
				  if(messageId.equals(msgid)){
					 List<Element> fieldList = list.get(i).element("rule").elements("field");
					 String idindex ="";
					 for(int j=0;j<fieldList.size();j++){
					 	 idindex = fieldList.get(j).attributeValue("id");
						 //��õ�������id == ���ҵ�ID
						 if(idindex.equals(indexOld)){
							 //ɾ����field
							 flag1 = list.get(i).element("rule").remove(fieldList.get(j));
						 }
					 }
				  }
			  }
			  flag = saveXMLFile(document);
		  }catch(Exception e){
			  e.printStackTrace();
			  flag = false;
		  } 
	  }
	  return flag & flag1;
	  
  }
  /**
   * ������Ϣ����ID�����Ӵ��ĳ����
   * @author zymei
   * @param msgid �����ڵ���Ϣ����id
   * */
  public boolean addFieldInfo(String msgid,ObInterface ob){
	  boolean flag = false;
	  if(document != null){
		  try{
			  //�����ӵ�����ID
			  String indexOld=ob.getString("fieldIndex");  
			  System.out.println(indexOld+"indexOld");
			  List<Element> list = document.getRootElement().elements("op_rule");
			  for(int i=0;i<list.size();i++){
				  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
				  //�ҵ���ͬ��msgid
				  if(messageId.equals(msgid)){
					  	List<Element> fieldList = list.get(i).element("rule").elements("field");
						 String idindex ="";
						 //�ж�����д���������Ƿ���֮ǰ����������ͻ
						 //��ֹ��һ�����ʱ��falg=false�Ĵ���
						 if(fieldList.size()>0){
							 for(int j=0;j<fieldList.size();j++){
							 	 idindex = fieldList.get(j).attributeValue("id");
								 //��õ�������id == ���ҵ�ID
								 if(idindex.equals(indexOld)){
									 //��������,����Ӳ��ɹ�
									flag = false;
									break;
								 }else{
									 //�����������Ϣ
									 flag = true;
								 }
							 }
						 }else{
							 flag = true;
						 }
						 Element field = list.get(i).element("rule").addElement("field");
						 field.addAttribute("class", "OFPOPRule");
						 field.addAttribute("id", indexOld);
						 //������
						 Element name = field.addElement("name");
						 name.setText(ob.getString("fieldName"));
						 //mac У��
						 Element need_mac = field.addElement("need_mac");
						 need_mac.setText(ob.getString("needMac"));
						 //packԪ��
						 Element pack = field.addElement("pack");
						 Element interfield = pack.addElement("interfield");
						 Element id = interfield.addElement("id");
						 id.setText(ob.getString("innerField"));
						 //����
						 Element value_len = pack.addElement("value_len");
						 value_len.setText(ob.getString("valueLen"));
						 //Ĭ��ֵ
						 Element default_value = pack.addElement("default_value");
						 default_value.setText(ob.getString("defaultValue"));
						 
				  }
			  }
			  if(flag){
				  saveXMLFile(document);
			  }
		  }catch(Exception e){
			  e.printStackTrace();
			  flag = false;
		  }  
	  }
	  return flag;
	  
  }
  public boolean addPtlInfo(ObInterface ob){
	  boolean flag = false;
	  if(document != null){
		  try{
			  //�����ӵ�����ID
			  Element field = document.getRootElement().element("ptl_fields").addElement("field");
			  field.addAttribute("class", "OFPPtlRule");
			  field.addAttribute("id", obInterface.getString("fieldId")); 
			  
			 //������
			 Element name = field.addElement("name");
			 name.setText(ob.getString("fieldName"));
			 
			 if(ob.getString("max_field_len").length()>0){
				 Element need_mac = field.addElement("max_field_len");
				 need_mac.setText(ob.getString("max_field_len"));
			 }
			 
			 if(ob.getString("field_len_type").length()>0){
				 Element ele = field.addElement("field_len_type");
				 ele.setText(ob.getString("field_len_type"));
			 }
			 
			 if(ob.getString("stream_type").length()>0){
				 Element ele = field.addElement("stream_type");
				 ele.setText(ob.getString("stream_type"));
			 }
			 
			 if(ob.getString("store_type").length()>0){
				 Element ele = field.addElement("store_type");
				 ele.setText(ob.getString("store_type"));
			 }
			 
			 if(ob.getString("field_exist_type").length()>0){
				 Element ele = field.addElement("field_exist_type");
				 ele.setText(ob.getString("field_exist_type"));
			 }
			
			 if(ob.getString("bitmap_type").length()>0){
				 Element ele = field.addElement("bitmap_type");
				 ele.setText(ob.getString("bitmap_type"));
			 }
			 
			 if(ob.getString("bitmap_position").length()>0){
				 Element ele = field.addElement("bitmap_position");
				 ele.setText(ob.getString("bitmap_position"));
			 }
			 if(ob.getString("fill_type").length()>0){
				 Element ele = field.addElement("fill_type");
				 ele.setText(ob.getString("fill_type"));
			 }
			
			 if(ob.getString("fill_char").length()>0){
				 Element ele = field.addElement("fill_char");
				 ele.setText(ob.getString("fill_char"));
			 }
			 
			 if(ob.getString("self_store_len_type").length()>0){
				 Element ele = field.addElement("self_store_len_type");
				 ele.setText(ob.getString("self_store_len_type"));
			 }
			 
			 if(ob.getString("self_store_len").length()>0){
				 Element ele = field.addElement("self_store_len");
				 ele.setText(ob.getString("self_store_len"));
			 }
			 
			 if(ob.getString("pack_len_field").length()>0){
				 Element ele = field.addElement("pack_len_field");
				 ele.setText(ob.getString("pack_len_field"));
			 }
			 
			 if(ob.getString("mac_field").length()>0){
				 Element ele = field.addElement("mac_field");
				 ele.setText(ob.getString("mac_field"));
			 }
			 
			 if(ob.getString("head_cut").length()>0){
				 Element ele = field.addElement("head_cut");
				 ele.setText(ob.getString("head_cut"));
			 }
			 
			 if(ob.getString("force_exist").length()>0){
				 Element ele = field.addElement("force_exist");
				 ele.setText(ob.getString("force_exist"));
			 }
			 
			 flag = saveXMLFile(document);
			 
		  }catch(Exception e){
			  e.printStackTrace();
			  flag = false;
		  }  
	  }
	  return flag;
	  
  }
  
  public boolean updatePtlInfo(ObInterface ob){
	  boolean flag = false;
	  
	  if(document != null){
		 try{
		  List<Element> fieldList = document.getRootElement().element("ptl_fields").elements("field");
		  for(int i=0;i<fieldList.size();i++){
			  String filedId = fieldList.get(i).attributeValue("id");
			  if(filedId.equals(ob.getString("fieldId"))){
				  	 //�Ƿ���ҪmacУ��
			  	 fieldList.get(i).element("name").setText(ob.getString("FieldName"));
			  	 fieldList.get(i).element("max_field_len").setText(ob.getString("max_field_len"));
			  	 fieldList.get(i).element("field_len_type").setText(ob.getString("field_len_type"));
			  	 fieldList.get(i).element("stream_type").setText(ob.getString("stream_type"));
			  	 fieldList.get(i).element("store_type").setText(ob.getString("store_type"));
			  	 fieldList.get(i).element("field_exist_type").setText(ob.getString("field_exist_type"));
				 
			  	 if(fieldList.get(i).element("bitmap_type")!= null){
		 			if(ob.getString("bitmap_type").trim().length()>0){
		 				fieldList.get(i).element("bitmap_type").setText(ob.getString("bitmap_type"));
		 			}else{
		 				fieldList.get(i).remove(fieldList.get(i).element("bitmap_type"));
		 			}
		 			
		 		}else{
		 				if(ob.getString("bitmap_type").trim().length()>0){
			    				Element needMac = fieldList.get(i).addElement("bitmap_type");
			    				needMac.setText(ob.getString("bitmap_type"));
			    			}
		 		}
			  	 
			  	if(fieldList.get(i).element("bitmap_position")!= null){
		 			if(ob.getString("bitmap_position").trim().length()>0){
		 				fieldList.get(i).element("bitmap_position").setText(ob.getString("bitmap_position"));
		 			}else{
		 				fieldList.get(i).remove(fieldList.get(i).element("bitmap_position"));
		 			}
		 			
		 		}else{
		 				if(ob.getString("bitmap_position").trim().length()>0){
			    				Element needMac = fieldList.get(i).addElement("bitmap_position");
			    				needMac.setText(ob.getString("bitmap_position"));
			    			}
		 		}
			  	
			  	if(fieldList.get(i).element("fill_type")!= null){
		 			if(ob.getString("fill_type").trim().length()>0){
		 				fieldList.get(i).element("fill_type").setText(ob.getString("fill_type"));
		 			}else{
		 				fieldList.get(i).remove(fieldList.get(i).element("fill_type"));
		 			}
		 			
		 		}else{
		 				if(ob.getString("fill_type").trim().length()>0){
			    				Element needMac = fieldList.get(i).addElement("fill_type");
			    				needMac.setText(ob.getString("fill_type"));
			    			}
		 		}
			  	
			  	if(fieldList.get(i).element("fill_char")!= null){
		 			if(ob.getString("fill_char").trim().length()>0){
		 				fieldList.get(i).element("fill_char").setText(ob.getString("fill_char"));
		 			}else{
		 				fieldList.get(i).remove(fieldList.get(i).element("fill_char"));
		 			}
		 			
		 		}else{
		 				if(ob.getString("fill_char").trim().length()>0){
			    				Element needMac = fieldList.get(i).addElement("fill_char");
			    				needMac.setText(ob.getString("fill_char"));
			    			}
		 		}
			  	
			  	if(fieldList.get(i).element("self_store_len_type")!= null){
		 			if(ob.getString("self_store_len_type").trim().length()>0){
		 				fieldList.get(i).element("self_store_len_type").setText(ob.getString("self_store_len_type"));
		 			}else{
		 				fieldList.get(i).remove(fieldList.get(i).element("self_store_len_type"));
		 			}
		 			
		 		}else{
		 				if(ob.getString("self_store_len_type").trim().length()>0){
			    				Element needMac = fieldList.get(i).addElement("self_store_len_type");
			    				needMac.setText(ob.getString("self_store_len_type"));
			    			}
		 		}

			  	if(fieldList.get(i).element("self_store_len")!= null){
		 			if(ob.getString("self_store_len").trim().length()>0){
		 				fieldList.get(i).element("self_store_len").setText(ob.getString("self_store_len"));
		 			}else{
		 				fieldList.get(i).remove(fieldList.get(i).element("self_store_len"));
		 			}
		 			
		 		}else{
		 				if(ob.getString("self_store_len").trim().length()>0){
			    				Element needMac = fieldList.get(i).addElement("self_store_len");
			    				needMac.setText(ob.getString("self_store_len"));
			    			}
		 		}
			  	
			  	if(fieldList.get(i).element("pack_len_field")!= null){
		 			if(ob.getString("pack_len_field").trim().length()>0){
		 				fieldList.get(i).element("pack_len_field").setText(ob.getString("pack_len_field"));
		 			}else{
		 				fieldList.get(i).remove(fieldList.get(i).element("pack_len_field"));
		 			}
		 			
		 		}else{
		 				if(ob.getString("pack_len_field").trim().length()>0){
			    				Element needMac = fieldList.get(i).addElement("pack_len_field");
			    				needMac.setText(ob.getString("pack_len_field"));
			    			}
		 		}
			  	
			  	if(fieldList.get(i).element("mac_field")!= null){
		 			if(ob.getString("mac_field").trim().length()>0){
		 				fieldList.get(i).element("mac_field").setText(ob.getString("mac_field"));
		 			}else{
		 				fieldList.get(i).remove(fieldList.get(i).element("mac_field"));
		 			}
		 			
		 		}else{
		 				if(ob.getString("mac_field").trim().length()>0){
			    				Element needMac = fieldList.get(i).addElement("mac_field");
			    				needMac.setText(ob.getString("mac_field"));
			    			}
		 		}
			  	
			  	if(fieldList.get(i).element("head_cut")!= null){
		 			if(ob.getString("head_cut").trim().length()>0){
		 				fieldList.get(i).element("head_cut").setText(ob.getString("head_cut"));
		 			}else{
		 				fieldList.get(i).remove(fieldList.get(i).element("head_cut"));
		 			}
		 			
		 		}else{
		 				if(ob.getString("head_cut").trim().length()>0){
			    				Element needMac = fieldList.get(i).addElement("head_cut");
			    				needMac.setText(ob.getString("head_cut"));
			    			}
		 		}
			  	
			  	if(fieldList.get(i).element("force_exist")!= null){
		 			if(ob.getString("force_exist").trim().length()>0){
		 				fieldList.get(i).element("force_exist").setText(ob.getString("force_exist"));
		 			}else{
		 				fieldList.get(i).remove(fieldList.get(i).element("force_exist"));
		 			}
		 			
		 		}else{
		 				if(ob.getString("force_exist").trim().length()>0){
			    				Element needMac = fieldList.get(i).addElement("force_exist");
			    				needMac.setText(ob.getString("force_exist"));
			    			}
		 		}
			  	flag = saveXMLFile(document);
			  }
		  }
		 }catch(Exception e){
			  flag = false;
		 }
	  }
	  return flag;
  }
  /**
   * ������Ϣ����ID�����Ӵ��ĳ����
   * @author zymei
   * @param msgid �����ڵ���Ϣ����id
   * */
  public boolean s_addFieldInfo(String msgid,ObInterface ob){
	  boolean flag = false;
	  if(document != null){
		  try{
			  //�����ӵ�����ID
			  String indexOld=ob.getString("s_fieldIndex");  
			  System.out.println(indexOld+"indexOld");
			  List<Element> list = document.getRootElement().elements("op_rule");
			  for(int i=0;i<list.size();i++){
				  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
				  //�ҵ���ͬ��msgid
				  if(messageId.equals(msgid)){
					  	List<Element> fieldList = list.get(i).element("rule").elements("field");
						 String idindex ="";
						 //�ж�����д���������Ƿ���֮ǰ����������ͻ
						 //��ֹ��һ�����ʱ��falg=false�Ĵ���
						 if(fieldList.size()>0){
							 for(int j=0;j<fieldList.size();j++){
							 	 idindex = fieldList.get(j).attributeValue("id");
								 //��õ�������id == ���ҵ�ID
								 if(idindex.equals(indexOld)){
									 //��������,����Ӳ��ɹ�
									flag = false;
									break;
								 }else{
									 //�����������Ϣ
									 flag = true;
								 }
							 }
						 }else{
							 flag = true;
						 }
						 Element field = list.get(i).element("rule").addElement("field");
						 field.addAttribute("class", "OFPOPRule");
						 field.addAttribute("id", indexOld);
						 //������
						 Element name = field.addElement("name");
						 name.setText(ob.getString("s_fieldName"));
						 //mac У��
						 Element need_mac = field.addElement("need_mac");
						 need_mac.setText(ob.getString("s_needMac"));
						 //packԪ��
						 Element pack = field.addElement("pack");
						 Element interfield = pack.addElement("interfield");
						 Element id = interfield.addElement("id");
						 id.setText(ob.getString("s_innerField"));
						 //����
						 Element value_len = pack.addElement("value_len");
						 value_len.setText(ob.getString("s_valueLen"));
						 //Ĭ��ֵ
						 Element default_value = pack.addElement("default_value");
						 default_value.setText(ob.getString("s_defaultValue"));
						 
				  }
			  }
			  if(flag){
				  saveXMLFile(document);
			  }
		  }catch(Exception e){
			  e.printStackTrace();
			  flag = false;
		  }
	  }
	  return flag;
	  
  }
  /**
   * ������Ϣ����ID�����ӽ��ĳ����
   * @author zymei
   * @param msgid �����ڵ���Ϣ����id
   * */
  public boolean addUnpackFieldInfo(String msgid,ObInterface ob){
	  boolean flag = false;
	  if(document != null){
		  try{
			  //�����ӵ�����ID
			  String indexOld=ob.getString("unfieldIndex");  
			  List<Element> list = document.getRootElement().elements("op_rule");
			  for(int i=0;i<list.size();i++){
				  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
				  //�ҵ���ͬ��msgid
				  if(messageId.equals(msgid)){
					  	List<Element> fieldList = list.get(i).element("rule").elements("field");
						 String idindex ="";
						 //�ж�����д���������Ƿ���֮ǰ����������ͻ
						 //��ֹ��һ�����ʱ��falg=false�Ĵ���
						 if(fieldList.size()>0){
							 for(int j=0;j<fieldList.size();j++){
							 	 idindex = fieldList.get(j).attributeValue("id");
								 //��õ�������id == ���ҵ�ID
								 if(idindex.equals(indexOld)){
									 //��������,����Ӳ��ɹ�
									flag = false;
									break;
								 }else{
									 //�����������Ϣ
									 flag = true;
								 }
							 }
						 }else{
							 flag = true;
						 }
						 Element field = list.get(i).element("rule").addElement("field");
						 field.addAttribute("class", "OFPOPRule");
						 field.addAttribute("id", indexOld);
						 //������
						 Element name = field.addElement("name");
						 name.setText(ob.getString("unfieldName"));
						 //mac У��
						 Element need_mac = field.addElement("need_mac");
						 need_mac.setText(ob.getString("unneedMac"));
						 //packԪ��
						 Element pack = field.addElement("unpack");
						 
						 String[] inner =ob.getString("uninnerField").split(",");
						 for(int k=0;k<inner.length;k++){
							 System.out.println(inner[k]);
							 Element interfield = pack.addElement("interfield");
							 Element id = interfield.addElement("id");
							 id.setText(inner[k]);
						 }
						 
						
						 
				  }
			  }
			  if(flag){
				  saveXMLFile(document);
			  }
		  }catch(Exception e){
			  e.printStackTrace();
			  flag = false;
		  }
	  }
	  return flag;
	  
  }
  /**
   * ���Ӵ����Ϣ����
   * @author zymei
   * @param msgid �����ڵ���Ϣ����id
   * */
  public boolean addMsgInfo(String msgid,String msgname){
	  boolean flag = false;
	  if(document != null){
		  try{
			  //�����ӵ�����ID
			 List<Element> list = document.getRootElement().elements("op_rule");
			 //��ֹ��һ�����ʱ��falg=false�Ĵ���
			 if(list.size()>0){
				 for(int i=0;i<list.size();i++){
					  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
					  //�ҵ���ͬ��msgid
					  if(messageId.equals(msgid)){
						 //��������,����Ӳ��ɹ�
						flag = false;
						break;
					 }else{
						 //�����������Ϣ
						 flag = true;
					 }
				 }
			 }else{
				 flag = true;
			 }
			  Element op =  document.getRootElement().addElement("op_rule");
			  Element name =  op.addElement("name");
			  name.setText(msgname);
			  Element msg_type =  op.addElement("msg_type");
			  Element msg_id =  msg_type.addElement("msg_id");
			  msg_id.setText(msgid);
			  Element rule =  op.addElement("rule");
	 		 
			  if(flag){
				  saveXMLFile(document);
			  }
		  }catch(Exception e){
			  e.printStackTrace();
			  flag = false;
		  }
	  }
	  return flag;
	  
  }
  /**
   * ���ӽ����Ϣ����
   * @author zymei
   * @param msgid �����ڵ���Ϣ����id
   * */
  public boolean addUnpackMsgInfo(String msgid,String msgname,ObInterface ob){
	  boolean flag = false;
	  if(document != null){
		  try{
			  //�����ӵ�����ID
			 List<Element> list = document.getRootElement().elements("op_rule");
			 //��ֹ��һ�����ʱ,falg=false�Ĵ���
			 if(list.size()>0){
				 for(int i=0;i<list.size();i++){
					  String messageId = list.get(i).element("msg_type").element("msg_id").getText();
					  //�ҵ���ͬ��msgid
					  if(messageId.equals(msgid)){
						 //��������,����Ӳ��ɹ�
						flag = false;
						break;
					 }else{
						 //�����������Ϣ
						 flag = true;
					 }
				 }
			 }else{
				 flag = true;
			 }
			  Element op =  document.getRootElement().addElement("op_rule");
			  Element name =  op.addElement("name");
			  name.setText(msgname);
			  Element msg_type =  op.addElement("msg_type");
			  Element msg_id =  msg_type.addElement("msg_id");
			  msg_id.setText(msgid);
			  
			//�Ƿ���ҪfieldId1 
			
		    			boolean u = false;
		    			if(op.element("msg_type").element("field") != null){
		    				u = op.element("msg_type").remove(op.element("msg_type").element("field"));
		    			}else {
		    				u = true;
		    			}
		    			if(u){
		    				if(ob.getString("fieldId1").trim().length()>0){
		    					Element field1 = op.element("msg_type").addElement("field");
		    					field1.addAttribute("id",ob.getString("fieldId1"));
		    					field1.addAttribute("type",ob.getString("fieldType1"));
		    					field1.addAttribute("value",ob.getString("fieldValue1"));
		    				}
		    				if(ob.getString("fieldId2").trim().length()>0){
		    					Element field1 = op.element("msg_type").addElement("field");
		    					field1.addAttribute("id",ob.getString("fieldId2"));
		    					field1.addAttribute("type",ob.getString("fieldType2"));
		    					field1.addAttribute("value",ob.getString("fieldValue2"));
		    				}
		    			}
			
			  
			  
			  
			  Element rule =  op.addElement("rule");
	 		 
			  if(flag){
				  saveXMLFile(document);
			  }
		  }catch(Exception e){
			  e.printStackTrace();
			  flag = false;
		  }
	  }
	  return flag;
	  
  }
  /**
   * @param path ·��
   * @author zymei
   * */
  public List<ObInterface> getAllFileName(String path,String channelNo){
	   ArrayList<ObInterface> fileList =new ArrayList<ObInterface>();
		   try{
			   if(channelNo.equals("state")){
				   File file1 = new File(path);
		              //�г����е��ļ���Ŀ¼
		           File[] files1 = file1.listFiles();
		           if(files1 != null){
		              for(int i = 0; i < files1.length; i++){
		                  //���г�Ŀ¼
		                  if(!files1[i].isDirectory()){//�Ƿ�ΪĿ¼
		                      //ȡ��·����
		                      //�ļ��ȴ���fileList��������ȡ��
		                	String str = files1[i].toString();
		                	String sep = File.separator;
		                	int index = str.lastIndexOf(sep);
		                    String temp =  str.substring(index+1, str.length());
		                    ObInterface ob = new ObInterface();
		                    try {
								ob.setFieldByName("fName", temp);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		                    if(!temp.equals("state.xml")){
	                    		fileList.add(ob);
		                    }
		                  }
		              }	
		           }
			   }else if(channelNo.length()>0 && path.length()>0){
				  File file = new File(path);
	              //�г����е��ļ���Ŀ¼
	              File[] files = file.listFiles();
	              if(files != null){
		              for(int i = 0; i < files.length; i++){
		                  //���г�Ŀ¼
		                  if(files[i].isDirectory()){//�Ƿ�ΪĿ¼
		                      //ȡ��·����
		                  }else{
		                      //�ļ��ȴ���fileList��������ȡ��
		                	String str = files[i].toString();
		                	String sep = File.separator;
		                	int index = str.lastIndexOf(sep);
		                    String temp =  str.substring(index+1, str.length());
		                    ObInterface ob = new ObInterface();
		                    try {
								ob.setFieldByName("fName", temp);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		                    if(temp.length()>2){
		                    	String s = temp.substring(0,2);
		                    	if(s.equals(channelNo)){
			                    		fileList.add(ob);
		                    	}
		                    }
		                  }
		              }
	              }
			   }
	      }catch(ArrayIndexOutOfBoundsException e){
	          System.out.println("using : java FileDemo pathname");    
	      }
	      
	      Collections.sort(fileList, new Comparator<ObInterface>() {
				public int compare(ObInterface o1, ObInterface o2) {
					// TODO Auto-generated method stub
					return  (o1.getString("fName")).compareTo((o2.getString("fName")));
				}
			 });
	      
	  return fileList;
  }
  /**
   * ����״̬�����б�
   * @author zymei
   * */
  public List<ObInterface> getStateFunList(){
	
	  List<ObInterface> obList = new ArrayList<ObInterface>();
	  if(document != null){
		  if(document.getRootElement().element("tMsgState")!=null){
			  List<Element> list = document.getRootElement().element("tMsgState").elements("tMsgState_field");
			  for(int i=0;i<list.size();i++){
				 
				  String m_iChannel = list.get(i).element("m_iChannel").getText();
				  String m_iMsgId = list.get(i).element("m_iMsgId").getText();
				  String m_iStateNum = list.get(i).element("m_iStateNum").getText();
				  String m_iStateType = list.get(i).element("m_iStateType").getText();
				  String m_iParaMeter0 ="";
				  String m_iParaMeter1 ="";
				  String m_iParaMeter2 ="";
				  String m_iParaMeter3 ="";
				  String m_iParaMeter4 ="";
				  String m_acParaMeter5="";
				  String m_acParaMeter6="";
				  String m_acParaMeter7="";
				  String m_acParaMeter8="";
				  String m_acParaMeter9="";
				  if(list.get(i).element("m_iParaMeter0")!= null){
					  m_iParaMeter0 = list.get(i).element("m_iParaMeter0").getText();
				  }
				  if(list.get(i).element("m_iParaMeter1")!= null){
					  m_iParaMeter1 = list.get(i).element("m_iParaMeter1").getText();
				  }
				  if(list.get(i).element("m_iParaMeter2")!= null){
					  m_iParaMeter2 = list.get(i).element("m_iParaMeter2").getText();
				  }
				  if(list.get(i).element("m_iParaMeter3")!= null){
					  m_iParaMeter3 = list.get(i).element("m_iParaMeter3").getText();
				  }
				  if(list.get(i).element("m_iParaMeter4")!= null){
					  m_iParaMeter4 = list.get(i).element("m_iParaMeter4").getText();
				  }
				  if(list.get(i).element("m_acParaMeter5")!= null){
					  m_acParaMeter5 = list.get(i).element("m_acParaMeter5").getText();
				  }
				  if(list.get(i).element("m_acParaMeter6")!= null){
					  m_acParaMeter6 = list.get(i).element("m_acParaMeter6").getText();
				  }
				  if(list.get(i).element("m_acParaMeter7")!= null){
					  m_acParaMeter7 = list.get(i).element("m_acParaMeter7").getText();
				  }
				  if(list.get(i).element("m_acParaMeter8")!= null){
					  m_acParaMeter8 = list.get(i).element("m_acParaMeter8").getText();
				  }
				  if(list.get(i).element("m_acParaMeter9")!= null){
					  m_acParaMeter9 = list.get(i).element("m_acParaMeter9").getText();
				  }
				
				  
				  ObInterface ob= new ObInterface();
				  try {
					ob.setFieldByName("fm_iChannel", m_iChannel);
					ob.setFieldByName("fm_iMsgId", m_iMsgId);
					ob.setFieldByName("fm_iStateNum", m_iStateNum);
					ob.setFieldByName("fm_iStateType", m_iStateType);
					ob.setFieldByName("fm_iParaMeter0", m_iParaMeter0);
					ob.setFieldByName("fm_iParaMeter1", m_iParaMeter1);
					ob.setFieldByName("fm_iParaMeter2", m_iParaMeter2);
					ob.setFieldByName("fm_iParaMeter3", m_iParaMeter3);
					ob.setFieldByName("fm_iParaMeter4", m_iParaMeter4);
					ob.setFieldByName("fm_acParaMeter5", m_acParaMeter5);
					ob.setFieldByName("fm_acParaMeter6", m_acParaMeter6);
					ob.setFieldByName("fm_acParaMeter7", m_acParaMeter7);
					ob.setFieldByName("fm_acParaMeter8", m_acParaMeter8);
					ob.setFieldByName("fm_acParaMeter9", m_acParaMeter9);
				  } catch (Exception e) {
					// TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  obList.add(ob);
			  }
		  }
	  }
	  return obList;
  }
  /**
   * Ϊ״̬������д����
   * Comparator �Ƚ���
   * Collections ��̬������sort ����
   * */
  public List<ObInterface> stateFunSort(List<ObInterface> list){
	  Collections.sort(list, new Comparator<ObInterface>() {
			public int compare(ObInterface o1, ObInterface o2) {
				// TODO Auto-generated method stub
				return  (o1.getString("fm_iMsgId")+o1.getString("fm_iStateNum")).compareTo((o2.getString("fm_iMsgId")+o2.getString("fm_iStateNum")));
			}
		 });
	  return list;
  }
  /**
   * 
   * */
  public boolean rewriteStateFile(List<ObInterface> list){
	  boolean flag = false;
	  if(document != null){
		  Element root = document.getRootElement();
		  flag = root.remove(root.element("tMsgState"));
		  if(flag){
			 Element tMsgState = root.addElement("tMsgState");
			 for(int i=0;i<list.size();i++){
				 Element tMsgState_field = tMsgState.addElement("tMsgState_field");
				 tMsgState_field.addAttribute("table", "tMsgState");
				 tMsgState_field.addAttribute("ChannleId", list.get(i).getString("fm_iChannel"));

				 Element m_iChannel = tMsgState_field.addElement("m_iChannel");
				 m_iChannel.setText(list.get(i).getString("fm_iChannel"));
				 Element m_iMsgId = tMsgState_field.addElement("m_iMsgId");
				 m_iMsgId.setText(list.get(i).getString("fm_iMsgId"));
				 Element m_iStateNum = tMsgState_field.addElement("m_iStateNum");
				 m_iStateNum.setText(list.get(i).getString("fm_iStateNum"));
				 Element m_iStateType = tMsgState_field.addElement("m_iStateType");
				 m_iStateType.setText(list.get(i).getString("fm_iStateType"));
				 if(list.get(i).getString("fm_iParaMeter0").length()>0){
					 Element m_iParaMeter0 = tMsgState_field.addElement("m_iParaMeter0");
					 m_iParaMeter0.setText(list.get(i).getString("fm_iParaMeter0"));
				 }
				 if(list.get(i).getString("fm_iParaMeter1").length()>0){
					 Element m_iParaMeter1 = tMsgState_field.addElement("m_iParaMeter1");
					 m_iParaMeter1.setText(list.get(i).getString("fm_iParaMeter1"));
				 }
				 if(list.get(i).getString("fm_iParaMeter2").length()>0){
					 Element m_iParaMeter2 = tMsgState_field.addElement("m_iParaMeter2");
					 m_iParaMeter2.setText(list.get(i).getString("fm_iParaMeter2"));
				 }
				 if(list.get(i).getString("fm_iParaMeter3").length()>0){
					 Element m_iParaMeter3 = tMsgState_field.addElement("m_iParaMeter3");
					 m_iParaMeter3.setText(list.get(i).getString("fm_iParaMeter3"));
				 }
				 if(list.get(i).getString("fm_iParaMeter4").length()>0){
					 Element m_iParaMeter4 = tMsgState_field.addElement("m_iParaMeter4");
					 m_iParaMeter4.setText(list.get(i).getString("fm_iParaMeter4"));
				 }
				 if(list.get(i).getString("fm_acParaMeter5").length()>0){
					 Element m_acParaMeter5 = tMsgState_field.addElement("m_acParaMeter5");
					 m_acParaMeter5.setText(list.get(i).getString("fm_acParaMeter5"));
				 }
				 if(list.get(i).getString("fm_acParaMeter6").length()>0){
					 Element m_acParaMeter6 = tMsgState_field.addElement("m_acParaMeter6");
					 m_acParaMeter6.setText(list.get(i).getString("fm_acParaMeter6"));
				 }
				 if(list.get(i).getString("fm_acParaMeter7").length()>0){
					 Element m_acParaMeter7 = tMsgState_field.addElement("m_acParaMeter7");
					 m_acParaMeter7.setText(list.get(i).getString("fm_acParaMeter7"));
				 }
				 if(list.get(i).getString("fm_acParaMeter8").length()>0){
					 Element m_acParaMeter8 = tMsgState_field.addElement("m_acParaMeter8");
					 m_acParaMeter8.setText(list.get(i).getString("fm_acParaMeter8"));
				 }
				 if(list.get(i).getString("fm_acParaMeter9").length()>0){
					 Element m_acParaMeter9 = tMsgState_field.addElement("m_acParaMeter9");
					 m_acParaMeter9.setText(list.get(i).getString("fm_acParaMeter9"));
				 }
			 }
			 
			 if(flag){
				  saveXMLFile(document);
			  }
		  }
	  }	
	  return flag;
  }
  /**
   * ������״����е�ĳһ�н��ף�������Ϣ����ID������Ϣ�������֣�
   * ����������Ϣ����������
   * 
   * 
   * */
  public boolean tSelectPackMsg(String msgid, String currPath,String commPath){
	  boolean flag = false;
	  return flag;
  }
  /**
   * ���ӽ��״�������
   * */
  public boolean s_addTranPro(ObInterface ob){
	  boolean flag = false;
	  if(document != null){
		  try{
			  Element root = document.getRootElement();
			  Element tMsg = root.element("tMsg");
			  Element tMsg_field = tMsg.addElement("tMsg_field");
			  tMsg_field.attributeValue("table", "tMsg");
			  tMsg_field.attributeValue("Channel", ob.getString("channelid"));
			  
			  Element m_acMsgType = tMsg_field.addElement("m_acMsgType");
			  m_acMsgType.setText(ob.getString("s_acMsgType"));
			
			  Element m_acMsgCode = tMsg_field.addElement("m_acMsgCode");
			  m_acMsgCode.setText(ob.getString("s_acMsgCode"));
			
			  Element m_iTxnId = tMsg_field.addElement("m_iTxnId");
			  m_iTxnId.setText(ob.getString("s_iTxnId"));
			 
			  Element m_iChannelType = tMsg_field.addElement("m_iChannelType");
			  m_iChannelType.setText(ob.getString("st_iChannel"));
			 
			  Element m_iReqResp = tMsg_field.addElement("m_iReqResp");
			  m_iReqResp.setText(ob.getString("s_iReqResp"));
			 
			  Element m_iStartEnd = tMsg_field.addElement("m_iStartEnd");
			  m_iStartEnd.setText(ob.getString("s_iStartEnd"));
			  
			  Element m_iMsgId = tMsg_field.addElement("m_iMsgId");
			  m_iMsgId.setText(ob.getString("s_iMsgId"));
			  
			  Element m_iInterMsgId = tMsg_field.addElement("m_iInterMsgId");
			  m_iInterMsgId.setText(ob.getString("s_iInterMsgId"));
			  
			  Element m_iPackFlag = tMsg_field.addElement("m_iPackFlag");
			  m_iPackFlag.setText(ob.getString("s_iPackFlag"));
			  
			  Element m_iComment = tMsg_field.addElement("m_iComment");
			  m_iComment.setText(ob.getString("s_iComment"));
			 
			  if(ob.getString("m_acAdd1").length()>0){
				  Element m_acAdd1 = tMsg_field.addElement("m_acAdd1");
				  m_acAdd1.setText(ob.getString("m_acAdd1"));
			  }
			  if(ob.getString("m_acAdd2").length()>0){
				  Element m_acAdd2 = tMsg_field.addElement("m_acAdd2");
				  m_acAdd2.setText(ob.getString("m_acAdd2"));
			  }
			  flag = saveXMLFile(document);
			  
		  }catch(Exception e){
			  flag = false;
		  }
	  }
	  return flag;
  }
  /**
   * �޸Ľ��״�������
   * */
  public boolean s_updateTranPro(ObInterface ob){
	  boolean flag = false;
	  if(document != null){
		  try{
			  List<Element> list = new ArrayList<Element>();
			  Element root = document.getRootElement();
			  Element tMsg = root.element("tMsg");
			  list = tMsg.elements("tMsg_field");
			  for(int i=0;i<list.size();i++){
				  String type = list.get(i).element("m_acMsgType").getText();
				  String code = list.get(i).element("m_acMsgCode").getText();
				  String req = list.get(i).element("m_iReqResp").getText();
				  String pack = list.get(i).element("m_iPackFlag").getText();
				 
				//  if((type.equals(ob.getString("s_acMsgType"))) && (code.equals(ob.getString("s_acMsgCode"))) && (req.equals(ob.getString("s_iReqResp"))) && (pack.equals(ob.getString("s_iPackFlag")))){
				  if((type.equals(ob.getString("s_acMsgType"))) && (code.equals(ob.getString("s_acMsgCode")))){
					  
					  list.get(i).element("m_iTxnId").setText(ob.getString("s_iTxnId"));
					 // list.get(i).element("m_iChannelType").setText(ob.getString("st_iChannel"));
					  list.get(i).element("m_iStartEnd").setText(ob.getString("s_iStartEnd"));
					  list.get(i).element("m_iMsgId").setText(ob.getString("s_iMsgId"));
					  
					  list.get(i).element("m_iReqResp").setText(ob.getString("s_iReqResp"));
					  list.get(i).element("m_iPackFlag").setText(ob.getString("s_iPackFlag"));
					  
					  list.get(i).element("m_iInterMsgId").setText(ob.getString("s_iInterMsgId"));
					  list.get(i).element("m_iComment").setText(ob.getString("s_iComment"));
					 
					  
					  if(list.get(i).element("m_acAdd1")!= null){
				 			if(ob.getString("m_acAdd1").trim().length()>0){
				 				list.get(i).element("m_acAdd1").setText(ob.getString("m_acAdd1"));
				 			}else{
				 				list.get(i).remove(list.get(i).element("m_acAdd1"));
				 			}
				 			
				 		}else{
				 				if(ob.getString("m_acAdd1").trim().length()>0){
					    				Element needMac = list.get(i).addElement("m_acAdd1");
					    				needMac.setText(ob.getString("m_acAdd1"));
					    			}
				 		}
					  
					  
					  if(list.get(i).element("m_acAdd2")!= null){
				 			if(ob.getString("m_acAdd2").trim().length()>0){
				 				list.get(i).element("m_acAdd2").setText(ob.getString("m_acAdd2"));
				 			}else{
				 				list.get(i).remove(list.get(i).element("m_acAdd2"));
				 			}
				 			
				 		}else{
				 				if(ob.getString("m_acAdd2").trim().length()>0){
					    				Element needMac = list.get(i).addElement("m_acAdd2");
					    				needMac.setText(ob.getString("m_acAdd2"));
					    			}
				 		}
					  
				  }
			  }
			  flag = saveXMLFile(document);
		  }catch(Exception e){
			  flag = false;
		  }
	  }
	  return flag;
  }
  /**
   * ɾ�����״�������
   * */
  public boolean s_deleteTranPro(ObInterface ob){
	  boolean flag = false;
	  if(document != null){
		  try{
			  List<Element> list = new ArrayList<Element>();
			  Element root = document.getRootElement();
			  Element tMsg = root.element("tMsg");
			  list = tMsg.elements("tMsg_field");
			  for(int i=0;i<list.size();i++){
				  String type = list.get(i).element("m_acMsgType").getText();
				  String code = list.get(i).element("m_acMsgCode").getText();
				  String req = list.get(i).element("m_iReqResp").getText();
				  String pack = list.get(i).element("m_iPackFlag").getText();
				  if((type.equals(ob.getString("s_acMsgType"))) && (code.equals(ob.getString("s_acMsgCode"))) && (req.equals(ob.getString("s_iReqResp"))) && (pack.equals(ob.getString("s_iPackFlag")))){
					 tMsg.remove(list.get(i));
				  }
			  }
			  flag = saveXMLFile(document);
		  }catch(Exception e){
			  flag = false;
		  } 
	  }
	  return flag;
  }
  
  
  /**
   * ����״̬��������
   * */
  public boolean s_addState(ObInterface ob){
	  boolean flag = false;
	  if(document != null){
		  try{
			  Element root = document.getRootElement();
			  Element tMsgState = root.element("tMsgState");
			  Element tMsgState_field = tMsgState.addElement("tMsgState_field");
			  tMsgState_field.attributeValue("table", "tMsgState");
			  tMsgState_field.attributeValue("Channel", ob.getString("channelid"));
			  
			  Element m_iChannel = tMsgState_field.addElement("m_iChannel");
			  m_iChannel.setText(ob.getString("channelid"));
			  
			  if(ob.getString("st_iMsgId").trim().length()>0){
				  Element m_iMsgId = tMsgState_field.addElement("m_iMsgId");
				  m_iMsgId.setText(ob.getString("st_iMsgId"));
			  }
			  if(ob.getString("st_iStateNum").trim().length()>0){
				  Element m_iStateNum = tMsgState_field.addElement("m_iStateNum");
				  m_iStateNum.setText(ob.getString("st_iStateNum"));
			  }
			  if(ob.getString("st_iStateType").trim().length()>0){
				  Element m_iMsgId = tMsgState_field.addElement("m_iStateType");
				  m_iMsgId.setText(ob.getString("st_iStateType"));
			  }
			  if(ob.getString("s_iParaMeter0").trim().length()>0){
				  Element m_iMsgId = tMsgState_field.addElement("m_iParaMeter0");
				  m_iMsgId.setText(ob.getString("s_iParaMeter0"));
			  }
			  if(ob.getString("s_iParaMeter1").trim().length()>0){
				  Element m_iMsgId = tMsgState_field.addElement("m_iParaMeter1");
				  m_iMsgId.setText(ob.getString("s_iParaMeter1"));
			  }
			  
			  if(ob.getString("s_iParaMeter2").trim().length()>0){
				  Element m_iMsgId = tMsgState_field.addElement("m_iParaMeter2");
				  m_iMsgId.setText(ob.getString("s_iParaMeter2"));
			  }
			  if(ob.getString("s_iParaMeter3").trim().length()>0){
				  Element m_iMsgId = tMsgState_field.addElement("m_iParaMeter3");
				  m_iMsgId.setText(ob.getString("s_iParaMeter3"));
			  }
			  if(ob.getString("s_iParaMeter4").trim().length()>0){
				  Element m_iMsgId = tMsgState_field.addElement("m_iParaMeter4");
				  m_iMsgId.setText(ob.getString("s_iParaMeter4"));
			  }
			  if(ob.getString("s_acParaMeter5").trim().length()>0){
				  Element m_iMsgId = tMsgState_field.addElement("m_acParaMeter5");
				  m_iMsgId.setText(ob.getString("s_acParaMeter5"));
			  }
			  if(ob.getString("s_acParaMeter6").trim().length()>0){
				  Element m_iMsgId = tMsgState_field.addElement("m_acParaMeter6");
				  m_iMsgId.setText(ob.getString("s_acParaMeter6"));
			  }
			  if(ob.getString("s_acParaMeter7").trim().length()>0){
				  Element m_iMsgId = tMsgState_field.addElement("m_acParaMeter7");
				  m_iMsgId.setText(ob.getString("s_acParaMeter7"));
			  }
			  if(ob.getString("s_acParaMeter8").trim().length()>0){
				  Element m_iMsgId = tMsgState_field.addElement("m_acParaMeter8");
				  m_iMsgId.setText(ob.getString("s_acParaMeter8"));
			  }
			  if(ob.getString("s_acParaMeter9").trim().length()>0){
				  Element m_iMsgId = tMsgState_field.addElement("m_acParaMeter9");
				  m_iMsgId.setText(ob.getString("s_acParaMeter9"));
			  }
			 
			  flag = saveXMLFile(document);
			  
		  }catch(Exception e){
			  flag = false;
		  }  
	  }
	  return flag;
  }
  
  /**
   * �޸�״̬��������
   * */
  public boolean s_modifyState(ObInterface ob){
	  boolean flag = false;
	  if(document != null){
		  try{
			  Element root = document.getRootElement();
			  Element tMsgState = root.element("tMsgState");
			  
			  List<Element> list = new ArrayList<Element>();
			  list = tMsgState.elements("tMsgState_field");
			  
			  for(int i=0;i<list.size();i++){
				  String msgId = list.get(i).element("m_iMsgId").getText();
				  String stateNum = list.get(i).element("m_iStateNum").getText();
				  if((msgId.trim().equals(ob.getString("st_iMsgId").trim())) && (stateNum.trim().equals(ob.getString("st_iStateNum").trim())) ){
					 flag = tMsgState.remove(list.get(i));
					 if(flag){
						 Element tMsgState_field = tMsgState.addElement("tMsgState_field");
						  tMsgState_field.attributeValue("table", "tMsgState");
						  tMsgState_field.attributeValue("Channel", ob.getString("channelid"));
						  
						  Element m_iChannel = tMsgState_field.addElement("m_iChannel");
						  m_iChannel.setText(ob.getString("channelid"));
						  
						  if(ob.getString("st_iMsgId").trim().length()>0){
							  Element m_iMsgId = tMsgState_field.addElement("m_iMsgId");
							  m_iMsgId.setText(ob.getString("st_iMsgId"));
						  }
						  if(ob.getString("st_iStateNum").trim().length()>0){
							  Element m_iStateNum = tMsgState_field.addElement("m_iStateNum");
							  m_iStateNum.setText(ob.getString("st_iStateNum"));
						  }
						  if(ob.getString("st_iStateType").trim().length()>0){
							  Element m_iMsgId = tMsgState_field.addElement("m_iStateType");
							  m_iMsgId.setText(ob.getString("st_iStateType"));
						  }
						  if(ob.getString("s_iParaMeter0").trim().length()>0){
							  Element m_iMsgId = tMsgState_field.addElement("m_iParaMeter0");
							  m_iMsgId.setText(ob.getString("s_iParaMeter0"));
						  }
						  if(ob.getString("s_iParaMeter1").trim().length()>0){
							  Element m_iMsgId = tMsgState_field.addElement("m_iParaMeter1");
							  m_iMsgId.setText(ob.getString("s_iParaMeter1"));
						  }
						  
						  if(ob.getString("s_iParaMeter2").trim().length()>0){
							  Element m_iMsgId = tMsgState_field.addElement("m_iParaMeter2");
							  m_iMsgId.setText(ob.getString("s_iParaMeter2"));
						  }
						  if(ob.getString("s_iParaMeter3").trim().length()>0){
							  Element m_iMsgId = tMsgState_field.addElement("m_iParaMeter3");
							  m_iMsgId.setText(ob.getString("s_iParaMeter3"));
						  }
						  if(ob.getString("s_iParaMeter4").trim().length()>0){
							  Element m_iMsgId = tMsgState_field.addElement("m_iParaMeter4");
							  m_iMsgId.setText(ob.getString("s_iParaMeter4"));
						  }
						  if(ob.getString("s_acParaMeter5").trim().length()>0){
							  Element m_iMsgId = tMsgState_field.addElement("m_acParaMeter5");
							  m_iMsgId.setText(ob.getString("s_acParaMeter5"));
						  }
						  if(ob.getString("s_acParaMeter6").trim().length()>0){
							  Element m_iMsgId = tMsgState_field.addElement("m_acParaMeter6");
							  m_iMsgId.setText(ob.getString("s_acParaMeter6"));
						  }
						  if(ob.getString("s_acParaMeter7").trim().length()>0){
							  Element m_iMsgId = tMsgState_field.addElement("m_acParaMeter7");
							  m_iMsgId.setText(ob.getString("s_acParaMeter7"));
						  }
						  if(ob.getString("s_acParaMeter8").trim().length()>0){
							  Element m_iMsgId = tMsgState_field.addElement("m_acParaMeter8");
							  m_iMsgId.setText(ob.getString("s_acParaMeter8"));
						  }
						  if(ob.getString("s_acParaMeter9").trim().length()>0){
							  Element m_iMsgId = tMsgState_field.addElement("m_acParaMeter9");
							  m_iMsgId.setText(ob.getString("s_acParaMeter9"));
						  }
					 }
				  }
			  }
			  flag = saveXMLFile(document);
			  
		  }catch(Exception e){
			  flag = false;
		  }  
	  }
	  return flag;
  }
  
  /**
   * �޸�״̬��������
   * */
  public boolean s_deleteState(ObInterface ob){
	  boolean flag = false;
	  if(document != null){
		  try{
			  Element root = document.getRootElement();
			  Element tMsgState = root.element("tMsgState");
			  List<Element> list = new ArrayList<Element>();
			  list = tMsgState.elements("tMsgState_field");
			  
			  for(int i=0;i<list.size();i++){
				  String msgId = list.get(i).element("m_iMsgId").getText();
				  String stateNum = list.get(i).element("m_iStateNum").getText();
				  if((msgId.trim().equals(ob.getString("st_iMsgId").trim())) && (stateNum.trim().equals(ob.getString("st_iStateNum").trim())) ){
					 flag = tMsgState.remove(list.get(i));
				  }
			  }
			  saveXMLFile(document);
		  }catch(Exception e){
			  flag = false;
		  } 
	  }
	  return flag;
  }
  /**
   * @param msgid,stateNum ״̬������ţ�״̬����ID��
   * @author zymei
   * ��ѯ״̬��������Ϣ
   * */
  public ObInterface selectState(String msgId,String stateNum){
	  ObInterface ob = new ObInterface();
	  if(document != null){
		  try{
			  Element root = document.getRootElement();
			  Element tMsgState = root.element("tMsgState");
			  List<Element> list = new ArrayList<Element>();
			  list = tMsgState.elements("tMsgState_field");
			  
			  for(int i=0;i<list.size();i++){
				  String msgId1 = list.get(i).element("m_iMsgId").getText();
				  String stateNum1 = list.get(i).element("m_iStateNum").getText();
				  if((msgId.trim().equals(msgId1.trim())) && (stateNum.trim().equals(stateNum1.trim()))){
					 ob.setFieldByName("channelid",list.get(i).element("m_iChannel").getText());
					 ob.setFieldByName("st_iMsgId",list.get(i).element("m_iMsgId").getText());
					 ob.setFieldByName("st_iStateNum",list.get(i).element("m_iStateNum").getText());
					 ob.setFieldByName("st_iStateType",list.get(i).element("m_iStateType").getText());
					 
					 if(list.get(i).element("m_iParaMeter0") != null){
						 ob.setFieldByName("s_iParaMeter0",list.get(i).element("m_iParaMeter0").getText());
					 }
					 if(list.get(i).element("m_iParaMeter1") != null){
						 ob.setFieldByName("s_iParaMeter1",list.get(i).element("m_iParaMeter1").getText());
					 }
					 if(list.get(i).element("m_iParaMeter2") != null){
						 ob.setFieldByName("s_iParaMeter2",list.get(i).element("m_iParaMeter2").getText());
					 }
					 if(list.get(i).element("m_iParaMeter3") != null){ 
						 ob.setFieldByName("s_iParaMeter3",list.get(i).element("m_iParaMeter3").getText());
					 }
					 if(list.get(i).element("m_iParaMeter4") != null){
						 ob.setFieldByName("s_iParaMeter4",list.get(i).element("m_iParaMeter4").getText());
					 }
					 if(list.get(i).element("m_acParaMeter5") != null){
						 ob.setFieldByName("s_acParaMeter5",list.get(i).element("m_acParaMeter5").getText());
					 }
					 if(list.get(i).element("m_acParaMeter6") != null){
						 ob.setFieldByName("s_acParaMeter6",list.get(i).element("m_acParaMeter6").getText());
					 }
					 if(list.get(i).element("m_acParaMeter7") != null){
						 ob.setFieldByName("s_acParaMeter7",list.get(i).element("m_acParaMeter7").getText());
					 }
					 if(list.get(i).element("m_acParaMeter8") != null){
						 ob.setFieldByName("s_acParaMeter8",list.get(i).element("m_acParaMeter8").getText());
					 }
					 if(list.get(i).element("m_acParaMeter9") != null){
						 ob.setFieldByName("s_acParaMeter9",list.get(i).element("m_acParaMeter9").getText());
					 }
				  }
			  }
			  saveXMLFile(document);
		  }catch(Exception e){
			  e.printStackTrace();
		  } 
	  }
	  return ob;
  }
  /**
   * @param path,id ״̬������ţ�״̬����ID��
   * @author zymei
   * �������·�����Ƿ���������ID
   * */
  public boolean checkId(String path,String id){
	  boolean flag = true;
	  List<Element> list = new ArrayList<Element>();
	  if(document != null){
		  list = document.selectNodes(path);
		  for(int i=0;i<list.size();i++){
			  if(list.get(i).getText().equals(id)){
				  flag = false;
				  break;
			  }
		  }
	  }
	  return flag;
  }

  /**
   *  ����·��ɾ��ָ����Ŀ¼���ļ������۴������
   *@param sPath  Ҫɾ����Ŀ¼���ļ�
   *@return ɾ���ɹ����� true�����򷵻� false��
   */
  public boolean DeleteFolder(String sPath) {
    boolean  flag = false;
     File file = new File(sPath);
      // �ж�Ŀ¼���ļ��Ƿ����
      if (!file.exists()) {  // �����ڷ��� false
          return flag;
      } else {
          // �ж��Ƿ�Ϊ�ļ�
          if (file.isFile()) {  // Ϊ�ļ�ʱ����ɾ���ļ�����
              return deleteFile(sPath);
          } else {  // ΪĿ¼ʱ����ɾ��Ŀ¼����
              return deleteDirectory(sPath);
          }
      }
  }
  /**
   * ɾ�������ļ�
   * @param   sPath    ��ɾ���ļ����ļ���
   * @return �����ļ�ɾ���ɹ�����true�����򷵻�false
   */
  public boolean deleteFile(String sPath) {
     boolean flag = false;
     File file = new File(sPath);
      // ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��
      if (file.isFile() && file.exists()) {
          file.delete();
          flag = true;
      }
      return flag;
  }
  /**
   * ɾ��Ŀ¼���ļ��У��Լ�Ŀ¼�µ��ļ�
   * @param   sPath ��ɾ��Ŀ¼���ļ�·��
   * @return  Ŀ¼ɾ���ɹ�����true�����򷵻�false
   */
  public boolean deleteDirectory(String sPath) {
      //���sPath�����ļ��ָ�����β���Զ�����ļ��ָ���
      if (!sPath.endsWith(File.separator)) {
          sPath = sPath + File.separator;
      }
      File dirFile = new File(sPath);
      //���dir��Ӧ���ļ������ڣ����߲���һ��Ŀ¼�����˳�
      if (!dirFile.exists() || !dirFile.isDirectory()) {
          return false;
      }
     boolean flag = true;
      //ɾ���ļ����µ������ļ�(������Ŀ¼)
      File[] files = dirFile.listFiles();
      for (int i = 0; i < files.length; i++) {
          //ɾ�����ļ�
          if (files[i].isFile()) {
              flag = deleteFile(files[i].getAbsolutePath());
              if (!flag) break;
          } //ɾ����Ŀ¼
          else {
              flag = deleteDirectory(files[i].getAbsolutePath());
              if (!flag) break;
          }
      }
      if (!flag) return false;
      //ɾ����ǰĿ¼
      if (dirFile.delete()) {
          return true;
      } else {
          return false;
      }
  }
  public List<ObInterface> getPtlList(String path){
	  List<ObInterface> list = new ArrayList();
	  
	  return list;
  }
  public static void main(String args[]){
		  //����xml 
		  String filepath = "D:"+File.separator+"cfg"+File.separator+"33.conf";
		  DOM4JForXml DeleteFolder = new DOM4JForXml(filepath); 
		  DeleteFolder.deleteFile(filepath);
		  
  	}
} 



