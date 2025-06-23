package com.qonto.banking.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bank_accounts")
public class BankAccount {

    public BankAccount() {
    }

    public BankAccount(String id, String organizationName, long balanceCents, String iban, String bic) {
        this.id = id;
        this.organizationName = organizationName;
        this.balanceCents = balanceCents;
        this.iban = iban;
        this.bic = bic;
    }
    public BankAccount(String organizationName, long balanceCents, String iban, String bic) {
        this.id = UUID.randomUUID().toString();
        this.organizationName = organizationName;
        this.balanceCents = balanceCents;
        this.iban = iban;
        this.bic = bic;
    }
    @Id
    @Column(name = "id", length = 36, updatable = false, nullable = false)
    private String id = UUID.randomUUID().toString();

    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "balance_cents")
    private Long balanceCents;

    @Column(name = "iban")
    private String iban;
    @Column(name = "bic")
    private String bic;

    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Long getBalanceCents() {
        return balanceCents;
    }

    public void setBalanceCents(Long balanceCents) {
        this.balanceCents = balanceCents;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
