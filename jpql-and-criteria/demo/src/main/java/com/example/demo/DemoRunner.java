package com.example.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Seeds a small product catalog then runs the same query two ways:
 *
 *   1. Static JPQL - readable, terse, best when you know the query at compile time.
 *   2. Criteria API - dynamic, type-safe, best when the shape of the query
 *      depends on runtime input.
 *
 * Both queries answer the same question:
 *   "Give me all in-stock products in the 'electronics' category
 *    priced under $100, ordered by price ascending."
 */
public class DemoRunner {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("demoPU");
        try {
            seed(emf);

            System.out.println("---- 1. Static JPQL ----");
            runJpql(emf, "electronics", new BigDecimal("100.00"), true);

            System.out.println("---- 2. Criteria API ----");
            runCriteria(emf, "electronics", new BigDecimal("100.00"), true);
        } finally {
            emf.close();
        }
    }

    private static void seed(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        em.persist(new Product("USB-C Cable",         "electronics", new BigDecimal("12.00"),  true));
        em.persist(new Product("Wireless Mouse",      "electronics", new BigDecimal("29.00"),  true));
        em.persist(new Product("Mechanical Keyboard", "electronics", new BigDecimal("140.00"), true));
        em.persist(new Product("Bluetooth Speaker",   "electronics", new BigDecimal("55.00"),  false));
        em.persist(new Product("Ceramic Mug",         "kitchen",     new BigDecimal("14.00"),  true));
        em.persist(new Product("Chef Knife",          "kitchen",     new BigDecimal("85.00"),  true));

        em.getTransaction().commit();
        em.close();
    }

    /**
     * Static JPQL. Best for known queries. Concise and readable.
     */
    private static void runJpql(EntityManagerFactory emf,
                                String category, BigDecimal maxPrice, boolean inStockOnly) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        String jpql =
                "SELECT p FROM Product p " +
                "WHERE p.category = :category " +
                "  AND p.price < :maxPrice " +
                "  AND p.inStock = :inStock " +
                "ORDER BY p.price ASC";

        TypedQuery<Product> query = em.createQuery(jpql, Product.class);
        query.setParameter("category", category);
        query.setParameter("maxPrice", maxPrice);
        query.setParameter("inStock", inStockOnly);

        List<Product> results = query.getResultList();
        results.forEach(p -> System.out.println("  " + p));

        em.getTransaction().commit();
        em.close();
    }

    /**
     * Same query built with the Criteria API. Every filter is optional, added
     * only when its argument is present. This is where Criteria shines.
     */
    private static void runCriteria(EntityManagerFactory emf,
                                    String category, BigDecimal maxPrice, boolean inStockOnly) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        CriteriaBuilder cb    = em.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> product = cq.from(Product.class);

        // Build the WHERE predicates as a dynamic list.
        List<Predicate> where = new ArrayList<>();
        if (category != null) {
            where.add(cb.equal(product.get("category"), category));
        }
        if (maxPrice != null) {
            where.add(cb.lessThan(product.get("price"), maxPrice));
        }
        if (inStockOnly) {
            where.add(cb.isTrue(product.get("inStock")));
        }

        cq.select(product)
          .where(cb.and(where.toArray(new Predicate[0])))
          .orderBy(cb.asc(product.get("price")));

        List<Product> results = em.createQuery(cq).getResultList();
        results.forEach(p -> System.out.println("  " + p));

        em.getTransaction().commit();
        em.close();
    }
}
