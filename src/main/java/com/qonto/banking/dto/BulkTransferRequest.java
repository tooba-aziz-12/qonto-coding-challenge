package com.qonto.banking.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
public class BulkTransferRequest {

    @JsonProperty("organization_iban")
    private String organizationIban;

    @JsonProperty("organization_bic")
    private String organizationBic;

    @JsonProperty("credit_transfers")
    private List<CreditTransfer> creditTransfers;

    public BulkTransferRequest() {
    }

    public BulkTransferRequest(String organizationIban, String organizationBic, List<CreditTransfer> creditTransfers) {
        this.organizationIban = organizationIban;
        this.organizationBic = organizationBic;
        this.creditTransfers = creditTransfers;
    }

    public String getOrganizationIban() {
        return organizationIban;
    }

    public void setOrganizationIban(String organizationIban) {
        this.organizationIban = organizationIban;
    }

    public String getOrganizationBic() {
        return organizationBic;
    }

    public void setOrganizationBic(String organizationBic) {
        this.organizationBic = organizationBic;
    }

    public List<CreditTransfer> getCreditTransfers() {
        return creditTransfers;
    }

    public void setCreditTransfers(List<CreditTransfer> creditTransfers) {
        this.creditTransfers = creditTransfers;
    }
}
