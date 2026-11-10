package com.example.shop.app;

import static org.junit.jupiter.api.Assertions.*;

import com.example.shop.domain.Money;
import org.junit.jupiter.api.Test;

class ShopAppTest {

    @Test
    void demoRunsEndToEnd() {
        ShopApp app = new ShopApp();
        String summary = app.runDemo();

        // alice: laptop 1200 + 2 mice 50 = 1250, gold 10% off = 1125, + 8.5% tax = 1220.63 (rounded)
        assertTrue(summary.contains("Orders: 2"));
        assertTrue(summary.contains("Revenue: 1220.63 USD"));
        assertEquals(Money.usd("1220.63"), app.reports().totalRevenue());
    }

    @Test
    void demoLeavesExpectedStock() {
        ShopApp app = new ShopApp();
        app.runDemo();
        assertEquals(4, app.inventory().available("LAP-0001"));
        assertEquals(48, app.inventory().available("MOU-0002"));
        // bob's order was cancelled so the books are back in stock
        assertEquals(20, app.inventory().available("BOK-0003"));
    }
}
