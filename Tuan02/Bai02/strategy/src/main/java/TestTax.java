import context.Product;
import service.LuxuryTaxStrategy;
import service.VATTaxStrategy;

public class TestTax {
    public static void main(String[] args) {
        Product product = new Product(1000, new VATTaxStrategy());
        System.out.println("Giá sau thuế: " + product.getFinalPrice());

        product.setTaxStrategy(new LuxuryTaxStrategy());
        System.out.println("Giá sau thuế xa xỉ: " + product.getFinalPrice());
    }
}
