package component;

public class FeeDecorator extends PaymentDecorator {
    public FeeDecorator(Payment payment) {
        super(payment);
    }

    public void pay(double amount) {
        super.pay(amount + 10);
        System.out.println("Phí xử lý: 10");
    }
}

