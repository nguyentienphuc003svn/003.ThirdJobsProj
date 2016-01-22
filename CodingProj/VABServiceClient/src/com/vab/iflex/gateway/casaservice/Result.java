/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vab.iflex.gateway.casaservice;

/**
 *
 * @author LêMinhNhựt
 */
public class Result{

    private String status;    //failure "success"
    private Error[] errors;

    public Result() {
    }
    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the errors
     */
    public Error[] getErrors() {
        return errors;
    }

    /**
     * @param errors the errors to set
     */
    public void setErrors(Error[] errors) {
        this.errors = errors;
    }
}
