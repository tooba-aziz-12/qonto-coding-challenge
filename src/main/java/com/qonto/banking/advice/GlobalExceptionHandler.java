package com.qonto.banking.advice;

import com.qonto.banking.exception.AccountNotFoundException;
import com.qonto.banking.exception.BulkTransferFailedException;
import com.qonto.banking.exception.InsufficientFundsException;
import com.qonto.banking.exception.InvalidAmountException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFound(AccountNotFoundException ex) {
        log.warn("Account not found for encoded IBAN={}: {}", ex.getEncodedIban(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFunds(InsufficientFundsException ex) {
        log.warn("Insufficient funds for encoded IBAN={}: {}", ex.getEncodedIban(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }

    @ExceptionHandler(BulkTransferFailedException.class)
    public ResponseEntity<String> handleBulkTransferFailed(BulkTransferFailedException ex) {
        log.error("Unexpected error during bulk transfer for encoded IBAN={}: {}", ex.getEncodedIban(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Bulk transfer failed. Please try again later.");
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<String> handleInvalidAmount(InvalidAmountException ex) {
        log.error("Invalid amount found in transfer IBAN={}: {}", ex.getEncodedIban(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body("Bulk transfer failed. Please try again later.");
    }
}
