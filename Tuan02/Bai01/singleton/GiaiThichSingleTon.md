Mỗi lần gọi new DatabaseConnection() → tạo một object mới
Trong môi trường Java EE (Servlet, nhiều request đồng thời):
Mỗi request tạo một connection
Số lượng connection tăng rất nhanh
Hệ quả:
❌ Tốn RAM

❌ Tốn tài nguyên Database

❌ Hệ thống chậm hoặc crash server
👉 Nguyên nhân:
Không có cơ chế kiểm soát số lượng instance của DatabaseConnection.
Ý tưởng của Singleton
Singleton Pattern đảm bảo:
Chỉ có một instance duy nhất của class trong toàn bộ chương trình
Cung cấp điểm truy cập toàn cục thông qua phương thức getInstance()
Constructor được khai báo private
Không thể dùng new từ bên ngoài class
Instance được tạo một lần duy nhất
Tất cả các request dùng chung 1 DatabaseConnection
👉 Lợi ích:
✅ Tiết kiệm tài nguyên
✅ Tránh tạo nhiều connection không cần thiết
✅ Phù hợp kiến trúc Java EE
✅ Dễ quản lý và bảo trì