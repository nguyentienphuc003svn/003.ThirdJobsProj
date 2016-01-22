/**
 * WSBeanServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.vab.iflex.gateway.paybillservice;

public class WSBeanServiceLocator extends org.apache.axis.client.Service implements com.vab.iflex.gateway.paybillservice.WSBeanService {

	public WSBeanServiceLocator() {
	}


	public WSBeanServiceLocator(org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public WSBeanServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	// Use to get a proxy class for WSBean
	private java.lang.String WSBean_address = "http://localhost:8084/ebankwebservice/services/WSBean";

	public java.lang.String getWSBeanAddress() {
		return WSBean_address;
	}

	// The WSDD service name defaults to the port name.
	private java.lang.String WSBeanWSDDServiceName = "WSBean";

	public java.lang.String getWSBeanWSDDServiceName() {
		return WSBeanWSDDServiceName;
	}

	public void setWSBeanWSDDServiceName(java.lang.String name) {
		WSBeanWSDDServiceName = name;
	}

	public com.vab.iflex.gateway.paybillservice.WSBean getWSBean() throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(WSBean_address);
		}
		catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getWSBean(endpoint);
	}

	public com.vab.iflex.gateway.paybillservice.WSBean getWSBean(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
		try {
			com.vab.iflex.gateway.paybillservice.WSBeanSoapBindingStub _stub = new com.vab.iflex.gateway.paybillservice.WSBeanSoapBindingStub(portAddress, this);
			_stub.setPortName(getWSBeanWSDDServiceName());
			return _stub;
		}
		catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setWSBeanEndpointAddress(java.lang.String address) {
		WSBean_address = address;
	}

	/**
	 * For the given interface, get the stub implementation.
	 * If this service has no port for the given interface,
	 * then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		try {
			if (com.vab.iflex.gateway.paybillservice.WSBean.class.isAssignableFrom(serviceEndpointInterface)) {
				com.vab.iflex.gateway.paybillservice.WSBeanSoapBindingStub _stub = new com.vab.iflex.gateway.paybillservice.WSBeanSoapBindingStub(new java.net.URL(WSBean_address), this);
				_stub.setPortName(getWSBeanWSDDServiceName());
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
		if ("WSBean".equals(inputPortName)) {
			return getWSBean();
		}
		else  {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName("http://vn.com.phn", "WSBeanService");
	}

	private java.util.HashSet ports = null;

	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new javax.xml.namespace.QName("http://vn.com.phn", "WSBean"));
		}
		return ports.iterator();
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

		if ("WSBean".equals(portName)) {
			setWSBeanEndpointAddress(address);
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
