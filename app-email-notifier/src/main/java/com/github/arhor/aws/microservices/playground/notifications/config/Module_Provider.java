package com.github.arhor.aws.microservices.playground.notifications.config;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import javax.sql.DataSource;

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
    static DataSource dataSource() {
        final var dbUrl = require("JDBC_DATABASE_URL");
        final var dbUsername = require("JDBC_DATABASE_USERNAME");
        final var dbPassword = require("JDBC_DATABASE_PASSWORD");

        final var config = new HikariConfig();

        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        config.setAutoCommit(false);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(config);
    }

    @Provides
    @Singleton
    static AmazonSimpleEmailService amazonSimpleEmailService() {
        return AmazonSimpleEmailServiceClientBuilder.defaultClient();
    }

    private static String require(final String name) {
        final var envVariable = System.getenv(name);

        if ((envVariable == null) || envVariable.isEmpty() || envVariable.isBlank()) {
            throw new IllegalStateException("Missing environment variable: '" + name + "'");
        }
        return envVariable;
    }
}
