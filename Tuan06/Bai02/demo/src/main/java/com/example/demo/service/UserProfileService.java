package com.example.demo.service;

import com.example.demo.entity.UserProfile;
import com.example.demo.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service cho UserProfile (Vertical Partitioning)
 * Tách thông tin cá nhân ra để giảm kích thước bảng users
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    /**
     * Tạo profile mới
     */
    public UserProfile createProfile(UserProfile profile) {
        UserProfile saved = userProfileRepository.save(profile);
        log.info("UserProfile created: userId={}", saved.getUserId());
        return saved;
    }

    /**
     * Lấy thông tin cá nhân
     * Index: user_id -> O(1) lookup
     */
    @Transactional(readOnly = true)
    public Optional<UserProfile> getProfileByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId);
    }

    /**
     * Cập nhật thông tin cá nhân
     */
    public UserProfile updateProfile(Long userId, UserProfile updatedProfile) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (updatedProfile.getFirstName() != null) {
            profile.setFirstName(updatedProfile.getFirstName());
        }
        if (updatedProfile.getLastName() != null) {
            profile.setLastName(updatedProfile.getLastName());
        }
        if (updatedProfile.getPhoneNumber() != null) {
            profile.setPhoneNumber(updatedProfile.getPhoneNumber());
        }
        if (updatedProfile.getAddress() != null) {
            profile.setAddress(updatedProfile.getAddress());
        }
        if (updatedProfile.getCity() != null) {
            profile.setCity(updatedProfile.getCity());
        }
        if (updatedProfile.getCountry() != null) {
            profile.setCountry(updatedProfile.getCountry());
        }
        if (updatedProfile.getPostalCode() != null) {
            profile.setPostalCode(updatedProfile.getPostalCode());
        }
        if (updatedProfile.getDateOfBirth() != null) {
            profile.setDateOfBirth(updatedProfile.getDateOfBirth());
        }
        if (updatedProfile.getGender() != null) {
            profile.setGender(updatedProfile.getGender());
        }
        if (updatedProfile.getAvatar() != null) {
            profile.setAvatar(updatedProfile.getAvatar());
        }

        UserProfile saved = userProfileRepository.save(profile);
        log.info("UserProfile updated: userId={}", userId);
        return saved;
    }

    /**
     * Tìm kiếm theo số điện thoại
     * Index: phone_number -> O(log n)
     */
    @Transactional(readOnly = true)
    public Optional<UserProfile> getProfileByPhoneNumber(String phoneNumber) {
        return userProfileRepository.findByPhoneNumber(phoneNumber);
    }
}
