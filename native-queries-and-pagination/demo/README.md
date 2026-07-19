# Module 19 Demo: Native Queries and Efficient Pagination

Three query stories in one demo:

1. **Native SQL** with a Postgres-specific `string_agg` aggregate. When JPQL
   can't express a database feature, drop to `createNativeQuery`.
2. **Offset pagination** with `setFirstResult` + `setMaxResults`. Fine at
   shallow offsets, slow at deep ones because the database has to scan and
   discard every row before the offset.
3. **Keyset pagination** with a (created_at, id) cursor. Stays fast at any
   depth because the WHERE clause becomes a range predicate the database
   can serve straight out of the index.

## Files

- `Article.java` - simple entity with an indexed `created_at` column
- `DemoRunner.java` - seeds 8 articles then runs each variant

## How to run

Uses the `banking` Postgres database via Docker. Schema is dropped and
recreated on each run.

```
mvn compile
mvn exec:java
```
