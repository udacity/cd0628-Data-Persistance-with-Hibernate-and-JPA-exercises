# Exercise: Native SQL Reporting and Keyset Pagination

## Overview

Two query implementations against an Order/Product starter. First, a
native SQL monthly-revenue-by-category report that uses PostgreSQL's
`date_trunc` and maps the result to a typed DTO. Second, keyset
pagination over a Product list that scales where offset pagination
falls apart. A pre-written test verifies both, and an `EXPLAIN ANALYZE`
comparison shows why keyset wins on large tables.

## Exercise Instructions

Open the starter project and work through the TODOs across two
repository files.

### Part 1: OrderRepository — Native SQL Report

Open `OrderRepository.java`.

**TODO 1: Implement `findMonthlyRevenueByCategory()` using a native query**
Write a native query with `@Query(value = "...", nativeQuery = true)`
that:
- Uses `date_trunc('month', placed_at)` to group by month
- Groups by month and category
- Sums the order amounts
- Orders by month, then category

Map the result to `MonthlyRevenueReport` (three columns: month,
category, total) using the `@SqlResultSetMapping` already declared
above the entity class.

### Part 2: ProductRepository — Keyset Pagination

Open `ProductRepository.java`.

**TODO 2: Implement `findProductsAfter(String lastName, int limit)` using keyset pagination**
Write a JPQL query that:
- Selects products where `name > :lastName`
- Orders by name ascending
- Limits to `:limit` rows

The query is annotated with `@Query` and uses `LIMIT` via Spring
Data's `Pageable` or a direct `setMaxResults` call. Returns
`List<Product>`.

The pre-written test calls this method repeatedly, passing the last
result's name each time, to walk through the full product catalog.

## Deliverable

`QueryTest` passes:
- Monthly report rows match expected aggregates for the seeded data
- Keyset pagination correctly returns the next slice for any starting
  point
- `EXPLAIN ANALYZE` output (captured in the test logs) shows offset
  pagination scans more rows as the page number grows; keyset stays
  constant

## What's Included

- `OrderRepository.java` with TODO 1
- `ProductRepository.java` with TODO 2
- `Order.java` and `Product.java`, complete
- `MonthlyRevenueReport.java` DTO with `@SqlResultSetMapping`
  declaration already on the Order entity
- `QueryTest.java`, pre-written, including the EXPLAIN ANALYZE
  comparison
- `schema.sql` (10,000 products with indexed name, 1,800 orders across
  12 months and 5 categories)
- `data.sql` for both tables
- `application.yml` with SQL logging on and statistics enabled

## Common Mistakes

- **Native query returning `Object[]` instead of a typed DTO:** forgot the `@SqlResultSetMapping` reference in `@Query(name = "...")`. Without it, Hibernate hands back arrays
- **Forgetting `nativeQuery = true`:** Hibernate tries to parse `date_trunc` as JPQL and fails — JPQL has no such function
- **Keyset pagination without a unique tiebreaker:** if you order by `name` and two products share a name, the cursor can skip or repeat. Either ensure name is unique or add `, id` to the ORDER BY
- **Comparing offset vs keyset on page 1 only:** they perform identically there. The difference shows up at page 100, 1000, etc., when offset has to scan and skip more rows
- **Hardcoding the LIMIT value:** the method takes `limit` as a parameter for a reason — different callers want different page sizes