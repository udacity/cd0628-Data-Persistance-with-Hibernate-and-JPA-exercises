# Module 11 Demo: Advanced Entity Mappings with a Restaurant Menu

A small restaurant menu domain demonstrating three advanced mapping features:

- **@Embedded / @Embeddable** — `Money` is a value type embedded directly into
  the `MenuItem` table. Its `amount` and `currency` fields become columns on
  `menu_item`, no separate table.
- **@ElementCollection** — `List<String> ingredients` is stored in a side table
  `menu_item_ingredient` with a foreign key back to `menu_item`. Each ingredient
  is a row.
- **@Convert with AttributeConverter** — `Set<DietaryFlag>` is packed into a
  compact single-character string via `DietaryFlagsConverter`. On write, the
  set is joined into "VS" (Vegan + Spicy). On read, it is decoded back into a
  Set.

## Files

- `Money.java` - `@Embeddable` value type
- `DietaryFlag.java` - enum used in the converter example
- `DietaryFlagsConverter.java` - custom `AttributeConverter`
- `MenuItem.java` - the entity, wires all three features together
- `DemoRunner.java` - persists two menu items and reads one back

## How to run

Uses the `banking` Postgres database via Docker. Schema is auto-created via
`hibernate.hbm2ddl.auto=update`.

```
mvn compile
mvn exec:java
```
