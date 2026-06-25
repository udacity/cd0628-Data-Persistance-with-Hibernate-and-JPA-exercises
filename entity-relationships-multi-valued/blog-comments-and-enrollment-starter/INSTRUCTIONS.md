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

Open the starter project and work through the TODOs across three
entity files.

### Part 1: BlogPost owns Comments

Open `BlogPost.java`.

**TODO 1: Add the comments collection**
Annotate a `List<Comment>` field with `@OneToMany(mappedBy = "blogPost",
cascade = CascadeType.ALL, orphanRemoval = true)`. The `mappedBy`
declares this side is the inverse; the Comment side owns the FK.
`orphanRemoval = true` means removing a Comment from this list
actually deletes that row.

Open `Comment.java`.

**TODO 2: Add the owning side**
Annotate a `BlogPost blogPost` field with `@ManyToOne(fetch = FetchType.LAZY)`
and `@JoinColumn(name = "blog_post_id")`. This is the owning side of
the relationship -- the FK column lives here.

### Part 2: Student/Course via Enrollment join entity

Open `Enrollment.java`.

**TODO 3: Add the two @ManyToOne fields**
Annotate `student` with `@ManyToOne` and `@JoinColumn(name = "student_id")`.
Annotate `course` with `@ManyToOne` and `@JoinColumn(name = "course_id")`.
The composite primary key (or the auto id you already see) ties them
together with the enrollment date.

## Deliverable

`RelationshipTest` passes:
- Adding a Comment to a BlogPost persists the comment
- Removing a Comment from the list deletes the row (orphanRemoval)
- Enrolling a Student in a Course creates an Enrollment row
- The enrollment carries its own enrolledOn date

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
- `Student.java` and `Course.java`, complete
- `Enrollment.java` with TODO 3
- `BlogPostRepository.java`, complete
- `RelationshipTest.java`, pre-written
- `schema.sql` and `data.sql`
- `application.yml` with SQL logging on

## Common Mistakes

- **Forgetting `mappedBy`:** Hibernate creates a SECOND FK column or a join table you didn't want. The non-owning side always uses `mappedBy`.
- **orphanRemoval vs CascadeType.REMOVE:** orphanRemoval deletes when you remove the child from the parent's collection. CascadeType.REMOVE deletes when you remove the parent. They're different triggers.
- **Many-to-many without a join entity:** works for simple cases but you can't attach metadata (like enrollment date) without a real entity in the middle.
