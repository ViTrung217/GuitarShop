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

    public CheckoutController(OrderService orderService,
                              CartService cartService,
                              UserService userService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.userService = userService;
    }

    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        orderService.createOrder(user, cartService.getCart());
        cartService.clear();
        return "redirect:/?success";
    }
}
