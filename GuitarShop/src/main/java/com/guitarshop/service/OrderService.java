package com.guitarshop.service;

import com.guitarshop.model.*;
import com.guitarshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(User user, Map<Long, CartItem> cart) {
        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("NEW");

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.values()) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setQuantity(item.quantity);
            oi.setPrice(item.price);
            total = total.add(item.getTotal());
            order.getItems().add(oi);
        }

        order.setTotal(total);
        return orderRepository.save(order);
    }
}
