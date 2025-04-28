/* 
------------------------------------------------------------
Smart Shop Management System
Programming 2 - Coursework 2 - Group Project

Authors:
- Di’light Sarah Olisah – 26117363
- Charlie Ryder – 26186781
- Harley Webster – 26084678
- Josh Bell – 26174804
- Paul Nyamwela – 26126796
- Dylan Clarke – 26115999

Date: 25/04/2025

Description:
This file 
------------------------------------------------------------
*
import javax.swing.*; // this is imported for GUI components
import java.awt.event.*; // this is imported to handle events
import java.util.ArrayList; // this is imported to use the array list class

public class ShopManagementGUI {
    // These are the static variables that are utilized throughout the GUI code
    static InventoryManager inventory = new InventoryManager(new Product[]{});
    static SalesManager salesManager = new SalesManager();

    public static void main(String[] args) {
        // This is to set up the main GUI frame
        JFrame frame = new JFrame("Shop Management System");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // This is to create the manage products button to the application
        JButton manageProductsButton = new JButton("Manage Products");
        manageProductsButton.setBounds(100, 50, 200, 40);
        frame.add(manageProductsButton);

        // This is to create the record sale button within the application
        JButton recordSaleButton = new JButton("Record Sale");
        recordSaleButton.setBounds(100, 120, 200, 40);
        frame.add(recordSaleButton);

        // This is to create the generate report button within the application
        JButton generateReportButton = new JButton("Generate Report");
        generateReportButton.setBounds(100, 190, 200, 40);
        frame.add(generateReportButton);

        // this is to create an exit button within the application
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(100, 260, 200, 40);
        frame.add(exitButton);

        frame.setVisible(true); // This line is to make the frame visible

        // This adds functionality to the buttons allowing the user to interact and execute actions within the GUI
        manageProductsButton.addActionListener(e -> manageProducts());
        recordSaleButton.addActionListener(e -> recordSale());
        generateReportButton.addActionListener(e -> generateReport());
        exitButton.addActionListener(e -> frame.dispose());
    }

    // This creats a method to manage the products within the application (add, view, and remove products)
     static void manageProducts() {
    String[] options = {"Add Product", "View Inventory", "Remove Product", "Back"};
    // This creates a dialog box to allow the options to be displayed to the user
    int choice = JOptionPane.showOptionDialog(null, "Manage Products", "Manage",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

    switch (choice) {
        case 0: // Add Product
         // This prompts the user to enter the product name, price, and stock to add a new product to the 
            String name = JOptionPane.showInputDialog("Enter product name:");
            if (name == null) return;
            String priceInput = JOptionPane.showInputDialog("Enter product price:");
            if (priceInput == null) return;
            String stockInput = JOptionPane.showInputDialog("Enter initial stock:");
            if (stockInput == null) return;
            try {
                double price = Double.parseDouble(priceInput); // This is to validate the price input
                if (price < 0) { // Validation so that user can't input negative numbers.
                    JOptionPane.showMessageDialog(null, "Price cannot be negative."); 
                    manageProducts();
                    return;
                }
                int stock = Integer.parseInt(stockInput); // This is to validate the stock input
                if (stock < 0) {
                    JOptionPane.showMessageDialog(null, "Stock cannot be negative.");
                    manageProducts();
                    return;
                }
                Product newProduct = new Product(name, price, stock);
                inventory.addProduct(newProduct); // This adds the new product to the inventory
                JOptionPane.showMessageDialog(null, "Product added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid price or stock input.");
            }
            break;

        case 1: // View Inventory
            StringBuilder sb = new StringBuilder();
            for (Product p : inventory.products) {
                sb.append(p.getName()).append(" - $").append(p.getPrice()).append(" - Stock: ").append(p.getStock()).append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "No products in inventory.");
            break;

        case 2: // Remove Product
            if (inventory.products.length == 0) {
                JOptionPane.showMessageDialog(null, "No products to remove.");
                return;
            }

            String[] productNames = new String[inventory.products.length];
            for (int i = 0; i < inventory.products.length; i++) {
                productNames[i] = inventory.products[i].getName();
            }

            String selectedProduct = (String) JOptionPane.showInputDialog(null, "Select a product to remove:",
                    "Remove Product", JOptionPane.QUESTION_MESSAGE, null, productNames, productNames[0]);

            if (selectedProduct == null) return;

            ArrayList<Product> updatedList = new ArrayList<>();
            for (Product p : inventory.products) {
                if (!p.getName().equals(selectedProduct)) {
                    updatedList.add(p);
                }
            }

            inventory.products = updatedList.toArray(new Product[0]);
            JOptionPane.showMessageDialog(null, "Product removed successfully!");
            break;

        case 3:
            // Back
            break;
    }
}

     // This allows for a method to be created to record a sale within the application
     static void recordSale() {
         if (inventory.products.length == 0) {
             JOptionPane.showMessageDialog(null, "No products available to sell.");
             return;
         }

         // This builds a list of product names to be displayed in the dialog box for the user to select from
         String[] productNames = new String[inventory.products.length];
         for (int i = 0; i < inventory.products.length; i++) {
             productNames[i] = inventory.products[i].getName();
         }

         // This allows the user to select a product from the list of products in the inventory to sell
         String selectedProduct = (String) JOptionPane.showInputDialog(null, "Select a product:",
                 "Record Sale", JOptionPane.QUESTION_MESSAGE, null, productNames, productNames[0]);

         if (selectedProduct == null) return;

         // This finds the selected product in the inventory
         Product productToSell = null;
         for (Product p : inventory.products) {
             if (p.getName().equals(selectedProduct)) {
                 productToSell = p;
                 break;
             }
         }

         if (productToSell == null) return;

         // This prompts the user to enter the quantity of the selected product to sell
         String quantityInput = JOptionPane.showInputDialog("Enter quantity to sell:");
         if (quantityInput == null) return;

         try {
             int quantity = Integer.parseInt(quantityInput); // This displays a message if there is not enough stock available for the selected product
             if (quantity <= 0) {
                JOptionPane.showMessageDialog(null, "Quantity must be greater than 0.");
                recordSale();
                return;
            }
             if (quantity > productToSell.getStock()) {
                 JOptionPane.showMessageDialog(null, "Not enough stock available.");
             } else {
                 Product[] soldProducts = {productToSell};
                 int[] quantities = {quantity};
                 SalesRecord sale = new SalesRecord(soldProducts, "Today", quantities);
                 salesManager.recordSale(sale);
                 // This updates the inventory stock after a sale is recorded
                 inventory.updateStock(productToSell, productToSell.getStock() - quantity);
                 JOptionPane.showMessageDialog(null, "Sale recorded! Total: $" + sale.getTotalPrice());
             }
         } catch (NumberFormatException ex) {
             JOptionPane.showMessageDialog(null, "Invalid quantity input.");
         }
     }

     // This is the method to generate a report of sales and inventory
     static void generateReport() {
         StringBuilder sb = new StringBuilder();
         // This loops through the sales records and appends the details to the report
         sb.append("Sales Report:\n");
         for (SalesRecord record : salesManager.salesRecords) {
             if (record != null) {
                 sb.append("Date: ").append(record.getDate()).append(", Total: $").append(record.getTotalPrice()).append("\n");
             }
         }

         sb.append("\nInventory Report:\n");
         for (Product p : inventory.products) {
             sb.append(p.getName()).append(" - Stock: ").append(p.getStock()).append("\n");
         }

         JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "No data to display.");
     }
 }
