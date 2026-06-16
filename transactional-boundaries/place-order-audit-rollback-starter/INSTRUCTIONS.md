# Exercise: Transactional placeOrder with Audit That Survives Rollback

## Overview

A multi-step `placeOrder` service method that saves an order, decrements
inventory, and charges a (simulated) payment gateway — all atomic. Plus
an audit log call that uses `Propagation.REQUIRES_NEW` so audit rows
persist even when the main transaction rolls back. A pre-written test
verifies both the happy path and the rollback-with-surviving-audit
scenario.

## Exercise Instructions

Open the starter project and work through the TODOs in two service
files. The entities, repositories, and payment gateway are already
wired.

### Part 1: AuditLogService — Independent Transaction

Open `AuditLogService.java`. The `recordEvent` method body is already
written. You just need the right annotation so it runs in a separate
transaction from its caller.

**TODO 1: Annotate `recordEvent` with `@Transactional(propagation = Propagation.REQUIRES_NEW)`**
`REQUIRES_NEW` suspends any existing transaction and starts a brand-new
one for this method. That new transaction commits independently — so
even when the caller's transaction rolls back, the audit row stays.

### Part 2: OrderService — Atomic placeOrder

Open `OrderService.java`. The class is wired with all needed
dependencies (`OrderRepository`, `ProductRepository`,
`AuditLogService`, `PaymentGateway`).

**TODO 2: Annotate `placeOrder` with `@Transactional`**
The whole method runs in a single atomic transaction. If anything
throws, everything rolls back.

**TODO 3: Implement the body of `placeOrder`**
Perform these steps in order:
1. Save the new `Order` using `orderRepository.save(...)`
2. Find the `Product` by id and decrement its `inventory` by the order
   quantity, then save it
3. Call `auditLogService.recordEvent(...)` with a description like
   `"Attempting payment for order " + orderId` — this MUST happen BEFORE
   payment is attempted so the audit row exists if payment fails
4. Call `paymentGateway.charge(order)` — this throws
   `PaymentDeclinedException` (a RuntimeException) if the simulated
   gateway returns DECLINE

If `charge` throws, the `@Transactional` annotation rolls back the
order save and the inventory decrement automatically. Because
`recordEvent` ran in REQUIRES_NEW, its row stays committed.

## Deliverable

`TransactionTest` passes both scenarios:
- **Happy path:** order saved, inventory decremented, audit row exists
- **Payment declined:** order and inventory rolled back (verify zero
  matching rows for the failed order; verify product inventory back to
  its starting value), but the audit row persists

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

- **Importing `@Transactional` from `jakarta.transaction`:** doesn't honor Spring's propagation settings. Use `org.springframework.transaction.annotation.Transactional` instead
- **Self-invocation bypasses `@Transactional`:** if `placeOrder` calls another method on `this`, Spring's proxy doesn't intercept the inner call, and that method's `@Transactional` is ignored. Cross-service calls (OrderService calling AuditLogService) work fine because they go through the proxy
- **Catching the exception inside placeOrder instead of letting it propagate:** swallowing `PaymentDeclinedException` means the transaction commits instead of rolling back. Let it propagate
- **Calling `recordEvent` AFTER payment instead of before:** if you put the audit AFTER the payment call, the audit only happens on the happy path. The whole point is to record the attempt before it can fail
- **Forgetting `rollbackFor` for checked exceptions:** not needed here because `PaymentDeclinedException` is a `RuntimeException`. But worth knowing — `@Transactional` only rolls back on RuntimeException by default. For checked exceptions, you need `@Transactional(rollbackFor = ...)`