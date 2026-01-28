// Demo
public class Main {
    public static void main(String[] args) {
        AppleStock stock = new AppleStock();

        stock.attach(new Investor("Hải"));
        stock.attach(new Investor("Minh"));

        stock.setPrice(150.5);
        stock.setPrice(200);

    }
}
