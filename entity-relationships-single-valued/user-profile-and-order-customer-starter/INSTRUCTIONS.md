# Exercise: Map UserProfile and Order with Single-Valued Relationships

## Overview

In this exercise, you'll model two common single-valued JPA
relationships: a User and UserProfile sharing a primary key via
`@OneToOne` + `@MapsId`, and an Order with a lazy `@ManyToOne` to
Customer. You'll verify the relationships persist correctly and
confirm that LAZY fetching defers the customer load until the
field is actually accessed.


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

Open the starter project and work through the TODOs across three
entity files.

### Part 1: The User side of the @OneToOne

Open `User.java`.

**TODO 1: Add the inverse side of the relationship to UserProfile**
Annotate the `profile` field with
`@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)`.
`mappedBy` marks User as the inverse (non-owning) side -- UserProfile
owns the foreign key. `cascade = ALL` means persisting a User also
persists its profile.

### Part 2: The UserProfile side with @MapsId

Open `UserProfile.java`.

**TODO 2: Add the owning side of the relationship to User**
Annotate the `user` field with three annotations: `@OneToOne`,
`@MapsId`, and `@JoinColumn(name = "id")`. With `@MapsId`, the
UserProfile's primary key IS the User's primary key (no separate id
column), enforcing the strict 1:1 pairing at the schema level.

### Part 3: Order with LAZY @ManyToOne to Customer

Open `Order.java`.

**TODO 3 and TODO 4: Add the lazy relationship to Customer**
Annotate the `customer` field with
`@ManyToOne(fetch = FetchType.LAZY)` (TODO 3) and a
`@JoinColumn(name = "customer_id")` (TODO 4). LAZY tells Hibernate to
issue the SELECT for Customer only when the field is actually
accessed, not at order load time.

## Deliverable

`RelationshipTest` passes:
- UserProfile shares the same id as its User
- Loading a User loads its profile via @MapsId
- Loading an Order does NOT immediately load its Customer (lazy)
- Accessing order.getCustomer() triggers a separate SELECT

Run the test with:

```
mvn test
```

Expected output ends with:

```
[INFO] BUILD SUCCESS
```

The SQL log shows the lazy customer SELECT firing on demand, not
at order load time.

## What's Included

- `User.java` with TODO 1
- `Customer.java`, complete
- `UserProfile.java` with TODO 2
- `Order.java` with TODOs 3-4
- `OrderRepository.java`, complete
- `RelationshipTest.java`, pre-written
- `schema.sql` and `data.sql`
- `application.yml` with SQL logging on

## Common Mistakes

- **@OneToOne without @MapsId creates a separate id column:** the schema fails because `id` is meant to be the foreign key, not its own sequence.
- **LAZY only works inside an active session:** accessing a lazy field after the session closes throws `LazyInitializationException`.
- **N+1 in the wild:** if a test loops orders and reads each customer, you'll see N+1 SELECTs even with LAZY. That's a separate problem (Module 6 fixes it).