# Exercise: Build a Spring Data JPA Repository Four Ways

## Overview

The same product catalog exposed through four different Spring Data
techniques: a derived method (generated from method name), a `@Query`
annotated method, an interface projection, and a dynamic Specification
search. Each is a focused, isolated addition to the repository. The
goal is to see when each technique is the right tool.

## Exercise Instructions

Open the starter project and work through the TODOs across three
files.

### Part 1: Derived Method

Open `ProductRepository.java`.

**TODO 1: Add a derived method signature: `findByCategoryAndPriceLessThan`**
Just declare the method on the interface — Spring Data implements it
from the method name alone. The method takes `String category` and
`BigDecimal maxPrice`, returns `List<Product>`. No body, no `@Query`
annotation needed.

### Part 2: @Query Method

Same file.

**TODO 2: Add `findTopSellersByMonth` annotated with `@Query`**
Write a JPQL query that joins `Product` to `OrderItem`, filters by
month, groups by product, and orders by quantity sold descending.
Returns `List<Product>` for the top sellers in a given month.

### Part 3: Interface Projection

Same file.

**TODO 3: Add `findSummariesByCategory` returning `List<ProductSummary>`**
The `ProductSummary` projection interface is provided. It exposes
`id`, `name`, and `price` only. Spring Data generates a narrower SELECT
clause (just those three columns) when the method returns the
projection type instead of the entity.

### Part 4: Specification-Based Search

Open `ProductSpecs.java`. Three Specification builders are stubbed.

**TODO 4: Implement `categoryIs(String category)`**
Return a `Specification<Product>` that adds an equality predicate for
the `category` field. If `category` is null, return a no-op spec
(`(root, query, cb) -> null`).

**TODO 5: Implement `priceBetween(BigDecimal min, BigDecimal max)`**
Return a Specification that adds a `between` predicate for `price`.
Handle nullable min and max gracefully — if both are null, return a
no-op spec.

**TODO 6: Implement `isInStock()`**
Return a Specification that filters where `inStock = true`. This one
takes no parameters — it's a fixed predicate.

Open `ProductRepository.java`.

**TODO 7: Extend `JpaSpecificationExecutor<Product>`**
Add `JpaSpecificationExecutor<Product>` to the `extends` clause of the
ProductRepository interface (alongside `JpaRepository`). Without it,
`findAll(Specification)` won't be available and Specifications will
silently fail.

## Deliverable

`QueryTest` passes:
- Derived method returns products in the right category under the
  price ceiling
- `@Query` top-sellers returns products ordered by quantity sold
- Projection method's SQL shows only three columns selected
- Specification search applies only the non-null filters

## What's Included

- `ProductRepository.java` with TODOs 1, 2, 3, 7
- `ProductSpecs.java` with TODOs 4, 5, 6
- `ProductSummary.java`, complete interface projection
- `Product.java` and `OrderItem.java`, complete
- `ProductSearchSpec.java` record, complete (the input DTO for the
  search service)
- `QueryTest.java`, pre-written
- `schema.sql` and `data.sql` (200 products across 5 categories,
  1,000 order items)
- `application.yml` with SQL logging on

## Common Mistakes

- **Derived method name typos:** Spring Data fails at startup with a long error like "No property xyz found for type Product" — read it carefully, it tells you which fragment didn't match
- **Forgetting `JpaSpecificationExecutor<Product>`:** the `findAll(Specification)` method won't exist, and Specifications silently do nothing
- **Projection method returning the entity instead of the projection interface:** you still get the full SELECT, defeating the projection's purpose
- **Using `@Query` for queries that derived methods handle natively:** the derived approach is shorter and equally fast. Reserve `@Query` for things you can't express in the method name (joins, aggregates, complex predicates)
- **Returning `null` from a Specification builder when you mean "no predicate":** that works at the top level (`findAll((root, q, cb) -> null)`) but breaks when composing with `.and()`. Use a no-op spec instead