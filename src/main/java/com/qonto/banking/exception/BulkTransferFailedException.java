package com.qonto.banking.exception;
public class BulkTransferFailedException extends RuntimeException {
    private final String encodedIban;

    public BulkTransferFailedException(String message, String encodedIban, Throwable cause) {
        super(message, cause);
        this.encodedIban = encodedIban;
    }

    public String getEncodedIban() {
        return encodedIban;
    }
}
