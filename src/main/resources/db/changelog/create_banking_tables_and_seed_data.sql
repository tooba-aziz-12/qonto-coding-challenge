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
('11111111-1111-1111-1111-111111111111', 'ACME Corp', 10000000, 'FR10474608000002006107XXXXX', 'OIVUSCLQXXX');

-- changeset tooba:insert-transactions
INSERT INTO transactions (id, counterparty_name, counterparty_iban, counterparty_bic, amount_cents, amount_currency, bank_account_id, description) VALUES
('22222222-2222-2222-2222-222222222222', 'ACME Corp. Main Account', 'EE382200221020145685', 'CCOPFRPPXXX', 11000000, 'EUR', '11111111-1111-1111-1111-111111111111', 'Treasury income'),
('33333333-3333-3333-3333-333333333333', 'Bip Bip', 'EE383680981021245685', 'CRLYFRPPTOU', -1000000, 'EUR', '11111111-1111-1111-1111-111111111111', 'Bip Bip Salary');
