package com.github.arhor.aws.microservices.playground.notifications;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import java.util.List;

final class TestObjectFactory {

    private TestObjectFactory() {}

    static SQSEvent.SQSMessage createSqsMessage(final String messageId, final String messageBody) {
        final var sqsMessage = new SQSEvent.SQSMessage();

        sqsMessage.setMessageId(messageId);
        sqsMessage.setBody(messageBody);

        return sqsMessage;
    }

    static SQSEvent createSQSEvent(final SQSEvent.SQSMessage... messages) {
        final var sqsEvent = new SQSEvent();

        sqsEvent.setRecords(List.of(messages));

        return sqsEvent;
    }
}
