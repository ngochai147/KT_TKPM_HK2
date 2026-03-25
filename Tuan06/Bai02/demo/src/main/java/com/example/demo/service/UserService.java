package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Đăng ký người dùng mới
     * Index: email -> O(log n) lookup
     */
    public User registerUser(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .status("ACTIVE")
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered: userId={}, email={}", savedUser.getUserId(), email);
        return savedUser;
    }

    /**
     * Xác thực đăng nhập
     * Index: email -> < 100ms
     */
    @Transactional(readOnly = true)
    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if ("BLOCKED".equals(user.getStatus())) {
                log.warn("Login attempt for blocked account: {}", email);
                return Optional.empty();
            }

            if (passwordEncoder.matches(password, user.getPasswordHash())) {
                return Optional.of(user);
            }
        }

        log.debug("Failed login attempt: {}", email);
        return Optional.empty();
    }

    /**
     * Lấy thông tin người dùng
     * Index: user_id (primary key) -> O(1)
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * Tìm kiếm theo email
     * Index: email (unique) -> O(log n)
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Lấy danh sách người dùng theo trạng thái
     * Index: status -> O(n*log n) scan
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByStatus(String status) {
        return userRepository.findByStatus(status);
    }

    /**
     * Cập nhật trạng thái người dùng
     */
    public User updateUserStatus(Long userId, String newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(newStatus);
        return userRepository.save(user);
    }

    /**
     * Đổi mật khẩu
     */
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password changed for userId={}", userId);
    }

    /**
     * Xóa người dùng (soft delete)
     */
    public void deleteUser(Long userId) {
        updateUserStatus(userId, "INACTIVE");
        log.info("User deleted (marked inactive): userId={}", userId);
    }
}
