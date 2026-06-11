# Exercise: Native SQL Reporting and Keyset Pagination

## What You'll Build

Two query implementations against an Order/Product starter — a native
SQL monthly-revenue-by-category report mapped to a DTO, and keyset
pagination for a large Product list that scales where offset pagination
doesn't.

## Requirements

- Implement findMonthlyRevenueByCategory in OrderRepository — native
  query with @Query(nativeQuery = true), uses date_trunc, mapped to
  MonthlyRevenueReport via @SqlResultSetMapping (provided)
- Implement findProductsAfter(String lastName, int limit) in
  ProductRepository — keyset pagination using WHERE name > :lastName
  ORDER BY name LIMIT :limit
- The provided QueryTest verifies:
  - Monthly report rows match expected aggregates
  - Keyset pagination returns the correct slice for any starting point
  - EXPLAIN ANALYZE shows keyset uses the index while offset doesn't

## Starter Code

- PostgreSQL with seeded data (12 months × 5 categories of orders;
  10,000 products with indexed name)
- Order, Product, MonthlyRevenueReport DTO — all complete
- @SqlResultSetMapping for the DTO already declared on the entity
- OrderRepository and ProductRepository with one method stub each
- QueryTest pre-written including the EXPLAIN ANALYZE comparison

## Verification

- All QueryTest methods pass
- EXPLAIN ANALYZE output shows offset pagination scanning more rows
  as page number grows; keyset stays constant
- You can explain when offset pagination is fine and when keyset wins

## Common Mistakes

- Native query without @SqlResultSetMapping returning Object[] instead
  of a typed DTO
- Forgetting nativeQuery = true — Hibernate tries to parse as JPQL and
  fails on date_trunc
- Keyset pagination sorting on a non-unique column without a tiebreaker
- Comparing offset vs keyset on page 1 only (where they're identical)