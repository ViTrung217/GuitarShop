package com.guitarshop.controller;

import com.guitarshop.model.Category;
import com.guitarshop.model.Product;
import com.guitarshop.repository.OrderRepository;
import com.guitarshop.repository.RoleRepository;
import com.guitarshop.repository.UserRepository;
import com.guitarshop.service.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(ProductService productService,
                           CategoryService categoryService,
                           UserRepository userRepository,
                           OrderRepository orderRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("productCount", productService.findAll().size());
        model.addAttribute("categoryCount", categoryService.findAll().size());
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("orderCount", orderRepository.count());
        return "admin/dashboard";
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/products";
    }

    @GetMapping("/products/new")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("isEdit", false);
        return "admin/product-form";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/admin/products?error=notfound";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("isEdit", true);
        return "admin/product-form";
    }

    @PostMapping("/products/save")
    public String save(@ModelAttribute Product product,
                       @RequestParam(value = "categoryId", required = false) Long categoryId,
                       @RequestParam(value = "image", required = false) MultipartFile imageFile,
                       @RequestParam(value = "imageUrl", required = false) String imageUrl) {
        Product existing = null;
        if (product.getId() != null) {
            existing = productService.findById(product.getId());
            if (existing == null) {
                return "redirect:/admin/products?error=notfound";
            }
            if (product.getImagePath() == null || product.getImagePath().isEmpty()) {
                product.setImagePath(existing.getImagePath());
            }
        }
        if (categoryId != null) {
            Category category = categoryService.findById(categoryId);
            product.setCategory(category);
        } else if (existing != null) {
            product.setCategory(existing.getCategory());
        }

        // Handle image from upload file or URL, then always store in data/guitar
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                product.setImagePath(storeUploadedImage(imageFile));
            } else if (imageUrl != null && !imageUrl.isBlank()) {
                product.setImagePath(downloadImageFromUrl(imageUrl));
            }
        } catch (Exception e) {
            return "redirect:/admin/products?error=image";
        }

        productService.save(product);
        return "redirect:/admin/products";
    }

    private String storeUploadedImage(MultipartFile imageFile) throws Exception {
        Path uploadDir = ensureUploadDir();
        String extension = getExtension(imageFile.getOriginalFilename());
        String filename = System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;

        Path filePath = uploadDir.resolve(filename);
        imageFile.transferTo(filePath.toFile());

        return "/data/guitar/" + filename;
    }

    private String downloadImageFromUrl(String imageUrl) throws Exception {
        Path uploadDir = ensureUploadDir();
        URI uri = URI.create(imageUrl.trim());

        URLConnection connection = uri.toURL().openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);

        String extension = getExtension(uri.getPath());
        if ("".equals(extension)) {
            String contentType = connection.getContentType();
            extension = extensionFromContentType(contentType);
        }

        String filename = System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;
        Path filePath = uploadDir.resolve(filename);

        try (InputStream in = connection.getInputStream()) {
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return "/data/guitar/" + filename;
    }

    private Path ensureUploadDir() throws Exception {
        Path uploadDir = Paths.get("data/guitar");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        return uploadDir;
    }

    private String getExtension(String fileName) {
        if (fileName == null) return "";
        int dot = fileName.lastIndexOf('.');
        if (dot < 0 || dot == fileName.length() - 1) return "";
        String ext = fileName.substring(dot).toLowerCase();
        if (ext.length() > 6) return "";
        return ext;
    }

    private String extensionFromContentType(String contentType) {
        if (contentType == null) return ".jpg";
        String ct = contentType.toLowerCase();
        if (ct.contains("png")) return ".png";
        if (ct.contains("webp")) return ".webp";
        if (ct.contains("gif")) return ".gif";
        if (ct.contains("bmp")) return ".bmp";
        if (ct.contains("jpeg") || ct.contains("jpg")) return ".jpg";
        return ".jpg";
    }

    @GetMapping("/products/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }

    // ===== CATEGORIES =====
    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin/categories";
    }

    @GetMapping("/categories/new")
    public String createCategory(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("isEdit", false);
        return "admin/category-form";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.findById(id));
        model.addAttribute("isEdit", true);
        return "admin/category-form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@ModelAttribute Category category) {
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        try {
            categoryService.delete(id);
        } catch (Exception e) {
            // Category has products, cannot delete
        }
        return "redirect:/admin/categories";
    }

    // ===== USERS =====
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @GetMapping("/users/new")
    public String createUser(Model model) {
        model.addAttribute("user", new com.guitarshop.model.User());
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/user-form";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        model.addAttribute("user", userRepository.findById(id).orElse(null));
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/user-form";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute com.guitarshop.model.User user,
                           @RequestParam(value = "password", required = false) String password,
                           @RequestParam(value = "roleIds", required = false) Long[] roleIds,
                           Model model) {
        com.guitarshop.model.User existing = null;
        if (user.getId() != null) {
            existing = userRepository.findById(user.getId()).orElse(null);
        }

        if (existing == null) {
            if (user.getUsername() == null || user.getUsername().isBlank()) {
                model.addAttribute("error", "Tên đăng nhập không được để trống.");
                model.addAttribute("user", user);
                model.addAttribute("roles", roleRepository.findAll());
                return "admin/user-form";
            }
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                model.addAttribute("error", "Tên đăng nhập đã tồn tại.");
                model.addAttribute("user", user);
                model.addAttribute("roles", roleRepository.findAll());
                return "admin/user-form";
            }
            if (password == null || password.isBlank()) {
                model.addAttribute("error", "Mật khẩu không được để trống.");
                model.addAttribute("user", user);
                model.addAttribute("roles", roleRepository.findAll());
                return "admin/user-form";
            }
        }

        // Build roles if provided
        Set<com.guitarshop.model.Role> roles = null;
        if (roleIds != null && roleIds.length > 0) {
            roles = new HashSet<>();
            for (Long roleId : roleIds) {
                com.guitarshop.model.Role role = roleRepository.findById(roleId).orElse(null);
                if (role != null) {
                    roles.add(role);
                }
            }
        }

        if (existing != null) {
            // Edit mode
            existing.setFullName(user.getFullName());
            existing.setEmail(user.getEmail());
            existing.setPhone(user.getPhone());
            existing.setAddress(user.getAddress());
            if (password != null && !password.isEmpty()) {
                existing.setPassword(passwordEncoder.encode(password));
            }
            if (roles != null) {
                existing.setRoles(roles);
            }
            userRepository.save(existing);
        } else {
            // Create mode
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(user.getEmail());
            user.setPhone(user.getPhone());
            user.setAddress(user.getAddress());
            if (roles != null) {
                user.setRoles(roles);
            } else {
                com.guitarshop.model.Role userRole = roleRepository.findByName("ROLE_USER").orElse(null);
                if (userRole != null) {
                    user.setRoles(Set.of(userRole));
                }
            }
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            // Cannot delete user with orders
        }
        return "redirect:/admin/users";
    }

    // ===== ORDERS =====
    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "admin/orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderRepository.findById(id).orElse(null));
        return "admin/order-detail";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        com.guitarshop.model.Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
        return "redirect:/admin/orders/" + id;
    }}