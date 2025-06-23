package com.qonto.banking.cron;

import com.qonto.banking.service.TransactionRetryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StaleTransferCleanupJob {

    TransactionRetryService transactionRetryService;

    public StaleTransferCleanupJob(TransactionRetryService transactionRetryService) {
        this.transactionRetryService = transactionRetryService;
    }

    @Scheduled(cron = "0 0 3 * * *") // every day at 3 AM
    public void attemptFinalReverts() {
        // Read stale failed rollbacks from DB/queue
        // Try reverting again

        transactionRetryService.retryAllFailedTransfers();
    }
}
