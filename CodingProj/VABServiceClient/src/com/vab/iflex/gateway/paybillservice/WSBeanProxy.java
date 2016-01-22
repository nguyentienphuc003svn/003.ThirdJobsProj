package com.vab.iflex.gateway.paybillservice;

public class WSBeanProxy implements com.vab.iflex.gateway.paybillservice.WSBean {
  private String _endpoint = null;
  private com.vab.iflex.gateway.paybillservice.WSBean wSBean = null;
  
  public WSBeanProxy() {
    _initWSBeanProxy();
  }
  
  public WSBeanProxy(String endpoint) {
    _endpoint = endpoint;
    _initWSBeanProxy();
  }
  
  private void _initWSBeanProxy() {
    try {
      wSBean = (new com.vab.iflex.gateway.paybillservice.WSBeanServiceLocator()).getWSBean();
      if (wSBean != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wSBean)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wSBean)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wSBean != null)
      ((javax.xml.rpc.Stub)wSBean)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.vab.iflex.gateway.paybillservice.WSBean getWSBean() {
    if (wSBean == null)
      _initWSBeanProxy();
    return wSBean;
  }
  
  public java.lang.String getString(java.lang.String str) throws java.rmi.RemoteException{
    if (wSBean == null)
      _initWSBeanProxy();
    return wSBean.getString(str);
  }
  
  public java.lang.Object[] callExecution(java.lang.String methodname, java.lang.String partnercode, java.lang.Object[] arrObjParas) throws java.rmi.RemoteException{
    if (wSBean == null)
      _initWSBeanProxy();
    return wSBean.callExecution(methodname, partnercode, arrObjParas);
  }
  
  
}