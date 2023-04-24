-- table 'expenses' >>> START
CREATE TABLE IF NOT EXISTS "expenses"
(
    "id"                   BIGSERIAL         NOT NULL PRIMARY KEY,
    "user_id"              BIGINT            NOT NULL,
    "amount"               DECIMAL(12, 2)    NOT NULL,
    "date"                 DATE              NOT NULL,
    "version"              BIGINT            NOT NULL,
    "created_date_time"    TIMESTAMP         NOT NULL,
    "updated_date_time"    TIMESTAMP         NULL
) WITH (OIDS = FALSE);
-- table 'expenses' <<< END
