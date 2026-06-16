# Exercise: Cache a Product Entity and Confirm the Hit Ratio

## Overview

Enable Hibernate's second-level cache for a Product entity, configure
Ehcache as the cache provider, and confirm via Hibernate statistics
that the cache is actually serving reads. A pre-written test performs
1,000 findById calls across 10 product IDs and asserts a >95% cache
hit ratio with ≤10 total SQL queries.

## Exercise Instructions

Open the starter project and work through the TODOs across three files.

### Part 1: Application Configuration

Open `application.yml`. Find the commented-out cache section in the
`spring.jpa.properties.hibernate` block.

**TODO 1: Uncomment the cache config block**
Uncomment the lines for:
- `cache.use_second_level_cache: true`
- `cache.region.factory_class: org.hibernate.cache.jcache.JCacheRegionFactory`
- `javax.cache.provider: org.ehcache.jsr107.EhcacheCachingProvider`
- `javax.cache.uri: classpath:ehcache.xml`

These tell Hibernate to use a JCache-backed L2 cache pointed at
Ehcache.

### Part 2: Cache Region

Open `ehcache.xml`. The XML shell is in place but the product region
is empty.

**TODO 2: Define the product cache region**
Inside the existing `<config>` element, add a `<cache alias="com.udacity.cache.Product">`
block with:
- A `<expiry>` element using `<ttl unit="minutes">10</ttl>`
- A `<heap>` element with size `100` and unit `entries`

The alias must match the fully-qualified entity class name so
Hibernate knows which region to use.

### Part 3: Cache the Product Entity

Open `Product.java`.

**TODO 3: Annotate the class with `@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)`**
- `READ_WRITE` is the right choice for entities that get updated.
  `READ_ONLY` throws on update; `NONSTRICT_READ_WRITE` allows stale
  reads, which we don't want.

## Deliverable

`CacheBenchmarkTest` passes:
- 1,000 `findById` calls complete
- Hibernate statistics show >95% cache hit ratio
- Total SQL query count is ≤10 (one per unique product ID)

The test also prints the hit ratio and final query count so you can
see the cache effect.

## What's Included

- `application.yml` with TODO 1
- `ehcache.xml` with TODO 2
- `Product.java` with TODO 3
- `CacheBenchmarkTest.java`, pre-written with statistics assertions
- `schema.sql` and `data.sql` for the products table (10 seeded rows)

## Common Mistakes

- **Cache config enabled but `@Cache` missing on the entity:** the L2 cache silently does nothing — no error, just no hits
- **`READ_ONLY` strategy on a mutable entity:** throws on update
- **Confusing first-level (session) cache with second-level (shared):** L1 hits across calls in the same session can mask whether L2 is actually working. The test bypasses L1 by clearing the session between calls
- **Measuring without a warmup phase:** first read always misses. The test includes warmup
- **Region alias mismatch:** if `ehcache.xml`'s alias doesn't match the entity's fully-qualified class name, Hibernate uses a default region with different settings