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
public class AmountBlockInput  extends Input {

    private String account;
    private Double amount;
    private String remarks;

    public String toXML() {
        XStream xstream = new XStream();
        xstream.alias("amountblock", AmountBlockInput.class);
        return xstream.toXML(this);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remark) {
        this.remarks = remark;
    }
}
