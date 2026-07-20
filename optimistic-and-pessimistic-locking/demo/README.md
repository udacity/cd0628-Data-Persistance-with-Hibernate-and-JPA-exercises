# Module 25 Demo: Optimistic and Pessimistic Locking with Inventory

Two workers race to decrement the same inventory row. Shows what happens under
each locking strategy.

## What this demo shows

### Section 1: Optimistic locking

The entity has a `@Version` field. Hibernate reads it when it loads a row and
includes it in the UPDATE's WHERE clause. If someone else updates the row
first, the version no longer matches, zero rows update, and
`OptimisticLockException` fires.

Simulated flow:

1. Worker A loads the row (version 0)
2. Worker B loads the same row (version 0)
3. Worker A decrements and commits. Version now 1.
4. Worker B tries to decrement and commit. Its version is still 0. UPDATE
   matches zero rows. `OptimisticLockException` thrown.

Best for: reads are common, conflicts are rare, users can retry cheaply.

### Section 2: Pessimistic locking

Worker A calls `find(id, LockModeType.PESSIMISTIC_WRITE)`. Hibernate translates
this to `SELECT ... FOR UPDATE` on Postgres. The row is locked for the
duration of the transaction. Worker B's own `find(id, PESSIMISTIC_WRITE)`
would block (in a threaded scenario) until Worker A commits.

Simulated flow (interleaved on one thread for reproducibility):

1. Worker A takes PESSIMISTIC_WRITE lock, decrements, commits (releases lock)
2. Worker B takes PESSIMISTIC_WRITE lock, decrements, commits

No exception. Both operations succeed. In a real threaded scenario Worker B
would have waited on the database lock instead of running immediately.

Best for: high contention, cannot tolerate retries, short-lived transactions.

## Files

- `InventoryItem.java` - entity with `@Version`
- `DemoRunner.java` - runs both scenarios end to end

## How to run

Uses the `banking` Postgres database via Docker. Schema is dropped and
recreated on each run.

```
mvn compile
mvn exec:java
```
