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
public enum Coin {    
    PENNY(1), 
    NICKEL(5), 
    DIME(10), 
    QUARTER(25);
    
    private Coin(int denomination){
        this.denomination = denomination;
    }
    private int denomination;
    public int getDenomination(){return denomination;}
}
