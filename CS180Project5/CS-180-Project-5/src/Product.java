import java.io.Serializable;

//GAURAV SINGH

/**
 * Product
 * <p>
 * Stores all product info
 *
 * @author Isaac, Cynthia, Gaurav, Hayden, Albert
 * @version 11/14
 */
public class Product implements Serializable {


    private String name;
    private String storeName;
    private String description;
    private int quantity;
    private double price;
    private int originalQuantity;

    public Product(String storeName, String name, String description, int quantity, double price) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.storeName = storeName;
        originalQuantity = quantity;
    }

    public String toString() {
        return String.format("%s, %s, %s, %d, %.2f", name, storeName, description, quantity, price);

//        return String.format("%s @ %s: %s | %d units at $%.2f", name, storeName, description, quantity, price);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumberOfSales() {
        return originalQuantity - quantity;
    }


}

