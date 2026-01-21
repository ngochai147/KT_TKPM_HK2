import component.CreditCardPayment;
import component.DiscountDecorator;
import component.FeeDecorator;
import component.Payment;

public class TestPayment {
    public static void main(String[] args) {
        Payment payment = new CreditCardPayment();
        payment = new FeeDecorator(payment);
        payment = new DiscountDecorator(payment);

        payment.pay(100);
    }
}
