# Exercise: Native SQL Reporting and Keyset Pagination

## Overview

In this exercise, you'll write two queries that JPQL alone can't
express well. First, a native SQL monthly-revenue-by-category
report using PostgreSQL's `date_trunc`, mapped into a typed DTO
via `@SqlResultSetMapping`. Second, keyset pagination over a
product list -- the technique that scales where offset pagination
falls apart on large tables. The pre-written test also captures
`EXPLAIN ANALYZE` so you can see why keyset wins.

## Exercise Instructions

Open the starter project and work through the TODOs across two
repository files.

### Part 1: OrderRepository - Native SQL Report

Open `OrderRepository.java`.

**TODO 1: Implement `findMonthlyRevenueByCategoryRaw()` using a native query**
Group orders by month and category, sum the amounts, and return
the rows. The `@SqlResultSetMapping` on the Order entity already
defines how to map these columns into `MonthlyRevenueReport`; you
just need to use the right column aliases.

### Part 2: ProductRepository - Keyset Pagination

Open `ProductRepository.java`.

**TODO 2: Implement `findProductsAfter(String lastName, Limit limit)`**
Write a JPQL query that returns up to `limit` products whose name
is strictly greater than `lastName`, ordered by name. Add a tiebreaker
on id so duplicate names don't cause skip/repeat.

## Deliverable

`QueryTest` passes:
- Monthly report rows match expected aggregates for the seeded data
- Keyset pagination walks through the full 10,000-product catalog
  with no skips or repeats
- EXPLAIN ANALYZE output (printed to test logs) shows offset pagination
  scans more rows as page number grows; keyset stays constant

Run the test with:

```
mvn test
```

Expected output ends with:

```
[INFO] BUILD SUCCESS
```

## What's Included

- `OrderRepository.java` with TODO 1
- `ProductRepository.java` with TODO 2
- `Order.java` and `Product.java`, complete
- `MonthlyRevenueReport.java` DTO with `@SqlResultSetMapping`
  declaration on the Order entity
- `QueryTest.java`, pre-written, including the EXPLAIN ANALYZE
  comparison
- `schema.sql` (10,000 products with indexed name, 1,800 orders
  across 12 months and 5 categories)
- `data.sql` for both tables
- `application.yml` with SQL logging on and statistics enabled

## Common Mistakes

- **Native query returning `Object[]` instead of a typed DTO:** forgot to use the `@SqlResultSetMapping` mapping name when executing via `createNativeQuery(..., "MonthlyRevenueMapping")`. Without it, Hibernate hands back raw arrays.
- **Forgetting `nativeQuery = true`:** Hibernate tries to parse `date_trunc` as JPQL and fails - JPQL has no such function.
- **Keyset pagination without a unique tiebreaker:** if you order by `name` and two products share a name, the cursor can skip or repeat. Either ensure name is unique or add `, id` to the ORDER BY.
- **Comparing offset vs keyset on page 1 only:** they perform identically there. The difference shows up at page 100, 1000, etc., when offset has to scan and skip more rows.
- **Hardcoding the LIMIT value:** the method takes `limit` as a parameter for a reason - different callers want different page sizes.
