package com.fooddelivery.order.service;

import com.fooddelivery.order.dto.OrderDTO;
import com.fooddelivery.order.dto.OrderItemDTO;
import com.fooddelivery.order.entity.Order;
import com.fooddelivery.order.entity.OrderItem;
import com.fooddelivery.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemService orderItemService;

    public OrderDTO getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(this::convertToDTO).orElse(null);
    }

    public List<OrderDTO> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByRestaurant(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByStatus(String status) {
        return orderRepository.findByOrderStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);

        if (orderDTO.getItems() != null && !orderDTO.getItems().isEmpty()) {
            for (OrderItemDTO itemDTO : orderDTO.getItems()) {
                itemDTO.setOrderId(savedOrder.getId());
                orderItemService.createOrderItem(itemDTO);
            }
        }

        return convertToDTO(savedOrder);
    }

    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Optional<Order> existingOrder = orderRepository.findById(id);
        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();
            order.setOrderStatus(orderDTO.getOrderStatus());
            order.setDeliveryAddress(orderDTO.getDeliveryAddress());
            order.setNotes(orderDTO.getNotes());
            Order updatedOrder = orderRepository.save(order);
            return convertToDTO(updatedOrder);
        }
        return null;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId());
        dto.setRestaurantId(order.getRestaurantId());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setNotes(order.getNotes());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                    .map(orderItemService::convertToDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private Order convertToEntity(OrderDTO dto) {
        Order order = new Order();
        order.setCustomerId(dto.getCustomerId());
        order.setRestaurantId(dto.getRestaurantId());
        order.setTotalPrice(dto.getTotalPrice());
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setNotes(dto.getNotes());
        return order;
    }
}
