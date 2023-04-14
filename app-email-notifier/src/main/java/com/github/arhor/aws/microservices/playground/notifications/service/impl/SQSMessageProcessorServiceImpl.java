package com.github.arhor.aws.microservices.playground.notifications.service.impl;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arhor.aws.microservices.playground.notifications.model.Notification;
import com.github.arhor.aws.microservices.playground.notifications.service.SQSMessageProcessorService;
import com.github.arhor.aws.microservices.playground.notifications.service.UserEmailSender;
import com.github.arhor.aws.microservices.playground.notifications.service.UsersApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SQSMessageProcessorServiceImpl implements SQSMessageProcessorService {

    private final ObjectMapper objectMapper;
    private final UsersApiClient usersApiClient;
    private final UserEmailSender userEmailSender;

    @Override
    public void process(final SQSEvent.SQSMessage message) throws IOException, InterruptedException {
        final var notification = objectMapper.readValue(message.getBody(), Notification.class);
        log.debug("Deserialized message content: {}", notification);

        log.debug("Trying to fetch user information by id: {}", notification.getUser());
        final var user = usersApiClient.getUserById(notification.getUser());
        log.debug("Received the user details: {}", user);

        userEmailSender.sendOverrunNotification(
            user.getEmail(),
            user.getBudgetLimit(),
            notification.getText()
        );
    }
}
