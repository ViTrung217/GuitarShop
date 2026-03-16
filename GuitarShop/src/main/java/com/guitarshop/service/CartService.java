package com.guitarshop.service;

import com.guitarshop.model.Product;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component
@SessionScope
public class CartService implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<Long, CartItem> cart = new HashMap<>();

    public void add(Product product) {
        add(product, 1);
    }

    public void add(Product product, int quantity) {
        if (product == null) return;
        int qty = Math.max(1, quantity);

        CartItem item = cart.getOrDefault(product.getId(), new CartItem());
        item.productId = product.getId();
        item.name = product.getName();
        item.price = product.getPrice();
        item.image = product.getImagePath();
        item.quantity += qty;
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
