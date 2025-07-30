-- Таблица section
CREATE TABLE sudstaff.section (
    id UUID PRIMARY KEY,
    personel_number VARCHAR(255)    NOT NULL,
    name VARCHAR(255)               NOT NULL,
    created_at TIMESTAMP            NOT NULL,
    updated_at TIMESTAMP            NOT NULL,
    district_id UUID                NOT NULL,

    CONSTRAINT fk_district
        FOREIGN KEY (district_id)
            REFERENCES sudstaff.district(id)
            ON DELETE CASCADE
);