package service;

import context.OrderContext;

public class ProcessingOrderState implements OrderState {
    @Override
    public void handle(OrderContext order) {
        System.out.println("Đang xử lý: Đóng gói và vận chuyển");
        order.setState(new DeliveredOrderState());
    }
}
