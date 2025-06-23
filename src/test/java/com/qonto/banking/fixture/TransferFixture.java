package com.qonto.banking.fixture;

import com.qonto.banking.dto.BulkTransferRequest;
import com.qonto.banking.dto.CreditTransfer;

import java.util.List;

public final class TransferFixture {

    private TransferFixture() {
    }
        public static BulkTransferRequest transferRequest() {
            return new BulkTransferRequest(
                    "IBAN1",
                    "BIC1",
                    List.of(
                            new CreditTransfer("Bip Bip", "IBAN2", "BIC2", "5.00", "Salary"),
                            new CreditTransfer("Bip Bip", "IBAN2", "BIC2", "3.00", "Bonus")
                    )
            );
        }

        public static BulkTransferRequest highPrecisionRequest() {
            return new BulkTransferRequest(
                    "IBAN_BIG",
                    "BIC_BIG",
                    List.of(
                            new CreditTransfer("Vendor A", "IBAN_VA", "BIC_VA", "1234.56789", "Invoice A"),
                            new CreditTransfer("Vendor B", "IBAN_VB", "BIC_VB", "9876.54321", "Invoice B")
                    )
            );
        }
}
