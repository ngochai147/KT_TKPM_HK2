package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service cho Order (Horizontal & Function Partitioning)
 * - Horizontal: Chia theo user_id (range partition)
 * - Function: Chia theo YEAR(order_date) để quản lý dữ liệu lịch sử
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    /**
     * Tạo đơn hàng mới
     * Partition sẽ được chọn dựa trên:
     * - Horizontal: range của user_id
     * - Time-based: năm hiện tại
     */
    public Order createOrder(Long userId, BigDecimal totalAmount, String shippingAddress) {
        String orderCode = generateOrderCode(userId);

        Order order = Order.builder()
                .userId(userId)
                .orderCode(orderCode)
                .totalAmount(totalAmount)
                .status("PENDING")
                .shippingAddress(shippingAddress)
                .build();

        Order saved = orderRepository.save(order);
        log.info("Order created: orderId={}, userId={}, orderCode={}", saved.getOrderId(), userId, orderCode);
        return saved;
    }

    /**
     * Tạo mã đơn hàng duy nhất
     */
    private String generateOrderCode(Long userId) {
        return String.format("ORD-%d-%s", userId, UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    }

    /**
     * Lấy đơn hàng theo ID
     * Index: primary key -> O(1)
     */
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    /**
     * Lấy danh sách đơn hàng của user
     * Index composite: (user_id) + partition -> O(log n) + partition scan
     */
    @Transactional(readOnly = true)
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    /**
     * Lấy đơn hàng theo trạng thái
     * Index composite: (user_id, status)
     */
    @Transactional(readOnly = true)
    public List<Order> getUserOrdersByStatus(Long userId, String status) {
        return orderRepository.findByUserIdAndStatus(userId, status);
    }

    /**
     * Lấy đơn hàng trong khoảng thời gian (tận dụng time-based partition)
     * Partition chọn theo: YEAR(order_date)
     */
    @Transactional(readOnly = true)
    public List<Order> getUserOrdersBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findOrdersBetween(userId, startDate, endDate);
    }

    /**
     * Lấy n đơn hàng gần nhất
     * Native query với LIMIT -> tối ưu performance
     */
    @Transactional(readOnly = true)
    public List<Order> getRecentOrders(Long userId, String status, int limit) {
        return orderRepository.findRecentOrdersByUserAndStatus(userId, status, limit);
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    public Order updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(newStatus);
        Order updated = orderRepository.save(order);
        log.info("Order status updated: orderId={}, status={}", orderId, newStatus);
        return updated;
    }

    /**
     * Tìm kiếm đơn hàng theo order code
     * Index: order_code (unique) -> O(log n)
     */
    @Transactional(readOnly = true)
    public Optional<Order> findByOrderCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }

    /**
     * Tính tổng chi tiêu của user
     * Aggregate query trên partition
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalSpent(Long userId) {
        BigDecimal total = orderRepository.getTotalSpentByUser(userId);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Đếm đơn hàng theo trạng thái
     */
    @Transactional(readOnly = true)
    public long countOrdersByStatus(Long userId, String status) {
        return orderRepository.countByUserIdAndStatus(userId, status);
    }

    /**
     * Lấy thống kê đơn hàng
     */
    @Transactional(readOnly = true)
    public OrderStatistics getOrderStatistics(Long userId) {
        return OrderStatistics.builder()
                .totalOrders(getUserOrders(userId).size())
                .pendingOrders(countOrdersByStatus(userId, "PENDING"))
                .processingOrders(countOrdersByStatus(userId, "PROCESSING"))
                .shippedOrders(countOrdersByStatus(userId, "SHIPPED"))
                .deliveredOrders(countOrdersByStatus(userId, "DELIVERED"))
                .cancelledOrders(countOrdersByStatus(userId, "CANCELLED"))
                .totalSpent(getTotalSpent(userId))
                .build();
    }

    /**
     * Hủy đơn hàng
     */
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!"PENDING".equals(order.getStatus()) && !"PROCESSING".equals(order.getStatus())) {
            throw new IllegalStateException("Order cannot be cancelled in current status: " + order.getStatus());
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);
        log.info("Order cancelled: orderId={}", orderId);
    }
}
