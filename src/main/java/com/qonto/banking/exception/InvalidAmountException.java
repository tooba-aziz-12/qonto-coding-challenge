package com.qonto.banking.exception;


public class InvalidAmountException extends RuntimeException {
    private final String encodedIban;

    public InvalidAmountException(String message, String encodedIban, Throwable cause) {
        super(message, cause);
        this.encodedIban = encodedIban;
    }

    public String getEncodedIban() {
        return encodedIban;
    }
}

