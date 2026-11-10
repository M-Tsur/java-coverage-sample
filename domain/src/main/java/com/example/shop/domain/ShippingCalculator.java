package com.example.shop.domain;

import java.math.BigDecimal;

/** Computes shipping cost from region, weight and order value. */
public final class ShippingCalculator {

    public enum Zone { LOCAL, DOMESTIC, INTERNATIONAL }

    private static final Money FREE_SHIPPING_FROM = Money.usd("100.00");

    private ShippingCalculator() {}

    public static Money cost(Zone zone, int weightGrams, Money orderValue) {
        if (weightGrams <= 0) {
            throw new IllegalArgumentException("weight must be positive");
        }
        if (zone != Zone.INTERNATIONAL && orderValue.compareTo(FREE_SHIPPING_FROM) >= 0) {
            return Money.zero(orderValue.currency());
        }
        Money base;
        switch (zone) {
            case LOCAL:
                base = Money.usd("3.00");
                break;
            case DOMESTIC:
                base = Money.usd("7.50");
                break;
            default:
                base = Money.usd("25.00");
                break;
        }
        int extraKilos = Math.max(0, (weightGrams - 1) / 1000);
        return base.add(Money.usd("1.25").multiply(extraKilos));
    }

    public static int estimatedDays(Zone zone, boolean express) {
        int days;
        switch (zone) {
            case LOCAL:
                days = 1;
                break;
            case DOMESTIC:
                days = 4;
                break;
            default:
                days = 12;
                break;
        }
        if (express) {
            days = Math.max(1, days / 2);
        }
        return days;
    }

    public static Money insurance(Money orderValue) {
        if (orderValue.compareTo(Money.usd("500")) < 0) {
            return Money.zero(orderValue.currency());
        }
        return orderValue.percent(new BigDecimal("1.5"));
    }

    public static String label(Zone zone) {
        switch (zone) {
            case LOCAL:
                return "Local courier";
            case DOMESTIC:
                return "National post";
            default:
                return "International freight";
        }
    }
}
