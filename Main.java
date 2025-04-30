/*
This is the main class for the Smart Shop Management System.
It contains the main classes and methods for managing products, sales, and inventory.

Authors:
Di’light Sarah Olisah – 26117363, Charlie Ryder - 26186781, Harley Webster – 26084678
Josh Bell - 26174804, Paul Nyamwela - 26126796, Dylan Clarke - 26115999

Date: 25/04/2025
Programming 2 - Coursework 2 - Group Project - Smart Shop Management System
*/

import java.util.*; // importing the java util package for using arrays and other data structures

//Product class
class Product {
    //Atributes for the product class
    public String name; //product name
    public double price; // product price
    public int stock; // product stock

    //product constructor
    public Product(String name, double price, int stock) {
        // sets the attributes of the class with the given parameters
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    //getters for the attributes
    public String getName() {
        return this.name;
    }
    public double getPrice() {
        return this.price;
    }
    public int getStock() {
        return this.stock;
    }
} // end of product class

// sales manager class
class SalesManager {
    //Atributes for Sales manager class
    public SalesRecord[] salesRecords; // array of sales records
    public int index; // index for the sales records

    public SalesManager() {
        salesRecords = new SalesRecord[100]; //assumes only 100 sales records can be made for now will fix later
        this.index = 0;//sets the initial index for writing to the list
    }

    public void recordSale(SalesRecord record){
        salesRecords[this.index] = record; //sets the index of the list to the record given by param
        this.index++;//increments the index by 1
    }

    public void generateSalesReport(){
        //Method not utilised by the proggram
    }

}

//sales record class
class SalesRecord {
    //Atributes for SalesRecord object
    public Product[] products;
    public String date;
    public int[] quantitySold;
    public double totalPrice;

    // constructor for sales record
    public SalesRecord(Product[] products, String date, int[] quantity) {
        this.products = products;//sets up the atributes with parameters
        this.date = date;
        this.quantitySold = quantity;
        this.totalPrice = 0;

        for (int i = 0; i < products.length; i++) {
            //works out the total price upon initialisation
            this.totalPrice += this.products[i].getPrice()*this.quantitySold[i];
        }

    }
    //getters for the attributes
    public Product[] getProducts() {
        return this.products;
    }
    public String getDate() {
        return this.date;
    }
    public double getTotalPrice() {
        return this.totalPrice;
    }
} // end of sales record class

//inventory manager class
class InventoryManager {
    //Atributes for the InventoryManager class
    Product[] products = new Product[1]; //initialises the product list to a size of 1

    // constructor for inventory manager
    public InventoryManager(Product[] products) {
        this.products = products;
    }

    public void updateStock(Product product, int newStock) {
        product.stock = newStock; //sets the products stock to the given number
    }

    public Boolean checkLowStock(Product product, int lowAmount) {
        if (product.getStock() < lowAmount) {//checks the product stock to see if the amount is low
            return true;                       //then returns true or false based on the outcome
        }
        return false;
    }

    // Method to add a product to the inventory
    public void addProduct(Product product) {
        /*Description
        Creates a temporary list that stores the current products 
        Re-defines the product list as an empty list with a greater size
        All items are then added into the new product list 
        */
        Product[] tempList = this.products;
        this.products = new Product[tempList.length + 1];
        for (int i = 0; i < tempList.length; i++) {
            this.products[i] = tempList[i];
        }
        this.products[this.products.length-1] = product;
    }

    // Method to remove a product from the inventory
    public void removeProduct(Product product) {
        Product[] tempList = this.products;
        this.products = new Product[tempList.length - 1];
        int currentIndex = 0;
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i] != product) {
                this.products[currentIndex] = tempList[i];
                currentIndex++;
            }
        }
    }
} // end of inventory manager class


// Main class to run the program
public class Main {
    public static void main(String[] args) {

        //code for testing
        Product banana =  new Product("Banana", 100.0, 0);//the price was auto-fill but goddamn that's expensive
        Product apple =  new Product("Apple", 100.0, 1); // same again wth
        Product[] ps = {banana};
        InventoryManager inventory = new InventoryManager(ps);
        inventory.addProduct(apple);
        System.out.println(Arrays.toString(inventory.products));
        inventory.removeProduct(banana);
        System.out.println(Arrays.toString(inventory.products));
    }
} // end main class
