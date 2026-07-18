# Module 15 Demo: N+1 Detection and Resolution with a Library

Two entities, a parent and children, wired for lazy loading so we can reproduce
the N+1 problem on purpose and then fix it two different ways.

## What this demo shows

Seeds three authors, each with three books, then runs three separate queries:

1. **Naive query** - loads authors, then iterates and touches
   `author.getBooks()`. Because the collection is lazy, each touch fires a
   separate SELECT. Total: 1 + N queries.
2. **JOIN FETCH** - a single JPQL query with `JOIN FETCH a.books` pulls
   authors and books together in one round trip.
3. **EntityGraph** - declare a fetch graph, attach it to a normal query via
   the `jakarta.persistence.fetchgraph` hint. Same result at the SQL level as
   JOIN FETCH but usable across multiple queries.

Watch the SQL in the console to see the query counts. The first section fires
four SELECTs (one authors, then three books). The other two fire one SELECT
each.

## Files

- `Author.java` - parent entity with a lazy `List<Book>`
- `Book.java` - child entity with @ManyToOne back to Author
- `DemoRunner.java` - seeds the graph, runs three variants, prints results

## How to run

Uses the `banking` Postgres database via Docker. Schema is dropped and
recreated on each run via `hibernate.hbm2ddl.auto=create-drop`.

```
mvn compile
mvn exec:java
```
