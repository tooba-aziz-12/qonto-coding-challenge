package com.qonto.banking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {
    public Transaction() {
    }

    public Transaction(UUID id, String counterpartyName, String counterpartyIban, String counterpartyBic, Long amountCents, String amountCurrency, String description, BankAccount bankAccount) {
        this.id = id;
        this.counterpartyName = counterpartyName;
        this.counterpartyIban = counterpartyIban;
        this.counterpartyBic = counterpartyBic;
        this.amountCents = amountCents;
        this.amountCurrency = amountCurrency;
        this.description = description;
        this.bankAccount = bankAccount;
    }

    @Id
    @Column(name = "id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "counterparty_name")
    private String counterpartyName;

    @Column(name = "counterparty_iban")
    private String counterpartyIban;

    @Column(name = "counterparty_bic")
    private String counterpartyBic;

    @Column(name = "amount_cents")
    private Long amountCents;

    @Column(name = "amount_currency")
    private String amountCurrency;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    // transactionStatus -> PENDING/SUCCESSFUL/ERROR


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Long getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(Long amountCents) {
        this.amountCents = amountCents;
    }

    public String getAmountCurrency() {
        return amountCurrency;
    }

    public void setAmountCurrency(String amountCurrency) {
        this.amountCurrency = amountCurrency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
}
