/**
 * SMSSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package _42._1._168._192;

public interface SMSSoap extends java.rmi.Remote {
    public java.lang.String sendSMS(_42._1._168._192.MO vMO) throws java.rmi.RemoteException;
    public java.lang.String sendTestSMS(java.lang.String phone, java.lang.String content) throws java.rmi.RemoteException;
}
