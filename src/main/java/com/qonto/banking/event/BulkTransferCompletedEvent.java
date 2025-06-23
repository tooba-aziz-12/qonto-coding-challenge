package com.qonto.banking.event;

import com.qonto.banking.model.Transaction;

import java.util.List;

public class BulkTransferCompletedEvent {

    private final String transferBatchId;
    private final List<Transaction> transactions;

    public BulkTransferCompletedEvent(String transferBatchId, List<Transaction> transactions) {
        this.transferBatchId = transferBatchId;
        this.transactions = transactions;
    }

    public String getTransferBatchId() {
        return transferBatchId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
