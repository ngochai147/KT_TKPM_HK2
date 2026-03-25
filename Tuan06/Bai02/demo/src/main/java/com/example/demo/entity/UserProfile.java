package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Vertical Partitioning: Tách thông tin cá nhân từ bảng users
 * Giảm kích thước bảng users, tăng cache efficiency
 */
@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    @Id
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true)
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String city;

    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(columnDefinition = "ENUM('MALE', 'FEMALE', 'OTHER')")
    private String gender;

    @Column(columnDefinition = "TEXT")
    private String avatar;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
