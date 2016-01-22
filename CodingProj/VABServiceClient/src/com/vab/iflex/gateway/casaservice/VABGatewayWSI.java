/**
 * CASAGatewayWSI.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.vab.iflex.gateway.casaservice;

public interface VABGatewayWSI extends java.rmi.Remote {
    public java.lang.String CasaTransfer(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException;
    public java.lang.String AmountBlock(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException;
    public java.lang.String AmountUnblock(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException;
    public java.lang.String CASAStatement(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException;
}
