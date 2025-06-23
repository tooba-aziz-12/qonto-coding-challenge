package com.qonto.banking.event.inbound;

import com.qonto.banking.dto.TransferResult;
import com.qonto.banking.gateway.TransferNotificationClient;
import com.qonto.banking.event.BulkTransferCompletedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BulkTransferCompletedListener {

    @Autowired
    private TransferNotificationClient notifier;

    @EventListener
    public void onBulkTransferCompleted(BulkTransferCompletedEvent event) {
        try {
            List<TransferResult> transferResultList = notifier.notifyExternalService(event.getTransactions());
            transferResultList.stream().filter(it -> it.getHasError().equals(true)).forEach(transferResult -> {
                //mark as fail in db
            });

        }catch (Exception ex){
            //log error
            // throw custom exception
        }

    }
}
