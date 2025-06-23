package com.qonto.banking.service.util;

import java.math.BigDecimal;

public class MoneyUtils {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    public static long eurosToCents(String euroAmountStr) {
        BigDecimal euros = new BigDecimal(euroAmountStr)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        return euros.multiply(HUNDRED).longValueExact();
    }

    public static long eurosToCents(BigDecimal euroAmount) {
        BigDecimal rounded = euroAmount.setScale(2,  BigDecimal.ROUND_HALF_UP);
        return rounded.multiply(HUNDRED).longValueExact();
    }
}