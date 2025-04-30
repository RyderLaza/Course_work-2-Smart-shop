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
        All items are then added into the new product list with a for loop to add the original products back 
        and finnaly the new item is added
        */
        Product[] tempList = this.products;// temp list to store all previous producs while they are added back to the new list
        this.products = new Product[tempList.length + 1];//adds 1 to the size of the list when the new one is created
        for (int i = 0; i < tempList.length; i++) {
            this.products[i] = tempList[i];//adds the current item back to the list 
        }
        this.products[this.products.length-1] = product;//adds the final/new product into the new list 
    }

    // Method to remove a product from the inventory
    public void removeProduct(Product product) {
        /*
        Creates a temporary list that stores the current products 
        then the products atribute is assigned as an empty array of products that is shorter than the original 
        then a for loop adds the item back into the new product list if the item isn't equal to the product being removed
        */
        Product[] tempList = this.products;//temp list to keep all products in wile they are being added back or removed
        this.products = new Product[tempList.length - 1];//takes away the size by one of the new list
        int currentIndex = 0;//current index variable to be used for the new products list so that no array index errors occur
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i] != product) {//checks that the current item isn't equal to the one being removed
                this.products[currentIndex] = tempList[i];//adds item back to products list
                currentIndex++;//increments the index by 1
            }
        }
    }
} // end of inventory manager class


// Main class to run the program
public class Main {
    public static void main(String[] args) {

        //code for testing
        Product banana =  new Product("Banana", 100.0, 0);
        Product apple =  new Product("Apple", 100.0, 1); //creates 2 different products for testing purposes
        Product[] ps = {banana};// creating a list of products
        InventoryManager inventory = new InventoryManager(ps);//initialising the inventory manager object with a product list containing bananas
        inventory.addProduct(apple);//adding apples to the list to test if it works
        System.out.println(Arrays.toString(inventory.products));//outputs the current list so i can see the changes
        inventory.removeProduct(banana);//removes the banana product object 
        System.out.println(Arrays.toString(inventory.products));//outputs the changes 
    }
} // end main class
