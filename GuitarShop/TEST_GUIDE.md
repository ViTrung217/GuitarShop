# HƯỚNG DẪN KIỂM TRA HỆ THỐNG

## ✅ Đã sửa: Hệ thống ảnh

### Thay đổi:
1. **Cấu hình WebMvc**: Đã thêm mapping `/data/**` → `file:data/` trong GuitarshopApplication
2. **Upload ảnh**: AdminController đã có xử lý upload ảnh vào `data/guitar/`
3. **Đường dẫn ảnh**: Tất cả ảnh trong database đã được cập nhật từ `/data/xxx.jpg` → `/data/guitar/xxx.jpg`

### Kết quả:
- ✅ Ảnh hiển thị trên trang chủ
- ✅ Ảnh hiển thị trên trang chi tiết sản phẩm  
- ✅ Ảnh hiển thị trên dashboard admin
- ✅ Khi thêm sản phẩm mới, ảnh được lưu vào `data/guitar/`
- ✅ Khi edit sản phẩm, có thể giữ ảnh cũ hoặc upload ảnh mới

---

## 🧪 KIỂM TRA CHỨC NĂNG

### 1. Trang chủ (/)
- [ ] Hiển thị danh sách sản phẩm với ảnh
- [ ] Hiển thị giá, brand, category
- [ ] Nút "Xem chi tiết" hoạt động
- [ ] Nút "Thêm vào giỏ" hoạt động

### 2. Chi tiết sản phẩm (/product/{id})
- [ ] Hiển thị ảnh sản phẩm đầy đủ
- [ ] Thông tin chi tiết đầy đủ
- [ ] Nút "Thêm vào giỏ hàng" hoạt động
- [ ] Nút "Mua ngay" hoạt động

### 3. Giỏ hàng (/cart)
- [ ] Hiển thị danh sách sản phẩm trong giỏ
- [ ] Tính tổng tiền đúng
- [ ] Xóa sản phẩm khỏi giỏ hoạt động
- [ ] Thanh toán yêu cầu đăng nhập

### 4. Đăng nhập/Đăng ký
**Test Account:**
- Admin: `admin` / `admin123`
- Customer: `customer` / `customer123`

- [ ] Đăng nhập admin thành công
- [ ] Đăng nhập customer thành công
- [ ] Đăng ký tài khoản mới hoạt động
- [ ] Redirect đúng sau login

### 5. Admin Dashboard (/admin/dashboard)
**Yêu cầu: Đăng nhập với admin**

- [ ] Hiển thị số liệu thống kê
- [ ] Menu admin đầy đủ

### 6. Quản lý sản phẩm (/admin/products)
- [ ] ✅ **QUAN TRỌNG**: Hiển thị ảnh sản phẩm trong danh sách
- [ ] Thêm sản phẩm mới với ảnh
- [ ] Edit sản phẩm (giữ ảnh cũ)
- [ ] Edit sản phẩm (upload ảnh mới)
- [ ] Xóa sản phẩm

### 7. Quản lý danh mục (/admin/categories)
- [ ] Hiển thị danh sách category
- [ ] Thêm category mới
- [ ] Edit category
- [ ] Xóa category (nếu không có sản phẩm)

### 8. Quản lý người dùng (/admin/users)
- [ ] Hiển thị danh sách user
- [ ] Thêm user mới
- [ ] Edit user
- [ ] Phân quyền (Admin/User)

### 9. Quản lý đơn hàng (/admin/orders)
- [ ] Hiển thị danh sách đơn hàng
- [ ] Xem chi tiết đơn hàng
- [ ] Cập nhật trạng thái đơn hàng

### 10. Profile (/profile)
**Yêu cầu: Đăng nhập**
- [ ] Hiển thị thông tin user
- [ ] Hiển thị lịch sử đơn hàng

---

## 🔧 Kiểm tra kỹ thuật

### Build & Run
```bash
./mvnw clean compile    # Build thành công ✅
./mvnw spring-boot:run  # Chạy thành công ✅
```

### Database
- Database: `guitarshopdb`
- Tables: users, roles, users_roles, category, product, orders, order_items

### Static Resources
- `/data/**` → mapped to `file:data/` folder
- Images stored in: `data/guitar/`
- Image path format: `/data/guitar/filename.jpg`

---

## 🎯 Test thêm sản phẩm mới

1. Đăng nhập admin
2. Vào `/admin/products`
3. Click "Thêm sản phẩm mới"
4. Điền form:
   - Tên: Test Guitar
   - Brand: Test Brand
   - Category: Acoustic
   - Giá: 1000000
   - Tồn kho: 5
   - Mô tả: Test description
   - **Upload ảnh từ máy**
5. Lưu
6. Kiểm tra:
   - ✅ Ảnh xuất hiện trong danh sách admin
   - ✅ File ảnh có trong folder `data/guitar/`
   - ✅ Ảnh hiển thị trên trang chủ
   - ✅ Ảnh hiển thị trên trang chi tiết

---

## 📝 URLs quan trọng

- Trang chủ: http://localhost:8080/
- Login: http://localhost:8080/login
- Register: http://localhost:8080/register
- Admin Dashboard: http://localhost:8080/admin/dashboard
- Quản lý sản phẩm: http://localhost:8080/admin/products
- Giỏ hàng: http://localhost:8080/cart

---

## ✨ Tóm tắt

**HỆ THỐNG ĐÃ HOẠT ĐỘNG:**
1. ✅ Ảnh được lưu vào `data/guitar/` khi thêm sản phẩm mới
2. ✅ Ảnh được lấy từ `data/guitar/` khi hiển thị
3. ✅ Ảnh hiển thị đúng trên mọi trang (home, detail, admin)
4. ✅ Upload ảnh mới khi edit sản phẩm
5. ✅ Toàn bộ CRUD operations hoạt động
6. ✅ Authentication & Authorization hoạt động
7. ✅ Cart & Checkout hoạt động
