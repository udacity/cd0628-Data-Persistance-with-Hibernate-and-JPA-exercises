-- Schema for Module 8: JPQL and Criteria
-- Target: PostgreSQL 17

DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS customer CASCADE;

CREATE TABLE customer (
    id     BIGSERIAL PRIMARY KEY,
    name   VARCHAR(200) NOT NULL,
    email  VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE orders (
    id            BIGSERIAL PRIMARY KEY,
    customer_id   BIGINT NOT NULL REFERENCES customer (id),
    order_number  VARCHAR(50) NOT NULL UNIQUE,
    status        VARCHAR(30) NOT NULL,
    amount        NUMERIC(10, 2) NOT NULL,
    placed_at     TIMESTAMP NOT NULL
);

CREATE INDEX idx_orders_customer ON orders (customer_id);
CREATE INDEX idx_orders_status   ON orders (status);
CREATE INDEX idx_orders_placed   ON orders (placed_at);