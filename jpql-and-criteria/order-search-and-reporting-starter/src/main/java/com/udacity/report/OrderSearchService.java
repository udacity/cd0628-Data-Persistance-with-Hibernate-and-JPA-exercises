package com.udacity.report;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderSearchService {

    @PersistenceContext
    private EntityManager em;

    // ============================================================
    // TODO 2: Implement dynamic search using the Criteria API.
    //
    // Steps:
    //
    //   CriteriaBuilder cb = em.getCriteriaBuilder();
    //   CriteriaQuery<Order> query = cb.createQuery(Order.class);
    //   Root<Order> root = query.from(Order.class);
    //   List<Predicate> predicates = new ArrayList<>();
    //
    //   if (criteria.status() != null) {
    //       predicates.add(cb.equal(root.get("status"), criteria.status()));
    //   }
    //   if (criteria.customerId() != null) {
    //       predicates.add(cb.equal(root.get("customer").get("id"),
    //                               criteria.customerId()));
    //   }
    //   if (criteria.minAmount() != null) {
    //       predicates.add(cb.greaterThanOrEqualTo(root.get("amount"),
    //                                              criteria.minAmount()));
    //   }
    //   if (criteria.fromDate() != null && criteria.toDate() != null) {
    //       predicates.add(cb.between(root.get("placedAt"),
    //                                 criteria.fromDate(),
    //                                 criteria.toDate()));
    //   }
    //
    //   query.select(root)
    //        .where(cb.and(predicates.toArray(new Predicate[0])));
    //
    //   return em.createQuery(query).getResultList();
    // ============================================================
    public List<Order> searchOrders(OrderSearchCriteria criteria) {
        return new ArrayList<>(); // placeholder
    }
}