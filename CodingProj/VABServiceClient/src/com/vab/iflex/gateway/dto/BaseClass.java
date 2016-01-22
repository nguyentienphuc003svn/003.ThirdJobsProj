/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vab.iflex.gateway.dto;

import java.io.Serializable;

/**
 *
 * @author CHUONGNT
 */
public abstract class BaseClass implements Serializable{
    
       public abstract BaseClass fromXML(String xml) throws Exception ;
}
