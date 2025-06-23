package com.qonto.banking.event.inbound;

import com.qonto.banking.dto.TransferResultExternal;
import com.qonto.banking.gateway.BulkTransferExternalServiceClient;
import com.qonto.banking.event.BulkTransferCompletedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BulkTransferCompletedListener {

    @Autowired
    private BulkTransferExternalServiceClient notifier;

    @EventListener
    public void onBulkTransferCompleted(BulkTransferCompletedEvent event) {
        try {
            List<TransferResultExternal> transferResultExternalList = notifier.notifyExternalService(event.getTransactions());
            transferResultExternalList.stream().filter(it -> it.getHasError().equals(true)).forEach(transferResultExternal -> {
                //mark as failed in db
            });

        }catch (Exception ex){
            //log error
            // throw custom exception
        }

    }
}
