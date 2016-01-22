/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vab.iflex.gateway.dto;

import com.thoughtworks.xstream.XStream;

/**
 *
 * @author CHUONGNT
 */
public class TDOpenResponseDTO extends BaseClass {

    public String msgid;
    public String custno;
    public String custname;
    public String brn;
    public String accls;
    public String ccy;
    public String adesc;
    public String accopendt;
    public String autoroll;
    public String rolltype;
    public String tdamt;
    public String offbrn;
    public String offacc;
    public String bookaccbrn;
    public String bookacc;
    public String liqdbrn;
    public String liqdacc;
    public String stmtac;
    public String acc;
    public String rate;
    public String xref;
    public Result result;

    @Override
    public TDOpenResponseDTO fromXML(String xml) throws Exception {

        XStream xstream = new XStream();
        xstream.alias("tdac", TDOpenResponseDTO.class);
        xstream.alias("result", Result.class);

        xstream.addImplicitCollection(Result.class, "errors");
        xstream.alias("error", Error.class);
        return (TDOpenResponseDTO) xstream.fromXML(xml.replace("&", "<![CDATA[&]]>"));
    }

    public String toXML() {
        XStream xstream = new XStream();
        xstream.alias("tdac", TDOpenResponseDTO.class);
        xstream.alias("result", Result.class);

        xstream.addImplicitCollection(Result.class, "errors");
        xstream.alias("error", Error.class);
        return xstream.toXML(this);
    }

    //    <tdac><msgid>MSG00000000000060824</msgid>
    //    <custno>020006713</custno>
    //    <brn>020</brn>
    //    <accls>TV0001</accls>
    //    <ccy>VND</ccy>
    //    <adesc></adesc>
    //    <accopendt>2013-12-12</accopendt>
    //    <autoroll>N</autoroll>
    //    <rolltype>I</rolltype>
    //    <tdamt>1900</tdamt>
    //    <offbrn>020</offbrn>
    //    <offacc>0203000053819000</offacc>
    //    <bookaccbrn>020</bookaccbrn>
    //    <bookacc>0203000053819000</bookacc>
    //    <liqdbrn>020</liqdbrn>
    //    <liqdacc>0203000053819000</liqdacc>
    //    <stmtac>IB0000001</stmtac>
    //    <acc>0209000231430100</acc>
    //    <rate></rate><xref>IBK0000000059759</xref>    //
    //    <result>
    //    <status>SUCCESS</status>
    //    <errors>
    //        <ecode>ST-SAVE-002</ecode>
    //        <edesc>Record Successfully Saved and Authorized</edesc>
    //    </errors>
    //    </result>
    //    </tdac>
    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getCustno() {
        return custno;
    }

    public void setCustno(String custno) {
        this.custno = custno;
    }

    public String getBrn() {
        return brn;
    }

    public void setBrn(String brn) {
        this.brn = brn;
    }

    public String getAccls() {
        return accls;
    }

    public void setAccls(String accls) {
        this.accls = accls;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getAdesc() {
        return adesc;
    }

    public void setAdesc(String adesc) {
        this.adesc = adesc;
    }

    public String getAccopendt() {
        return accopendt;
    }

    public void setAccopendt(String accopendt) {
        this.accopendt = accopendt;
    }

    public String getAutoroll() {
        return autoroll;
    }

    public void setAutoroll(String autoroll) {
        this.autoroll = autoroll;
    }

    public String getRolltype() {
        return rolltype;
    }

    public void setRolltype(String rolltype) {
        this.rolltype = rolltype;
    }

    public String getTdamt() {
        return tdamt;
    }

    public void setTdamt(String tdamt) {
        this.tdamt = tdamt;
    }

    public String getOffbrn() {
        return offbrn;
    }

    public void setOffbrn(String offbrn) {
        this.offbrn = offbrn;
    }

    public String getOffacc() {
        return offacc;
    }

    public void setOffacc(String offacc) {
        this.offacc = offacc;
    }

    public String getBookaccbrn() {
        return bookaccbrn;
    }

    public void setBookaccbrn(String bookaccbrn) {
        this.bookaccbrn = bookaccbrn;
    }

    public String getBookacc() {
        return bookacc;
    }

    public void setBookacc(String bookacc) {
        this.bookacc = bookacc;
    }

    public String getLiqdbrn() {
        return liqdbrn;
    }

    public void setLiqdbrn(String liqdbrn) {
        this.liqdbrn = liqdbrn;
    }

    public String getLiqdacc() {
        return liqdacc;
    }

    public void setLiqdacc(String liqdacc) {
        this.liqdacc = liqdacc;
    }

    public String getStmtac() {
        return stmtac;
    }

    public void setStmtac(String stmtac) {
        this.stmtac = stmtac;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getXref() {
        return xref;
    }

    public void setXref(String xref) {
        this.xref = xref;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

}
