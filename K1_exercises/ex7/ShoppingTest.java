package K1_exercises.ex7;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class InvalidOperationException extends Exception{
    public InvalidOperationException() {
    }

    //    public InvalidOperationException(String message) {
//        super(message);
//    }

    public InvalidOperationException(long productID) {
        super(String.format("The quantity of the product with id %d can not be 0.",productID));
    }

    public InvalidOperationException(String message) {
        super(message);
    }
}

enum typeProduct{
    PS,WS
}

class Product implements Comparable<Product>{
    long productId;
    String productName;
    double productPrice;
    double quantity;
    String typeOfProduct;

    public Product(long productId, String productName, double productPrice, double quantity,String typeOfProduct) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.typeOfProduct = typeOfProduct;
    }

    public double getTotalPrice(){
        if (typeOfProduct.equals("WS")){
            return quantity * productPrice;
        }
        else {
            return (quantity / 1000) * productPrice;
        }
    }

    @Override
    public String toString() {
        return String.format("%d - %.2f",productId,getTotalPrice());
    }

    public long getProductId() {
        return productId;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getTypeProduct() {
        return typeOfProduct;
    }

    public void discount(){
        this.productPrice = productPrice * 0.9;
    }


    @Override
    public int compareTo(Product o) {
        return Double.compare(this.getTotalPrice(),o.getTotalPrice());
    }
}

class ShoppingCart {

    List<Product> products;

    public ShoppingCart(List<Product> products) {
        this.products = products;
    }

    ShoppingCart(){
        this.products = new ArrayList<>();
    }

    public void addItem(String s) throws InvalidOperationException {

        //PS;107965;Flour;409;800.78
        //WS;101569;Coca Cola;970;64

        String [] parts = s.split(";");

        String type = parts[0];
        long productID = Long.parseLong(parts[1]);
        String productName = parts[2];
        double productPrice = Double.parseDouble(parts[3]);
        double quantity = Double.parseDouble(parts[4]);

        if (quantity == 0){
            throw new InvalidOperationException(productID);
        }
        products.add(new Product(productID,productName,productPrice,quantity,type));
    }

    public void printShoppingCart(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        products.stream().sorted(Comparator.reverseOrder()).forEach(pw::println);
        pw.flush();
    }

    public void blackFridayOffer(List<Integer> discountItems, PrintStream out) throws InvalidOperationException {
        PrintWriter pw = new PrintWriter(out);

        if (discountItems.isEmpty()){
            throw new InvalidOperationException("There are no products with discount.\n");
        }

        for (Product product : products){
            if (discountItems.contains((int) product.getProductId())){
                double originalPrice = product.getTotalPrice();
                product.discount();
                double newTotalPriceWithDiscount = product.getTotalPrice();
                double difference = originalPrice - newTotalPriceWithDiscount;
                pw.println(String.format("%d - %.2f",product.getProductId(),difference));
            }
        }
        pw.flush();
    }
}

public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}