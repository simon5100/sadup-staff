-- Таблица district
CREATE TABLE sudstaff.district (
    id UUID         PRIMARY KEY,
    name            VARCHAR(255)    NOT NULL,
    description     TEXT,
    created_at      TIMESTAMP       NOT NULL,
    updated_at      TIMESTAMP       NOT NULL
);