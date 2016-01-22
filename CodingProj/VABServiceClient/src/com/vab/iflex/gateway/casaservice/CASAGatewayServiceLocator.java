/**
 * CASAGatewayServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.vab.iflex.gateway.casaservice;

public class CASAGatewayServiceLocator extends org.apache.axis.client.Service{

    public CASAGatewayServiceLocator() {
    }


    public CASAGatewayServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CASAGatewayServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CASAGatewayPort
    private java.lang.String CASAGatewayPort_address = "http://172.30.2.243:7001/FCCGateway/CASAGatewayService";

    public java.lang.String getCASAGatewayPortAddress() {
        return CASAGatewayPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CASAGatewayPortWSDDServiceName = "CASAGatewayPort";

    public java.lang.String getCASAGatewayPortWSDDServiceName() {
        return CASAGatewayPortWSDDServiceName;
    }

    public void setCASAGatewayPortWSDDServiceName(java.lang.String name) {
        CASAGatewayPortWSDDServiceName = name;
    }

    public com.vab.iflex.gateway.casaservice.VABGatewayWSI getCASAGatewayPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CASAGatewayPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCASAGatewayPort(endpoint);
    }

    public com.vab.iflex.gateway.casaservice.VABGatewayWSI getCASAGatewayPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.vab.iflex.gateway.casaservice.CASAGatewayPortBindingStub _stub = new com.vab.iflex.gateway.casaservice.CASAGatewayPortBindingStub(portAddress, this);
            _stub.setPortName(getCASAGatewayPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCASAGatewayPortEndpointAddress(java.lang.String address) {
        CASAGatewayPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.vab.iflex.gateway.casaservice.VABGatewayWSI.class.isAssignableFrom(serviceEndpointInterface)) {
                com.vab.iflex.gateway.casaservice.CASAGatewayPortBindingStub _stub = new com.vab.iflex.gateway.casaservice.CASAGatewayPortBindingStub(new java.net.URL(CASAGatewayPort_address), this);
                _stub.setPortName(getCASAGatewayPortWSDDServiceName());
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
        if ("CASAGatewayPort".equals(inputPortName)) {
            return getCASAGatewayPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://gateway.iflex.vab.com/", "CASAGatewayService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://gateway.iflex.vab.com/", "CASAGatewayPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CASAGatewayPort".equals(portName)) {
            setCASAGatewayPortEndpointAddress(address);
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
