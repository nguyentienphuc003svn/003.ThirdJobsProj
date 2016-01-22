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
public class AmountUnBlockInput  extends Input{

    private String account;
    private String amountblockno;

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

    public String toXML() {
        XStream xstream = new XStream();
        xstream.alias("amountblock", AmountUnBlockInput.class);
        return xstream.toXML(this);
    }
}
