package com.qonto.banking.advice;

import com.qonto.banking.exception.AccountNotFoundException;
import com.qonto.banking.exception.BulkTransferFailedException;
import com.qonto.banking.exception.InsufficientFundsException;
import com.qonto.banking.exception.InvalidAmountException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerUnitTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleAccountNotFound() {
        AccountNotFoundException ex = new AccountNotFoundException("encoded-iban-123");
        ResponseEntity<String> response = handler.handleAccountNotFound(ex);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Bank account not found for IBAN: encoded-iban-123", response.getBody());
    }

    @Test
    void testHandleInsufficientFunds() {
        InsufficientFundsException ex = new InsufficientFundsException("encoded-iban-456");
        ResponseEntity<String> response = handler.handleInsufficientFunds(ex);

        assertEquals(422, response.getStatusCodeValue());
        assertEquals("Insufficient funds to complete the transfer request for IBAN: encoded-iban-456", response.getBody());
    }

    @Test
    void testHandleInvalidAmount() {
        InvalidAmountException ex = new InvalidAmountException("bad input", "encoded-iban-789", new ArithmeticException());
        ResponseEntity<String> response = handler.handleInvalidAmount(ex);

        assertEquals(422, response.getStatusCodeValue());
        assertEquals("Bulk transfer failed. Please try again later.", response.getBody());
    }

    @Test
    void testHandleBulkTransferFailed() {
        BulkTransferFailedException ex = new BulkTransferFailedException("something broke", "encoded-iban-999", new RuntimeException());
        ResponseEntity<String> response = handler.handleBulkTransferFailed(ex);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Bulk transfer failed. Please try again later.", response.getBody());
    }
}
