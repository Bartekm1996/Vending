package machine;

import javafx.scene.image.Image;

public class LineItem{


    private int amount;
    private String name;
    private Product product;

    public LineItem(String name, int quantity){
        this.amount = quantity;
        this.name = name;
        this.product = new Product(name,0,null);
    }

    public LineItem(String name, String des, int quantity, double price,String src){
        this.name = name;
        this.amount = quantity;
        this.product = new Product(des,price,src);
    }

    public Product getProduct() {
        return product;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount(){
        return this.amount;
    }

    public void removeProducts(int amount)throws VendingException{
        if(getAmount() == 0){
            throw new RuntimeException("Product sold out");
        }else setAmount(getAmount() - amount);
    }

    public void addProducts(int amount){
        setAmount(getAmount() + amount);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
