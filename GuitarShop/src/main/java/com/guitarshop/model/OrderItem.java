package com.guitarshop.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ BẮT BUỘC
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // ✅ BẮT BUỘC - Reference to Product
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // ✅ BẮT BUỘC
    private int quantity;

    // ✅ BẮT BUỘC
    private BigDecimal price;

    // ===== Getter / Setter =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSubtotal() {
        if (price == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
