# Exercise: Top Customers Report and Dynamic Order Search

## What You'll Build

Implement two query methods on the same Order/Customer starter — one
static JPQL aggregation (top customers by spend), one dynamic Criteria
search (orders matching optional filters).

## Requirements

- findTopCustomers(int year) — JPQL with JOIN, GROUP BY, ORDER BY DESC,
  LIMIT 10, returning a List<TopCustomerDto> via constructor expression
- searchOrders(OrderSearchCriteria) — Criteria API, predicates added
  only for non-null fields
- The provided QueryTest verifies both behaviors

## Starter Code

- PostgreSQL with 50 customers and 500 orders across two years
- Customer, Order entities (complete)
- TopCustomerDto class (complete)
- OrderRepository with one method stub for findTopCustomers
- OrderSearchCriteria DTO with four optional fields (complete)
- OrderSearchService with one method stub for searchOrders (with hints
  on CriteriaBuilder usage)
- QueryTest pre-written

## Verification

- All QueryTest methods pass
- Top-customers SQL shows JOIN, GROUP BY, ORDER BY DESC, LIMIT 10
- Criteria SQL contains predicates only for non-null inputs

## Common Mistakes

- SUM without GROUP BY
- Omitting DISTINCT when joins multiply rows
- String-concatenating Criteria predicates
- Returning entity types from a reporting query