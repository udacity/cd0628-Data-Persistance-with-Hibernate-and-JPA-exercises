package com.udacity.nativequeries;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // ============================================================
    // TODO 2: Implement keyset pagination.
    //
    // The goal: return up to `limit` products with a name strictly
    // greater than `lastName`, walking forward through the catalog
    // alphabetically. Repeated calls passing the previous batch's
    // last name should yield the next batch with no overlap or gaps.
    //
    // Why keyset, not offset?
    //   - Offset pagination scans (offset + limit) rows on every page,
    //     so latency grows with page number
    //   - Keyset uses an index on the cursor column, so cost stays
    //     constant regardless of how deep into the catalog you are
    //
    // Your query needs to:
    //   - Find products where the name is strictly greater than the
    //     cursor value
    //   - Order by name ascending (the cursor column comes first)
    //   - Include a deterministic tiebreaker for duplicate names so
    //     pagination doesn't skip or repeat rows
    //
    // Hints:
    //   - Annotate the method with @Query containing your JPQL
    //   - The `limit` parameter uses Spring Data's Limit type, which
    //     caps rows without paying any offset cost
    //   - First call starts from an empty string ("") since every
    //     name is lexically greater than that
    // ============================================================
    List<Product> findProductsAfter(@Param("lastName") String lastName, Limit limit);
}