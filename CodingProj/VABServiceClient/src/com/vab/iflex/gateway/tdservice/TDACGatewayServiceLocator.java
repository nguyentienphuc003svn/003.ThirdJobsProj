/**
 * TDACGatewayServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.vab.iflex.gateway.tdservice;

public class TDACGatewayServiceLocator extends org.apache.axis.client.Service implements com.vab.iflex.gateway.tdservice.TDACGatewayService {

    public TDACGatewayServiceLocator() {
    }


    public TDACGatewayServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TDACGatewayServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TDACGatewayPort
    private java.lang.String TDACGatewayPort_address = "http://192.168.28.45:7001/FCCGateway/TDACGatewayService";

    public java.lang.String getTDACGatewayPortAddress() {
        return TDACGatewayPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TDACGatewayPortWSDDServiceName = "TDACGatewayPort";

    public java.lang.String getTDACGatewayPortWSDDServiceName() {
        return TDACGatewayPortWSDDServiceName;
    }

    public void setTDACGatewayPortWSDDServiceName(java.lang.String name) {
        TDACGatewayPortWSDDServiceName = name;
    }

    public com.vab.iflex.gateway.tdservice.TDACGatewayWSI getTDACGatewayPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TDACGatewayPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTDACGatewayPort(endpoint);
    }

    public com.vab.iflex.gateway.tdservice.TDACGatewayWSI getTDACGatewayPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.vab.iflex.gateway.tdservice.TDACGatewayPortBindingStub _stub = new com.vab.iflex.gateway.tdservice.TDACGatewayPortBindingStub(portAddress, this);
            _stub.setPortName(getTDACGatewayPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTDACGatewayPortEndpointAddress(java.lang.String address) {
        TDACGatewayPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.vab.iflex.gateway.tdservice.TDACGatewayWSI.class.isAssignableFrom(serviceEndpointInterface)) {
                com.vab.iflex.gateway.tdservice.TDACGatewayPortBindingStub _stub = new com.vab.iflex.gateway.tdservice.TDACGatewayPortBindingStub(new java.net.URL(TDACGatewayPort_address), this);
                _stub.setPortName(getTDACGatewayPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("TDACGatewayPort".equals(inputPortName)) {
            return getTDACGatewayPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://gateway.iflex.vab.com/", "TDACGatewayService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://gateway.iflex.vab.com/", "TDACGatewayPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TDACGatewayPort".equals(portName)) {
            setTDACGatewayPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
