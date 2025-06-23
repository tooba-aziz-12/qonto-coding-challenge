package com.qonto.banking.service.event;

import com.qonto.banking.event.BulkTransferCompletedEvent;
import com.qonto.banking.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BulkTransferEventPublisher {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public void publishTransferCompleted(List<Transaction> transactions) {
        // Create and fire event after saving all transactions
        String batchId = UUID.randomUUID().toString();
        eventPublisher.publishEvent(new BulkTransferCompletedEvent(batchId, transactions));
    }
}
