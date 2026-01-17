package com.guitarshop.config;

import com.guitarshop.service.CustomUserDetailsService;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationSuccessHandler successHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,
                                    CustomUserDetailsService userDetailsService)
            throws Exception {

        http
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/products/**",
                                "/login", "/register",
                                "/css/**", "/js/**", "/images/**", "/uploads/**"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
