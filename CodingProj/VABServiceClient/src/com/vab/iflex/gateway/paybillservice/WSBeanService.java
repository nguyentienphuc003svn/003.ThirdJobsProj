/**
 * WSBeanService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.vab.iflex.gateway.paybillservice;

public interface WSBeanService extends javax.xml.rpc.Service {
    public java.lang.String getWSBeanAddress();

    public com.vab.iflex.gateway.paybillservice.WSBean getWSBean() throws javax.xml.rpc.ServiceException;

    public com.vab.iflex.gateway.paybillservice.WSBean getWSBean(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
