# Exercise: Build Static and Dynamic Order Queries

## Overview

In this exercise, you'll implement two complementary query
techniques. First, a static JPQL aggregation that returns the top
10 customers by total spend for a given year, projected into a
typed DTO. Second, a dynamic Criteria search that builds a query
at runtime from optional filter fields, adding WHERE predicates
only for the ones that are actually provided.

## Exercise Instructions

Open the starter project and work through the TODOs across two
files.

### Part 1: OrderRepository - Static JPQL Aggregation

Open `OrderRepository.java`.

**TODO 1: Implement `findTopCustomers(int year)` using a JPQL `@Query`**
Aggregate orders by customer for the given year, sum the amounts,
order by total descending, and project directly into TopCustomerDto
via a JPQL constructor expression.

### Part 2: OrderSearchService - Dynamic Criteria

Open `OrderSearchService.java`.

**TODO 2: Implement `searchOrders(OrderSearchCriteria criteria)`**
Build a Criteria query that adds a predicate for each non-null
field on the criteria record. Combine the predicates with AND.
All-null criteria should return everything.

## Deliverable

`QueryTest` passes:
- Top customers query returns 10 rows for a year with data, ordered
  by total spend descending
- All-null search returns the full table (no WHERE clause)
- Single-filter searches each narrow correctly
- Multi-filter combinations AND together properly

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
- `OrderSearchService.java` with TODO 2 (the EntityManager is wired)
- `Customer.java` and `Order.java`, complete
- `TopCustomerDto.java`, complete (the DTO for the projection)
- `OrderSearchCriteria.java`, complete (4 optional fields)
- `QueryTest.java`, pre-written
- `schema.sql` and `data.sql` (50 customers, 500 orders across 2 years)
- `application.yml` with SQL logging on

## Common Mistakes

- **`SUM` without `GROUP BY`:** Hibernate may throw, or return a single aggregated row, depending on dialect. Always group by every non-aggregated column you select.
- **Omitting `DISTINCT` when joins multiply rows:** if a customer has multiple orders, the join produces a row per order; without DISTINCT (or proper GROUP BY) you get duplicates.
- **Building Criteria predicates by concatenating Strings:** defeats the entire point of the Criteria API - type safety and SQL injection protection. Use `criteriaBuilder.equal(...)`, `.between(...)`, etc.
- **Returning entity types from a reporting query:** when you only need a few fields, use a DTO + constructor expression. Returning full entities triggers lazy loading and wastes memory.
- **Forgetting that `EXTRACT` in JPQL must wrap a date field:** `EXTRACT(YEAR FROM o.placedAt)`, not `o.placedAt.year`.
