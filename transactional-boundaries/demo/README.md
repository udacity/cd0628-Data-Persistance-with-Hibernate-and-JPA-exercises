# Module 23 Demo: Transactional Boundaries with Bank Account Transfers

Shows three transaction concepts using a simple money-transfer scenario:

1. **Default `@Transactional`** - a single atomic unit. Debits and credits
   commit together or roll back together.
2. **`Propagation.REQUIRES_NEW`** - the audit service runs in its own
   transaction, independent of the outer transfer. Audit rows survive even
   when the caller rolls back.
3. **Runtime exception rollback** - throwing `IllegalStateException` from
   inside a `@Transactional` method rolls back automatically. Balances stay
   as they were before the method started.

## The scenarios

1. Show starting balances (Alice $500, Bob $200)
2. Happy transfer of $100 - balances change, audit row committed
3. Failing transfer of $50 - service throws AFTER writing the audit. The
   audit commits (its own tx). Balances roll back.
4. Final state - balances unchanged from scenario 2, but two audit rows exist:
   the successful transfer AND the failed attempt.

That last point is the payoff. Auditing HAS to survive rollback for it to be
useful in incident forensics. REQUIRES_NEW is how you get it.

## Files

- `Account.java` - simple entity with a balance
- `AuditLog.java` - entity for audit rows
- `AccountRepository.java`, `AuditLogRepository.java` - Spring Data interfaces
- `AuditService.java` - `@Transactional(propagation = REQUIRES_NEW)`
- `TransferService.java` - the transaction boundary owner
- `DemoRunner.java` - Spring Boot app that runs the scenarios

## How to run

Uses the `banking` Postgres database via Docker. Schema is dropped and
recreated on each run.

```
mvn spring-boot:run
```
