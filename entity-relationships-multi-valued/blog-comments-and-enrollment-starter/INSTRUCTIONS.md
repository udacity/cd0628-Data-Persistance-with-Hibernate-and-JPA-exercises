# Exercise: Blog Comments and Student Enrollments

## What You'll Build

Annotate four entities so BlogPost ↔ Comment is one-to-many with
orphanRemoval, and Student ↔ Course is many-to-many via an Enrollment
intermediate entity.

## Requirements

- BlogPost has @OneToMany(mappedBy="post", cascade=ALL,
  orphanRemoval=true)
- Comment has @ManyToOne BlogPost as the owning side
- Enrollment has @ManyToOne to Student and @ManyToOne to Course
- The provided helpers addComment and addEnrollment are pre-written —
  you just need the annotations in place for them to work
- The pre-written RelationshipTest verifies cascade, orphan removal,
  and bidirectional consistency

## Starter Code

- PostgreSQL schema and seed data
- BlogPost, Comment, Student, Course, Enrollment — fields and helper
  methods complete; only relationship annotations have TODOs
- RelationshipTest pre-written, currently fails

## Verification

- All RelationshipTest methods pass
- DELETE SQL fires for comments when their post is deleted
- Helpers correctly maintain both sides

## Common Mistakes

- Direct @ManyToMany when extra columns are needed (use intermediate)
- orphanRemoval without cascade
- Forgetting mappedBy — Hibernate creates an extra join table
- equals/hashCode bugs in Sets