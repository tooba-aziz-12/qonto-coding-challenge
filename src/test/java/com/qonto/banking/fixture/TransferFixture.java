package com.qonto.banking.fixture;

public final class TransferFixture {

    private TransferFixture() {
    }

    public static String transferRequest = """
    {
      "organization_bic": "BIC1",
      "organization_iban": "IBAN1",
      "credit_transfers": [
        {
          "counterparty_name": "Bip Bip",
          "counterparty_iban": "IBAN2",
          "counterparty_bic": "BIC2",
          "amount": "5.00",
          "description": "Salary"
        },
        {
          "counterparty_name": "Bip Bip",
          "counterparty_iban": "IBAN2",
          "counterparty_bic": "BIC2",
          "amount": "3.00",
          "description": "Bonus"
        }
      ]
    }
    """;

    public static String highPrecisionRequest = """
        {
          "organization_bic": "BIC_BIG",
          "organization_iban": "IBAN_BIG",
          "credit_transfers": [
            {
              "counterparty_name": "Vendor A",
              "counterparty_iban": "IBAN_VA",
              "counterparty_bic": "BIC_VA",
              "amount": "1234.56789",
              "description": "Invoice A"
            },
            {
              "counterparty_name": "Vendor B",
              "counterparty_iban": "IBAN_VB",
              "counterparty_bic": "BIC_VB",
              "amount": "9876.54321",
              "description": "Invoice B"
            }
          ]
        }
        """;
}
