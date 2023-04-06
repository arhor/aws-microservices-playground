package com.github.arhor.aws.microservices.playground.notifications;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.github.arhor.aws.microservices.playground.notifications.config.DaggerServiceFactory;
import com.github.arhor.aws.microservices.playground.notifications.service.SQSMessageProcessorService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
@SuppressWarnings("unused")
public class SQSEventHandler implements RequestHandler<SQSEvent, SQSBatchResponse> {

    private final SQSMessageProcessorService sqsMessageProcessorService;

    public SQSEventHandler() {
        this(DaggerServiceFactory.create().sqsMessageProcessorService());
    }

    SQSEventHandler(final SQSMessageProcessorService sqsMessageProcessorService) {
        this.sqsMessageProcessorService = sqsMessageProcessorService;
    }

    @Override
    public SQSBatchResponse handleRequest(final SQSEvent event, final Context context) {
        final var errors = new ArrayList<SQSBatchResponse.BatchItemFailure>();

        for (final var message : event.getRecords()) {
            final var messageId = message.getMessageId();
            log.debug("Processing of the SQS message with id = [{}] - START", messageId);
            try {
                sqsMessageProcessorService.process(message);
                log.debug("Processing of the SQS message with id = [{}] - SUCCESS", messageId);
            } catch (final Exception e) {
                log.error("Processing of the SQS message with id = [{}] - FAILURE", messageId, e);
                errors.add(new SQSBatchResponse.BatchItemFailure(messageId));
            }
        }
        return new SQSBatchResponse(errors);
    }
}
