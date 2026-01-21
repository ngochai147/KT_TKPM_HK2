package component;

public class DiscountDecorator extends PaymentDecorator {
    public DiscountDecorator(Payment payment) {
        super(payment);
    }

    public void pay(double amount) {
        super.pay(amount - 20);
        System.out.println("Áp dụng mã giảm giá: 20");
    }
}
