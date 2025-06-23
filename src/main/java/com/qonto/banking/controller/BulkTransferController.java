package com.qonto.banking.controller;

import com.qonto.banking.dto.BulkTransferRequest;
import com.qonto.banking.service.BulkTransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BulkTransferController {

    private final BulkTransferService bulkTransferService;

    public BulkTransferController(BulkTransferService bulkTransferService) {
        this.bulkTransferService = bulkTransferService;
    }

    @PostMapping("/bulk-transfers")
    public ResponseEntity<?> handleBulkTransfer(@Valid @RequestBody BulkTransferRequest request) {
        bulkTransferService.process(request);
        return ResponseEntity.status(201).build();
    }
}
