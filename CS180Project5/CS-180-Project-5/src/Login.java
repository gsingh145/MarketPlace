import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Login
 * <p>
 * Main method which prompts user
 *
 * @author Isaac, Cynthia, Gaurav, Hayden, Albert
 * @version 11/14
 */
public class Login {
    private static final boolean DEBUG_MODE = true;
    private static final String TEST_CUSTOMER_EMAIL = "testcustomer@purdue.edu";
    private static final String TEST_SELLER_EMAIL = "testseller@purdue.edu";


    private static final String ACCOUNTS_FILE_PATH = "accounts.bin";
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    // Map from email (unique id) for each account -> account object
    private static Map<String, Account> accountsMap;
    private boolean logout = false;
    private Server server;


    public Login(Server server) {
        accountsMap = new HashMap<>();
        importAccounts();

        this.server = server;

    }

    public void importAccounts() {
        if (!(new File(ACCOUNTS_FILE_PATH).exists())) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ACCOUNTS_FILE_PATH))) {
            Object nextObject;
            while ((nextObject = ois.readObject()) != null) {
                Account acct = (Account) nextObject;
                accountsMap.put(acct.getEmail(), acct);
            }
        } catch (EOFException eofe) {
            System.out.println("Done importing accounts!");
            System.out.println("Emails present: " + accountsMap.keySet()); // debug
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void exportAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ACCOUNTS_FILE_PATH))) {
            for (Account x : accountsMap.values()) {
                oos.writeObject(x);
            }
        } catch (Exception e) {
            System.err.println("Encountered error attempting to export accounts");
            e.printStackTrace();
        }
    }


    public Account logInPrompt() {
        int tries = 0;
        while (tries <= MAX_LOGIN_ATTEMPTS) {
            int option = showLoginInputDialog();
            if (option == RemoteDisplayConstants.CLOSED_OPTION) {
                System.out.println("exiting");
                return null;
            }
            if (option == 1) {

                if (DEBUG_MODE) System.out.println("[DEBUG] Existing accounts: " + accountsMap.keySet());

                //What happens if the user tries to log in, fails, and then decides
                //that they want to create a new account instead?
                String email = "";
                String password = "";
                while (true) {
                    email = server.remoteInputDisplay("Enter email", "Login",
                            RemoteDisplayConstants.QUESTION_MESSAGE);

                    if (email == null) {
                        System.out.println("returning back to login prompt");
                        break;
                    }

                    password = server.remoteInputDisplay("Enter password", "Login",
                            RemoteDisplayConstants.QUESTION_MESSAGE);
                    if (password == null) {
                        System.out.println("returning back to login prompt");
                        break;
                    }

                    Account accountAtEmail = accountsMap.get(email);
                    if (email.isEmpty() || password.isEmpty()) {
                        server.remoteMessageDisplay("There was no inputted email or password!", "Error",
                                RemoteDisplayConstants.ERROR_MESSAGE);
                    } else if (accountAtEmail != null) {
                        System.out.println(password);
                        if (accountAtEmail.authenticateAccount(password)) {
                            server.remoteMessageDisplay("Successfully signed in!", "Login",
                                    RemoteDisplayConstants.PLAIN_MESSAGE);
                            return accountAtEmail;
                        } else {
                            server.remoteMessageDisplay("Wrong email or password",
                                    "Login", RemoteDisplayConstants.ERROR_MESSAGE);
                        }
                    } else {
                        server.remoteMessageDisplay("Wrong email or password",
                                "Error", RemoteDisplayConstants.ERROR_MESSAGE);
                    }


                    tries++;
                    System.out.println(tries);
                    if (tries > MAX_LOGIN_ATTEMPTS) {

                        server.remoteMessageDisplay(String.format("You exceeded the maximum of %d login attempts. " +
                                "Try again " +
                                "later!", MAX_LOGIN_ATTEMPTS), "Error", RemoteDisplayConstants.ERROR_MESSAGE);
                        break;
                    }
                }
            } else if (option == 2) {
                while (true) {
                    String email = server.remoteInputDisplay("Enter email", "Login",
                            RemoteDisplayConstants.QUESTION_MESSAGE);
                    if (email == null) {
                        System.out.println("returning back to login prompt");
                        break;
                    }
                    if (email.isEmpty()) {
                        continue;
                    }
                    while (accountsMap.containsKey(email)) {
                        server.remoteMessageDisplay("Email is already in use! Try another email.",
                                "Error", RemoteDisplayConstants.ERROR_MESSAGE);
                        email = server.remoteInputDisplay("Enter email", "Login",
                                RemoteDisplayConstants.QUESTION_MESSAGE);
                    }
                    String password = server.remoteInputDisplay("Enter password", "Login",
                            RemoteDisplayConstants.QUESTION_MESSAGE);
                    if (password == null) {
                        System.out.println("returning back to login prompt");
                        break;
                    }
                    if (password.isEmpty()) {
                        continue;
                    }

                    int role = showRoleInputDialog();
                    if (role == 1) {
                        Customer newCustomer = new Customer(email, password);
                        accountsMap.put(email, newCustomer);
                        return newCustomer;
                    } else if (role == 2) {
                        Seller newSeller = new Seller(email, password);
                        accountsMap.put(email, newSeller);
                        return newSeller;
                    } else if (role == RemoteDisplayConstants.CLOSED_OPTION) {
                        System.out.println("exiting");
                        break;
                    }
                    break;
                }
                // test account information
            } else if (option == 3 && DEBUG_MODE) {
                Customer newCustomer = new Customer(TEST_CUSTOMER_EMAIL, "test1");
                accountsMap.put(TEST_CUSTOMER_EMAIL, newCustomer);
                Seller newSeller = new Seller(TEST_SELLER_EMAIL, "test2");
                accountsMap.put(TEST_SELLER_EMAIL, newSeller);
                System.out.println("Created test customer account (testcustomer@purdue.edu) and test seller " +
                        "account " + "(testseller@purdue.edu)");
            } else if (option == 4 && DEBUG_MODE) {
                // Auto-login as test seller. Use this option to avoid having to type email + password
                Account accountAtEmail = accountsMap.get(TEST_SELLER_EMAIL);
                server.remoteMessageDisplay("[DEBUG] Bypassed login as test seller", "DEBUG LOGIN",
                        RemoteDisplayConstants.PLAIN_MESSAGE);
                return accountAtEmail;

            } else if (option == 5 && DEBUG_MODE) {
                // Auto-login as test buyer. Use this option to avoid having to type email + password
                Account accountAtEmail = accountsMap.get(TEST_CUSTOMER_EMAIL);
                server.remoteMessageDisplay("[DEBUG] Bypassed login as test CUSTOMER", "DEBUG LOGIN",
                        RemoteDisplayConstants.PLAIN_MESSAGE);
                return accountAtEmail;

            }
        }
        return null;
    }

    public int showRoleInputDialog() {
        String[] options = {"1. Customer", "2. Seller"};
        int role = server.remoteOptionDisplay("Select role", "Marketplace",
                RemoteDisplayConstants.INFORMATION_MESSAGE, options);
        return role + 1;
    }

    public int showLoginInputDialog() {
        String[] options = {"1. Login", "2. Create Login"};
        String loginPrompt = "Select login option";
        if (DEBUG_MODE) {
            options = new String[]{"1. Login", "2. Create Login", "3. (DEBUG) Test Accounts",
                                   "4. (DEBUG) Login as test seller", "5. (DEBUG) Login as test buyer"
            };
            loginPrompt = "Select login option";
        }
        int loginOption = server.remoteOptionDisplay(loginPrompt, "Marketplace",
                RemoteDisplayConstants.INFORMATION_MESSAGE, options);
        return loginOption + 1;
    }

    public void sellerPrompt(Seller seller, Object gatekeeper) {
        String[] options = {"0: View Stores", "1: View All Products", "2: Create Store"
                , "3: Create Product", "4: Edit Product", "5: Delete Product", "6: View Sales List"
                , "7: Export Products", "8: Import Products", "9: View Stats", "10: View Shopping cart", "11: Logout"};
        String x = server.remoteInputDisplayArray("Seller Menu", "Seller Menu",
                RemoteDisplayConstants.PLAIN_MESSAGE, options);
        int option = Integer.parseInt(x.substring(0, x.indexOf(":")));
        synchronized (gatekeeper) {
            seller.setActiveServer(this.server);
            switch (option) {
                case -1:
                case 11:
                    try {
                        server.sendQuit();
                    } catch (Exception ignored) {
                        int q = 0;
                    }
                    logout = true;
                    break;
                case 0:
                    int hasStores = seller.displayStores();
                    if (hasStores == -1) {
                        server.remoteMessageDisplay("You have no stores, create one first!",
                                "Error", RemoteDisplayConstants.ERROR_MESSAGE);
                    }
                    break;
                case 1:
                    int hasProducts = seller.displayProducts();
                    if (hasProducts == -1) {
                        server.remoteMessageDisplay("You have no products, create one first!",
                                "Error", RemoteDisplayConstants.ERROR_MESSAGE);
                    }
                    break;
                case 2:
                    String nameOfStore = server.remoteInputDisplay("Enter store name",
                            "Seller Menu", RemoteDisplayConstants.PLAIN_MESSAGE);
                    if (nameOfStore == null) {
                        break;
                    }
                    seller.createStore(nameOfStore);
                    break;
                case 3:
                    seller.createProduct();
                    break;
                case 4:
                    if (seller.getOwnedStores().size() == 0) {
                        server.remoteMessageDisplay("You have no stores, create one first!",
                                "Error", RemoteDisplayConstants.ERROR_MESSAGE);
                        break;
                    }
                    if (seller.getAllProducts().size() == 0) {
                        server.remoteMessageDisplay("You have no products, create one first!",
                                "Error", RemoteDisplayConstants.ERROR_MESSAGE);
                        break;
                    }
                    seller.editProduct();
                    break;
                case 5:
                    seller.deleteProduct();
                    break;
                case 6:
                    seller.displaySalesList();
                    break;
                case 7:
                    JFileChooser fccc = new JFileChooser();
                    ClientFileInput cfi = new ClientFileInput();
                    fccc.setDialogTitle("Enter file name to Export to");
                    int num = fccc.showOpenDialog(null);
                    File fileExport = fccc.getSelectedFile();
                    cfi.sellerExportFile(fileExport, seller.getAllProducts());
                    break;
                case 8:
                    JFileChooser fcc = new JFileChooser();
                    fcc.setDialogTitle("Enter file name to import from");
                    int selectorResponse = fcc.showOpenDialog(null);
                    File fil = fcc.getSelectedFile();
                    ArrayList<Product> products = ClientFileInput.sellerImportFile(fil);
                    seller.importProducts(products, accountsMap);
                    break;
                case 9:
                    seller.displayStats(seller);
                    break;
                case 10:
                    for (Account account3 : accountsMap.values()) {
                        seller.showCarts(account3);
                    }
                    break;
            }
        }
    }

    public void customerPrompt(Customer customer, Object gatekeeper) {
        String[] options = {"0: Show marketplace", "1: View previously purchased items", "2: Export file"
                , "3: View stats", "4: View shopping cart", "5: Logout"};

        String x = server.remoteInputDisplayArray("Customer Menu", "Customer Menu",
                RemoteDisplayConstants.PLAIN_MESSAGE, options);
        int option = Integer.parseInt(x.substring(0, x.indexOf(":")));
        ClientFileInput fi = new ClientFileInput();
        synchronized (gatekeeper) {
            customer.setActiveServer(this.server);

            switch (option) {
                case -1, 5 -> {
                    logout = true;
                    try {
                        server.sendQuit();
                    } catch (Exception ignored) {
                        int q = 0;
                    }
                }
                case 0 -> {
                    Market market = new Market(accountsMap.values(), customer);
                    market.display(server);
                }
                case 1 -> customer.displayPastPurchases();
                case 2 -> {
                    JFileChooser fc = new JFileChooser();
                    fc.setDialogTitle("Choose file");
                    int fileSelectOutput = fc.showOpenDialog(null);
                    File f = fc.getSelectedFile();
                    fi.sellerExportFile(f, customer.getProducts());
                }
                case 3 -> {
                    customer.stats(accountsMap.values(), customer);
                }
                case 4 -> {
                    int choice;
                    do {
                        String cart = "Your cart has " + customer.getShoppingCart().size() +
                                " products.\n" + "product name, " +
                                "description, quantity, price";
                        for (int i = 0; i < customer.getShoppingCart().size(); i++) {
                            cart += "\n" + customer.getShoppingCart().get(i).toString();
                        }
                        server.remoteMessageDisplay(cart, "Customer Shopping Cart",
                                RemoteDisplayConstants.PLAIN_MESSAGE);
                        String[] choices = new String[]{"1:add", "2:remove", "3:purchase all", "4:quit"};
                        choice = server.remoteOptionDisplay("Select an action",
                                "Customer Shopping Cart", RemoteDisplayConstants.QUESTION_MESSAGE, choices);
                        if (choice == 0) {
                            customer.displayProducts(accountsMap.values());
                            String itemName = server.remoteInputDisplay("Enter name",
                                    "Customer Shopping Cart", RemoteDisplayConstants.PLAIN_MESSAGE);
                            int inputQuantity = Integer.parseInt(server.remoteInputDisplay("Enter quantity",
                                    "Customer " + "Shopping Cart", RemoteDisplayConstants.PLAIN_MESSAGE));
                            customer.shoppingCartAdd(accountsMap.values(), itemName, inputQuantity);
                        } else if (choice == 1) {
                            customer.displayCart();
                            String productName = server.remoteInputDisplay("Enter product name to remove",
                                    "Customer " +
                                            "Shopping Cart", RemoteDisplayConstants.PLAIN_MESSAGE);
                            customer.shoppingCartRemove(accountsMap.values(), productName);
                        } else if (choice == 2) {
                            customer.shoppingCartPurchaseAll();
                        }
                    } while (choice != 3);
                }
            }
        }
    }

    public boolean isLogout() {
        return logout;
    }
}
