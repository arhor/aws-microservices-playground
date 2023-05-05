package com.github.arhor.aws.microservices.playground.notifications.repository.impl;

import com.github.arhor.aws.microservices.playground.notifications.model.User;
import com.github.arhor.aws.microservices.playground.notifications.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class UserRepositoryImpl implements UserRepository {

    private static final String SQL_SELECT_BY_ID =
        "SELECT u.id, u.email, u.budget_limit FROM users u WHERE u.id = ?";

    private static final String SQL_INSERT_OR_UPDATE =
        "INSERT INTO users (id, email, budget_limit) " +
        "VALUES (?, ?, ?) " +
        "ON CONFLICT (id) " +
        "DO UPDATE SET " +
        "email = EXCLUDED.email, " +
        "budget_limit = EXCLUDED.budget_limit";

    private static final String SQL_DELETE_BY_ID =
        "DELETE FROM users u WHERE u.id = ?";

    private final DataSource dataSource;

    @Override
    @SneakyThrows
    public Optional<User> findById(final long userId) {
        var users = new ArrayList<User>(1);
        try (
            final var conn = dataSource.getConnection();
            final var stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
        ) {
            stmt.setLong(1, userId);

            try (final var result = stmt.executeQuery()) {

                while (result.next()) {
                    final var id = result.getLong("id");
                    final var email = result.getString("email");
                    final var budgetLimit = result.getDouble("budget_limit");

                    users.add(
                        new User(
                            id != 0 ? id : null,
                            email,
                            budgetLimit
                        )
                    );
                }
            }
        }

        if (users.size() <= 1) {
            return users.stream().findFirst();
        } else {
            throw new IllegalStateException("Database returned more that 1 row");
        }
    }

    @Override
    @SneakyThrows
    public void save(final User user) {
        transaction(conn -> {
            try (final var stmt = conn.prepareStatement(SQL_INSERT_OR_UPDATE)) {
                stmt.setLong(1, user.getId());
                stmt.setString(2, user.getEmail());
                stmt.setDouble(3, user.getBudgetLimit());

                final var rowsAffected = stmt.executeUpdate();

                if (rowsAffected != 1) {
                    throw new IllegalStateException("Updated more or less that one row");
                }
            }
        });
    }

    @Override
    @SneakyThrows
    public void deleteById(final long userId) {
        transaction(conn -> {
            try (final var stmt = conn.prepareStatement(SQL_DELETE_BY_ID)) {
                stmt.setLong(1, userId);

                final var rowsAffected = stmt.executeUpdate();

                if (rowsAffected != 1) {
                    throw new IllegalStateException("Updated more or less that one row");
                }
            }
        });
    }

    @SneakyThrows
    private void transaction(final DbAction action) {
        try (final var connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                action.execute(connection);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        }
    }

    @FunctionalInterface
    private interface DbAction {
        void execute(Connection connection) throws SQLException;
    }
}
