package com.github.arhor.aws.microservices.playground.notifications;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class SQSMessageProcessor {

    private final NotificationMapper notificationMapper;

    @Inject
    public SQSMessageProcessor(final NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    public void process(final SQSEvent.SQSMessage message) throws IOException {
        final var notification = notificationMapper.mapStringToNotification(message.getBody());

        System.out.println(notification);

        // all business logic should be placed here
    }
}
