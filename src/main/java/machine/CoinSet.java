package machine;

import java.util.*;

public class CoinSet {

    private SortedMap<String,String> coinsTable;
    private double total;

    public CoinSet(String fileName){
        /**
         * File could be empty thus has to be checked if size == 0
         */
        coinsTable = Util.logInDetails(fileName);
        if(coinsTable.size() == 0)preloadTable();
    }

    public CoinSet(){
        /**
         * Not read in from file has to be preloaded
         */
        coinsTable = new TreeMap<>();
        preloadTable();
    }

    public SortedMap<String,String> getCoinsTable(){
        return coinsTable;
    }

    private void preloadTable(){
        String[] values = {"0.05","0.1","0.2","0.5","1.0","2.0"};
        for(String val : values){
            getCoinsTable().put(val,Integer.toString(0));
        }
    }

    public int[] amounts(){
        int[] amounts = new int[6];
        Set<String> keys = getCoinsTable().keySet();
        int i = 0;
        for(String key : keys){
            amounts[i] = Integer.parseInt(getCoinsTable().get(key));
            i++;
        }
        return amounts;
    }


/*
    public void addNewCoinToTable(String value,String amount){
        getCoinsTable().put(value,amount);
    }

    public void removeCoinFromTable(String value){
        if(getCoinsTable().containsKey(value)){
            getCoinsTable().remove(value);
        }
    }
*/

    public void removeCoinsFromTable(String name,int amount){
        if(getCoinsTable().containsKey(name)){
            int prevAmount = Integer.parseInt(getCoinsTable().get(name));
            int newAmount = prevAmount - amount;
            setTotal(getTotal()  - (Double.parseDouble(name)*amount));
            getCoinsTable().replace(name,Integer.toString(newAmount));
        }
    }

    public void addCoinsToTable(double value,int amount){
        if(getCoinsTable().containsKey(Double.toString(value))){
            int prevAmount = Integer.parseInt(getCoinsTable().get(Double.toString(value)));
            int newAmount = prevAmount + amount;
            setTotal(getTotal() + (value*amount));
            getCoinsTable().put(Double.toString(value),Integer.toString(newAmount));
        }
    }

    public void removeAllCoins(){
        Set<String> keys = getCoinsTable().keySet();
        System.out.println("Coins Removed = " + getCoinsAmount());
        for(String key : keys){
            removeCoinsFromTable(key,Integer.parseInt(getCoinsTable().get(key)));
        }

    }

    public int getCoinsAmount(){
        Set<String> keys = getCoinsTable().keySet();
        int  total = 0;
        for(String key : keys){
            total += Integer.parseInt(getCoinsTable().get(key));
        }
        return total;
    }

    public double getTotalValue(){
        Set<String> keys = getCoinsTable().keySet();
        double total = 0;
        for(String key : keys){
            total += (Double.parseDouble(key) * Integer.parseInt(getCoinsTable().get(key)));
        }
        return total;
    }

    /*
    private static String coinValueToName(String value){
        switch (value){
            case ".05": return "5 cents";
            case ".1": return "10 cents";
            case ".2": return "20 cents";
            case ".5": return "50 cents";
            case "1.0": return "1 euro";
            case "2.0": return "2 euro";
        }
        return "";
    }

    private static double coinNameToValue(String name){

        switch (name){
            case "5 cents" : return 0.05;
            case "10 cents": return 0.1;
            case "20 cents": return 0.2;
            case "50 cents": return 0.5;
            case "1 euro": return 1.0;
            case "2 euro": return 2.0;
        }

        return 0;

    }*/

    public double coinIconToValue(String name){
        switch (name){
            case "5c" : return 0.05;
            case "10c": return 0.1;
            case "20c": return 0.2;
            case "50c": return 0.5;
            case "$1": return 1.0;
            case "$2": return 2.0;
        }
        return 0;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
