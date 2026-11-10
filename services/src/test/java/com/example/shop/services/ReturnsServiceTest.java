package com.example.shop.services;

import static org.junit.jupiter.api.Assertions.*;

import com.example.shop.domain.*;
import org.junit.jupiter.api.Test;

class ReturnsServiceTest {

    private final InventoryService inv = new InventoryService();
    private final ReturnsService returns = new ReturnsService(inv);

    private Order delivered() {
        Order o = new Order("o1", new Customer("c", "x", "x@example.com", null));
        o.addLine(new Product("RET-0001", "Gadget", Money.usd("50"), "misc"), 3);
        o.advance();
        o.advance();
        o.advance();
        return o;
    }

    @Test
    void fullRefundWithinTwoWeeks() {
        assertEquals(Money.usd("100.00"), returns.refund(delivered(), "RET-0001", 2, 5));
        assertEquals(2, inv.available("RET-0001"));
    }

    @Test
    void notReturnableUnlessDelivered() {
        Order o = new Order("o2", new Customer("c", "x", "x@example.com", null));
        assertFalse(returns.isReturnable(o, 1));
        assertThrows(IllegalStateException.class, () -> returns.refund(o, "RET-0001", 1, 1));
    }
    // restocking fee, unknown sku, over-return and log helpers untested
}
