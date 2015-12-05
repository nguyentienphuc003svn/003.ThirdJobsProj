package ccas;

import java.io.*;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;

public class XmlBean	{
	static private String xmlFile;
	static private final String myencode="GBK";
	private	long stime=System.currentTimeMillis();
	private	Document doc=null;

	public XmlBean()	{
	}

	public void setXmlFile(String xmlFile)	{
		this.xmlFile=xmlFile;
	}
	public String getXmlFile()	{
		return xmlFile;
	}

	public Document getDocument(String xmlContent, boolean b)		throws DocumentException	{
		//System.out.println("getDocument boolean");
		Document d = DocumentHelper.parseText(xmlContent);
		return d;
	}

	public String transformDOM(Document d)	{
		//System.out.println("transformDOM");
		String xmlContent = "";
		xmlContent = d.asXML();
		//System.out.println((new StringBuilder(String.valueOf(xmlContent))).append("xmlContent").toString());
		return xmlContent;
	}

	public Element getNode(Document d, String elePath, String eleValue)	{
		Element ele = null;
		List l = d.selectNodes(elePath);
		for	(Iterator iter = l.iterator(); iter.hasNext();)
		{
			Element tmp = (Element)iter.next();
			if	(tmp.getText().equals(eleValue))				ele = tmp;
		}

		return ele;
	}

	public Element addAttAndElem(Element parentEle, String eleName, String eleValue, String attname, String attvalue)	{
		Element newEle = null;
		newEle = parentEle.addElement(eleName);
		newEle.setText(eleValue);
		addAttribute(newEle, attname, attvalue);
		return newEle;
	}

	public void addAttribute(Element ele, String attributeName, String attributeValue)	{
		ele.addAttribute(attributeName, attributeValue);
	}

	public void removeNode(Element parentEle, String eleName, String eleValue)	{
		Iterator iter = parentEle.elementIterator();
		Element delEle = null;
		while(iter.hasNext())		{
			Element tmp = (Element)iter.next();
			if	(tmp.getName().equals(eleName) && tmp.getText().equals(eleValue))
				delEle = tmp;
		}
		if	(delEle != null)			parentEle.remove(delEle);
	}

	public void removeAttr(Element ele, String attributeName)	{
		Attribute att = ele.attribute(attributeName);
		ele.remove(att);
	}

	public void setNodeText(Element ele, String newValue)	{
		ele.setText(newValue);
	}

	public void setAttribute(Element ele, String attributeName, String attributeValue)	{
		Attribute att = ele.attribute(attributeName);
		att.setText(attributeValue);
	}

	public Document OpenXml(String xmlFile)		throws Exception	{
		SAXReader reader = new SAXReader();
		reader.setEncoding(myencode);
		Document document = reader.read(new FileInputStream(xmlFile));
		this.xmlFile=xmlFile;
		return document;
	}

	public boolean CheckListmore(String xmlFile, String xmlpath, String namelist, String textlist)		throws Exception	{
		int flagnum = 0;
		String name1,text1;
		StringTokenizer tokens1=new StringTokenizer(namelist,"|");
		StringTokenizer tokens2=new StringTokenizer(textlist,"|");
		int maxnum = tokens1.countTokens();
		for	(;tokens1.hasMoreTokens();)	{
			name1=tokens1.nextToken();
			if	(name1==null || name1.length()<=0)			maxnum--;
		}

		Document document = OpenXml(xmlFile);
		this.xmlFile=xmlFile;
		List list = document.selectNodes(xmlpath);
		//System.out.println((new StringBuilder("list size=")).append(list.size()).toString());
		for	(Iterator iter = list.iterator(); iter.hasNext();)		{
			flagnum = 0;
			Element myElement = (Element)iter.next();
			//System.out.println((new StringBuilder(" //count= ")).append(myElement.nodeCount()).toString());
			for	(int k = 0; k < myElement.nodeCount(); k++)			{
				Node node = myElement.node(k);
				if	(node instanceof Element)	{

					tokens1=new StringTokenizer(namelist,"|");
					tokens2=new StringTokenizer(textlist,"|");
					for	(;tokens1.hasMoreTokens();)	{
						name1=tokens1.nextToken();
						text1=tokens2.hasMoreTokens()?tokens2.nextToken():"";
						if	(name1!=null && node.getName().equals(name1) && node.getText().equals(text1))	flagnum++;
					}
					if	(flagnum >= maxnum)						return true;
					//System.out.println((new StringBuilder("node.getText() node instanceof=")).append(node.getText()).toString());
				}
			}

		}

		if	(flagnum >= maxnum)			return true;			else			return false;
	}


	public boolean CheckListmore(String xmlFile, String xmlpath, String name1, String text1, String name2, String text2)		throws Exception	{
		return CheckListmore(xmlFile,xmlpath,name1,text1,name2,text2,null,null,null,null);
	}
	public boolean CheckListmore(String xmlFile, String xmlpath, String name1, String text1, String name2, String text2, String name3, String text3)		throws Exception	{
		return CheckListmore(xmlFile,xmlpath,name1,text1,name2,text2,name3,text3,null,null);
	}
	public boolean CheckListmore(String xmlFile, String xmlpath, String name1, String text1, String name2, String text2, String name3, String text3, String name4, String text4)		throws Exception	{
		int flagnum = 0;
		int maxnum = 4;
		if	(name1==null || name1.length()<=0)			maxnum--;
		if	(name2==null || name2.length()<=0)			maxnum--;
		if	(name3==null || name3.length()<=0)			maxnum--;
		if	(name4==null || name4.length()<=0)			maxnum--;
		Document document = OpenXml(xmlFile);
		this.xmlFile=xmlFile;
		List list = document.selectNodes(xmlpath);
		//System.out.println((new StringBuilder("list size=")).append(list.size()).toString());
		for	(Iterator iter = list.iterator(); iter.hasNext();)		{
			flagnum = 0;
			Element myElement = (Element)iter.next();
			//System.out.println((new StringBuilder(" //count= ")).append(myElement.nodeCount()).toString());
			for	(int k = 0; k < myElement.nodeCount(); k++)			{
				Node node = myElement.node(k);
				if	(node instanceof Element)
				{
					if	(node.getName().equals(name1) && node.getText().equals(text1))
					{
						//System.out.println((new StringBuilder("name text equal text=")).append(node.getText()).toString());
						flagnum++;
					}
					if	(node.getName().equals(name2) && node.getText().equals(text2))
					{
						//System.out.println((new StringBuilder("name text equal text=")).append(node.getText()).toString());
						flagnum++;
					}
					if	(flagnum >= maxnum)						return true;
					//System.out.println((new StringBuilder("node.getText() node instanceof=")).append(node.getText()).toString());
				}
			}

		}

		if	(flagnum >= maxnum)			return true;			else			return false;
	}

	public boolean CheckList(String xmlFile, String xmlpath, String name, String text)		throws Exception	{
		boolean flag = false;
		Document document = OpenXml(xmlFile);
		this.xmlFile=xmlFile;
		List list = document.selectNodes(xmlpath);
		//System.out.println((new StringBuilder("list size=")).append(list.size()).toString());
		for	(Iterator iter = list.iterator(); iter.hasNext();)		{
			Element myElement = (Element)iter.next();
			//System.out.println((new StringBuilder(" //count= ")).append(myElement.nodeCount()).toString());
			for	(int k = 0; k < myElement.nodeCount(); k++)
			{
				Node node = myElement.node(k);
				if	(node instanceof Element)
				{
					if	(node.getName().equals(name))
					{
						//System.out.println((new StringBuilder("name equal name=")).append(node.getName()).toString());
						if	(node.getText().equals(text))
						{
							//System.out.println((new StringBuilder("name text equal text=")).append(node.getText()).toString());
							flag = true;
							return flag;
						}
					}
					//System.out.println((new StringBuilder("node.getText() node instanceof=")).append(node.getText()).toString());
				}
			}

		}

		return flag;
	}

	public Element removeElement1(Element parentEle)	throws Exception	{
		//if	(parentEle == null)			return parentEle;
		if	(parentEle == null)			throw	new Exception();
		Iterator iter = parentEle.elementIterator();
		Element delEle = null;
		Element tmp;
		try {
			for	(; iter.hasNext(); parentEle.remove(tmp)){
				tmp = (Element)iter.next();
				//System.out.print((new StringBuilder("parentlemnt name=")).append(parentEle.getName()).append(" ").toString());
				//System.out.print((new StringBuilder("child elemnt name=")).append(tmp.getName()).append(" ").toString());
				//System.out.println((new StringBuilder("child elemnt text=")).append(tmp.getText()).toString());
			}
		}	catch(Exception e)	{
			CLog.writeLog("error,removeElement1:"+parentEle);
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+e.toString());
		}

		CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+parentEle);
		return parentEle.getParent();
	}

	public Element GetParentEle(Document doc, String xmlpath, String name, String text)		throws Exception	{
		Element rtelement = null;
		boolean flag = false;
		LinkedList rtlist = new LinkedList();
		List list = doc.selectNodes(xmlpath);
		for	(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Element myElement = (Element)iter.next();
			for	(int k = 0; k < myElement.nodeCount(); k++)
			{
				Node node = myElement.node(k);
				if	(!(node instanceof Element) || !node.getName().equals(name) || !node.getText().equals(text))
					continue;
				flag = true;
				break;
			}

			if	(flag)
			{
				rtelement = myElement;
				return rtelement;
			}
			flag = false;
		}

		CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+xmlpath+":"+name+":"+text);
		return rtelement;
	}

	public Element GetParentEleMore(Document doc, String xmlpath, String namelist, String textlist)	throws Exception	{
		Element rtelement = null;
		boolean flag = false;
		LinkedList rtlist = new LinkedList();
		String name1,text1;
		StringTokenizer tokens1=new StringTokenizer(namelist,"|");
		StringTokenizer tokens2=new StringTokenizer(textlist,"|");
		int maxnum = tokens1.countTokens();
		int flagnum = 0;

		List list = doc.selectNodes(xmlpath);
		for	(;tokens1.hasMoreTokens();)	{
			name1=tokens1.nextToken();
			if	(name1==null || name1.length()<=0)			maxnum--;
		}
		//System.out.println((new StringBuilder("list size=")).append(list.size()).toString());
		for	(Iterator iter = list.iterator(); iter.hasNext();)	{
			flagnum = 0;
			Element myElement = (Element)iter.next();
			//System.out.println((new StringBuilder(" //count= ")).append(myElement.nodeCount()).toString());
			for	(int k = 0; k < myElement.nodeCount(); k++)	{
				Node node = myElement.node(k);
				if	(node instanceof Element){
					tokens1=new StringTokenizer(namelist,"|");
					tokens2=new StringTokenizer(textlist,"|");
					for	(;tokens1.hasMoreTokens();)	{
						name1=tokens1.nextToken();
						text1=tokens2.hasMoreTokens()?tokens2.nextToken():"";
						if	(name1!=null && node.getName().equals(name1) && node.getText().equals(text1))							flagnum++;
						if	(name1!=null && node.getName().equals(name1) && !node.getText().equals(text1)){
								//CLog.writeLog(name1+":"+node.getName()+":"+text1+":"+node.getText()+":"+flagnum+"/"+maxnum);
								break;
							}
					}
					//System.out.println((new StringBuilder("name text equal text=")).append(node.getText()).toString());
					//System.out.println((new StringBuilder("node.getText() node instanceof=")).append(node.getText()).toString());
				}
				if	(flagnum < maxnum)			continue;
					else	rtelement = myElement;
				flag = true;
				break;
			}
			if	(flag)				break;
		}

		CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+flagnum+"/"+maxnum+":"+xmlpath+":"+namelist+":"+textlist+":"+flag);
		if	(!flag)					throw	new Exception();
		return rtelement;
	}

	public List GetListValues(String xmlFile, String xmlpath, String name, String text)		throws Exception	{
		boolean flag = false;
		LinkedList rtlist = new LinkedList();

		Document document = OpenXml(xmlFile);
		this.xmlFile=xmlFile;
		List list = document.selectNodes(xmlpath);
		//System.out.println((new StringBuilder("list size=")).append(list.size()).toString());
		for	(Iterator iter = list.iterator(); iter.hasNext();)	{
			Element myElement = (Element)iter.next();
			for	(int k = 0; k < myElement.nodeCount(); k++)	{
				Node node = myElement.node(k);
				if	(!(node instanceof Element) || !node.getName().equals(name) || !node.getText().equals(text))
					continue;
				flag = true;
				break;
			}

			if	(flag){
				for	(int k = 0; k < myElement.nodeCount(); k++){
					Node node = myElement.node(k);
					if	(node instanceof Element)						rtlist.add(node.getText());
				}

			}
		}

		return rtlist;
	}

	public List ReadTextByName(String xmlFile, String xmlpath)		throws Exception	{
		List rtlist = new LinkedList();

		Document document = OpenXml(xmlFile);
		this.xmlFile=xmlFile;
		List list = document.selectNodes(xmlpath);
		for	(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Element myElement = (Element)iter.next();
			//System.out.println((new StringBuilder(" //count= ")).append(myElement.nodeCount()).toString());
			for	(int k = 0; k < myElement.nodeCount(); k++)
			{
				Node node = myElement.node(k);
				if	(node instanceof Element)
					rtlist.add((new StringBuilder(String.valueOf(node.getName()))).append(node.getText()).toString());
			}

		}

		list = null;
		return rtlist;
	}

	public void showroot(String xmlFile)		throws Exception	{
		Document document = OpenXml(xmlFile);
		this.xmlFile=xmlFile;
		Element root = document.getRootElement();
		Element element;
		int count;
		for	(Iterator i = root.elementIterator(); i.hasNext();)
		{
			element = (Element)i.next();
			count = 0;
			for	(int k = 0; k < element.nodeCount(); k++)
			{
				Node node = element.node(k);
				if	(node instanceof Element)
				{
					count++;
					//System.out.println((new StringBuilder("the same element text=")).append(element.getText()).toString());
				}
			}
			//System.out.println((new StringBuilder("element text=")).append(element.getText()).append("node count=").append(element.nodeCount()).append("count=").append(count).toString())
		}

	}

	public int GetElemChildNum(Element element)		throws DocumentException	{
		int count = 0;
		for	(int k = 0; k < element.nodeCount(); k++)
		{
			Node node = element.node(k);
			if	(node instanceof Element)
				count++;
		}

		return count;
	}

	public Element AddFirstNode(Document doc, String xmlpath, String elem, String newelem, String values, String attrname, String attrvalue)		throws DocumentException	{
		Element firstelm = null;
		List list = doc.selectNodes(xmlpath);
		//System.out.println((new StringBuilder("list.size=")).append(list.size()).toString());
		for	(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Element element = (Element)iter.next();
			//System.out.println((new StringBuilder("haha")).append(element.getName()).toString());
			if	(element.getName().equals(elem))
			{
				firstelm = addAttAndElem(element, newelem, values, attrname, attrvalue);
				return firstelm;
			}
		}

		CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+xmlFile+":"+xmlpath);
		return firstelm;
	}

	public boolean WriteTextByName(String xmlFile, String xmlpath, String elem, String newelem, String values, boolean fl, String attr,String attname, int rownumber)	throws Exception	{
		boolean flag = false;
		Document document = OpenXml(xmlFile);
		List list = document.selectNodes(xmlpath);
		//System.out.println((new StringBuilder("list.size=")).append(list.size()).toString());
		for	(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Element element = (Element)iter.next();
			int t1 = GetElemChildNum(element);
			//System.out.println((new StringBuilder("the  ")).append(element.getText()).append("  have  ").append(t1).append("   child").toString());
			if	(element.getName().equals(elem))
				if	(fl)
				{
					if	(t1 < rownumber)
					{
						addNode(element, newelem, values);
						flag = xmlCommit(document, xmlFile);
						return flag;
					}
				} else
				{
					addAttAndElem(element, newelem, values, attr, attname);
					flag = xmlCommit(document, xmlFile);
					return flag;
				}
		}

		CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+xmlFile+":"+xmlpath);
		return flag;
	}

	public List getPageList(List rsList, int pageSize, int pageIndex)		throws Exception	{
		if	(rsList == null)			return null;
		if	(pageSize < 1)			pageSize = 5;
		if	(pageIndex < 1)			pageIndex = 1;
		int rsSize = rsList.size();
		int allPage = 0;
		if	(pageIndex < 1)			pageIndex = 1;
		if	(rsSize > 0)
			if	(rsSize % pageSize > 0)
				allPage = rsSize / pageSize + 1;
			else
				allPage = rsSize / pageSize;
		if	(pageIndex > allPage)			pageIndex = allPage;
		int startIdx = (pageIndex - 1) * pageSize;
		int endIdx = pageIndex * pageSize;
		if	(startIdx < 0)			startIdx = 0;
		if	(endIdx > rsSize)			endIdx = rsSize;
		return rsList.subList(startIdx, endIdx);
	}

	public List getValues(Element ele)
	{
		List eles = ele.elements();
		List list = new ArrayList();
		Element e;
		for	(Iterator iterator = eles.iterator(); iterator.hasNext(); list.add((new StringBuilder(String.valueOf(e.getName()))).append("|").append(e.getText()).toString()))
			e = (Element)iterator.next();

		return list;
	}

	public Element getNodeByKey(String xmlFile, String xmlpath, String value)		throws Exception	{
		Element element = null;
		Document document = OpenXml(xmlFile);
		this.xmlFile=xmlFile;
		List list = document.selectNodes(xmlpath);
		for	(Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			Element ele = (Element)iterator.next();
			if	(ele.getTextTrim().equals(value))
				element = ele;
		}

		return element;
	}

	public Element getNodeByKey(Document document, String xmlpath, String value)	throws Exception	{
		Element element = null;
		List list = document.selectNodes(xmlpath);
		for	(Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			Element ele = (Element)iterator.next();
			if	(ele.getTextTrim().equals(value))
				element = ele;
		}

		return element;
	}

	public void removeElements(Document document, Element parentElement)	throws Exception	{
		List elements = parentElement.elements();
		Element element;
		for	(Iterator iterator = elements.iterator(); iterator.hasNext(); parentElement.remove(element))
			element = (Element)iterator.next();

		if	(parentElement.getParent() != null)
			parentElement.getParent().remove(parentElement);
	}

	public boolean createxmlFile(String xmlFile, String author, String instruction, String rootelm, String rootvalue)	{
		boolean rtn = false;
		Document document = DocumentHelper.createDocument();
		if	(author.trim() != "")
			document.addComment((new StringBuilder("   Manipulate Configure! Author: ")).append(author).append("  ").toString());
		Element catalogElement = document.addElement(rootelm);
		catalogElement.setText(rootvalue);
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(myencode);
		format.setSuppressDeclaration(true);
		format.setIndent(true);
		format.setIndent("   ");
		format.setNewlines(true);
		try
		{
			XMLWriter output = new XMLWriter(new FileWriter(xmlFile), format);
			this.xmlFile=xmlFile;
			if	(instruction != "")
				output.processingInstruction("xml", instruction);
			output.write(document);
			output.close();
			rtn = true;
		}	catch(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+e.toString());
		}
		return rtn;
	}

	public Document getDocument(String xmlFile)		throws Exception	{
		Document d = OpenXml(xmlFile);
		this.xmlFile=xmlFile;
		return d;
	}

	public Element getNode(Document d, String eleName)	{
		Element ele = (Element)d.selectSingleNode(eleName);
		return ele;
	}

	public Element addNode(Element parentEle, String eleName, String eleValue)	{
		Element newEle = null;
		//if	(!"".equals(eleName))	{
		if	(eleName!=null && eleName.length()>0)	{
			newEle = parentEle.addElement(eleName);
			//System.out.println(eleValue);
			//System.out.println(eleValue.length());
			newEle.setText(eleValue);
		}
		return newEle;
	}

	public boolean xmlCommit(Document doc, String xmlFile){
		stime=System.currentTimeMillis();
		boolean retboolean = false;
		try {

			BufferedReader br = new BufferedReader(new FileReader(xmlFile));
			this.xmlFile=xmlFile;
			String str = null;
			str = br.readLine();
			str = str.substring(str.indexOf("?") + 1, str.lastIndexOf("?")).trim();
			String tmp = str.substring(str.indexOf(" ")).trim();
/*
			String result="";
			String line=null;
			while  ((line = br.readLine()) !=  null ) {
				//CLog.writeLog(new String(re, myencode));
				//result+=new String(re, myencode);
				result+=line+"\n";
			}
			CLog.writeLog(xmlFile+"=0["+result+"]");
*/
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setSuppressDeclaration(true);
			format.setIndent(false);
			format.setIndent(" ");
			format.setIndentSize(4);
			format.setNewlines(true);
			format.setExpandEmptyElements(true);
			format.setEncoding(myencode);
			XMLWriter writer = new XMLWriter(new FileOutputStream(xmlFile), format);
			writer.processingInstruction("xml", tmp);
			writer.write(doc);
			writer.close();
			retboolean = true;
/*
			result="";
			br = new BufferedReader(new FileReader(xmlFile));
			while  ((line = br.readLine()) !=  null ) {
				//CLog.writeLog(new String(re, myencode));
				//result+=new String(re, myencode);
				result+=line+"\n";
			}
			CLog.writeLog(xmlFile+"=1["+result+"]");
*/
		}	catch(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+e.toString());
		}
		CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+xmlFile+":"+retboolean);
		CLog.writeLog("==Processing time:"+(System.currentTimeMillis() - stime)+" ms==");
		return retboolean;
	}

	public List getNodes(String xmlFile, String xmlpath)	throws Exception	{
		Document document = OpenXml(xmlFile);
		List list = document.selectNodes(xmlpath);
		return list;
	}

	public Element getNodeByAttribute(Document doc, String xmlpath, String attrName, String attrValue)	throws Exception	{
		List list = doc.selectNodes(xmlpath);
		Element element = null;
		for	(Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			Element ele = (Element)iterator.next();
			if	(attrValue.equals(ele.attribute(attrName).getText()))
			{
				element = ele;
				break;
			}
		}

		return element;
	}

	public Element getNodeByAttribute(Element parent, String child, String attrName, String attrValue)	throws Exception	{
		List list = parent.elements(child);
		Element element = null;
		for	(Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			Element ele = (Element)iterator.next();
			if	(attrValue.equals(ele.attribute(attrName).getText()))
			{
				element = ele;
				break;
			}
		}

		return element;
	}

	public void removeNodesByAttribute(Element parent, String child, String attrName, List attrValues)	throws Exception	{
		List list = parent.elements(child);
		for	(Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			Element ele = (Element)iterator.next();
			if	(attrValues.contains(ele.attribute(attrName).getText()))
				parent.remove(ele);
		}

	}

	public Element addNodeByAttribute(Element parent, String child, String attrName, int attrValue)	throws Exception	{
		Element element = null;
		int id = 0;
		Element afterEle = null;
		List list = parent.elements(child);
		if	(list.size() != 0)
		{
			for	(Iterator iterator = list.iterator(); iterator.hasNext();)
			{
				Element ele = (Element)iterator.next();
				id = Integer.parseInt(ele.attribute(attrName).getText());
				if	(attrValue < id)
				{
					afterEle = ele;
					break;
				}
			}

			if	(afterEle == null)
			{
				element = parent.addElement(child);
				element.addAttribute(attrName, (new StringBuilder(String.valueOf(attrValue))).toString());
			} else
			{
				List content = parent.content();
				int position = content.indexOf(afterEle);
				element = DocumentHelper.createElement(child);
				element.addAttribute(attrName, (new StringBuilder(String.valueOf(attrValue))).toString());
				content.add(position, element);
			}
		} else
		{
			element = parent.addElement(child);
			element.addAttribute(attrName, (new StringBuilder(String.valueOf(attrValue))).toString());
		}
		return element;
	}

	public Element addNodeByAttribute(Element parent, String child, String attrName1, int attrValue1, String attrName2, String attrValue2)	throws Exception	{
		Element element = null;
		int id = 0;
		Element afterEle = null;
		List list = parent.elements(child);
		if	(list.size() != 0)
		{
			for	(Iterator iterator = list.iterator(); iterator.hasNext();)
			{
				Element ele = (Element)iterator.next();
				id = Integer.parseInt(ele.attribute(attrName1).getText());
				if	(attrValue1 < id)
				{
					afterEle = ele;
					break;
				}
			}

			if	(afterEle == null)
			{
				element = parent.addElement(child);
				element.addAttribute(attrName2, attrValue2);
				element.addAttribute(attrName1, (new StringBuilder(String.valueOf(attrValue1))).toString());
			} else
			{
				List content = parent.content();
				int position = content.indexOf(afterEle);
				element = DocumentHelper.createElement(child);
				element.addAttribute(attrName2, attrValue2);
				element.addAttribute(attrName1, (new StringBuilder(String.valueOf(attrValue1))).toString());
				content.add(position, element);
			}
		} else
		{
			element = parent.addElement(child);
			element.addAttribute(attrName2, attrValue2);
			element.addAttribute(attrName1, (new StringBuilder(String.valueOf(attrValue1))).toString());
		}
		return element;
	}

	public void removeElement(Element parent, String elementName)
	{
		List eles = parent.elements();
		for	(Iterator iterator = eles.iterator(); iterator.hasNext();)
		{
			Element ele = (Element)iterator.next();
			if	(ele.getName().equals(elementName))
			{
				parent.remove(ele);
				return;
			}
		}

	}

	public void CpxmlFile(String srcfile, String desfile)	throws Exception	{
		Document doc = getDocument(srcfile);
		BufferedReader br = new BufferedReader(new FileReader(srcfile));
		String str = null;
		str = br.readLine();
		str = str.substring(str.indexOf("?") + 1, str.lastIndexOf("?")).trim();
		String tmp = str.substring(str.indexOf(" ")).trim();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setSuppressDeclaration(true);
		format.setIndent(true);
		format.setIndent("   ");
		format.setNewlines(true);
		format.setExpandEmptyElements(true);
		format.setEncoding(myencode);
		XMLWriter writer = new XMLWriter(new FileOutputStream(desfile), format);
		writer.processingInstruction("xml", tmp);
		writer.write(doc);
		writer.close();
	}



	//批量查询
	public Collection	select(String xmlFile,String xmlpath,ObInterface obInterface) {
		return	select(xmlFile,xmlpath,obInterface,null);
	}

	//批量查询
	public Collection	select(String xmlFile,String xmlpath,ObInterface obInterface,String where) {
		Collection ret=new ArrayList();
		stime=System.currentTimeMillis();
		boolean			retboolean=false;
		if	(where==null)		where="";


		try {
			int maxnum=(int)Math.floor(where.split("\\|").length/2);
			//CLog.writeLog("maxnum="+maxnum+",length="+where.split("\\|").length);
			Collection xmllist=new ArrayList();
			java.util.List<org.dom4j.Element> eles=getNodes(xmlFile,xmlpath);
			this.xmlFile=xmlFile;
			java.util.List<org.dom4j.Element> listeles=new ArrayList();

			obInterface.setRetNum(eles.size());
			if	(where!=null && where.length()>0 && maxnum>0){
				for	(int j=0;j<obInterface.getRetNum();j++){
					org.dom4j.Element ele=eles.get(j);
					java.util.List<String> list=getValues(ele);
					int	flagnum = 0;
					retboolean=false;
					for	(String str:list){
						str=" "+str+" ";
			
/*
						if	(str.split("\\|")[0].trim().equals(where.split("\\|")[0].trim())){
							if	(str.split("\\|")[1].trim().indexOf(where.split("\\|")[1].trim())>=0)								retboolean=true;
							//CLog.writeLog(j+"/"+obInterface.getRetNum()+"==str["+str+"],where["+where+"]"+retboolean);
							break;
						}
*/
						for	(int m=0;m<maxnum;m++){
							if	(where.split("\\|")[2*m+1].trim().length()==0){
								flagnum++;
								continue;
							}
							if	(str.split("\\|")[0].trim().equals(where.split("\\|")[2*m].trim()) && str.split("\\|")[1].trim().indexOf(where.split("\\|")[2*m+1].trim())>=0){
								flagnum++;
								continue;
							}
						}
/*
*/
					}
					if	(flagnum>=maxnum)				retboolean=true;
					if	(retboolean)						listeles.add(ele);
					//temp.setErrMsg(idname);
					//temp.setSql(data);
				}
				eles=listeles;
			}

			obInterface.setRetNum(eles.size());
			int retNum=obInterface.getRetNum();
			int colnum=1,i=0;

			int maxPage=retNum/obInterface.getPageSize();
			maxPage+= (retNum%obInterface.getPageSize())==0?0:1;
			int curPage=obInterface.getPageNo();
			int pageSize=obInterface.getPageSize();

			if	((curPage < 1) || (curPage > maxPage)) curPage=1;
			if	(curPage>maxPage)		maxPage=curPage;

			obInterface.setPageNo(curPage);
			obInterface.setMaxPage(maxPage);
			String	idname="",data="";
			java.util.List<String> list=null;
			ObInterface temp=null;

			for	(i=0;i<retNum;i++){
				temp=new ObInterface();
				org.dom4j.Element ele=eles.get(i);
				list=getValues(ele);
				temp.setSerialNo(i+1);
				data="";
				idname="";
				for	(String str:list){
					str=" "+str+" ";
					idname+=str.split("\\|")[0].trim()+"|";
					data+=str.split("\\|")[1].trim()+"|";
				}/**/
				temp.setErrMsg(idname);
				temp.setSql(data);
				if	(i==(curPage-1)*pageSize)	obInterface.setErrMsg(idname);
				xmllist.add(temp);
			}

			ContentComparator comp = new ContentComparator();
			Collections.sort((List)xmllist,comp);

			Iterator		it=xmllist.iterator();

			for	(i=0;i<(curPage-1)*pageSize;i++)		it.next();
			for	(i=(curPage-1)*pageSize;(i<=curPage*pageSize-1)&&(i<=retNum-1);i++){
				temp=(ObInterface)it.next();
				temp.setSerialNo(i+1);
				ret.add(temp);
			}
			obInterface.setRowNum(i);
		}	catch(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+e.toString());
		}

		CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+xmlFile+":"+xmlpath+":"+where);
		CLog.writeLog("==Processing time:"+(System.currentTimeMillis() - stime)+" ms==");
		return ret;
	}

	//批量查询
	public Collection	select(String xmlFile,String xmlpath,ObInterface obInterface,String where,int flag) {
		Collection ret=new ArrayList();
		stime=System.currentTimeMillis();
		boolean			retboolean=false;
		if	(where==null)		where="";


		try {
			int maxnum=(int)Math.floor(where.split("\\|").length/2);
			//CLog.writeLog("maxnum="+maxnum+",length="+where.split("\\|").length);
			Collection xmllist=new ArrayList();
			java.util.List<org.dom4j.Element> eles=getNodes(xmlFile,xmlpath);
			this.xmlFile=xmlFile;
			java.util.List<org.dom4j.Element> listeles=new ArrayList();

			obInterface.setRetNum(eles.size());
			if	(where!=null && where.length()>0 && maxnum>0){
				for	(int j=0;j<obInterface.getRetNum();j++){
					org.dom4j.Element ele=eles.get(j);
					java.util.List<String> list=getValues(ele);
					int	flagnum = 0;
					retboolean=false;
					for	(String str:list){
						str=" "+str+" ";
			
/*
						if	(str.split("\\|")[0].trim().equals(where.split("\\|")[0].trim())){
							if	(str.split("\\|")[1].trim().indexOf(where.split("\\|")[1].trim())>=0)								retboolean=true;
							//CLog.writeLog(j+"/"+obInterface.getRetNum()+"==str["+str+"],where["+where+"]"+retboolean);
							break;
						}
*/
						for	(int m=0;m<maxnum;m++){
							if	(where.split("\\|")[2*m+1].trim().length()==0){
								flagnum++;
								continue;
							}
							if	(str.split("\\|")[0].trim().equals(where.split("\\|")[2*m].trim()) && str.split("\\|")[1].trim().indexOf(where.split("\\|")[2*m+1].trim())>=0){
								flagnum++;
								continue;
							}
						}
/*
*/
					}
					if	(flagnum>=maxnum)				retboolean=true;
					if	(retboolean)						listeles.add(ele);
					//temp.setErrMsg(idname);
					//temp.setSql(data);
				}
				eles=listeles;
			}

			obInterface.setRetNum(eles.size());
			int retNum=obInterface.getRetNum();
			int colnum=1,i=0;

			int maxPage=retNum/obInterface.getPageSize();
			maxPage+= (retNum%obInterface.getPageSize())==0?0:1;
			int curPage=obInterface.getPageNo();
			int pageSize=obInterface.getPageSize();

			if	((curPage < 1) || (curPage > maxPage)) curPage=1;
			if	(curPage>maxPage)		maxPage=curPage;

			obInterface.setPageNo(curPage);
			obInterface.setMaxPage(maxPage);
			String	idname="",data="";
			java.util.List<String> list=null;
			ObInterface temp=null;

			for	(i=0;i<retNum;i++){
				temp=new ObInterface();
				org.dom4j.Element ele=eles.get(i);
				list=getValues(ele);
				temp.setSerialNo(i+1);
				data="";
				idname="";
				for	(String str:list){
					str=" "+str+" ";
					idname+=str.split("\\|")[0].trim()+"|";			
					data+= str.split("\\|")[0].trim()+"=" + str.split("\\|")[1].trim()+"|";					
				}/**/
		
				temp.setErrMsg(idname);
				temp.setSql(data);
				if	(i==(curPage-1)*pageSize)	obInterface.setErrMsg(idname);
				xmllist.add(temp);
			}

			ContentComparator comp = new ContentComparator();
			Collections.sort((List)xmllist,comp);

			Iterator		it=xmllist.iterator();

			for	(i=0;i<(curPage-1)*pageSize;i++)		it.next();
			for	(i=(curPage-1)*pageSize;(i<=curPage*pageSize-1)&&(i<=retNum-1);i++){
				temp=(ObInterface)it.next();
				temp.setSerialNo(i+1);
				ret.add(temp);
			}
			obInterface.setRowNum(i);
		}	catch(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+e.toString());
		}

		CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+xmlFile+":"+xmlpath+":"+where);
		CLog.writeLog("==Processing time:"+(System.currentTimeMillis() - stime)+" ms==");
		return ret;
	}

	//update
	public boolean	update(String xmlFile, String xmlpath, String namelist1, String textlist1, String namelist2, String textlist2){
		stime=System.currentTimeMillis();
		boolean			retboolean=false;
		try {
			delete(xmlFile, xmlpath, namelist1, textlist1);
			retboolean=insert(xmlFile, xmlpath, namelist2, textlist2);
		}	catch(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+e.toString());
			return false;
		}

		CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+xmlFile+":"+xmlpath+":"+namelist1+":"+textlist1+":"+namelist2+":"+textlist2+":"+retboolean);
		CLog.writeLog("==Processing time:"+(System.currentTimeMillis() - stime)+" ms==");
		return true;
	}

	//delete
	public boolean	delete(String xmlFile, String xmlpath, String namelist, String textlist){
		stime=System.currentTimeMillis();
		boolean			retboolean=false;
		try {
			Document doc = OpenXml(xmlFile);
			Element firelemnt=GetParentEleMore(doc, xmlpath, namelist, textlist);
			Element parentelemnt=removeElement1(firelemnt);
			parentelemnt.remove(firelemnt);
			retboolean=xmlCommit(doc,xmlFile);
		}	catch(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+e.toString());
		}

		CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+xmlFile+":"+xmlpath+":"+namelist+":"+textlist+":"+retboolean);
		CLog.writeLog("==Processing time:"+(System.currentTimeMillis() - stime)+" ms==");
		return retboolean;
	}

	//insert
	public boolean	insert(String xmlFile, String xmlpath, String namelist, String textlist){
		//CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+xmlFile+":"+xmlpath+":"+namelist+":"+textlist);
		stime=System.currentTimeMillis();
		boolean			retboolean=false;
		try {
			delete(xmlFile, xmlpath, namelist, textlist);
		}	catch(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+e.toString());
		}
		try {
			StringTokenizer tokens1=new StringTokenizer(namelist,"|");
			StringTokenizer tokens2=new StringTokenizer(textlist,"|");
			int colnum=tokens1.countTokens();
			if	(colnum==0)	return false;
			StringTokenizer tokens0=new StringTokenizer(xmlpath.substring(1),"/");
			if	(tokens0.countTokens()!=3)	return false;
			String	name1=tokens0.nextToken();
			String	name2=tokens0.nextToken();
			String	name3=tokens0.nextToken();

			Document doc = OpenXml(xmlFile);
			Element firelemnt=AddFirstNode(doc, "//"+name1+"/"+name2, name2, name3, " ", "table", name2);
			//Element firelemnt=AddFirstNode(doc, "//ManipulateConf/tCardType", "tCardType", "tCardType_field", " ", "table", "tCardType");

			for	(int i=1;tokens1.hasMoreTokens();i++){
				addNode(firelemnt,tokens1.nextToken(),tokens2.hasMoreTokens()?tokens2.nextToken():"");
			}
			retboolean=xmlCommit(doc,xmlFile);
		}	catch(Exception e)	{
			CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+e.toString());
		}

		CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+xmlFile+":"+xmlpath+":"+namelist+":"+textlist+":"+retboolean);
		CLog.writeLog("==Processing time:"+(System.currentTimeMillis() - stime)+" ms==");
		return retboolean;
	}

	//单一查询
	public boolean		getRow(String xmlFile, String xmlpath, String namelist, String textlist) throws Exception	{
		int flagnum = 0;
		String name1,text1;
		StringTokenizer tokens1=new StringTokenizer(namelist,"|");
		StringTokenizer tokens2=new StringTokenizer(textlist,"|");
		int maxnum = tokens1.countTokens();
		for	(;tokens1.hasMoreTokens();)	{
			name1=tokens1.nextToken();
			if	(name1==null || name1.length()<=0)			maxnum--;
		}

		Document document = OpenXml(xmlFile);
		this.xmlFile=xmlFile;
		List list = document.selectNodes(xmlpath);
		//System.out.println((new StringBuilder("list size=")).append(list.size()).toString());
		for	(Iterator iter = list.iterator(); iter.hasNext();)		{
			flagnum = 0;
			Element myElement = (Element)iter.next();
			//System.out.println((new StringBuilder(" //count= ")).append(myElement.nodeCount()).toString());
			for	(int k = 0; k < myElement.nodeCount(); k++)			{
				Node node = myElement.node(k);
				if	(node instanceof Element)	{

					tokens1=new StringTokenizer(namelist,"|");
					tokens2=new StringTokenizer(textlist,"|");
					for	(;tokens1.hasMoreTokens();)	{
						name1=tokens1.nextToken();
						text1=tokens2.hasMoreTokens()?tokens2.nextToken():"";
						if	(name1!=null && node.getName().equals(name1) && node.getText().equals(text1))	flagnum++;
					}
					if	(flagnum >= maxnum)						return true;
					//System.out.println((new StringBuilder("node.getText() node instanceof=")).append(node.getText()).toString());
				}
			}

		}

		CLog.writeLog(new Exception().getStackTrace()[0].getClassName()+":"+new Exception().getStackTrace()[0].getMethodName()+":"+new Exception().getStackTrace()[0].getLineNumber()+":"+xmlFile+":"+xmlpath+":"+namelist+":"+textlist);
		CLog.writeLog("==Processing time:"+(System.currentTimeMillis() - stime)+" ms==");
		if	(flagnum >= maxnum)			return true;			else			return false;
	}
}

//ManipulateConf/tCardType/tCardType_field
//ManipulateConf/tCardType", "tCardType", "tCardType_field", " ", "table", "tCardType");
//String xmlpath,            String elem,  String newelem,String values, String attrname, String attrvalue)		throws DocumentException	{


//ManipulateConf/tCardType/tCardType_field




class ContentComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		ObInterface c1 = (ObInterface) o1;
		ObInterface c2 = (ObInterface) o2;
		int ret=c1.getSql().compareTo(c2.getSql());

		return ret;
	}
}