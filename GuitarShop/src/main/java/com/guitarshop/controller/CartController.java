package com.guitarshop.controller;

import com.guitarshop.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService,
                          ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @PostMapping("/add/{id}")
    public String add(@PathVariable Long id) {
        cartService.add(productService.findById(id));
        return "redirect:/cart";
    }

    @GetMapping
    public String view(Model model) {
        model.addAttribute("cart", cartService.getCart());
        return "shop/cart";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id) {
        cartService.remove(id);
        return "redirect:/cart";
    }
}
