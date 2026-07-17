package com.example.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;

/**
 * Persists an Album with several Tracks (OneToMany) and links each Track to
 * one or more Artists (ManyToMany). Then reads the album back and traverses
 * every relationship so viewers can see the fetches Hibernate emits.
 */
public class DemoRunner {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("demoPU");

        try {
            Long albumId = createAlbumWithTracksAndArtists(emf);
            readAlbumBack(emf, albumId);
        } finally {
            emf.close();
        }
    }

    private static Long createAlbumWithTracksAndArtists(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Album album = new Album("Late Night Sessions", LocalDate.of(2026, 5, 20));

        Artist nova   = new Artist("Nova Ray");
        Artist casper = new Artist("Casper Lin");
        Artist maren  = new Artist("Maren Ito");

        Track t1 = new Track("Opening", 210, album);
        t1.addArtist(nova);

        Track t2 = new Track("Drift", 245, album);
        t2.addArtist(nova);
        t2.addArtist(casper);

        Track t3 = new Track("Late Reply", 198, album);
        t3.addArtist(maren);

        album.addTrack(t1);
        album.addTrack(t2);
        album.addTrack(t3);

        // Only the album is passed to persist. Tracks cascade from the album,
        // artists cascade from each track.
        em.persist(album);

        em.getTransaction().commit();
        em.close();

        System.out.println("Persisted album " + album + " with " +
                album.getTracks().size() + " tracks.");
        return album.getId();
    }

    private static void readAlbumBack(EntityManagerFactory emf, Long albumId) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Album album = em.find(Album.class, albumId);
        System.out.println("Loaded: " + album);

        // Access the tracks list to trigger the OneToMany fetch.
        System.out.println("Tracks (" + album.getTracks().size() + "):");
        for (Track t : album.getTracks()) {
            // Access artists to trigger the ManyToMany fetch (one per track).
            System.out.println("  " + t + " by " + t.getArtists());
        }

        em.getTransaction().commit();
        em.close();
    }
}
