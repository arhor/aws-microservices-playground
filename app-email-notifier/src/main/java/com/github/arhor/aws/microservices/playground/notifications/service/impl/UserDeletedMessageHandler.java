package com.github.arhor.aws.microservices.playground.notifications.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arhor.aws.microservices.playground.notifications.model.UserDeletedMessage;
import com.github.arhor.aws.microservices.playground.notifications.repository.UserRepository;
import com.github.arhor.aws.microservices.playground.notifications.service.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UserDeletedMessageHandler implements MessageHandler {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Override
    @SneakyThrows
    public void handle(final String body) {
        final var message = objectMapper.readValue(body, UserDeletedMessage.class);

        userRepository.deleteById(message.getUserId());
    }
}
