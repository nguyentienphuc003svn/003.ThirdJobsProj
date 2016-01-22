/**
 * TDACGatewayService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.vab.iflex.gateway.tdservice;

public interface TDACGatewayService extends javax.xml.rpc.Service {
    public java.lang.String getTDACGatewayPortAddress();

    public com.vab.iflex.gateway.tdservice.TDACGatewayWSI getTDACGatewayPort() throws javax.xml.rpc.ServiceException;

    public com.vab.iflex.gateway.tdservice.TDACGatewayWSI getTDACGatewayPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
