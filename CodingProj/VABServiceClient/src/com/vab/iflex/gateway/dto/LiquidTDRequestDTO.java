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
public class LiquidTDRequestDTO extends Input{
    
        public String brn;
        public String tdac;
        public String savingbrn;
        public String savingaccno;
        public String savingccy;
        public String narrative;
        
        public String toXML()
        {
            XStream xstream = new XStream();
            xstream.alias("tdacmliq", LiquidTDRequestDTO.class);
            return xstream.toXML(this);
        }
}
