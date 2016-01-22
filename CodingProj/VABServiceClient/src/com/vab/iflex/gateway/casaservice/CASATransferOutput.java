/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 package com.vab.iflex.gateway.casaservice;

import com.vab.iflex.gateway.casaservice.converter.DoubleConverter;
import com.thoughtworks.xstream.XStream;

/**
 *
 * @author LêMinhNhựt
 */
public class CASATransferOutput extends Output{

    private String msgid;//message id
    private String xref;//transaction ref no
    private String prd;//product code
    private String brn;//transaction branch
    private String valdate;//transaction value date
    private String bookdate;//transation account book date
    private String txndate;//transation account date
    private String txnbrn;//transation account branch
    private String txnacc;//transation account number
    private String txnamt;//transation account mount 
    private String txnccy;//transation account currency
    private String txndrcr;//transation account debit/credit
    private String offsetbrn;//offset account branch
    private String offsetacc;//offset account 
    private String offsetccy;//offset account ccy
    private String offsetamt;// offset account amount
    private String ft;
    private String routecode;
    private String narrative;
    private WBTBSChargeDetail[] wbtbs_charge_details;
    private WBTBSAdviceDetail wbtbs_advice_details;
    private Result result;

    public CASATransferOutput() {
    }
    
    /**
     * @return the msgid
     */   

    public String getMsgid() {
        return msgid;
    }

    /**
     * @param msgid the msgid to set
     */
    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    /**
     * @return the xref
     */
    public String getXref() {
        return xref;
    }

    /**
     * @param xref the xref to set
     */
    public void setXref(String xref) {
        this.xref = xref;
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
     * @return the valdate
     */
    public String getValdate() {
        return valdate;
    }

    /**
     * @param valdate the valdate to set
     */
    public void setValdate(String valdate) {
        this.valdate = valdate;
    }

    /**
     * @return the bookdate
     */
    public String getBookdate() {
        return bookdate;
    }

    /**
     * @param bookdate the bookdate to set
     */
    public void setBookdate(String bookdate) {
        this.bookdate = bookdate;
    }

    /**
     * @return the txndate
     */
    public String getTxndate() {
        return txndate;
    }

    /**
     * @param txndate the txndate to set
     */
    public void setTxndate(String txndate) {
        this.txndate = txndate;
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
    public String getTxnamt() {
        return txnamt;
    }

    /**
     * @param txnamt the txnamt to set
     */
    public void setTxnamt(String txnamt) {
        this.txnamt = txnamt;
    }

    /**
     * @return the tnxtccy
     */
    public String getTxnccy() {
        return txnccy;
    }

    /**
     * @param txntccy the txntccy to set
     */
    public void setTxnccy(String txntccy) {
        this.txnccy = txntccy;
    }

    /**
     * @return the txndrcr
     */
    public String getTxndrcr() {
        return txndrcr;
    }

    /**
     * @param txndrcr the txndrcr to set
     */
    public void setTxndrcr(String txndrcr) {
        this.txndrcr = txndrcr;
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
     * @return the offsetccy
     */
    public String getOffsetccy() {
        return offsetccy;
    }

    /**
     * @param offsetccy the offsetccy to set
     */
    public void setOffsetccy(String offsetccy) {
        this.offsetccy = offsetccy;
    }

    /**
     * @return the offsetamt
     */
    public String getOffsetamt() {
        return offsetamt;
    }

    /**
     * @param offsetamt the offsetamt to set
     */
    public void setOffsetamt(String offsetamt) {
        this.offsetamt = offsetamt;
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

    /**
     * @return the wbtbs_charge_details
     */
    public WBTBSChargeDetail[] getWbtbs_charge_details() {
        return wbtbs_charge_details;
    }

    /**
     * @param wbtbs_charge_details the wbtbs_charge_details to set
     */
    public void setWbtbs_charge_details(WBTBSChargeDetail[] wbtbs_charge_details) {
        this.wbtbs_charge_details = wbtbs_charge_details;
    }

    /**
     * @return the wbtbs_advice_details
     */
    public WBTBSAdviceDetail getWbtbs_advice_details() {
        return wbtbs_advice_details;
    }

    /**
     * @param wbtbs_advice_details the wbtbs_advice_details to set
     */
    public void setWbtbs_advice_details(WBTBSAdviceDetail wbtbs_advice_details) {
        this.wbtbs_advice_details = wbtbs_advice_details;
    }

    /**
     * @return the result
     */
    public Result getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Result result) {
        this.result = result;
    }
    public static CASATransferOutput fromXML(String xml) throws Exception {
        
        //String[] formats = {"yyyy-MM-dd", ""};
        XStream xstream = new XStream();
        xstream.registerConverter(new DoubleConverter());
        
        //xstream.registerConverter(new DateConverter("yyyy-MM-dd", formats));
        xstream.alias("casatransfer", CASATransferOutput.class);
        xstream.alias("wbtbs_advice_details", WBTBSAdviceDetail.class);
        xstream.alias("wbtbs_charge_details", WBTBSChargeDetail.class);
        xstream.alias("result", Result.class);
        xstream.addImplicitCollection(CASATransferOutput.class, "wbtbs_charge_details");
        xstream.addImplicitCollection(Result.class, "errors");
        xml = xml.replace("<narrative>", "<narrative><![CDATA[");
        xml = xml.replace("</narrative>", "]]></narrative>");
        return (CASATransferOutput) xstream.fromXML(xml);        
        
    }   

    /**
     * @return the ft
     */
    public String getFt() {
        return ft;
    }

    /**
     * @param ft the ft to set
     */
    public void setFt(String ft) {
        this.ft = ft;
    }

    /**
     * @return the routecode
     */
    public String getRoutecode() {
        return routecode;
    }

    /**
     * @param routecode the routecode to set
     */
    public void setRoutecode(String routecode) {
        this.routecode = routecode;
    }
}
