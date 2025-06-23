package com.qonto.banking.exception;

public class InsufficientFundsException extends RuntimeException {

    private final String traceId;

    public InsufficientFundsException(String traceId) {
        super("Insufficient funds to complete the transfer request for IBAN: " + traceId);
        this.traceId = traceId;
    }

    public InsufficientFundsException(String message, String traceId) {
        super(message);
        this.traceId = traceId;
    }

    public InsufficientFundsException(String message, Throwable cause, String traceId) {
        super(message, cause);
        this.traceId = traceId;
    }

    public InsufficientFundsException(Throwable cause, String traceId) {
        super(cause);
        this.traceId = traceId;
    }

    public String getTraceId() {
        return traceId;
    }
}

