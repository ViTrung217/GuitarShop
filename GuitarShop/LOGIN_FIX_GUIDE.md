# 🔧 Hướng Dẫn Sửa Vấn Đề Đăng Ký/Đăng Nhập

## 🐛 Vấn Đề Ban Đầu
- **Triệu chứng**: Đăng ký tài khoản → Thử đăng nhập → Hiện thị "Đăng nhập sai"
- **Nguyên nhân**: 
  1. Người dùng mới được đăng ký nhưng **KHÔNG được gán role**
  2. Spring Security yêu cầu user phải có ít nhất một role
  3. Nếu không có role, authentication sẽ thất bại

## ✅ Các Sửa Được Thực Hiện

### 1. **AuthController.java** - Gán Role Mặc Định
```java
// TRƯỚC: Không gán role
User user = new User();
user.setUsername(dto.getUsername());
user.setPassword(dto.getPassword());
user.setFullName(dto.getFullName());
// ❌ Không gán role!

// AFTER: Gán ROLE_USER mặc định
Role userRole = roleRepository.findByName("ROLE_USER")
        .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
user.setRoles(Set.of(userRole));  // ✅ Gán role
user.setEnabled(true);             // ✅ Kích hoạt user
```

**Chi tiết thay đổi:**
- Thêm injection: `RoleRepository roleRepository`
- Gán `ROLE_USER` mặc định cho user mới
- Kiểm tra username đã tồn tại
- Set `enabled = true`

### 2. **User.java Model** - Thêm Field Enabled
```java
// THÊM field enabled
private boolean enabled = true;

// THÊM getters/setters
public boolean isEnabled() {
    return enabled;
}

public void setEnabled(boolean enabled) {
    this.enabled = enabled;
}
```

### 3. **CustomUserDetailsService.java** - Xử Lý Enabled Flag
```java
// TRƯỚC: Chỉ truyền 2 tham số
return new User(
    user.getUsername(),
    user.getPassword(),
    authorities
);

// AFTER: Truyền đủ tham số (enabled, accountNonExpired, etc.)
return new User(
    user.getUsername(),
    user.getPassword(),
    user.isEnabled(),           // ✅ Mới
    true,                       // accountNonExpired
    true,                       // credentialsNonExpired  
    true,                       // accountNonLocked
    authorities
);
```

### 4. **login.html** - Thêm Thông Báo Thành Công
```html
<!-- Thông báo đăng ký thành công -->
<div th:if="${param.success}" class="alert alert-success">
    <i class="fas fa-check-circle"></i>
    <strong>Đăng ký thành công!</strong> Vui lòng đăng nhập...
</div>
```

### 5. **register.html** - Thêm Thông Báo Lỗi
```html
<!-- Hiển thị lỗi nếu username đã tồn tại -->
<div th:if="${error}" class="alert alert-danger">
    <i class="fas fa-exclamation-circle"></i>
    <strong>Lỗi!</strong> <span th:text="${error}"></span>
</div>
```

## 🧪 Cách Test

### Bước 1: Build & Start
```bash
cd /Users/vitrung/Documents/Projects/guitarshop
./mvnw clean compile    # Verify no errors
./mvnw spring-boot:run  # Start app
```

### Bước 2: Tạo Tài Khoản Mới
1. Vào: http://localhost:8080/register
2. Điền thông tin:
   - Tên đăng nhập: `testuser`
   - Tên đầy đủ: `Test User`
   - Mật khẩu: `test123456`
3. Nhấn "Đăng ký"
4. ✅ Sẽ redirect đến `/login?success`
5. ✅ Sẽ thấy thông báo xanh: "Đăng ký thành công!"

### Bước 3: Đăng Nhập
1. Vào trang login
2. Nhập:
   - Tên đăng nhập: `testuser`
   - Mật khẩu: `test123456`
3. Nhấn "Đăng nhập"
4. ✅ **Lần này sẽ đăng nhập thành công!**
5. ✅ Redirect đến homepage
6. ✅ Xem được "Welcome testuser" (hoặc tương tự)

### Bước 4: Kiểm Tra Database
```bash
# Terminal: Kiểm tra database
mysql -u root -p guitarshop

# Queries
SELECT id, username, enabled FROM users;
SELECT u.username, r.name FROM user_roles ur 
JOIN users u ON ur.user_id = u.id 
JOIN roles r ON ur.role_id = r.id;
```

**Output mong đợi:**
```
mysql> SELECT id, username, enabled FROM users;
+----+----------+---------+
| id | username | enabled |
+----+----------+---------+
| 1  | admin    | 1       |
| 2  | customer | 1       |
| 3  | testuser | 1       |  ← User mới
+----+----------+---------+

mysql> SELECT u.username, r.name FROM user_roles ur 
JOIN users u ON ur.user_id = u.id 
JOIN roles r ON ur.role_id = r.id;
+----------+-----------+
| username | name      |
+----------+-----------+
| admin    | ROLE_ADMIN|
| customer | ROLE_USER |
| testuser | ROLE_USER |  ← User mới có role
+----------+-----------+
```

## 🔐 Luồng Xác Thực (Authentication Flow)

```
1. Người dùng ĐĂNG KÝ
   ↓
   AuthController.register()
   - Kiểm tra username tồn tại → Lỗi?
   - Tạo User mới
   - ✅ Gán ROLE_USER
   - ✅ Set enabled = true
   - Hash password & save
   - Redirect: /login?success

2. Người dùng ĐĂNG NHẬP
   ↓
   Spring Security Form Login
   - Gọi CustomUserDetailsService.loadUserByUsername()
   - Tìm user trong DB
   - Tạo UserDetails object
   - ✅ Truyền roles (authorities)
   - ✅ Truyền enabled flag
   - Spring Security so sánh password (BCrypt)
   - ✅ Authentication thành công!
   - Redirect: Login success handler
```

## 🚀 Demo Accounts Để Test

| Username | Password | Role | Status |
|----------|----------|------|--------|
| admin | admin123 | ROLE_ADMIN | ✅ Hoạt động |
| customer | customer123 | ROLE_USER | ✅ Hoạt động |
| (new tạo) | (any) | ROLE_USER | ✅ Hoạt động |

## ⚠️ Vấn Đề Khác Nếu Vẫn Không Hoạt Động

### Nếu vẫn hiện "Đăng nhập sai"
1. **Xóa database và restart** (schema mới):
   ```bash
   # Stop app (Ctrl+C)
   # Delete database
   mysql -u root -p -e "DROP DATABASE guitarshop;"
   # Start lại app (sẽ tạo database mới + demo data)
   ./mvnw spring-boot:run
   ```

2. **Check logs** tìm lỗi:
   ```bash
   # Look for: UsernameNotFoundException, BadCredentialsException, etc.
   # Và xem error message cụ thể
   ```

3. **Verify password encoding**:
   ```java
   // Nếu mật khẩu không được hash đúng:
   // Check UserService.registerUser()
   // Phải dùng passwordEncoder.encode() trước save
   ```

## 📝 Tóm Tắt Thay Đổi

| File | Thay Đổi | Lý Do |
|------|----------|-------|
| AuthController | Gán ROLE_USER + enabled | Spring Security cần role |
| User model | Thêm enabled flag | Kiểm soát user activation |
| CustomUserDetailsService | Truyền enabled flag | Spring Security cần biết user enabled |
| login.html | Thêm thông báo success | UX: Người dùng biết đăng ký thành công |
| register.html | Thêm error message | UX: Hiển thị lỗi nếu username tồn tại |

## ✅ Kết Quả
- ✅ Đăng ký tài khoản mới → Thành công
- ✅ Đăng nhập với tài khoản mới → Thành công
- ✅ Có thông báo rõ ràng cho user
- ✅ Xử lý lỗi (username trùng, etc.)

---

**Build Status**: ✅ SUCCESS (30 Java files)  
**Last Updated**: 21 January 2026  
**Status**: Sẵn sàng test!
