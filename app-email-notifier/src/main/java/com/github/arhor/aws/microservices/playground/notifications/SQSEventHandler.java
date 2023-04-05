package com.github.arhor.aws.microservices.playground.notifications;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class SQSEventHandler implements RequestHandler<SQSEvent, SQSBatchResponse> {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
            logger.debug("Processing of the SQS message with id = [{}] - START", messageId);
            try {
                sqsMessageProcessorService.process(message);
                logger.debug("Processing of the SQS message with id = [{}] - SUCCESS", messageId);
            } catch (final Exception e) {
                logger.error("Processing of the SQS message with id = [{}] - FAILURE", messageId, e);
                errors.add(new SQSBatchResponse.BatchItemFailure(messageId));
            }
        }
        return new SQSBatchResponse(errors);
    }
}
