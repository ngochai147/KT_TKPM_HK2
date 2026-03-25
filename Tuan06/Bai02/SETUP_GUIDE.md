# 🔧 HƯỚNG DẪN SỬA LỖI VÀ CHẠY ỨNG DỤNG

## ✅ TẤT CẢ LỖI ĐÃ ĐƯỢC SỬA

### 🎉 Backend - Đã sửa xong:
1. ✅ Spring Boot version 3.4.1
2. ✅ SecurityConfig đã được tạo
3. ✅ UserProfile entity đã được sửa (firstName/lastName)
4. ✅ OrderController catch exception đã được sửa
5. ✅ UserProfileService đã được cập nhật

### ✅ Frontend - Không có lỗi
Tất cả components đã sẵn sàng

---

## ⚠️ VẤN ĐỀ HIỆN TẠI: DATABASE CHƯA TỒN TẠI

Backend compile thành công nhưng không kết nối được vì database `user_management_db` chưa được tạo.

---

## 🚀 HƯỚNG DẪN SETUP VÀ CHẠY

### Bước 1: Kiểm tra MariaDB/MySQL đang chạy

```bash
# Kiểm tra service
# Windows:
services.msc (tìm MySQL/MariaDB service)

# Hoặc kiểm tra port 3306
netstat -ano | findstr :3306
```

### Bước 2: Tạo Database

**Cách 1: Sử dụng MySQL Command Line**

```bash
# Kết nối vào MySQL/MariaDB
mysql -u root -p

# Nhập password (mặc định là 'sapassword' theo application.properties)
# Nếu password khác, cập nhật trong application.properties

# Chạy lệnh SQL
CREATE DATABASE IF NOT EXISTS user_management_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

# Kiểm tra
SHOW DATABASES;
USE user_management_db;

# Thoát
exit;
```

**Cách 2: Sử dụng file SQL script**

```bash
# Chạy file setup_database.sql đã được tạo
mysql -u root -p < setup_database.sql
```

**Cách 3: Sử dụng phpMyAdmin/MySQL Workbench**
- Mở phpMyAdmin hoặc MySQL Workbench
- Tạo database mới tên `user_management_db`
- Charset: utf8mb4
- Collation: utf8mb4_unicode_ci

### Bước 3: Cấu hình Password (nếu cần)

Nếu password MySQL của bạn **KHÔNG phải** `sapassword`, cập nhật trong file:

`demo/src/main/resources/application.properties`

```properties
spring.datasource.password=YOUR_PASSWORD_HERE
```

### Bước 4: Chạy Backend

```bash
cd demo

# Compile và chạy
./mvnw clean spring-boot:run

# Hoặc trên Windows
mvnw.cmd clean spring-boot:run
```

**Backend sẽ chạy tại:** `http://localhost:8080`

**Kiểm tra backend đã chạy:**
```bash
curl http://localhost:8080/api/users/status/ACTIVE
```

Hoặc mở browser: http://localhost:8080/api/users/status/ACTIVE

### Bước 5: Chạy Frontend

```bash
cd frontend

# Cài đặt dependencies (chỉ lần đầu)
npm install

# Chạy frontend
npm start
```

**Frontend sẽ mở tại:** `http://localhost:3000`

---

## 📊 HIBERNATE SẼ TỰ ĐỘNG TẠO TABLES

Khi backend chạy lần đầu, Hibernate sẽ tự động tạo các bảng:
- `users`
- `user_profiles`
- `orders`
- `login_history`

Kiểm tra bằng cách:
```sql
USE user_management_db;
SHOW TABLES;
DESCRIBE users;
```

---

## 🐛 TROUBLESHOOTING

### Lỗi: "Access denied for user 'root'@'localhost'"
**Giải pháp:** Password sai. Cập nhật password trong `application.properties`

### Lỗi: "Communications link failure"
**Giải pháp:**
- MariaDB/MySQL chưa chạy
- Port 3306 bị chiếm
- Kiểm tra firewall

### Lỗi: "Unknown database 'user_management_db'"
**Giải pháp:** Chạy lại Bước 2 để tạo database

### Lỗi: "Table doesn't exist"
**Giải pháp:**
1. Đảm bảo `spring.jpa.hibernate.ddl-auto=update` trong application.properties
2. Restart backend

### Frontend không kết nối được Backend
**Giải pháp:**
1. Đảm bảo backend đang chạy trên port 8080
2. Kiểm tra CORS trong `SecurityConfig.java`
3. Xem console của browser để check lỗi

---

## ✨ TEST ỨNG DỤNG

### 1. Test Backend API trực tiếp:

**Đăng ký user:**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

**Đăng nhập:**
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'
```

### 2. Test Frontend:

1. Mở `http://localhost:3000`
2. Click "Sign up" → Đăng ký tài khoản mới
3. Đăng nhập với tài khoản vừa tạo
4. Tạo Profile
5. Tạo Order
6. Xem Statistics và Login History

---

## 📝 CHECKLIST TRƯỚC KHI CHẠY

- [ ] MariaDB/MySQL đang chạy
- [ ] Database `user_management_db` đã được tạo
- [ ] Password trong `application.properties` đúng
- [ ] Port 8080 không bị chiếm (cho backend)
- [ ] Port 3000 không bị chiếm (cho frontend)
- [ ] Node.js và npm đã được cài đặt
- [ ] Java 17+ đã được cài đặt

---

## 🎯 KẾT QUẢ MONG ĐỢI

Sau khi setup xong:

**Backend Console sẽ hiển thị:**
```
Started DemoApplication in X seconds
Tomcat started on port 8080
```

**Frontend Browser sẽ hiển thị:**
- Login/Register page
- Dashboard với statistics
- Tất cả các menus hoạt động

---

## 📧 HỖ TRỢ

Nếu vẫn gặp lỗi, kiểm tra:
1. Backend logs trong terminal
2. Frontend console trong browser (F12)
3. Database connection trong MySQL

## 🎊 HOÀN THÀNH!

Sau khi làm theo các bước trên, ứng dụng sẽ chạy hoàn hảo!
