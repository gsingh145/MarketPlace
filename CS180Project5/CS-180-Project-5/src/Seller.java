import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Seller
 * <p>
 * Type of account which stores seller info
 *
 * @author Isaac, Cynthia, Gaurav, Hayden, Albert
 * @version 11/14
 */
public class Seller extends Account {
    static final long serialVersionUID = 352L;

    private ArrayList<Store> ownedStores = new ArrayList<>();

    private ArrayList<Customer> allCustomers = new ArrayList<>();

    private ArrayList<Integer> correspondingCustomer = new ArrayList<>();

    private transient Server activeServer;

    public Seller(String email, String password) {
        super(email, password);
    }

    public void setActiveServer(Server activeServer) {
        this.activeServer = activeServer;
    }

    public void createStore(String name) {
        for (int i = 0; i < ownedStores.size(); i++) {
            if (ownedStores.get(i).getName().equalsIgnoreCase(name)) {
                activeServer.remoteMessageDisplay(String.format("Store with %s already exists. Please enter another " +
                        "name.", name), "Seller", JOptionPane.PLAIN_MESSAGE);
                return;
            }
        }
        ownedStores.add(new Store(this, name));
    }

    public ArrayList<Store> getOwnedStores() {
        return ownedStores;
    }

    public ArrayList<Customer> getAllCustomers() {
        return allCustomers;
    }

    public void displayCustomerList(boolean increasing) {
        if (increasing) {
            for (int i = 0; i < allCustomers.size(); i++) {
                for (int j = i + 1; j < allCustomers.size(); j++) {
                    Customer temp;
                    int temp2;
                    if (correspondingCustomer.get(j) < correspondingCustomer.get(i)) {
                        temp = allCustomers.get(i);
                        allCustomers.set(i, allCustomers.get(j));
                        allCustomers.set(j, temp);
                        temp2 = correspondingCustomer.get(i);
                        correspondingCustomer.set(i, correspondingCustomer.get(j));
                        correspondingCustomer.set(j, temp2);
                    }
                }
            }
        } else {
            for (int i = 0; i < allCustomers.size(); i++) {
                for (int j = i + 1; j < allCustomers.size(); j++) {
                    Customer temp;
                    int temp2;
                    if (correspondingCustomer.get(j) > correspondingCustomer.get(i)) {
                        temp = allCustomers.get(i);
                        allCustomers.set(i, allCustomers.get(j));
                        allCustomers.set(j, temp);
                        temp2 = correspondingCustomer.get(i);
                        correspondingCustomer.set(i, correspondingCustomer.get(j));
                        correspondingCustomer.set(j, temp2);
                    }
                }
            }
        }
        String customerStats = "Customer Stats";
        for (int i = 0; i < correspondingCustomer.size(); i++) {
            customerStats += ("\nCustomer email: " + allCustomers.get(i).getEmail() + " items purchased:" +
                    correspondingCustomer.get(i));
        }
        activeServer.remoteMessageDisplay(customerStats, "Stats by increasing product sales",
                RemoteDisplayConstants.PLAIN_MESSAGE);
    }

    public void displayProductSales(boolean increasing) {
        ArrayList<Product> allSellerProducts = new ArrayList<>();
        ArrayList<Integer> corresponding = new ArrayList<>();
        for (Store x : ownedStores) {
            for (Product y : x.getProductList()) {
                allSellerProducts.add(y);
                corresponding.add(y.getNumberOfSales());
            }
        }
        if (increasing) {
            for (int i = 0; i < allSellerProducts.size(); i++) {
                for (int j = i + 1; j < allSellerProducts.size(); j++) {
                    Product temp;
                    int temp2;
                    if (corresponding.get(j) < corresponding.get(i)) {
                        temp = allSellerProducts.get(i);
                        allSellerProducts.set(i, allSellerProducts.get(j));
                        allSellerProducts.set(j, temp);
                        temp2 = corresponding.get(i);
                        corresponding.set(i, corresponding.get(j));
                        corresponding.set(j, temp2);
                    }
                }
            }
        } else {
            for (int i = 0; i < allSellerProducts.size(); i++) {
                for (int j = i + 1; j < allSellerProducts.size(); j++) {
                    Product temp;
                    int temp2;
                    if (corresponding.get(j) > corresponding.get(i)) {
                        temp = allSellerProducts.get(i);
                        allSellerProducts.set(i, allSellerProducts.get(j));
                        allSellerProducts.set(j, temp);
                        temp2 = corresponding.get(i);
                        corresponding.set(i, corresponding.get(j));
                        corresponding.set(j, temp2);
                    }
                }
            }
        }
        String salesStats = "Product Sales";
        for (int i = 0; i < allSellerProducts.size(); i++) {
            salesStats += ("\nProduct name: " + allSellerProducts.get(i).getName() + " Number of sales: " +
                    corresponding.get(i));
        }
        activeServer.remoteMessageDisplay(salesStats, "Stats by increasing product sales",
                RemoteDisplayConstants.PLAIN_MESSAGE);
    }

    public ArrayList<Integer> getCorrespondingCustomer() {
        return correspondingCustomer;
    }

    public void displaySalesList() {
        String fullList = "Store Name;Email;Revenue";
        for (Store store : ownedStores) {
            if (!(store.getSalesList() == null || store.getSalesList().isEmpty())) {
                for (String output : store.getSalesList()) {
                    fullList += "\n" + output;
                }
            }
        }
        activeServer.remoteMessageDisplay(fullList, null, RemoteDisplayConstants.PLAIN_MESSAGE);
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> all = new ArrayList<>();
        for (Store store : ownedStores) {
            all.addAll(store.getProductList());
        }
        return all;
    }

    public int displayStores() {
        String storeList = "List of Stores";
        if (getOwnedStores().size() == 0) {
            return -1;
        }
        for (int i = 0; i < getOwnedStores().size(); i++) {
            storeList += ("\nStore " + (i + 1) + ": " + getOwnedStores().get(i).getName());
        }
        activeServer.remoteMessageDisplay(storeList, "Seller Menu", RemoteDisplayConstants.PLAIN_MESSAGE);
        return 0;
    }

    public int chooseStore() {
        boolean hasStores = false;
        //checking what is in getOwnedStores
        String storeList = "Please Enter a Store Number\nList of Stores";

        for (int i = 0; i < getOwnedStores().size(); i++) {
            storeList += ("\nStore " + (i + 1) + ": " + getOwnedStores().get(i).getName());
            hasStores = true;
        }
        while (true) {
            if (hasStores) {
                try {
                    String storeSelection = activeServer.remoteInputDisplay(storeList, "Seller Menu",
                            RemoteDisplayConstants.PLAIN_MESSAGE);
                    if (storeSelection == null) {
                        return -2;
                    } else if (Integer.parseInt(storeSelection) < 1 || storeSelection.equals("") ||
                            Integer.parseInt(storeSelection) > ownedStores.size()) {
                        activeServer.remoteMessageDisplay("Please enter a valid store number!", "Error",
                                RemoteDisplayConstants.ERROR_MESSAGE);
                    } else {
                        return Integer.parseInt(storeSelection);
                    }
                } catch (Exception e) {
                    activeServer.remoteMessageDisplay("Please enter a valid store number!", "Error",
                            RemoteDisplayConstants.ERROR_MESSAGE);
                }
            } else {
                return -1;
            }
        }
    }

    public void createProduct() {
        int storeNum = chooseStore();
        if (storeNum == -1) {
            activeServer.remoteMessageDisplay("You have no stores, create one first!", "Error",
                    RemoteDisplayConstants.ERROR_MESSAGE);
            return;
        } else if (storeNum == -2) {
            return;
        }
        String name = activeServer.remoteInputDisplay("Enter Product Name", "Create Product",
                RemoteDisplayConstants.QUESTION_MESSAGE);
        if (name == null) {
            return;
        }
        String description = activeServer.remoteInputDisplay("Enter Description", "Create Product",
                RemoteDisplayConstants.QUESTION_MESSAGE);
        if (description == null) {
            return;
        }
        int quantity;
        while (true) {
            try {
                String tempQuantity = activeServer.remoteInputDisplay("Enter Quantity",
                        "Create Product", RemoteDisplayConstants.QUESTION_MESSAGE);
                quantity = Integer.parseInt(tempQuantity);
                break;
            } catch (Exception e) {
                activeServer.remoteMessageDisplay("Please enter a valid quantity!", "Error",
                        RemoteDisplayConstants.ERROR_MESSAGE);
            }
        }
        double price;
        while (true) {
            try {
                String tempPrice = activeServer.remoteInputDisplay("Enter Price",
                        "Create Product", RemoteDisplayConstants.QUESTION_MESSAGE);
                price = Double.parseDouble(tempPrice);
                break;
            } catch (Exception e) {
                activeServer.remoteMessageDisplay("Please enter a valid price!",
                        "Error", RemoteDisplayConstants.ERROR_MESSAGE);
            }
        }
        getOwnedStores().get(storeNum - 1)
                .getProductList()
                .add(new Product(getOwnedStores().get(storeNum - 1).getName(), name, description, quantity, price));
    }

    public int displayProducts() {
        String productList = "List of Products\nStore,Product Name,Description,Quantity,Price";
        if (getAllProducts().size() == 0) {
            return -1;
        }
        for (int i = 0; i < getAllProducts().size(); i++) {
            productList += ("\nProduct " + (i + 1) + ": " + getAllProducts().get(i).toString());
        }
        activeServer.remoteMessageDisplay(productList, "Seller Menu", RemoteDisplayConstants.PLAIN_MESSAGE);
        return 0;
    }

    public int chooseProducts() {
        boolean hasProducts = false;
        String productList = "Please Select a Product Number\n List of Products:";
        for (int i = 0; i < getAllProducts().size(); i++) {
            productList += ("\nProduct " + (i + 1) + ": " + getAllProducts().get(i).toString());
            hasProducts = true;
        }
        while (true) {
            if (hasProducts) {
                try {
                    String productSelection = activeServer.remoteInputDisplay(productList,
                            "Seller Menu", RemoteDisplayConstants.PLAIN_MESSAGE);
                    if (productSelection == null) {
                        return -2;
                    } else if (Integer.parseInt(productSelection) < 1 || productSelection.equals("")
                            || Integer.parseInt(productSelection) > getAllProducts().size()) {
                        activeServer.remoteMessageDisplay("Please enter a valid product number!",
                                "Error", RemoteDisplayConstants.ERROR_MESSAGE);
                    } else {
                        return Integer.parseInt(productSelection);
                    }
                } catch (Exception e) {
                    activeServer.remoteMessageDisplay("Please enter a valid product number!",
                            "Error", RemoteDisplayConstants.ERROR_MESSAGE);
                }
            } else {
                return -1;
            }
        }
    }

    public void editProduct() {
        try {
            int storeNum = chooseStore();
            while (true) {
                if (storeNum == -1) {
                    activeServer.remoteMessageDisplay("You have no stores, create one first!",
                            "Error", RemoteDisplayConstants.ERROR_MESSAGE);
                    return;
                } else if (storeNum == -2) {
                    return;
                } else {
                    break;
                }
            }


            int selectedProduct = chooseProducts();
            Product editedProduct =
                    getOwnedStores().get(storeNum - 1).getProductList().get(selectedProduct - 1);

            String addedName = activeServer.remoteInputDisplay("Enter New Product Name",
                    "Edit Product", RemoteDisplayConstants.QUESTION_MESSAGE);
            String addedDesc = activeServer.remoteInputDisplay("Enter New Description",
                    "Edit Product", RemoteDisplayConstants.QUESTION_MESSAGE);

            int inputQuantity;
            while (true) {
                try {
                    String tempQuantity = activeServer.remoteInputDisplay("Enter New Quantity",
                            "Edit Product", RemoteDisplayConstants.QUESTION_MESSAGE);
                    inputQuantity = Integer.parseInt(tempQuantity);
                    break;
                } catch (Exception e) {
                    activeServer.remoteMessageDisplay("Please enter a valid quantity!", "Error",
                            RemoteDisplayConstants.ERROR_MESSAGE);
                }
            }
            double addedPrice;
            while (true) {
                try {
                    String tempPrice = activeServer.remoteInputDisplay("Enter New Price",
                            "Edit Product", RemoteDisplayConstants.QUESTION_MESSAGE);
                    addedPrice = Double.parseDouble(tempPrice);
                    break;
                } catch (Exception e) {
                    activeServer.remoteMessageDisplay("Please enter a valid price!",
                            "Error", RemoteDisplayConstants.ERROR_MESSAGE);
                }
            }

            if (addedDesc != null) editedProduct.setDescription(addedDesc);
            if (addedName != null) editedProduct.setName(addedName);
            if (addedPrice != 0) editedProduct.setPrice(addedPrice);
            editedProduct.setQuantity(inputQuantity);
        } catch (Exception e) {
            activeServer.remoteMessageDisplay("Error in editing product, please try again!",
                    "Error", RemoteDisplayConstants.ERROR_MESSAGE);
        }
    }

    public void deleteProduct() {
        int storeNum = chooseStore();
        while (true) {
            if (storeNum == -1) {
                activeServer.remoteMessageDisplay("You have no stores, create one first!",
                        "Error", RemoteDisplayConstants.ERROR_MESSAGE);
                return;
            } else if (storeNum == -2) {
                return;
            } else {
                break;
            }
        }
        int deleteProduct = chooseProducts();
        while (true) {
            if (deleteProduct == -1) {
                activeServer.remoteMessageDisplay("You have no products, create one first!",
                        "Error", RemoteDisplayConstants.ERROR_MESSAGE);
                return;
            } else if (deleteProduct == -2) {
                return;
            } else {
                break;
            }
        }
        ownedStores.get(storeNum - 1).getProductList().remove(deleteProduct - 1);
    }

    public void importProducts(ArrayList<Product> products, Map<String, Account> accountsMap) {
        for (Product product : products) {
            boolean added = false;
            for (Account acct : accountsMap.values()) {
                if (acct instanceof Seller) {
                    for (Store store2 : ((Seller) acct).getOwnedStores()) {
                        if (store2.getName().equals(product.getStoreName())) {
                            store2.getProductList().add(product);
                            added = true;
                        }
                    }
                }
            }
            if (!added) {
                createStore(product.getStoreName());
                ownedStores.get(ownedStores.size() - 1).getProductList().add(product);
            }
        }
    }

    public void displayStats(Seller account) {
        int listBy;
        while (true) {
            try {
                String options = "Please Select List\n1: List Customer by Items\n2: List Products by Sales";
                String listChoice = activeServer.remoteInputDisplay(options, "View Stats",
                        RemoteDisplayConstants.PLAIN_MESSAGE);
                if (listChoice == null) {
                    return;
                } else if (listChoice.equals("") || Integer.parseInt(listChoice) > 2 ||
                        Integer.parseInt(listChoice) <= 0) {
                    activeServer.remoteMessageDisplay("Please enter a valid list number!", "Error",
                            RemoteDisplayConstants.ERROR_MESSAGE);
                } else {
                    listBy = Integer.parseInt(listChoice);
                    break;
                }
            } catch (Exception e) {
                activeServer.remoteMessageDisplay("Please enter a valid list number!", "Error",
                        RemoteDisplayConstants.ERROR_MESSAGE);
            }
        }
        int sort;
        while (true) {
            try {
                String order = "Sort by\n1: increasing\n2: decreasing";
                String orderChoice = activeServer.remoteInputDisplay(order, "View Stats",
                        RemoteDisplayConstants.PLAIN_MESSAGE);
                if (orderChoice == null) {
                    return;
                } else if (orderChoice.equals("") || Integer.parseInt(orderChoice) > 2 ||
                        Integer.parseInt(orderChoice) <= 0) {
                    activeServer.remoteMessageDisplay("Please enter a valid order number!", "Error",
                            RemoteDisplayConstants.ERROR_MESSAGE);
                } else {
                    sort = Integer.parseInt(orderChoice);
                    break;
                }
            } catch (Exception e) {
                activeServer.remoteMessageDisplay("Please enter a valid order number!", "Error",
                        RemoteDisplayConstants.ERROR_MESSAGE);
            }
        }
        if (listBy == 1) {
            account.displayCustomerList(sort == 1);
        } else {
            account.displayProductSales(sort == 1);
        }
    }

    public void showCarts(Account account3) {
        if (account3 instanceof Customer) {
            String cart = ("Customer has " + ((Customer) account3).getShoppingCart().size() +
                    " products in cart\nStore Name, Product Name, Description, Quantity, Price");
            for (Product shop : ((Customer) account3).getShoppingCart()) {
                cart += ("\n" + shop.getStoreName() + "," + shop);
            }
            activeServer.remoteMessageDisplay(cart, "Customer Carts", RemoteDisplayConstants.INFORMATION_MESSAGE);
        }
    }
}
