package com.github.arhor.aws.microservices.playground.notifications.repository.impl;

import com.github.arhor.aws.microservices.playground.notifications.model.User;
import com.github.arhor.aws.microservices.playground.notifications.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public final class UserRepositoryImpl implements UserRepository {

    private final Supplier<Connection> dbConnectionSource;

    @Override
    @SneakyThrows
    public Optional<User> findById(final long userId) {
        var users = new ArrayList<User>(1);
        try (
            final var conn = dbConnectionSource.get();
            final var stmt = conn.prepareStatement("SELECT * FROM \"users\" u WHERE u.id = ?");
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
    public void save(final User user) {

    }

    @Override
    public void deleteById(final long userId) {

    }
}
