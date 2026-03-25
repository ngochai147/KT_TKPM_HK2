package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrderStatistics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller cho Order (Horizontal & Function Partitioning)
 */
@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * POST /api/orders
     * Tạo đơn hàng mới (Partition: user_id range + YEAR(order_date))
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            Order order = orderService.createOrder(
                    request.getUserId(),
                    request.getTotalAmount(),
                    request.getShippingAddress()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order created successfully");
            response.put("orderId", order.getOrderId());
            response.put("orderCode", order.getOrderCode());
            response.put("status", order.getStatus());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * GET /api/orders/user/{userId}
     * Lấy danh sách đơn hàng của user (Horizontal Partition by user_id)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getUserOrders(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("userId", userId);
        response.put("count", orders.size());
        response.put("data", orders);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/orders/user/{userId}/status/{status}
     * Lấy đơn hàng theo trạng thái (Index composite: user_id + status)
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<?> getUserOrdersByStatus(@PathVariable Long userId, @PathVariable String status) {
        List<Order> orders = orderService.getUserOrdersByStatus(userId, status);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("userId", userId);
        response.put("status", status);
        response.put("count", orders.size());
        response.put("data", orders);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/orders/user/{userId}/recent
     * Lấy n đơn hàng gần nhất
     */
    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<?> getRecentOrders(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "PENDING") String status) {

        List<Order> orders = orderService.getRecentOrders(userId, status, Math.min(limit, 100));

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("userId", userId);
        response.put("count", orders.size());
        response.put("data", orders);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/orders/code/{orderCode}
     * Tìm kiếm đơn hàng theo order code
     */
    @GetMapping("/code/{orderCode}")
    public ResponseEntity<?> getOrderByCode(@PathVariable String orderCode) {
        var orderOpt = orderService.findByOrderCode(orderCode);

        if (orderOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Order not found"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", orderOpt.get());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/orders/{orderId}
     * Lấy thông tin chi tiết đơn hàng
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        var orderOpt = orderService.getOrderById(orderId);

        if (orderOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Order not found"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", orderOpt.get());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/orders/user/{userId}/range
     * Lấy đơn hàng trong khoảng thời gian (Function Partition: YEAR(order_date))
     */
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<?> getOrdersInRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime start = LocalDateTime.parse(startDate, formatter);
            LocalDateTime end = LocalDateTime.parse(endDate, formatter);

            List<Order> orders = orderService.getUserOrdersBetween(userId, start, end);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", userId);
            response.put("startDate", startDate);
            response.put("endDate", endDate);
            response.put("count", orders.size());
            response.put("data", orders);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid date format. Use ISO format: yyyy-MM-ddTHH:mm:ss"));
        }
    }

    /**
     * PUT /api/orders/{orderId}/status
     * Cập nhật trạng thái đơn hàng
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody UpdateOrderStatusRequest request) {
        try {
            Order order = orderService.updateOrderStatus(orderId, request.getStatus());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order status updated successfully");
            response.put("orderId", orderId);
            response.put("status", order.getStatus());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Order not found"));
        }
    }

    /**
     * DELETE /api/orders/{orderId}/cancel
     * Hủy đơn hàng
     */
    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        try {
            orderService.cancelOrder(orderId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order cancelled successfully");
            response.put("orderId", orderId);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Order not found"));
        }
    }

    /**
     * GET /api/orders/user/{userId}/statistics
     * Lấy thống kê đơn hàng
     */
    @GetMapping("/user/{userId}/statistics")
    public ResponseEntity<?> getOrderStatistics(@PathVariable Long userId) {
        OrderStatistics stats = orderService.getOrderStatistics(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("userId", userId);
        response.put("statistics", stats);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/orders/user/{userId}/spent
     * Tính tổng chi tiêu
     */
    @GetMapping("/user/{userId}/spent")
    public ResponseEntity<?> getTotalSpent(@PathVariable Long userId) {
        BigDecimal spent = orderService.getTotalSpent(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("userId", userId);
        response.put("totalSpent", spent);
        return ResponseEntity.ok(response);
    }

    // Request DTOs
    @lombok.Data
    public static class CreateOrderRequest {
        private Long userId;
        private BigDecimal totalAmount;
        private String shippingAddress;
    }

    @lombok.Data
    public static class UpdateOrderStatusRequest {
        private String status;
    }
}
