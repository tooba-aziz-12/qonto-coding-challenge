package com.qonto.banking.service;

import com.qonto.banking.dto.BulkTransferRequest;
import com.qonto.banking.exception.AccountNotFoundException;
import com.qonto.banking.exception.BulkTransferFailedException;
import com.qonto.banking.exception.InsufficientFundsException;
import com.qonto.banking.service.mapper.TransactionMapper;
import com.qonto.banking.model.BankAccount;
import com.qonto.banking.model.Transaction;
import com.qonto.banking.repository.BankAccountRepository;
import com.qonto.banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
public class BulkTransferService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public BulkTransferService(BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void process(BulkTransferRequest request) {
        String encodedIban = encodeIban(request.getOrganizationIban());
        try {
            BankAccount sender = bankAccountRepository
                    .findByIbanAndBic(request.getOrganizationIban(), request.getOrganizationBic())
                    .orElseThrow(() -> new AccountNotFoundException(encodedIban));

            System.out.println("=== Bulk Transfer Debug ===");

            System.out.println("Sender balance before: " + sender.getBalanceCents());

            Long totalCents = request.getCreditTransfers().stream()
                    .map(t -> {
                        BigDecimal cents =   new BigDecimal(t.getAmount()).multiply(BigDecimal.valueOf(100));

                        System.out.println("============== Euro to Cents ===========");
                        System.out.println(cents);
                        return cents;
                        }
                    )
                    .mapToLong(BigDecimal::longValue)
                    .sum();

            System.out.println("Total cents to deduct: " + totalCents);


            if (sender.getBalanceCents() < totalCents) {
                throw new InsufficientFundsException(encodedIban);
            }

            List<Transaction> txns = TransactionMapper.mapToTransactions(request.getCreditTransfers(), sender);
            transactionRepository.saveAll(txns);

            sender.setBalanceCents(sender.getBalanceCents() - totalCents);
            bankAccountRepository.save(sender);
            System.out.println("Sender balance after: " + sender.getBalanceCents());

        } catch (AccountNotFoundException | InsufficientFundsException e) {
            throw e;
        } catch (Exception e) {
            throw new BulkTransferFailedException("An unexpected error occurred while processing the bulk transfer", encodedIban, e);
        }
    }

    private String encodeIban(String iban) {
        if (iban == null) return "null";
        return Base64.getEncoder().encodeToString(iban.getBytes(StandardCharsets.UTF_8));
    }
}
