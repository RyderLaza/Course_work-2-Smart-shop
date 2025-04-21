import java.util.*;

//Product
class Product {
    public String name;
    public double price;
    public int stock;

    public Product(String name, double price, int stock) { //product constructor
        this.name = name; //sets the atributes of the class with the given parameters
        this.price = price;
        this.stock = stock;
    }

    public String getName() {
        return this.name;
    }
    public double getPrice() {
        return this.price;
    }
    public int getStock() {
        return this.stock;
    }
}
//sales manager
class SalesManager {
    public SalesRecord[] salesRecords;
    public int index;

    public SalesManager() {
        salesRecords = new SalesRecord[100]; //assumes only 100 sales records can be made for now will fix later
        this.index = 0;//sets the initial index for writing to the list
    }

    public void recordSale(SalesRecord record){
        salesRecords[this.index] = record; //sets the index of the list to the record given by param
        this.index++;//increments the index by 1
    }

    public void generateSalesReport(){
        //I have no clue what yous want doing with this
    }

}

//sales record
class SalesRecord {
    public Product[] products;
    public String date;
    public int[] quantitySold;
    public double totalPrice;

    public SalesRecord(Product[] products, String date, int[] quantity) {
        this.products = products;//sets up the atributes with parameters
        this.date = date;
        this.quantitySold = quantity;
        this.totalPrice = 0;
        for (int i = 0; i < products.length; i++) {//works out the total price upon initialisation
            this.totalPrice += this.products[i].getPrice()*this.quantitySold[i];
        }

    }

    public Product[] getProducts() {
        return this.products;
    }
    public String getDate() {
        return this.date;
    }
    public double getTotalPrice() {
       return this.totalPrice;
    }
}

//inventory manager
class InventoryManager {
    Product[] products = new Product[1];

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

    public void addProduct(Product product) {
        Product[] tempList = this.products;
        this.products = new Product[tempList.length + 1];
        for (int i = 0; i < tempList.length; i++) {
            this.products[i] = tempList[i];
        }
        this.products[this.products.length-1] = product;
    }

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
}





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
}
