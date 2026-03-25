# Demo Management System - Full Stack Application

Hệ thống quản lý demo với backend Spring Boot và frontend ReactJS + TailwindCSS.

## 📋 Mục lục

- [Tổng quan](#tổng-quan)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cài đặt và chạy](#cài-đặt-và-chạy)
- [Tính năng](#tính-năng)
- [API Documentation](#api-documentation)
- [Screenshots](#screenshots)

## 🎯 Tổng quan

Hệ thống quản lý toàn diện với đầy đủ các chức năng:
- Quản lý người dùng (User Management)
- Quản lý đơn hàng (Order Management)
- Quản lý hồ sơ cá nhân (User Profile)
- Theo dõi lịch sử đăng nhập (Login History)
- Thống kê và báo cáo (Statistics & Analytics)

## 🛠 Công nghệ sử dụng

### Backend
- **Java 17+**
- **Spring Boot 3.x**
  - Spring Data JPA
  - Spring Web
  - Lombok
- **Database**: MySQL/PostgreSQL
- **Password Encryption**: BCrypt

### Frontend
- **React 18**
- **React Router DOM 6**
- **TailwindCSS 3.x**
- **Axios**

## 🚀 Cài đặt và chạy

### 1. Backend (Spring Boot)

```bash
# Di chuyển vào thư mục demo
cd demo

# Cấu hình database trong application.properties
# src/main/resources/application.properties

# Chạy ứng dụng
./mvnw spring-boot:run

# Hoặc trên Windows
mvnw.cmd spring-boot:run
```

Backend sẽ chạy tại: `http://localhost:8080`

### 2. Frontend (React)

```bash
# Di chuyển vào thư mục frontend
cd frontend

# Cài đặt dependencies
npm install

# Khởi động development server
npm start
```

Frontend sẽ mở tại: `http://localhost:3000`

## ✨ Tính năng

### 🔐 Authentication & Authorization
- ✅ Đăng ký tài khoản mới
- ✅ Đăng nhập với email/password
- ✅ Mã hóa mật khẩu với BCrypt
- ✅ Quản lý session
- ✅ Đổi mật khẩu

### 👥 User Management
- ✅ Danh sách người dùng
- ✅ Lọc theo trạng thái (ACTIVE, INACTIVE, SUSPENDED)
- ✅ Tìm kiếm theo email
- ✅ Cập nhật trạng thái
- ✅ Xóa người dùng (soft delete)

### 🛒 Order Management
- ✅ Tạo đơn hàng mới
- ✅ Xem danh sách đơn hàng
- ✅ Tìm kiếm theo mã đơn hàng
- ✅ Lọc theo trạng thái
- ✅ Cập nhật trạng thái đơn hàng
- ✅ Hủy đơn hàng
- ✅ Thống kê đơn hàng
- ✅ Tính tổng chi tiêu

### 👤 User Profile
- ✅ Tạo hồ sơ cá nhân
- ✅ Xem thông tin profile
- ✅ Cập nhật thông tin
- ✅ Tìm kiếm theo số điện thoại

### 📜 Login History
- ✅ Ghi nhận lịch sử đăng nhập
- ✅ Theo dõi IP address
- ✅ Ghi nhận thiết bị (mobile/desktop)
- ✅ Thống kê lần đăng nhập
- ✅ Phát hiện hoạt động đáng ngờ
- ✅ Cảnh báo bảo mật

### 📊 Dashboard
- ✅ Tổng quan thống kê
- ✅ Đơn hàng gần đây
- ✅ Quick actions
- ✅ Real-time data

## 📚 API Documentation

### User APIs (`/api/users`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/register` | Đăng ký user mới |
| POST | `/login` | Đăng nhập |
| GET | `/{userId}` | Lấy thông tin user |
| GET | `/email/{email}` | Tìm kiếm theo email |
| GET | `/status/{status}` | Lọc theo trạng thái |
| PUT | `/{userId}/status` | Cập nhật trạng thái |
| POST | `/{userId}/change-password` | Đổi mật khẩu |
| DELETE | `/{userId}` | Xóa user |

### Order APIs (`/api/orders`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Tạo đơn hàng |
| GET | `/user/{userId}` | Lấy đơn hàng của user |
| GET | `/user/{userId}/status/{status}` | Lọc theo trạng thái |
| GET | `/user/{userId}/recent` | Đơn hàng gần nhất |
| GET | `/code/{orderCode}` | Tìm theo mã đơn |
| GET | `/{orderId}` | Chi tiết đơn hàng |
| GET | `/user/{userId}/range` | Đơn hàng theo khoảng thời gian |
| PUT | `/{orderId}/status` | Cập nhật trạng thái |
| DELETE | `/{orderId}/cancel` | Hủy đơn hàng |
| GET | `/user/{userId}/statistics` | Thống kê đơn hàng |
| GET | `/user/{userId}/spent` | Tổng chi tiêu |

### Profile APIs (`/api/profiles`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/` | Tạo profile |
| GET | `/{userId}` | Lấy profile |
| PUT | `/{userId}` | Cập nhật profile |
| GET | `/phone/{phoneNumber}` | Tìm theo SĐT |

### Login History APIs (`/api/login-history`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/record` | Ghi nhận đăng nhập |
| POST | `/{historyId}/logout` | Ghi nhận đăng xuất |
| GET | `/user/{userId}` | Lịch sử đăng nhập |
| GET | `/user/{userId}/recent` | Đăng nhập gần nhất |
| GET | `/user/{userId}/today` | Số lần đăng nhập hôm nay |
| GET | `/user/{userId}/range` | Lịch sử theo thời gian |
| GET | `/user/{userId}/suspicious` | Kiểm tra hoạt động đáng ngờ |

## 🔒 Security Features

- **Password Encryption**: BCrypt với cost factor 10
- **Session Management**: LocalStorage based authentication
- **Protected Routes**: Client-side route protection
- **CORS Configuration**: Configured for localhost:3000
- **SQL Injection Prevention**: JPA/Hibernate parameterized queries
- **Suspicious Activity Detection**: Multi-IP login tracking

## 📱 Responsive Design

- Mobile-first approach với TailwindCSS
- Responsive tables và layouts
- Touch-friendly UI components
- Optimized cho các kích thước màn hình

## 🐛 Troubleshooting

### Backend không khởi động
- Kiểm tra cấu hình database trong `application.properties`
- Đảm bảo MySQL/PostgreSQL đang chạy
- Kiểm tra port 8080 có bị chiếm không

### Frontend không kết nối được backend
- Đảm bảo backend đang chạy tại `http://localhost:8080`
- Kiểm tra CORS configuration trong `AppConfig.java`
- Xem console của browser để check lỗi

### CORS Error
- Đảm bảo `AppConfig.java` đã có CORS configuration
- Restart backend sau khi thêm CORS config

## 📝 License

This project is for educational purposes.

## 👥 Contributors

- Backend: Spring Boot with JPA
- Frontend: React with TailwindCSS
- Database Design: Horizontal & Vertical Partitioning

## 📞 Support

Nếu gặp vấn đề, vui lòng tạo issue hoặc liên hệ qua email.
