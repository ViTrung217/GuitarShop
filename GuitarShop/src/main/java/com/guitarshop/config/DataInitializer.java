package com.guitarshop.config;

import com.guitarshop.model.*;
import com.guitarshop.model.Role;
import com.guitarshop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepo,
                           PasswordEncoder encoder) {
        return args -> {
            if (!userRepo.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setFullName("Administrator");
                admin.setRoles(Set.of(Role.ROLE_ADMIN));
                userRepo.save(admin);
            }
        };
    }
}
