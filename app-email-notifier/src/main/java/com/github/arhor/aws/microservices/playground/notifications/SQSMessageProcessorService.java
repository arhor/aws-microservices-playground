package com.github.arhor.aws.microservices.playground.notifications;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class SQSMessageProcessorService {

    private final ObjectMapper objectMapper;
    private final UsersApiClient usersApiClient;
    private final UserEmailSender userEmailSender;

    @Inject
    public SQSMessageProcessorService(
        final ObjectMapper objectMapper,
        final UsersApiClient usersApiClient,
        final UserEmailSender userEmailSender
    ) {
        this.usersApiClient = usersApiClient;
        this.objectMapper = objectMapper;
        this.userEmailSender = userEmailSender;
    }

    public void process(final SQSEvent.SQSMessage message) throws IOException, InterruptedException {
        final var notification = objectMapper.readValue(message.getBody(), Notification.class);
        final var user = usersApiClient.getUserById(notification.user());

        userEmailSender.sendOverrunNotification(
            user.email(),
            user.budgetLimit(),
            notification.text()
        );
    }
}
