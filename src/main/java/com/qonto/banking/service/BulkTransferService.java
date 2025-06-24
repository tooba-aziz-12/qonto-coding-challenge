package com.qonto.banking.service;

import com.qonto.banking.dto.BulkTransferRequest;
import com.qonto.banking.event.BulkTransferCompletedEvent;
import com.qonto.banking.exception.AccountNotFoundException;
import com.qonto.banking.exception.BulkTransferFailedException;
import com.qonto.banking.exception.InsufficientFundsException;
import com.qonto.banking.exception.InvalidAmountException;
import com.qonto.banking.model.BankAccount;
import com.qonto.banking.model.Transaction;
import com.qonto.banking.repository.BankAccountRepository;
import com.qonto.banking.repository.TransactionRepository;
import com.qonto.banking.service.mapper.TransactionMapper;
import com.qonto.banking.service.util.MoneyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BulkTransferService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    //private final ApplicationEventPublisher eventPublisher;


    @Autowired
    public BulkTransferService(BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void process(BulkTransferRequest request, String traceId) {
        try {
            BankAccount sender = bankAccountRepository
                    .findByIbanAndBic(request.getOrganizationIban(), request.getOrganizationBic())
                    .orElseThrow(() -> new AccountNotFoundException(traceId));

            Long totalCents = request.getCreditTransfers().stream()
                    .map(t -> MoneyUtils.eurosToCents(t.getAmount()))
                    .mapToLong(Long::longValue)
                    .sum();

            if (sender.getBalanceCents() < totalCents) {
                throw new InsufficientFundsException(traceId);
            }

            List<Transaction> transactions = TransactionMapper.mapToTransactions(request.getCreditTransfers(), sender);
            transactionRepository.saveAll(transactions);

            sender.setBalanceCents(sender.getBalanceCents() - totalCents);
            bankAccountRepository.save(sender);

            // After save, emit event for async delivery
            // The listener will:
            // 1. Attempt to notify external system of transfer
            // 2. If notification fails → enqueue (db) for retry the failing ones
            // 3. A scheduled cron job runs daily to reattempt these failed transfers from the DB
            // 4. If retry fails , alerting mechanism to escalate to dev team.

            /* eventPublisher.publishEvent(new BulkTransferCompletedEvent(
                    this,
                    generateTransferBatchId(), // Semantic placeholder
                    transactions
            ));*/

        } catch (ArithmeticException | NumberFormatException e) {
            throw new InvalidAmountException("Invalid amount provided in transfer request", traceId, e);
        } catch (AccountNotFoundException | InsufficientFundsException e) {
            throw e;
        } catch (Exception e) {
            throw new BulkTransferFailedException("An unexpected error occurred while processing the bulk transfer", traceId, e);
        }
    }
}
