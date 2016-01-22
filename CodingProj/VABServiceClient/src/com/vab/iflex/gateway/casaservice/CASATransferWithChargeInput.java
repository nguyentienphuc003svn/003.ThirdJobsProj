/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vab.iflex.gateway.casaservice;

import com.thoughtworks.xstream.XStream;

/**
 *
 * @author MinhNhut
 */
public class CASATransferWithChargeInput extends Input{
    private String prd;//product code
    private String brn;//transaction branch       
    private String offsetbrn;//offset branch
    private String offsetacc; //offset account
    private Double offsetamt; //transaction account amount
    private String offsetccy;
    private String routecode;
    private String narrative;//transaction narrative
    public String toXML()
    {
        XStream xstream = new XStream();
        xstream.alias("casatransfer", CASATransferWithChargeInput.class);
        xstream.alias("charge", double.class);
        xstream.addImplicitCollection(CASATransferWithChargeInput.class, "charge");
        return xstream.toXML(this);
    }
    public static String toXML(CASATransferWithChargeInput input)
    {
        return input.toXML();
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
     * @return the offsetamt
     */
    public Double getOffsetamt() {
        return offsetamt;
    }

    /**
     * @param offsetamt the offsetamt to set
     */
    public void setOffsetamt(Double offsetamt) {
        this.offsetamt = offsetamt;
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
}

