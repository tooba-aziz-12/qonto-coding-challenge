package com.qonto.banking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qonto.banking.fixture.TransferFixture;
import com.qonto.banking.model.BankAccount;
import com.qonto.banking.model.Transaction;
import com.qonto.banking.repository.BankAccountRepository;
import com.qonto.banking.repository.TransactionRepository;
import com.qonto.banking.service.util.MoneyUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static com.qonto.banking.fixture.TransferFixture.highPrecisionRequest;
import static com.qonto.banking.fixture.TransferFixture.transferRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BulkTransferIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        transactionRepository.deleteAll();
        bankAccountRepository.deleteAll();

        bankAccountRepository.save(new BankAccount("ACME Corp", 1000L, "IBAN1", "BIC1"));
        bankAccountRepository.save(new BankAccount("Bip Bip", 1000L, "IBAN2", "BIC2"));
    }

    @Test
    void shouldProcessBulkTransferAndUpdateBalance() throws Exception {
        mockMvc.perform(
                post("/api/bulk-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest()))
        ).andExpect(status().isCreated());

        BankAccount sender = bankAccountRepository.findByIbanAndBic("IBAN1", "BIC1").orElseThrow();
        BankAccount receiver = bankAccountRepository.findByIbanAndBic("IBAN2", "BIC2").orElseThrow();

        // 1000 - (5.00 + 3.00) = 200
        assertEquals(200L, sender.getBalanceCents(), "Sender balance should be reduced after bulk transfer");

        List<Transaction> transactions = transactionRepository.findAll();
        assertEquals(2, transactions.size(), "Two transactions should be created");

        assertTrue(transactions.stream().allMatch(txn ->
                txn.getCounterpartyIban().equals("IBAN2") &&
                        txn.getCounterpartyName().equals("Bip Bip")
        ));
    }

    @Test
    void shouldReturnErrorWhenInsufficientFunds() throws Exception {
        BankAccount sender = bankAccountRepository.findByIbanAndBic("IBAN1", "BIC1").orElseThrow();
        sender.setBalanceCents(100L); // less than needed (800)
        bankAccountRepository.save(sender);

        mockMvc.perform(
                post("/api/bulk-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest()))
        ).andExpect(status().isUnprocessableEntity());

        BankAccount updatedSender = bankAccountRepository.findByIbanAndBic("IBAN1", "BIC1").orElseThrow();
        assertEquals(100L, updatedSender.getBalanceCents(), "Balance should not change on failure");

        List<Transaction> transactions = transactionRepository.findAll();
        assertEquals(0, transactions.size(), "No transactions should be created");
    }

    @Test
    void shouldReturnErrorWhenSenderAccountNotFound() throws Exception {
        bankAccountRepository.deleteAll();

        mockMvc.perform(
                post("/api/bulk-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest()))
        ).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnErrorWhenInvalidRequestProvided() throws Exception {
        String malformedRequest = """
            {
                "organization_iban": "IBAN1",
                "organizationBic": "BIC1",
                "creditTransfers": [
                    { "amount": "12.345.67", "counterpartyName": "Oops", "counterpartyIban": "IBAN2" }
                ]
            }
        """;

        mockMvc.perform(
                post("/api/bulk-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedRequest)
        ).andExpect(status().isNotFound());
    }

    @Test
    void shouldNotLoseCentsWithHighPrecisionValues() throws Exception {
        long totalBalanceInCents = 2_000_000L;

        bankAccountRepository.deleteAll();
        transactionRepository.deleteAll();

        bankAccountRepository.save(new BankAccount("High Roller", totalBalanceInCents, "IBAN_BIG", "BIC_BIG"));

        mockMvc.perform(
                post("/api/bulk-transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(highPrecisionRequest()))
        ).andExpect(status().isCreated());

        BankAccount sender = bankAccountRepository.findByIbanAndBic("IBAN_BIG", "BIC_BIG").orElseThrow();

        long expectedCentsA = MoneyUtils.eurosToCents("1234.56789");
        long expectedCentsB = MoneyUtils.eurosToCents("9876.54321");

        long totalExpectedCents = expectedCentsA + expectedCentsB;
        long expectedRemainingBalance = totalBalanceInCents - totalExpectedCents;

        assertEquals(expectedRemainingBalance, sender.getBalanceCents(), "No cents should be lost on large decimal transfer");

        List<Transaction> transactions = transactionRepository.findAll();
        long totalStoredCents = transactions.stream().mapToLong(Transaction::getAmountCents).sum();

        assertEquals(totalExpectedCents, totalStoredCents, "Stored transaction amounts must exactly match deduction");
    }

}
