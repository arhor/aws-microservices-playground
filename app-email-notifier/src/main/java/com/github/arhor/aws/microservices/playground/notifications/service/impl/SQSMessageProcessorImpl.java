package com.github.arhor.aws.microservices.playground.notifications.service.impl;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arhor.aws.microservices.playground.notifications.service.MessageHandler;
import com.github.arhor.aws.microservices.playground.notifications.service.SQSMessageProcessor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SQSMessageProcessorImpl implements SQSMessageProcessor {

    private static final String HEADER_PAYLOAD_TYPE = "xPayloadType";

    private final ObjectMapper objectMapper;
    private final Map<String, MessageHandler> messageHandlers;

    @Override
    @SneakyThrows
    public void process(final SQSEvent.SQSMessage message) {
        final var notification = objectMapper.readValue(message.getBody(), SNSEvent.SNS.class);
        final var messageAttributes = notification.getMessageAttributes();

        if (messageAttributes != null) {
            final var payloadType = messageAttributes.get(HEADER_PAYLOAD_TYPE);

            if (payloadType != null) {
                final var messageHandler = messageHandlers.get(payloadType.getValue());

                if (messageHandler != null) {
                    messageHandler.handle(notification.getMessage());
                } else {
                    throw new IllegalArgumentException("Unknown payload type: " + payloadType);
                }
            } else {
                throw new IllegalArgumentException("Required message attribute is missing: " + HEADER_PAYLOAD_TYPE);
            }
        } else {
            throw new IllegalArgumentException("MessageAttributes cannot be null");
        }
    }
}
