package com.github.arhor.aws.microservices.playground.notifications.config;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * Declares provider methods, which return instantiated and preconfigured objects.
 */
@Module
abstract class Module_Provider {

    @Provides
    @Singleton
    static ObjectMapper objectMapper() {
        return JsonMapper.builder().build();
    }

    @Provides
    @Singleton
    static Supplier<Connection> dbConnectionSource() throws SQLException {
        final var dbUrl = System.getenv("JDBC_DATABASE_URL");
        final var dbUsername = System.getenv("JDBC_DATABASE_USERNAME");
        final var dbPassword = System.getenv("JDBC_DATABASE_PASSWORD");

        return () -> {
            try {
                return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Provides
    @Singleton
    static AmazonSimpleEmailService amazonSimpleEmailService() {
        return AmazonSimpleEmailServiceClientBuilder.defaultClient();
    }
}
