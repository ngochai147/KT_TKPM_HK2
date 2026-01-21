import context.OrderContext;

public class Test {
    public static void main(String[] args) {
        OrderContext order = new OrderContext();

        order.process();
        order.process();
        order.process();
    }
}
