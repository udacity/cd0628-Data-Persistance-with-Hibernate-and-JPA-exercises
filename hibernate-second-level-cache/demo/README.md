# Module 13 Demo: Hibernate Second-Level Cache with cache2k

A small country reference dataset showing how the Hibernate L2 cache behaves.
Reference data like country codes, currency codes, or product categories is
the classic L2 use case: read many times per request, changes rarely.

## What this demo shows

Seeds four countries then loads `US` three times from separate EntityManager
sessions and `GB` once from a fourth:

1. First `US` load - cache miss, SELECT fires
2. Second `US` load - cache hit, NO SELECT
3. Third `US` load - cache hit again
4. First `GB` load - cache miss

Fresh EntityManagers each time so the first-level cache is empty. Any hit we
see is the second-level cache doing its job.

The Statistics printout confirms the counts of hits, misses, and puts.

## Files

- `Country.java` - entity marked with `@Cacheable` and Hibernate `@Cache`
- `DemoRunner.java` - seeds, then loads across separate sessions
- `persistence.xml` - wires up the JCache region factory with cache2k

## Cache provider

Hibernate ships a JCache (JSR-107) integration via `hibernate-jcache`.
This demo uses **cache2k** as the JSR-107 provider - it plays nicely with
current Hibernate releases without the dependency compatibility issues you
can hit with older Ehcache builds.

## How to run

Uses the `banking` Postgres database via Docker. Schema is dropped and
recreated on each run.

```
mvn compile
mvn exec:java
```
