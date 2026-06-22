package com.udacity.nplusone;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Demonstrates the N+1 problem and three different ways to fix it.
 *
 * Each scenario clears the persistence context first, then asks
 * Hibernate Statistics for a fresh query count, runs the fetch,
 * touches every comment to force lazy loads, and checks the result.
 *
 *   Baseline findAll() = 1 + 10 = 11 queries (the N+1 problem)
 *   JOIN FETCH         = 1 query
 *   EntityGraph        = 1 query
 *   BatchSize(20)      = 2 queries (1 posts + 1 batched comments)
 */
@SpringBootTest(classes = Application.class)
@Transactional
class NPlusOneTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BlogPostRepository blogPostRepository;

    private Statistics statistics;

    @BeforeEach
    void setUp() {
        SessionFactory sessionFactory = em.getEntityManagerFactory().unwrap(SessionFactory.class);
        statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);
    }

    @Test
    void baseline_findAll_triggersNPlusOneQueries() {
        em.clear();
        statistics.clear();

        List<BlogPost> posts = blogPostRepository.findAll();
        for (BlogPost post : posts) {
            post.getComments().size(); // force lazy load on each
        }

        long queries = statistics.getPrepareStatementCount();
        System.out.println("Baseline findAll(): " + queries + " queries");
        assertThat(posts).hasSize(10);
        assertThat(queries).isEqualTo(11); // 1 + 10
    }

    @Test
    void joinFetch_triggersOneQuery() {
        em.clear();
        statistics.clear();

        List<BlogPost> posts = blogPostRepository.findAllWithJoinFetch();
        for (BlogPost post : posts) {
            post.getComments().size();
        }

        long queries = statistics.getPrepareStatementCount();
        System.out.println("findAllWithJoinFetch(): " + queries + " queries");
        assertThat(posts).hasSize(10);
        assertThat(queries).isEqualTo(1);
    }

    @Test
    void entityGraph_triggersOneQuery() {
        em.clear();
        statistics.clear();

        List<BlogPost> posts = blogPostRepository.findAllWithEntityGraph();
        for (BlogPost post : posts) {
            post.getComments().size();
        }

        long queries = statistics.getPrepareStatementCount();
        System.out.println("findAllWithEntityGraph(): " + queries + " queries");
        assertThat(posts).hasSize(10);
        assertThat(queries).isEqualTo(1);
    }

    @Test
    void batchSize_triggersTwoQueries() {
        em.clear();
        statistics.clear();

        List<BlogPost> posts = blogPostRepository.findAllWithBatchSize();
        for (BlogPost post : posts) {
            post.getComments().size();
        }

        long queries = statistics.getPrepareStatementCount();
        System.out.println("findAllWithBatchSize(): " + queries + " queries");
        assertThat(posts).hasSize(10);
        assertThat(queries).isEqualTo(2); // 1 posts + 1 batched comments
    }
}
