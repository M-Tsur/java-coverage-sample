package com.example.shop.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ValidatorTest {

    @Test
    void requireNonBlank() {
        assertEquals("x", Validator.requireNonBlank("x", "f"));
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> Validator.requireNonBlank(" ", "field"));
        assertTrue(ex.getMessage().contains("field"));
    }

    @Test
    void requirePositive() {
        assertEquals(3, Validator.requirePositive(3, "q"));
        assertThrows(IllegalArgumentException.class, () -> Validator.requirePositive(0, "q"));
    }

    @Test
    void emails() {
        assertTrue(Validator.isValidEmail("a.b@example.com"));
        assertFalse(Validator.isValidEmail("nope"));
        assertFalse(Validator.isValidEmail(null));
        assertThrows(IllegalArgumentException.class, () -> Validator.requireValidEmail("bad@"));
    }

    @Test
    void skus() {
        assertTrue(Validator.isValidSku("ABC-1234"));
        assertFalse(Validator.isValidSku("abc-1234"));
        assertThrows(IllegalArgumentException.class, () -> Validator.requireValidSku("X"));
    }
    // requireInRange intentionally left untested
}
