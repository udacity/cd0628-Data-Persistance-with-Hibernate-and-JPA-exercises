-- Schema for Module 4: Multi-Valued Relationships
-- Target: PostgreSQL 17

DROP TABLE IF EXISTS enrollment CASCADE;
DROP TABLE IF EXISTS course CASCADE;
DROP TABLE IF EXISTS student CASCADE;
DROP TABLE IF EXISTS comment CASCADE;
DROP TABLE IF EXISTS blog_post CASCADE;

CREATE TABLE blog_post (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(300) NOT NULL,
    body        TEXT NOT NULL,
    published_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE comment (
    id          BIGSERIAL PRIMARY KEY,
    post_id     BIGINT NOT NULL REFERENCES blog_post (id) ON DELETE CASCADE,
    author      VARCHAR(100) NOT NULL,
    body        TEXT NOT NULL,
    posted_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_comment_post ON comment (post_id);

CREATE TABLE student (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(200) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE course (
    id         BIGSERIAL PRIMARY KEY,
    code       VARCHAR(20)  NOT NULL UNIQUE,
    title      VARCHAR(300) NOT NULL,
    credits    INT NOT NULL
);

CREATE TABLE enrollment (
    id              BIGSERIAL PRIMARY KEY,
    student_id      BIGINT NOT NULL REFERENCES student (id),
    course_id       BIGINT NOT NULL REFERENCES course (id),
    enrollment_date DATE   NOT NULL,
    UNIQUE (student_id, course_id)
);

CREATE INDEX idx_enrollment_student ON enrollment (student_id);
CREATE INDEX idx_enrollment_course  ON enrollment (course_id);