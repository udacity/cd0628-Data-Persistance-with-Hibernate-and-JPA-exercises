package com.example.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Three pagination stories in one demo:
 *
 *   1. A native SQL query. When you need a database-specific feature that JPQL
 *      cannot express, you drop to native SQL via createNativeQuery.
 *   2. Offset pagination. Convenient and readable. Fine at small offsets.
 *      Falls apart at deep offsets because the database has to scan and
 *      discard every row before the OFFSET point.
 *   3. Keyset pagination. Instead of "skip 2000000 rows", we say "give me the
 *      next N rows after the last-seen created_at plus id". The database can
 *      seek directly using the index and return in constant time regardless of
 *      how deep we are.
 */
public class DemoRunner {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("demoPU");
        try {
            seed(emf);

            System.out.println("---- 1. Native SQL query ----");
            runNativeQuery(emf);

            System.out.println("---- 2. Offset pagination (page 1) ----");
            runOffsetPage(emf, 0, 3);

            System.out.println("---- 3. Keyset pagination (page 1) ----");
            KeysetCursor cursor = runKeysetPage(emf, null, null, 3);

            System.out.println("---- 4. Keyset pagination (page 2, using cursor from page 1) ----");
            runKeysetPage(emf, cursor.lastCreatedAt, cursor.lastId, 3);
        } finally {
            emf.close();
        }
    }

    private static void seed(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        LocalDateTime base = LocalDateTime.of(2026, 7, 1, 9, 0);

        String[] titles = {
                "Postgres tuning basics",
                "Reactive Java in practice",
                "Testing Hibernate the smart way",
                "Sizing a connection pool",
                "Choosing a caching strategy",
                "Idempotency in distributed systems",
                "How to interview a senior engineer",
                "The truth about NoSQL"
        };
        String[] authors = {"Amelia", "Ben", "Cara", "Dev"};

        for (int i = 0; i < titles.length; i++) {
            em.persist(new Article(
                    titles[i],
                    authors[i % authors.length],
                    base.plusHours(i)));
        }
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Native SQL. Note the vendor-specific string_agg call. That kind of
     * function is exactly when you need to drop below JPQL.
     */
    private static void runNativeQuery(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Query q = em.createNativeQuery(
                "SELECT author, COUNT(*) AS c, string_agg(title, ', ') AS titles " +
                "FROM article GROUP BY author ORDER BY author");

        @SuppressWarnings("unchecked")
        List<Object[]> rows = q.getResultList();
        for (Object[] row : rows) {
            System.out.println("  " + row[0] + " (" + row[1] + "): " + row[2]);
        }

        em.getTransaction().commit();
        em.close();
    }

    /**
     * Offset pagination. Fine at page 1. Gets slow when the offset is huge.
     */
    private static void runOffsetPage(EntityManagerFactory emf, int offset, int pageSize) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        List<Article> page = em.createQuery(
                "SELECT a FROM Article a ORDER BY a.createdAt DESC, a.id DESC",
                Article.class)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();

        page.forEach(a -> System.out.println("  " + a));

        em.getTransaction().commit();
        em.close();
    }

    /**
     * Keyset pagination. The cursor is (lastCreatedAt, lastId). Null on the
     * very first page. Stays fast at any depth because the WHERE clause is a
     * range predicate the database can serve straight out of the index.
     */
    private static KeysetCursor runKeysetPage(EntityManagerFactory emf,
                                              LocalDateTime lastCreatedAt,
                                              Long lastId,
                                              int pageSize) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        List<Article> page;
        if (lastCreatedAt == null) {
            page = em.createQuery(
                    "SELECT a FROM Article a ORDER BY a.createdAt DESC, a.id DESC",
                    Article.class)
                    .setMaxResults(pageSize)
                    .getResultList();
        } else {
            page = em.createQuery(
                    "SELECT a FROM Article a " +
                    "WHERE a.createdAt < :lastCreatedAt " +
                    "   OR (a.createdAt = :lastCreatedAt AND a.id < :lastId) " +
                    "ORDER BY a.createdAt DESC, a.id DESC",
                    Article.class)
                    .setParameter("lastCreatedAt", lastCreatedAt)
                    .setParameter("lastId", lastId)
                    .setMaxResults(pageSize)
                    .getResultList();
        }

        page.forEach(a -> System.out.println("  " + a));

        em.getTransaction().commit();
        em.close();

        if (page.isEmpty()) return new KeysetCursor(null, null);
        Article last = page.get(page.size() - 1);
        return new KeysetCursor(last.getCreatedAt(), last.getId());
    }

    /** Small holder for the (created_at, id) cursor between pages. */
    private record KeysetCursor(LocalDateTime lastCreatedAt, Long lastId) {}
}
