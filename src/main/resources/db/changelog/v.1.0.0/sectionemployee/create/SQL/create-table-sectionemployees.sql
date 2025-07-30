-- Таблица section_employee
CREATE TABLE sudstaff.section_employee (
    id UUID PRIMARY KEY,
    personel_number VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    patronymic VARCHAR(255),
    position VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    section_id UUID,

    CONSTRAINT fk_section
        FOREIGN KEY (section_id)
            REFERENCES sudstaff.section(id)
            ON DELETE CASCADE
);