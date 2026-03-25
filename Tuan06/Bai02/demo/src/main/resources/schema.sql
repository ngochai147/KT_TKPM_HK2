-- =====================================================
-- USER MANAGEMENT SYSTEM - SCHEMA WITH PARTITIONING
-- =====================================================

-- 1. USERS TABLE - Baseline (Vertical Partitioning)
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'BLOCKED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_email (email),
    KEY idx_status (status),
    KEY idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. USER_PROFILES TABLE - Vertical Partitioning (tách thông tin cá nhân)
CREATE TABLE IF NOT EXISTS user_profiles (
    user_id BIGINT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) UNIQUE,
    address TEXT,
    date_of_birth DATE,
    gender ENUM('MALE', 'FEMALE', 'OTHER'),
    avatar LONGTEXT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    KEY idx_phone (phone_number),
    KEY idx_full_name (full_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. LOGIN_HISTORIES TABLE - Horizontal Partitioning (chia theo user_id)
-- Các partition sẽ được tạo dựa trên range của user_id
CREATE TABLE IF NOT EXISTS login_histories (
    history_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    login_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    logout_at TIMESTAMP NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    device ENUM('WEB', 'MOBILE', 'API') DEFAULT 'WEB',
    KEY idx_user_id (user_id),
    KEY idx_login_at (login_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- PARTITIONING STRATEGY 1: HORIZONTAL PARTITIONING
-- chia login_histories theo user_id (Range)
-- =====================================================
ALTER TABLE login_histories PARTITION BY RANGE (user_id) (
    PARTITION p0 VALUES LESS THAN (2501),
    PARTITION p1 VALUES LESS THAN (5001),
    PARTITION p2 VALUES LESS THAN (7501),
    PARTITION p3 VALUES LESS THAN (10001),
    PARTITION p4 VALUES LESS THAN (20001),
    PARTITION p5 VALUES LESS THAN (50001),
    PARTITION p6 VALUES LESS THAN (100001),
    PARTITION p_max VALUES LESS THAN MAXVALUE
);

-- 4. ORDERS TABLE - Horizontal & Function Partitioning
-- Horizontal: chia theo user_id
-- Function (Time-based): chia theo năm (order_date)
CREATE TABLE IF NOT EXISTS orders (
    order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    order_code VARCHAR(50) NOT NULL UNIQUE,
    order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(12, 2) NOT NULL,
    status ENUM('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    shipping_address TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_user_id (user_id),
    KEY idx_order_code (order_code),
    KEY idx_order_date (order_date),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- PARTITIONING STRATEGY 3: FUNCTION PARTITIONING
-- chia orders theo year(order_date)
-- =====================================================
ALTER TABLE orders PARTITION BY RANGE (YEAR(order_date)) (
    PARTITION p_2023 VALUES LESS THAN (2024),
    PARTITION p_2024 VALUES LESS THAN (2025),
    PARTITION p_2025 VALUES LESS THAN (2026),
    PARTITION p_2026 VALUES LESS THAN (2027),
    PARTITION p_2027 VALUES LESS THAN (2028),
    PARTITION p_future VALUES LESS THAN MAXVALUE
);

-- =====================================================
-- INDEXES FOR PERFORMANCE (< 100ms queries)
-- =====================================================

-- User queries
CREATE INDEX IF NOT EXISTS idx_user_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_user_status ON users(status);
CREATE INDEX IF NOT EXISTS idx_user_created_at ON users(created_at);

-- UserProfile queries
CREATE INDEX IF NOT EXISTS idx_profile_phone ON user_profiles(phone_number);
CREATE INDEX IF NOT EXISTS idx_profile_name ON user_profiles(full_name);

-- LoginHistory queries
CREATE INDEX IF NOT EXISTS idx_login_user_id ON login_histories(user_id);
CREATE INDEX IF NOT EXISTS idx_login_at ON login_histories(login_at);
CREATE INDEX IF NOT EXISTS idx_login_composite ON login_histories(user_id, login_at);

-- Order queries
CREATE INDEX IF NOT EXISTS idx_order_user_id ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_order_date ON orders(order_date);
CREATE INDEX IF NOT EXISTS idx_order_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_order_code ON orders(order_code);
CREATE INDEX IF NOT EXISTS idx_order_composite ON orders(user_id, status, order_date);

-- =====================================================
-- TEST DATA INSERTION
-- =====================================================

-- Insert test users
INSERT INTO users (email, password_hash, status) VALUES
('user1@example.com', '$2a$10$hash1', 'ACTIVE'),
('user2@example.com', '$2a$10$hash2', 'ACTIVE'),
('user3@example.com', '$2a$10$hash3', 'ACTIVE'),
('user4@example.com', '$2a$10$hash4', 'ACTIVE'),
('user5@example.com', '$2a$10$hash5', 'ACTIVE');

-- Insert user profiles
INSERT INTO user_profiles (user_id, full_name, phone_number, address, gender)
SELECT user_id, CONCAT('User ', user_id), CONCAT('090', LPAD(user_id, 7, '0')), '123 Main St', 'MALE'
FROM users LIMIT 5;

-- Insert login history
INSERT INTO login_histories (user_id, login_at, ip_address, device)
SELECT user_id, DATE_SUB(NOW(), INTERVAL FLOOR(RAND()*30) DAY), '192.168.1.1', 'WEB'
FROM users LIMIT 5;

-- Insert orders
INSERT INTO orders (user_id, order_code, order_date, total_amount, status, shipping_address)
SELECT user_id, CONCAT('ORD-', user_id, '-', DATE_FORMAT(NOW(), '%Y%m%d')), NOW(), ROUND(RAND()*1000, 2), 'PENDING', '123 Delivery St'
FROM users LIMIT 5;

-- =====================================================
-- VERIFICATION QUERIES
-- =====================================================

-- View partition information
SELECT TABLE_NAME, PARTITION_NAME, PARTITION_METHOD, PARTITION_EXPRESSION
FROM INFORMATION_SCHEMA.PARTITIONS
WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME IN ('login_histories', 'orders');

-- Show table status
SHOW TABLE STATUS LIKE 'users%';
SHOW TABLE STATUS LIKE 'orders';
SHOW TABLE STATUS LIKE 'login_histories';
