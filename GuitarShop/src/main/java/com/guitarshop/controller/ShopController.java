package com.guitarshop.controller;

import com.guitarshop.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ShopController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ShopController(ProductService productService,
                          CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String shop(org.springframework.security.core.Authentication authentication, Model model) {
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("products", productService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "shop/index";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "shop/detail";
    }
}
