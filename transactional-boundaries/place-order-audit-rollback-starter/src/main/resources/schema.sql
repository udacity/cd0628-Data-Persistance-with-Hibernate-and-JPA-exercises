-- Schema for Module 11: Transactional Boundaries and Propagation
-- Target: PostgreSQL 17

DROP TABLE IF EXISTS audit_log CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS product CASCADE;

CREATE TABLE product (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    price       NUMERIC(10, 2) NOT NULL,
    inventory   INT NOT NULL
);

CREATE TABLE orders (
    id            BIGSERIAL PRIMARY KEY,
    product_id    BIGINT NOT NULL REFERENCES product (id),
    quantity      INT NOT NULL,
    total_amount  NUMERIC(10, 2) NOT NULL,
    placed_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE audit_log (
    id            BIGSERIAL PRIMARY KEY,
    event_type    VARCHAR(50) NOT NULL,
    description   TEXT NOT NULL,
    recorded_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_event_type ON audit_log (event_type);