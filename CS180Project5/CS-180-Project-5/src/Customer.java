import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Customer
 * <p>
 * Stores a type of account that holds customer info
 *
 * @author Isaac, Cynthia, Gaurav, Hayden, Albert
 * @version 11/14
 */
public class Customer extends Account {
    static final long serialVersionUID = 354L;

    private ArrayList<Product> products = new ArrayList<>();

    private ArrayList<Product> shoppingCart = new ArrayList<>();

    private transient Server activeServer;

    public Customer(String email, String password) {
        super(email, password);
    }

    public static List<Store> sortStoresByProductsSold(List<Store> list, boolean increasing) {
        int lessThanComparatorDirection = increasing ? -1 : 1;
        int greaterThanComparatorDirection = increasing ? 1 : -1;
        list.sort((a, b) -> a.getQuantitySold() < b.getQuantitySold() ? lessThanComparatorDirection :
                a.getQuantitySold() == b.getQuantitySold()
                        ? 0 : greaterThanComparatorDirection);
        return list;
    }

    public void setActiveServer(Server activeServer) {
        this.activeServer = activeServer;
    }

    public ArrayList<Product> getShoppingCart() {
        return shoppingCart;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void displayPastPurchases() {
        String[] options = new String[products.size()];
        int counter = 0;
        for (Product x : products) {
            options[counter] = x.toString();
            counter++;
        }
        if (products.size() > 0) {
            activeServer.remoteInputDisplayArray("Past Purchases\nname, description, quantity, price",
                    "Customer Menu", RemoteDisplayConstants.PLAIN_MESSAGE, options);
        } else {
            activeServer.remoteMessageDisplay("No products found", "Error",
                    RemoteDisplayConstants.ERROR_MESSAGE);
        }

    }

    public void sortStoresByCustomerPurchases(ArrayList<Store> store, boolean increasing) {
        ArrayList<Store> stores = store;
        ArrayList<Integer> correspondingStore = new ArrayList<>();
        for (int i = 0; i < stores.size(); i++) {
            correspondingStore.add(0);
        }
        for (int i = 0; i < stores.size(); i++) {
            int counter = 0;
            for (int j = 0; j < products.size(); j++) {
                if (products.get(j).getStoreName().equals(stores.get(i).getName())) {
                    counter += products.get(j).getQuantity();
                }
            }
            correspondingStore.set(i, counter);
        }

        if (increasing) {
            for (int i = 0; i < stores.size(); i++) {
                for (int j = i + 1; j < stores.size(); j++) {
                    Store temp;
                    int tempInt = 0;
                    if (correspondingStore.get(j) < correspondingStore.get(i)) {
                        temp = stores.get(i);
                        stores.set(i, stores.get(j));
                        stores.set(j, temp);
                        tempInt = correspondingStore.get(i);
                        correspondingStore.set(i, correspondingStore.get(j));
                        correspondingStore.set(j, tempInt);
                    }
                }
            }
        } else {
            for (int i = 0; i < stores.size(); i++) {
                for (int j = i + 1; j < stores.size(); j++) {
                    Store temp;
                    int tempInt = 0;
                    if (correspondingStore.get(j) > correspondingStore.get(i)) {
                        temp = stores.get(i);
                        stores.set(i, stores.get(j));
                        stores.set(j, temp);
                        tempInt = correspondingStore.get(i);
                        correspondingStore.set(i, correspondingStore.get(j));
                        correspondingStore.set(j, tempInt);
                    }
                }
            }

        }
        int count = 0;
        for (Store x : stores) {
            activeServer.remoteMessageDisplay("Quantity bought at Store " + x.getName() + ": " +
                    correspondingStore.get(count), "Marketplace", RemoteDisplayConstants.PLAIN_MESSAGE);
            count++;
        }

    }

    public void stats(Collection<Account> accountList, Customer account) {
        String[] sortingMethods = new String[]{"1: List stores by products sold in total", "2: List stores by " +
                "products purchased by you"};
        int listBy = activeServer.remoteOptionDisplay("Select how you want to sort the products",
                "Customer Stats", RemoteDisplayConstants.QUESTION_MESSAGE,
                sortingMethods);


        String[] order = new String[]{"1: increasing", "2: decreasing"};
        int sort = activeServer.remoteOptionDisplay("Sort by:", "Customer Stats",
                RemoteDisplayConstants.QUESTION_MESSAGE, order);

        ArrayList<Store> allStores = listOfStores(accountList);
        ArrayList<Customer> allCustomers = listOfCustomers(accountList);
        ArrayList<Store> storeOutput;

        if (listBy == 1) {
            storeOutput = (ArrayList<Store>) account.sortStoresByProductsSold(allStores, sort == 1);
            for (Store x : storeOutput) {
                activeServer.remoteMessageDisplay(x.getName() + " Quantity sold:" +
                                x.getQuantitySold(), "Customer",
                        RemoteDisplayConstants.PLAIN_MESSAGE);
            }
        } else {
            account.sortStoresByCustomerPurchases(allStores, sort == 1);
        }
    }

    public ArrayList<Store> listOfStores(Collection<Account> accountList) {
        ArrayList<Store> allStores = new ArrayList<>();
        for (Account a : accountList) {
            if (a instanceof Seller) {
                allStores.addAll(((Seller) a).getOwnedStores());
            }
        }
        return allStores;
    }

    public ArrayList<Customer> listOfCustomers(Collection<Account> accountList) {
        ArrayList<Customer> allCustomers = new ArrayList<>();
        for (Account a : accountList) {
            if (a instanceof Customer) {
                allCustomers.add((Customer) a);
            }
        }
        return allCustomers;
    }

    public void displayProducts(Collection<Account> accountList) {
        String all = "";
        for (Account a : accountList) {
            if (a instanceof Seller) {
                for (Store store : ((Seller) a).getOwnedStores()) {
                    for (Product product : store.getProductList()) {
                        all = product.toString() + "`";
                    }
                }
            }
        }
        activeServer.remoteInputDisplayArray("List of avaiable products",
                "Customer Menu", RemoteDisplayConstants.PLAIN_MESSAGE, all.split("`"));
    }

    public void shoppingCartAdd(Collection<Account> accountList, String itemName, int quantity) {
        for (Account a : accountList) {
            if (a instanceof Seller) {
                for (Store store : ((Seller) a).getOwnedStores()) {
                    for (Product product : store.getProductList()) {
                        if (product.getName().toUpperCase().contains(itemName.toUpperCase())) {

                            if ((product.getQuantity() - quantity) >= 0) {
                                getShoppingCart().add(product);
                                int indexOfProduct = store.getProductList().indexOf(product);
                                store.getProductList().get(indexOfProduct).setQuantity(
                                        store.getProductList().get(indexOfProduct).getQuantity() - quantity);
                                store.getSalesList().add(store.getName() + ";" +
                                        getEmail() +
                                        ";" + quantity * product.getPrice());
                                store.setQuantitySold(store.getQuantitySold() + quantity);
                            } else {
                                activeServer.remoteMessageDisplay("Out of stock",
                                        "Customer Shopping Cart", RemoteDisplayConstants.PLAIN_MESSAGE);
                            }

                        }
                    }
                }
            }
        }
    }

    public void shoppingCartRemove(Collection<Account> accountList, String productName) {
        for (Product a : getShoppingCart()) {
            activeServer.remoteMessageDisplay(a.toString(), "Customer", RemoteDisplayConstants.PLAIN_MESSAGE);
        }
        for (Account a : accountList) {
            if (a instanceof Seller) {
                for (Store store : ((Seller) a).getOwnedStores()) {
                    //code likes to break here
                    for (Product product : store.getProductList()) {
                        if (product.getName().toUpperCase().contains(productName.toUpperCase())) {
                            getShoppingCart().remove(product);
                            store.getProductList().add(product);
                        }
                    }
                }
            }
        }
    }

    public void shoppingCartPurchaseAll() {
        getProducts().addAll(getShoppingCart());
        getShoppingCart().clear();
    }


    public void findProducts(Collection<Account> accountList) {
        for (Account a : accountList) {
            if (a instanceof Seller) {
                for (Store store : ((Seller) a).getOwnedStores()) {
                    for (Product product : store.getProductList()) {
                        System.out.println(product.toString());
                    }
                }
            }
        }
    }

    public void displayCart() {
        String[] cart = new String[shoppingCart.size()];
        int index = 0;
        for (Product x : shoppingCart) {
            cart[index] = x.toString();
            index++;
        }
        activeServer.remoteInputDisplayArray("Current items in cart",
                "Customer Menu", RemoteDisplayConstants.PLAIN_MESSAGE, cart);
    }
}
