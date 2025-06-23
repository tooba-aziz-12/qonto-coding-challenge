package com.qonto.banking.exception;

public class InsufficientFundsException extends RuntimeException {

    private final String encodedIban;

    public InsufficientFundsException(String encodedIban) {
        super("Insufficient funds to complete the transfer request for IBAN: " + encodedIban);
        this.encodedIban = encodedIban;
    }

    public InsufficientFundsException(String message, String encodedIban) {
        super(message);
        this.encodedIban = encodedIban;
    }

    public InsufficientFundsException(String message, Throwable cause, String encodedIban) {
        super(message, cause);
        this.encodedIban = encodedIban;
    }

    public InsufficientFundsException(Throwable cause, String encodedIban) {
        super(cause);
        this.encodedIban = encodedIban;
    }

    public String getEncodedIban() {
        return encodedIban;
    }
}

