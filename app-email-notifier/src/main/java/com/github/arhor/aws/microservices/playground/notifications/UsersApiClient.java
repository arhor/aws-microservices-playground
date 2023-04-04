package com.github.arhor.aws.microservices.playground.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

@Singleton
public class UsersApiClient {

    private static final String USER_SERVICE_URL = System.getenv("USER_SERVICE_URL");
    private static final String USER_API_URL = USER_SERVICE_URL + "/api/users/";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Inject
    public UsersApiClient(
        final HttpClient httpClient,
        final ObjectMapper objectMapper
    ) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public User getUserById(final String id) throws IOException, InterruptedException {
        final var req = HttpRequest.newBuilder(URI.create(USER_API_URL + id)).build();
        final var res = httpClient.send(req, BodyHandlers.ofString());

        return objectMapper.readValue(res.body(), User.class);
    }
}