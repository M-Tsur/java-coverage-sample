package com.example.shop.domain;

/** Lifecycle of an order. */
public enum OrderStatus {
    NEW, PAID, SHIPPED, DELIVERED, CANCELLED;

    public boolean isFinal() {
        return this == DELIVERED || this == CANCELLED;
    }

    public OrderStatus next() {
        switch (this) {
            case NEW:
                return PAID;
            case PAID:
                return SHIPPED;
            case SHIPPED:
                return DELIVERED;
            default:
                throw new IllegalStateException("no next status for " + this);
        }
    }

    public boolean canCancel() {
        return this == NEW || this == PAID;
    }
}
