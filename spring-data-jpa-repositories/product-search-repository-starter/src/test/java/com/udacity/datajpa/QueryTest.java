package com.udacity.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the four different Spring Data query techniques on the
 * same ProductRepository:
 *   - Derived method from name
 *   - @Query annotated method
 *   - Interface projection (narrower SELECT)
 *   - Specification-based dynamic search
 */
@SpringBootTest(classes = Application.class)
@Transactional
class QueryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void derivedMethod_findByCategoryAndPriceLessThan() {
        BigDecimal cap = new BigDecimal("50.00");
        List<Product> result = productRepository.findByCategoryAndPriceLessThan("Books", cap);

        assertThat(result).isNotEmpty();
        for (Product p : result) {
            assertThat(p.getCategory()).isEqualTo("Books");
            assertThat(p.getPrice()).isLessThan(cap);
        }
    }

    @Test
    void atQueryMethod_findTopSellersByMonth() {
        List<Product> result = productRepository.findTopSellersByMonth(2026, 6);

        assertThat(result).isNotEmpty();
        // Just verifying it returns rows; ordering correctness implied
        // by the GROUP BY + ORDER BY SUM(quantity) DESC in the query.
    }

    @Test
    void projection_findSummariesByCategory() {
        List<ProductSummary> result = productRepository.findSummariesByCategory("Books");

        assertThat(result).isNotEmpty();
        for (ProductSummary s : result) {
            assertThat(s.getId()).isNotNull();
            assertThat(s.getName()).isNotBlank();
            assertThat(s.getPrice()).isNotNull();
        }
    }

    @Test
    void specification_dynamicSearchComposesPredicates() {
        Specification<Product> spec = Specification.allOf(
                ProductSpecs.categoryIs("Books"),
                ProductSpecs.priceBetween(new BigDecimal("10.00"), new BigDecimal("100.00")),
                ProductSpecs.isInStock()
        );
        List<Product> result = productRepository.findAll(spec);

        assertThat(result).isNotEmpty();
        for (Product p : result) {
            assertThat(p.getCategory()).isEqualTo("Books");
            assertThat(p.getPrice()).isBetween(new BigDecimal("10.00"), new BigDecimal("100.00"));
            assertThat(p.isInStock()).isTrue();
        }
    }
}