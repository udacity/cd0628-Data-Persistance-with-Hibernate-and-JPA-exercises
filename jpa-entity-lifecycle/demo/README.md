# Module 5 Demo: JPA Entity Lifecycle with Pilot Roster

A pilot roster showing the JPA entity lifecycle transitions. Kept intentionally
different from the exercise so learners see the same concepts applied to a fresh
domain.

## What this demo shows

Each state transition walks through explicitly:

- **TRANSIENT to MANAGED**: `em.persist(pilot)`
- **MANAGED**: dirty checking automatically flushes updates on commit
- **MANAGED to DETACHED**: `em.detach(pilot)` or transaction close
- **DETACHED to MANAGED**: `em.merge(detachedPilot)`
- **MANAGED to REMOVED**: `em.remove(pilot)`

## Files

- `Pilot.java` - the entity with `@Entity`, `@Id`, `@GeneratedValue`, and column mappings
- `DemoRunner.java` - main entry point walking through each lifecycle transition
- `persistence.xml` - the persistence unit configuration

## How to run

The demo uses the `banking` Postgres database via Docker. Schema is created
automatically via `hibernate.hbm2ddl.auto=update`.

```
mvn compile
mvn exec:java
```

Expected output shows each lifecycle transition being printed, plus the SQL
Hibernate emits for the corresponding INSERT, UPDATE, and DELETE statements.
