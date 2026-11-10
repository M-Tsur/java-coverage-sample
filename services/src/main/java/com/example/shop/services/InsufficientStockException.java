package com.example.shop.services;

/** Thrown when an order requests more units than are in stock. */
public class InsufficientStockException extends RuntimeException {

    private final String sku;

    public InsufficientStockException(String sku, int requested, int available) {
        super("insufficient stock for " + sku + ": requested " + requested + ", available " + available);
        this.sku = sku;
    }

    public String sku() {
        return sku;
    }
}
