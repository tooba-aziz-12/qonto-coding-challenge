-- liquibase formatted sql

-- changeset tooba:create-bank-accounts
CREATE TABLE bank_accounts (
    id CHAR(36) PRIMARY KEY,
    organization_name TEXT,
    balance_cents BIGINT,
    iban TEXT,
    bic TEXT
);

-- changeset tooba:create-transactions
CREATE TABLE transactions (
    id CHAR(36) PRIMARY KEY,
    counterparty_name TEXT,
    counterparty_iban TEXT,
    counterparty_bic TEXT,
    amount_cents BIGINT,
    amount_currency TEXT,
    bank_account_id CHAR(36),
    description TEXT,
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id)
);

-- changeset tooba:insert-bank-accounts
INSERT INTO bank_accounts (id, organization_name, balance_cents, iban, bic) VALUES
('7f3b9e62-4d8e-4c98-b35e-2f89a7d44fae', 'ACME Corp', 10000000, 'FR10474608000002006107XXXXX', 'OIVUSCLQXXX');

-- changeset tooba:insert-transactions
INSERT INTO transactions (id, counterparty_name, counterparty_iban, counterparty_bic, amount_cents, amount_currency, bank_account_id, description) VALUES
('a6d2b4c1-9a64-4e7f-b65d-88f8e601d94c', 'ACME Corp. Main Account', 'EE382200221020145685', 'CCOPFRPPXXX', 11000000, 'EUR', '7f3b9e62-4d8e-4c98-b35e-2f89a7d44fae', 'Treasury income'),
('c1fa3d77-5e0a-4bd4-92fb-f2be95f65e3f', 'Bip Bip', 'EE383680981021245685', 'CRLYFRPPTOU', 1000000, 'EUR', '7f3b9e62-4d8e-4c98-b35e-2f89a7d44fae', 'Bip Bip Salary');
