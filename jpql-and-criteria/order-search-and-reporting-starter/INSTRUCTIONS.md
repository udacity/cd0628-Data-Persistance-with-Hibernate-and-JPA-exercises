# Exercise: Top Customers Report and Dynamic Order Search

## Overview

Two complementary query implementations against an Order/Customer
domain. First, a static JPQL aggregation query that returns the top
10 customers by total spend in a given year. Second, a dynamic Criteria
search that builds a query at runtime from optional filters (status,
customer ID, minimum amount, date range), adding WHERE predicates only
for the non-null parameters.

## Exercise Instructions

Open the starter project and work through the TODOs across two files.

### Part 1: OrderRepository — Static JPQL Aggregation

Open `OrderRepository.java`.

**TODO 1: Implement `findTopCustomers(int year)` using a JPQL `@Query`**
Write a JPQL query that:
- Joins Order to Customer
- Groups by customer ID and name
- Sums the order amounts
- Orders by total descending
- Limits to 10 rows
- Returns `List<TopCustomerDto>` via constructor expression

The query uses `EXTRACT(YEAR FROM o.placedAt) = :year` to filter by
year, and `new com.udacity.report.TopCustomerDto(c.id, c.name, SUM(o.amount))`
to project into the DTO.

### Part 2: OrderSearchService — Dynamic Criteria

Open `OrderSearchService.java`. The class is wired up with an
`EntityManager`. The `OrderSearchCriteria` record (with four optional
fields) is already provided.

**TODO 2: Implement `searchOrders(OrderSearchCriteria criteria)`**
Build a Criteria query that:
- Selects from `Order`
- Adds a predicate for each non-null filter field:
  - `status` (equals)
  - `customerId` (equals, requires joining Customer)
  - `minAmount` (greater-than-or-equal-to)
  - `dateRange` (between)
- Combines all collected predicates with `AND`
- Returns the matching orders

The key insight: build a `List<Predicate>` and add to it only when
the corresponding field is non-null. Then pass the whole list to
`criteriaBuilder.and(...)`.

## Deliverable

`QueryTest` passes:
- Top customers query returns 10 rows for a year with data, ordered
  by total spend descending
- All-null search returns the full table (no WHERE clause)
- Single-filter searches each narrow correctly
- Multi-filter combinations AND together properly

## What's Included

- `OrderRepository.java` with TODO 1
- `OrderSearchService.java` with TODO 2 (hints on CriteriaBuilder usage)
- `Customer.java` and `Order.java`, complete
- `TopCustomerDto.java`, complete
- `OrderSearchCriteria.java`, complete (4 optional fields)
- `QueryTest.java`, pre-written
- `schema.sql` and `data.sql` (50 customers, 500 orders across 2 years)
- `application.yml` with SQL logging on

## Common Mistakes

- **`SUM` without `GROUP BY`:** Hibernate may throw, or worse, return a single aggregated row depending on the dialect — always group by every non-aggregated column you select
- **Omitting `DISTINCT` when joins multiply rows:** if a customer has multiple orders, the join produces a row per order; without DISTINCT (or proper GROUP BY) you get duplicates
- **Building Criteria predicates by concatenating Strings:** defeats the entire point of the Criteria API — type safety and SQL injection protection. Use `criteriaBuilder.equal(...)`, `.between(...)`, etc.
- **Returning entity types from a reporting query:** when you only need a few fields, use a DTO + constructor expression. Returning full entities triggers lazy loading and wastes memory
- **Forgetting that `EXTRACT` in JPQL must wrap a date field:** `EXTRACT(YEAR FROM o.placedAt)`, not `o.placedAt.year`