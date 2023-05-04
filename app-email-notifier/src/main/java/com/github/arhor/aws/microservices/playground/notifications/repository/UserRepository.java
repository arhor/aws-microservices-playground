package com.github.arhor.aws.microservices.playground.notifications.repository;

import com.github.arhor.aws.microservices.playground.notifications.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(long userId);

    void save(User user);

    void deleteById(long userId);
}
