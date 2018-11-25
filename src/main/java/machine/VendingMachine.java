package machine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


public class VendingMachine
{
    private Hashtable<String,LineItem> products;
    private CoinSet coinSet;
    private int purchasedProducts;
    public final static int MAX_AMOUNT = 20;

    public VendingMachine(String productsFileName, String coinsFileName)
    {
        products = Util.readProductsFromFile(productsFileName);
        coinSet = new CoinSet(coinsFileName);

    }

    public VendingMachine(){

        products = new Hashtable<>();
        coinSet = new CoinSet();

    }

    public CoinSet getCoinSet(){
        return coinSet;
    }

    public Hashtable<String,LineItem> getProductsTable(){
        return products;
    }


    public void addProduct(String name,LineItem lineItem){
        if(!getProductsTable().containsKey(name)){
            getProductsTable().put(name,lineItem);
        }
    }

    public boolean addProduct(String name,String des,int quantity, double amount, String src){
        if(!getProductsTable().containsKey(name)){
            getProductsTable().put(name,new LineItem(name,des,quantity,amount,src));
            return true;
        }
        return false;
    }

    public void removeProduct(String name){
            getProductsTable().remove(name);
    }

    public double giveChange() {
        double[] cents = {2.0, 1.0, 0.5, 0.2, 0.1, 0.05};
        double change = BigDecimal.valueOf(getCoinSet().getTotal()).setScale(3, RoundingMode.HALF_UP).doubleValue();
        int cent, counter = 0, i = 0;
        while (change > 0) {
            cent = (int) (change / cents[i]);
            for (int j = 0; j < cents.length; j++) {
                if (cents[j] <= change) {
                    if (Integer.parseInt(getCoinSet().getCoinsTable().get(Double.toString(cents[j]))) >= cent) {
                        change = BigDecimal.valueOf(change - (cents[j] * cent)).setScale(3, RoundingMode.HALF_UP).doubleValue();
                        getCoinSet().removeCoinsFromTable(Double.toString(cents[j]),cent);
                    }

                }
                counter++;
            }

            if (counter >= 25) {
                break;
            }
            i++;
        }
        return change;
    }

    public void purchaseProduct(String name) {
        Product product = getProductsTable().get(name).getProduct();
        double priceOfProduct = product.getPrice();
        if (getCoinSet().getTotal() >= priceOfProduct) {
            try {
                getProductsTable().get(product.getName()).removeProducts(1);
                setPurchasedProducts(getPurchasedProducts()+1);
            }catch (VendingException e){
                e.getLocalizedMessage();
            }
            getCoinSet(). setTotal(BigDecimal.valueOf(getCoinSet().getTotal() - product.getPrice()).setScale(3,RoundingMode.HALF_UP).doubleValue());
            giveChange();
        }
    }

    private double price(ArrayList<Double> list){
       double total = 0;
       for(Double num : list){
           total += num;
       }
       return total;
    }


    public void printProducts(String userType){
        Set<String> keys = getProductsTable().keySet();
        if(keys.isEmpty())System.out.println("No Products Have Been Yet Added");
        else {
            for (String key : keys) {
                if(userType.equals("admin")) {
                    System.out.println(key + " Price : " + getProductsTable().get(key).getProduct().getPrice() + " Quantity : " + getProductsTable().get(key).getAmount());
                }
            }
        }
    }

    public LineItem[] lineItems(){

        Set<String> keys = getProductsTable().keySet();
        LineItem[] items = new LineItem[keys.size()];
        int i = 0;
        for(String key : keys){
            items[i] = getProductsTable().get(key);
            i++;
        }

        return items;
    }

    public int getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(int purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

}
