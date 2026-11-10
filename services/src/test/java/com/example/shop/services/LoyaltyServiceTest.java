package com.example.shop.services;

import static org.junit.jupiter.api.Assertions.*;

import com.example.shop.domain.*;
import org.junit.jupiter.api.Test;

class LoyaltyServiceTest {

    private final LoyaltyService loyalty = new LoyaltyService();
    private final Customer c = new Customer("c1", "kim", "kim@example.com", CustomerTier.BRONZE);

    @Test
    void accrueOnePointPerTenDollars() {
        assertEquals(12, loyalty.accrue(c, Money.usd("125.00")));
        assertEquals(12, loyalty.balance(c));
    }

    @Test
    void redeem() {
        loyalty.accrue(c, Money.usd("100"));
        assertTrue(loyalty.redeem(c, 4));
        assertFalse(loyalty.redeem(c, 100));
        assertEquals(6, loyalty.balance(c));
        assertThrows(IllegalArgumentException.class, () -> loyalty.redeem(c, 0));
    }

    @Test
    void tierThresholds() {
        assertEquals(CustomerTier.BRONZE, loyalty.tierFor(10));
        assertEquals(CustomerTier.GOLD, loyalty.tierFor(2000));
    }
    // reevaluate, leaderboard, expireAll untested
}
