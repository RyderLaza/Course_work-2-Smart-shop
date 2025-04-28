import java.util.ArrayList;
import javax.swing.*;
 
 public class ShopManagementGUI {
     static InventoryManager inventory = new InventoryManager(new Product[]{});
     static SalesManager salesManager = new SalesManager();
 
     public static void main(String[] args) {
         JFrame frame = new JFrame("Shop Management System");
         frame.setSize(400, 400);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setLayout(null);
 
         JButton manageProductsButton = new JButton("Manage Products");
         manageProductsButton.setBounds(100, 50, 200, 40);
         frame.add(manageProductsButton);
 
         JButton recordSaleButton = new JButton("Record Sale");
         recordSaleButton.setBounds(100, 120, 200, 40);
         frame.add(recordSaleButton);
 
         JButton generateReportButton = new JButton("Generate Report");
         generateReportButton.setBounds(100, 190, 200, 40);
         frame.add(generateReportButton);
 
         JButton exitButton = new JButton("Exit");
         exitButton.setBounds(100, 260, 200, 40);
         frame.add(exitButton);
 
         frame.setVisible(true);
 
         // Button actions
         manageProductsButton.addActionListener(e -> manageProducts());
         recordSaleButton.addActionListener(e -> recordSale());
         generateReportButton.addActionListener(e -> generateReport());
         exitButton.addActionListener(e -> frame.dispose());
     }
 
     static void manageProducts() {
    String[] options = {"Add Product", "View Inventory", "Remove Product", "Back"};
    int choice = JOptionPane.showOptionDialog(null, "Manage Products", "Manage",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

    switch (choice) {
        case 0: // Add Product
            String name = JOptionPane.showInputDialog("Enter product name:");
            if (name == null) return;
            String priceInput = JOptionPane.showInputDialog("Enter product price:");
            if (priceInput == null) return;
            String stockInput = JOptionPane.showInputDialog("Enter initial stock:");
            if (stockInput == null) return;
            try {
                double price = Double.parseDouble(priceInput);
                if (price < 0) {
                    JOptionPane.showMessageDialog(null, "Price cannot be negative.");
                    manageProducts();
                    return;
                }
                int stock = Integer.parseInt(stockInput);
                if (stock < 0) {
                    JOptionPane.showMessageDialog(null, "Stock cannot be negative.");
                    manageProducts();
                    return;
                }
                Product newProduct = new Product(name, price, stock);
                inventory.addProduct(newProduct);
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
             }
         } catch (NumberFormatException ex) {
             JOptionPane.showMessageDialog(null, "Invalid quantity input.");
         }
     }
 
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
