package com.github.arhor.aws.microservices.playground.overruns;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NotificationWritingConverter {
    private final ObjectMapper objectMapper;

    @Inject
    public NotificationWritingConverter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String convert(final Notification data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
}
