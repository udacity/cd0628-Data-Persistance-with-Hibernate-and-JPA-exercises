# Exercise: Wire and Tune a HikariCP Data Source

## Overview

Complete a partly-built JDBC data access layer and tune a HikariCP
connection pool. You'll implement two repository methods, set four pool
configuration values, and run a benchmark that compares pool sizes
under concurrent load.

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

All tests pass, and `BenchmarkRunner` prints throughput for both pool
configurations. You should see higher throughput at `pool=10` than at
`pool=2` under 20 concurrent threads.

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