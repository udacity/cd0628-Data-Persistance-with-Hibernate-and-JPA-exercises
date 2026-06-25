# Exercise: Wire and Tune a HikariCP Data Source

## Overview

In this exercise, you'll measure the performance difference between
unpooled JDBC connections and a HikariCP-backed connection pool.
You'll implement the pooled path, then run a benchmark that issues
many short queries through each strategy. The benchmark prints
average per-query latency so you can see the cost of connection
setup directly.

## Exercise Instructions

Open the starter project and work through the TODOs in two files.

### Part 1: CustomerRepositoryImpl

Open `CustomerRepositoryImpl.java`. The `create`, `update`, and `delete`
methods are already implemented as reference. Implement the two
remaining methods.

**TODO 1: Implement `findById`**
Select by id, map the row using the provided `mapRow` helper, and return
an `Optional<Customer>`. Use try-with-resources for Connection,
PreparedStatement, and ResultSet.

**TODO 2: Implement `batchInsert`**
Use the same INSERT SQL as `create()`. Loop the list calling
`addBatch()`, then call `executeBatch()` once. Looping single inserts
is roughly 10x slower.

### Part 2: HikariConfigFactory

Open `HikariConfigFactory.java`. JDBC URL and credentials are already
wired. Fill in the four pool settings.

**TODO 3: Call `setMaximumPoolSize`**
Use the parameter passed into `createConfig`. The benchmark calls this
method twice with different sizes (2 and 10) so it can compare them.

**TODO 4: Call `setConnectionTimeout` (milliseconds)**
Controls how long a thread waits for a connection before failing.
Start with 30_000 (30 seconds).

**TODO 5: Call `setIdleTimeout` (milliseconds)**
Controls how long idle connections stay open before being released.
Start with 600_000 (10 minutes).

**TODO 6: Call `setMaxLifetime` (milliseconds)**
Maximum lifetime of any single connection. Start with 1_800_000
(30 minutes).

## Deliverable

`CustomerRepositoryTest` passes:
- Pooled queries return identical results to unpooled queries
- Pooled latency is meaningfully lower than unpooled

Run the test with:

```
mvn test
```

Expected output ends with:

```
[INFO] BUILD SUCCESS
```

The benchmark log lines also show average latency per query for
each strategy.

## What's Included

- `CustomerRepositoryImpl.java` with TODOs 1 and 2
- `HikariConfigFactory.java` with TODOs 3-6
- `BenchmarkRunner.java`, complete, runs automatically on startup
- `CustomerRepositoryTest.java`, pre-written
- `schema.sql` and `data.sql` for the customer table

## Common Mistakes

- **`findById` empty for valid ids:** missing `rs.next()` before reading
- **Batch not faster than a loop:** opening Connection inside the loop
- **"Pool exhausted" errors:** `connectionTimeout` too low for the load
- **Leaked connection warnings:** missing try-with-resources somewhere