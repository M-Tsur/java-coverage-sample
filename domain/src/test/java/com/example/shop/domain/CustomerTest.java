package com.example.shop.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void nameIsTitleCased() {
        Customer c = new Customer("1", "jOHN   doe", "john@example.com", CustomerTier.SILVER);
        assertEquals("John Doe", c.name());
    }

    @Test
    void defaultsToBronze() {
        assertEquals(CustomerTier.BRONZE, new Customer("1", "a", "a@b.io", null).tier());
    }

    @Test
    void invalidEmailRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Customer("1", "a", "nope", null));
    }

    @Test
    void tierUpgradeAndDowngrade() {
        Customer c = new Customer("1", "a", "a@b.io", CustomerTier.GOLD);
        c.upgrade();
        assertEquals(CustomerTier.PLATINUM, c.tier());
        c.upgrade();
        assertEquals(CustomerTier.PLATINUM, c.tier());
        c.downgrade();
        assertEquals(CustomerTier.GOLD, c.tier());
        assertEquals(CustomerTier.BRONZE, CustomerTier.BRONZE.downgrade());
    }
}
