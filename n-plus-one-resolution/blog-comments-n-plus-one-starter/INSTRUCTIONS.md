# Exercise: Diagnose and Fix the N+1 Query Problem

## Overview

In this exercise, you'll see the N+1 query problem fire in real
Hibernate logs and then fix it three different ways: with JPQL's
`JOIN FETCH`, with `@EntityGraph`, and with `@BatchSize` on the
collection. You'll measure the query count for each approach so
the trade-offs are concrete, not abstract.


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

### Part 1: Baseline N+1 demonstration

Open `BlogPostRepository.java`.

**TODO 1: Implement `findAllNaive` returning all blog posts**
No annotation, no special fetch -- just a derived method. When the
test iterates the result and accesses each post's comments, you'll
see one SELECT per post in the log: the N+1 problem.

### Part 2: Fix with JOIN FETCH

Same file.

**TODO 2: Implement `findAllWithJoinFetch` annotated with `@Query`**
Use `SELECT DISTINCT p FROM BlogPost p LEFT JOIN FETCH p.comments`.
JOIN FETCH pulls comments in the same SELECT as posts. DISTINCT is
needed because the cartesian product would otherwise return one
row per post-comment pair.

### Part 3: Fix with @EntityGraph

Same file.

**TODO 3: Implement `findAllWithEntityGraph` annotated with `@EntityGraph`**
Use `@EntityGraph(attributePaths = "comments")` on the method. The
declarative form of the same JOIN FETCH idea, configurable per
call site without changing the entity itself.

You'll also need `@Query("SELECT p FROM BlogPost p")` alongside
the `@EntityGraph` -- without it, Spring Data parses the
`findAllWithEntityGraph` name and ignores the entity graph.

### Part 4: Fix with @BatchSize

Open `BlogPost.java`.

**TODO 4: Annotate the comments collection with `@BatchSize`**
Use `@BatchSize(size = 10)`. This doesn't change individual queries
to JOIN FETCH; instead, when lazy loading kicks in, Hibernate batches
up to 10 deferred loads into a single `IN (...)` query. Different
trade-off: keeps each row's first SELECT lean, but trades latency
for batched follow-ups.

## Deliverable

`NPlusOneTest` passes:
- Naive version fires N+1 queries (assertion catches this)
- JOIN FETCH version fires 1 query
- EntityGraph version fires 1 query
- BatchSize version fires <= 2 queries (initial + one batched lazy load)

Run the test with:

```
mvn test
```

Expected output ends with:

```
[INFO] BUILD SUCCESS
```

The SQL log shows the query count drop dramatically with each fix.

## What's Included

- `BlogPost.java` with TODO 4
- `Comment.java`, complete
- `BlogPostRepository.java` with TODOs 1-3
- `NPlusOneTest.java`, pre-written with query-count assertions
- `schema.sql` and `data.sql` (multiple posts, many comments each)
- `application.yml` with SQL logging and statistics enabled

## Common Mistakes

- **Spring Data parses `findAll*` as a derived query by default:** if you want `@EntityGraph` on a method named like `findAllSomething`, you must also annotate with `@Query("SELECT p FROM BlogPost p")` or it falls back to derivation.
- **DISTINCT missing with JOIN FETCH on a collection:** you get duplicate parent rows, one per child.
- **@BatchSize on the wrong side:** put it on the collection (the `@OneToMany` field), not on the parent entity itself.
- **Test reads collection AFTER closing session:** triggers LazyInitializationException, which masks whether the fix even worked.
