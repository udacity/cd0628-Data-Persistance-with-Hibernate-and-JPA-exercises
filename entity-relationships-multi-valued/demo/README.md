# Module 9 Demo: One-to-Many and Many-to-Many Mappings with a Music Streaming Domain

A small music streaming domain demonstrating both multi-valued relationship
types. The domain is intentionally different from the exercise so learners see
the same concepts applied to fresh code.

## Relationships shown

- **One-to-Many**: `Album` has many `Track`s. The Album owns the collection via
  `@OneToMany(mappedBy = "album")` and Track holds the foreign key back to
  Album with a `@ManyToOne`. Tracks are ordered on the album, so we use
  `@OrderColumn(name = "position")` to make this an indexed list rather than a
  bag. This avoids Hibernate's delete-all + reinsert behavior when the list
  changes.
- **Many-to-Many**: `Track` has many `Artist`s. Track owns the relationship via
  `@JoinTable(name = "track_artist")`. Artist is the inverse side with
  `@ManyToMany(mappedBy = "artists")`. We use `Set<Artist>` because artists on
  a track have no inherent order and should not repeat.

## Files

- `Album.java` - owns the OneToMany. Uses `@OrderColumn` and cascade + orphan
  removal.
- `Track.java` - the many side of the OneToMany, plus owning side of the
  ManyToMany with `@JoinTable`.
- `Artist.java` - the inverse side of the ManyToMany, points back at
  `Track.artists`.
- `DemoRunner.java` - persists one album with three tracks and three artists
  (with shared artists across tracks), then loads and traverses the whole graph.

## How to run

Uses the `banking` Postgres database via Docker. Schema is auto-created via
`hibernate.hbm2ddl.auto=update`.

```
mvn compile
mvn exec:java
```
