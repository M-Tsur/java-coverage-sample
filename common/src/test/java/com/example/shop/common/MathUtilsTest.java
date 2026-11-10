package com.example.shop.common;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MathUtilsTest {

    @Test
    void clamp() {
        assertEquals(5, MathUtils.clamp(5, 0, 10));
        assertEquals(0, MathUtils.clamp(-5, 0, 10));
        assertEquals(10, MathUtils.clamp(50, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> MathUtils.clamp(1, 10, 0));
    }

    @Test
    void rounding() {
        assertEquals(new BigDecimal("2.35"), MathUtils.roundHalfUp(new BigDecimal("2.345"), 2));
    }

    @Test
    void percentage() {
        assertEquals(new BigDecimal("15.00"), MathUtils.percentage(new BigDecimal("200"), new BigDecimal("7.5")));
    }

    @Test
    void gcdAndLcm() {
        assertEquals(6, MathUtils.gcd(12, 18));
        assertEquals(6, MathUtils.gcd(-12, 18));
        assertEquals(36, MathUtils.lcm(12, 18));
        assertEquals(0, MathUtils.lcm(0, 5));
    }

    @Test
    void primes() {
        assertFalse(MathUtils.isPrime(1));
        assertTrue(MathUtils.isPrime(2));
        assertTrue(MathUtils.isPrime(97));
        assertFalse(MathUtils.isPrime(91));
    }

    @Test
    void fibonacci() {
        assertEquals(0, MathUtils.fibonacci(0));
        assertEquals(55, MathUtils.fibonacci(10));
        assertThrows(IllegalArgumentException.class, () -> MathUtils.fibonacci(-1));
    }

    @Test
    void average() {
        assertEquals(2.0, MathUtils.average(1, 2, 3));
        assertEquals(0.0, MathUtils.average());
    }
    // factorial and median intentionally left untested
}
