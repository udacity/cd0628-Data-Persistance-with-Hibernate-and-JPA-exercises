# Exercise: Trace JPA Lifecycle and Id Generation Timing

## Overview

In this exercise, you'll trace JPA's entity lifecycle by observing
when SQL actually fires for the IDENTITY versus SEQUENCE generator
strategies. You'll persist entities under each strategy and watch
the Hibernate logs to see how the generator choice changes the
timing of INSERT statements relative to your code.


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

Open the starter project and work through the TODOs in two entity
files plus one test setup method.

### Part 1: Customer with IDENTITY Generator

Open `Customer.java`.

**TODO 1: Annotate the `id` field**
Add `@Id` and `@GeneratedValue(strategy = GenerationType.IDENTITY)`.
With IDENTITY, the database assigns the id and Hibernate must issue
an INSERT immediately on `persist` to learn the generated value.

### Part 2: Order with SEQUENCE Generator

Open `Order.java`.

**TODO 2: Annotate the `id` field with a sequence generator**
Add `@Id` and `@GeneratedValue(strategy = GenerationType.SEQUENCE,
generator = "order_seq")`, plus a matching `@SequenceGenerator(name =
"order_seq", sequenceName = "order_sequence", allocationSize = 50)`.
SEQUENCE pre-allocates ids in batches so Hibernate can defer INSERTs
until flush.

### Part 3: Lifecycle Walkthrough

Open `LifecycleTest.java`.

**TODO 3: Observe and assert lifecycle transitions**
The test scaffolding is provided. Read through and confirm:
- A new entity is `transient` until persist
- After persist with IDENTITY, an INSERT has already fired
- After persist with SEQUENCE, no INSERT until flush or commit
- `em.detach(...)` moves an entity to detached state
- `em.merge(...)` brings it back as managed
- `em.remove(...)` schedules a DELETE on flush

## Deliverable

`LifecycleTest` passes:
- IDENTITY strategy: an INSERT fires immediately on persist
- SEQUENCE strategy: INSERTs batch and flush at commit
- Detached, merged, and removed states transition as expected

Run the test with:

```
mvn test
```

Expected output ends with:

```
[INFO] BUILD SUCCESS
```

The SQL log shows the difference in INSERT timing between strategies.

## What's Included

- `Customer.java` with TODO 1 (IDENTITY)
- `Order.java` with TODO 2 (SEQUENCE)
- `LifecycleTest.java`, pre-written with the walkthrough
- `application.yml` with SQL logging on
- H2 in-memory database (no Postgres needed for this exercise)

## Common Mistakes

- **No INSERT visible with IDENTITY:** flush is implicit at commit, but logs may be filtered. Check `org.hibernate.SQL` is at DEBUG.
- **SEQUENCE strategy throws "sequence does not exist":** Hibernate creates the sequence at startup based on the generator name. Mismatched names won't auto-create.
- **`em.merge` returns a new object:** the parameter stays detached. Always use the returned reference for further work.
