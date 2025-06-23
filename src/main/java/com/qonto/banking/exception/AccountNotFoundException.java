package com.qonto.banking.exception;

public class AccountNotFoundException extends RuntimeException {

    private final String encodedIban;

    public AccountNotFoundException(String encodedIban) {
        super("Bank account not found for IBAN: " + encodedIban);
        this.encodedIban = encodedIban;
    }

    public AccountNotFoundException(String message, String encodedIban) {
        super(message);
        this.encodedIban = encodedIban;
    }

    public AccountNotFoundException(String message, Throwable cause, String encodedIban) {
        super(message, cause);
        this.encodedIban = encodedIban;
    }

    public AccountNotFoundException(Throwable cause, String encodedIban) {
        super(cause);
        this.encodedIban = encodedIban;
    }

    public String getEncodedIban() {
        return encodedIban;
    }
}
