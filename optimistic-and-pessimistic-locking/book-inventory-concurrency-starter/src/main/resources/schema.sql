-- Schema for Module 12: Optimistic and Pessimistic Locking
-- Target: PostgreSQL 17

DROP TABLE IF EXISTS book;

CREATE TABLE book (
    id          BIGSERIAL PRIMARY KEY,
    isbn        VARCHAR(20) NOT NULL UNIQUE,
    title       VARCHAR(300) NOT NULL,
    author      VARCHAR(200) NOT NULL,
    description TEXT,
    stock       INT NOT NULL,
    version     BIGINT NOT NULL DEFAULT 0    -- @Version column
);