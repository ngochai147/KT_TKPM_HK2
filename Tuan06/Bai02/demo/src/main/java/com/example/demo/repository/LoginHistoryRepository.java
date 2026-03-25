package com.example.demo.repository;

import com.example.demo.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Horizontal Partitioning: Chia theo user_id
 * Các queries sẽ sử dụng user_id để tận dụng partition
 */
@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

    @Query("SELECT lh FROM LoginHistory lh WHERE lh.userId = :userId ORDER BY lh.loginAt DESC")
    List<LoginHistory> findByUserId(@Param("userId") Long userId);

    @Query("SELECT lh FROM LoginHistory lh WHERE lh.userId = :userId AND lh.loginAt BETWEEN :startDate AND :endDate")
    List<LoginHistory> findLoginsBetween(@Param("userId") Long userId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT * FROM login_histories WHERE user_id = :userId ORDER BY login_at DESC LIMIT :limit",
           nativeQuery = true)
    List<LoginHistory> findRecentLoginsByUserId(@Param("userId") Long userId, @Param("limit") int limit);

    @Query("SELECT COUNT(lh) FROM LoginHistory lh WHERE lh.userId = :userId AND lh.loginAt >= :date")
    long countLoginsAfterDate(@Param("userId") Long userId, @Param("date") LocalDateTime date);
}
