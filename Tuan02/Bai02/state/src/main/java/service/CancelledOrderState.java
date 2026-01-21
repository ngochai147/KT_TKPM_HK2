package service;

import context.OrderContext;

public class CancelledOrderState implements OrderState {
    @Override
    public void handle(OrderContext order) {
        System.out.println("Hủy: Hủy đơn hàng và hoàn tiền");
    }
}
