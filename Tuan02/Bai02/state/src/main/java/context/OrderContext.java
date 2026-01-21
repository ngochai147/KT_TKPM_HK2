package context;

import service.NewOrderState;
import service.OrderState;

public class OrderContext {
    private OrderState state;

    public OrderContext() {
        state = new NewOrderState();
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public void process() {
        state.handle(this);
    }
}
