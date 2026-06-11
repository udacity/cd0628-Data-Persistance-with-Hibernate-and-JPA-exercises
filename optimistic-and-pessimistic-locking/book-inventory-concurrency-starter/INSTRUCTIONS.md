# Exercise: Concurrency Control for a Book Inventory

## What You'll Build

Apply both optimistic and pessimistic locking to the same BookInventory
domain — optimistic for normal updates with a retry wrapper, pessimistic
for the high-contention "reserve last copy" operation.

## Requirements

- Add @Version to Book — Hibernate manages version increment
- Wrap the optimistic update call site with a retry interceptor (the
  starter provides RetryConfig with backoff already configured); the
  retry should kick in on OptimisticLockException
- Add a findByIdForUpdate method on BookRepository annotated
  @Lock(LockModeType.PESSIMISTIC_WRITE)
- Implement reserveLastCopy(bookId) in BookInventoryService — calls
  findByIdForUpdate inside a @Transactional method
- The pre-written ConcurrencyTest verifies:
  - Two threads updating the same Book — optimistic locking detects
    the conflict, retry succeeds on second attempt
  - Two threads reserving the same last copy — pessimistic locking
    serializes them; one succeeds, the other sees stock=0

## Starter Code

- PostgreSQL with books table seeded
- Book entity with @Id and fields complete, @Version TODO
- BookRepository with findByIdForUpdate stub
- BookInventoryService with reserveLastCopy stub
- RetryConfig pre-wired with StatelessRetryOperationsInterceptor
- ConcurrencyTest pre-written using ExecutorService

## Verification

- Both ConcurrencyTest scenarios pass
- SQL log shows SELECT ... FOR NO KEY UPDATE for pessimistic case
- Hibernate statistics show optimistic version mismatch + retry on the
  optimistic case
- You can explain when to choose optimistic vs pessimistic (read
  frequency, contention rate, retry tolerance)

## Common Mistakes

- Placing the @Transactional + findByIdForUpdate in the same class as
  the caller — Spring proxy self-invocation bypasses both
- Optimistic retry without exponential backoff — thundering-herd on
  contended rows
- Pessimistic lock without @Transactional — "No active transaction"
  error on the lock query
- Forgetting that PESSIMISTIC_WRITE on PostgreSQL emits FOR NO KEY
  UPDATE, not FOR UPDATE (worth noting in SQL log inspection)