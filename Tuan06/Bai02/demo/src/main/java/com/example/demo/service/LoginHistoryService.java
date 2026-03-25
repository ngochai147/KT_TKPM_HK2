package com.example.demo.service;

import com.example.demo.entity.LoginHistory;
import com.example.demo.repository.LoginHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service cho LoginHistory (Horizontal Partitioning)
 * Partitioned by user_id -> giúp quản lý hàng triệu login logs
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginHistoryService {

    private final LoginHistoryRepository loginHistoryRepository;

    /**
     * Ghi lại login event
     * Partition sẽ được chọn dựa trên user_id
     */
    public LoginHistory recordLogin(Long userId, String ipAddress, String userAgent, String device) {
        LoginHistory history = LoginHistory.builder()
                .userId(userId)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .device(device)
                .build();

        LoginHistory saved = loginHistoryRepository.save(history);
        log.info("Login recorded: userId={}, ip={}, device={}", userId, ipAddress, device);
        return saved;
    }

    /**
     * Ghi lại logout event
     */
    public void recordLogout(Long historyId) {
        loginHistoryRepository.findById(historyId).ifPresent(history -> {
            history.setLogoutAt(LocalDateTime.now());
            loginHistoryRepository.save(history);
            log.info("Logout recorded: historyId={}", historyId);
        });
    }

    /**
     * Lấy lịch sử login của user
     * Index composite: (user_id, login_at) -> O(log n) + index scan
     * Partition sẽ được chọn dựa trên user_id
     */
    @Transactional(readOnly = true)
    public List<LoginHistory> getLoginHistory(Long userId) {
        return loginHistoryRepository.findByUserId(userId);
    }

    /**
     * Lấy login history trong khoảng thời gian
     * Tận dụng partitioning và index composite
     */
    @Transactional(readOnly = true)
    public List<LoginHistory> getLoginHistoryBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return loginHistoryRepository.findLoginsBetween(userId, startDate, endDate);
    }

    /**
     * Lấy n lần login gần nhất (tối ưu với LIMIT)
     * Native query để tận dụng tối đa database engine
     */
    @Transactional(readOnly = true)
    public List<LoginHistory> getRecentLogins(Long userId, int limit) {
        return loginHistoryRepository.findRecentLoginsByUserId(userId, limit);
    }

    /**
     * Đếm lần login trong ngày
     */
    @Transactional(readOnly = true)
    public long getLoginCountToday(Long userId) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        return loginHistoryRepository.countLoginsAfterDate(userId, startOfDay);
    }

    /**
     * Kiểm tra hoạt động đáng ngờ (nhiều login từ IP khác nhau)
     */
    @Transactional(readOnly = true)
    public long countUniqueIPsLastNDays(Long userId, int days) {
        LocalDateTime nDaysAgo = LocalDateTime.now().minusDays(days);
        List<LoginHistory> histories = loginHistoryRepository.findLoginsBetween(
                userId, nDaysAgo, LocalDateTime.now()
        );
        return histories.stream()
                .map(LoginHistory::getIpAddress)
                .distinct()
                .count();
    }
}
