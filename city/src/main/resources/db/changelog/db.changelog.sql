--liquibase formatted sql
-- DONT BE EMPTY YOU'LL BREAK EVERYTHING

--changeset bart:1

INSERT INTO cities
(city_name, city_code, line_number, inhabitants)
VALUES
('Santiago','STG',1,7000000);
