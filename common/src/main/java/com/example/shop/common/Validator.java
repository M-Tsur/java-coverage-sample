package com.example.shop.common;

import java.util.regex.Pattern;

/** Argument validation helpers. */
public final class Validator {

    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern SKU = Pattern.compile("^[A-Z]{3}-\\d{4}$");

    private Validator() {}

    public static String requireNonBlank(String value, String name) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }

    public static int requirePositive(int value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be positive");
        }
        return value;
    }

    public static int requireInRange(int value, int min, int max, String name) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(name + " must be between " + min + " and " + max);
        }
        return value;
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL.matcher(email).matches();
    }

    public static boolean isValidSku(String sku) {
        return sku != null && SKU.matcher(sku).matches();
    }

    public static String requireValidEmail(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("invalid email: " + email);
        }
        return email;
    }

    public static String requireValidSku(String sku) {
        if (!isValidSku(sku)) {
            throw new IllegalArgumentException("invalid sku: " + sku);
        }
        return sku;
    }
}
