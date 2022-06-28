CREATE USER "classifier-service_user" WITH PASSWORD '111';
CREATE DATABASE "classifier-service" WITH OWNER = "classifier-service_user";
\c "classifier-service"

SET client_encoding = 'UTF8';

DROP TABLE IF EXISTS app.currency;

DROP SCHEMA IF EXISTS app;

CREATE SCHEMA app;

ALTER SCHEMA app OWNER TO "classifier-service_user";

CREATE TABLE IF NOT EXISTS app.currency
(
    id uuid NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone NOT NULL,
    description character varying(255) NOT NULL,
    title character varying(255) NOT NULL,
    CONSTRAINT pk_currency PRIMARY KEY (id),
    CONSTRAINT uk_currency_title UNIQUE (title)
);

ALTER TABLE IF EXISTS app.currency
    OWNER to "classifier-service_user";

INSERT INTO app.currency(id, created, updated, title, description)
VALUES
    (gen_random_uuid(), now(), now(), 'USD', 'Доллар США'),
    (gen_random_uuid(), now(), now(), 'EUR', 'Евро'),
    (gen_random_uuid(), now(), now(), 'UAH', 'Гривна'),
    (gen_random_uuid(), now(), now(), 'RUB', 'Российский Рубль'),
    (gen_random_uuid(), now(), now(), 'BYN', 'Белорусский Рубль');

DROP TABLE IF EXISTS app.category;

CREATE TABLE IF NOT EXISTS app.category
(
    id uuid NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone NOT NULL,
    title character varying(255) NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uk_category_title UNIQUE (title)
);

ALTER TABLE IF EXISTS app.category
    OWNER to "classifier-service_user";

INSERT INTO app.category(id, created, updated, title)
VALUES (gen_random_uuid(), now(), now(), 'Покупки'),
       (gen_random_uuid(), now(), now(), 'Здоровье'),
       (gen_random_uuid(), now(), now(), 'Спорт'),
       (gen_random_uuid(), now(), now(), 'Развлечения'),
       (gen_random_uuid(), now(), now(), 'Транспорт'),
       (gen_random_uuid(), now(), now(), 'Зарплата'),
       (gen_random_uuid(), now(), now(), 'Коммунальные услуги'),
       (gen_random_uuid(), now(), now(), 'Интернет'),
       (gen_random_uuid(), now(), now(), 'Связь'),
       (gen_random_uuid(), now(), now(), 'Взлом'),
       (gen_random_uuid(), now(), now(), 'Образование'),
       (gen_random_uuid(), now(), now(), 'Одежда и обувь'),
       (gen_random_uuid(), now(), now(), 'Заправка'),
       (gen_random_uuid(), now(), now(), 'Отдых');
