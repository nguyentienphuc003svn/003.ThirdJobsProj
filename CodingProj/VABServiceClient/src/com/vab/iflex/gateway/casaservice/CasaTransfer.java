
package com.vab.iflex.gateway.casaservice;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Minh Nhut
 */
public class CasaTransfer {

	private String consumerName = "EBANKING";
	private String wsurladdress = "http://localhost:7001/FCCGateway/CASAGatewayService";
	private String prdCode = "IN07";
	
	private int timeout = 120000;
	private final CASAGatewayServiceLocator serviceLocator;
	private CASAGatewayPortBindingStub port;

	public String getWsUrlAddress() {
		return wsurladdress;
	}

	public void setWsUrlAddress(String wsurladdress) {
		this.wsurladdress = wsurladdress;
	}

	//---------------------------------------\\
	public void initGateway() throws Exception {
		serviceLocator.setCASAGatewayPortEndpointAddress(this.wsurladdress);
		port = (CASAGatewayPortBindingStub) serviceLocator.getCASAGatewayPort();
		port.setTimeout(timeout);
	}

	public CasaTransfer() throws Exception {
		serviceLocator = new CASAGatewayServiceLocator();
	}

	public CasaTransfer(String timeOut) throws Exception {
		this.timeout = Integer.parseInt(timeOut);
		serviceLocator = new CASAGatewayServiceLocator();
	}

	public CasaTransfer(String consumerName, String wsUrlAddress) throws Exception {
		this.consumerName = consumerName;
		//this.productCode = productCode;
		this.wsurladdress = wsUrlAddress;
		this.serviceLocator = new CASAGatewayServiceLocator();
		port = (CASAGatewayPortBindingStub) serviceLocator.getCASAGatewayPort();
	}

	public CasaTransfer(String GRG, Integer Flag) throws Exception {
		//Load Config
		this.loadPara();
		
		this.serviceLocator = new CASAGatewayServiceLocator();
		serviceLocator.setCASAGatewayPortEndpointAddress(this.wsurladdress);
		port = (CASAGatewayPortBindingStub) serviceLocator.getCASAGatewayPort();
		port.setTimeout(timeout);
	}
	
	public CasaTransfer(String consumerName, String wsUrlAddress, String timeOut) throws Exception {
		//Load Config
		this.loadPara();
		
		this.consumerName = consumerName;
		//this.productCode = productCode;
		this.timeout = Integer.parseInt(timeOut);
		this.wsurladdress = wsUrlAddress;
		this.serviceLocator = new CASAGatewayServiceLocator();
		serviceLocator.setCASAGatewayPortEndpointAddress(this.wsurladdress);
		port = (CASAGatewayPortBindingStub) serviceLocator.getCASAGatewayPort();
		port.setTimeout(timeout);
	}
	
	public String getPrdCode()
	{
		return this.prdCode.trim();
	}

	private String initInput(String prd, String brnAcctFrom, String acctFrom, String brnAcctTo, String acctTo, Double amount, String ccy, String narrative) throws Exception {
		CASATransferInput transferInputObj = new CASATransferInput();
		transferInputObj.setPrd(prd);
		transferInputObj.setBrn(brnAcctFrom);
		transferInputObj.setTxnbrn(brnAcctFrom);
		transferInputObj.setTxnacc(acctFrom);
		transferInputObj.setTxnamt(amount);
		transferInputObj.setTxnccy(ccy);
		transferInputObj.setOffsetbrn(brnAcctTo);
		transferInputObj.setOffsetacc(acctTo);
		transferInputObj.setOffsetccy(ccy);
		transferInputObj.setNarrative(narrative);
		//        transferInputObj.setCharge(new double[]{10000.0, 5000.0, 2000.0});

		return transferInputObj.toXML();
	}

	private String initInputGRG(String prd, String brnAcctFrom, String acctFrom, String brnAcctTo, String acctTo, Double amount, String ccy, String narrative, String msgid) throws Exception {
		CASATransferInput transferInputObj = new CASATransferInput();
		transferInputObj.setPrd(prd);
		transferInputObj.setBrn(brnAcctFrom);
		transferInputObj.setTxnbrn(brnAcctFrom);
		transferInputObj.setTxnacc(acctFrom);
		transferInputObj.setTxnamt(amount);
		transferInputObj.setTxnccy(ccy);
		transferInputObj.setOffsetbrn(brnAcctTo);
		transferInputObj.setOffsetacc(acctTo);
		transferInputObj.setOffsetccy(ccy);
		transferInputObj.setNarrative(narrative);
		//        transferInputObj.setCharge(new double[]{10000.0, 5000.0, 2000.0});

		transferInputObj.setMsgid(msgid);//GRG Switch Msgid

		return transferInputObj.toXML();
	}

	private String initInputWithCharge(String prd, String brnAcctFrom, String acctFrom, Double amount, String ccy, String routeCode, String narrative) throws Exception {
		CASATransferWithChargeInput transferInputObj = new CASATransferWithChargeInput();
		transferInputObj.setPrd(prd);
		transferInputObj.setBrn(brnAcctFrom);
		transferInputObj.setOffsetbrn(brnAcctFrom);
		transferInputObj.setOffsetacc(acctFrom);
		transferInputObj.setOffsetamt(amount);
		transferInputObj.setOffsetccy(ccy);
		transferInputObj.setRoutecode(routeCode);
		transferInputObj.setNarrative(narrative);
		//        transferInputObj.setCharge(new double[]{10000.0, 2000.0});
		return transferInputObj.toXML();
	}

	private String initInputWithCharge(String prd, String brnCharge, String brnAcctFrom, String acctFrom, Double amount, Double[] charges, String ccy, String routeCode, String narrative) throws Exception {
		CASATransferWithChargeInput transferInputObj = new CASATransferWithChargeInput();
		transferInputObj.setPrd(prd);
		transferInputObj.setBrn(brnCharge); // Chi nhanh thu phi
		transferInputObj.setOffsetbrn(brnAcctFrom);
		transferInputObj.setOffsetacc(acctFrom);
		transferInputObj.setOffsetamt(amount);
		transferInputObj.setOffsetccy(ccy);
		transferInputObj.setRoutecode(routeCode);
		transferInputObj.setNarrative(narrative);
		transferInputObj.setCharge(charges);
		return transferInputObj.toXML();
	}

	public String[] casaToCasaTransfer(String prd, String brnAcctFrom, String acctFrom, String brnAcctTo, String acctTo, Double amount, String ccy, String narrative) throws Exception {

		String[] returnVal;
		String inputXmlStr = null;
		String xmlOutput = null;
		try {
			inputXmlStr = initInput(prd, brnAcctFrom, acctFrom, brnAcctTo, acctTo, amount, ccy, narrative);

			//fcc. Ko loi. Giao dich thanh cong. Phia CORE.
			//SUCCESS. Content: abc
			xmlOutput = port.CasaTransfer(consumerName, inputXmlStr);

			//Parse loi. Phia IB.
			CASATransferOutput transferOutputObj = CASATransferOutput.fromXML(xmlOutput);
			returnVal = new String[]{
					transferOutputObj.getResult().getStatus(),
					transferOutputObj.getResult().getErrors()[0].getEcode(),
					transferOutputObj.getResult().getErrors()[0].getEdesc(),
					transferOutputObj.getXref() != null ? transferOutputObj.getXref() : ""
			};
		} catch (Exception ex) {

			returnVal = new String[]{"FAILURE", ex.getMessage(), inputXmlStr, xmlOutput != null ? xmlOutput : "null"};
		}
		return returnVal;
	}

	public String[] casaToCasaTransferGRG(String prd, String brnAcctFrom, String acctFrom, String brnAcctTo, String acctTo, Double amount, String ccy, String narrative, String msgid) throws Exception {

		String[] returnVal;
		String inputXmlStr = null;
		String xmlOutput = null;
		try {
			inputXmlStr = initInputGRG(prd, brnAcctFrom, acctFrom, brnAcctTo, acctTo, amount, ccy, narrative, msgid);

			//fcc. Ko loi. Giao dich thanh cong. Phia CORE.
			//SUCCESS. Content: abc
			xmlOutput = port.CasaTransfer(consumerName, inputXmlStr);

			//Parse loi. Phia IB.
			CASATransferOutput transferOutputObj = CASATransferOutput.fromXML(xmlOutput);
			returnVal = new String[]{
					transferOutputObj.getResult().getStatus(),
					msgid,
					transferOutputObj.getResult().getErrors()[0].getEcode(),
					transferOutputObj.getResult().getErrors()[0].getEdesc(),
					transferOutputObj.getXref() != null ? transferOutputObj.getXref() : ""
			};
		} catch (Exception ex) {

			returnVal = new String[]{"FAILURE", ex.getMessage(), inputXmlStr, xmlOutput != null ? xmlOutput : "null"};
		}
		return returnVal;
	}

	public String[] casaTransferWithCharge(String prd, String brnAcctFrom, String acctFrom, Double amount, String ccy, String routeCode, String narrative) {
		String[] returnVal;
		String inputXmlStr = null;
		String xmlOutput = null;
		try {
			inputXmlStr = initInputWithCharge(prd, brnAcctFrom, acctFrom, amount, ccy, routeCode, narrative);
			xmlOutput = port.CasaTransfer(consumerName, inputXmlStr);
			CASATransferOutput transferOutputObj = CASATransferOutput.fromXML(xmlOutput);
			returnVal = new String[]{
					transferOutputObj.getResult().getStatus(),
					transferOutputObj.getResult().getErrors()[0].getEcode(),
					transferOutputObj.getResult().getErrors()[0].getEdesc(),
					transferOutputObj.getXref()
			};
		} catch (Exception ex) {
			returnVal = new String[]{"FAILURE", ex.getMessage(), inputXmlStr, xmlOutput != null ? xmlOutput : "null"};
		}
		return returnVal;
	}


	public String[] casaTransferWithCharge(String prd, String brnCharge, String brnAcctFrom, String acctFrom, Double amount, Double[] charges, String ccy, String routeCode, String narrative) {
		String[] returnVal;
		String inputXmlStr = null;
		String xmlOutput = null;
		try {
			inputXmlStr = initInputWithCharge(prd, brnCharge, brnAcctFrom, acctFrom, amount, charges, ccy, routeCode, narrative);
			xmlOutput = port.CasaTransfer(consumerName, inputXmlStr);
			CASATransferOutput transferOutputObj = CASATransferOutput.fromXML(xmlOutput);
			returnVal = new String[]{
					transferOutputObj.getResult().getStatus(),
					transferOutputObj.getResult().getErrors()[0].getEcode(),
					transferOutputObj.getResult().getErrors()[0].getEdesc(),
					transferOutputObj.getXref()
			};
		} catch (Exception ex) {
			returnVal = new String[]{"FAILURE", ex.getMessage(), inputXmlStr, xmlOutput != null ? xmlOutput : "null"};
		}
		return returnVal;
	}

	public String[] casaAmountBlock(AmountBlockInput abInputObj) throws Exception {
		String[] returnVal;
		String inputXmlStr = null;
		String xmlResponse = null;
		try {
			inputXmlStr = abInputObj.toXML();
			xmlResponse = port.AmountBlock(consumerName, inputXmlStr);
			AmountBlockOutput output = AmountBlockOutput.fromXML(xmlResponse);
			returnVal = new String[]{
					output.getResult().getStatus(),
					output.getResult().getErrors()[0].getEcode(),
					output.getResult().getErrors()[0].getEdesc(),
					output.getAmountblockno()
			};
		} catch (Exception ex) {
			returnVal = new String[]{"FAILURE", ex.getMessage(), inputXmlStr, xmlResponse};
		}
		return returnVal;
	}

	public String[] casaAmountBlock(String acctNo, Double amount, String remark) throws Exception {
		try {
			AmountBlockInput abInputObj = new AmountBlockInput();
			abInputObj.setAccount(acctNo);
			abInputObj.setAmount(amount);
			abInputObj.setRemarks(remark);
			return casaAmountBlock(abInputObj);
		} catch (Exception ex) {
			String[] returnVal = new String[]{"FAILURE", ex.getMessage(), "", ""};
			return returnVal;
		}
	}

	public String[] casaAmountUnBlock(AmountUnBlockInput amubInputObj) throws Exception {
		String[] returnVal;
		String inputXmlStr = null;
		String xmlResponse = null;
		try {
			inputXmlStr = amubInputObj.toXML();
			xmlResponse = port.AmountUnblock(consumerName, inputXmlStr);
			AmountBlockOutput output = AmountBlockOutput.fromXML(xmlResponse);
			returnVal = new String[]{
					output.getResult().getStatus(),
					output.getResult().getErrors()[0].getEcode(),
					output.getResult().getErrors()[0].getEdesc(),
					output.getAmountblockno()
			};
		} catch (Exception ex) {
			returnVal = new String[]{"FAILURE", ex.getMessage(), inputXmlStr, xmlResponse};
		}
		return returnVal;
	}

	public String[] casaAmountUnBlock(String acctNo, String amountBlockRefNo) throws Exception {
		try {
			AmountUnBlockInput amubInputObj = new AmountUnBlockInput();
			amubInputObj.setAccount(acctNo);
			amubInputObj.setAmountblockno(amountBlockRefNo);
			return casaAmountUnBlock(amubInputObj);
		} catch (Exception ex) {
			String[] returnVal = new String[]{"FAILURE", ex.getMessage(), "", ""};
			return returnVal;
		}
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
			System.out.println(prop.getProperty("deposit_url"));
			this.wsurladdress = prop.getProperty("deposit_url").trim();
			System.out.println(prop.getProperty("deposit_consumername"));
			this.consumerName = prop.getProperty("deposit_consumername");
			System.out.println(prop.getProperty("deposit_prdCode"));
			this.prdCode = prop.getProperty("deposit_prdCode").trim(); 

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
			CasaTransfer instance = new CasaTransfer("EBANKING", "http://192.168.31.200:7001/FCCGateway/CASAGatewayService", "600000");
			String[] out = instance.casaToCasaTransfer("IN07", "010", "459901001", 
					"020", "0200000085379000", 20000.0, "VND", "Test chuyen khoan");

			System.out.println("\n" + out[0]);
			System.out.println("\n" + out[1]);
			System.out.println("\n" + out[2]);
			System.out.println("\n" + out[3]);

		} 
		catch (Exception ex) {
			ex.printStackTrace();

		}
	}
}
