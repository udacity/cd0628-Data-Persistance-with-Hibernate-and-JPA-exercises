# Exercise: Transactional placeOrder with Audit That Survives Rollback

## Overview

In this exercise, you'll implement a multi-step `placeOrder` service
that saves an order, decrements inventory, and charges a simulated
payment gateway -- all atomically. You'll also add an audit log
call that uses `Propagation.REQUIRES_NEW`, so the audit row
persists even when the main transaction rolls back. The pre-written
test verifies both the happy path and the declined-payment path.


## Before You Start

This exercise connects to a PostgreSQL database named `banking` as the
`banking` user. If you haven't run the one-time workspace setup script
yet, run it from the repository root:

```
bash setup/setup-postgres.sh
```

The script is idempotent and safe to re-run. See `setup/SETUP.md` at
the repository root for details.

## Exercise Instructions

Open the starter project and work through the TODOs in two service
files. The entities, repositories, and payment gateway are already
wired.

### Part 1: AuditLogService - Independent Transaction

Open `AuditLogService.java`. The `recordEvent` method body is
already written; you just need the right annotation so it runs
in a separate transaction from its caller.

**TODO 1: Annotate `recordEvent` with the right transaction propagation**
The method needs to run in a brand-new transaction that commits
independently of the caller. That way, even when the caller rolls
back, the audit row stays.

### Part 2: OrderService - Atomic placeOrder

Open `OrderService.java`. The class is wired with all needed
dependencies.

**TODO 2: Annotate `placeOrder` as transactional**
The whole method should run in a single atomic transaction. If
anything throws, everything rolls back.

**TODO 3: Implement the body of `placeOrder`**
Save the order, decrement inventory, record an audit event for the
payment attempt, then charge the payment gateway. The order of
those last two matters.

## Deliverable

`TransactionTest` passes both scenarios:
- **Happy path:** order saved, inventory decremented, audit row exists
- **Payment declined:** order and inventory rolled back (verify zero
  matching rows for the failed order; verify product inventory back
  to its starting value), but the audit row persists

Run the test with:

```
mvn test
```

Expected output ends with:

```
[INFO] BUILD SUCCESS
```

SQL logs on the failing path show ROLLBACK on the outer transaction
and COMMIT on the inner (audit) transaction.

## What's Included

- `OrderService.java` with TODOs 2-3
- `AuditLogService.java` with TODO 1
- `PaymentGateway.java`, complete (toggleable between APPROVE and
  DECLINE for testing)
- `PaymentDeclinedException.java`, complete
- `Order.java`, `Product.java`, `AuditLog.java` entities, complete
- `OrderRepository.java`, `ProductRepository.java`,
  `AuditLogRepository.java`, complete
- `TransactionTest.java`, pre-written
- `schema.sql` and `data.sql` (products with starting inventory)
- `application.yml` with SQL logging and transaction logging on

## Common Mistakes

- **Importing `@Transactional` from `jakarta.transaction`:** doesn't honor Spring's propagation settings. Use `org.springframework.transaction.annotation.Transactional` instead.
- **Self-invocation bypasses `@Transactional`:** if `placeOrder` calls another method on `this`, Spring's proxy doesn't intercept the inner call. Cross-service calls (OrderService calling AuditLogService) work because they go through the proxy.
- **Catching the exception inside placeOrder instead of letting it propagate:** swallowing `PaymentDeclinedException` means the transaction commits instead of rolling back. Let it propagate.
- **Calling `recordEvent` AFTER payment instead of before:** if you put the audit AFTER the payment call, the audit only happens on the happy path. The whole point is to record the attempt before it can fail.
- **Forgetting `rollbackFor` for checked exceptions:** not needed here because `PaymentDeclinedException` is a `RuntimeException`. But worth knowing - `@Transactional` only rolls back on RuntimeException by default.
