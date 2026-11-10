package com.example.shop.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.example.shop.domain.ShippingCalculator.Zone;
import org.junit.jupiter.api.Test;

class ShippingCalculatorTest {

    @Test
    void domesticCostWithExtraWeight() {
        // 2.5kg -> 2 extra kilos
        assertEquals(Money.usd("10.00"), ShippingCalculator.cost(Zone.DOMESTIC, 2500, Money.usd("20")));
    }

    @Test
    void freeShippingForBigDomesticOrders() {
        assertTrue(ShippingCalculator.cost(Zone.LOCAL, 500, Money.usd("150")).isZero());
    }

    @Test
    void invalidWeight() {
        assertThrows(IllegalArgumentException.class, () -> ShippingCalculator.cost(Zone.LOCAL, 0, Money.usd("1")));
    }
    // international, estimatedDays, insurance, label untested
}
