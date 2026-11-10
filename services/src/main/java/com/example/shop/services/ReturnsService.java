package com.example.shop.services;

import com.example.shop.domain.Money;
import com.example.shop.domain.Order;
import com.example.shop.domain.OrderLine;
import com.example.shop.domain.OrderStatus;
import java.util.ArrayList;
import java.util.List;

/** Handles product returns and refunds for delivered orders. */
public class ReturnsService {

    public static final int RETURN_WINDOW_DAYS = 30;

    private final InventoryService inventory;
    private final List<String> log = new ArrayList<>();

    public ReturnsService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public boolean isReturnable(Order order, int daysSinceDelivery) {
        return order.status() == OrderStatus.DELIVERED && daysSinceDelivery <= RETURN_WINDOW_DAYS;
    }

    public Money refund(Order order, String sku, int quantity, int daysSinceDelivery) {
        if (!isReturnable(order, daysSinceDelivery)) {
            throw new IllegalStateException("order is not returnable");
        }
        for (OrderLine line : order.lines()) {
            if (line.product().sku().equals(sku)) {
                if (quantity > line.quantity()) {
                    throw new IllegalArgumentException("cannot return more than purchased");
                }
                inventory.add(sku, quantity);
                Money amount = line.product().price().multiply(quantity);
                if (daysSinceDelivery > 14) {
                    amount = amount.subtract(amount.percent(java.math.BigDecimal.TEN));
                }
                log.add(order.id() + ":" + sku + ":" + quantity + ":" + amount);
                return amount;
            }
        }
        throw new IllegalArgumentException("sku not in order: " + sku);
    }

    public List<String> log() {
        return new ArrayList<>(log);
    }

    public int totalReturnedUnits() {
        int total = 0;
        for (String entry : log) {
            total += Integer.parseInt(entry.split(":")[2]);
        }
        return total;
    }

    public boolean hasReturns() {
        return !log.isEmpty();
    }

    public void clearLog() {
        log.clear();
    }
}
