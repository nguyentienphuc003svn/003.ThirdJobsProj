/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vab.iflex.gateway.casaservice;

import java.io.Serializable;

/**
 *
 * @author LêMinh
 */
public class WBTBSAdviceDetail{
    private String dcn; //advice control number
    private String msg1;
    public String msg2;
    private String msg3;

    /**
     * @return the dcn
     */
    public String getDcn() {
        return dcn;
    }

    /**
     * @param dcn the dcn to set
     */
    public void setDcn(String dcn) {
        this.dcn = dcn;
    }

    /**
     * @return the msg1
     */
    public String getMsg1() {
        return msg1;
    }

    /**
     * @param msg1 the msg1 to set
     */
    public void setMsg1(String msg1) {
        this.msg1 = msg1;
    }

    /**
     * @return the msg3
     */
    public String getMsg3() {
        return msg3;
    }

    /**
     * @param msg3 the msg3 to set
     */
    public void setMsg3(String msg3) {
        this.msg3 = msg3;
    }

    /**
     * @return the msg2
     */
    public String getMsg2() {
        return msg2;
    }

    /**
     * @param msg2 the msg2 to set
     */
    public void setMsg2(String msg2) {
        this.msg2 = msg2;
    }
}
