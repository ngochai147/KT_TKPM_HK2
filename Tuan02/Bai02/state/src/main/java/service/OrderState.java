package service;

import context.OrderContext;

public interface OrderState {
    void handle(OrderContext order);
}
