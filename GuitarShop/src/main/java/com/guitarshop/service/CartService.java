package com.guitarshop.service;

import com.guitarshop.model.Product;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {

    private final Map<Long, CartItem> cart = new HashMap<>();

    public void add(Product product) {
        CartItem item = cart.getOrDefault(product.getId(), new CartItem());
        item.productId = product.getId();
        item.name = product.getName();
        item.price = product.getPrice();
        item.image = product.getImagePath();
        item.quantity++;
        cart.put(product.getId(), item);
    }

    public void remove(Long id) {
        cart.remove(id);
    }

    public Map<Long, CartItem> getCart() {
        return cart;
    }

    public void clear() {
        cart.clear();
    }
}
