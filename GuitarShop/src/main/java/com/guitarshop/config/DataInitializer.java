package com.guitarshop.config;

import com.guitarshop.model.Category;
import com.guitarshop.model.Product;
import com.guitarshop.model.User;
import com.guitarshop.model.Role;
import com.guitarshop.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepo,
                           CategoryRepository catRepo,
                           ProductRepository prodRepo,
                           RoleRepository roleRepo,
                           PasswordEncoder encoder) {
        return args -> {
            // Khởi tạo roles
            Role adminRole, userRole;
            if (roleRepo.count() == 0) {
                Role admin = new Role();
                admin.setName("ROLE_ADMIN");
                adminRole = roleRepo.save(admin);
                
                Role user = new Role();
                user.setName("ROLE_USER");
                userRole = roleRepo.save(user);
            } else {
                adminRole = roleRepo.findByName("ROLE_ADMIN").orElse(null);
                userRole = roleRepo.findByName("ROLE_USER").orElse(null);
            }
            
            // Khởi tạo users
            if (userRepo.count() == 0) {
                // Tạo admin user
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setFullName("Administrator");
                admin.setEnabled(true);
                admin.setRoles(Set.of(adminRole));
                userRepo.save(admin);
                
                // Tạo customer user
                User customer = new User();
                customer.setUsername("customer");
                customer.setPassword(encoder.encode("customer123"));
                customer.setFullName("Customer Test");
                customer.setEnabled(true);
                customer.setRoles(Set.of(userRole));
                userRepo.save(customer);
            }
            
            // Khởi tạo categories
            if (catRepo.count() == 0) {
                Category acoustic = new Category();
                acoustic.setName("Acoustic");
                catRepo.save(acoustic);
                
                Category electric = new Category();
                electric.setName("Electric");
                catRepo.save(electric);
                
                Category classical = new Category();
                classical.setName("Classical");
                catRepo.save(classical);
            }
            
            // Khởi tạo products
            if (prodRepo.count() == 0) {
                Category acoustic = catRepo.findAll().stream().filter(c -> c.getName().equals("Acoustic")).findFirst().orElse(null);
                Category electric = catRepo.findAll().stream().filter(c -> c.getName().equals("Electric")).findFirst().orElse(null);
                Category classical = catRepo.findAll().stream().filter(c -> c.getName().equals("Classical")).findFirst().orElse(null);
                
                // Product 1
                Product p1 = new Product();
                p1.setName("CEF-39 Acoustic");
                p1.setBrand("CEF");
                p1.setPrice(new BigDecimal("2500000"));
                p1.setStock(10);
                p1.setDescription("Đàn guitar acoustic cao cấp từ CEF");
                p1.setCategory(acoustic);
                p1.setImagePath("/data/guitar/cef-39.jpg");
                prodRepo.save(p1);
                
                // Product 2
                Product p2 = new Product();
                p2.setName("Clover 912CX");
                p2.setBrand("Clover");
                p2.setPrice(new BigDecimal("3200000"));
                p2.setStock(8);
                p2.setDescription("Guitar electric chất lượng cao");
                p2.setCategory(electric);
                p2.setImagePath("/data/guitar/clover-912cx.jpg");
                prodRepo.save(p2);
                
                // Product 3
                Product p3 = new Product();
                p3.setName("Daisy-2 Classical");
                p3.setBrand("Daisy");
                p3.setPrice(new BigDecimal("1800000"));
                p3.setStock(12);
                p3.setDescription("Đàn guitar classic dành cho người học");
                p3.setCategory(classical);
                p3.setImagePath("/data/guitar/daisy-2.jpg");
                prodRepo.save(p3);
                
                // Product 4
                Product p4 = new Product();
                p4.setName("FDV6-1");
                p4.setBrand("FDV");
                p4.setPrice(new BigDecimal("2800000"));
                p4.setStock(5);
                p4.setDescription("Guitar folk cao cấp");
                p4.setCategory(acoustic);
                p4.setImagePath("/data/guitar/fdv6-1.jpg");
                prodRepo.save(p4);
                
                // Product 5
                Product p5 = new Product();
                p5.setName("HD34TUT-2");
                p5.setBrand("HD");
                p5.setPrice(new BigDecimal("4500000"));
                p5.setStock(3);
                p5.setDescription("Guitar electric cao cấp chuyên nghiệp");
                p5.setCategory(electric);
                p5.setImagePath("/data/guitar/hd34tut-2.jpg");
                prodRepo.save(p5);
                
                // Product 6
                Product p6 = new Product();
                p6.setName("Koa1V Premium");
                p6.setBrand("Koa");
                p6.setPrice(new BigDecimal("6000000"));
                p6.setStock(2);
                p6.setDescription("Guitar acoustic premium với gỗ Koa");
                p6.setCategory(acoustic);
                p6.setImagePath("/data/guitar/koa1v.jpg");
                prodRepo.save(p6);
                
                // Product 7
                Product p7 = new Product();
                p7.setName("Long Mang-6");
                p7.setBrand("Long Mang");
                p7.setPrice(new BigDecimal("1500000"));
                p7.setStock(15);
                p7.setDescription("Guitar giá rẻ cho người mới bắt đầu");
                p7.setCategory(classical);
                p7.setImagePath("/data/guitar/long-mang-6.jpg");
                prodRepo.save(p7);
                
                // Product 8
                Product p8 = new Product();
                p8.setName("VG-CV8 Modern");
                p8.setBrand("VG");
                p8.setPrice(new BigDecimal("3800000"));
                p8.setStock(7);
                p8.setDescription("Guitar electric với thiết kế hiện đại");
                p8.setCategory(electric);
                p8.setImagePath("/data/guitar/vg-cv8.jpg");
                prodRepo.save(p8);
            }
        };
    }
}
