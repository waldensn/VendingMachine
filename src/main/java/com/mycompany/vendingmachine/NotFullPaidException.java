/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.vendingmachine;

/**
 *
 * @author walde
 */

//Thrown by VendingMachineImpl when user tries to get item without paying full amount
public class NotFullPaidException extends Exception {
    private String message; 
    private long remaining; 
    
    public NotFullPaidException(String message, long remaining) { 
        this.message = message; 
        this.remaining = remaining; 
    }
    
    public long getRemaining(){ 
        return remaining; 
    } 
    
    @Override public String getMessage(){ 
        return message + remaining; 
    }
}
