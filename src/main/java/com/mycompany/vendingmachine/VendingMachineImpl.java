/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.vendingmachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author walde
 */

//implementation of the vending machine
public class VendingMachineImpl implements VendingMachine {
    private Inventory<Coin> coinInventory = new Inventory<Coin>();
    private Inventory<Item> itemInventory = new Inventory<Item>();
    private long totalSales; 
    private Item currentItem; 
    private long currentBalance;
    
    public VendingMachineImpl(){
        initialize();
    }
    //load machine with 5 coins of each type and 5 cans of each type
    private void initialize(){
        for (Coin c : Coin.values()){
            coinInventory.put(c, 5);
        }
        
        for (Item i : Item.values()){
            itemInventory.put(i, 5);
        }
    }
    
    //return the price of an item, if we have it in the machine
    @Override
    public long selectItemAndGetPrice(Item item) throws SoldOutException {
        if (itemInventory.hasItem(item)){
            currentItem = item;
            return item.getPrice();
        }
        throw new SoldOutException(item.getName() + " is sold out.");
    }

    //insert a coin into the machine
    @Override
    public void insertCoin(Coin coin) {
        currentBalance += coin.getDenomination();
        coinInventory.add(coin);
    }

    //give a refund
    @Override
    public List<Coin> refund() throws NotSufficientChangeException {
        List<Coin> refund = getChange(currentBalance);
        updateCoinInventory(refund);
        currentItem = null;
        currentBalance = 0;
        return refund;
    }

    //get the selected item and correct change
    @Override
    public Bucket<Item, List<Coin>> collectItemAndChange() throws NotSufficientChangeException, NotFullPaidException {
        Item item = collectItem();
        totalSales += item.getPrice();
        List<Coin> change = collectChange();
        return new Bucket<Item, List<Coin>>(item, change);
    }

    //reset the vending machine
    @Override
    public void reset() {
        coinInventory.clear(); 
        itemInventory.clear(); 
        totalSales = 0; 
        currentItem = null; 
        currentBalance = 0;
    }
    
    //get the item if we have change and if we paid in full
    private Item collectItem() throws NotSufficientChangeException, NotFullPaidException {
        if ( isFullPaid()){
            if ( hasSufficientChange() ){
                itemInventory.deduct(currentItem);
                return currentItem;
            }
            else
            {
                throw new NotSufficientChangeException("Insufficient change.");
            }
        }
        long remainingBalance = currentItem.getPrice() - currentBalance; 
        throw new NotFullPaidException("Price not full paid, remaining: ", remainingBalance);
    }
    
    //collect the change if we have enough coins
    private List<Coin> collectChange() throws NotSufficientChangeException { 
        long changeAmount = currentBalance - currentItem.getPrice(); 
        List<Coin> change = getChange(changeAmount); 
        updateCoinInventory(change); 
        currentBalance = 0; 
        currentItem = null; 
        return change; 
    }

    //put the change into a list of coins
    private List<Coin> getChange(long amount) throws NotSufficientChangeException{
        List<Coin> change = new ArrayList<Coin>();
        if ( amount > 0 ){
            long balance = amount;
            while ( balance > 0 ){
                if ( balance >= Coin.QUARTER.getDenomination() && coinInventory.hasItem(Coin.QUARTER) ){
                    balance -= Coin.QUARTER.getDenomination();
                    change.add(Coin.QUARTER);
                    continue;
                }
                else if ( balance >= Coin.DIME.getDenomination() && coinInventory.hasItem(Coin.DIME)){
                    balance -= Coin.DIME.getDenomination();
                    change.add(Coin.DIME);
                    continue;
                }
                else if ( balance >= Coin.NICKEL.getDenomination() && coinInventory.hasItem(Coin.NICKEL) ){
                    balance -= Coin.NICKEL.getDenomination();
                    change.add(Coin.NICKEL);
                    continue;
                }
                else if ( balance >= Coin.PENNY.getDenomination() && coinInventory.hasItem(Coin.PENNY) ){
                    balance -= Coin.PENNY.getDenomination();
                    change.add(Coin.PENNY);
                }
                else{
                    throw new NotSufficientChangeException("Insufficient coins in the machine to make change.");
                }
            }
        }
            
        return change;
    }
    
    //have we paid for the item?
    private boolean isFullPaid() 
    { 
        if( currentBalance >= currentItem.getPrice() ){ 
            return true; 
        }
            return false; 
    }
    
    //do we have enough change?
    private boolean hasSufficientChange(){
        return hasSufficientChangeForAmount(currentBalance - currentItem.getPrice() ); 
    }

    //do we have enough coins for the amount?
    private boolean hasSufficientChangeForAmount(long amount)
    { 
        boolean hasChange = true; 
        try{ 
            getChange(amount); 
        }
        catch(NotSufficientChangeException nsce){ 
            hasChange = false; 
        } 
        return hasChange; 
    }   
    
    //print some information
    public void printStats(){ 
        System.out.println("Total Sales : " + totalSales); 
        System.out.println("Current Item Inventory : " + itemInventory); 
        System.out.println("Current Cash Inventory : " + coinInventory); 
    }
    
    //remove coins from the coin inventory
    private void updateCoinInventory(List<Coin> change) { 
        for ( Coin c : change ){ 
            coinInventory.deduct(c); 
        } 
    }
    
    @Override
    //return total sales
    public long getTotalSales(){
        return totalSales;
    }
    
    @Override
    public long getCurrentBalance(){
        return currentBalance;
    }
}
