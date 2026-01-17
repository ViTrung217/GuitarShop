package com.guitarshop.controller;

import com.guitarshop.model.Product;
import com.guitarshop.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminController(ProductService productService,
                           CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
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
        return "admin/product-form";
    }

    @PostMapping("/products/save")
    public String save(@ModelAttribute Product product,
                       @RequestParam("image") MultipartFile image) throws Exception {
        productService.save(product, image);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }
}
