# Exercise: Reproduce N+1 and Apply Three Fixes

## What You'll Build

Complete three repository methods that each fix a textbook N+1 problem
a different way — JOIN FETCH, @EntityGraph, and @BatchSize — then watch
the pre-written test prove each fires the expected query count.

## Requirements

- findAllWithJoinFetch uses JPQL with JOIN FETCH + DISTINCT
- findAllWithEntityGraph uses @EntityGraph(attributePaths = "comments")
- For BatchSize: add @BatchSize(size = 20) to the comments collection;
  findAllWithBatchSize is just a plain findAll()
- The provided NPlusOneTest asserts:
  - Default: 1 + 10 queries
  - JOIN FETCH: 1
  - EntityGraph: 1
  - BatchSize: 2 (1 + 1 batched)

## Starter Code

- PostgreSQL with 10 posts × 5 comments
- BlogPost, Comment — complete except for @BatchSize TODO
- BlogPostRepository with three method stubs (TODOs)
- NPlusOneTest pre-written with assertions
- Hibernate statistics enabled

## Verification

- All four NPlusOneTest methods pass with the asserted counts
- You can explain when each fix wins

## Common Mistakes

- JOIN FETCH on two collections at once — MultipleBagFetchException
- Omitting DISTINCT — duplicate parent rows
- @BatchSize too small or too large
- Measuring with first-level cache warm