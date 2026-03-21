# Frontend Login & Register System

Frontend React JS để kết nối với 2 backend services: service-login và service-register.

## Cấu trúc dự án

```
frontend/
├── public/
│   └── index.html              # HTML template
├── src/
│   ├── components/
│   │   ├── Login.jsx          # Component đăng nhập
│   │   └── Register.jsx       # Component đăng ký
│   ├── services/
│   │   └── api.js             # API service để gọi backend
│   ├── App.jsx                # Main App component với routing
│   ├── App.css                # Styles
│   └── index.js               # Entry point
├── package.json               # Dependencies
└── vite.config.js            # Vite configuration
```

## Yêu cầu

- Node.js (v16 trở lên)
- npm hoặc yarn
- Backend services đang chạy:
  - service-login trên port 8080
  - service-register trên port 8081

## Cài đặt

1. Di chuyển vào thư mục frontend:
```bash
cd frontend
```

2. Cài đặt dependencies:
```bash
npm install
```

## Chạy ứng dụng

1. Đảm bảo 2 backend services đang chạy:
   - service-login: http://localhost:8080
   - service-register: http://localhost:8081

2. Khởi động frontend:
```bash
npm run dev
```

3. Mở trình duyệt và truy cập: http://localhost:3000

## Tính năng

### Đăng ký (Register)
- URL: `/register`
- Nhập username và password
- Xác nhận password
- Validate password (tối thiểu 6 ký tự)
- Gọi API: POST http://localhost:8081/register
- Sau khi đăng ký thành công, tự động chuyển đến trang đăng nhập

### Đăng nhập (Login)
- URL: `/login`
- Nhập username và password
- Gọi API: POST http://localhost:8080/auth/login
- Nhận token JWT và lưu vào localStorage
- Hiển thị thông báo đăng nhập thành công

## API Endpoints

### Login Service
- **Endpoint**: `POST http://localhost:8080/auth/login`
- **Request Body**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Response**:
  ```json
  {
    "token": "jwt_token_string"
  }
  ```

### Register Service
- **Endpoint**: `POST http://localhost:8081/register`
- **Request Body**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Response**: String message

## Cấu hình Backend Ports

Nếu backend services chạy trên các port khác, bạn có thể chỉnh sửa trong file `src/services/api.js`:

```javascript
const API_LOGIN_URL = 'http://localhost:8080/auth';
const API_REGISTER_URL = 'http://localhost:8081/register';
```

## Build cho Production

```bash
npm run build
```

Các file build sẽ được tạo trong thư mục `dist/`.

## Công nghệ sử dụng

- **React 18** - UI library
- **React Router DOM 6** - Client-side routing
- **Axios** - HTTP client
- **Vite** - Build tool và dev server
- **CSS3** - Styling với gradient và animations

## Ghi chú

- Token JWT được lưu trong localStorage sau khi đăng nhập thành công
- Có thể mở rộng thêm các tính năng như:
  - Dashboard sau khi đăng nhập
  - Protected routes
  - Token refresh
  - Logout functionality
