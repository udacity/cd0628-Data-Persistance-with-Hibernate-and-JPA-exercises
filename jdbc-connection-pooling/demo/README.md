# Module 2 Demo: HikariCP with Raw JDBC

A minimal concert events tracker showing HikariCP-managed connections and PreparedStatement patterns. This is intentionally different from the exercise, which uses a Customer repository.

## What this demo shows

- HikariCP configuration with maximum pool size, connection timeout, idle timeout, and max lifetime
- A single-row lookup using `PreparedStatement`, parameter binding, and try-with-resources
- A batch insert of five events using `setAutoCommit(false)`, `addBatch`, `executeBatch`, and `commit`

## Files

- `Event.java` - plain POJO for a concert event
- `EventRepository.java` - `findById` and `batchInsert` methods against JDBC + HikariCP
- `DemoRunner.java` - main entry point that seeds five events and reads one back
- `schema.sql` - creates the `event` table in Postgres

## How to run

1. Create a Postgres database named `demo` with a user `demo` / `demo`
2. Execute `schema.sql` against it once
3. Run `DemoRunner.main`

Expected output:
```
Inserted 5 events in one batch.
Fetched: Event[id=1, Fleetwood Mac at Madison Square Garden on 2026-08-01, $129.00]
```
