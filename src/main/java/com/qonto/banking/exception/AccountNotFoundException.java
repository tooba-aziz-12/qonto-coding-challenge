package com.qonto.banking.exception;

public class AccountNotFoundException extends RuntimeException {

    private final String traceId;

    public AccountNotFoundException(String traceId) {
        super("Bank account not found for IBAN: " + traceId);
        this.traceId = traceId;
    }

    public AccountNotFoundException(String message, String traceId) {
        super(message);
        this.traceId = traceId;
    }

    public AccountNotFoundException(String message, Throwable cause, String traceId) {
        super(message, cause);
        this.traceId = traceId;
    }

    public AccountNotFoundException(Throwable cause, String traceId) {
        super(cause);
        this.traceId = traceId;
    }

    public String getTraceId() {
        return traceId;
    }
}
