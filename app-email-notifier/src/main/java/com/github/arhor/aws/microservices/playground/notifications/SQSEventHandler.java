package com.github.arhor.aws.microservices.playground.notifications;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class SQSEventHandler implements RequestHandler<SQSEvent, SQSBatchResponse> {

    final SQSMessageProcessorService sqsMessageProcessorService;

    public SQSEventHandler() {
        this(DaggerServiceFactory.create().sqsMessageProcessorService());
    }

    SQSEventHandler(final SQSMessageProcessorService sqsMessageProcessorService) {
        this.sqsMessageProcessorService = sqsMessageProcessorService;
    }

    @Override
    public SQSBatchResponse handleRequest(final SQSEvent event, final Context context) {
        final var logger = context.getLogger();
        final var errors = new ArrayList<SQSBatchResponse.BatchItemFailure>();

        for (final var message : event.getRecords()) {
            try {
                sqsMessageProcessorService.process(message);
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
}
