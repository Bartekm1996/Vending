package machine;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

import java.io.File;


/**
 A product in a vending machine.
 */

public class Product
{
    private String name;
    private double price;
    private ImageView imageView;
    private Image image;
    /**
     Constructs a Product object
     @param aDescription the description of the product
     @param aPrice the price of the product
     */
    public Product(String aDescription, double aPrice)
    {
        name = aDescription;
        price = aPrice;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView() {
        this.imageView.setImage(getImage());
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

        public Product(String des, double price, String src){
            this.name = des;
        this.price = price;
        this.image = new Image(new File(src).toURI().toString());
        this.imageView = new ImageView(getImage());

    }
}
