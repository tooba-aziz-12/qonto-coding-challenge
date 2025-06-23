package com.qonto.banking.exception;
public class BulkTransferFailedException extends RuntimeException {
    private final String traceId;

    public BulkTransferFailedException(String message, String traceId, Throwable cause) {
        super(message, cause);
        this.traceId = traceId;
    }

    public String getTraceId() {
        return traceId;
    }
}
