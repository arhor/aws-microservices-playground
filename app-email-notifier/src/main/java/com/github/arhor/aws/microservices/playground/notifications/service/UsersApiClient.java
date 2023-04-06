package com.github.arhor.aws.microservices.playground.notifications.service;

import com.github.arhor.aws.microservices.playground.notifications.model.User;

import java.io.IOException;

public interface UsersApiClient {

    User getUserById(final String id) throws IOException, InterruptedException;
}
