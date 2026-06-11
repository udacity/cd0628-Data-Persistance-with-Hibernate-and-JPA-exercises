-- Schema for Module 6: Hibernate Second-Level Cache
-- Target: PostgreSQL 17

DROP TABLE IF EXISTS product;

CREATE TABLE product (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    category    VARCHAR(50)  NOT NULL,
    price       NUMERIC(10, 2) NOT NULL,
    in_stock    BOOLEAN NOT NULL DEFAULT TRUE,
    version     BIGINT NOT NULL DEFAULT 0
);