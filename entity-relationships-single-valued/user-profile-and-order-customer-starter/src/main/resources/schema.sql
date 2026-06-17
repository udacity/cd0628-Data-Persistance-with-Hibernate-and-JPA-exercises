-- Schema for Module 3: Single-Valued Relationships
-- Target: PostgreSQL 17

DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS user_profile CASCADE;
DROP TABLE IF EXISTS app_user CASCADE;
DROP TABLE IF EXISTS customer CASCADE;

CREATE TABLE app_user (
    id        BIGSERIAL PRIMARY KEY,
    username  VARCHAR(100) NOT NULL UNIQUE,
    email     VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE user_profile (
    -- Shared primary key via @MapsId â€” no separate generated id
    id           BIGINT PRIMARY KEY REFERENCES app_user (id),
    display_name VARCHAR(100),
    bio          TEXT
);

CREATE TABLE customer (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE orders (
    id            BIGSERIAL PRIMARY KEY,
    order_number  VARCHAR(50)  NOT NULL UNIQUE,
    placed_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    customer_id   BIGINT       NOT NULL REFERENCES customer (id)
);

CREATE INDEX idx_orders_customer ON orders (customer_id);