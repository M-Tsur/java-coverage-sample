package com.example.shop.domain;

import com.example.shop.common.Validator;

/** A product and quantity within an order. */
public final class OrderLine {

    private final Product product;
    private final int quantity;

    public OrderLine(Product product, int quantity) {
        this.product = java.util.Objects.requireNonNull(product);
        this.quantity = Validator.requirePositive(quantity, "quantity");
    }

    public Product product() {
        return product;
    }

    public int quantity() {
        return quantity;
    }

    public Money total() {
        return product.price().multiply(quantity);
    }
}
