package com.udacity.report;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderSearchService {

    @PersistenceContext
    private EntityManager em;

    // ============================================================
    // TODO 2: Implement dynamic search using the Criteria API.
    //
    // The goal: build a query at runtime that only filters on the
    // fields that were actually provided. Null fields should be
    // skipped so they don't constrain the result set.
    //
    // Key types you'll need (all in jakarta.persistence.criteria):
    //   - CriteriaBuilder  -- factory for predicates, get from em
    //   - CriteriaQuery    -- the query itself
    //   - Root             -- the FROM clause anchor
    //   - Predicate        -- a single WHERE condition
    //
    // Approach:
    //   - Get a CriteriaBuilder from the EntityManager
    //   - Create a CriteriaQuery<Order> and its Root<Order>
    //   - For each non-null field on the criteria record, build a
    //     Predicate and collect them in a list
    //   - Combine the predicates with AND
    //   - Execute the query and return the results
    //
    // Pitfalls to avoid:
    //   - For customerId, you'll need to navigate the relationship:
    //     root.get("customer").get("id")
    //   - For the date range, only build the predicate when BOTH
    //     bounds are present
    //   - All-null criteria should return everything (no WHERE clause)
    // ============================================================
    public List<Order> searchOrders(OrderSearchCriteria criteria) {
        return List.of(); // placeholder
    }
}