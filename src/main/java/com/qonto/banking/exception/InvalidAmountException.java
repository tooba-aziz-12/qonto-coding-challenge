package com.qonto.banking.exception;


public class InvalidAmountException extends RuntimeException {
    private final String traceId;

    public InvalidAmountException(String message, String traceId, Throwable cause) {
        super(message, cause);
        this.traceId = traceId;
    }

    public String getTraceId() {
        return traceId;
    }
}

