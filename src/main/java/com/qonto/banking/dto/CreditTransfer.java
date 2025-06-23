package com.qonto.banking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public class CreditTransfer {

    @NotBlank
    @JsonProperty("counterparty_name")
    private String counterpartyName;

    @NotBlank
    @JsonProperty("counterparty_iban")
    private String counterpartyIban;

    @NotBlank
    @JsonProperty("counterparty_bic")
    private String counterpartyBic;

    @DecimalMin(value = "0.01", inclusive = true)
    @JsonProperty("amount")
    private String amount;

    @JsonProperty("description")
    private String description;

    public CreditTransfer() {
    }

    public CreditTransfer(String counterpartyName, String counterpartyIban, String counterpartyBic, String amount, String description) {
        this.counterpartyName = counterpartyName;
        this.counterpartyIban = counterpartyIban;
        this.counterpartyBic = counterpartyBic;
        this.amount = amount;
        this.description = description;
    }

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public String getCounterpartyIban() {
        return counterpartyIban;
    }

    public void setCounterpartyIban(String counterpartyIban) {
        this.counterpartyIban = counterpartyIban;
    }

    public String getCounterpartyBic() {
        return counterpartyBic;
    }

    public void setCounterpartyBic(String counterpartyBic) {
        this.counterpartyBic = counterpartyBic;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
