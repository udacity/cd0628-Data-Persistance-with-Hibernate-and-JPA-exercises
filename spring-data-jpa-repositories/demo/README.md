# Module 21 Demo: Spring Data JPA Repositories with a Task Tracker

A small Spring Boot app showing how much boilerplate Spring Data JPA takes off
your plate. One interface, no implementation, four different query styles.

## What this demo shows

The `TaskRepository` extends `JpaRepository<Task, Long>`, which gives us the
full CRUD surface for free (save, findById, findAll, delete, count). On top of
that we add four query styles:

1. **Derived queries** - method names get parsed into JPQL. `findByStatus`,
   `findByPriorityGreaterThanEqual`, `findByStatusAndDueDateBefore`,
   `countByStatus`.
2. **@Query with JPQL** - custom query, database-agnostic. Used for
   `findOverdueOpenTasks`.
3. **@Query with native SQL** - Postgres-specific query, useful when JPQL
   cannot express what you need. Used for `countByStatusNative`.
4. **Pageable + Sort** - built-in pagination and sorting via method
   parameters. No manual offset math.

## Files

- `Task.java` - the entity, with a Status enum
- `TaskRepository.java` - the repository interface (no impl class)
- `DemoRunner.java` - Spring Boot app that seeds data and runs each query
- `application.properties` - Spring Boot / DataSource / JPA config

## How to run

Uses the `banking` Postgres database via Docker. Schema is dropped and
recreated on each run.

```
mvn spring-boot:run
```
