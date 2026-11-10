package com.example.shop.services;

import static org.junit.jupiter.api.Assertions.*;

import com.example.shop.domain.*;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportServiceTest {

    private OrderService orders;
    private ReportService reports;
    private Customer ann;
    private Customer ben;
    private Product pen;
    private Product desk;

    @BeforeEach
    void setUp() {
        InventoryService inv = new InventoryService();
        inv.add("PEN-0001", 100);
        inv.add("DSK-0001", 10);
        orders = new OrderService(inv, new PricingService(BigDecimal.ZERO));
        reports = new ReportService(orders);
        ann = new Customer("1", "ann", "ann@example.com", CustomerTier.BRONZE);
        ben = new Customer("2", "ben", "ben@example.com", CustomerTier.BRONZE);
        pen = new Product("PEN-0001", "Pen", Money.usd("2.00"), "office");
        desk = new Product("DSK-0001", "Desk", Money.usd("150.00"), "furniture");
    }

    private Order place(Customer c, Product p, int qty) {
        Map<Product, Integer> m = new LinkedHashMap<>();
        m.put(p, qty);
        return orders.place(c, m);
    }

    @Test
    void revenueIgnoresCancelledOrders() {
        place(ann, pen, 5);
        Order cancelled = place(ben, desk, 1);
        orders.cancel(cancelled.id());
        assertEquals(Money.usd("10.00"), reports.totalRevenue());
    }

    @Test
    void revenueByCategory() {
        place(ann, pen, 5);
        place(ben, desk, 2);
        Map<String, Money> byCat = reports.revenueByCategory();
        assertEquals(Money.usd("10.00"), byCat.get("office"));
        assertEquals(Money.usd("300.00"), byCat.get("furniture"));
    }

    @Test
    void topCustomersSortedBySpend() {
        place(ann, pen, 5);
        place(ben, desk, 1);
        assertEquals(List.of("Ben", "Ann"), reports.topCustomers(5));
        assertEquals(List.of("Ben"), reports.topCustomers(1));
    }

    @Test
    void summaryMentionsRevenue() {
        place(ann, pen, 5);
        String s = reports.summary();
        assertTrue(s.contains("Orders: 1"));
        assertTrue(s.contains("Revenue: 10.00 USD"));
    }
    // countByStatus and toCsv intentionally left untested
}
