CREATE USER "account-service_user" WITH PASSWORD '111';
CREATE DATABASE "account-service" WITH OWNER = "account-service_user";
\c "account-service"

SET client_encoding = 'UTF8';

DROP TABLE IF EXISTS app.accounts CASCADE;
DROP TABLE IF EXISTS app.balances CASCADE;
DROP TABLE IF EXISTS app.operations CASCADE;

DROP SCHEMA IF EXISTS app;

CREATE SCHEMA app;

ALTER SCHEMA app OWNER TO "account-service_user";

CREATE TABLE IF NOT EXISTS app.accounts
(
    id uuid NOT NULL,
    created timestamp(3) without time zone,
    updated timestamp(3) without time zone,
    description character varying(255) NOT NULL,
    title character varying(255)  NOT NULL,
    type integer NOT NULL,
    username character varying(255) NOT NULL,
    CONSTRAINT accounts_pkey PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS app.accounts
    OWNER to "account-service_user";

CREATE TABLE IF NOT EXISTS app.balances
(
    account_id uuid NOT NULL,
    currency uuid NOT NULL,
    value numeric(19,2) NOT NULL,
    CONSTRAINT pk_balances PRIMARY KEY (account_id),
    CONSTRAINT fk_balances_accounts FOREIGN KEY (account_id)
        REFERENCES app.accounts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

ALTER TABLE IF EXISTS app.balances
    OWNER to "account-service_user";

CREATE TABLE IF NOT EXISTS app.operations
(
    id uuid NOT NULL,
    created timestamp(3) without time zone,
    updated timestamp(3) without time zone,
    category uuid NOT NULL,
    currency uuid NOT NULL,
    date date NOT NULL,
    description character varying(255) NOT NULL,
    value numeric(19,2) NOT NULL,
    account_id uuid NOT NULL,
    CONSTRAINT pk_operations PRIMARY KEY (id),
    CONSTRAINT fk_operations_accounts FOREIGN KEY (account_id)
        REFERENCES app.accounts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

ALTER TABLE IF EXISTS app.operations
    OWNER to "account-service_user";
