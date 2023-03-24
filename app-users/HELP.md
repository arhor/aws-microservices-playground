Creating `.env.local` file inside service directory is necessary for the local deployment.

Content of the file for the current service should be the following (with `<placeholders>` replaced):

```properties
POSTGRES_DB       = <database-name>
POSTGRES_PORT     = <database-port>
POSTGRES_USER     = <database-username>
POSTGRES_PASSWORD = <database-password>

JDBC_DATABASE_URL       = jdbc:postgresql://localhost:${POSTGRES_PORT}/${POSTGRES_DB}
JDBC_DATABASE_USERNAME  = ${POSTGRES_USER}
JDBC_DATABASE_PASSWORD  = ${POSTGRES_PASSWORD}

R2DBC_DATABASE_URL      = r2dbc:postgresql://localhost:${POSTGRES_PORT}/${POSTGRES_DB}
R2DBC_DATABASE_USERNAME = ${POSTGRES_USER}
R2DBC_DATABASE_PASSWORD = ${POSTGRES_PASSWORD}
```
