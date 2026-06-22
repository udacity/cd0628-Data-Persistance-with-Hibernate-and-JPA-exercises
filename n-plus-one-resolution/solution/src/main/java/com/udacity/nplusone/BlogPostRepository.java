package com.udacity.nplusone;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    // JOIN FETCH approach. DISTINCT prevents duplicate parent rows
    // when one post has multiple comments. Result: ONE query.
    @Query("SELECT DISTINCT p FROM BlogPost p JOIN FETCH p.comments")
    List<BlogPost> findAllWithJoinFetch();

    // EntityGraph approach. Spring Data combines the graph with the
    // default findAll() query. Result: ONE query, reusable.
   
    @EntityGraph(attributePaths = "comments")
    @Query("SELECT p FROM BlogPost p")
    List<BlogPost> findAllWithEntityGraph();

    // BatchSize approach. Plain findAll() pairs with @BatchSize(20)
    // on BlogPost.comments. Result: 2 queries (1 posts + 1 batched).
    default List<BlogPost> findAllWithBatchSize() {
        return findAll();
    }
}