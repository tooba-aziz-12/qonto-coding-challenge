package com.qonto.banking.controller;

import com.qonto.banking.dto.BulkTransferRequest;
import com.qonto.banking.service.BulkTransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
public class BulkTransferController {

    private final BulkTransferService bulkTransferService;

    public BulkTransferController(BulkTransferService bulkTransferService) {
        this.bulkTransferService = bulkTransferService;
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> handleBulkTransfer(
            @Valid @RequestBody BulkTransferRequest request,
            @RequestHeader("trace-id") String traceId
    ) {
        bulkTransferService.process(request, traceId);
        return ResponseEntity.status(201).build();
    }
}
