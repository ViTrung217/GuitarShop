package com.guitarshop.controller;

import com.guitarshop.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
    public String add(@PathVariable Long id,
                      @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                      @RequestHeader(value = "Referer", required = false) String referer) {
        cartService.add(productService.findById(id), quantity);
        // Quay lại trang hiện tại hoặc trang chủ
        String target = "/";
        if (referer != null && !referer.contains("/cart")) {
            target = referer.replaceAll(".*://.*?(/.*)", "$1");
        }
        // append added flag for notification
        if (target.contains("?")) {
            return "redirect:" + target + "&added=true";
        }
        return "redirect:" + target + "?added=true";
    }

    @GetMapping
    public String view(Model model) {
        model.addAttribute("cart", cartService.getCart());
        
        // Tính tổng tiền
        BigDecimal total = cartService.getCart().values().stream()
                .map(item -> item.getTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("total", total);
        
        return "shop/cart";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id) {
        cartService.remove(id);
        return "redirect:/cart";
    }
}
