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
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public ProfileController(UserService userService, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) String address,
                                Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        if (user != null) {
            user.setFullName(fullName);
            user.setEmail((email != null && !email.isBlank()) ? email : null);
            user.setPhone((phone != null && !phone.isBlank()) ? phone : null);
            user.setAddress((address != null && !address.isBlank()) ? address : null);
            userService.updateProfile(user);
            model.addAttribute("success", "Cập nhật thông tin thành công!");
        }
        model.addAttribute("user", user);
        return "redirect:/profile?success=true";
    }

    @GetMapping("/change-password")
    public String changePasswordForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) return "redirect:/login";
        model.addAttribute("username", userDetails.getUsername());
        return "profile/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 Model model) {
        if (userDetails == null) return "redirect:/login";
        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) return "redirect:/profile?error=notfound";

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return "redirect:/profile/change-password?error=badcurrent";
        }

        userService.changePassword(user, newPassword);
        return "redirect:/profile?pwchanged=true";
    }
}
