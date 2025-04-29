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

import javax.swing.*; // GUI components
import javax.swing.table.DefaultTableModel; // Table models
import java.awt.*; // Layout and color handling
import java.awt.event.*; // Event handling
import java.util.ArrayList; // ArrayList for dynamic lists

public class ShopManagementGUI {
    // Shared static instances of management components
    static InventoryManager inventory = new InventoryManager(new Product[]{});
    static SalesManager salesManager = new SalesManager();
    static InventoryWindow inventoryWindow = new InventoryWindow(); // Inventory view window

    public static void main(String[] args) {
        // Set up the main frame
        JFrame frame = new JFrame("Shop Management System");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null); // Center the window

        // GridBagLayout constraints for layout management
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15, 40, 15, 40);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Fonts
        Font titleFont = new Font("Roboto", Font.BOLD, 24);
        Font instructionFont = new Font("Roboto", Font.PLAIN, 14);
        Font buttonFont = new Font("Roboto", Font.BOLD, 18);

        // Heading label
        JLabel headingLabel = new JLabel("Smart Shop Management System", SwingConstants.CENTER);
        headingLabel.setFont(titleFont);
        gbc.gridy = 0;
        gbc.weighty = 0.05;
        frame.add(headingLabel, gbc);

        // Instruction label
        JLabel instructionLabel = new JLabel("Hover over each button for more details", SwingConstants.CENTER);
        instructionLabel.setFont(instructionFont);
        gbc.gridy = 1;
        frame.add(instructionLabel, gbc);

        // Manage Products button
        JButton manageProductsButton = new JButton("Manage Products");
        manageProductsButton.setToolTipText("Add, view, or remove products in your shop");
        manageProductsButton.setFont(buttonFont);
        gbc.gridy = 2;
        gbc.weighty = 0.1;
        frame.add(manageProductsButton, gbc);

        // Record Sale button
        JButton recordSaleButton = new JButton("Record Sale");
        recordSaleButton.setToolTipText("Used to record a sale in the shop");
        recordSaleButton.setFont(buttonFont);
        gbc.gridy = 3;
        frame.add(recordSaleButton, gbc);

        // Generate Report button
        JButton generateReportButton = new JButton("Generate Report");
        generateReportButton.setToolTipText("Used to generate a report of sales and inventory");
        generateReportButton.setFont(buttonFont);
        gbc.gridy = 4;
        frame.add(generateReportButton, gbc);

        // Exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setToolTipText("Used to exit the application");
        exitButton.setFont(buttonFont);
        gbc.gridy = 5;
        frame.add(exitButton, gbc);

        frame.setVisible(true); // Show the window

        // Button actions
        manageProductsButton.addActionListener(e -> manageProducts());
        recordSaleButton.addActionListener(e -> recordSale());
        generateReportButton.addActionListener(e -> generateReport());
        exitButton.addActionListener(e -> frame.dispose());
    }

    // Handles the Manage Products workflow
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
                                validInput = true;
                                inventoryWindow.refreshTable();
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid price or stock input.");
                        }
                    }
                    break;

                case 1: // View Inventory
                    inventoryWindow.setVisible(true);
                    inventoryWindow.refreshTable();
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
                    inventoryWindow.refreshTable();
                    break;

                case 3: // Back
                default:
                    stayInManageProducts = false;
                    break;
            }
        }
    }

    // Handles sale recording
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
                inventoryWindow.refreshTable();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid quantity input.");
        }
    }

    // Generates a sales and inventory report
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

// InventoryWindow class with no background color set
class InventoryWindow extends JFrame {
    JTable table; // Table to show inventory
    DefaultTableModel model; // Table model

    public InventoryWindow() {
        setTitle("Inventory"); // Title of the window
        setSize(400, 300); // Dimensions
        setLayout(new BorderLayout()); // Layout manager

        // Initialize table and model
        model = new DefaultTableModel(new Object[]{"Name", "Price", "Stock"}, 0);
        table = new JTable(model);
        table.setBackground(Color.WHITE); // Table background for readability
        table.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Font style
        table.setRowHeight(22); // Row height

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE); // Scroll pane background

        add(scrollPane, BorderLayout.CENTER); // Add scrollable table to center
    }

    // Updates table with current inventory
    public void refreshTable() {
        model.setRowCount(0); // Clear existing data
        for (Product p : ShopManagementGUI.inventory.products) {
            model.addRow(new Object[]{p.getName(), "$" + p.getPrice(), p.getStock()});
        }
    }
}

