# Exercise: ID Strategies and Lifecycle Transitions

## Overview

Two JPA entities, Customer and Order, with different ID generation
strategies. You'll add the right annotations, then watch a pre-written
test walk both entities through every lifecycle state. The point is to
see how IDENTITY and SEQUENCE generate IDs at different times in the
transaction, and to recognize each lifecycle transition by its SQL.

## Exercise Instructions

Open the starter project and work through the TODOs in two entity files.
H2 is used in-memory, so no Postgres setup is needed.

### Part 1: Customer Entity

Open `Customer.java`.

**TODO 1: Annotate the `id` field with `@Id`**
Marks the id field as the primary key.

**TODO 2: On the same `id` field, add `@GeneratedValue(strategy = GenerationType.IDENTITY)`**
Tells Hibernate to let the database assign the ID via an auto-increment
column. The INSERT will fire immediately on `persist()` because
Hibernate needs the row to learn the ID.

### Part 2: Order Entity

Open `Order.java`.

**TODO 3: Annotate the `id` field with `@Id`**
Same as Customer.

**TODO 4: On the same `id` field, add `@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")`**
Tells Hibernate to use a database sequence to assign IDs.

**TODO 5: On the same `id` field, add `@SequenceGenerator(name = "order_seq", sequenceName = "order_sequence", allocationSize = 50)`**
Defines the sequence Hibernate calls. `allocationSize = 50` lets
Hibernate batch insert orders efficiently.

## Deliverable

`LifecycleTest` passes, and the SQL logs show the timing difference:
IDENTITY INSERT fires on `persist()`, SEQUENCE INSERT fires at flush
(or commit).

## What's Included

- `Customer.java` with TODOs 1-2
- `Order.java` with TODOs 3-5
- `LifecycleTest.java`, complete, walks each entity through
  persist → detach → merge → remove
- `application.yml`, H2 in-memory with SQL logging on

## Common Mistakes

- **`@SequenceGenerator` missing:** Hibernate uses a default named "hibernate_sequence" and you get unexpected names
- **persist on a managed entity:** silent no-op (not an error)
- **detach vs remove confusion:** detach only affects the session; remove emits DELETE
- **`@Id` on a wrapper type without `nullable=false`:** can hide bugs where the id never gets set