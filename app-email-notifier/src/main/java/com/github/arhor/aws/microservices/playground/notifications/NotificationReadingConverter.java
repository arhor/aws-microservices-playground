package com.github.arhor.aws.microservices.playground.notifications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NotificationReadingConverter {
    private final ObjectMapper objectMapper;

    @Inject
    public NotificationReadingConverter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Notification convert(final String data) throws JsonProcessingException {
        return objectMapper.readValue(data, Notification.class);
    }
}
