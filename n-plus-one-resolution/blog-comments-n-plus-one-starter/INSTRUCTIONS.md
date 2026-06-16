# Exercise: Reproduce N+1 and Apply Three Fixes

## Overview

A starter that exhibits a textbook N+1 query problem: 10 blog posts,
each with 5 comments, loaded via a LAZY collection. You'll complete
three different fix strategies (JOIN FETCH, `@EntityGraph`,
`@BatchSize`) and watch a pre-written test prove each one fires the
expected number of queries. Hibernate statistics drive the assertions.

## Exercise Instructions

Open the starter project and work through the TODOs across two files.
Hibernate statistics are already enabled in `application.yml`.

### Part 1: BlogPostRepository

Open `BlogPostRepository.java`. The interface has stubs for three
fetch methods. Implement each one using a different strategy.

**TODO 1: Implement `findAllWithJoinFetch` using JPQL JOIN FETCH**
Use `@Query("SELECT DISTINCT p FROM BlogPost p JOIN FETCH p.comments")`.
The `DISTINCT` keyword prevents duplicate parent rows when one post
has multiple comments. The result should fire exactly ONE query.

**TODO 2: Implement `findAllWithEntityGraph` using `@EntityGraph`**
Annotate the method with `@EntityGraph(attributePaths = "comments")`.
The method body itself can be a standard `findAll()` — Spring Data
combines the entity graph with the default query. The result should
fire exactly ONE query, but in a reusable way: the graph can be named
and used on multiple methods.

**TODO 3: Implement `findAllWithBatchSize` as a plain `findAll()`**
No special query annotation. The actual fix lives on the entity
(TODO 4 below). The method body is just `return findAll();`. The
result should fire TWO queries: one for posts, one batched for all
their comments.

### Part 2: BlogPost Entity

Open `BlogPost.java`.

**TODO 4: Annotate the `comments` field with `@BatchSize(size = 20)`**
Tells Hibernate to load comments for up to 20 posts in one query
instead of N separate queries. Pair with `findAllWithBatchSize` from
TODO 3 — together they produce the 2-query result.

## Deliverable

`NPlusOneTest` passes all four scenarios:
- Default `findAll()` triggers 11 queries (1 + 10) — the baseline N+1
- `findAllWithJoinFetch()` triggers exactly 1
- `findAllWithEntityGraph()` triggers exactly 1
- `findAllWithBatchSize()` triggers exactly 2

The test prints the query count after each scenario so you can see
the cache effect.

## What's Included

- `BlogPostRepository.java` with TODOs 1-3
- `BlogPost.java` with TODO 4 (entity is otherwise complete)
- `Comment.java`, complete
- `NPlusOneTest.java`, pre-written with statistics-based assertions
- `schema.sql` and `data.sql` (10 posts × 5 comments)
- `application.yml` with SQL logging and Hibernate statistics on

## Common Mistakes

- **JOIN FETCH on two collections in one query:** throws `MultipleBagFetchException`. You can only JOIN FETCH one bag (List) at a time
- **Omitting `DISTINCT` in JOIN FETCH:** when one post has multiple comments, the post row gets duplicated in the result — your `findAll()` returns 50 posts instead of 10
- **`@BatchSize` too small:** defeats the point; you still fire many small batches. Too large: wastes memory loading rows you may not use. 20-50 is a reasonable default
- **Measuring with first-level cache warm:** the second test scenario runs against a session that's already loaded entities from the previous one. The pre-written test calls `entityManager.clear()` between scenarios to keep results comparable
- **Forgetting that EntityGraph requires the method, not just the entity:** the `@EntityGraph` annotation goes on the repository method, not the entity field