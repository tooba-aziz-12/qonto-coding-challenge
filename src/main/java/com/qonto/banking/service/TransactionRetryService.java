package com.qonto.banking.service;

import com.qonto.banking.dto.TransferResultExternal;
import com.qonto.banking.gateway.BulkTransferExternalServiceClient;
import com.qonto.banking.model.Transaction;
import com.qonto.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionRetryService {


    TransactionRepository transactionRepository;

    BulkTransferExternalServiceClient notifier;

    public TransactionRetryService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    //This method is to be called by the cron
    public void retryAllFailedTransfers() {
        List<Transaction> failedTransactions = transactionRepository.fetchFailedTransactions();

        List<TransferResultExternal> transferResultExternals = notifier.notifyExternalService(failedTransactions);

        transferResultExternals.stream()
                .filter(it -> it.getHasError().equals(true))
                .forEach(transferResultExternal -> {
            // Retry failed -> Alerting mechanism should trigger to escalate to the dev team
        });
    }

}
