--liquibase formatted sql

--changeset bart:1
CREATE TABLE IF NOT EXISTS cities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    city_name VARCHAR(255) NOT NULL,
    city_code VARCHAR(10) NOT NULL,
    line_number INT,
    inhabitants INT
);

--changeset bart:2
INSERT INTO cities (city_name, city_code, line_number, inhabitants)
VALUES ('Santiago', 'STG', 1, 7000000);