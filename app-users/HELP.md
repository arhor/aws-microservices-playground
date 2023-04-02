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

AWS_URL        = <aws-host-name>
AWS_REGION     = <aws-region>
AWS_ACCESS_KEY = <aws-access-key>
AWS_SECRET_KEY = <aws-secret-key>
AWS_QUEUE_NAME = <aws-sqs-user-delete-events-queue-name>
```
