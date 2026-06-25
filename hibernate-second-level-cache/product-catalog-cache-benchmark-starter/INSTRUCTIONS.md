# Exercise: Cache a Read-Heavy Product Catalog

## Overview

In this exercise, you'll cut SQL traffic to a small product catalog
by enabling Hibernate's second-level cache. You'll add the `@Cache`
annotation to the Product entity, configure JCache with a cache
provider in `application.yml`, then run a benchmark that hits the
catalog 1,000 times across 10 product ids. With the cache warm,
you should see the hit ratio climb above 95% and total SQL queries
drop to roughly the number of unique ids.


## Before You Start

This exercise connects to a PostgreSQL database named `banking` as the
`banking` user. If you haven't run the one-time workspace setup script
yet, run it from the repository root:

```
bash setup/setup-postgres.sh
```

The script is idempotent and safe to re-run. See `setup/SETUP.md` at
the repository root for details.

## Exercise Instructions

Open the starter project and work through the TODOs in two files.

### Part 1: Configure the L2 cache region factory

Open `application.yml`. The dependencies are already wired in pom.xml
for hibernate-jcache + cache2k.

**TODO 1: Enable second-level cache and point to JCache**
Add the following under `spring.jpa.properties.hibernate`:
- `cache.use_second_level_cache: true`
- `cache.region.factory_class: org.hibernate.cache.jcache.JCacheRegionFactory`
- `javax.cache.provider: org.cache2k.jcache.provider.JCacheProvider`
- `javax.cache.missing_cache_strategy: create`

### Part 2: Mark Product as cacheable

Open `Product.java`.

**TODO 2: Annotate the entity with `@Cache`**
Add `@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)`. READ_WRITE
is safe for entities that get updated. The cache region uses the
fully-qualified entity class name by default.

## Deliverable

`CacheBenchmarkTest` passes:
- Cache hit ratio > 95% after warmup
- Total SQL queries <= 10 (one per unique id)

Run the test with:

```
mvn test
```

Expected output ends with:

```
[INFO] BUILD SUCCESS
```

The test prints the actual hit count and ratio so you can see the
cache in action.

## What's Included

- `Product.java` with TODO 2
- `ProductRepository.java`, complete
- `Application.java`, complete
- `application.yml` with TODO 1
- `cache2k.xml`, complete (cache region config)
- `CacheBenchmarkTest.java`, pre-written
- `schema.sql` and `data.sql` (10 products for the benchmark)

## Common Mistakes

- **Test class is `@Transactional`:** the L2 cache only commits entries on real transaction commit. A test transaction that rolls back at the end will defeat the cache.
- **`missing_cache_strategy` not set:** cache2k requires this to auto-create the region on demand. Otherwise you get an "ignoreMissingCacheConfiguration" error at startup.
- **Wrong cache provider class:** hibernate-jcache uses ServiceLoader to find the provider. Wrong classpath = silent fallback or hard failure.
- **`@Cache` on Product but not on its relationships:** the entity is cached but associations still hit the database. Add `@Cache` to those collections too if you need them cached.
