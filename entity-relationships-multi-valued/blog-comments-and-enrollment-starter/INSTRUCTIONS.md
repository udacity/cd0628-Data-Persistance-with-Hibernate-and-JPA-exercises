# Exercise: Map Comments and Enrollment with Multi-Valued Relationships

## Overview

In this exercise, you'll model two multi-valued JPA relationships
that come up constantly in real apps. A BlogPost owns a collection
of Comments via `@OneToMany` with `orphanRemoval`, where deleting
a Comment from the collection actually deletes the row. And a
Student/Course many-to-many is modeled with an explicit Enrollment
join entity, since it carries its own data (the enrollment date).


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

Open the starter project and work through the TODOs across four
entity files.

### Part 1: BlogPost owns Comments

Open `BlogPost.java`.

**TODO 1: Add the comments collection**
Annotate the `List<Comment>` field with `@OneToMany(mappedBy = "post",
cascade = CascadeType.ALL, orphanRemoval = true)`. The `mappedBy = "post"`
declares this side is the inverse; the Comment side owns the FK.
`orphanRemoval = true` means removing a Comment from this list
actually deletes that row.

Open `Comment.java`.

**TODO 2: Add the owning side**
Annotate the `post` field with `@ManyToOne` and
`@JoinColumn(name = "post_id", nullable = false)`. This is the owning
side of the relationship -- the FK column lives here.

### Part 2: Student/Course via Enrollment join entity

Open `Enrollment.java`.

**TODO 3: Add the @ManyToOne to Student**
Annotate the `student` field with `@ManyToOne` and
`@JoinColumn(name = "student_id", nullable = false)`.

**TODO 4: Add the @ManyToOne to Course**
Annotate the `course` field with `@ManyToOne` and
`@JoinColumn(name = "course_id", nullable = false)`. Together with
TODO 3, these two foreign keys plus the enrollment date make
Enrollment a real join entity rather than a hidden join table.

### Part 3: Student owns its Enrollments

Open `Student.java`.

**TODO 5: Add the enrollments collection**
Annotate the `List<Enrollment>` field with
`@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)`. This is
the inverse side -- Enrollment owns the `student_id` FK (TODO 3). It
lets you navigate from a Student to all their enrollments.

## Deliverable

`RelationshipTest` passes:
- Adding a Comment to a BlogPost persists the comment
- Removing a Comment from the list deletes the row (orphanRemoval)
- Enrolling a Student in a Course creates an Enrollment row
- The enrollment carries its own enrollment date

Run the test with:

```
mvn test
```

Expected output ends with:

```
[INFO] BUILD SUCCESS
```

## What's Included

- `BlogPost.java` with TODO 1
- `Comment.java` with TODO 2
- `Enrollment.java` with TODOs 3-4
- `Student.java` with TODO 5
- `Course.java`, complete
- `BlogPostRepository.java`, complete
- `RelationshipTest.java`, pre-written
- `schema.sql` and `data.sql`
- `application.yml` with SQL logging on

## Common Mistakes

- **Forgetting `mappedBy`:** Hibernate creates a SECOND FK column or a join table you didn't want. The non-owning side always uses `mappedBy`.
- **orphanRemoval vs CascadeType.REMOVE:** orphanRemoval deletes when you remove the child from the parent's collection. CascadeType.REMOVE deletes when you remove the parent. They're different triggers.
- **Many-to-many without a join entity:** works for simple cases but you can't attach metadata (like enrollment date) without a real entity in the middle.