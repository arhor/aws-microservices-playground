package com.github.arhor.aws.microservices.playground.notifications;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class SQSMessageProcessorService {

    private final NotificationReadingConverter notificationReadingConverter;

    @Inject
    public SQSMessageProcessorService(final NotificationReadingConverter notificationReadingConverter) {
        this.notificationReadingConverter = notificationReadingConverter;
    }

    public void process(final SQSEvent.SQSMessage message) throws IOException {
        final var notification = notificationReadingConverter.convert(message.getBody());

        System.out.println(notification);

        // all business logic should be placed here
    }
}
