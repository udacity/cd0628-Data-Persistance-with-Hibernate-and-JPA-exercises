package com.example.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

/**
 * Walks through the second-level cache lifecycle:
 *
 *   1. Seed a small country reference dataset in one session.
 *   2. Load "US" for the first time in a fresh session -> cache miss, SELECT fires.
 *   3. Load "US" again in another fresh session -> cache hit, NO SELECT.
 *   4. Print Hibernate's statistics to prove it.
 *
 * Two separate EntityManager sessions are used so the first-level cache is not
 * masking the second-level behavior. L1 lives inside a single EntityManager;
 * L2 lives inside the SessionFactory and is shared across sessions.
 */
public class DemoRunner {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("demoPU");

        // Enable stats so we can prove the hit vs miss story.
        SessionFactory sf = emf.unwrap(SessionFactory.class);
        Statistics stats = sf.getStatistics();
        stats.setStatisticsEnabled(true);

        try {
            seed(emf);
            sf.getCache().evictAllRegions();
            System.out.println("Cleared L2 cache to demonstrate cold reads.");
            System.out.println("---- 1. First read of 'US' (cache miss expected) ----");
            loadInFreshSession(emf, "US");
            printCacheStats(stats);

            System.out.println("---- 2. Second read of 'US' (cache hit expected) ----");
            loadInFreshSession(emf, "US");
            printCacheStats(stats);

            System.out.println("---- 3. Third read (still hit) + one uncached read ----");
            loadInFreshSession(emf, "US");
            loadInFreshSession(emf, "GB");
            printCacheStats(stats);
        } finally {
            emf.close();
        }
    }

    private static void seed(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(new Country("US", "United States",  "North America"));
        em.persist(new Country("GB", "United Kingdom", "Europe"));
        em.persist(new Country("JP", "Japan",          "Asia"));
        em.persist(new Country("BR", "Brazil",         "South America"));
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Fresh EntityManager per call so the L1 cache is empty every time. Any
     * hit we see is the L2 doing its job.
     */
    private static void loadInFreshSession(EntityManagerFactory emf, String code) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Country c = em.find(Country.class, code);
        System.out.println("  Loaded: " + c);
        em.getTransaction().commit();
        em.close();
    }

    private static void printCacheStats(Statistics stats) {
        System.out.println("  Second-level cache: " +
                stats.getSecondLevelCacheHitCount() + " hits, " +
                stats.getSecondLevelCacheMissCount() + " misses, " +
                stats.getSecondLevelCachePutCount() + " puts");
    }
}
