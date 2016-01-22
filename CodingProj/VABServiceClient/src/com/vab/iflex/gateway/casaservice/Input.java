/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vab.iflex.gateway.casaservice;

/**
 *
 * @author LêMinhNhựt
 */
public abstract class Input {
    private String msgid;
    private Double[] charge;
    public Input()
    {
        msgid = java.util.UUID.randomUUID().toString().replace("-", "");
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

    public String newID() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * @return the charge
     */
    public Double[] getCharge() {
        return charge;
    }

    /**
     * @param charge the charge to set
     */
    public void setCharge(Double[] charge) {
        this.charge = charge;
    }
}
