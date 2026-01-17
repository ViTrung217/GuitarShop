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

    // ✅ BẮT BUỘC
    private int quantity;

    // ✅ BẮT BUỘC
    private BigDecimal price;

    // ===== Getter / Setter =====

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
}
