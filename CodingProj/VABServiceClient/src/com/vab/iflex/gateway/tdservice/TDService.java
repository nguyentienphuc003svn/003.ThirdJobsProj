/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vab.iflex.gateway.tdservice;

import com.vab.iflex.gateway.dto.TDOpenRequestDTO;
import com.vab.iflex.gateway.dto.TDOpenResponseDTO;
import com.vab.iflex.gateway.dto.LiquidTDRequestDTO;
import com.vab.iflex.gateway.dto.LiquidTDResponseDTO;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TDService {

    private String consumerName = "EBANKING";
    private String wsurladdress = "http://192.168.31.200:7001/FCCGateway/TDACGatewayService?wsdl";
    private int timeout = 120000;
    private TDACGatewayServiceLocator serviceLocator;
    private TDACGatewayPortBindingStub port;

    public TDService() {
        try {
            serviceLocator = new TDACGatewayServiceLocator();
            serviceLocator.setTDACGatewayPortEndpointAddress(this.wsurladdress);
            port = (TDACGatewayPortBindingStub) serviceLocator.getTDACGatewayPort();
            port.setTimeout(timeout);
        } catch (Exception ex) {
            Logger.getLogger(TDService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public TDService(String _consumerName, String _wsurladdress, String _timeout) {
        try {
            this.consumerName = _consumerName;
            this.wsurladdress = _wsurladdress;
            this.timeout = Integer.parseInt(_timeout);
            serviceLocator = new TDACGatewayServiceLocator();
            serviceLocator.setTDACGatewayPortEndpointAddress(this.wsurladdress);
            port = (TDACGatewayPortBindingStub) serviceLocator.getTDACGatewayPort();
            port.setTimeout(timeout);
        } catch (Exception ex) {
            Logger.getLogger(TDService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public TDOpenResponseDTO OpenTDAccount(TDOpenRequestDTO tdopenrequest) {
        try {
            String xmlRequest = null;
            String xmlResponse = null;
            TDOpenResponseDTO tdopenresponse = new TDOpenResponseDTO();

            xmlRequest = tdopenrequest.toXML();
            xmlResponse = port.openTDAccount(consumerName, xmlRequest);
            tdopenresponse = tdopenresponse.fromXML(xmlResponse);
            return tdopenresponse;
        } catch (Exception ex) {
            Logger.getLogger(TDService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public LiquidTDResponseDTO LiquidTDAccount(LiquidTDRequestDTO p_request) {
        try {
            String xmlRequest = null;
            String xmlResponse = null;
            LiquidTDResponseDTO l_response = new LiquidTDResponseDTO();
            xmlRequest = p_request.toXML();
            xmlResponse = port.liquidTDAccount(consumerName, xmlRequest);
            l_response = l_response.fromXML(xmlResponse);
            return l_response;
        } catch (Exception ex) {
            Logger.getLogger(TDService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static void main(String[] args) throws Exception {
        testOpen();
    }

    public static void testOpen() {
        try {
            TDService tdservice = new TDService();
            TDOpenRequestDTO request = new TDOpenRequestDTO();            
            TDOpenResponseDTO tdopenresponse = new TDOpenResponseDTO();
            
            LiquidTDResponseDTO liquidTDResponseDTO = new LiquidTDResponseDTO();
            
            //    Test OpenTD

            //        request.custno="020006713";
            //        request.brn="020";
            //        request.accls="TV0001";
            //        request.ccy="VND";
            //        request.autoroll=false;
            //        request.rolltype="I";
            //        request.tdamt=1900;
            //        request.offbrn="020";
            //        request.offacc="0203000053819000";
            //        request.bookaccbrn="020";
            //        request.bookacc="0203000053819000";
            //        request.liqdbrn="020";
            //        request.liqdacc="0203000053819000";
            //N: khong tai tuc
            //P:Tai tuc goc
            //I: Tai tuc cả goc va lai
            request.custno = "303001256";
            request.brn = "303";
            request.accls = "TV00W1";
            request.ccy = "VND";
            request.autoroll = true;
            request.rolltype = "I";
            request.tdamt = 1540000 + "";
            request.offbrn = "303";
            request.offacc = "3033000002429000";
            request.bookaccbrn = "303";
            request.bookacc = "3033000002429000";
            request.liqdbrn = "303";
            request.liqdacc = "3033000002429000";
            
            System.out.println("\nXML Resquest :"+request.toXML());
            
            //tdservice.OpenTDAccount(request);
            tdopenresponse=tdservice.OpenTDAccount(request);

            System.out.println("\nXML Response :"+tdopenresponse.toXML());
            System.out.println("Finished");
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(TDService.class.getName()).log(Level.SEVERE, null, ex);
            
            System.out.println("Terminak Deposit Test Error");
            ex.printStackTrace();
        }
    }
    
    private static void testLiquid() {
        TDService tdservice = new TDService();
        
        LiquidTDRequestDTO request = new LiquidTDRequestDTO();
        request.brn = "020";
        request.savingaccno = "0205000147649000";
        request.savingbrn = "020";
        request.savingccy = "VND";
        request.tdac = "0202000240220100";
        tdservice.LiquidTDAccount(request);
    }
}
