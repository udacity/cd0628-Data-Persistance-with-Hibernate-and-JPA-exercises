package com.udacity.cache;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Benchmarks the second-level cache by hammering 10 product IDs
 * with 1,000 findById calls. The L1 (session) cache is cleared
 * between calls to ensure measurements reflect L2 behavior.
 *
 * Expected after the cache is configured:
 *   - >95% cache hit ratio
 *   - <=10 total SQL queries (one per unique ID, the rest served
 *     from the cache)
 *
 * Note: NOT @Transactional. The L2 cache only commits entries on
 * transaction commit, so a single rolled-back wrapping transaction
 * across the whole test would defeat the cache.
 */
@SpringBootTest(classes = Application.class)
class CacheBenchmarkTest {

    private static final int CALLS = 1000;
    private static final int UNIQUE_IDS = 10;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ProductRepository productRepository;

    private Statistics statistics;

    @BeforeEach
    void setUp() {
        SessionFactory sessionFactory = em.getEntityManagerFactory().unwrap(SessionFactory.class);
        statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();
    }

    @Test
    void thousandFindsAcrossTenIds_useTheL2Cache() {
        // Warmup: prime the cache so first reads aren't counted
        for (long id = 1; id <= UNIQUE_IDS; id++) {
            productRepository.findById(id).orElseThrow();
        }
        em.clear();
        statistics.clear();

        for (int i = 0; i < CALLS; i++) {
            long id = (i % UNIQUE_IDS) + 1;
            Product p = productRepository.findById(id).orElseThrow();
            assertThat(p.getName()).isNotNull();
            em.clear(); // bypass L1 so we measure L2
        }

        long hits = statistics.getSecondLevelCacheHitCount();
        long misses = statistics.getSecondLevelCacheMissCount();
        long queries = statistics.getPrepareStatementCount();
        long totalLookups = hits + misses;
        double hitRatio = totalLookups == 0 ? 0 : ((double) hits / totalLookups) * 100;

        System.out.printf("L2 cache: hits=%d misses=%d hit-ratio=%.2f%%%n", hits, misses, hitRatio);
        System.out.printf("Total SQL queries: %d%n", queries);

        assertThat(hitRatio).isGreaterThan(95.0);
        assertThat(queries).isLessThanOrEqualTo(10L);
    }
}