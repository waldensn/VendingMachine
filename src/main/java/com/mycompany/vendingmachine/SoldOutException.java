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

//Thrown by VendingMachineImpl when there are not enough items remaining
public class SoldOutException extends Exception {
  private String message;
    
    public SoldOutException(String message) {
        this.message = message;
    }
    
    @Override
    public String getMessage(){
        return this.message;
    }
}
