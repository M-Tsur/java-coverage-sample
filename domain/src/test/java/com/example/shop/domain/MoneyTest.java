package com.example.shop.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MoneyTest {

    @Test
    void addAndSubtract() {
        assertEquals(Money.usd("15.50"), Money.usd("10").add(Money.usd("5.5")));
        assertEquals(Money.usd("4.50"), Money.usd("10").subtract(Money.usd("5.5")));
    }

    @Test
    void multiplyAndPercent() {
        assertEquals(Money.usd("30.00"), Money.usd("10").multiply(3));
        assertEquals(Money.usd("2.50"), Money.usd("50").percent(new BigDecimal("5")));
    }

    @Test
    void scaleIsNormalised() {
        assertEquals("10.35 USD", Money.usd("10.345").toString());
        assertEquals(Money.usd("1"), Money.usd("1.00"));
        assertEquals(Money.usd("1").hashCode(), Money.usd("1.00").hashCode());
    }

    @Test
    void currencyMismatchRejected() {
        assertThrows(IllegalArgumentException.class, () -> Money.usd("1").add(Money.of("1", "EUR")));
        assertThrows(IllegalArgumentException.class, () -> Money.usd("1").compareTo(Money.of("1", "EUR")));
    }

    @Test
    void comparisonAndSign() {
        assertTrue(Money.usd("2").compareTo(Money.usd("1")) > 0);
        assertTrue(Money.usd("-1").isNegative());
        assertTrue(Money.zero("USD").isZero());
        assertFalse(Money.usd("1").isZero());
    }

    @Test
    void equalsContract() {
        Money m = Money.usd("5");
        assertEquals(m, m);
        assertNotEquals(m, "5");
        assertNotEquals(m, Money.of("5", "EUR"));
    }
}
