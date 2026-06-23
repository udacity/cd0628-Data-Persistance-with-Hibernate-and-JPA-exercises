package com.udacity.report;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the two query implementations:
 *   - findTopCustomers: static JPQL aggregation
 *   - searchOrders: dynamic Criteria with optional filters
 */
@SpringBootTest(classes = Application.class)
@Transactional
class QueryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderSearchService searchService;

    @Test
    void topCustomers_returnsTenRowsOrderedBySpendDescending() {
        List<TopCustomerDto> result = orderRepository.findTopCustomers(2026);

        assertThat(result).hasSize(10);
        for (int i = 1; i < result.size(); i++) {
            assertThat(result.get(i).totalSpend())
                    .isLessThanOrEqualTo(result.get(i - 1).totalSpend());
        }
        assertThat(result.get(0).customerName()).isNotNull();
    }

    @Test
    void searchOrders_allNullFilters_returnsEverything() {
        OrderSearchCriteria criteria = new OrderSearchCriteria(null, null, null, null, null);
        List<Order> result = searchService.searchOrders(criteria);

        // 500 orders are seeded
        assertThat(result).hasSize(500);
    }

    @Test
    void searchOrders_statusFilter_narrowsCorrectly() {
        OrderSearchCriteria criteria = new OrderSearchCriteria("COMPLETED", null, null, null, null);
        List<Order> result = searchService.searchOrders(criteria);

        assertThat(result).isNotEmpty();
        for (Order order : result) {
            assertThat(order.getStatus()).isEqualTo("COMPLETED");
        }
    }

    @Test
    void searchOrders_minAmountFilter_narrowsCorrectly() {
        BigDecimal floor = new BigDecimal("500.00");
        OrderSearchCriteria criteria = new OrderSearchCriteria(null, null, floor, null, null);
        List<Order> result = searchService.searchOrders(criteria);

        assertThat(result).isNotEmpty();
        for (Order order : result) {
            assertThat(order.getAmount()).isGreaterThanOrEqualTo(floor);
        }
    }

    @Test
    void searchOrders_combinedFilters_AndsTogether() {
        OrderSearchCriteria criteria = new OrderSearchCriteria(
                "COMPLETED",
                null,
                new BigDecimal("200.00"),
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 12, 31, 23, 59)
        );
        List<Order> result = searchService.searchOrders(criteria);

        for (Order order : result) {
            assertThat(order.getStatus()).isEqualTo("COMPLETED");
            assertThat(order.getAmount()).isGreaterThanOrEqualTo(new BigDecimal("200.00"));
            assertThat(order.getPlacedAt()).isBetween(
                    LocalDateTime.of(2026, 1, 1, 0, 0),
                    LocalDateTime.of(2026, 12, 31, 23, 59)
            );
        }
    }
}