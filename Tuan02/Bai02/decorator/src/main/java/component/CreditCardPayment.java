package component;

public class CreditCardPayment implements Payment {
    public void pay(double amount) {
        System.out.println("Thanh toán bằng thẻ tín dụng: " + amount);
    }
}
