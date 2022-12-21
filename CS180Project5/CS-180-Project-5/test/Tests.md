Test 1: User Create Login
1. User starts the application.
2. User selects "create login".
3. User enters email. 
4. User enters password.
5. User selects their role.

Expected result: If no error messages appear, the customer or seller account menu will pop up. Application will check 
whether the email is not already registered. If it is, it will show an error message and ask the user to enter another 
email. If there is no input for the email or the password, the program will bring the user back to the "enter email" 
dialog. If no error messages appear, the user will select whether they are a customer or a seller and then the customer
or seller account menu will pop up. 

Test 2: User Login
1. User starts the application. 
2. User selects "login".
3. User enters email.
4. User enters password.

Expected result: Application will authenticate whether the inputted email and password. If it is valid, then the 
customer or seller account menu will pop up. An error message will pop up if an email or password is not inputted. 
If the email or password is inputted incorrectly, an error message will pop up. 

Test 3: Seller Menu View Stores and Products
1. Assume user has successfully login as a seller and can view the seller menu.
2. Select option 0 "View stores" in the menu.
3. Select option 1 "View all products" in the menu.

Expected result: The stores display menu will display the stores and the products display menu will list all the 
products the seller has. If there are no stores, selecting option 0 will lead to a message indicating there are no 
stores and a similar message will be displayed after selecting option 1 if there are no products. 

Test 4: Seller Menu Importing Products
1. Assume user has successfully login as a seller and can view the seller menu.
2. Select option 8 "Import products" in the menu.
3. Choose file that contains the products. (i.e. our example file, importProduct)
4. Select option 0 "View stores" in the menu.
5. Select option 1 "View all products" in the menu.

Expected result: The stores display menu will list the store names. The products display menu will list all the 
products the seller has and give information such as the store name, product name, quantity, and price. If the csv file
that contains the products is in the wrong format, the application will show an error message and no products will be 
imported.

Test 5: Seller Menu Create Store, Create Product, Edit Product, Delete Product, and Export Products
1. Assume user has successfully login as a seller and can view the seller menu.
2. Select option 2 "Create Store" in the menu.
3. Enter store name.
4. Select Option 0 "View stores" in the menu. 
5. Select Option 3 "Create product" in the menu.
6. Enter store.
7. Enter product information.
8. Select Option 1 "View all products".
9. Select Option 4 "Edit product".
10. Enter store.
11. Enter product information.
12. Select Option 1 "View all products".
13. Select Option 5 "Delete products".
14. Enter store.
15. Enter product.
16. Select Option 1 "View all products".
17. Select Option 7 "Export products".
18. Choose file to export product information.

Expected Results: A store will be created. It will show up in the list of stores. A product will be created.
It will show up in the list of products. A product will get edited and the change will reflect in the view all 
products display. Then, a product will be deleted. It will disappear from the list of products. Then, the 
products that the seller has will be exported to a file that the user chooses.

Test 6: Customer Menu: 
1. Assume user has successfully login as a customer and can view the customer menu.
2. Select "View Marketplace". There should be information displayed since Test 4 imported products in as a seller.
3. Within marketplace, the user can search for products and sort by price or quantity. 
4. They can purchase a product if they want.
5. After purchasing a product, select "view previous purchases" to view the purchase history.
6. Select "View stats" after a few purchases and this function shows relations between stores, customers and products. 
7. Select "Export files" to store anything that was completed in the customer menu.

Expected Results: A customer will be able to view products sold by the sellers. The user will be able to search for 
products and sort the products. They can purchase the product if they want. They wil be able to view purchase 
history and export their purchase history. 

Test 7: Customer Menu: Customer Shopping Cart
1. Assume user has successfully login as a customer and can view the customer menu.
2. Select "View shopping cart".
3. Select one of the options: add, remove, purchase all or quit.
4. Select "View previous purchases" if a purchase was made to view it.

Expected Results: A customer will be able to go into the shopping cart and add or remove products. After they are 
satisfied, they can purchase all or quit from the shopping cart menu. 