package com.example.shop.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InventoryServiceTest {

    private final InventoryService inv = new InventoryService();

    @Test
    void addAccumulates() {
        inv.add("ABC-0001", 3);
        inv.add("ABC-0001", 4);
        assertEquals(7, inv.available("ABC-0001"));
        assertEquals(0, inv.available("ZZZ-9999"));
    }

    @Test
    void removeReducesAndDropsEmpty() {
        inv.add("ABC-0001", 5);
        inv.remove("ABC-0001", 2);
        assertEquals(3, inv.available("ABC-0001"));
        inv.remove("ABC-0001", 3);
        assertTrue(inv.snapshot().isEmpty());
    }

    @Test
    void removeTooMuchFails() {
        inv.add("ABC-0001", 1);
        InsufficientStockException ex =
                assertThrows(InsufficientStockException.class, () -> inv.remove("ABC-0001", 2));
        assertEquals("ABC-0001", ex.sku());
    }

    @Test
    void invalidInputRejected() {
        assertThrows(IllegalArgumentException.class, () -> inv.add("bad", 1));
        assertThrows(IllegalArgumentException.class, () -> inv.add("ABC-0001", 0));
    }

    @Test
    void totals() {
        inv.add("ABC-0001", 2);
        inv.add("DEF-0002", 3);
        assertEquals(5, inv.totalUnits());
        assertTrue(inv.has("DEF-0002", 3));
        assertFalse(inv.has("DEF-0002", 4));
    }
    // lowStock intentionally left untested
}
