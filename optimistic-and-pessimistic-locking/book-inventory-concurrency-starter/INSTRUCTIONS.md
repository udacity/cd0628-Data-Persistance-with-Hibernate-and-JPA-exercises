# Exercise: Concurrency Control for a Book Inventory

## Overview

Apply both locking strategies to the same Book inventory domain.
Optimistic locking with a `@Version` field handles normal updates and
retries on conflict. Pessimistic locking with `PESSIMISTIC_WRITE`
serializes the high-contention "reserve last copy" path. A pre-written
concurrency test runs two threads against the same row for each
strategy and asserts the expected outcomes.

## Exercise Instructions

Open the starter project and work through the TODOs across three
files. The retry interceptor and concurrency test are already
configured.

### Part 1: Optimistic Locking on Book

Open `Book.java`.

**TODO 1: Add a `version` field annotated with `@Version`**
Add a `private Long version;` field with `@Version` above it. Hibernate
manages this column automatically: it increments the version on every
update and throws `OptimisticLockException` if the row was modified by
another transaction in the meantime.

### Part 2: Pessimistic Lock Query

Open `BookRepository.java`.

**TODO 2: Add a `findByIdForUpdate` method with `@Lock(LockModeType.PESSIMISTIC_WRITE)`**
Spring Data picks up the annotation and translates it to a
`SELECT ... FOR UPDATE` (or PostgreSQL's `FOR NO KEY UPDATE`) in the
SQL. Any other transaction trying to read or write the same row will
block until this one commits.

Method signature: takes `Long id`, returns `Optional<Book>`.

### Part 3: Reserve Last Copy with Pessimistic Lock

Open `BookInventoryService.java`.

**TODO 3: Annotate `reserveLastCopy` with `@Transactional`**
Required for the pessimistic lock to do anything. The lock is released
when the transaction commits or rolls back.

**TODO 4: Implement the body of `reserveLastCopy(long bookId)`**
Perform these steps in order:
1. Call `bookRepository.findByIdForUpdate(bookId)` to acquire the lock
2. If the Optional is empty, throw `BookNotFoundException`
3. If `book.getStock() > 0`, decrement stock by 1 and save the book
4. If `book.getStock() <= 0`, throw `OutOfStockException`

Because of the pessimistic lock, only one thread enters this method at
a time for a given book. The second thread waits until the first
commits, then re-reads the updated stock value.

## Deliverable

`ConcurrencyTest` passes both scenarios:
- **Optimistic conflict:** two threads update the same Book
  simultaneously. One succeeds on first attempt; the other catches the
  `OptimisticLockException`, retries via the pre-configured interceptor,
  and succeeds on the retry. Both final updates are present.
- **Pessimistic serialization:** two threads call `reserveLastCopy`
  for a Book with stock=1. One succeeds and decrements stock to 0. The
  other waits, then sees stock=0 and throws `OutOfStockException`.

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
- `application.yml` with SQL logging, statistics, and Spring transaction
  logging on

## Common Mistakes

- **Self-invocation defeats `@Transactional`:** if the test calls a method on the same class as `reserveLastCopy`, Spring's proxy doesn't intercept. Always call across services, or use AspectJ weaving
- **Optimistic retry without backoff:** thundering-herd on a contended row. The pre-configured retry uses exponential backoff (50ms, 100ms, 200ms...) to spread retries
- **Pessimistic lock without `@Transactional`:** throws "No active transaction" because the lock needs a transaction to scope its duration
- **Forgetting that PostgreSQL emits `FOR NO KEY UPDATE` instead of `FOR UPDATE`:** worth checking the SQL log. Both block writers; `FOR NO KEY UPDATE` allows concurrent FK-only reads, which is what JPA usually wants
- **Mixing optimistic and pessimistic on the same row:** if one thread uses `findById` (no lock) and another uses `findByIdForUpdate`, the first can still get an `OptimisticLockException` when committing. Pick one strategy per call site