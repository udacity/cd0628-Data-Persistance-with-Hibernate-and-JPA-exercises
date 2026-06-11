# Exercise: Wire and Tune a HikariCP Data Source

## What You'll Build

Complete a partly-built JDBC data access layer: implement two repository
methods, fill in four HikariCP config values, and confirm pool tuning
makes a measurable throughput difference.

## Requirements

- Implement findById and batchInsert in CustomerRepositoryImpl using
  PreparedStatement with try-with-resources
- Set four explicit values in HikariConfigFactory: maximumPoolSize,
  connectionTimeout, idleTimeout, maxLifetime
- The provided BenchmarkRunner exercises findById under 20 threads
  twice (pool=2, then pool=10) and prints throughput for both

## Starter Code

- pom.xml, schema, seed data, Customer POJO, repository interface
  (all complete)
- CustomerRepositoryImpl with create, update, delete already done —
  only findById and batchInsert have TODOs
- HikariConfigFactory with 4 TODOs
- BenchmarkRunner is complete
- Tests are pre-written and currently fail

## Verification

- All repository tests pass
- BenchmarkRunner prints throughput for both pool sizes
- pool=10 outperforms pool=2 under concurrent load

## Common Mistakes

- Manual close() instead of try-with-resources
- Looping single inserts instead of executeBatch
- Setting maximumPoolSize=50 "to be safe"
- Catching SQLException with an empty block