package com.github.arhor.aws.microservices.playground.notifications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NotificationMapper {
    private final ObjectMapper objectMapper;

    @Inject
    public NotificationMapper(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Notification mapStringToNotification(final String data) throws JsonProcessingException {
        return objectMapper.readValue(data, Notification.class);
    }
}
