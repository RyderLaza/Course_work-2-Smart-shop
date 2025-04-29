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
This file contains Java Swing components and it is the GUI of the
Smart Shop system
------------------------------------------------------------
*/
package ya;

import javax.swing.*; // this is imported for GUI components
import javax.swing.table.DefaultTableModel; // this is imported for table models
import java.awt.*; // this is imported for layout managers
import java.awt.event.*; // this is imported to handle events
import java.util.ArrayList; // this is imported to use the array list class

public class ShopManagementGUI {
    // These are the static variables that are utilized throughout the GUI code
    static InventoryManager inventory = new InventoryManager(new Product[]{});
    static SalesManager salesManager = new SalesManager();
    static InventoryWindow inventoryWindow = new InventoryWindow(); // This creates a separate window to view inventory

    public static void main(String[] args) {
        // This is to set up the main GUI frame
        JFrame frame = new JFrame("Shop Management System");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // This is to create the manage products button within the application
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

        // This is to create an exit button within the application
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

    // This creates a method to manage the products within the application (add, view, and remove products)
    static void manageProducts() {
        boolean stayInManageProducts = true;

        while (stayInManageProducts) {
            String[] options = {"Add Product", "View Inventory", "Remove Product", "Back"};
            int choice = JOptionPane.showOptionDialog(null, "Manage Products", "Manage",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0: // Add Product
                    boolean validInput = false;
                    while (!validInput) {
                        String name = JOptionPane.showInputDialog("Enter product name:");
                        if (name == null) break;

                        String priceInput = JOptionPane.showInputDialog("Enter product price:");
                        if (priceInput == null) break;

                        String stockInput = JOptionPane.showInputDialog("Enter initial stock:");
                        if (stockInput == null) break;

                        try {
                            double price = Double.parseDouble(priceInput);
                            int stock = Integer.parseInt(stockInput);

                            if (price < 0 || stock < 0) {
                                JOptionPane.showMessageDialog(null, "Price and stock must be non-negative.");
                            } else {
                                Product newProduct = new Product(name, price, stock);
                                inventory.addProduct(newProduct);
                                JOptionPane.showMessageDialog(null, "Product added successfully!");
                                validInput = true; // input is valid, exit the inner loop
                                inventoryWindow.refreshTable(); // This refreshes the inventory window after adding a product
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid price or stock input.");
                        }
                    }
                    break;

                case 1: // View Inventory
                    inventoryWindow.setVisible(true); // This displays the inventory window
                    inventoryWindow.refreshTable(); // This refreshes the inventory window when viewed
                    break;

                case 2: // Remove Product
                    if (inventory.products.length == 0) {
                        JOptionPane.showMessageDialog(null, "No products to remove.");
                        break;
                    }

                    String[] productNames = new String[inventory.products.length];
                    for (int i = 0; i < inventory.products.length; i++) {
                        productNames[i] = inventory.products[i].getName();
                    }

                    String selectedProduct = (String) JOptionPane.showInputDialog(null, "Select a product to remove:",
                            "Remove Product", JOptionPane.QUESTION_MESSAGE, null, productNames, productNames[0]);

                    if (selectedProduct == null) break;

                    ArrayList<Product> updatedList = new ArrayList<>();
                    for (Product p : inventory.products) {
                        if (!p.getName().equals(selectedProduct)) {
                            updatedList.add(p);
                        }
                    }

                    inventory.products = updatedList.toArray(new Product[0]);
                    JOptionPane.showMessageDialog(null, "Product removed successfully!");
                    inventoryWindow.refreshTable(); // This refreshes the inventory window after removing a product
                    break;

                case 3: // Back
                default:
                    stayInManageProducts = false; // Exit the while loop
                    break;
            }
        }
    }

    // This allows for a method to be created to record a sale within the application
    static void recordSale() {
        if (inventory.products.length == 0) {
            JOptionPane.showMessageDialog(null, "No products available to sell.");
            return;
        }

        String[] productNames = new String[inventory.products.length];
        for (int i = 0; i < inventory.products.length; i++) {
            productNames[i] = inventory.products[i].getName();
        }

        String selectedProduct = (String) JOptionPane.showInputDialog(null, "Select a product:",
                "Record Sale", JOptionPane.QUESTION_MESSAGE, null, productNames, productNames[0]);

        if (selectedProduct == null) return;

        Product productToSell = null;
        for (Product p : inventory.products) {
            if (p.getName().equals(selectedProduct)) {
                productToSell = p;
                break;
            }
        }

        if (productToSell == null) return;

        String quantityInput = JOptionPane.showInputDialog("Enter quantity to sell:");
        if (quantityInput == null) return;

        try {
            int quantity = Integer.parseInt(quantityInput);
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
                inventory.updateStock(productToSell, productToSell.getStock() - quantity);
                JOptionPane.showMessageDialog(null, "Sale recorded! Total: $" + sale.getTotalPrice());
                inventoryWindow.refreshTable(); // This refreshes the inventory window after recording a sale
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid quantity input.");
        }
    }

    // This is the method to generate a report of sales and inventory
    static void generateReport() {
        StringBuilder sb = new StringBuilder();

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

// This creates a new class to handle the inventory window separately
class InventoryWindow extends JFrame {
    JTable table; // This declares a JTable to display the inventory
    DefaultTableModel model; // This declares a DefaultTableModel to manage the table data

    public InventoryWindow() {
        setTitle("Inventory"); // This sets the title of the inventory window
        setSize(400, 300); // This sets the size of the inventory window
        setLayout(new BorderLayout()); // This sets the layout of the inventory window

        model = new DefaultTableModel(new Object[]{"Name", "Price", "Stock"}, 0); // This creates the table columns
        table = new JTable(model); // This initializes the table with the model
        add(new JScrollPane(table), BorderLayout.CENTER); // This adds the table to a scroll pane and places it in the center
    }

    public void refreshTable() {
        model.setRowCount(0); // This clears the current table data
        for (Product p : ShopManagementGUI.inventory.products) {
            model.addRow(new Object[]{p.getName(), "$" + p.getPrice(), p.getStock()}); // This adds each product to the table
        }
    }
}
