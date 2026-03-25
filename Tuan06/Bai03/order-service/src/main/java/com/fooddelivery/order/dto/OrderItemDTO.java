package com.fooddelivery.order.dto;

public class OrderItemDTO {
    private Long id;
    private Long dishId;
    private Integer quantity;
    private Double price;
    private Long orderId;

    // Constructors and Getters/Setters
    public OrderItemDTO() {}

    public OrderItemDTO(Long dishId, Integer quantity, Double price) {
        this.dishId = dishId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
}
