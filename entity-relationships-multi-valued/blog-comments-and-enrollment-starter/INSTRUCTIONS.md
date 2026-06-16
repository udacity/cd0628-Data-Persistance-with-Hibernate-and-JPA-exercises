# Exercise: Blog Comments and Student Enrollments

## Overview

Two multi-valued relationships in one project. BlogPost has many Comments
with `orphanRemoval`. Student and Course have a many-to-many relationship
through an Enrollment intermediate entity that carries an
`enrollmentDate`. You'll add five annotations across five entities and
watch a pre-written test verify cascade, orphan removal, and
bidirectional consistency.

## Exercise Instructions

Open the starter project and work through the TODOs in five entity files.
The helper methods and tests are already written.

### Part 1: BlogPost and Comment (@OneToMany with orphanRemoval)

Open `BlogPost.java`.

**TODO 1: Annotate the `comments` field with `@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)`**
- `mappedBy = "post"` says Comment owns the FK
- `cascade = ALL` propagates saves and deletes
- `orphanRemoval = true` means removing a Comment from this collection
  triggers a DELETE on that Comment

Open `Comment.java`.

**TODO 2: Annotate the `post` field with `@ManyToOne`, plus `@JoinColumn(name = "post_id", nullable = false)`**
Comment is the owning side. The `post_id` column carries the FK to
blog_post.

### Part 2: Student, Course, Enrollment (@ManyToMany via Intermediate Entity)

Open `Enrollment.java`.

**TODO 3: Annotate the `student` field with `@ManyToOne`, plus `@JoinColumn(name = "student_id", nullable = false)`**
The student side of the join.

**TODO 4: Annotate the `course` field with `@ManyToOne`, plus `@JoinColumn(name = "course_id", nullable = false)`**
The course side.

Open `Student.java`.

**TODO 5: Annotate the `enrollments` field with `@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)`**
Lets you navigate from a Student to all their Enrollments. The
`addEnrollment` helper method (pre-written) uses this collection.

## Deliverable

`RelationshipTest` passes. SQL logs show DELETE statements when:
- A BlogPost is deleted (cascade)
- A Comment is removed from `blogPost.getComments()` (orphan removal)

You can navigate `student.getEnrollments()` and
`course.getEnrollments()` and see consistent state from both sides.

## What's Included

- `BlogPost.java` with TODO 1, plus pre-written `addComment` helper
- `Comment.java` with TODO 2
- `Student.java` with TODO 5, plus pre-written `addEnrollment` helper
- `Course.java`, complete
- `Enrollment.java` with TODOs 3-4
- `RelationshipTest.java`, pre-written
- `schema.sql` and `data.sql` for all five tables
- `application.yml` with SQL logging on

## Common Mistakes

- **Using `@ManyToMany` directly with `@JoinTable`:** works until you need extra columns like `enrollmentDate`. Migrating later is painful. Use an intermediate entity from the start.
- **Setting only one side of the relationship in tests:** the inverse collection looks empty. Use the pre-written helpers, which set both sides.
- **`orphanRemoval` without `cascade`:** leaves orphaned rows in the child table when the parent is deleted
- **Missing `mappedBy`:** Hibernate quietly creates an extra join table you didn't ask for
- **equals/hashCode on entities used in Sets:** if you compare by `@Id` and the id is null pre-persist, you get duplicate-by-identity bugs