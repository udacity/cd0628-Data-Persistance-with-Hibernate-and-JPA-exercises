package com.udacity.nplusone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    
    // ============================================================
    // TODO 1: Implement using JPQL LEFT JOIN FETCH.
    //
    // Annotate this method with:
    //   @Query("SELECT DISTINCT p FROM BlogPost p LEFT JOIN FETCH p.comments")
    //
    // The LEFT matters: a plain JOIN FETCH is an inner join and would
    // drop any post that has no comments. DISTINCT prevents duplicate
    // parent rows when one post has multiple comments. Result: exactly
    // ONE query.
    // ============================================================
    List<BlogPost> findAllWithJoinFetch();

    // ============================================================
    // TODO 2: Implement using @EntityGraph.
    //
    // Annotate this method with:
    //   @EntityGraph(attributePaths = "comments")
    //
    // You also need @Query("SELECT p FROM BlogPost p") on this method --
    // without it, Spring Data derives a query from the method name and
    // ignores the entity graph. With the @Query in place, Spring Data
    // combines the graph with that query for exactly ONE query.
    //
    // Import:
    //   import org.springframework.data.jpa.repository.EntityGraph;
    // ============================================================
    List<BlogPost> findAllWithEntityGraph();

    // ============================================================
    // TODO 3: Implement as a plain default method delegating to findAll().
    //
    // No special query annotation. The actual fix lives on the
    // BlogPost entity (TODO 4: @BatchSize). Together they produce
    // the 2-query result.
    //
    // Body:
    //   default List<BlogPost> findAllWithBatchSize() {
    //       return findAll();
    //   }
    //
    // (Already structured as a default method below -- just leave
    // the body returning findAll().)
    // ============================================================
    default List<BlogPost> findAllWithBatchSize() {
        return findAll();
    }
}
