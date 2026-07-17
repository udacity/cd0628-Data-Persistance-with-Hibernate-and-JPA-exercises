package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * An album. Owns a OneToMany of Tracks. Tracks have a natural order (position
 * on the album), so we use @OrderColumn to make this an indexed list, not a bag.
 * Without @OrderColumn, Hibernate would treat this as a bag and would fall back
 * to delete-all + insert-all when we add or remove tracks.
 */
@Entity
@Table(name = "album")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    // mappedBy tells Hibernate the "album" field on Track owns this relationship.
    // Cascade ALL so persisting the album persists its tracks and orphanRemoval so
    // clearing the list also deletes the track rows.
    @OneToMany(mappedBy = "album",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @OrderBy("id")
    private List<Track> tracks = new ArrayList<>();

    protected Album() {}

    public Album(String title, LocalDate releaseDate) {
        this.title = title;
        this.releaseDate = releaseDate;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public List<Track> getTracks() { return tracks; }

    public void addTrack(Track track) {
        this.tracks.add(track);
    }

    @Override
    public String toString() {
        return String.format("Album[id=%d, %s, released %s]", id, title, releaseDate);
    }
}
