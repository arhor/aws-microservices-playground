package com.github.arhor.aws.microservices.playground.notifications.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arhor.aws.microservices.playground.notifications.model.BudgetOverrunFoundMessage;
import com.github.arhor.aws.microservices.playground.notifications.repository.UserRepository;
import com.github.arhor.aws.microservices.playground.notifications.service.MessageHandler;
import com.github.arhor.aws.microservices.playground.notifications.service.UserEmailSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class BudgetOverrunMessageHandler implements MessageHandler {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final UserEmailSender userEmailSender;

    @Override
    @SneakyThrows
    public void handle(final String body) {
        final var message = objectMapper.readValue(body, BudgetOverrunFoundMessage.class);
        final var userId = message.getUserId();

        userRepository.findById(userId).ifPresentOrElse(
            user -> {
                userEmailSender.sendOverrunNotification(
                    user.getEmail(),
                    user.getBudgetLimit(),
                    message.getAmount()
                );
            },
            () -> {
                log.debug("Received BudgetOverrunMessage, but user with id: {} is not found", userId);
            }
        );
    }
}
