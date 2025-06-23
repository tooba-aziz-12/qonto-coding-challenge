package com.qonto.banking.service;

import com.qonto.banking.dto.TransferResult;
import com.qonto.banking.gateway.TransferNotificationClient;
import com.qonto.banking.model.Transaction;
import com.qonto.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionRetryService {


    TransactionRepository transactionRepository;

    TransferNotificationClient notifier;

    public TransactionRetryService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    //This method is to be called by the cron
    public void retryAllFailedTransfers() {
       /* List<Transaction> failedTransactions = transactionRepository.fetchFailedTransactions();

        List<TransferResult> transferResults = notifier.notifyExternalService(failedTransactions);

        transferResults.stream()
                .filter(it -> it.getHasError().equals(true))
                .forEach(transferResult -> {
            // Retry failed -> Alerting mechanism should trigger to escalate to the dev team
        });*/
    }

}
