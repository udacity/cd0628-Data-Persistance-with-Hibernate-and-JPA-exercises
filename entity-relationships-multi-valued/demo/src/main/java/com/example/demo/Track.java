package com.example.demo;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A single track on an album. Two relationships live here:
 *
 *   - ManyToOne back to Album. Track is the many side. The FK is on this table.
 *   - ManyToMany to Artist. Track owns this via @JoinTable. Artists on a track
 *     are unordered and unique, so we use a Set.
 */
@Entity
@Table(name = "track")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "duration_seconds", nullable = false)
    private int durationSeconds;

    // Many tracks belong to one album. Album is the parent.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    // Owning side of the ManyToMany. The join table is created from this
    // annotation. Artist.tracks has mappedBy = "artists" pointing back here.
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "track_artist",
            joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private Set<Artist> artists = new HashSet<>();

    protected Track() {}

    public Track(String title, int durationSeconds, Album album) {
        this.title = title;
        this.durationSeconds = durationSeconds;
        this.album = album;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public int getDurationSeconds() { return durationSeconds; }
    public Album getAlbum() { return album; }
    public Set<Artist> getArtists() { return artists; }

    public void addArtist(Artist artist) {
        this.artists.add(artist);
        artist.getTracks().add(this);
    }

    @Override
    public String toString() {
        return String.format("Track[id=%d, %s (%ds)]", id, title, durationSeconds);
    }
}
