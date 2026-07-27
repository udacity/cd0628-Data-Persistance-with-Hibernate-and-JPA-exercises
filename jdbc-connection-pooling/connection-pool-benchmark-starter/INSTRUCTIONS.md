# Exercise: Wire and Tune a HikariCP Data Source

## Overview

In this exercise, you'll implement a HikariCP-backed data source and
tune its pool settings, then measure how pool size affects throughput
under concurrent load. You'll implement the repository's read and
batch-insert paths, fill in the pool configuration, then run a
benchmark that fires many short queries from 20 concurrent threads
against two different pool sizes. The benchmark prints throughput in
queries per second for each, so you can see the effect of pool sizing
directly.


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
- `findById` returns the correct customer, and an empty Optional for a missing id
- `batchInsert` persists every customer in the list

Run the test with:

```
mvn test
```

Expected output ends with:

```
[INFO] BUILD SUCCESS
```

When the application runs, `BenchmarkRunner` prints a throughput line
for each pool size (2 and 10), so you can compare queries per second
across the two configurations.

## What's Included

- `CustomerRepositoryImpl.java` with TODOs 1 and 2
- `HikariConfigFactory.java` with TODOs 3-6
- `BenchmarkRunner.java`, complete, runs automatically on startup
- `schema.sql` and `data.sql` for the customer table

## Common Mistakes

- **`findById` empty for valid ids:** missing `rs.next()` before reading
- **Batch not faster than a loop:** opening Connection inside the loop
- **"Pool exhausted" errors:** `connectionTimeout` too low for the load
- **Leaked connection warnings:** missing try-with-resources somewhere