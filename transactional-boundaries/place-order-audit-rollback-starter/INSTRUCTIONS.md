# Exercise: Transactional placeOrder with Audit That Survives Rollback

## What You'll Build

A multi-step placeOrder service method that creates an order, decrements
inventory, and (simulated) charges a payment gateway — all atomic. Plus
an audit log call that uses REQUIRES_NEW so audit rows persist even
when the main transaction rolls back.

## Requirements

- OrderService.placeOrder is @Transactional and performs:
  1. Save the new Order
  2. Decrement the matching Product's inventory
  3. Call AuditLogService.recordEvent BEFORE attempting payment
  4. Call PaymentGateway.charge (provided — throws
     PaymentDeclinedException on simulated failure)
- AuditLogService.recordEvent is annotated
  @Transactional(propagation = Propagation.REQUIRES_NEW)
- The pre-written TransactionTest verifies:
  - Happy path: order saved, inventory decremented, audit row exists
  - PaymentDeclinedException: order and inventory rolled back, but
    audit row still exists

## Starter Code

- PostgreSQL with orders, products, audit_log tables seeded
- Order, Product, AuditLog entities (complete)
- OrderService with placeOrder method stub
- AuditLogService with recordEvent method stub (logic complete, just
  needs the right annotation)
- PaymentGateway provided — toggleable between success and decline
- TransactionTest pre-written

## Verification

- Both TransactionTest methods pass
- SQL log on the failing path shows ROLLBACK on the outer transaction
  and COMMIT on the audit transaction (the inner one)
- You can explain why REQUIRES_NEW is required here — what would
  happen with REQUIRED instead

## Common Mistakes

- Importing @Transactional from jakarta.transaction — doesn't honor
  Spring's propagation settings
- Putting placeOrder on the audit service or vice versa — Spring proxy
  self-invocation bypasses @Transactional
- Catching the exception inside placeOrder instead of letting it
  propagate — rollback never triggers
- Forgetting rollbackFor for checked exceptions (not needed here since
  PaymentDeclinedException is a RuntimeException, but worth knowing)