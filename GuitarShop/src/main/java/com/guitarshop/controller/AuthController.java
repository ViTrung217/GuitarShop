package com.guitarshop.controller;

import com.guitarshop.dto.RegisterDTO;
import com.guitarshop.model.User;
import com.guitarshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
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
    public String register(@ModelAttribute RegisterDTO dto) {

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setFullName(dto.getFullName());

        userService.registerUser(user);
        return "redirect:/login";
    }


}
