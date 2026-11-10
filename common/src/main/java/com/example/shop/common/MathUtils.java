package com.example.shop.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/** Numeric helpers. */
public final class MathUtils {

    private MathUtils() {}

    public static int clamp(int value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min > max");
        }
        return Math.max(min, Math.min(max, value));
    }

    public static BigDecimal roundHalfUp(BigDecimal value, int scale) {
        return value.setScale(scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal percentage(BigDecimal value, BigDecimal percent) {
        return roundHalfUp(value.multiply(percent).divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP), 2);
    }

    public static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    public static int lcm(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        return Math.abs(a / gcd(a, b) * b);
    }

    public static boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }
        for (int i = 2; (long) i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        if (n > 20) {
            throw new ArithmeticException("overflow");
        }
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public static double average(int... values) {
        if (values == null || values.length == 0) {
            return 0;
        }
        return Arrays.stream(values).average().orElse(0);
    }

    public static double median(int... values) {
        if (values == null || values.length == 0) {
            return 0;
        }
        int[] sorted = values.clone();
        Arrays.sort(sorted);
        int mid = sorted.length / 2;
        if (sorted.length % 2 == 1) {
            return sorted[mid];
        }
        return (sorted[mid - 1] + sorted[mid]) / 2.0;
    }

    public static int fibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        int a = 0;
        int b = 1;
        for (int i = 0; i < n; i++) {
            int t = a + b;
            a = b;
            b = t;
        }
        return a;
    }
}
