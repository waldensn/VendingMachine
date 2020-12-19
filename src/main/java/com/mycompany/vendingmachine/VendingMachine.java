package com.mycompany.vendingmachine;


import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author walde
 */

//public API of the vending machine, high level functionality should go here
public interface VendingMachine {
    public long selectItemAndGetPrice(Item item) throws SoldOutException; 
    public void insertCoin(Coin coin); 
    public List<Coin> refund() throws NotSufficientChangeException; 
    public Bucket<Item, List<Coin>> collectItemAndChange() throws NotSufficientChangeException, NotFullPaidException; 
    public void reset();
    public long getTotalSales();
    public long getCurrentBalance();
}
