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
public class TDOpenRequestDTO extends Input{
    public String custno;
    public String brn;
    public String accls;
    public String ccy;
    public boolean autoroll;
    public String rolltype;
    public String tdamt;
    public String offbrn;
    public String offacc;
    public String bookaccbrn;
    public String bookacc;
    public String liqdbrn;
    public String liqdacc;
    
    public String desc;

    
    public String toXML()
    {
        XStream xstream = new XStream();
        xstream.alias("tdac", TDOpenRequestDTO.class);
        return xstream.toXML(this);
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

    public boolean isAutoroll() {
        return autoroll;
    }

    public void setAutoroll(boolean autoroll) {
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    
}
