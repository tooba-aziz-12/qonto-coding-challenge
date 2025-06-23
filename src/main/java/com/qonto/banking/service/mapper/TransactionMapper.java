package com.qonto.banking.service.mapper;

import com.qonto.banking.dto.CreditTransfer;
import com.qonto.banking.model.BankAccount;
import com.qonto.banking.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class TransactionMapper {

    public static Transaction mapToTransaction(CreditTransfer creditTransfer, BankAccount sender) {
        return new Transaction(
                UUID.randomUUID(),
                creditTransfer.getCounterpartyName(),
                creditTransfer.getCounterpartyIban(),
                creditTransfer.getCounterpartyBic(),
                new BigDecimal(creditTransfer.getAmount())
                        .setScale(2, BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .longValueExact(),
                "EUR",
                creditTransfer.getDescription(),
                sender
        );
    }

    public static List<Transaction> mapToTransactions(List<CreditTransfer> creditTransfers, BankAccount sender) {
        return creditTransfers.stream()
                .map(ct -> mapToTransaction(ct, sender))
                .toList();
    }
}
