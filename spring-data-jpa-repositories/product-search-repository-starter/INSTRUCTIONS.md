# Exercise: Build a Spring Data JPA Repository Four Ways

## Overview

In this exercise, you'll expose the same product catalog through
four different Spring Data techniques: a derived method generated
from the method name, a `@Query`-annotated method, an interface
projection, and a dynamic Specification search. Each is a small,
isolated addition to the repository, and the goal is to see when
each technique is the right tool.


## Before You Start

This exercise connects to a PostgreSQL database named `banking` as the
`banking` user. If you haven't run the one-time workspace setup script
yet, run it from the repository root:

```
bash setup/setup-postgres.sh
```

The script is idempotent and safe to re-run. See `setup/SETUP.md` at
the repository root for details.

## Exercise Instructions

Open the starter project and work through the TODOs across two
files.

### Part 1: Derived Method

Open `ProductRepository.java`.

**TODO 1: Add a derived method signature**
Add a method that finds products by category and price under a
maximum. The Spring Data naming grammar lets you express this
without writing the query body.

### Part 2: @Query Method

Same file.

**TODO 2: Add `findTopSellersByMonth` annotated with `@Query`**
Write a JPQL query that joins Product to OrderItem, filters by
year and month, groups by product, and orders by quantity sold
descending. Returns `List<Product>`.

### Part 3: Interface Projection

Same file.

**TODO 3: Add `findSummariesByCategory` returning `List<ProductSummary>`**
The `ProductSummary` projection interface is already provided. By
returning the projection type instead of the entity, Spring Data
generates a narrower SELECT (just the three projection columns).

### Part 4: Specification-Based Search

Open `ProductSpecs.java`. Three Specification builders are stubbed.

**TODO 4: Implement `categoryIs(String category)`**
Return a Specification that adds an equality predicate for the
category field. Handle null category as a no-op spec.

**TODO 5: Implement `priceBetween(BigDecimal min, BigDecimal max)`**
Return a Specification that handles each null combination gracefully:
both null, only min, only max, or both.

**TODO 6: Implement `isInStock()`**
Fixed predicate, no parameters - just filter where inStock = true.

Open `ProductRepository.java`.

**TODO 7: Extend `JpaSpecificationExecutor<Product>`**
Add it to the `extends` clause alongside `JpaRepository`. Without
it, `findAll(Specification)` won't compile.

## Deliverable

`QueryTest` passes:
- Derived method returns products in the right category under the
  price ceiling
- `@Query` top-sellers returns products ordered by quantity sold
- Projection method's SQL shows only three columns selected
- Specification search applies only the non-null filters

Run the test with:

```
mvn test
```

Expected output ends with:

```
[INFO] BUILD SUCCESS
```

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

- **Derived method name typos:** Spring Data fails at startup with a long error like "No property xyz found for type Product" - read it carefully, it tells you which fragment didn't match.
- **Forgetting `JpaSpecificationExecutor<Product>`:** the `findAll(Specification)` method won't exist, and Specifications silently do nothing.
- **Projection method returning the entity instead of the projection interface:** you still get the full SELECT, defeating the projection's purpose.
- **Using `@Query` for queries that derived methods handle natively:** the derived approach is shorter and equally fast. Reserve `@Query` for things you can't express in the method name (joins, aggregates, complex predicates).
- **Returning `null` from a Specification builder when you mean "no predicate":** that works at the top level (`findAll((root, q, cb) -> null)`) but breaks when composing with `.and()`. Use a no-op spec instead.
