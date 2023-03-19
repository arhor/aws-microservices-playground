package com.github.arhor.aws.microservices.playground.notifications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class NotificationMapperImpl implements NotificationMapper {
    private final ObjectMapper objectMapper;

    public NotificationMapperImpl() {
        this(new JsonMapper());
    }

    NotificationMapperImpl(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Notification convert(final String data) throws JsonProcessingException {
        return objectMapper.readValue(data, Notification.class);
    }
}
