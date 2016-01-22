/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vab.iflex.gateway.casaservice.converter;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 *
 * @author MinhNhut
 */
public class DoubleConverter extends AbstractSingleValueConverter {

    @Override
    public boolean canConvert(Class type) {
        return type.equals(double.class) || type.equals(java.lang.Double.class);
        
    }

    @Override
    public Object fromString(String string) {
        if(string==null || string.equals("") ){
            string = "0";
        }
        return Double.parseDouble(string);
    }
    
}
