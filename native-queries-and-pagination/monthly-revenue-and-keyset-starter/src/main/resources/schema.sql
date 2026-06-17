-- Schema for Module 9: Native Queries and Keyset Pagination
-- Target: PostgreSQL 17

DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS product CASCADE;

CREATE TABLE product (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    category    VARCHAR(50)  NOT NULL,
    price       NUMERIC(10, 2) NOT NULL
);

-- Indexed name for keyset pagination demonstration
CREATE INDEX idx_product_name ON product (name);

CREATE TABLE orders (
    id          BIGSERIAL PRIMARY KEY,
    category    VARCHAR(50) NOT NULL,    -- denormalized for the monthly report
    amount      NUMERIC(10, 2) NOT NULL,
    placed_at   TIMESTAMP NOT NULL
);

CREATE INDEX idx_orders_placed_at ON orders (placed_at);