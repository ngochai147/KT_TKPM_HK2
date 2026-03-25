package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Horizontal & Function Partitioning:
 * - Horizontal: chia theo user_id (range partition)
 * - Function: chia theo YEAR(order_date) cho khả năng mở rộng thời gian
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.userId = :userId ORDER BY o.orderDate DESC")
    List<Order> findByUserId(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.status = :status")
    List<Order> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    @Query("SELECT o FROM Order o WHERE o.orderCode = :orderCode")
    Optional<Order> findByOrderCode(@Param("orderCode") String orderCode);

    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findOrdersBetween(@Param("userId") Long userId,
                                  @Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT * FROM orders WHERE user_id = :userId AND status = :status ORDER BY order_date DESC LIMIT :limit",
           nativeQuery = true)
    List<Order> findRecentOrdersByUserAndStatus(@Param("userId") Long userId,
                                                 @Param("status") String status,
                                                 @Param("limit") int limit);

    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.userId = :userId AND o.status = 'DELIVERED'")
    BigDecimal getTotalSpentByUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.userId = :userId AND o.status = :status")
    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
}
