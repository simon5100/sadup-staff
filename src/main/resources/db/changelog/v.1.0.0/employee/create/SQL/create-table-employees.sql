-- Таблица employees
CREATE TABLE sudstaff.employees (
    id              UUID            PRIMARY KEY,
    personel_number VARCHAR(255)    NOT NULL,
    first_name      VARCHAR(255)    NOT NULL,
    last_name       VARCHAR(255)    NOT NULL,
    patronymic      VARCHAR(255),
    position        VARCHAR(255)    NOT NULL,
    created_at      TIMESTAMP       NOT NULL,
    updated_at      TIMESTAMP       NOT NULL,
    department_id   UUID            NOT NULL,

    CONSTRAINT fk_department
    FOREIGN KEY (department_id)
        REFERENCES sudstaff.departments(id)
        ON DELETE CASCADE
);