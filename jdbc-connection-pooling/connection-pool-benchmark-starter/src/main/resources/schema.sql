-- Schema for Module 1: JDBC + HikariCP Connection Pool Benchmark
-- Target: PostgreSQL 17

DROP TABLE IF EXISTS customer;

CREATE TABLE customer (
    id          BIGSERIAL PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_customer_email ON customer (email);