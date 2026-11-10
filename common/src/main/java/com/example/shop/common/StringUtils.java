package com.example.shop.common;

import java.util.Locale;

/** Small collection of string helpers. */
public final class StringUtils {

    private StringUtils() {}

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static String capitalize(String s) {
        if (isBlank(s)) {
            return s;
        }
        return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1);
    }

    public static String truncate(String s, int max) {
        if (s == null) {
            return null;
        }
        if (max < 0) {
            throw new IllegalArgumentException("max must be >= 0");
        }
        if (s.length() <= max) {
            return s;
        }
        if (max <= 3) {
            return s.substring(0, max);
        }
        return s.substring(0, max - 3) + "...";
    }

    public static String slugify(String s) {
        if (isBlank(s)) {
            return "";
        }
        return s.trim().toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }

    public static String reverse(String s) {
        return s == null ? null : new StringBuilder(s).reverse().toString();
    }

    public static boolean isPalindrome(String s) {
        if (s == null) {
            return false;
        }
        String cleaned = s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
        return cleaned.equals(new StringBuilder(cleaned).reverse().toString());
    }

    public static String padLeft(String s, int width, char pad) {
        String value = s == null ? "" : s;
        StringBuilder sb = new StringBuilder();
        for (int i = value.length(); i < width; i++) {
            sb.append(pad);
        }
        return sb.append(value).toString();
    }

    public static String padRight(String s, int width, char pad) {
        String value = s == null ? "" : s;
        StringBuilder sb = new StringBuilder(value);
        for (int i = value.length(); i < width; i++) {
            sb.append(pad);
        }
        return sb.toString();
    }

    public static int countOccurrences(String haystack, String needle) {
        if (haystack == null || isBlank(needle)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = haystack.indexOf(needle, idx)) >= 0) {
            count++;
            idx += needle.length();
        }
        return count;
    }

    public static String toTitleCase(String s) {
        if (isBlank(s)) {
            return s;
        }
        String[] words = s.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(capitalize(w.toLowerCase(Locale.ROOT)));
        }
        return sb.toString();
    }

    public static String csvEscape(String s) {
        if (s == null) {
            return "";
        }
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }
}
