/**
 * MTMsgServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package vn.com.saigonbank.smswebservices;

public interface MTMsgServiceSoap extends java.rmi.Remote {
    public int send(java.lang.String userID, java.lang.String message, java.lang.String serviceID, java.lang.String commandCode, java.lang.String messageType, java.lang.String requestID, java.lang.String totalMessage, java.lang.String messageIndex, java.lang.String isMore, java.lang.String contentType) throws java.rmi.RemoteException;
    public int sendEx(java.lang.String userID, java.lang.String message, java.lang.String commandCode) throws java.rmi.RemoteException;
    public int sendExTest(java.lang.String userID, java.lang.String message, java.lang.String commandCode) throws java.rmi.RemoteException;
    public vn.com.saigonbank.smswebservices.ResponseCode sendEx4Topup(java.lang.String smsID, java.lang.String originalSmsID, java.lang.String userID, java.lang.String datetime, java.lang.String message, java.lang.String destPort, java.lang.String totalMessage, java.lang.String messageIndex, java.lang.String isMore) throws java.rmi.RemoteException;
}
