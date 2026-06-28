--liquibase formatted sql
-- DONT BE EMPTY YOU'LL BREAK EVERYTHING

--changeset bart:1

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
