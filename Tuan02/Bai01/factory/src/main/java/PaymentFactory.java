import service.CashPayment;
import service.CreditCardPayment;
import service.Payment;
import service.PaypalPayment;

public class PaymentFactory {

    public static Payment createPayment(String type) {
        switch (type) {
            case "CASH":
                return new CashPayment();
            case "CARD":
                return new CreditCardPayment();
            case "PAYPAL":
                return new PaypalPayment();
            default:
                throw new IllegalArgumentException("Invalid payment type");
        }
    }
}
