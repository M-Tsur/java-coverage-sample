package com.example.shop.services;

import static org.junit.jupiter.api.Assertions.*;

import com.example.shop.domain.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PricingServiceTest {

    private final PricingService pricing = new PricingService(new BigDecimal("10"));
    private final Product item = new Product("ITM-0001", "Item", Money.usd("100.00"), "misc");

    private Order order(CustomerTier tier, int qty) {
        Order o = new Order("o", new Customer("c", "cust", "c@example.com", tier));
        o.addLine(item, qty);
        return o;
    }

    @Test
    void tierDiscount() {
        Order o = order(CustomerTier.GOLD, 1);
        assertEquals(Money.usd("10.00"), pricing.tierDiscount(o.customer(), o.subtotal()));
    }

    @Test
    void bulkDiscountOnlyAtThreshold() {
        assertTrue(pricing.bulkDiscount(order(CustomerTier.BRONZE, 9)).isZero());
        assertEquals(Money.usd("50.00"), pricing.bulkDiscount(order(CustomerTier.BRONZE, 10)));
    }

    @Test
    void discountIsCappedAtThirtyPercent() {
        // platinum 15% + bulk 5% = 20% -> below the cap
        assertEquals(Money.usd("200.00"), pricing.totalDiscount(order(CustomerTier.PLATINUM, 10)));
    }

    @Test
    void finalTotalIncludesTax() {
        // 1 x 100, gold 10% off -> 90, +10% tax -> 99
        assertEquals(Money.usd("99.00"), pricing.finalTotal(order(CustomerTier.GOLD, 1)));
    }

    @Test
    void negativeTaxRejected() {
        assertThrows(IllegalArgumentException.class, () -> new PricingService(new BigDecimal("-1")));
    }

    @Test
    void splitEvenly() {
        assertEquals(Money.usd("33.33"), pricing.splitEvenly(Money.usd("100"), 3));
        assertThrows(IllegalArgumentException.class, () -> pricing.splitEvenly(Money.usd("1"), 0));
    }
}
