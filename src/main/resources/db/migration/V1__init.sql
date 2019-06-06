DROP TABLE IF EXISTS services;

CREATE TABLE services (
    id SERIAL PRIMARY KEY,
    name VARCHAR(400) NOT NULL,
    active BOOLEAN
);

DROP TABLE IF EXISTS organizations;

CREATE TABLE organizations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(400) NOT NULL,
    actual_address VARCHAR(400) NOT NULL,
    description VARCHAR(400) NOT NULL
);

DROP TABLE IF EXISTS children;

CREATE TABLE children (
    id SERIAL PRIMARY KEY,
    birth_certificate_series VARCHAR(6) NOT NULL,
    birth_certificate_number INTEGER NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50)
);

DROP TABLE IF EXISTS specialists;

CREATE TABLE specialists (
    id SERIAL PRIMARY KEY,
    name VARCHAR(400) NOT NULL,
    room_number VARCHAR(16) NOT NULL,
    active BOOLEAN,
    organization_id INTEGER REFERENCES organizations (id)
);

DROP TABLE IF EXISTS schedules;

CREATE TABLE schedules (
    id BIGSERIAL PRIMARY KEY,
    specialist_id INTEGER REFERENCES specialists (id),
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    interval_of_reception SMALLINT NOT NULL,
    active BOOLEAN
);

DROP TABLE IF EXISTS services_schedules;

CREATE TABLE services_schedules (
    service_id INTEGER REFERENCES services (id),
    schedule_id BIGINT REFERENCES schedules (id),
    PRIMARY KEY (service_id, schedule_id)
);

DROP TABLE IF EXISTS reservations;

CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    date_time TIMESTAMP NOT NULL,
    schedule_id BIGINT REFERENCES schedules (id),
    service_id INTEGER REFERENCES services (id),
    active BOOLEAN,
    child_id INTEGER REFERENCES children (id)
);