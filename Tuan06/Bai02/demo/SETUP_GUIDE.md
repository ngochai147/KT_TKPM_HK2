# 🚀 Hướng Dẫn Cài Đặt & Chạy Hệ Thống

## 📋 Yêu Cầu Hệ Thống

### Software Requirements
- **JDK:** Java 17 hoặc cao hơn
- **Maven:** 3.8.1 hoặc cao hơn
- **MariaDB/MySQL:** 10.5 hoặc cao hơn
- **IDE:** IntelliJ IDEA, Eclipse, VS Code

### Port Requirements
- **Application:** 8080
- **Database:** 3306

---

## 🔧 Cài Đặt Database

### 1. Khởi Động MariaDB/MySQL

**Windows:**
```bash
# Sử dụng XAMPP
# Start Apache & MariaDB from XAMPP Control Panel
```

**Linux/Mac:**
```bash
# Start service
brew services start mariadb
# hoặc
sudo service mysql start
```

### 2. Tạo Database

```bash
# Kết nối MySQL client
mysql -u root -p

# Hoặc nếu không có password
mysql -u root
```

```sql
-- Tạo database
CREATE DATABASE user_management_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Kiểm tra
SHOW DATABASES;
SHOW CREATE DATABASE user_management_db;
```

### 3. Cấu Hình Connection (application.properties)

Mở file: `src/main/resources/application.properties`

```properties
# Database Configuration
spring.datasource.url=jdbc:mariadb://localhost:3306/user_management_db?autoReconnect=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root          # Thay bằng password của bạn
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update     # Auto create/update schema
```

---

## 🏗️ Xây Dựng & Chạy Ứng Dụng

### 1. Tải Dependencies

```bash
cd demo
mvn clean install
```

### 2. Chạy Application

**Option A: Maven**
```bash
mvn spring-boot:run
```

**Option B: IDE**
- Mở DemoApplication.java
- Click "Run" hoặc Shift+F10

**Output mong đợi:**
```
2025-03-25 14:30:45.123 INFO  Application started in 2.345s
2025-03-25 14:30:45.456 INFO  Tomcat started on port(s): 8080
2025-03-25 14:30:45.789 INFO  Hibernate: Creating tables...
```

### 3. Kiểm Tra Ứng Dụng Khởi Động Thành Công

```bash
# Kiểm tra health check
curl http://localhost:8080/actuator/health

# Hoặc mở browser
http://localhost:8080/
```

---

## 📡 Testing API Endpoints

### Tool: Postman/Insomnia/curl

### 1️⃣ User Management (POST /api/users/register)

```bash
# Đăng ký người dùng mới
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'

# Response:
{
  "success": true,
  "message": "User registered successfully",
  "userId": 1,
  "email": "john@example.com"
}
```

### 2️⃣ User Login (POST /api/users/login)

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'

# Response:
{
  "success": true,
  "message": "Login successful",
  "userId": 1,
  "email": "john@example.com",
  "status": "ACTIVE"
}
```

### 3️⃣ Get User Profile (GET /api/profiles/{userId})

```bash
# Đầu tiên tạo profile
curl -X POST http://localhost:8080/api/profiles \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "fullName": "John Doe",
    "phoneNumber": "0909123456",
    "address": "123 Main Street",
    "dateOfBirth": "1990-01-15",
    "gender": "MALE"
  }'

# Sau đó lấy profile
curl -X GET http://localhost:8080/api/profiles/1
```

### 4️⃣ Record Login History (POST /api/login-history/record)

```bash
curl -X POST http://localhost:8080/api/login-history/record \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "ipAddress": "192.168.1.100",
    "userAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)",
    "device": "WEB"
  }'

# Response:
{
  "success": true,
  "message": "Login recorded successfully",
  "historyId": 1
}
```

### 5️⃣ Get Login History (GET /api/login-history/user/{userId})

```bash
curl -X GET http://localhost:8080/api/login-history/user/1

# Response:
{
  "success": true,
  "userId": 1,
  "count": 1,
  "data": [
    {
      "historyId": 1,
      "userId": 1,
      "loginAt": "2025-03-25T14:35:00",
      "ipAddress": "192.168.1.100",
      "device": "WEB"
    }
  ]
}
```

### 6️⃣ Create Order (POST /api/orders)

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "totalAmount": 500.50,
    "shippingAddress": "123 Delivery Street"
  }'

# Response:
{
  "success": true,
  "message": "Order created successfully",
  "orderId": 1,
  "orderCode": "ORD-1-A1B2C3D4",
  "status": "PENDING"
}
```

### 7️⃣ Get Orders by User (GET /api/orders/user/{userId})

```bash
curl -X GET http://localhost:8080/api/orders/user/1

# Response:
{
  "success": true,
  "userId": 1,
  "count": 1,
  "data": [
    {
      "orderId": 1,
      "userId": 1,
      "orderCode": "ORD-1-A1B2C3D4",
      "totalAmount": 500.50,
      "status": "PENDING",
      "shippingAddress": "123 Delivery Street"
    }
  ]
}
```

### 8️⃣ Update Order Status (PUT /api/orders/{orderId}/status)

```bash
curl -X PUT http://localhost:8080/api/orders/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "DELIVERED"
  }'
```

---

## 🗄️ Database Verification

### 1. Kiểm Tra Schema

```bash
mysql -u root -p user_management_db -e "SHOW TABLES;"
```

**Output:**
```
login_histories
orders
user_profiles
users
```

### 2. Kiểm Tra Data

```sql
-- SSH vào MySQL
mysql -u root -p user_management_db

-- Kiểm tra users
SELECT * FROM users;

-- Kiểm tra user_profiles
SELECT * FROM user_profiles;

-- Kiểm tra login_histories
SELECT * FROM login_histories;

-- Kiểm tra orders
SELECT * FROM orders;

-- Kiểm tra partitions (login_histories)
SELECT TABLE_NAME, PARTITION_NAME, PARTITION_METHOD
FROM INFORMATION_SCHEMA.PARTITIONS
WHERE TABLE_SCHEMA = 'user_management_db'
AND TABLE_NAME = 'login_histories';

-- Kiểm tra partitions (orders)
SELECT TABLE_NAME, PARTITION_NAME, PARTITION_EXPRESSION
FROM INFORMATION_SCHEMA.PARTITIONS
WHERE TABLE_SCHEMA = 'user_management_db'
AND TABLE_NAME = 'orders';
```

### 3. Kiểm Tra Index

```sql
-- Index trên users table
SHOW INDEXES FROM users;

-- Index trên login_histories
SHOW INDEXES FROM login_histories;

-- Index trên orders
SHOW INDEXES FROM orders;
```

---

## 🔍 Monitoring & Logs

### Log Configuration

Logs được cấu hình trong `application.properties`:

```properties
logging.level.root=INFO
logging.level.com.example.demo=DEBUG
logging.level.org.spring.web=INFO
```

### Xem Logs Real-time

```bash
# Nếu chạy từ Maven
mvn spring-boot:run

# Nhìn console output cho DEBUG logs
# 2025-03-25 14:35:45.123 DEBUG [UserService] User registered: userId=1, email=john@example.com
```

### View Database Queries

Enable Hibernate SQL logging:

```properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

---

## 🐛 Troubleshooting

### ❌ Lỗi: "Connection refused" (Database)

**Giải pháp:**
```bash
# Kiểm tra MariaDB đang chạy không
mysql -u root -p

# Nếu không kết nối được, khởi động lại service
# Windows: net start MySQL80
# Linux: sudo service mysql start
```

### ❌ Lỗi: "Table doesn't exist"

**Giải pháp:**
1. Xóa application.properties `spring.jpa.hibernate.ddl-auto=update`
2. Chạy lại application
3. Kiểm tra logs cho error

### ❌ Lỗi: "Bad password"

**Giải pháp:**
```bash
# Thay đổi password User root
mysql -u root
ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password';
FLUSH PRIVILEGES;

# Cập nhật application.properties
spring.datasource.password=new_password
```

### ❌ Port 8080 đang sử dụng

**Giải pháp:**
```bash
# Thay đổi port trong application.properties
server.port=8081

# Hoặc kill process đang dùng port 8080
# Windows: netstat -ano | findstr :8080
# Linux: lsof -i :8080
```

---

## ✅ Checklist Cài Đặt

- [ ] Java 17+ installed
- [ ] Maven installed
- [ ] MariaDB/MySQL running
- [ ] Database `user_management_db` created
- [ ] `application.properties` configured
- [ ] `mvn clean install` successful
- [ ] Application started on port 8080
- [ ] API endpoints responsive
- [ ] Database schema created
- [ ] Sample data inserted

---

## 📞 Tiếp Theo

Sau khi cài đặt thành công:

1. **Đọc tài liệu:** [USER_MANAGEMENT_SYSTEM.md](USER_MANAGEMENT_SYSTEM.md)
2. **Khám phá APIs:** Dùng Postman để test endpoints
3. **Monitor Database:** Kiểm tra performance & partitions
4. **Deploy:** Chuẩn bị cho production

---

**Happy Development! 🚀**
