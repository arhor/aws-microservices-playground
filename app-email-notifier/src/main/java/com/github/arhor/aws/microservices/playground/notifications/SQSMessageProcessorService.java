package com.github.arhor.aws.microservices.playground.notifications;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

@Singleton
public class SQSMessageProcessorService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
        logger.debug("Deserialized message content: {}", notification);

        logger.debug("Trying to fetch user information by id: {}", notification.user());
        final var user = usersApiClient.getUserById(notification.user());
        logger.debug("Received the user details: {}", user);

        userEmailSender.sendOverrunNotification(
            user.email(),
            user.budgetLimit(),
            notification.text()
        );
    }
}
