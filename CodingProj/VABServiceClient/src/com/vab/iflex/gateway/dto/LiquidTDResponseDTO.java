/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vab.iflex.gateway.dto;

import com.thoughtworks.xstream.XStream;

/**
 *
 * @author chuongnt
 */
public class LiquidTDResponseDTO extends BaseClass {

    public String msgid;
    public String xref;
    public String brn;
    public String tdac;
    public String custid;
    public String amount;
    public String ccy;
    public String liqamount;
    public String savingbrn;
    public String savingaccno;
    public String savingccy;
    public String savingrate;
    public String savingamt;
    public String narrative;
    public String makerid;
    public String makerdt;
    public String checkerid;
    public String checkerdt;
    public Result result;

    public LiquidTDResponseDTO fromXML(String xml) throws Exception {

        XStream xstream = new XStream();
        xstream.alias("tdacmliq", LiquidTDResponseDTO.class);
        xstream.alias("result", Result.class);

        xstream.addImplicitCollection(Result.class, "errors");
        xstream.alias("error", Error.class);
        xml = xml.replace("<narrative>", "<narrative><![CDATA[");
        xml = xml.replace("</narrative>", "]]></narrative>");
        return (LiquidTDResponseDTO) xstream.fromXML(xml);
    }

    public String toXML() {
        XStream xstream = new XStream();
        xstream.alias("tdacmliq", LiquidTDResponseDTO.class);
        xstream.alias("result", Result.class);

        xstream.addImplicitCollection(Result.class, "errors");
        xstream.alias("error", Error.class);
        return xstream.toXML(this);
    }
}
