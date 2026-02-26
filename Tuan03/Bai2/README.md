# Bài 2 - 4 Tính Năng Phi Chức Năng

## Tổng quan

| # | Tính năng | Class | Mô tả |
|---|-----------|-------|-------|
| 1 | **Availability** | `Availability.java` | Kiểm tra service có sẵn sàng không, nếu DOWN thì từ chối request |
| 2 | **Fault Tolerance** | `FaultTolerance.java` | Tự động retry khi gặp lỗi, trả về fallback nếu hết retry |
| 3 | **Performance Cache** | `PerformanceCache.java` | Cache LRU lưu dữ liệu vào bộ nhớ, giảm tải truy vấn |
| 4 | **Scalability** | `Scalability.java` | Dùng thread pool xử lý song song nhiều request cùng lúc |

---

## 1. Availability (Tính sẵn sàng)

- Khi service **UP** → xử lý request bình thường, trả về kết quả
- Khi service **DOWN** → ném exception, từ chối request
- Có thể bật/tắt trạng thái để mô phỏng sự cố và recovery

## 2. Fault Tolerance (Khả năng chịu lỗi)

- Khi gọi operation bị lỗi → **tự động retry** (mặc định 3 lần)
- Nếu tất cả retry đều fail → trả về **fallback data** thay vì crash
- Nếu thành công → trả về kết quả ngay

## 3. Performance Cache (Bộ nhớ đệm)

- Lưu dữ liệu vào RAM với cơ chế **LRU** (Least Recently Used)
- Khi cache đầy → tự động xóa phần tử **ít được dùng nhất**
- `get()` trả về `null` nếu không có trong cache (cache miss)

## 4. Scalability (Khả năng mở rộng)

- Tạo **thread pool** với N workers
- Gửi danh sách requests → xử lý **song song** trên nhiều thread
- Trả về danh sách kết quả khi tất cả hoàn thành

---
