package com.qonto.banking.dto;

public class TransferResult{
    Boolean hasError;
    Throwable exception;

    public TransferResult(Boolean hasError, Throwable exception) {
        this.hasError = hasError;
        this.exception = exception;
    }

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }
}