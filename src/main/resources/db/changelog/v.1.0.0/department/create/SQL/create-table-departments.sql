-- Таблица departments
CREATE TABLE sudstaff.departments (
    id UUID     PRIMARY KEY,
    name        VARCHAR(255)    not null,
    description TEXT,
    created_at  TIMESTAMP       not null,
    updated_at  TIMESTAMP       not null
);