package com.guitarshop.controller;

import com.guitarshop.dto.RegisterDTO;
import com.guitarshop.model.User;
import com.guitarshop.model.Role;
import com.guitarshop.repository.RoleRepository;
import com.guitarshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@Controller
public class AuthController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public AuthController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("dto", new RegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterDTO dto, Model model) {
        // Check if username already exists
        if (userService.findByUsername(dto.getUsername()) != null) {
            model.addAttribute("error", "Username đã tồn tại!");
            model.addAttribute("dto", dto);
            return "auth/register";
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setFullName(dto.getFullName());
        user.setEnabled(true);
        
        // Assign default USER role
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
        user.setRoles(Set.of(userRole));

        userService.registerUser(user);
        return "redirect:/login?success";
    }


}
