-- =====================================================
-- HORIZONTAL PARTITIONING SETUP
-- Tạo 10 partition databases
-- =====================================================

-- Tạo 10 databases
CREATE DATABASE IF NOT EXISTS user_db_0 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS user_db_1 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS user_db_2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS user_db_3 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS user_db_4 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS user_db_5 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS user_db_6 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS user_db_7 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS user_db_8 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS user_db_9 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Kiểm tra
SHOW DATABASES LIKE 'user_db_%';

-- =====================================================
-- THÔNG TIN PARTITIONING
-- =====================================================
-- Công thức: partition_index = user_id % 10
--
-- Ví dụ:
-- - user_id = 1 -> 1 % 10 = 1 -> user_db_1
-- - user_id = 10 -> 10 % 10 = 0 -> user_db_0
-- - user_id = 123 -> 123 % 10 = 3 -> user_db_3
-- - user_id = 5000 -> 5000 % 10 = 0 -> user_db_0
-- - user_id = 9999 -> 9999 % 10 = 9 -> user_db_9
--
-- 10,000 users sẽ được chia đều vào 10 databases:
-- - user_db_0: ~ 1,000 users
-- - user_db_1: ~ 1,000 users
-- - ...
-- - user_db_9: ~ 1,000 users

-- Hibernate sẽ tự động tạo các tables trong mỗi database
