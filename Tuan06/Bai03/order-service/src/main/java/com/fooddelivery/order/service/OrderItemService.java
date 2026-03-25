package com.fooddelivery.order.service;

import com.fooddelivery.order.dto.OrderItemDTO;
import com.fooddelivery.order.entity.OrderItem;
import com.fooddelivery.order.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public OrderItemDTO getOrderItemById(Long id) {
        Optional<OrderItem> item = orderItemRepository.findById(id);
        return item.map(this::convertToDTO).orElse(null);
    }

    public List<OrderItemDTO> getOrderItemsByOrder(Long orderId) {
        return orderItemRepository.findByOrderId(orderId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderItemDTO createOrderItem(OrderItemDTO item) {
        OrderItem orderItem = convertToEntity(item);
        OrderItem savedItem = orderItemRepository.save(orderItem);
        return convertToDTO(savedItem);
    }

    public OrderItemDTO updateOrderItem(Long id, OrderItemDTO itemDTO) {
        Optional<OrderItem> existingItem = orderItemRepository.findById(id);
        if (existingItem.isPresent()) {
            OrderItem item = existingItem.get();
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            OrderItem updatedItem = orderItemRepository.save(item);
            return convertToDTO(updatedItem);
        }
        return null;
    }

    public void deleteOrderItem(Long id) {
        orderItemRepository.deleteById(id);
    }

    public OrderItemDTO convertToDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setDishId(item.getDishId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setOrderId(item.getOrder().getId());
        return dto;
    }

    private OrderItem convertToEntity(OrderItemDTO dto) {
        OrderItem item = new OrderItem();
        item.setDishId(dto.getDishId());
        item.setQuantity(dto.getQuantity());
        item.setPrice(dto.getPrice());
        return item;
    }
}
