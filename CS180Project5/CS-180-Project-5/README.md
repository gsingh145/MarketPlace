Executing the program:

- All files are automatically created by the program except for when a seller wants to import products. I have included an example of seller import file named importProducts.csv that you can use to test and exportProduct.csv
- Delete the account.bins file to reset any previously stored login info. The program will automatically create another loginInfo file
- Compile all the Java files
- Run the main method within the Server class
- Run the main within the Client class
- Create login for seller and customer
 
Submissions:

Code
- Gaurav Singh

Report
- Albert Wu

Presentation 
- Cynthia Liu
 
Class Descriptions:

Account

- Account is a superclass that reduces code redundancy between other classes that have the account type. This class is extended by Seller and Customer classes. It uses getter methods for the email and password. It implements Serializable in order for an account to be output to a file using the object output stream. The getEmail and getPassword methods were tested to properly return the correct email and password for that corresponding user.
 
Customer

- Customer is a class that extends the Account class, it has the email and password instance variables. Customer stores its own shopping cart array list and previously purchased products array list. It also includes sorting methods for the statistics overview. We tested the sorting methods by comparing expected and actual output.
 
Seller

- Seller is also a class that extends the Account class. It has an array list of the Store objects that Seller owns and an array list of all the Customer users who have purchased from this Sellers store along with an array list of the quantity purchased by that specific Customer at this Seller’s store. It also includes sorting methods for the statistics overview. We tested the sorting methods by comparing expected and actual output.
 
ClientFileInput

- ClientFileInput is for importing and exporting csv files. The Export method can be used by either Seller or Customer whereas the import method can only be used by Seller. We tested the files and verified that their import matches the expected input. A user chooses when to import or export a file in the login main method.
 
Login

- Login has the main method for prompting the user. It also reads and writes all the created accounts into an Object output stream so that data is persistent. The main method calls and creates methods from other classes. It also handles any exceptions which may occur. We tested the main method thoroughly and made sure the expected output matches the actual. 
 
Store

- Store objects are stored in the Seller class’ ownedStores arraylist. A store has an arraylist of the current products that they have and an arraylist of previous sales. It has multiple get methods to return store variables. Store was tested to display the correct product history and store the correct products. 
 
Product

- Sellers can create products and store it in one of their stores. Customers can buy products to add it to their shopping cart or to add it to their owned products. Products have multiple instance variables to help identify the product. It also has many get methods to return its instance variables. The main method often uses these get methods to display info about products which were tested and displayed the correct String.
 
Market

- Market displays the centralized view for all the products and stores to the user. The login class can ask a customer user if he wants to view this dashboard. From there a customer can purchase items in the marketplace. We tested the marketplace dashboard to output the correct expected strings to the console. 

RemoteDisplayOption

- RemoteDisplayOption gives both the Client and Server classes access to important constants. These constants tell the client what type of JOptionPane the server wants the user to display. 

RemoteDisplayConstants

- RemoteDisplayConstant gives both the Client and Server classes access to other important constants. These constants tell the client what type of message or option that is available to a certain dialog the server wants the user to display. 

Server
- Contains helper methods that tell client what GUI dialogs to display and sends information to the client to display.g

Client
- Displays the user interface of the marketplace. It communicates with the server to retrieve account and market 
  information to display.
 
## Team member assignments:
 
Isaac Arvin

- Seller's statistics , Report
 
Albert Wu 

- Customer's shopping cart , Report , GUI ,concurrency, networking
 
Hayden Walters 

- Seller's shopping cart , GUI ,concurrency, networking
 
Gaurav Singh 

- Files , Main  method (Login class) , Marketplace , Read me , GUI ,concurrency, networking
 
Cynthia Liu 

- Customer's statistics , Test cases , GUI ,concurrency, networking , Login class
 





