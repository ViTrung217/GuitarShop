# Guitar Shop - Functionality Checklist

## Build Status
- ✅ **BUILD SUCCESS** - 30 Java files compiled successfully

---

## Module 1: Authentication & User Management

### Register/Login Page
- [ ] **GET /register** - Display registration form
- [ ] **POST /register** - Create new user account
- [ ] **GET /login** - Display login form  
- [ ] **POST /login** - Authenticate user
- [ ] **GET /logout** - Logout current user
- **Expected**: Register form loads, login redirects to dashboard/homepage

### User Profile
- [ ] **GET /profile** - View user profile
- [ ] **POST /profile/update** - Update user information
- [ ] **Expected**: Show user info, order history visible

---

## Module 2: Shopping - Products

### Homepage/Product Listing
- [ ] **GET /** - Load homepage with 8 guitar products
- [ ] **GET /product/{id}** - View product details
- **Expected**: Products display with images, prices, descriptions

### Product Management (Admin Only)
- [ ] **GET /admin/products** - List all products
- [ ] **GET /admin/products/new** - Create product form
- [ ] **POST /admin/products/save** - Save new/edited product
- [ ] **GET /admin/products/edit/{id}** - Edit product form
- [ ] **GET /admin/products/delete/{id}** - Delete product
- **Expected**: CRUD operations work, image upload saves

---

## Module 3: Shopping Cart

### Add to Cart Feature
- [ ] **POST /cart/add/{id}** - Add product to cart (stays on current page)
- [ ] **GET /cart** - View shopping cart
- [ ] **GET /cart/remove/{id}** - Remove item from cart
- **Expected**: 
  - Adding product doesn't redirect away
  - Cart shows all items with total price
  - Correct calculation of totals
  - Empty cart shows proper message

### Buy Now Feature
- [ ] **POST /checkout** with `quickBuyId` - Quick purchase
- **Expected**: 
  - Requires login redirect if not authenticated
  - Adds product + processes payment immediately

---

## Module 4: Checkout & Orders

### Checkout Process
- [ ] **POST /checkout** - Create order from cart
- [ ] **Redirect to /cart?success=true** - Show success message
- [ ] **GET /cart** with success param - Display success alert
- **Expected**: 
  - Success message appears
  - Cart empties after checkout
  - Order created in database

### Admin Order Management
- [ ] **GET /admin/orders** - List all orders with:
  - [ ] Order ID displays correctly
  - [ ] Customer name shows
  - [ ] Total price calculated & formatted
  - [ ] Order status badge colors
  - [ ] Created date formats correctly
- [ ] **GET /admin/orders/{id}** - View order detail:
  - [ ] Customer info section
  - [ ] Order items with product names
  - [ ] Item prices & quantities
  - [ ] Total amount calculation
  - [ ] Status update form
- [ ] **POST /admin/orders/{id}/status** - Update order status
- **Expected**: No template parsing errors, all fields populate

---

## Module 5: Categories Management

### Admin Category CRUD
- [ ] **GET /admin/categories** - List categories
- [ ] **GET /admin/categories/new** - Create form
- [ ] **POST /admin/categories/save** - Save category
- [ ] **GET /admin/categories/edit/{id}** - Edit form
- [ ] **GET /admin/categories/delete/{id}** - Delete category
- **Expected**: CRUD operations work, proper error handling

---

## Module 6: Users Management (Admin)

### Admin User CRUD
- [ ] **GET /admin/users** - List all users
- [ ] **GET /admin/users/new** - Create user form
- [ ] **GET /admin/users/edit/{id}** - Edit user form
- [ ] **POST /admin/users/save** - Save user:
  - [ ] Password hashing (BCrypt)
  - [ ] Role assignment
  - [ ] Edit existing user preserves data
  - [ ] Create new user with roles
- [ ] **GET /admin/users/delete/{id}** - Delete user
- **Expected**: User roles properly set, duplicate code eliminated

---

## Module 7: Admin Dashboard

### Dashboard Stats
- [ ] **GET /admin/dashboard** - Display dashboard with:
  - [ ] Total products count
  - [ ] Total categories count
  - [ ] Total users count
  - [ ] Total orders count
- **Expected**: All counts calculate correctly

---

## Module 8: Data Persistence

### Database Schema
- [ ] **users table** - User accounts with roles
- [ ] **roles table** - ROLE_ADMIN, ROLE_USER
- [ ] **products table** - Product details + image paths
- [ ] **categories table** - Product categories
- [ ] **orders table** - Order header info
- [ ] **order_items table** - Order line items:
  - [ ] Has `product_id` foreign key
  - [ ] Has `order_id` foreign key
  - [ ] Stores quantity & price
- **Expected**: All tables exist with correct relationships

### Data Initialization
- [ ] **DataInitializer** runs on startup:
  - [ ] Creates admin & customer users
  - [ ] Creates 3 categories (Acoustic, Electric, Classical)
  - [ ] Creates 8 guitar products
  - [ ] Assigns proper roles
- **Expected**: Demo data loads automatically

---

## Module 9: Security & Authorization

### Route Protection
- [ ] **Public routes**: /, /login, /register, /product/*, /cart accessible without login
- [ ] **Authenticated routes**: /profile, /checkout require login
- [ ] **Admin routes**: /admin/** require ROLE_ADMIN
- [ ] **Session scope**: 
  - [ ] Different users have separate carts
  - [ ] Cart persists during session
  - [ ] Cart clears on logout
- **Expected**: Proper access control, no unauthorized access

---

## Module 10: Code Quality

### Cleanup Done
- ✅ Deleted unused DTOs: ProductDTO, LoginDTO
- ✅ Removed Kotlin dependencies (Java project only)
- ✅ Fixed test dependencies
- ✅ Removed unused imports
- ✅ Refactored duplicate code in AdminController
- ✅ CartService uses @SessionScope
- ✅ Order/OrderItem models have all getters/setters
- **Status**: Build is clean, no warnings

---

## Error Scenarios to Test

### Input Validation
- [ ] Register with empty fields
- [ ] Register with existing username
- [ ] Login with wrong password
- [ ] Add product with missing fields
- [ ] Edit without proper permissions

### Edge Cases
- [ ] Empty cart checkout
- [ ] Delete category with products
- [ ] Delete user with orders
- [ ] Negative quantities
- [ ] Null product when viewing order

### Session Management
- [ ] Cart persists across page navigations
- [ ] Cart clears after successful checkout
- [ ] Different browsers have separate carts
- [ ] Logout clears session

---

## Performance Checks

- [ ] Page load times < 2 seconds
- [ ] Product images load correctly
- [ ] Database queries optimized (no N+1)
- [ ] No memory leaks on repeated add/remove

---

## Browser Compatibility

- [ ] Chrome/Edge: ✅ Test
- [ ] Firefox: ✅ Test  
- [ ] Safari: ✅ Test
- [ ] Mobile responsive: ✅ Test

---

## Final Deployment Checklist

- [ ] All tests pass
- [ ] Build successful with no warnings
- [ ] No hardcoded passwords/configs
- [ ] Database backups created
- [ ] Error pages configured
- [ ] Logging enabled
- [ ] Documentation updated

---

## Testing Instructions

### To Run Application:
```bash
cd /Users/vitrung/Documents/Projects/guitarshop
./mvnw spring-boot:run
```

**Then test at:** http://localhost:8080

### Demo Accounts:
- **Admin**: username=`admin`, password=`admin123`
- **Customer**: username=`customer`, password=`customer123`

### Test Flow:
1. Register new account or login with demo
2. Browse products on homepage
3. Add 2-3 items to cart
4. View cart (verify totals)
5. Checkout (create order)
6. Login as admin
7. View dashboard (check stats)
8. View order details
9. Update order status
10. Manage products/categories/users

---

**Generated:** 21 Jan 2026
**Status:** Ready for QA Testing
