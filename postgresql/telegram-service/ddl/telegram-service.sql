CREATE USER "telegram-service_user" WITH PASSWORD '111';
CREATE DATABASE "telegram-service" WITH OWNER = "telegram-service_user";
\c "telegram-service"

SET client_encoding = 'UTF8';

DROP SCHEMA IF EXISTS app;

CREATE SCHEMA IF NOT EXISTS app;

DROP TABLE IF EXISTS public.telegram_chat;

CREATE TABLE IF NOT EXISTS app.telegram_chat
(
    id character varying(255) NOT NULL,
    chosen_account uuid,
    chosen_operation uuid,
    jwt_token character varying,
    menu_state integer NOT NULL,
    CONSTRAINT telegram_chat_pkey PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS app.telegram_chat
    OWNER to "telegram-service_user";