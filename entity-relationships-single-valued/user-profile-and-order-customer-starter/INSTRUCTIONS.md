# Exercise: User Profile and Order Customer Mappings

## Overview

Two single-valued relationships in one project. User and UserProfile
share a primary key as a bidirectional `@OneToOne`. Order references
Customer as a `@ManyToOne` with LAZY fetch. You'll add four annotations
across four entities and watch a pre-written test verify cascade,
shared-PK behavior, and the difference between LAZY and JOIN FETCH.

## Exercise Instructions

Open the starter project and work through the TODOs in four entity
files. The repository code and tests are already written.

### Part 1: User and UserProfile (Bidirectional @OneToOne with @MapsId)

Open `User.java`.

**TODO 1: Annotate the `profile` field with `@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)`**
Makes User the inverse (non-owning) side. Saving a User cascades to its
UserProfile.

Open `UserProfile.java`.

**TODO 2: Annotate the `user` field with `@OneToOne`, plus `@MapsId`, plus `@JoinColumn(name = "id")`**
Makes UserProfile the owning side. `@MapsId` tells Hibernate to use the
parent User's id as UserProfile's own id (shared primary key). No
extra column is needed.

### Part 2: Order to Customer (@ManyToOne LAZY)

Open `Order.java`.

**TODO 3: Annotate the `customer` field with `@ManyToOne(fetch = FetchType.LAZY)`**
The Customer reference loads on demand, not at the time the Order is
loaded. This is what causes the N+1 unless you explicitly fetch.

**TODO 4: Add `@JoinColumn(name = "customer_id", nullable = false)` (on the same `customer` field)**
Names the foreign key column explicitly. `nullable = false` enforces
that every Order has a Customer.

Open `Customer.java`. No annotations to add here. The class is provided
for reference.

## Deliverable

`RelationshipTest` passes. SQL logs show:
- A single INSERT cascade when you save a User with its UserProfile
- A 1+N query pattern when iterating Orders without JOIN FETCH
- A single query when using the pre-written `findAllWithCustomer`
  JOIN FETCH method

## What's Included

- `User.java` with TODO 1
- `UserProfile.java` with TODO 2
- `Order.java` with TODOs 3 and 4
- `Customer.java`, complete
- `OrderRepository.java` with `findAllWithCustomer` JOIN FETCH already
  written
- `RelationshipTest.java`, pre-written
- `schema.sql` and `data.sql` for all four tables
- `application.yml` with SQL logging on

## Common Mistakes

- **Forgetting `@MapsId`:** UserProfile ends up with a separate auto-generated id, breaking the shared-PK design
- **Two owning sides on `@OneToOne`:** without `mappedBy` on one side, Hibernate creates two updates instead of one
- **`fetch = EAGER` as an N+1 "fix":** it just shifts when the queries fire, not how many. JOIN FETCH or `@EntityGraph` is the real fix
- **Accessing LAZY outside a transaction:** `LazyInitializationException`. Make sure your test method is `@Transactional`