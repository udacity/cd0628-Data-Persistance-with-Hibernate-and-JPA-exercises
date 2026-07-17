# Module 7 Demo: One-to-One and Many-to-One Mappings with a Vet Clinic

A small vet clinic domain demonstrating both single-valued relationship types.
The domain is intentionally different from the exercise so learners see the same
concepts applied to fresh code.

## Relationships shown

- **Many-to-One**: many `Pet`s belong to one `Owner`. The foreign key lives on
  `Pet` via `@JoinColumn(name = "owner_id")`.
- **One-to-One**: each `Pet` has one `MedicalRecord`. The foreign key lives on
  `Pet` via `@JoinColumn(name = "medical_record_id")` with `unique = true`.

`Pet` owns both relationships to keep the demo focused. `Pet` also uses
`cascade = CascadeType.ALL` on the OneToOne so persisting a Pet also persists
its MedicalRecord.

## Files

- `Owner.java` - simple entity, one side of the ManyToOne
- `Pet.java` - owning side of both relationships
- `MedicalRecord.java` - simple entity, other side of the OneToOne
- `DemoRunner.java` - creates an Owner with two Pets (each with a MedicalRecord),
  then loads a Pet and traverses both associations

## How to run

Uses the `banking` Postgres database via Docker. Schema is auto-created via
`hibernate.hbm2ddl.auto=update`.

```
mvn compile
mvn exec:java
```
