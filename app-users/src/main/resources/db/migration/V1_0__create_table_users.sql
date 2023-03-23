-- table 'users' >>> START
CREATE TABLE IF NOT EXISTS "users"
(
    "id"              BIGSERIAL         NOT NULL PRIMARY KEY,
    "email"           VARCHAR(128)      NULL UNIQUE,
    "password"        VARCHAR(1024)     NULL,
    "budget_limit"    DECIMAL(12, 2)    NOT NULL,
    "version"         BIGINT            NOT NULL
) WITH (OIDS = FALSE);
-- table 'users' <<< END
