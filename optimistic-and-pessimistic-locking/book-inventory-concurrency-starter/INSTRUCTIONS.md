# Exercise: Concurrency Control for a Book Inventory

## Overview

In this exercise, you'll apply both JPA locking strategies to the
same Book inventory domain. Optimistic locking with a `@Version`
field handles ordinary updates and retries on conflict. Pessimistic
locking with `PESSIMISTIC_WRITE` serializes the high-contention
"reserve the last copy" path. The pre-written concurrency test
runs two threads against the same row for each strategy and
asserts the expected outcomes.


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

Open the starter project and work through the TODOs across three
files. The retry interceptor and concurrency test are already
configured.

### Part 1: Optimistic Locking on Book

Open `Book.java`.

**TODO 1: Add a `version` field annotated with `@Version`**
Hibernate manages this column automatically: it increments the
version on every update and throws `OptimisticLockException` if
the row was modified by another transaction in the meantime.

### Part 2: Pessimistic Lock Query

Open `BookRepository.java`.

**TODO 2: Add `findByIdForUpdate` with `@Lock(PESSIMISTIC_WRITE)`**
Spring Data picks up the annotation and translates it to
`SELECT ... FOR UPDATE` in the SQL (PostgreSQL emits `FOR NO KEY
UPDATE`). Any other transaction trying to read or write the same
row blocks until this one commits.

### Part 3: Reserve Last Copy with Pessimistic Lock

Open `BookInventoryService.java`.

**TODO 3: Annotate `reserveLastCopy` with `@Transactional`**
The pessimistic lock needs a transaction to scope its duration.

**TODO 4: Implement the body of `reserveLastCopy(long bookId)`**
Acquire the lock by calling `findByIdForUpdate`, then check stock
and either decrement and save (if stock > 0) or throw
`OutOfStockException` (if stock <= 0).

## Deliverable

`ConcurrencyTest` passes both scenarios:
- **Optimistic conflict:** two threads update the same Book
  simultaneously. One succeeds on first attempt; the other catches
  `OptimisticLockException`, retries via the pre-configured
  interceptor with exponential backoff, and succeeds on the retry.
  Both final updates are present.
- **Pessimistic serialization:** two threads call `reserveLastCopy`
  for a Book with stock=1. One succeeds and decrements stock to 0.
  The other waits, then sees stock=0 and throws `OutOfStockException`.

Run the test with:

```
mvn test
```

Expected output ends with:

```
[INFO] BUILD SUCCESS
```

SQL logs show `SELECT ... FOR NO KEY UPDATE` for the pessimistic case.

## What's Included

- `Book.java` with TODO 1
- `BookRepository.java` with TODO 2
- `BookInventoryService.java` with TODOs 3-4
- `BookNotFoundException.java` and `OutOfStockException.java`, complete
- `RetryConfig.java`, complete (Spring Retry interceptor configured
  with exponential backoff)
- `ConcurrencyTest.java`, pre-written using ExecutorService
- `schema.sql` and `data.sql` (books with varying stock levels,
  including some with stock=1 for the pessimistic test)
- `application.yml` with SQL logging, statistics, and Spring
  transaction logging on

## Common Mistakes

- **Self-invocation defeats `@Transactional`:** if the test calls a method on the same class as `reserveLastCopy`, Spring's proxy doesn't intercept. Always call across services, or use AspectJ weaving.
- **Optimistic retry without backoff:** thundering-herd on a contended row. The pre-configured retry uses exponential backoff (50ms, 100ms, 200ms...) to spread retries.
- **Pessimistic lock without `@Transactional`:** throws "No active transaction" because the lock needs a transaction to scope its duration.
- **Forgetting that PostgreSQL emits `FOR NO KEY UPDATE` instead of `FOR UPDATE`:** worth checking the SQL log. Both block writers; `FOR NO KEY UPDATE` allows concurrent FK-only reads, which is what JPA usually wants.
- **Mixing optimistic and pessimistic on the same row:** if one thread uses `findById` (no lock) and another uses `findByIdForUpdate`, the first can still get an `OptimisticLockException` when committing. Pick one strategy per call site.
