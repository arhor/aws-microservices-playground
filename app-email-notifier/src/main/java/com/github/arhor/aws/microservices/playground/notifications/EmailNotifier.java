package com.github.arhor.aws.microservices.playground.notifications;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

@SuppressWarnings("unused")
public class EmailNotifier implements RequestHandler<SQSEvent, Void> {

    private final NotificationMapper notificationMapper;

    public EmailNotifier() {
        this(new NotificationMapperImpl());
    }

    EmailNotifier(final NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Override
    public Void handleRequest(final SQSEvent input, final Context context) {
        final var logger = context.getLogger();

        // 1. remove outdated user records which were notified in previous months

        logger.log("Received a request to the AWS lambda function with the following payload: " + input);

        return null;
    }
}
