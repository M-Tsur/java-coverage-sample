package com.example.shop.services;

import com.example.shop.domain.Customer;
import com.example.shop.domain.Money;
import com.example.shop.domain.Order;
import com.example.shop.domain.OrderLine;
import com.example.shop.domain.OrderStatus;
import com.example.shop.domain.Product;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/** Orchestrates order placement using inventory and pricing. */
public class OrderService {

    private final InventoryService inventory;
    private final PricingService pricing;
    private final Map<String, Order> orders = new LinkedHashMap<>();
    private final AtomicInteger sequence = new AtomicInteger();

    public OrderService(InventoryService inventory, PricingService pricing) {
        this.inventory = inventory;
        this.pricing = pricing;
    }

    /** Places an order, reserving stock for each line. All-or-nothing. */
    public Order place(Customer customer, Map<Product, Integer> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("order must contain at least one item");
        }
        for (Map.Entry<Product, Integer> e : items.entrySet()) {
            if (!inventory.has(e.getKey().sku(), e.getValue())) {
                throw new InsufficientStockException(e.getKey().sku(), e.getValue(),
                        inventory.available(e.getKey().sku()));
            }
        }
        Order order = new Order("ORD-" + sequence.incrementAndGet(), customer);
        for (Map.Entry<Product, Integer> e : items.entrySet()) {
            inventory.remove(e.getKey().sku(), e.getValue());
            order.addLine(e.getKey(), e.getValue());
        }
        orders.put(order.id(), order);
        return order;
    }

    public Money totalFor(Order order) {
        return pricing.finalTotal(order);
    }

    public Optional<Order> find(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    public void pay(String id) {
        Order order = require(id);
        if (order.status() != OrderStatus.NEW) {
            throw new IllegalStateException("order " + id + " is not payable");
        }
        order.advance();
    }

    public void ship(String id) {
        Order order = require(id);
        if (order.status() != OrderStatus.PAID) {
            throw new IllegalStateException("order " + id + " must be paid before shipping");
        }
        order.advance();
    }

    /** Cancels an order and returns its stock to the inventory. */
    public void cancel(String id) {
        Order order = require(id);
        order.cancel();
        for (OrderLine line : order.lines()) {
            inventory.add(line.product().sku(), line.quantity());
        }
    }

    public List<Order> byStatus(OrderStatus status) {
        List<Order> result = new ArrayList<>();
        for (Order o : orders.values()) {
            if (o.status() == status) {
                result.add(o);
            }
        }
        return result;
    }

    public List<Order> all() {
        return new ArrayList<>(orders.values());
    }

    private Order require(String id) {
        return find(id).orElseThrow(() -> new IllegalArgumentException("unknown order " + id));
    }
}
