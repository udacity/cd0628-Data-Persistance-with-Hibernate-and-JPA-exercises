package com.example.demo;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.time.Year;
import java.util.List;

/**
 * Reproduces the N+1 problem and then shows two ways to fix it.
 *
 * Setup: three authors, each with three books.
 *
 * Section 1: the naive query.
 *     1 SELECT to load all authors.
 *     Then for each author, touching author.getBooks() fires another SELECT.
 *     Result: 1 + 3 = 4 SELECT statements total. That's the classic N+1.
 *
 * Section 2: fix with JOIN FETCH.
 *     A single JPQL query with join fetch pulls authors and books together.
 *     Result: 1 SELECT total.
 *
 * Section 3: fix with EntityGraph.
 *     Same result at the query level but declared as a graph attribute of
 *     the find call. Handy when you want to reuse the graph across queries.
 */
public class DemoRunner {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("demoPU");
        try {
            seed(emf);

            System.out.println("---- 1. Naive query (N+1) ----");
            naiveQuery(emf);

            System.out.println("---- 2. JOIN FETCH ----");
            joinFetchQuery(emf);

            System.out.println("---- 3. EntityGraph ----");
            entityGraphQuery(emf);
        } finally {
            emf.close();
        }
    }

    private static void seed(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Author austen = new Author("Jane Austen");
        austen.addBook(new Book("Pride and Prejudice", Year.of(1813)));
        austen.addBook(new Book("Emma",                Year.of(1815)));
        austen.addBook(new Book("Persuasion",          Year.of(1817)));

        Author orwell = new Author("George Orwell");
        orwell.addBook(new Book("Animal Farm",         Year.of(1945)));
        orwell.addBook(new Book("1984",                Year.of(1949)));
        orwell.addBook(new Book("Homage to Catalonia", Year.of(1938)));

        Author leguin = new Author("Ursula K. Le Guin");
        leguin.addBook(new Book("A Wizard of Earthsea",  Year.of(1968)));
        leguin.addBook(new Book("The Left Hand of Darkness", Year.of(1969)));
        leguin.addBook(new Book("The Dispossessed",      Year.of(1974)));

        em.persist(austen);
        em.persist(orwell);
        em.persist(leguin);

        em.getTransaction().commit();
        em.close();
    }

    private static void naiveQuery(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // One query to load the authors.
        List<Author> authors = em.createQuery(
                "SELECT a FROM Author a", Author.class).getResultList();

        for (Author a : authors) {
            // Each of these triggers a separate SELECT because books is LAZY.
            System.out.println(a + " has " + a.getBooks().size() + " books.");
        }

        em.getTransaction().commit();
        em.close();
    }

    private static void joinFetchQuery(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // Single JPQL query, one round trip.
        List<Author> authors = em.createQuery(
                "SELECT DISTINCT a FROM Author a JOIN FETCH a.books",
                Author.class).getResultList();

        for (Author a : authors) {
            System.out.println(a + " has " + a.getBooks().size() + " books.");
        }

        em.getTransaction().commit();
        em.close();
    }

    private static void entityGraphQuery(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // Declare the graph, then attach it to the query via a hint.
        EntityGraph<Author> graph = em.createEntityGraph(Author.class);
        graph.addAttributeNodes("books");

        TypedQuery<Author> query = em.createQuery(
                "SELECT a FROM Author a", Author.class);
        query.setHint("jakarta.persistence.fetchgraph", graph);

        List<Author> authors = query.getResultList();

        for (Author a : authors) {
            System.out.println(a + " has " + a.getBooks().size() + " books.");
        }

        em.getTransaction().commit();
        em.close();
    }
}
