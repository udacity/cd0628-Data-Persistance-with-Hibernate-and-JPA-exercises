# Exercise: Cache a Product Entity and Confirm the Hit Ratio

## What You'll Build

Enable Hibernate's second-level cache for a Product entity, configure
Ehcache, and confirm via Hibernate statistics that the cache is doing
real work.

## Requirements

- Uncomment and complete cache config in application.yml
- Add @Cache(usage = READ_WRITE) to the Product entity
- Define a product cache region in ehcache.xml with a 10-min TTL
- The provided CacheBenchmarkTest performs 1,000 findById calls and
  asserts hit ratio >95% and total queries ≤ 10

## Starter Code

- PostgreSQL with products table and 10 seeded rows
- Product entity, @Id done, caching annotation TODO
- application.yml with cache config sections commented out
- ehcache.xml shell with TODO for the region
- CacheBenchmarkTest pre-written, currently fails

## Verification

- CacheBenchmarkTest passes
- Statistics output shows the hit ratio and final query count
- You can explain why READ_WRITE is the right strategy here

## Common Mistakes

- Enabling cache config but forgetting @Cache on the entity
- READ_ONLY on an entity that changes
- Confusing first-level (session) with second-level (shared)
- Measuring without a warmup phase