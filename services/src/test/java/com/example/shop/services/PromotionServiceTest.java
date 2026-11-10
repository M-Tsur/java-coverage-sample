package com.example.shop.services;

import static org.junit.jupiter.api.Assertions.*;

import com.example.shop.domain.*;
import org.junit.jupiter.api.Test;

class PromotionServiceTest {

    private final PromotionService promos = new PromotionService();

    private Order order(String price) {
        Order o = new Order("o", new Customer("c", "x", "x@example.com", null));
        o.addLine(new Product("PRM-0001", "Thing", Money.usd(price), "misc"), 1);
        return o;
    }

    @Test
    void percentPromo() {
        promos.registerPercent("save20", 20);
        assertEquals(Money.usd("20.00"), promos.discountFor("SAVE20", order("100")));
        assertTrue(promos.isKnown("save20"));
    }

    @Test
    void unknownCodeGivesNoDiscount() {
        assertTrue(promos.discountFor("NOPE", order("100")).isZero());
    }

    @Test
    void fixedPromoNeverExceedsSubtotal() {
        promos.registerFixed("TEN", "10");
        assertEquals(Money.usd("10.00"), promos.discountFor("TEN", order("100")));
        assertEquals(Money.usd("4.00"), promos.discountFor("TEN", order("4")));
    }
    // min-spend promos, redeemOnce, describe, remove untested
}
