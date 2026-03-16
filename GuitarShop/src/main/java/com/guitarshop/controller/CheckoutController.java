package com.guitarshop.controller;

import com.guitarshop.model.User;
import com.guitarshop.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CheckoutController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;

    public CheckoutController(OrderService orderService,
                              CartService cartService,
                              UserService userService,
                              ProductService productService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/checkout")
    public String checkoutForm(@AuthenticationPrincipal UserDetails userDetails,
                               @RequestParam(required = false) Long quickBuyId,
                               @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                               org.springframework.ui.Model model) {
        if (userDetails == null) {
            return "redirect:/login?redirect=/checkout" + (quickBuyId != null ? ("?quickBuyId=" + quickBuyId) : "");
        }

        java.util.Map<Long, CartItem> cartView;
        if (quickBuyId != null) {
            cartView = new java.util.HashMap<>();
            com.guitarshop.model.Product product = productService.findById(quickBuyId);
            if (product != null) {
                CartItem item = new CartItem();
                item.productId = product.getId();
                item.name = product.getName();
                item.price = product.getPrice();
                item.image = product.getImagePath();
                item.quantity = Math.max(1, quantity);
                cartView.put(product.getId(), item);
            }
            model.addAttribute("quickBuyId", quickBuyId);
            model.addAttribute("quickBuyQty", Math.max(1, quantity));
        } else {
            cartView = cartService.getCart();
        }

        model.addAttribute("cart", cartView);
        java.math.BigDecimal total = cartView.values().stream()
                .map(item -> item.getTotal())
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        model.addAttribute("total", total);
        User user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        return "shop/checkout";
    }

    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam(required = false) Long quickBuyId,
                           @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                           @RequestParam(required = false) String shippingAddress,
                           @RequestParam(required = false) String email,
                           @RequestParam(required = false) String phone,
                           @RequestParam(required = false) String paymentMethod) {
        if (userDetails == null) {
            return "redirect:/login?redirect=/cart";
        }

        User user = userService.findByUsername(userDetails.getUsername());

        java.util.Map<Long, CartItem> cartToCheckout;
        boolean isQuickBuy = quickBuyId != null;
        if (isQuickBuy) {
            cartToCheckout = new java.util.HashMap<>();
            com.guitarshop.model.Product product = productService.findById(quickBuyId);
            if (product != null) {
                CartItem item = new CartItem();
                item.productId = product.getId();
                item.name = product.getName();
                item.price = product.getPrice();
                item.image = product.getImagePath();
                item.quantity = Math.max(1, quantity);
                cartToCheckout.put(product.getId(), item);
            }
        } else {
            cartToCheckout = cartService.getCart();
        }

        com.guitarshop.model.Order order = orderService.createOrder(user, cartToCheckout, shippingAddress, email, phone, paymentMethod);

        if (!isQuickBuy) {
            cartService.clear();
        }

        return "redirect:/orders/" + order.getId() + "?success=true";
    }
}
