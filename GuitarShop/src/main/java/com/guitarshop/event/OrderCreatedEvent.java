package com.guitarshop.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderCreatedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long orderId;
    private Long userId;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private int itemCount;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(Long orderId, Long userId, BigDecimal total, LocalDateTime createdAt, int itemCount) {
        this.orderId = orderId;
        this.userId = userId;
        this.total = total;
        this.createdAt = createdAt;
        this.itemCount = itemCount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}
