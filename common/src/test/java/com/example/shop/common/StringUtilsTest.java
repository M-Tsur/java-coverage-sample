package com.example.shop.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class StringUtilsTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n"})
    void blankValues(String s) {
        assertTrue(StringUtils.isBlank(s));
    }

    @Test
    void nonBlank() {
        assertFalse(StringUtils.isBlank(" a "));
    }

    @Test
    void capitalize() {
        assertEquals("Hello", StringUtils.capitalize("hello"));
        assertEquals("", StringUtils.capitalize(""));
        assertNull(StringUtils.capitalize(null));
    }

    @Test
    void truncate() {
        assertEquals("hello", StringUtils.truncate("hello", 10));
        assertEquals("hel...", StringUtils.truncate("hello world", 6));
        assertEquals("he", StringUtils.truncate("hello", 2));
        assertNull(StringUtils.truncate(null, 3));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.truncate("x", -1));
    }

    @Test
    void slugify() {
        assertEquals("hello-world", StringUtils.slugify("  Hello, World!  "));
        assertEquals("", StringUtils.slugify(null));
    }

    @Test
    void reverseAndPalindrome() {
        assertEquals("cba", StringUtils.reverse("abc"));
        assertNull(StringUtils.reverse(null));
        assertTrue(StringUtils.isPalindrome("A man, a plan, a canal: Panama"));
        assertFalse(StringUtils.isPalindrome("hello"));
        assertFalse(StringUtils.isPalindrome(null));
    }

    @Test
    void padding() {
        assertEquals("  ab", StringUtils.padLeft("ab", 4, ' '));
        assertEquals("ab--", StringUtils.padRight("ab", 4, '-'));
        assertEquals("abcdef", StringUtils.padRight("abcdef", 3, '-'));
        assertEquals("000", StringUtils.padLeft(null, 3, '0'));
    }

    @Test
    void csvEscape() {
        assertEquals("plain", StringUtils.csvEscape("plain"));
        assertEquals("\"a,b\"", StringUtils.csvEscape("a,b"));
        assertEquals("\"say \"\"hi\"\"\"", StringUtils.csvEscape("say \"hi\""));
        assertEquals("", StringUtils.csvEscape(null));
    }
    // countOccurrences and toTitleCase are covered indirectly (toTitleCase via Customer in domain)
}
