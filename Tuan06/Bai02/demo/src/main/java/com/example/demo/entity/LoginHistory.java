package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Horizontal Partitioning: Chia theo user_id
 * Giúp quản lý lịch sử login của hàng triệu users
 */
@Entity
@Table(name = "login_histories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "login_at", nullable = false)
    private LocalDateTime loginAt;

    @Column(name = "logout_at")
    private LocalDateTime logoutAt;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(columnDefinition = "ENUM('WEB', 'MOBILE', 'API')")
    private String device;

    @PrePersist
    protected void onCreate() {
        loginAt = LocalDateTime.now();
    }
}
