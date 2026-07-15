-- Schema for the concert events demo.
-- Run once against the demo database before executing DemoRunner.

DROP TABLE IF EXISTS event;

CREATE TABLE event (
    id           BIGSERIAL PRIMARY KEY,
    venue        VARCHAR(255) NOT NULL,
    artist       VARCHAR(255) NOT NULL,
    event_date   DATE         NOT NULL,
    price        NUMERIC(8,2) NOT NULL
);
