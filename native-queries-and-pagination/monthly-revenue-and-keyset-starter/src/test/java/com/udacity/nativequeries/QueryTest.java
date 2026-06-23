package com.udacity.nativequeries;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Limit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the two query implementations:
 *   - Monthly revenue native query mapped via @SqlResultSetMapping
 *   - Keyset pagination over the product catalog
 *
 * Also prints EXPLAIN ANALYZE output for both keyset and offset
 * pagination at page 1 vs page 100 to show why keyset wins on
 * large tables.
 */
@SpringBootTest(classes = Application.class)
@Transactional
class QueryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @SuppressWarnings("unchecked")
    void monthlyRevenue_returnsRowsForEachMonthAndCategory() {
        List<MonthlyRevenueReport> rows = em.createNativeQuery(
                "SELECT date_trunc('month', placed_at) AS month, " +
                "       category AS category, " +
                "       SUM(amount) AS total " +
                "FROM orders " +
                "GROUP BY date_trunc('month', placed_at), category " +
                "ORDER BY month, category",
                "MonthlyRevenueMapping")
                .getResultList();

        // Data: 1,800 orders across 12 months and 5 categories
        // Expected row count is at most 12 * 5 = 60
        assertThat(rows).isNotEmpty();
        assertThat(rows.size()).isLessThanOrEqualTo(60);

        for (MonthlyRevenueReport row : rows) {
            assertThat(row.getMonth()).isNotNull();
            assertThat(row.getCategory()).isNotBlank();
            assertThat(row.getTotal()).isPositive();
        }
    }

    @Test
    void keysetPagination_walksThroughTheFullCatalog() {
        int pageSize = 100;
        String cursor = "";
        int totalSeen = 0;
        int safetyCap = 200; // 10,000 products / 100 page size = 100, give 2x safety

        while (totalSeen < safetyCap * pageSize) {
            List<Product> page = productRepository.findProductsAfter(cursor, Limit.of(pageSize));
            if (page.isEmpty()) {
                break;
            }
            totalSeen += page.size();
            cursor = page.get(page.size() - 1).getName();
        }

        // Seeded data: 10,000 products
        assertThat(totalSeen).isGreaterThanOrEqualTo(10000);
    }

    @Test
    void explainAnalyze_keysetVsOffsetAtPage100() {
        // Page 100 with offset has to scan and skip 9,900 rows
        String offsetQuery = "EXPLAIN ANALYZE " +
                "SELECT * FROM product ORDER BY name LIMIT 100 OFFSET 9900";
        @SuppressWarnings("unchecked")
        List<String> offsetPlan = em.createNativeQuery(offsetQuery).getResultList();

        // Keyset: starts from a known cursor, no offset
        Product cursor = productRepository.findProductsAfter("", Limit.of(9900))
                .get(9899);
        String cursorName = cursor.getName().replace("'", "''");

        String keysetQuery = "EXPLAIN ANALYZE " +
                "SELECT * FROM product WHERE name > '" + cursorName +
                "' ORDER BY name LIMIT 100";
        @SuppressWarnings("unchecked")
        List<String> keysetPlan = em.createNativeQuery(keysetQuery).getResultList();

        System.out.println("=== OFFSET pagination plan (page 100) ===");
        offsetPlan.forEach(System.out::println);
        System.out.println();
        System.out.println("=== KEYSET pagination plan (page 100) ===");
        keysetPlan.forEach(System.out::println);

        assertThat(offsetPlan).isNotEmpty();
        assertThat(keysetPlan).isNotEmpty();
    }
}
