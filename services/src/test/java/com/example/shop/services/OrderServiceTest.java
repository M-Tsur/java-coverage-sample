package com.example.shop.services;

import static org.junit.jupiter.api.Assertions.*;

import com.example.shop.domain.*;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    private InventoryService inventory;
    private OrderService service;
    private Customer customer;
    private Product widget;

    @BeforeEach
    void setUp() {
        inventory = new InventoryService();
        inventory.add("WID-0001", 10);
        service = new OrderService(inventory, new PricingService(BigDecimal.ZERO));
        customer = new Customer("c1", "dana", "dana@example.com", CustomerTier.BRONZE);
        widget = new Product("WID-0001", "Widget", Money.usd("20.00"), "parts");
    }

    private Map<Product, Integer> items(int qty) {
        Map<Product, Integer> m = new LinkedHashMap<>();
        m.put(widget, qty);
        return m;
    }

    @Test
    void placeReservesStockAndPrices() {
        Order o = service.place(customer, items(4));
        assertEquals(6, inventory.available("WID-0001"));
        assertEquals(Money.usd("80.00"), service.totalFor(o));
        assertTrue(service.find(o.id()).isPresent());
    }

    @Test
    void placeFailsWithoutStockAndLeavesInventoryUntouched() {
        assertThrows(InsufficientStockException.class, () -> service.place(customer, items(11)));
        assertEquals(10, inventory.available("WID-0001"));
        assertTrue(service.all().isEmpty());
    }

    @Test
    void emptyOrderRejected() {
        assertThrows(IllegalArgumentException.class, () -> service.place(customer, new LinkedHashMap<>()));
    }

    @Test
    void payShipFlow() {
        Order o = service.place(customer, items(1));
        assertThrows(IllegalStateException.class, () -> service.ship(o.id()));
        service.pay(o.id());
        assertThrows(IllegalStateException.class, () -> service.pay(o.id()));
        service.ship(o.id());
        assertEquals(OrderStatus.SHIPPED, o.status());
        assertEquals(1, service.byStatus(OrderStatus.SHIPPED).size());
    }

    @Test
    void cancelRestocks() {
        Order o = service.place(customer, items(7));
        service.cancel(o.id());
        assertEquals(10, inventory.available("WID-0001"));
        assertEquals(OrderStatus.CANCELLED, o.status());
    }

    @Test
    void unknownOrderRejected() {
        assertThrows(IllegalArgumentException.class, () -> service.pay("nope"));
    }
}
