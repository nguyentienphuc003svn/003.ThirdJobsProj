/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vab.iflex.gateway.casaservice;

import java.io.Serializable;

/**
 *
 * @author LêMinh
 * charge information
 */
public class WBTBSChargeDetail{
    private String waiver;//charge waiver
    private Double chgamt;//charge amount
    
    private String chgccy;//charge currency
    private Double tcyxrate = 0.0;//charge currency exchange rate
    private Double lcychg;//charge locality amount
    private String txncode;//charge transaction code
    private String nettingind;//charge netting
    private String chggl;//charge GL account

    /**
     * @return the waiver
     */
    public String getWaiver() {
        return waiver;
    }

    /**
     * @param waiver the waiver to set
     */
    public void setWaiver(String waiver) {
        this.waiver = waiver;
    }

    /**
     * @return the cghamt
     */
    public Double getChgamt() {
        return chgamt;
    }

    /**
     * @param cghamt the cghamt to set
     */
    public void setChgamt(Double cghamt) {
        this.chgamt = cghamt;
    }

    /**
     * @return the chgccy
     */
    public String getChgccy() {
        return chgccy;
    }

    /**
     * @param chgccy the chgccy to set
     */
    public void setChgccy(String chgccy) {
        this.chgccy = chgccy;
    }

    /**
     * @return the tcxrate
     */
    public Double getTcyxrate() {
        return tcyxrate;
    }

    /**
     * @param tcxrate the tcxrate to set
     */
    public void setTcyxrate(Double tcxrate) {
        this.tcyxrate = tcxrate;
    }

    /**
     * @return the lcychg
     */
    public Double getLcychg() {
        return lcychg;
    }

    /**
     * @param lcychg the lcychg to set
     */
    public void setLcychg(Double lcychg) {
        this.lcychg = lcychg;
    }

    /**
     * @return the txncode
     */
    public String getTxncode() {
        return txncode;
    }

    /**
     * @param txncode the txncode to set
     */
    public void setTxncode(String txncode) {
        this.txncode = txncode;
    }

    /**
     * @return the nettingind
     */
    public String getNettingind() {
        return nettingind;
    }

    /**
     * @param nettingind the nettingind to set
     */
    public void setNettingind(String nettingind) {
        this.nettingind = nettingind;
    }

    /**
     * @return the chggl
     */
    public String getChggl() {
        return chggl;
    }

    /**
     * @param chggl the chggl to set
     */
    public void setChggl(String chggl) {
        this.chggl = chggl;
    }
}
