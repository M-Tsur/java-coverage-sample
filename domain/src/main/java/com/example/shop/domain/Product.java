package com.example.shop.domain;

import com.example.shop.common.Validator;

/** A sellable product. */
public final class Product {

    private final String sku;
    private final String name;
    private final Money price;
    private final String category;

    public Product(String sku, String name, Money price, String category) {
        this.sku = Validator.requireValidSku(sku);
        this.name = Validator.requireNonBlank(name, "name");
        if (price.isNegative()) {
            throw new IllegalArgumentException("price must not be negative");
        }
        this.price = price;
        this.category = Validator.requireNonBlank(category, "category");
    }

    public String sku() {
        return sku;
    }

    public String name() {
        return name;
    }

    public Money price() {
        return price;
    }

    public String category() {
        return category;
    }

    public Product withPrice(Money newPrice) {
        return new Product(sku, name, newPrice, category);
    }

    @Override
    public String toString() {
        return sku + " " + name + " @ " + price;
    }
}
