--liquibase formatted sql

--changeset bart:1
CREATE TABLE IF NOT EXISTS auths (
    id INT AUTO_INCREMENT PRIMARY KEY,
    request_description VARCHAR(255) NOT NULL,
    origin_city VARCHAR(255) NOT NULL,
    destination_city VARCHAR(255) NOT NULL,
    auth_code VARCHAR(50) NOT NULL,
    supervisor_code VARCHAR(50) NOT NULL,
    authorized BOOLEAN DEFAULT FALSE
);

--changeset bart:2
INSERT INTO auths
(request_description, origin_city, destination_city, auth_code, supervisor_code, authorized)
VALUES
(
'Transporte de Ingenieros',
'Santiago',
'Rancagua',
'AUTH001',
'SUP-STG-01',
true
);