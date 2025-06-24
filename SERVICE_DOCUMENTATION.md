# Bulk Transfer System

## Overview

This project simulates a **banking bulk transfer system** with support for:

- Transactional safety (isolation level: SERIALIZABLE)
- Event-driven architecture to notify external service to deposit money to the receiver's account
- External transfer notification (semantically modeled as an event-based HTTP notifier)
- Retry support for failed notifications (semantically modeled)
- Retry logic through cron (fetch from db and execute)
- High-precision monetary operations 
- Integration and unit test coverage

---

## Assumptions

- The flow of money is outwards always i.e From a qonto account to an external account 
- The money sent out is always a positive value, so a deposit essentially
- There is an external service responsible to deposit the transferred money into the receiver's account
- There is an Http call to notify this external service of a transfer, the response of it will suggest if the deposit succeeded or failed
- A cent is a whole number, no less than 1, anything below it will be rounded up
- A high precision number for Euros like 125.452855 is not standard in real world financial systems , and so these numbers will always we rounded up to 2 decimal places e.g 125.45
- Rounding half-up may cause the sender to transfer 1 cent more e.g "10.005" will be rounded to "10.01" (instead of truncated to "10.00"), aligning with financial norms that prevent systematic underpayment.
- **IMPORTANT NOTE**: The code in this service for the retry flow of failed transfer deposits is a symantic demonstration of how the system would work and not syntactically accurate. This is due to the 5 hour limit of the test.

## Transfer Flow

1. `BulkTransferService` processes the request:
    - Validates sender account and funds
    - Converts euro string amounts to long cents (accurately)
    - Deducts funds and stores all `Transaction` records atomically
    - Emits `BulkTransferCompletedEvent` after a successful operation

2. `BulkTransferCompletedListener` listens for the event `BulkTransferCompletedEvent`:
    - Attempts to notify an external service by making http calls
    - If the external call fails for any transaction:
        - Marks transaction as failed in db

3. `TransactionRetryService`:
    - Fetches failed Transactions from the db
    - Retries to notify external service through http calls
    - If retry fails, trigger alerting mechanism and escalate to the development team (on-call engineers)

4. A **cron job**:
    - Calls `TransactionRetryService.retryAllFailedTransfers()` once daily
    - Reattempts rollback of persisted failures from the DB

---

## Key Components

### `BulkTransferService`
- Accepts `BulkTransferRequest`
- Converts all amounts using `MoneyUtils`
- Verifies sufficient balance
- Persists `Transaction` entries
- Deducts total amount from sender account
- Emits `BulkTransferCompletedEvent`

### `MoneyUtils`
- Converts euro strings (e.g. `"123.456"`) to cents as `long`
- Rounds using `BigDecimal.ROUND_HALF_UP`
- Rounds to exactly **2 decimal places**, which is the standard for euro and most fiat currencies
- Centralized logic ensures consistent money handling

#### Why use `BigDecimal`?

When working with money, **accuracy is critical**. Java’s primitive types like `float` and `double` introduce **floating-point precision errors**, which are unacceptable in financial systems.


#### Why round to 2 decimal places?

Real-world currencies like EUR, USD, GBP, etc., **do not support more than 2 decimal places**. When processing transactions, systems must always ensure amounts like `"5.017"` are safely rounded to `"5.02"` before converting to cents (`502L`).

If not rounded:

- `"5.017" * 100 = 501.7` → `longValueExact()` throws exception
- `"5.017" * 100 = 501.7` → `longValue()` truncates to `501` → **user loses 1 cent**

This could result in:

| Input Amount | Without Rounding → Truncated | With Rounding → Proper |
|--------------|------------------------------|-------------------------|
| `"10.005"`   | 1000                         | 1001                   |
| `"3.999"`    | 399                          | 400                    |
| `"0.001"`    | 0                            | 0                      |

Rounding with `BigDecimal.ROUND_HALF_UP` follows the common financial rule of:

> Round up if the next digit is 5 or more, else round down.

This mirrors how banks and credit card companies round amounts on invoices or statements.

## Test Strategy

- Integration tests using `@SpringBootTest` and `MockMvc`
- Precision handling tests for high-value decimal input
- Failure simulation tests for insufficient funds and missing accounts
- Placeholder: Test cases for rollback and retry logic (pending implementation)

## Future Improvements 
- Idempotency should be enforced on the /transfers/bulk endpoint to prevent accidental double transfers in the event of retries or network glitches
- Monitory should be implemented to visualise incident patterns and quick finding of issues
- Rate limiting should be added to protect against abuse or accidental system overload
- Authentication and authorisation should be implemented
- Message queues (e.g., Kafka, RabbitMQ) could be introduced for decoupling the transfer processor and the external notification system, increasing resilience
- Timeouts and circuit breakers should be implemented for external service calls using a library like Resilience4j
- Dockerization can be improved, all dependencies should be added to the same script with the project run mechanism enabled for more production readiness
- The Service can benefit from clean architecture principles as the code base grows. DDD could help make code more scalable for this service
- There should be a robust alerting strategy implemented on this due to the criticality of the feature i.e money movement
- Currency support could be expanded to include multi-currency transfers with conversion logic

# Project Setup Guide

## 1. Start MySQL with Docker

Ensure Docker is installed. Then run:

```bash
docker compose up -d
```

## 2. Run the App

Open the project in IntelliJ (or any Java IDE)

Make sure you have Java 21

**Run BulkTransferApplication.java**

The app will auto-connect to MySQL and generate schema.