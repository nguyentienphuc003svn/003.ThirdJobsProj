/**
 * MO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package _42._1._168._192;

public class MO  implements java.io.Serializable {
    private java.lang.String sequenceNumber;

    private java.lang.String senderNumber;

    private java.lang.String serviceNumber;

    private java.lang.String commandCode;

    private java.lang.String info;

    private java.lang.String MO_Time;

    public MO() {
    }

    public MO(
           java.lang.String sequenceNumber,
           java.lang.String senderNumber,
           java.lang.String serviceNumber,
           java.lang.String commandCode,
           java.lang.String info,
           java.lang.String MO_Time) {
           this.sequenceNumber = sequenceNumber;
           this.senderNumber = senderNumber;
           this.serviceNumber = serviceNumber;
           this.commandCode = commandCode;
           this.info = info;
           this.MO_Time = MO_Time;
    }


    /**
     * Gets the sequenceNumber value for this MO.
     * 
     * @return sequenceNumber
     */
    public java.lang.String getSequenceNumber() {
        return sequenceNumber;
    }


    /**
     * Sets the sequenceNumber value for this MO.
     * 
     * @param sequenceNumber
     */
    public void setSequenceNumber(java.lang.String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }


    /**
     * Gets the senderNumber value for this MO.
     * 
     * @return senderNumber
     */
    public java.lang.String getSenderNumber() {
        return senderNumber;
    }


    /**
     * Sets the senderNumber value for this MO.
     * 
     * @param senderNumber
     */
    public void setSenderNumber(java.lang.String senderNumber) {
        this.senderNumber = senderNumber;
    }


    /**
     * Gets the serviceNumber value for this MO.
     * 
     * @return serviceNumber
     */
    public java.lang.String getServiceNumber() {
        return serviceNumber;
    }


    /**
     * Sets the serviceNumber value for this MO.
     * 
     * @param serviceNumber
     */
    public void setServiceNumber(java.lang.String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }


    /**
     * Gets the commandCode value for this MO.
     * 
     * @return commandCode
     */
    public java.lang.String getCommandCode() {
        return commandCode;
    }


    /**
     * Sets the commandCode value for this MO.
     * 
     * @param commandCode
     */
    public void setCommandCode(java.lang.String commandCode) {
        this.commandCode = commandCode;
    }


    /**
     * Gets the info value for this MO.
     * 
     * @return info
     */
    public java.lang.String getInfo() {
        return info;
    }


    /**
     * Sets the info value for this MO.
     * 
     * @param info
     */
    public void setInfo(java.lang.String info) {
        this.info = info;
    }


    /**
     * Gets the MO_Time value for this MO.
     * 
     * @return MO_Time
     */
    public java.lang.String getMO_Time() {
        return MO_Time;
    }


    /**
     * Sets the MO_Time value for this MO.
     * 
     * @param MO_Time
     */
    public void setMO_Time(java.lang.String MO_Time) {
        this.MO_Time = MO_Time;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MO)) return false;
        MO other = (MO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sequenceNumber==null && other.getSequenceNumber()==null) || 
             (this.sequenceNumber!=null &&
              this.sequenceNumber.equals(other.getSequenceNumber()))) &&
            ((this.senderNumber==null && other.getSenderNumber()==null) || 
             (this.senderNumber!=null &&
              this.senderNumber.equals(other.getSenderNumber()))) &&
            ((this.serviceNumber==null && other.getServiceNumber()==null) || 
             (this.serviceNumber!=null &&
              this.serviceNumber.equals(other.getServiceNumber()))) &&
            ((this.commandCode==null && other.getCommandCode()==null) || 
             (this.commandCode!=null &&
              this.commandCode.equals(other.getCommandCode()))) &&
            ((this.info==null && other.getInfo()==null) || 
             (this.info!=null &&
              this.info.equals(other.getInfo()))) &&
            ((this.MO_Time==null && other.getMO_Time()==null) || 
             (this.MO_Time!=null &&
              this.MO_Time.equals(other.getMO_Time())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getSequenceNumber() != null) {
            _hashCode += getSequenceNumber().hashCode();
        }
        if (getSenderNumber() != null) {
            _hashCode += getSenderNumber().hashCode();
        }
        if (getServiceNumber() != null) {
            _hashCode += getServiceNumber().hashCode();
        }
        if (getCommandCode() != null) {
            _hashCode += getCommandCode().hashCode();
        }
        if (getInfo() != null) {
            _hashCode += getInfo().hashCode();
        }
        if (getMO_Time() != null) {
            _hashCode += getMO_Time().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://192.168.1.42/", "MO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequenceNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://192.168.1.42/", "SequenceNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("senderNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://192.168.1.42/", "SenderNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://192.168.1.42/", "ServiceNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("commandCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://192.168.1.42/", "CommandCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("info");
        elemField.setXmlName(new javax.xml.namespace.QName("http://192.168.1.42/", "Info"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MO_Time");
        elemField.setXmlName(new javax.xml.namespace.QName("http://192.168.1.42/", "MO_Time"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
