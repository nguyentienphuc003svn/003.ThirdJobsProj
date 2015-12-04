package vn.com.saigonbank.smswebservices;

public class MTMsgServiceSoapProxy implements vn.com.saigonbank.smswebservices.MTMsgServiceSoap {
  private String _endpoint = null;
  private vn.com.saigonbank.smswebservices.MTMsgServiceSoap mTMsgServiceSoap = null;
  
  public MTMsgServiceSoapProxy() {
    _initMTMsgServiceSoapProxy();
  }
  
  public MTMsgServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initMTMsgServiceSoapProxy();
  }
  
  private void _initMTMsgServiceSoapProxy() {
    try {
      mTMsgServiceSoap = (new vn.com.saigonbank.smswebservices.MTMsgServiceLocator()).getMTMsgServiceSoap();
      if (mTMsgServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)mTMsgServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)mTMsgServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (mTMsgServiceSoap != null)
      ((javax.xml.rpc.Stub)mTMsgServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public vn.com.saigonbank.smswebservices.MTMsgServiceSoap getMTMsgServiceSoap() {
    if (mTMsgServiceSoap == null)
      _initMTMsgServiceSoapProxy();
    return mTMsgServiceSoap;
  }
  
  public int send(java.lang.String userID, java.lang.String message, java.lang.String serviceID, java.lang.String commandCode, java.lang.String messageType, java.lang.String requestID, java.lang.String totalMessage, java.lang.String messageIndex, java.lang.String isMore, java.lang.String contentType) throws java.rmi.RemoteException{
    if (mTMsgServiceSoap == null)
      _initMTMsgServiceSoapProxy();
    return mTMsgServiceSoap.send(userID, message, serviceID, commandCode, messageType, requestID, totalMessage, messageIndex, isMore, contentType);
  }
  
  public int sendEx(java.lang.String userID, java.lang.String message, java.lang.String commandCode) throws java.rmi.RemoteException{
    if (mTMsgServiceSoap == null)
      _initMTMsgServiceSoapProxy();
    return mTMsgServiceSoap.sendEx(userID, message, commandCode);
  }
  
  public int sendExTest(java.lang.String userID, java.lang.String message, java.lang.String commandCode) throws java.rmi.RemoteException{
    if (mTMsgServiceSoap == null)
      _initMTMsgServiceSoapProxy();
    return mTMsgServiceSoap.sendExTest(userID, message, commandCode);
  }
  
  public vn.com.saigonbank.smswebservices.ResponseCode sendEx4Topup(java.lang.String smsID, java.lang.String originalSmsID, java.lang.String userID, java.lang.String datetime, java.lang.String message, java.lang.String destPort, java.lang.String totalMessage, java.lang.String messageIndex, java.lang.String isMore) throws java.rmi.RemoteException{
    if (mTMsgServiceSoap == null)
      _initMTMsgServiceSoapProxy();
    return mTMsgServiceSoap.sendEx4Topup(smsID, originalSmsID, userID, datetime, message, destPort, totalMessage, messageIndex, isMore);
  }
  
  
}