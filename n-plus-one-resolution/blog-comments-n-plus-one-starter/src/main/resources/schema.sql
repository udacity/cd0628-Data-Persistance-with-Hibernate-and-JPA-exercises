-- Schema for Module 7: N+1 Detection and Resolution
-- Target: PostgreSQL 17

DROP TABLE IF EXISTS comment CASCADE;
DROP TABLE IF EXISTS blog_post CASCADE;

CREATE TABLE blog_post (
    id            BIGSERIAL PRIMARY KEY,
    title         VARCHAR(300) NOT NULL,
    body          TEXT NOT NULL,
    published_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE comment (
    id          BIGSERIAL PRIMARY KEY,
    post_id     BIGINT NOT NULL REFERENCES blog_post (id) ON DELETE CASCADE,
    author      VARCHAR(100) NOT NULL,
    body        TEXT NOT NULL,
    posted_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_comment_post ON comment (post_id);