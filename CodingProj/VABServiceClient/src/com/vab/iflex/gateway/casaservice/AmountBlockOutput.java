/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vab.iflex.gateway.casaservice;
 
import com.thoughtworks.xstream.XStream;

/**
 *
 * @author Minh Nhut
 */
public class AmountBlockOutput {

    private String msgid;
    private String account;
    private String amountblockno;
    private String amount;
    private String effectivedate;
    private String expirydate;
    private String remarks;
    private String makerid;
    private String makerdate;
    private String checkerid;
    private String checkerdate;
    private String recordstat;
    private String modno;
    private String authstat;
    private String onceauth;
    private Result result;

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAmountblockno() {
        return amountblockno;
    }

    public void setAmountblockno(String amountblockno) {
        this.amountblockno = amountblockno;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEffectivedate() {
        return effectivedate;
    }

    public void setEffectivedate(String effectivedate) {
        this.effectivedate = effectivedate;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMakerid() {
        return makerid;
    }

    public void setMakerid(String makerid) {
        this.makerid = makerid;
    }

    public String getMakerdate() {
        return makerdate;
    }

    public void setMakerdate(String makerdate) {
        this.makerdate = makerdate;
    }

    public String getCheckerid() {
        return checkerid;
    }

    public void setCheckerid(String checkerid) {
        this.checkerid = checkerid;
    }

    public String getCheckerdate() {
        return checkerdate;
    }

    public void setCheckerdate(String checkerdate) {
        this.checkerdate = checkerdate;
    }

    public String getRecordstat() {
        return recordstat;
    }

    public void setRecordstat(String recordstat) {
        this.recordstat = recordstat;
    }

    public String getModno() {
        return modno;
    }

    public void setModno(String modno) {
        this.modno = modno;
    }

    public String getAuthstat() {
        return authstat;
    }

    public void setAuthstat(String authstat) {
        this.authstat = authstat;
    }

    public String getOnceauth() {
        return onceauth;
    }

    public void setOnceauth(String onceauth) {
        this.onceauth = onceauth;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static AmountBlockOutput fromXML(String xml) {
        XStream xstream = new XStream();
        xstream.alias("amountblock", AmountBlockOutput.class);
        xstream.alias("result", Result.class);       
        xstream.addImplicitCollection(Result.class, "errors");
        return (AmountBlockOutput) xstream.fromXML(xml.replace("&", "<![CDATA[&]]>"));
    }
}
