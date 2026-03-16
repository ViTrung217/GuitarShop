package com.guitarshop.service;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    public Long productId;
    public String name;
    public BigDecimal price;
    public int quantity;
    public String image;

    public BigDecimal getTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
