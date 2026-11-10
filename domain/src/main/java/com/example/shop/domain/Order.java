package com.example.shop.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** An order placed by a customer. */
public final class Order {

    private final String id;
    private final Customer customer;
    private final List<OrderLine> lines = new ArrayList<>();
    private OrderStatus status = OrderStatus.NEW;

    public Order(String id, Customer customer) {
        this.id = id;
        this.customer = customer;
    }

    public String id() {
        return id;
    }

    public Customer customer() {
        return customer;
    }

    public OrderStatus status() {
        return status;
    }

    public List<OrderLine> lines() {
        return Collections.unmodifiableList(lines);
    }

    public void addLine(Product product, int quantity) {
        if (status != OrderStatus.NEW) {
            throw new IllegalStateException("cannot modify order in status " + status);
        }
        lines.add(new OrderLine(product, quantity));
    }

    public Money subtotal() {
        Money sum = Money.zero("USD");
        for (OrderLine line : lines) {
            sum = sum.add(line.total());
        }
        return sum;
    }

    public int itemCount() {
        return lines.stream().mapToInt(OrderLine::quantity).sum();
    }

    public void advance() {
        status = status.next();
    }

    public void cancel() {
        if (!status.canCancel()) {
            throw new IllegalStateException("cannot cancel order in status " + status);
        }
        status = OrderStatus.CANCELLED;
    }
}
