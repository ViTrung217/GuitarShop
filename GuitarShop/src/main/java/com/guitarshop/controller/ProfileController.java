package com.guitarshop.controller;

import com.guitarshop.model.User;
import com.guitarshop.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        return "profile/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam String fullName,
                                Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        if (user != null) {
            user.setFullName(fullName);
            userService.registerUser(user);
            model.addAttribute("success", "Cập nhật thông tin thành công!");
        }
        model.addAttribute("user", user);
        return "redirect:/profile?success=true";
    }
}
