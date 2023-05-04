package com.github.arhor.aws.microservices.playground.notifications;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.github.arhor.aws.microservices.playground.notifications.config.DaggerServiceFactory;
import com.github.arhor.aws.microservices.playground.notifications.service.SQSMessageProcessor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
@SuppressWarnings("unused")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SQSEventHandler implements RequestHandler<SQSEvent, SQSBatchResponse> {

    private static final String LOG_MESSAGE_TEMPLATE = "Processing of the SQS message with id = [{}] - {}";

    private final SQSMessageProcessor sqsMessageProcessor;

    public SQSEventHandler() {
        this(DaggerServiceFactory.create().sqsMessageProcessorService());
    }

    @Override
    public SQSBatchResponse handleRequest(final SQSEvent event, final Context context) {
        final var errors = new ArrayList<SQSBatchResponse.BatchItemFailure>();

        for (final var message : event.getRecords()) {
            final var messageId = message.getMessageId();
            log.debug(LOG_MESSAGE_TEMPLATE, messageId, "START");
            try {
                sqsMessageProcessor.process(message);
                log.debug(LOG_MESSAGE_TEMPLATE, messageId, "SUCCESS");
            } catch (final Exception e) {
                log.error(LOG_MESSAGE_TEMPLATE, messageId, "FAILURE", e);
                errors.add(new SQSBatchResponse.BatchItemFailure(messageId));
            }
        }
        return new SQSBatchResponse(errors);
    }
}
