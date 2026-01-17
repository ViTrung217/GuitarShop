package com.guitarshop.service;

import java.math.BigDecimal;

public class CartItem {
    public Long productId;
    public String name;
    public BigDecimal price;
    public int quantity;
    public String image;

    public BigDecimal getTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
