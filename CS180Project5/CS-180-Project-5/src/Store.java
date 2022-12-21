import java.io.Serializable;
import java.util.ArrayList;

/**
 * Store
 * <p>
 * Store info about seller's store
 *
 * @author Isaac, Cynthia, Gaurav, Hayden, Albert
 * @version 11/14
 */
public class Store implements Serializable {
    private Seller owner;
    private String name;

    private ArrayList<Product> products = new ArrayList<>();
    private int quantitySold;

    private ArrayList<String> salesList = new ArrayList<>();

    public Store(Seller owner, String name) {
        this.owner = owner;
        this.name = name;
    }

    /**
     * Constructor exists for testing purposes only.
     */
    Store(Seller owner, String name, int fakeQuantitySold) {
        this.owner = owner;
        this.name = name;
        this.quantitySold = fakeQuantitySold;
    }

    public Seller getOwner() {
        return owner;
    }

    public ArrayList<Product> getProductList() {
        return products;
    }

    public ArrayList<String> getSalesList() {
        return salesList;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Store" +
                "name='" + name + '\'' +
                '}';
    }
}
