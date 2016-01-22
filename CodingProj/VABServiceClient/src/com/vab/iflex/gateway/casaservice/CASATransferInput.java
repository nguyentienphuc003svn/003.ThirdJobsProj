/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vab.iflex.gateway.casaservice;
 
import com.thoughtworks.xstream.XStream;

/**
 *
 * @author LêMinhNhựt
 */
public class CASATransferInput extends Input {
    private String prd;//product code
    private String brn;//transaction branch
    private String txnbrn;//transaction account branch
    private String txnacc;//transaction account
    private Double txnamt; //transaction account amount
    private String txnccy;//transaction account currency
    private String offsetbrn;//offset branch
    private String offsetacc; //offset account
    private String offsetccy;
    private String narrative;//transaction narrative
    
    public String toXML()
    {
        XStream xstream = new XStream();
        xstream.alias("casatransfer", CASATransferInput.class);
        xstream.alias("charge", double.class);
        xstream.addImplicitCollection(CASATransferInput.class, "charge");
        return xstream.toXML(this);
    }
    public static String toXML(CASATransferInput input)
    {
        return input.toXML();
    }
    /**
     * @return the prd
     */
    public String getPrd() {
        return prd;
    }

    /**
     * @param prd the prd to set
     */
    public void setPrd(String prd) {
        this.prd = prd;
    }

    /**
     * @return the brn
     */
    public String getBrn() {
        return brn;
    }

    /**
     * @param brn the brn to set
     */
    public void setBrn(String brn) {
        this.brn = brn;
    }

    /**
     * @return the txnbrn
     */
    public String getTxnbrn() {
        return txnbrn;
    }

    /**
     * @param txnbrn the txnbrn to set
     */
    public void setTxnbrn(String txnbrn) {
        this.txnbrn = txnbrn;
    }

    /**
     * @return the txnacc
     */
    public String getTxnacc() {
        return txnacc;
    }

    /**
     * @param txnacc the txnacc to set
     */
    public void setTxnacc(String txnacc) {
        this.txnacc = txnacc;
    }

    /**
     * @return the txnamt
     */
    public Double getTxnamt() {
        return txnamt;
    }

    /**
     * @param txnamt the txnamt to set
     */
    public void setTxnamt(Double txnamt) {
        this.txnamt = txnamt;
    }

    /**
     * @return the txnccy
     */
    public String getTxnccy() {
        return txnccy;
    }

    /**
     * @param txnccy the txnccy to set
     */
    public void setTxnccy(String txnccy) {
        this.txnccy = txnccy;
    }

    /**
     * @return the offsetbrn
     */
    public String getOffsetbrn() {
        return offsetbrn;
    }

    /**
     * @param offsetbrn the offsetbrn to set
     */
    public void setOffsetbrn(String offsetbrn) {
        this.offsetbrn = offsetbrn;
    }

    /**
     * @return the offsetacc
     */
    public String getOffsetacc() {
        return offsetacc;
    }

    /**
     * @param offsetacc the offsetacc to set
     */
    public void setOffsetacc(String offsetacc) {
        this.offsetacc = offsetacc;
    }

    /**
     * @return the narrative
     */
    public String getNarrative() {
        return narrative;
    }

    /**
     * @param narrative the narrative to set
     */
    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public String getOffsetccy() {
        return offsetccy;
    }

    public void setOffsetccy(String offsetccy) {
        this.offsetccy = offsetccy;
    }

  
}
