package paystation.domain;

/**
 * Implementation of the pay station.
 *
 * Responsibilities:
 *
 * 1) Accept payment; 
 * 2) Calculate parking time based on payment; 
 * 3) Know earning, parking time bought; 
 * 4) Issue receipts; 
 * 5) Handle buy and cancel events.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */
import java.util.*;
public class PayStationImpl implements PayStation {
    
    private int insertedSoFar;
    private int timeBought;
    private int countN = 0;
    private int countD = 0;
    private int countQ = 0;
    
    //map<coinType,coinCount> keeps count of all coins of coinType
    Map<Integer,Integer> map = new HashMap<Integer,Integer>();

    @Override
    public void addPayment(int coinValue)
            throws IllegalCoinException {
        switch (coinValue) {
            //determine coin type. if map exists for coin, replace it, else create one 
            case 5:  
                if(map.get(coinValue)==null){
                    map.put(coinValue,countN+1);
                    countN++;
                    break;
                }else{
                    map.remove(coinValue);
                    map.put(coinValue, countN+1);
                    countN++;
                    break;
                }
            case 10:  
                if(map.get(coinValue)==null){
                    map.put(coinValue,countD+1);
                    countD++;
                    break;
                }else{
                    map.remove(coinValue);
                    map.put(coinValue, countD+1);
                    countD++;
                    break;
                }
            case 25:  
                if(map.get(coinValue)==null){
                    map.put(coinValue,countQ+1);
                    countQ++;
                    break;
                }else{
                    map.remove(coinValue);
                    map.put(coinValue, countQ+1);
                    countQ++;
                    break;
                }
            default:
                throw new IllegalCoinException("Invalid coin: " + coinValue);
        }
        
        insertedSoFar += coinValue;
        timeBought = insertedSoFar / 5 * 2;
    }

    @Override
    public int readDisplay() {
        return timeBought;
    }

    @Override
    public Receipt buy() {
        Receipt r = new ReceiptImpl(timeBought);
        reset();
        return r;
    }

    @Override
    public Map<Integer,Integer> cancel() {
        Map<Integer,Integer> mapClone = new HashMap<Integer,Integer>();
        
        if (map.get(5)==null){mapClone.put(5,0);}
        else{mapClone.put(5, countN);}
        if (map.get(10)==null){mapClone.put(10,0);}
        else{mapClone.put(10, countD);}
        if (map.get(25)==null){mapClone.put(25,0);}
        else{mapClone.put(25, countQ);}        

        reset();
        return mapClone;
    }
    
    private void reset() {
        timeBought = insertedSoFar = countN = countD = countQ = 0;
        if(map.get(5)!=null) {map.remove(5);}
        if(map.get(10)!=null) {map.remove(10);}
        if(map.get(25)!=null) {map.remove(25);}
    }
    
    
    public int empty() {
        int money = insertedSoFar;
        reset();
        return money;
    }
}
