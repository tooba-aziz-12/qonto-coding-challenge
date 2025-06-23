package com.qonto.banking.service.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyUtilsTest {

    @Test
    void shouldConvertWholeEurosCorrectly() {
        assertEquals(100L, MoneyUtils.eurosToCents("1.00"));
        assertEquals(2500L, MoneyUtils.eurosToCents("25.00"));
    }

    @Test
    void shouldRoundHalfUpCorrectly() {
        assertEquals(12346L, MoneyUtils.eurosToCents("123.456")); // 123.46 EUR
        assertEquals(12345L, MoneyUtils.eurosToCents("123.454")); // 123.45 EUR
    }

    @Test
    void shouldThrowOnOverflow() {
        BigDecimal huge = new BigDecimal("992233720368547758.08"); // > Long.MAX_VALUE / 100
        assertThrows(ArithmeticException.class, () -> MoneyUtils.eurosToCents(huge));
    }

    @Test
    void shouldWorkWithBigDecimalOverloads() {
        assertEquals(199L, MoneyUtils.eurosToCents(new BigDecimal("1.994")));
        assertEquals(200L, MoneyUtils.eurosToCents(new BigDecimal("1.995")));
    }

    @Test
    void shouldHandleZeroValues() {
        assertEquals(0L, MoneyUtils.eurosToCents("0"));
        assertEquals(0L, MoneyUtils.eurosToCents("0.00"));
    }

    @Test
    void shouldHandleNegativeValues() {
        assertEquals(-12346L, MoneyUtils.eurosToCents("-123.456")); // -123.46 EUR
        assertEquals(-200L, MoneyUtils.eurosToCents(new BigDecimal("-2.001")));
    }

    @Test
    void shouldThrowOnInvalidInput() {
        assertThrows(NumberFormatException.class, () -> MoneyUtils.eurosToCents("abc"));
        assertThrows(NullPointerException.class, () -> MoneyUtils.eurosToCents((String) null));
    }
    @Test
    void shouldConvertMaxValidLongValue() {
        BigDecimal maxValid = new BigDecimal("92233720368547758.07");
        assertEquals(Long.MAX_VALUE, MoneyUtils.eurosToCents(maxValid));
    }
}
