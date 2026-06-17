-- Schema for Module 10: Spring Data JPA Repositories
-- Target: PostgreSQL 17

DROP TABLE IF EXISTS order_item CASCADE;
DROP TABLE IF EXISTS product CASCADE;

CREATE TABLE product (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    category    VARCHAR(50)  NOT NULL,
    price       NUMERIC(10, 2) NOT NULL,
    in_stock    BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE order_item (
    id          BIGSERIAL PRIMARY KEY,
    product_id  BIGINT NOT NULL REFERENCES product (id),
    quantity    INT NOT NULL,
    sold_at     TIMESTAMP NOT NULL
);

CREATE INDEX idx_order_item_product ON order_item (product_id);
CREATE INDEX idx_order_item_sold_at ON order_item (sold_at);
CREATE INDEX idx_product_category   ON product (category);