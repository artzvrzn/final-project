CREATE USER "report-service_user" WITH PASSWORD '111';
CREATE DATABASE "report-service" WITH OWNER = "report-service_user";
\c "report-service"

DROP TABLE IF EXISTS app.reports CASCADE;

DROP SCHEMA IF EXISTS app;

CREATE SCHEMA app;

ALTER SCHEMA app OWNER TO "report-service_user";

CREATE TABLE IF NOT EXISTS app.reports
(
    id uuid NOT NULL,
    created timestamp(3) without time zone NOT NULL,
    description character varying(255),
    status integer NOT NULL,
    type integer NOT NULL,
    updated timestamp(3) without time zone NOT NULL,
    user_details bytea NOT NULL,
	username character varying(255) NOT NULL,
    CONSTRAINT pk_report PRIMARY KEY (id),
	params character varying NOT NULL

);

ALTER TABLE IF EXISTS app.reports
    OWNER to "report-service_user";

DROP TABLE IF EXISTS app.report_properties;

CREATE TABLE IF NOT EXISTS app.report_properties
(
    report_id uuid NOT NULL,
    filename character varying(255),
    CONSTRAINT pk_report_properties PRIMARY KEY (report_id),
    CONSTRAINT fk_reports_report_properties FOREIGN KEY (report_id)
        REFERENCES app.reports (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

ALTER TABLE IF EXISTS app.report_properties
    OWNER to "report-service_user";