-- Schema for Module 7: Hibernate Second-Level Cache
-- Target: PostgreSQL 17

DROP TABLE IF EXISTS product CASCADE;

CREATE TABLE product (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    category        VARCHAR(50)  NOT NULL,
    price           NUMERIC(10, 2) NOT NULL,
    stock_quantity  INT NOT NULL DEFAULT 0
);