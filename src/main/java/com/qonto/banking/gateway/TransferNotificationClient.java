package com.qonto.banking.gateway;

import com.qonto.banking.dto.TransferResult;
import com.qonto.banking.event.BulkTransferCompletedEvent;
import com.qonto.banking.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferNotificationClient {

    public List<TransferResult> notifyExternalService(List<Transaction> transactions) {
        return transactions.stream().map(transaction -> {
            try{
                // Make 'N' (num of transfers) HTTP call to external system
                return new TransferResult(false, null);
            }catch (Exception ex){
                return new TransferResult(true, ex);
            }
        }).toList();
    }
}
