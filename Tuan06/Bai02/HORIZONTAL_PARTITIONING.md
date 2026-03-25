# 🗂️ HORIZONTAL PARTITIONING - IMPLEMENTATION GUIDE

## 📊 Khái niệm

**Horizontal Partitioning** (chia theo chiều ngang) chia dữ liệu của một bảng vào nhiều databases dựa trên một **khóa phân chia** (partition key).

### Ví dụ thực tế:
- **10,000 users** được chia vào **10 databases**
  - **user_db_0**: chứa users có id % 10 = 0 (10, 20, 30, ..., 10000)
  - **user_db_1**: chứa users có id % 10 = 1 (1, 11, 21, ..., 9991)
  - **user_db_2**: chứa users có id % 10 = 2 (2, 12, 22, ..., 9992)
  - ...
  - **user_db_9**: chứa users có id % 10 = 9 (9, 19, 29, ..., 9999)

---

## 🔧 CÔNG THỨC PHÂN CHIA

```
partition_index = user_id % PARTITION_COUNT
database_name = "user_db_" + partition_index
```

### Ví dụ:
- `user_id = 1` → `1 % 10 = 1` → `user_db_1`
- `user_id = 10` → `10 % 10 = 0` → `user_db_0`
- `user_id = 15` → `15 % 10 = 5` → `user_db_5`
- `user_id = 123` → `123 % 10 = 3` → `user_db_3`
- `user_id = 5000` → `5000 % 10 = 0` → `user_db_0`
- `user_id = 9999` → `9999 % 10 = 9` → `user_db_9`

---

## 💾 SETUP: TẠO 10 DATABASES

### Cách 1: MySQL Command Line

```bash
mysql -u root -p
```

```sql
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

-- Thoát
exit;
```

### Cách 2: Sử dụng Script SQL

Tạo file `setup_partitions.sql`:

```sql
-- Tạo 10 partition databases
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
```

Chạy:
```bash
mysql -u root -p < setup_partitions.sql
```

---

## 🚀 SỬ DỤNG API

### 1. Lấy thông tin partition của user

**Request:**
```bash
GET http://localhost:8080/api/partitioning/info/123
```

**Response:**
```json
{
  "success": true,
  "userId": 123,
  "partitionIndex": 3,
  "databaseName": "user_db_3",
  "message": "User 123 belongs to user_db_3"
}
```

### 2. Liệt kê tất cả databases

**Request:**
```bash
GET http://localhost:8080/api/partitioning/all-databases
```

**Response:**
```json
{
  "success": true,
  "totalPartitions": 10,
  "databases": [
    "user_db_0",
    "user_db_1",
    "user_db_2",
    "user_db_3",
    "user_db_4",
    "user_db_5",
    "user_db_6",
    "user_db_7",
    "user_db_8",
    "user_db_9"
  ],
  "message": "System has 10 partitions for horizontal partitioning"
}
```

### 3. Test phân phối 100 users

**Request:**
```bash
GET http://localhost:8080/api/partitioning/test
```

**Response:**
```json
{
  "success": true,
  "testUsersCount": 100,
  "totalPartitions": 10,
  "distribution": {
    "0": 10,
    "1": 10,
    "2": 10,
    "3": 10,
    "4": 10,
    "5": 10,
    "6": 10,
    "7": 10,
    "8": 10,
    "9": 10
  },
  "examplesForFirst10Users": {
    "1": "user_db_1",
    "2": "user_db_2",
    "3": "user_db_3",
    "4": "user_db_4",
    "5": "user_db_5",
    "6": "user_db_6",
    "7": "user_db_7",
    "8": "user_db_8",
    "9": "user_db_9",
    "10": "user_db_0"
  }
}
```

### 4. Test với 10,000 users (Large Scale)

**Request:**
```bash
GET http://localhost:8080/api/partitioning/large-scale-test
```

**Response:**
```json
{
  "success": true,
  "scenario": "10,000 users distributed across 10 databases",
  "testUsersCount": 10000,
  "totalPartitions": 10,
  "distribution": {
    "0": 1000,
    "1": 1000,
    "2": 1000,
    "3": 1000,
    "4": 1000,
    "5": 1000,
    "6": 1000,
    "7": 1000,
    "8": 1000,
    "9": 1000
  },
  "averageUsersPerDatabase": 1000,
  "message": "With Horizontal Partitioning, 10,000 users are evenly distributed"
}
```

### 5. Xem công thức

**Request:**
```bash
GET http://localhost:8080/api/partitioning/formula
```

**Response:**
```json
{
  "success": true,
  "partitioningFormula": "partition_index = user_id % PARTITION_COUNT",
  "partitionCount": 10,
  "examples": {
    "user_id=1": "1 % 10 = 1 -> user_db_1",
    "user_id=10": "10 % 10 = 0 -> user_db_0",
    "user_id=15": "15 % 10 = 5 -> user_db_5",
    "user_id=123": "123 % 10 = 3 -> user_db_3",
    "user_id=5000": "5000 % 10 = 0 -> user_db_0",
    "user_id=9999": "9999 % 10 = 9 -> user_db_9"
  },
  "explanation": "Mỗi user được gán vào một partition dựa trên user_id..."
}
```

---

## 📂 FILE STRUCTURE

```
demo/
├── src/
│   └── main/
│       └── java/
│           └── com/example/demo/
│               ├── config/
│               │   └── PartitioningStrategy.java ✨ (New)
│               ├── service/
│               │   └── PartitioningService.java ✨ (New)
│               └── controller/
│                   └── PartitioningInfoController.java ✨ (New)
```

---

## 🎯 KIẾN TRÚC HIỆN TẠI

### Mô hình phân chia:

```
Frontend (React)
    ↓
Backend (Spring Boot)
    ↓
PartitioningService
    ↓ (xác định partition)
    ├── user_db_0
    ├── user_db_1
    ├── user_db_2
    ├── ...
    └── user_db_9
```

### Logic Flow:

1. **User đăng nhập** với id = 123
2. **PartitioningService** tính: `123 % 10 = 3`
3. **Dữ liệu user** được lưu vào `user_db_3`
4. **Query user** sẽ tự động đến `user_db_3`

---

## ⚙️ CẠU HÌNH CURRENT IMPLEMENTATION

Hiện tại, hệ thống:
- ✅ Có **PartitioningStrategy** để tính partition
- ✅ Có **PartitioningService** để quản lý logic
- ✅ Có **PartitioningInfoController** để hiển thị thông tin
- ⚠️ Chưa integrate vào **UserService**, **OrderService**, **LoginHistoryService**

---

## 📈 SCALING STRATEGY

Nếu muốn tăng từ 10 lên 100 partitions:

### Bước 1: Thay đổi PARTITION_COUNT

File: `PartitioningStrategy.java`

```java
private static final int PARTITION_COUNT = 100; // Thay 10 thành 100
```

### Bước 2: Tạo 100 databases

```sql
-- Tạo databases từ user_db_0 đến user_db_99
```

### Bước 3: Data Migration (quan trọng!)

Migrate dữ liệu từ 10 databases cũ sang 100 databases mới

---

## 🔍 MONITORING

Để kiểm tra phân phối dữ liệu:

```sql
USE user_db_0; SELECT COUNT(*) FROM users;
USE user_db_1; SELECT COUNT(*) FROM users;
...
USE user_db_9; SELECT COUNT(*) FROM users;
```

---

## ⚡ LỢI ÍCH

✅ **Scalability**: Chia dữ liệu vào nhiều databases
✅ **Performance**: Mỗi database nhỏ hơn → query nhanh hơn
✅ **High Availability**: Một database down không ảnh hưởng toàn bộ
✅ **Load Distribution**: Tải được phân đều

---

## ⚠️ CHALLENGES

❌ **Complexity**: Code phức tạp hơn
❌ **Joins khó**: Không thể join across partitions dễ dàng
❌ **Balancing**: Phải chắc chắn data phân phối đều
❌ **Migration**: Rebalancing khi thay đổi partition count

---

## 🎓 NEXT STEPS

1. **Tạo 10 databases** (xem phần SETUP)
2. **Compile & Run** backend
3. **Test APIs** để kiểm tra partitioning
4. **Monitor** distribution qua SQL queries
5. **Integrate** vào services (optional - advanced)

