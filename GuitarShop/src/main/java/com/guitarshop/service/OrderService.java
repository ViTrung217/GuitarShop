package com.guitarshop.service;

import com.guitarshop.event.OrderEventPublisher;
import com.guitarshop.model.*;
import com.guitarshop.repository.OrderRepository;
import com.guitarshop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderEventPublisher orderEventPublisher;

    public OrderService(OrderRepository orderRepository,
                       ProductRepository productRepository,
                       OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderEventPublisher = orderEventPublisher;
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
            
            // Set product reference
            Product product = productRepository.findById(item.productId).orElse(null);
            oi.setProduct(product);
            
            total = total.add(item.getTotal());
            order.getItems().add(oi);
        }

        order.setTotal(total);
        Order saved = orderRepository.save(order);
        orderEventPublisher.publishOrderCreated(saved);
        return saved;
    }

    public Order createOrder(User user, Map<Long, CartItem> cart, String shippingAddress, String email, String phone, String paymentMethod) {
        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("NEW");

        java.math.BigDecimal total = java.math.BigDecimal.ZERO;

        for (CartItem item : cart.values()) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setQuantity(item.quantity);
            oi.setPrice(item.price);

            // Set product reference
            Product product = productRepository.findById(item.productId).orElse(null);
            oi.setProduct(product);

            total = total.add(item.getTotal());
            order.getItems().add(oi);
        }

        order.setTotal(total);
        order.setShippingAddress(shippingAddress);
        order.setEmail(email);
        order.setPhone(phone);
        order.setPaymentMethod(paymentMethod);
        Order saved = orderRepository.save(order);
        orderEventPublisher.publishOrderCreated(saved);
        return saved;
    }
}
