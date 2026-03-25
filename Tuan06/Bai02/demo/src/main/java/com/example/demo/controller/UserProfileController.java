package com.example.demo.controller;

import com.example.demo.entity.UserProfile;
import com.example.demo.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller cho UserProfile (Vertical Partitioning)
 */
@Slf4j
@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    /**
     * POST /api/profiles
     * Tạo profile mới
     */
    @PostMapping
    public ResponseEntity<?> createProfile(@RequestBody UserProfile profile) {
        try {
            UserProfile created = userProfileService.createProfile(profile);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profile created successfully");
            response.put("data", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     * GET /api/profiles/{userId}
     * Lấy thông tin cá nhân
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable Long userId) {
        var profileOpt = userProfileService.getProfileByUserId(userId);

        if (profileOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Profile not found"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", profileOpt.get());
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/profiles/{userId}
     * Cập nhật thông tin cá nhân
     */
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(@PathVariable Long userId, @RequestBody UserProfile updatedProfile) {
        try {
            UserProfile updated = userProfileService.updateProfile(userId, updatedProfile);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profile updated successfully");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Profile not found"));
        }
    }

    /**
     * GET /api/profiles/phone/{phoneNumber}
     * Tìm profile theo số điện thoại
     */
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<?> getProfileByPhone(@PathVariable String phoneNumber) {
        var profileOpt = userProfileService.getProfileByPhoneNumber(phoneNumber);

        if (profileOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Profile not found"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", profileOpt.get());
        return ResponseEntity.ok(response);
    }
}
