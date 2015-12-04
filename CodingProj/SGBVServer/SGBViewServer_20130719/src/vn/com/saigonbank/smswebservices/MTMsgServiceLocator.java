/**
 * MTMsgServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package vn.com.saigonbank.smswebservices;

public class MTMsgServiceLocator extends org.apache.axis.client.Service implements vn.com.saigonbank.smswebservices.MTMsgService {

    public MTMsgServiceLocator() {
    }


    public MTMsgServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MTMsgServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for MTMsgServiceSoap
    private java.lang.String MTMsgServiceSoap_address = "http://192.168.8.102:8179/MTMsgService.asmx";

    public java.lang.String getMTMsgServiceSoapAddress() {
        return MTMsgServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MTMsgServiceSoapWSDDServiceName = "MTMsgServiceSoap";

    public java.lang.String getMTMsgServiceSoapWSDDServiceName() {
        return MTMsgServiceSoapWSDDServiceName;
    }

    public void setMTMsgServiceSoapWSDDServiceName(java.lang.String name) {
        MTMsgServiceSoapWSDDServiceName = name;
    }

    public vn.com.saigonbank.smswebservices.MTMsgServiceSoap getMTMsgServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MTMsgServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMTMsgServiceSoap(endpoint);
    }

    public vn.com.saigonbank.smswebservices.MTMsgServiceSoap getMTMsgServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            vn.com.saigonbank.smswebservices.MTMsgServiceSoapStub _stub = new vn.com.saigonbank.smswebservices.MTMsgServiceSoapStub(portAddress, this);
            _stub.setPortName(getMTMsgServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMTMsgServiceSoapEndpointAddress(java.lang.String address) {
        MTMsgServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (vn.com.saigonbank.smswebservices.MTMsgServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                vn.com.saigonbank.smswebservices.MTMsgServiceSoapStub _stub = new vn.com.saigonbank.smswebservices.MTMsgServiceSoapStub(new java.net.URL(MTMsgServiceSoap_address), this);
                _stub.setPortName(getMTMsgServiceSoapWSDDServiceName());
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
        if ("MTMsgServiceSoap".equals(inputPortName)) {
            return getMTMsgServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://saigonbank.com.vn/smswebservices", "MTMsgService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://saigonbank.com.vn/smswebservices", "MTMsgServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("MTMsgServiceSoap".equals(portName)) {
            setMTMsgServiceSoapEndpointAddress(address);
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
