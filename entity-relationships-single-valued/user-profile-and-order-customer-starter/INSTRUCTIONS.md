# Exercise: Map UserProfile and Order with Single-Valued Relationships

## Overview

In this exercise, you'll model two common single-valued JPA
relationships: a User and UserProfile sharing a primary key via
`@OneToOne` + `@MapsId`, and an Order with a lazy `@ManyToOne` to
Customer. You'll verify the relationships persist correctly and
confirm that LAZY fetching defers the customer load until the
field is actually accessed.

## Exercise Instructions

Open the starter project and work through the TODOs across two
entity files.

### Part 1: UserProfile with @OneToOne and @MapsId

Open `UserProfile.java`.

**TODO 1: Add the relationship to User**
Annotate the `user` field with `@OneToOne` and `@MapsId`. With
`@MapsId`, the UserProfile's primary key IS the User's primary key
(no separate id column), enforcing the 1:1 strict pairing at the
schema level.

### Part 2: Order with LAZY @ManyToOne to Customer

Open `Order.java`.

**TODO 2: Add the relationship to Customer**
Annotate the `customer` field with `@ManyToOne(fetch = FetchType.LAZY)`
and a `@JoinColumn(name = "customer_id")`. LAZY tells Hibernate to
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

- `User.java` and `Customer.java`, complete
- `UserProfile.java` with TODO 1
- `Order.java` with TODO 2
- `OrderRepository.java`, complete
- `RelationshipTest.java`, pre-written
- `schema.sql` and `data.sql`
- `application.yml` with SQL logging on

## Common Mistakes

- **@OneToOne without @MapsId creates a separate id column:** the schema fails because `id` is meant to be the foreign key, not its own sequence.
- **LAZY only works inside an active session:** accessing a lazy field after the session closes throws `LazyInitializationException`.
- **N+1 in the wild:** if a test loops orders and reads each customer, you'll see N+1 SELECTs even with LAZY. That's a separate problem (Module 6 fixes it).
