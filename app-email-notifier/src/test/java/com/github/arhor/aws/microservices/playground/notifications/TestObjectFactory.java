package com.github.arhor.aws.microservices.playground.notifications;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import java.util.List;

public final class TestObjectFactory {

    private TestObjectFactory() {}

    public static SQSEvent.SQSMessage createSqsMessage(final String messageId, final String messageBody) {
        final var sqsMessage = new SQSEvent.SQSMessage();

        sqsMessage.setMessageId(messageId);
        sqsMessage.setBody(messageBody);

        return sqsMessage;
    }

    public static SQSEvent createSQSEvent(final SQSEvent.SQSMessage... messages) {
        final var sqsEvent = new SQSEvent();

        sqsEvent.setRecords(List.of(messages));

        return sqsEvent;
    }
}
