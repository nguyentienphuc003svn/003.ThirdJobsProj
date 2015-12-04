package _42._1._168._192;

public class SMSSoapProxy implements _42._1._168._192.SMSSoap {
  private String _endpoint = null;
  private _42._1._168._192.SMSSoap sMSSoap = null;
  
  public SMSSoapProxy() {
    _initSMSSoapProxy();
  }
  
  public SMSSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initSMSSoapProxy();
  }
  
  private void _initSMSSoapProxy() {
    try {
      sMSSoap = (new _42._1._168._192.SMSLocator()).getSMSSoap();
      if (sMSSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sMSSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sMSSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sMSSoap != null)
      ((javax.xml.rpc.Stub)sMSSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public _42._1._168._192.SMSSoap getSMSSoap() {
    if (sMSSoap == null)
      _initSMSSoapProxy();
    return sMSSoap;
  }
  
  public java.lang.String sendSMS(_42._1._168._192.MO vMO) throws java.rmi.RemoteException{
    if (sMSSoap == null)
      _initSMSSoapProxy();
    return sMSSoap.sendSMS(vMO);
  }
  
  public java.lang.String sendTestSMS(java.lang.String phone, java.lang.String content) throws java.rmi.RemoteException{
    if (sMSSoap == null)
      _initSMSSoapProxy();
    return sMSSoap.sendTestSMS(phone, content);
  }
  
  
}