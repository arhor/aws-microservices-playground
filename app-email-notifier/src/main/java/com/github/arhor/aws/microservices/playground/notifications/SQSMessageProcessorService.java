package com.github.arhor.aws.microservices.playground.notifications;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.http.HttpClient;

@Singleton
public class SQSMessageProcessorService {

    private final HttpClient httpClient;
    private final NotificationReadingConverter notificationReadingConverter;

    @Inject
    public SQSMessageProcessorService(
        final HttpClient httpClient,
        final NotificationReadingConverter notificationReadingConverter
    ) {
        this.httpClient = httpClient;
        this.notificationReadingConverter = notificationReadingConverter;
    }

    public void process(final SQSEvent.SQSMessage message) throws IOException {
        final var notification = notificationReadingConverter.convert(message.getBody());

        // all business logic should be placed here
    }
}
