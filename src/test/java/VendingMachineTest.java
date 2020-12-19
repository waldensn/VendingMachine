/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mycompany.vendingmachine.*;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author walde
 */
public class VendingMachineTest {
    
    private static VendingMachine vm; 

//    @BeforeClass 
//    public static void setUp(){ 
//        vm = VendingMachineFactory.createVendingMachine();
//    }
    
    @Before
    public void setUp(){
        vm = VendingMachineFactory.createVendingMachine();
    }
    
    @AfterClass 
    public static void tearDown(){ 
        vm = null; 
    }
    
    @Test
    public void testBuyItemWithExactPrice() throws Exception{
        long price = vm.selectItemAndGetPrice(Item.COKE);
        assertEquals(Item.COKE.getPrice(), price);
        
        vm.insertCoin(Coin.QUARTER);
        Bucket<Item, List<Coin>> bucket = vm.collectItemAndChange(); 
        
        Item item = bucket.getFirst();
        List<Coin> change = bucket.getSecond();
        assertEquals(item, item.COKE);
        assertTrue(change.isEmpty());
    }

    @Test
    public void testBuyItemWithHigherPrice() throws Exception{
       long price = vm.selectItemAndGetPrice(Item.SODA);
       assertEquals(Item.SODA.getPrice(), price);
       
       vm.insertCoin(Coin.QUARTER);
       vm.insertCoin(Coin.QUARTER);
       
       Bucket<Item, List<Coin>> bucket = vm.collectItemAndChange();
       Item item = bucket.getFirst();
       List<Coin> change = bucket.getSecond();
       assertEquals(item, Item.SODA);
       assertTrue(!change.isEmpty());
       assertEquals(50 - Item.SODA.getPrice(), getTotal(change));
    }

    @Test
    public void testRefund() throws Exception{
       long price = vm.selectItemAndGetPrice(Item.PEPSI);
       assertEquals(Item.PEPSI.getPrice(), price );
       vm.insertCoin(Coin.QUARTER);
       vm.insertCoin(Coin.DIME);
       vm.insertCoin(Coin.NICKEL);
       vm.insertCoin(Coin.PENNY);
       assertEquals(41, getTotal(vm.refund()));
    }
    
    @Test(expected=NotFullPaidException.class)
    public void testNotFullPaid() throws Exception{
        vm.selectItemAndGetPrice(Item.PEPSI);
        vm.insertCoin(Coin.DIME);
        vm.insertCoin(Coin.DIME);
        vm.collectItemAndChange();
    }
    
    @Test(expected=SoldOutException.class)
    public void testSoldOut() throws Exception{
        for (int i = 0; i < 6; i++) { 
            vm.selectItemAndGetPrice(Item.COKE); 
            vm.insertCoin(Coin.QUARTER); 
            vm.collectItemAndChange(); 
        }
    }
    
    @Test(expected=NotSufficientChangeException.class)
    public void testNotSufficientChange() throws Exception{
        for (int i = 0; i < 4; i++){
            vm.selectItemAndGetPrice(Item.SODA);
            vm.insertCoin(Coin.QUARTER);
            vm.insertCoin(Coin.QUARTER);
            vm.collectItemAndChange();
        }
        
        for (int i = 0; i < 4; i++){
            vm.selectItemAndGetPrice(Item.COKE);
            vm.insertCoin(Coin.QUARTER);
            vm.insertCoin(Coin.QUARTER);
            vm.collectItemAndChange();
        }
        
        for (int j = 0; j < 3; j++){
            vm.selectItemAndGetPrice(Item.PEPSI);
            vm.insertCoin(Coin.QUARTER);
            vm.insertCoin(Coin.QUARTER);
            vm.collectItemAndChange();
        }
    }
    
    @Test(expected=SoldOutException.class)
    public void testReset() throws Exception{
        vm.reset();
        vm.selectItemAndGetPrice(Item.COKE);
    }
    
    private long getTotal(List<Coin> change){ 
        long total = 0; 
        for(Coin c : change){ 
            total = total + c.getDenomination(); 
        } 
        return total; }

}
