CREATE USER "classifier-service_user" WITH PASSWORD '111';
CREATE DATABASE "classifier-service" WITH OWNER = "classifier-service_user";
\c "classifier-service"

SET client_encoding = 'UTF8';

CREATE SCHEMA app;

ALTER SCHEMA app OWNER TO "classifier-service_user";

DROP TABLE IF EXISTS app.currency;

CREATE TABLE IF NOT EXISTS app.currency
(
    id uuid NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone NOT NULL,
    description character varying(255) COLLATE pg_catalog."default" NOT NULL,
    title character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_currency PRIMARY KEY (id),
    CONSTRAINT uk_currency_title UNIQUE (title)
);

ALTER TABLE IF EXISTS app.currency
    OWNER to "classifier-service_user";

DROP TABLE IF EXISTS app.category;

CREATE TABLE IF NOT EXISTS app.category
(
    id uuid NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone NOT NULL,
    title character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uk_category_title UNIQUE (title)
);

ALTER TABLE IF EXISTS app.category
    OWNER to "classifier-service_user";
