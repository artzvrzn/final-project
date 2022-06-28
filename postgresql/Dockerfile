FROM postgres:14.2-alpine
MAINTAINER ITAcademy

ADD /classifier-service/ddl/*.sql /docker-entrypoint-initdb.d/
ADD /account-service/ddl/*.sql /docker-entrypoint-initdb.d/
ADD /account-scheduler-service/ddl/*.sql /docker-entrypoint-initdb.d/
ADD /report-service/ddl/*.sql /docker-entrypoint-initdb.d/
ADD /user-service/ddl/*.sql /docker-entrypoint-initdb.d/
ADD /mail-scheduler-service/ddl/*.sql /docker-entrypoint-initdb.d/
ADD /telegram-service/ddl/*.sql /docker-entrypoint-initdb.d/