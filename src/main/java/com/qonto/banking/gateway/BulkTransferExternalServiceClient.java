package com.qonto.banking.gateway;

import com.qonto.banking.dto.TransferResultExternal;
import com.qonto.banking.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BulkTransferExternalServiceClient {

    public List<TransferResultExternal> notifyExternalService(List<Transaction> transactions) {
        return transactions.stream().map(transaction -> {
            try{
                // Make 'N' (num of transfers) HTTP call to external system
                return new TransferResultExternal(false, null);
            }catch (Exception ex){
                return new TransferResultExternal(true, ex);
            }
        }).toList();
    }
}
