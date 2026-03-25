package com.example.demo.controller;

import com.example.demo.entity.LoginHistory;
import com.example.demo.service.LoginHistoryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller cho LoginHistory (Horizontal Partitioning)
 */
@Slf4j
@RestController
@RequestMapping("/api/login-history")
@RequiredArgsConstructor
public class LoginHistoryController {

    private final LoginHistoryService loginHistoryService;

    /**
     * POST /api/login-history/record
     * Ghi nhận login event
     */
    @PostMapping("/record")
    public ResponseEntity<?> recordLogin(@RequestBody RecordLoginRequest request) {
        LoginHistory history = loginHistoryService.recordLogin(
                request.getUserId(),
                request.getIpAddress(),
                request.getUserAgent(),
                request.getDevice()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Login recorded successfully");
        response.put("historyId", history.getHistoryId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /api/login-history/{historyId}/logout
     * Ghi nhận logout event
     */
    @PostMapping("/{historyId}/logout")
    public ResponseEntity<?> recordLogout(@PathVariable Long historyId) {
        try {
            loginHistoryService.recordLogout(historyId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Logout recorded successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Login history not found"));
        }
    }

    /**
     * GET /api/login-history/user/{userId}
     * Lấy lịch sử login của user (Horizontal Partition)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getLoginHistory(@PathVariable Long userId) {
        List<LoginHistory> histories = loginHistoryService.getLoginHistory(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("userId", userId);
        response.put("count", histories.size());
        response.put("data", histories);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/login-history/user/{userId}/recent
     * Lấy n lần login gần nhất
     */
    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<?> getRecentLogins(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {

        List<LoginHistory> histories = loginHistoryService.getRecentLogins(userId, Math.min(limit, 100));

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("userId", userId);
        response.put("count", histories.size());
        response.put("data", histories);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/login-history/user/{userId}/today
     * Đếm lần login hôm nay
     */
    @GetMapping("/user/{userId}/today")
    public ResponseEntity<?> getLoginCountToday(@PathVariable Long userId) {
        long count = loginHistoryService.getLoginCountToday(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("userId", userId);
        response.put("loginsToday", count);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/login-history/user/{userId}/range
     * Lấy lịch sử login trong khoảng thời gian
     */
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<?> getLoginHistoryRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime start = LocalDateTime.parse(startDate, formatter);
            LocalDateTime end = LocalDateTime.parse(endDate, formatter);

            List<LoginHistory> histories = loginHistoryService.getLoginHistoryBetween(userId, start, end);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", userId);
            response.put("startDate", startDate);
            response.put("endDate", endDate);
            response.put("count", histories.size());
            response.put("data", histories);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid date format. Use ISO format: yyyy-MM-ddTHH:mm:ss"));
        }
    }

    /**
     * GET /api/login-history/user/{userId}/suspicious
     * Kiểm tra hoạt động đáng ngờ (nhiều IP khác nhau)
     */
    @GetMapping("/user/{userId}/suspicious")
    public ResponseEntity<?> checkSuspiciousActivity(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "7") int days) {

        long uniqueIPs = loginHistoryService.countUniqueIPsLastNDays(userId, days);
        List<LoginHistory> recentLogins = loginHistoryService.getRecentLogins(userId, 20);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("userId", userId);
        response.put("daysChecked", days);
        response.put("uniqueIPsCount", uniqueIPs);
        response.put("riskLevel", uniqueIPs > 3 ? "HIGH" : uniqueIPs > 1 ? "MEDIUM" : "LOW");
        response.put("recentLogins", recentLogins);
        return ResponseEntity.ok(response);
    }

    // Request DTO
    @Data
    public static class RecordLoginRequest {
        private Long userId;
        private String ipAddress;
        private String userAgent;
        private String device;
    }
}
