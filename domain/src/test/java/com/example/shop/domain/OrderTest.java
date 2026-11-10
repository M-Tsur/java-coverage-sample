package com.example.shop.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Customer customer;
    private Product pen;
    private Product desk;

    @BeforeEach
    void setUp() {
        customer = new Customer("1", "ann", "ann@example.com", CustomerTier.BRONZE);
        pen = new Product("PEN-0001", "Pen", Money.usd("2.00"), "office");
        desk = new Product("DSK-0001", "Desk", Money.usd("150.00"), "furniture");
    }

    @Test
    void subtotalAndItemCount() {
        Order o = new Order("o1", customer);
        o.addLine(pen, 5);
        o.addLine(desk, 1);
        assertEquals(Money.usd("160.00"), o.subtotal());
        assertEquals(6, o.itemCount());
        assertEquals(2, o.lines().size());
    }

    @Test
    void lifecycle() {
        Order o = new Order("o1", customer);
        o.addLine(pen, 1);
        o.advance();
        o.advance();
        o.advance();
        assertEquals(OrderStatus.DELIVERED, o.status());
        assertTrue(o.status().isFinal());
        assertThrows(IllegalStateException.class, o::advance);
    }

    @Test
    void cannotModifyAfterPayment() {
        Order o = new Order("o1", customer);
        o.advance();
        assertThrows(IllegalStateException.class, () -> o.addLine(pen, 1));
    }

    @Test
    void cancelRules() {
        Order o = new Order("o1", customer);
        o.cancel();
        assertEquals(OrderStatus.CANCELLED, o.status());
        Order shipped = new Order("o2", customer);
        shipped.advance();
        shipped.advance();
        assertThrows(IllegalStateException.class, shipped::cancel);
    }

    @Test
    void productValidation() {
        assertThrows(IllegalArgumentException.class, () -> new Product("bad", "x", Money.usd("1"), "c"));
        assertThrows(IllegalArgumentException.class, () -> new Product("ABC-1234", "x", Money.usd("-1"), "c"));
        assertEquals(Money.usd("3"), pen.withPrice(Money.usd("3")).price());
        assertThrows(IllegalArgumentException.class, () -> new OrderLine(pen, 0));
    }
}
