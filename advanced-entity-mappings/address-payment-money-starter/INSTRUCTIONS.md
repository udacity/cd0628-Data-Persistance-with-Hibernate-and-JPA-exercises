# Exercise: Map Address, Payment, and Money for an Order System

## Overview

In this exercise, you'll go beyond simple entities and model three
advanced mapping patterns: `@Embeddable` value types for Address,
JOINED inheritance for a Payment hierarchy with two subtypes
(CreditCard and BankTransfer), and a custom `AttributeConverter`
to persist a Money value object through a BigDecimal column. You'll
verify the schema is shaped correctly and that the inheritance
dispatches to the right table on read.


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

Open the starter project and work through the TODOs across seven
files.

### Part 1: Address as @Embeddable

Open `Address.java`.

**TODO 1: Annotate the class with `@Embeddable`**
Embeddable types are value objects -- they don't have an id of
their own, they live inside an owning entity's row.

Open `Customer.java`.

**TODO 2: Annotate the address field with `@Embedded`**
Marks the field as a value composition. The address columns will be
inlined into the customer table.

### Part 2: Two Addresses on Order with @AttributeOverrides

Open `Order.java`.

**TODO 3 and TODO 4: Annotate the shipping and billing address fields**
Each field is `@Embedded`, but since both reuse the same `Address`
columns you must add `@AttributeOverrides` to remap them to distinct
column names (e.g. `shipping_street` and `billing_street`). Without
the overrides the two addresses collide on the same columns.

### Part 3: Payment Inheritance (JOINED)

Open `Payment.java`.

**TODO 5: Annotate as `@Entity` with JOINED inheritance**
Add `@Inheritance(strategy = InheritanceType.JOINED)`. Each subtype
gets its own table; the parent table holds the shared columns.

Open `CreditCardPayment.java` and `BankTransferPayment.java`.

**TODO 6 and TODO 7: Annotate each subclass with `@Entity`**
Both already extend Payment. Annotating them as entities gives each
its own JOINED child table (one `payment` table plus a child table
per subtype).

### Part 4: Money via AttributeConverter

Open `MoneyConverter.java`.

**TODO 8, TODO 9, TODO 10: Annotate `@Converter(autoApply = true)` and implement the conversion**
The converter stores a `Money` object's amount as a `BigDecimal`
column (TODO 9) and rebuilds a `Money` on read, defaulting the
currency to "USD" (TODO 10). Return null for null input on both
directions. Add `@Converter(autoApply = true)` (TODO 8) so JPA picks
it up for every Money field automatically.

## Deliverable

`SchemaInspectionTest` passes:
- The customer table has flattened address columns (street, city, etc.)
- The orders table has prefixed shipping and billing address columns
- The payment table exists with shared columns
- credit_card_payment and bank_transfer_payment exist as JOINED tables
- Money values round-trip correctly through the converter

Run the test with:

```
mvn test
```

Expected output ends with:

```
[INFO] BUILD SUCCESS
```

## What's Included

- `Address.java` with TODO 1
- `Customer.java` with TODO 2
- `Order.java` with TODOs 3-4 (shipping and billing `@AttributeOverrides`)
- `Payment.java` with TODO 5
- `CreditCardPayment.java` and `BankTransferPayment.java` with TODOs 6-7
- `Money.java` and `MoneyConverter.java` with TODOs 8-10
- `SchemaInspectionTest.java`, pre-written
- `application.yml` with SQL logging and `ddl-auto: create`

## Common Mistakes

- **`@Embeddable` without `@Embedded` on the field:** Hibernate may treat the type as a separate entity and fail at startup.
- **Two embedded addresses without `@AttributeOverrides`:** both `Address` fields map to the same columns and collide. Override the column names so shipping and billing get their own.
- **JOINED inheritance and missing child tables:** each subtype declares its own table via `@Table(name = ...)`; missing that gives you implicit table names you may not want.
- **AttributeConverter forgetting `@Converter(autoApply = true)`:** then every Money field needs `@Convert(converter = MoneyConverter.class)` explicitly. autoApply removes the boilerplate.
- **JOINED is great for clean schemas, slow for deep hierarchies:** every read JOINs the parent and the child table. Worth knowing for Module 5's discussion.