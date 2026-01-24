package com.guitarshop.controller;

import com.guitarshop.model.Order;
import com.guitarshop.model.User;
import com.guitarshop.service.UserService;
import com.guitarshop.repository.OrderRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserService userService;

    public OrderController(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    @GetMapping
    public String myOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) return "redirect:/login";
        User user = userService.findByUsername(userDetails.getUsername());
        List<Order> orders = orderRepository.findByUser(user);
        model.addAttribute("orders", orders);
        return "shop/orders";
    }

    @GetMapping("/{id}")
    public String orderDetail(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, Model model) {
        if (userDetails == null) return "redirect:/login";
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            model.addAttribute("order", null);
            return "shop/order-detail";
        }
        // ensure the order belongs to the user (or admin can view via admin pages)
        if (!order.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/";
        }
        model.addAttribute("order", order);
        return "shop/order-detail";
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        if (userDetails == null) return "redirect:/login";
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return "redirect:/orders";
        if (!order.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/";
        }
        String status = order.getStatus();
        if ("NEW".equals(status) || "PROCESSING".equals(status)) {
            order.setStatus("CANCELLED");
            orderRepository.save(order);
        }
        return "redirect:/orders/" + id + "?cancelled=true";
    }
}
