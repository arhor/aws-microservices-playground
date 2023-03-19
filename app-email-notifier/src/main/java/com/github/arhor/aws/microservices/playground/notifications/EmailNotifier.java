package com.github.arhor.aws.microservices.playground.notifications;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class EmailNotifier implements RequestHandler<SQSEvent, SQSBatchResponse> {

    private final NotificationMapper notificationMapper;

    public EmailNotifier() {
        this(new NotificationMapperImpl());
    }

    EmailNotifier(final NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Override
    public SQSBatchResponse handleRequest(final SQSEvent input, final Context context) {
        final var logger = context.getLogger();
        final var errors = new ArrayList<SQSBatchResponse.BatchItemFailure>();

        for (final var message : input.getRecords()) {
            try {
                processSQSMessage(message);
            } catch (final Exception exception) {
                final var messageId = message.getMessageId();
                logger.log(
                    "[ERROR] An error occurred processing SQS message with id: %s - %s".formatted(
                        messageId,
                        exception
                    )
                );
                errors.add(new SQSBatchResponse.BatchItemFailure(messageId));
            }
        }

        return new SQSBatchResponse(errors);
    }

    private void processSQSMessage(final SQSEvent.SQSMessage message) throws IOException {
        final var notification = notificationMapper.convert(message.getBody());
        // business logic...
    }
}
