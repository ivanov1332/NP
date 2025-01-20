package K1_exercises.ex4;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(int total_amount) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", total_amount));
    }
}

enum TaxType {
    A, B, V
}

class Item {
    int price;
    TaxType type;

    Item(int price) {
        this.price = price;
    }

    public Item(int price, TaxType type) {
        this.price = price;
        this.type = type;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setType(TaxType type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public TaxType getType() {
        return type;
    }

    public double calculateTax() {
        if (type.equals(TaxType.A)) {
            return 0.18 * price;
        } else if (type.equals(TaxType.B)) {
            return 0.05 * price;
        } else return 0;
    }
}

class Receipt {
    long id;
    List<Item> items;

    public Receipt(long id, List<Item> items) {
        this.id = id;
        this.items = items;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        // “ID SUM_OF_AMOUNTS TAX_RETURN”
        return String.format("%d %d %.2f",id,totalAmount(),taxReturn());
    }

    public static Receipt createReceipt(String line) throws AmountNotAllowedException {
        //12334 1789 А 1238 B 1222 V 111 V
        String[] parts = line.split("\\s+");
        long id = Long.parseLong(parts[0]);
        List<Item> itemList = new ArrayList<>();
        Arrays.stream(parts)
                .skip(1)
                .forEach(i -> {
                    if (Character.isDigit(i.charAt(0))) {
                        itemList.add(new Item(Integer.parseInt(i)));
                    } else {
                        itemList.get(itemList.size() - 1).setType(TaxType.valueOf(i));
                    }
                });

        int totalAmount = totalAmount2(itemList);

        if (totalAmount > 30000) {
            throw new AmountNotAllowedException(totalAmount);
        }
        return new Receipt(id, itemList);
    }

    public int totalAmount(){
        return items.stream().mapToInt(Item::getPrice).sum();
    }
    public double taxReturn(){
        return items.stream().mapToDouble(Item::calculateTax).sum();
    }

    public static int totalAmount2(List<Item> itemList) {
        return itemList.stream().mapToInt(Item::getPrice).sum();
    }

}

class MojDDV {

    List<Receipt> receipts;

    MojDDV() {
        this.receipts = new ArrayList<>();
    }

    public MojDDV(List<Receipt> receipts) {
        this.receipts = receipts;
    }


    public void readRecords(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        receipts = br.lines()
                .map(receipt -> {
                    try {
                        return Receipt.createReceipt(receipt);
                    } catch (AmountNotAllowedException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    public void printTaxReturns(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);

        receipts.stream().forEach(pw::println);

        pw.flush();
    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}