package com.example.demo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository. Extending JpaRepository gives us full CRUD
 * (save, findById, findAll, delete, count) with zero implementation.
 *
 * Everything below shows the four ways Spring Data lets us go beyond CRUD:
 *
 *   1. Derived queries      - method name is parsed into a query
 *   2. @Query with JPQL     - custom query, database-agnostic
 *   3. @Query with native   - custom query, Postgres-specific
 *   4. Pageable             - built-in pagination, no manual offset math
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    // ---- 1. Derived queries. Method name -> query. No implementation. ----

    List<Task> findByStatus(Task.Status status);

    List<Task> findByPriorityGreaterThanEqual(int minPriority);

    List<Task> findByStatusAndDueDateBefore(Task.Status status, LocalDate cutoff);

    long countByStatus(Task.Status status);

    // ---- 2. Custom JPQL with @Query. Use when the derived name gets ugly. ----

    @Query("SELECT t FROM Task t WHERE t.status <> com.example.demo.Task$Status.DONE " +
           "AND t.dueDate < :today ORDER BY t.priority DESC, t.dueDate ASC")
    List<Task> findOverdueOpenTasks(@Param("today") LocalDate today);

    // ---- 3. Native SQL. Use when JPQL cannot express what you need. ----

    @Query(value = "SELECT status, COUNT(*) AS c FROM task GROUP BY status ORDER BY status",
           nativeQuery = true)
    List<Object[]> countByStatusNative();

    // ---- 4. Pageable. Sorting and paging are just parameters. ----

    Page<Task> findByStatus(Task.Status status, Pageable pageable);
}
