/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 package com.vab.iflex.gateway.casaservice;
 
/**
 *
 * @author LêMinhNhựt
 */
public class Error {

    private String ecode;
    private String edesc;
    public Error() {
    }
    /**
     * @return the ecode
     */
    public String getEcode() {
        return ecode;
    }

    /**
     * @param ecode the ecode to set
     */
    public void setEcode(String ecode) {
        this.ecode = ecode;
    }

    /**
     * @return the edesc
     */
    public String getEdesc() {
        return edesc;
    }

    /**
     * @param edesc the edesc to set
     */
    public void setEdesc(String edesc) {
        this.edesc = edesc;
    }
}
