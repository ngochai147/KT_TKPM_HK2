package com.example.demo.repository;

import com.example.demo.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("SELECT up FROM UserProfile up WHERE up.userId = :userId")
    Optional<UserProfile> findByUserId(@Param("userId") Long userId);

    @Query("SELECT up FROM UserProfile up WHERE up.phoneNumber = :phoneNumber")
    Optional<UserProfile> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query(value = "SELECT * FROM user_profiles WHERE phone_number = :phoneNumber LIMIT 1", nativeQuery = true)
    Optional<UserProfile> findByPhoneNumberNative(@Param("phoneNumber") String phoneNumber);
}
