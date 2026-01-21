package service;

import context.OrderContext;

public class DeliveredOrderState implements OrderState {
    @Override
    public void handle(OrderContext order) {
        System.out.println("Đã giao: Cập nhật trạng thái đơn hàng");
    }
}
