# So Sánh Technical Approach vs Domain Approach

## 1. Giới Thiệu

Đây là bài thực hành so sánh 2 cách tổ chức code trong dự án:
- **Technical Approach (Layered Architecture)**: Tổ chức theo tầng kỹ thuật
- **Domain Approach (Domain-Driven Design)**: Tổ chức theo nghiệp vụ/domain

## 2. Cấu Trúc Package

### 2.1 Technical Approach
```
technical/
├── model/
│   └── Product.java
├── repository/
│   └── ProductRepository.java
└── service/
    └── ProductService.java
```

**Đặc điểm:**
- Phân chia theo **tầng kỹ thuật** (model, repository, service)
- Tất cả model nằm chung 1 package
- Tất cả repository nằm chung 1 package
- Tất cả service nằm chung 1 package

### 2.2 Domain Approach
```
domain/
└── product/
    ├── Product.java
    ├── ProductRepository.java
    └── ProductService.java
```

**Đặc điểm:**
- Phân chia theo **domain/nghiệp vụ** (product, order, customer,...)
- Mỗi domain chứa tất cả các tầng liên quan (model, repo, service)
- Code liên quan đến 1 nghiệp vụ được nhóm lại với nhau

## 3. So Sánh Chi Tiết

| Tiêu Chí | Technical Approach | Domain Approach |
|----------|-------------------|-----------------|
| **Tổ chức** | Theo tầng kỹ thuật | Theo nghiệp vụ |
| **Tìm kiếm code** | Dễ tìm theo loại class (Service, Repository) | Dễ tìm theo tính năng |
| **Độc lập module** | Khó tách module | Dễ tách module |
| **Dự án nhỏ** | Đơn giản, dễ hiểu | Có thể over-engineering |
| **Dự án lớn** | Khó bảo trì, nhiều class/package | Dễ quản lý, mở rộng |
| **Team work** | Dễ conflict (nhiều người sửa cùng package) | Ít conflict (mỗi team lo 1 domain) |
| **Thêm tính năng** | Phải sửa nhiều package | Tập trung vào 1 domain |

## 4. Ưu Điểm & Nhược Điểm

### 4.1 Technical Approach

**Ưu điểm:**
- ✅ Đơn giản, dễ hiểu với người mới
- ✅ Rõ ràng về vai trò kỹ thuật của từng class
- ✅ Phù hợp dự án nhỏ, ít nghiệp vụ
- ✅ Dễ apply design pattern theo tầng

**Nhược điểm:**
- ❌ Khó scale khi dự án lớn
- ❌ Khó tách module độc lập
- ❌ Nhiều class cùng package → khó tìm
- ❌ Thay đổi 1 tính năng → sửa nhiều package
- ❌ Dễ vi phạm nguyên tắc "high cohesion"

### 4.2 Domain Approach

**Ưu điểm:**
- ✅ Tập trung vào nghiệp vụ
- ✅ Dễ tách module/microservice
- ✅ Dễ quản lý khi dự án lớn
- ✅ Team work hiệu quả (mỗi team 1 domain)
- ✅ Thêm tính năng mới không ảnh hưởng domain khác
- ✅ High cohesion, low coupling

**Nhược điểm:**
- ❌ Phức tạp hơn với người mới
- ❌ Over-engineering với dự án nhỏ
- ❌ Cần hiểu rõ domain boundary
- ❌ Có thể duplicate code giữa các domain

## 5. Khi Nào Dùng?

### Technical Approach - Phù hợp:
- Dự án nhỏ, đơn giản
- Team nhỏ (1-3 người)
- CRUD đơn giản
- Prototype/MVP
- Ít nghiệp vụ phức tạp

### Domain Approach - Phù hợp:
- Dự án lớn, phức tạp
- Team lớn (nhiều team)
- Nhiều nghiệp vụ độc lập
- Cần tách microservice
- Dự án lâu dài, cần maintain

## 6. Code Ví Dụ

### Technical Approach
```java
// Tìm Product Service: technical/service/ProductService.java
ProductService service = new ProductService();
service.addProduct(new Product(1L, "Laptop", 1500.0));
```

### Domain Approach
```java
// Tìm Product Service: domain/product/ProductService.java
ProductService service = new ProductService();
service.addProduct(new Product(1L, "Laptop", 1500.0));
```

*Code giống nhau, chỉ khác cách tổ chức package!*

## 7. Kết Luận

- **Technical Approach**: Tốt cho dự án nhỏ, học tập
- **Domain Approach**: Tốt cho dự án lớn, production
- Không có approach nào tốt nhất, tùy vào ngữ cảnh dự án
- Có thể kết hợp cả 2 (hybrid approach)

Cả 2 approach đều pass test giống nhau vì logic giống nhau, chỉ khác cách tổ chức!

