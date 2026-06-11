# Exercise: Build a Spring Data JPA Repository Four Ways

## What You'll Build

A ProductRepository that exposes the same product catalog through four
different Spring Data techniques: derived method, @Query, interface
projection, and Specification-based search.

## Requirements

- findByCategoryAndPriceLessThan as a derived method (just the signature
  in the interface — Spring Data implements it)
- findTopSellersByMonth as a @Query annotated JPQL method with a join
  to OrderItem
- ProductSummary interface projection (id, name, price) plus a
  findSummariesByCategory method that returns List<ProductSummary>
- searchProducts(ProductSearchSpec) using Specification builders for
  optional category, price-range, and inStock filters
- The pre-written QueryTest verifies all four

## Starter Code

- PostgreSQL with products, order_items seeded
- Product entity (complete)
- ProductSpecs class with method stubs for the three Specification
  builders (TODOs)
- ProductRepository interface with three method signature TODOs
- ProductSearchSpec record (complete)
- QueryTest pre-written

## Verification

- All QueryTest methods pass
- SQL for the derived method shows the expected WHERE clauses
- The interface projection generates a narrower SELECT (only the three
  columns)
- Specification SQL contains predicates only for non-null inputs

## Common Mistakes

- Derived method name typos — Spring Data fails at startup but the
  error is verbose and easy to miss
- Forgetting JpaSpecificationExecutor<Product> on the repository
  interface — Specifications won't work
- Projection method returning the entity instead of the projection
  interface
- Using @Query for queries that derived methods handle natively