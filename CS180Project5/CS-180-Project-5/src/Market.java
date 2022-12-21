import java.util.ArrayList;
import java.util.Collection;

/**
 * Market
 * <p>
 * Centralized view of all products
 *
 * @author Isaac, Cynthia, Gaurav, Hayden, Albert
 * @version 11/14
 */
public class Market {
    private Collection<Account> accountsList;
    private Account userAccount;

    public Market(Collection<Account> accountsList, Account userAccount) {
        this.accountsList = accountsList;
        this.userAccount = userAccount;
    }

    public ArrayList<Product> sortByPrice(ArrayList<Product> found) {
        for (int i = 0; i < found.size(); i++) {
            // Inner nested loop pointing 1 index ahead
            for (int j = i + 1; j < found.size(); j++) {

                // Checking elements
                Product temp;
                if (found.get(j).getPrice() < found.get(i).getPrice()) {
                    // Swapping
                    temp = found.get(i);
                    found.set(i, found.get(j));
                    found.set(j, temp);
                }
            }

        }
        return found;
    }


    public ArrayList<Product> sortByQuantity(ArrayList<Product> found) {
        for (int i = 0; i < found.size(); i++) {
            // Inner nested loop pointing 1 index ahead
            for (int j = i + 1; j < found.size(); j++) {
                // Checking elements
                Product temp;
                if (found.get(j).getQuantity() > found.get(i).getQuantity()) {
                    // Swapping
                    temp = found.get(i);
                    found.set(i, found.get(j));
                    found.set(j, temp);
                }
            }
        }
        return found;
    }

    public void display(Server server) {

        String y = "";
        for (Account account : accountsList) {
            if (account instanceof Seller) {
                for (Store store : ((Seller) account).getOwnedStores()) {
                    for (Product product : store.getProductList()) {
                        y += store.getName() + ";" + product.getName() + ";" + String.format("%.2f",
                                product.getPrice()) + "`";
                    }
                }
            }
        }
        if (y.split("'").length > 0) {
            server.remoteInputDisplayArray("List of products\nstore;product name;price", "Market",
                    RemoteDisplayConstants.PLAIN_MESSAGE, y.split("`"));
            if (userAccount instanceof Customer) {
                String searchTerm = server.remoteInputDisplay("Enter Search Term", "Market",
                        RemoteDisplayConstants.PLAIN_MESSAGE);
                int sortBy = server.remoteOptionDisplay("Sort by price or quantity",
                        "Market", RemoteDisplayConstants.PLAIN_MESSAGE, new String[]{"Price", "Quantity"});

                ArrayList<Product> found = new ArrayList<>();
                for (Account account : accountsList) {
                    if (account instanceof Seller) {
                        for (Store store : ((Seller) account).getOwnedStores()) {
                            if (store.getName().toUpperCase().contains(searchTerm.toUpperCase())) {
                                found.addAll(store.getProductList());
                            } else {
                                for (Product product : store.getProductList()) {
                                    if (product.getName().toUpperCase().contains(searchTerm.toUpperCase())
                                            || product.getDescription().toUpperCase().contains(searchTerm.toUpperCase()
                                    )) {
                                        found.add(product);
                                    }
                                }
                            }
                        }
                    }
                }
                if (sortBy == 1) {
                    found = sortByQuantity(found);
                } else if (sortBy == 0) {
                    found = sortByPrice(found);
                }

                String[] foundArray = new String[found.size()];
                int counter = 0;
                for (Product x : found) {
                    foundArray[counter] = x.toString();
                    counter++;
                }
                if (foundArray.length > 0) {
                    server.remoteInputDisplayArray("List of products matching the search term \n" +
                            "name;description;quantity;price", "Market",
                            RemoteDisplayConstants.PLAIN_MESSAGE, foundArray);
                    int option = server.remoteConfirmDisplay("Would you like to purchase a product?",
                            "Market", RemoteDisplayConstants.PLAIN_MESSAGE);
                    if (option == RemoteDisplayConstants.YES_OPTION) {
                        String productName = server.remoteInputDisplay("Enter product name",
                                "Market", RemoteDisplayConstants.PLAIN_MESSAGE);
                        int productQuantity = 0;
                        try {
                            productQuantity = Integer.parseInt(server.remoteInputDisplay("Enter " +
                                    "product quantity to purchase", "Market",
                                    RemoteDisplayConstants.PLAIN_MESSAGE));
                        } catch (Exception ignored) {
                            int x = 0;
                        }

                        boolean valid = false;
                        for (Product inputtedProduct : found) {
                            if (inputtedProduct.getName().toUpperCase().contains(productName.toUpperCase())) {
                                if (inputtedProduct.getQuantity() - productQuantity >= 0) {
                                    inputtedProduct.setQuantity(inputtedProduct.getQuantity() - productQuantity);
                                    ((Customer) userAccount).getProducts().add(new
                                            Product(inputtedProduct.getStoreName(), inputtedProduct.getName(),
                                            inputtedProduct.getDescription(), productQuantity,
                                            inputtedProduct.getPrice()));
                                    for (Account account : accountsList) {
                                        if (account instanceof Seller) {
                                            for (Store store : ((Seller) account).getOwnedStores()) {
                                                for (Product product : store.getProductList()) {
                                                    if (product.equals(inputtedProduct)) {
                                                        store.getSalesList().add(store.getName() + ";" +
                                                                userAccount.getEmail() +
                                                                ";" + productQuantity * product.getPrice());
                                                        store.setQuantitySold(store.getQuantitySold() +
                                                                productQuantity);
                                                        if (store.getOwner().getAllCustomers().contains((Customer)
                                                                userAccount)) {
                                                            int indexOfStore = store.getOwner().getAllCustomers()
                                                                    .indexOf((Customer) userAccount);
                                                            store.getOwner().getCorrespondingCustomer().set(
                                                                    indexOfStore,
                                                                    store.getOwner().getCorrespondingCustomer()
                                                                            .get(indexOfStore) + productQuantity);

                                                        } else {
                                                            store.getOwner().getAllCustomers().add((Customer)
                                                                    userAccount);
                                                            store.getOwner().getCorrespondingCustomer
                                                                    ().add(productQuantity);
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }
                                    valid = true;
                                } else
                                    server.remoteMessageDisplay("Out of stock", "Error",
                                            RemoteDisplayConstants.ERROR_MESSAGE);
                            }
                        }
                        if (!valid) {
                            server.remoteMessageDisplay("Could not find your product"
                                    , "Error", RemoteDisplayConstants.ERROR_MESSAGE);
                        }

                    }
                } else {
                    server.remoteMessageDisplay("No Products matched the search term", "Error",
                            RemoteDisplayConstants.ERROR_MESSAGE);
                }
            }
        } else {
            server.remoteMessageDisplay("No Products for sale", "Error",
                    RemoteDisplayConstants.ERROR_MESSAGE);

        }


    }

}


