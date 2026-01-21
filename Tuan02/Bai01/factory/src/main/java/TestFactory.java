import service.Payment;

public class TestFactory {
    public static void main(String[] args) {
        Payment payment1 = PaymentFactory.createPayment("CASH");
        Payment payment2 = PaymentFactory.createPayment("CARD");

        payment1.pay();
        payment2.pay();
    }
}