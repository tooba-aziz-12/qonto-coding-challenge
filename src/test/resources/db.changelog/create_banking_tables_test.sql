-- liquibase formatted sql

-- changeset tooba:create-bank-accounts
CREATE TABLE bank_accounts (
    id UUID PRIMARY KEY,
    organization_name TEXT,
    balance_cents BIGINT,
    iban TEXT,
    bic TEXT
);

-- changeset tooba:create-transactions
CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    counterparty_name TEXT,
    counterparty_iban TEXT,
    counterparty_bic TEXT,
    amount_cents BIGINT,
    amount_currency TEXT,
    bank_account_id UUID REFERENCES bank_accounts(id),
    description TEXT
);
