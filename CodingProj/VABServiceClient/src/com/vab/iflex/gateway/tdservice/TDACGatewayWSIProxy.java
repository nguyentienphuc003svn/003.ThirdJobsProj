package com.vab.iflex.gateway.tdservice;

public class TDACGatewayWSIProxy implements com.vab.iflex.gateway.tdservice.TDACGatewayWSI {
  private String _endpoint = null;
  private com.vab.iflex.gateway.tdservice.TDACGatewayWSI tDACGatewayWSI = null;
  
  public TDACGatewayWSIProxy() {
    _initTDACGatewayWSIProxy();
  }
  
  public TDACGatewayWSIProxy(String endpoint) {
    _endpoint = endpoint;
    _initTDACGatewayWSIProxy();
  }
  
  private void _initTDACGatewayWSIProxy() {
    try {
      tDACGatewayWSI = (new com.vab.iflex.gateway.tdservice.TDACGatewayServiceLocator()).getTDACGatewayPort();
      if (tDACGatewayWSI != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)tDACGatewayWSI)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)tDACGatewayWSI)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (tDACGatewayWSI != null)
      ((javax.xml.rpc.Stub)tDACGatewayWSI)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.vab.iflex.gateway.tdservice.TDACGatewayWSI getTDACGatewayWSI() {
    if (tDACGatewayWSI == null)
      _initTDACGatewayWSIProxy();
    return tDACGatewayWSI;
  }
  
  public java.lang.String openTDAccount(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException{
    if (tDACGatewayWSI == null)
      _initTDACGatewayWSIProxy();
    return tDACGatewayWSI.openTDAccount(arg0, arg1);
  }
  
  public java.lang.String liquidTDAccount(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException{
    if (tDACGatewayWSI == null)
      _initTDACGatewayWSIProxy();
    return tDACGatewayWSI.liquidTDAccount(arg0, arg1);
  }
  
  
}