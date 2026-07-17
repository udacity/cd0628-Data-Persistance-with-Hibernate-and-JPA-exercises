package com.example.demo;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A musical artist. The inverse side of the Track-Artist ManyToMany
 * relationship (Track owns it via @JoinTable). Uses a Set because artists on
 * a track have no order and should not repeat.
 */
@Entity
@Table(name = "artist")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Inverse side. Points back at Track.artists so Hibernate knows this is the
    // same association, not a new one.
    @ManyToMany(mappedBy = "artists")
    private Set<Track> tracks = new HashSet<>();

    protected Artist() {}

    public Artist(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Set<Track> getTracks() { return tracks; }

    @Override
    public String toString() {
        return String.format("Artist[id=%d, %s]", id, name);
    }
}
