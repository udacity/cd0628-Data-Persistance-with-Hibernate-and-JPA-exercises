# Module 17 Demo: JPQL and Criteria API with a Product Catalog

Same question, two ways. Runs a static JPQL query and the equivalent Criteria
API query side by side so learners can see when to reach for each.

## What this demo shows

Seeds six products across two categories, then answers the question:

> "Give me all in-stock products in the 'electronics' category priced under
> $100, ordered by price ascending."

Twice:

1. **Static JPQL** - concise, readable, best when you know the query at
   compile time.
2. **Criteria API** - the same query built programmatically. Every filter is
   optional so the shape adapts at runtime. This is Criteria's real strength:
   dynamic queries where JPQL string concatenation would be brittle.

The console shows the SQL Hibernate generates from each. It's the same shape,
which is the point: Criteria and JPQL are two front ends to the same engine.

## Files

- `Product.java` - simple flat entity
- `DemoRunner.java` - seeds, runs both queries, prints results

## How to run

Uses the `banking` Postgres database via Docker. Schema is dropped and
recreated on each run.

```
mvn compile
mvn exec:java
```
