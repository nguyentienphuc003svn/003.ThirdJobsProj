package com.vab.iflex.gateway.paybillservice;


import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import ccasws.CLog;



/**
 *
 * @author nhutlm
 */
public class DemoApp {

	private  String _accesskey = "iz1VlwidWkjUelpgfISs";
	private  String _partnercode = "VIETABANK";
	private  String _signature = "@Q.wv60q{zV>}~&$;R{=Y[ ]MVy1B+fy\"u835~)u";

	//private final String urlws = "http://localhost:8084/wsgwatm/services/WSBean";//http://sandbox.ebanking.vietabank.com.vn/wsebankgateway/services/WSBean";
	private  String urlws = "http://192.168.31.215:8080/wsgwatm/services/WSBean";//http://sandbox.ebanking.vietabank.com.vn/wsebankgateway/services/WSBean";

	private final String timeout = "60000";
	private final String l_methodname = "processPayment";

	public Document PayBillReqDoc = null;

	public String queryBillReq = "<PaymentRequestDTO><serviceType><DataMapDTO><dataName>Ä�iá»‡n thoáº¡i di Ä‘á»™ng</dataName><dataValue>MOBILE</dataValue></DataMapDTO></serviceType><provider><DataMapDTO><dataName>Vinaphone Há»“ ChĂ­ Minh</dataName><dataValue>VINASG</dataValue></DataMapDTO></provider><customerId>0913159595</customerId><procesingCode>QUERY</procesingCode><paymentMethod>CARD</paymentMethod><contactPhoneNumber></contactPhoneNumber><requestTime>%1$s</requestTime><promotionCode></promotionCode></PaymentRequestDTO>";

	public String srcAcc = "<srcAccount><AccountNoDTO><nbrAccount>0209000035849000</nbrAccount></AccountNoDTO></srcAccount>";

	public static final String US = Character.toString((char) 0x1F);
	
	public DemoApp()
	{
		this.loadPara();
	}

	private Object[] callWsGateway(String methodname, Object[] objs) throws Exception {

		WSBeanServiceLocator wssl = new WSBeanServiceLocator();
		wssl.setWSBeanEndpointAddress(urlws);
		WSBeanSoapBindingStub wss;

		try {
			wss = (WSBeanSoapBindingStub) wssl.getWSBean();
			wss.setTimeout(Integer.parseInt(timeout));
			wss.setHeader(Stub.setSoapHeaderElement(_accesskey, methodname, _signature));
			Object[] obj_result = wss.callExecution(methodname, _partnercode, objs);
			return obj_result;
		} 
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}

	public boolean GetList() throws Exception {
		//Tao chuoi xml request truyen vao tham so procesingCode la getServiceList
		String xmlreq = "<PaymentRequestDTO><procesingCode>getServiceList</procesingCode></PaymentRequestDTO>";
		Object[] p_objs = new Object[1];
		p_objs[0] = xmlreq;
		Object[] resqObject = callWsGateway(l_methodname, p_objs);

		//Ket qua tra ve bao gom 2 phan tu, phan tu [0] cho biet ket qua goi webservice, neu thanh cong thi phan tu [1] chua xnl cua doi tuong tra ve
		if (resqObject != null && resqObject.length > 0) {
			if (resqObject[0].equals(0)) {//Goi webservice thanh cong
				System.out.println("Query cac dich vu thanh cong:");
				String xmlresponse = resqObject[1].toString();
				//System.out.println("Danh sach dich vu:");
				//System.out.println(xmlresponse);

				return true;
			} 
			else {
				System.err.println("Query cac dich vu khong thanh cong, ma loi:" + resqObject[0].toString());
				return false;
			}
		} 
		else {
			System.err.println("Loi he thong, khong goi duoc webservice");
			return false;
		}

		//return true;
	}

	public String GetListCcas() throws Exception {
		//Tao chuoi xml request truyen vao tham so procesingCode la getServiceList
		String xmlreq = "<PaymentRequestDTO><procesingCode>getServiceList</procesingCode></PaymentRequestDTO>";
		Object[] p_objs = new Object[1];
		p_objs[0] = xmlreq;
		Object[] resqObject = callWsGateway(l_methodname, p_objs);

		//Ket qua tra ve bao gom 2 phan tu, phan tu [0] cho biet ket qua goi webservice, neu thanh cong thi phan tu [1] chua xnl cua doi tuong tra ve
		if (resqObject != null && resqObject.length > 0) {
			if (resqObject[0].equals(0)) {//Goi webservice thanh cong
				System.out.println("Query cac dich vu thanh cong:");
				String xmlresponse = resqObject[1].toString();
				//System.out.println("Danh sach dich vu:");
				//System.out.println(xmlresponse);

				return xmlresponse.trim();
			} 
			else {
				System.err.println("Query get List cac dich vu khong thanh cong, ma loi:" + resqObject[0].toString());
				return "01";
			}
		} 
		else {
			System.err.println("Loi he thong, khong goi duoc webservice get List");
			return "01";
		}
		//return true;	
	}

	public boolean QueryBill() throws Exception {
		//Tao chuoi xml request truyen vao tham so procesingCode la QUERY, serviceType vĂ  Provider lay tá»« káº¿t quáº£ tráº£ vá»� á»Ÿ bÆ°á»›c getList
		//String xmlreq = "<PaymentRequestDTO><serviceType><DataMapDTO><dataName>Ä�iá»‡n</dataName><dataValue>DIEN</dataValue></DataMapDTO></serviceType><provider><DataMapDTO><dataName>EVN Há»“ChĂ­Minh</dataName><dataValue>EVNSG</dataValue></DataMapDTO></provider><customerId>PE14000211385</customerId><procesingCode>QUERY</procesingCode><paymentMethod>CARD</paymentMethod><contactPhoneNumber></contactPhoneNumber><requestTime>12112015101939</requestTime><promotionCode></promotionCode></PaymentRequestDTO>";
		//String xmlreq = "<PaymentRequestDTO><serviceType><DataMapDTO><dataName>Ä�iá»‡n thoáº¡i di Ä‘á»™ng</dataName><dataValue>MOBILE</dataValue></DataMapDTO></serviceType><provider><DataMapDTO><dataName>Vinaphone Há»“ ChĂ­ Minh</dataName><dataValue>VINASG</dataValue></DataMapDTO></provider><customerId>0913159595</customerId><procesingCode>QUERY</procesingCode><paymentMethod>CARD</paymentMethod><contactPhoneNumber></contactPhoneNumber><requestTime>%1$s</requestTime><promotionCode></promotionCode></PaymentRequestDTO>";
		String xmlreq = this.queryBillReq;
		this.PayBillReqDoc = DemoApp.loadXMLFromString(xmlreq);        

		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		xmlreq = String.format(xmlreq, sdf.format(new Date()));
		Object[] p_objs = new Object[1];
		p_objs[0] = xmlreq;
		Object[] resqObject = callWsGateway(l_methodname, p_objs);

		//Ket qua tra ve bao gom 2 phan tu, phan tu [0] cho biet ket qua goi webservice, neu thanh cong thi phan tu [1] chua xnl cua doi tuong tra ve
		if (resqObject != null && resqObject.length > 0) {
			if (resqObject[0].equals(0)) {//Goi webservice thanh cong
				System.out.println("Tim Bill thanh cong:");

				//----Change Response to Doc-------- 
				String xmlresponse = resqObject[1].toString();
				CLog.writeLog("Tim Bill thanh cong: " + xmlresponse);
				Document xmlresponseDoc = DemoApp.loadXMLFromString(xmlresponse);
				//----Change Response to Doc-------- 

				//----Calculate total Amt--------
				boolean isInvoice = xmlresponseDoc.getElementsByTagName("invoices").item(0).hasChildNodes();
				System.out.println("Co Invoice " + isInvoice);
				long totalamt = 0l;
				if (isInvoice)
				{
					int numberOfInvoice = xmlresponseDoc.getElementsByTagName("invoices").item(0).getChildNodes().getLength();
					System.out.println("Co bao nhieu Invoice " + numberOfInvoice);

					for (int i=0; i<numberOfInvoice; i++)
					{
						//
						String amountInvoiceNodeName = xmlresponseDoc.getElementsByTagName("invoices").item(0).getChildNodes().item(i).getChildNodes().item(9).getNodeName().trim();
						System.out.println("Co fai Node Invoice " + amountInvoiceNodeName);
						String amountInvoiceNodeValue = xmlresponseDoc.getElementsByTagName("invoices").item(0).getChildNodes().item(i).getChildNodes().item(9).getTextContent().trim();
						System.out.println("Gia tri Node Invoice " + amountInvoiceNodeValue);

						if (amountInvoiceNodeValue.length() > 0) totalamt = totalamt + Long.parseLong(amountInvoiceNodeValue);
						System.out.println("Tong so tien Bill " + totalamt);
					}
				}
				//----Calculate total Amt-------- 


				//----Add to become Bill Request-------- 
				Node newNode = null;

				newNode = xmlresponseDoc.getElementsByTagName("customerName").item(0).cloneNode(true);this.PayBillReqDoc.adoptNode(newNode);this.PayBillReqDoc.getDocumentElement().appendChild(newNode);
				newNode = xmlresponseDoc.getElementsByTagName("address").item(0).cloneNode(true);this.PayBillReqDoc.adoptNode(newNode);this.PayBillReqDoc.getDocumentElement().appendChild(newNode);

				Document srcAccDoc = DemoApp.loadXMLFromString(this.srcAcc);
				newNode = srcAccDoc.getElementsByTagName("srcAccount").item(0).cloneNode(true);this.PayBillReqDoc.adoptNode(newNode);this.PayBillReqDoc.getDocumentElement().appendChild(newNode);

				//this.PayBillReqDoc.removeChild(this.PayBillReqDoc.getElementsByTagName("procesingCode").item(0));
				//this.PayBillReqDoc.adoptNode(xmlresponseDoc.getElementsByTagName("procesingCode").item(0));
				this.PayBillReqDoc.getElementsByTagName("procesingCode").item(0).setTextContent("PAY");

				newNode = xmlresponseDoc.getElementsByTagName("partnerService").item(0).cloneNode(true);this.PayBillReqDoc.adoptNode(newNode);this.PayBillReqDoc.getDocumentElement().appendChild(newNode);
				newNode = xmlresponseDoc.getElementsByTagName("invoices").item(0).cloneNode(true);this.PayBillReqDoc.adoptNode(newNode);this.PayBillReqDoc.getDocumentElement().appendChild(newNode);

				Node payAmt = this.PayBillReqDoc.createElement("paymentAmount");
				payAmt.setTextContent(String.valueOf(totalamt));
				this.PayBillReqDoc.adoptNode(payAmt);this.PayBillReqDoc.getDocumentElement().appendChild(payAmt);
				//----Add to become Bill Request------------- 

				//System.out.println("Thong Tin Bill:");
				//System.out.println(xmlresponse);

				return true;
			} 
			else {
				System.err.println("Khong Tim Thay Bill, ma loi:" + resqObject[0].toString());
				return false;
			}
		} 
		else {
			System.err.println("Loi he thong, khong goi duoc webservice");
			return false;
		}
	}


	public String QueryBillCcas(String tendichvu, String madichvu, String tennhacc, String manhacc, String custID, String custacc) throws Exception {
		String ATMRes = "00" + DemoApp.US;//Concat with Success case

		//Tao chuoi xml request truyen vao tham so procesingCode la QUERY, serviceType vĂ  Provider lay tá»« káº¿t quáº£ tráº£ vá»� á»Ÿ bÆ°á»›c getList
		this.queryBillReq = this.toQueryBillReqString(tendichvu, madichvu, tennhacc, manhacc, custID, custacc).trim();		
		String xmlreq = this.queryBillReq;
		this.PayBillReqDoc = DemoApp.loadXMLFromString(xmlreq);        

		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		xmlreq = String.format(xmlreq, sdf.format(new Date()));
		Object[] p_objs = new Object[1];
		p_objs[0] = xmlreq;
		Object[] resqObject = callWsGateway(l_methodname, p_objs);

		//Ket qua tra ve bao gom 2 phan tu, phan tu [0] cho biet ket qua goi webservice, neu thanh cong thi phan tu [1] chua xnl cua doi tuong tra ve
		if (resqObject != null && resqObject.length > 0) {
			if (resqObject[0].equals(0)) {//Goi webservice thanh cong
				System.out.println("Tim Bill thanh cong:");

				//----Change Response to Doc-------- 
				String xmlresponse = resqObject[1].toString();
				CLog.writeLog("Tim Bill thanh cong: " + xmlresponse);
				Document xmlresponseDoc = DemoApp.loadXMLFromString(xmlresponse);
				//----Change Response to Doc-------- 

				//----Calculate total Amt--------
				boolean isInvoice = xmlresponseDoc.getElementsByTagName("invoices").item(0).hasChildNodes();
				System.out.println("Co Invoice " + isInvoice);
				long totalamt = 0l;
				String sumBillDetail = "";				
				if (isInvoice)
				{
					int numberOfInvoice = xmlresponseDoc.getElementsByTagName("invoices").item(0).getChildNodes().getLength();
					System.out.println("Co bao nhieu Invoice " + numberOfInvoice);

					for (int i=0; i<numberOfInvoice; i++)
					{
						//
						String amountInvoiceNodeName = xmlresponseDoc.getElementsByTagName("invoices").item(0).getChildNodes().item(i).getChildNodes().item(9).getNodeName().trim();
						System.out.println("Co fai Node Invoice " + amountInvoiceNodeName);
						String amountInvoiceNodeValue = xmlresponseDoc.getElementsByTagName("invoices").item(0).getChildNodes().item(i).getChildNodes().item(9).getTextContent().trim();
						System.out.println("Gia tri Node Invoice " + amountInvoiceNodeValue);

						if (amountInvoiceNodeValue.length() > 0) totalamt = totalamt + Long.parseLong(amountInvoiceNodeValue);
						System.out.println("Tong so tien Bill " + totalamt);
						
						//
						String BillDetails = xmlresponseDoc.getElementsByTagName("invoices").item(0).getChildNodes().item(i).getChildNodes().item(11).getTextContent().trim();
						System.out.println("Gia tri Node Description " + BillDetails);
						sumBillDetail = sumBillDetail + BillDetails + "$";
					}
				}
				//----Calculate total Amt-------- 


				//----Add to become Bill Request-------- 
				Node newNode = null;

				newNode = xmlresponseDoc.getElementsByTagName("customerName").item(0).cloneNode(true);this.PayBillReqDoc.adoptNode(newNode);this.PayBillReqDoc.getDocumentElement().appendChild(newNode);ATMRes = ATMRes + newNode.getTextContent().trim() + DemoApp.US;  
				newNode = xmlresponseDoc.getElementsByTagName("address").item(0).cloneNode(true);this.PayBillReqDoc.adoptNode(newNode);this.PayBillReqDoc.getDocumentElement().appendChild(newNode);

				this.srcAcc = this.toSrcAccString(custacc).trim();
				Document srcAccDoc = DemoApp.loadXMLFromString(this.srcAcc);
				newNode = srcAccDoc.getElementsByTagName("srcAccount").item(0).cloneNode(true);this.PayBillReqDoc.adoptNode(newNode);this.PayBillReqDoc.getDocumentElement().appendChild(newNode);

				//this.PayBillReqDoc.removeChild(this.PayBillReqDoc.getElementsByTagName("procesingCode").item(0));
				//this.PayBillReqDoc.adoptNode(xmlresponseDoc.getElementsByTagName("procesingCode").item(0));
				this.PayBillReqDoc.getElementsByTagName("procesingCode").item(0).setTextContent("PAY");

				newNode = xmlresponseDoc.getElementsByTagName("partnerService").item(0).cloneNode(true);this.PayBillReqDoc.adoptNode(newNode);this.PayBillReqDoc.getDocumentElement().appendChild(newNode);
				newNode = xmlresponseDoc.getElementsByTagName("invoices").item(0).cloneNode(true);this.PayBillReqDoc.adoptNode(newNode);this.PayBillReqDoc.getDocumentElement().appendChild(newNode);

				Node payAmt = this.PayBillReqDoc.createElement("paymentAmount");
				payAmt.setTextContent(String.valueOf(totalamt));
				this.PayBillReqDoc.adoptNode(payAmt);this.PayBillReqDoc.getDocumentElement().appendChild(payAmt);
				//----Add to become Bill Request------------- 

				//System.out.println("Thong Tin Bill:");
				//System.out.println(xmlresponse);

				//--------Concat the ATMRes String 
				ATMRes = ATMRes + custID.trim() + DemoApp.US + String.valueOf(totalamt).trim() + DemoApp.US + sumBillDetail.trim() + DemoApp.US;

				return ATMRes.trim();
			} 
			else {
				String myReturn = "01" + DemoApp.US;
				System.err.println("Khong Tim Thay Bill, ma loi: " + resqObject[0].toString() + " return " + myReturn);
				return myReturn;
			}
		} 
		else {
			String myReturn = "01" + DemoApp.US;			
			System.err.println("Loi he thong, khong goi duoc webservice, return " + myReturn);
			return myReturn;
		}
	}

	public String toQueryBillReqString(String tendichvu, String madichvu, String tennhacc, String manhacc, String custID, String custacc)
	{
		String myString = "<PaymentRequestDTO><serviceType><DataMapDTO><dataName>"+tendichvu.trim()+"</dataName><dataValue>"+madichvu.trim()+"</dataValue></DataMapDTO></serviceType><provider><DataMapDTO><dataName>"+tennhacc.trim()+"</dataName><dataValue>"+manhacc.trim()+"</dataValue></DataMapDTO></provider><customerId>"+custID.trim()+"</customerId><procesingCode>QUERY</procesingCode><paymentMethod>CARD</paymentMethod><contactPhoneNumber></contactPhoneNumber><requestTime>%1$s</requestTime><promotionCode></promotionCode></PaymentRequestDTO>";
		System.out.println("Query Bill Req String " + myString.trim());

		return myString.trim();
	}

	public String toSrcAccString(String custacc)
	{
		String myString = "<srcAccount><AccountNoDTO><nbrAccount>"+custacc.trim()+"</nbrAccount></AccountNoDTO></srcAccount>";
		System.out.println("Src Acc String " + myString.trim());

		return myString.trim();
	}

	public boolean PayBill() throws Exception {
		//Tao chuoi xml request truyen vao tham so procesingCode la QUERY, serviceType vĂ  Provider lay tá»« káº¿t quáº£ tráº£ vá»� á»Ÿ bÆ°á»›c getList
		//      String xmlreq = "<PaymentRequestDTO><serviceType><DataMapDTO><dataName>Ä�iá»‡n</dataName><dataValue>DIEN</dataValue></DataMapDTO></serviceType><provider><DataMapDTO><dataName>EVN Há»“ChĂ­Minh</dataName><dataValue>EVNSG</dataValue></DataMapDTO></provider><customerId>PE14000211385</customerId><procesingCode>QUERY</procesingCode><paymentMethod>CARD</paymentMethod><contactPhoneNumber></contactPhoneNumber><requestTime>12112015101939</requestTime><promotionCode></promotionCode></PaymentRequestDTO>";
		//String xmlreq = "<PaymentRequestDTO><serviceType><DataMapDTO><dataName>Ä�iá»‡n thoáº¡i di Ä‘á»™ng</dataName><dataValue>MOBILE</dataValue></DataMapDTO></serviceType><provider><DataMapDTO><dataName>Vinaphone Há»“ ChĂ­ Minh</dataName><dataValue>VINASG</dataValue></DataMapDTO></provider><customerId>0913159595</customerId><procesingCode>QUERY</procesingCode><paymentMethod>CARD</paymentMethod><contactPhoneNumber></contactPhoneNumber><requestTime>%1$s</requestTime><promotionCode></promotionCode></PaymentRequestDTO>";
		String xmlreq =  DemoApp.toXMLFromDoc(this.PayBillReqDoc);
		CLog.writeLog("XML Cho Pay Bill: " + xmlreq);

		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		xmlreq = String.format(xmlreq, sdf.format(new Date()));

		Object[] p_objs = new Object[1];
		p_objs[0] = xmlreq;
		Object[] resqObject = callWsGateway(l_methodname, p_objs);

		//Ket qua tra ve bao gom 2 phan tu, phan tu [0] cho biet ket qua goi webservice, neu thanh cong thi phan tu [1] chua xnl cua doi tuong tra ve
		if (resqObject != null && resqObject.length > 0) {
			if (resqObject[0].equals(0)) {//Goi webservice thanh cong
				System.out.println("Bill Chuyen di Thanh Cong:");
				String xmlresponse = resqObject[1].toString().trim();
				//System.out.println("Pay Bill Thanh Cong Response:");
				//System.out.println(xmlresponse);
				CLog.writeLog("Bill Chuyen di Thanh Cong " + xmlresponse);

				//----Check Authorization thanh cong 
				Document payBillResDoc = DemoApp.loadXMLFromString(xmlresponse);
				String finalResNodeName = payBillResDoc.getElementsByTagName("result").item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeName().trim();
				System.out.println("Co fai Node Result " + finalResNodeName);
				String finalResNodeValue = payBillResDoc.getElementsByTagName("result").item(0).getChildNodes().item(0).getChildNodes().item(0).getTextContent().trim();
				System.out.println("Gia tri Node Result " + finalResNodeValue);

				if (finalResNodeValue.equals("0")) 
				{
					System.out.println("Thanh Toan Bill SUCCESS");
					return true;
				}
				else   
				{
					System.out.println("Thanh Toan Bill THAT BAI " + finalResNodeValue);
					return false;
				}

				//return true;
			} 
			else {
				System.err.println("Pay Bill KHONG thanh cong, ma loi:" + resqObject[0].toString());
				return false;
			}
		} 
		else {
			System.err.println("Loi he thong, khong goi duoc webservice");
			return false;
		}
	}

	public String PayBillCcas() throws Exception {
		String xmlreq =  DemoApp.toXMLFromDoc(this.PayBillReqDoc);
		CLog.writeLog("XML Cho Pay Bill: " + xmlreq);

		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
		xmlreq = String.format(xmlreq, sdf.format(new Date()));

		Object[] p_objs = new Object[1];
		p_objs[0] = xmlreq;
		Object[] resqObject = callWsGateway(l_methodname, p_objs);

		//Ket qua tra ve bao gom 2 phan tu, phan tu [0] cho biet ket qua goi webservice, neu thanh cong thi phan tu [1] chua xnl cua doi tuong tra ve
		String mySuccReturn = "00" + DemoApp.US;
		String myFailReturn = "01" + DemoApp.US;
		if (resqObject != null && resqObject.length > 0) {
			if (resqObject[0].equals(0)) {//Goi webservice thanh cong
				System.out.println("Bill Chuyen di Thanh Cong:");
				String xmlresponse = resqObject[1].toString().trim();
				//System.out.println("Pay Bill Thanh Cong Response:");
				//System.out.println(xmlresponse);
				CLog.writeLog("Bill Chuyen di Thanh Cong " + xmlresponse);

				//----Check Authorization thanh cong 
				Document payBillResDoc = DemoApp.loadXMLFromString(xmlresponse);
				String finalResNodeName = payBillResDoc.getElementsByTagName("result").item(0).getChildNodes().item(0).getChildNodes().item(0).getNodeName().trim();
				System.out.println("Co fai Node Result " + finalResNodeName);
				String finalResNodeValue = payBillResDoc.getElementsByTagName("result").item(0).getChildNodes().item(0).getChildNodes().item(0).getTextContent().trim();
				System.out.println("Gia tri Node Result " + finalResNodeValue);

				if (finalResNodeValue.equals("0")) 
				{
					System.out.println("Thanh Toan Bill SUCCESS, return " + mySuccReturn);
					return mySuccReturn;
				}
				else   
				{
					System.out.println("Thanh Toan Bill THAT BAI " + finalResNodeValue + " return " + myFailReturn);
					return myFailReturn;
				}

				//return true;
			} 
			else {
				System.err.println("Pay Bill KHONG thanh cong, ma loi:" + resqObject[0].toString() + " return " + myFailReturn);
				return myFailReturn;
			}
		} 
		else {
			System.err.println("Loi he thong, khong goi duoc webservice, return " + myFailReturn);
			return myFailReturn;
		}
	}

	public static Document loadXMLFromString(String xml) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		return builder.parse(is);
	}

	public static String toXMLFromDoc(Document newDoc) throws Exception{
		DOMSource domSource = new DOMSource(newDoc);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		StringWriter sw = new StringWriter();
		StreamResult sr = new StreamResult(sw);
		transformer.transform(domSource, sr);

		return sw.toString().trim();
		//System.out.println(sw.toString());  
	}
	
	public void loadPara()
	{
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("webservicecfg.properties");

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			System.out.println(prop.getProperty("bill_url"));
			this.urlws = prop.getProperty("bill_url").trim();
			System.out.println(prop.getProperty("bill_accesskey"));
			this._accesskey = prop.getProperty("bill_accesskey");
			System.out.println(prop.getProperty("bill_partnercode"));
			this._partnercode = prop.getProperty("bill_partnercode").trim();
			System.out.println(prop.getProperty("bill_signature"));
			this._signature = prop.getProperty("bill_signature").trim();
		} 
		catch (Exception ex) {
			ex.getMessage();
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
					e.getMessage();
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			DemoApp demo = new DemoApp();
			if (demo.GetList())
			{	
				if (demo.QueryBill())
					demo.PayBill();
			}
		} 
		catch (Exception ex) {
			ex.printStackTrace();        	
			Logger.getLogger(DemoApp.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
