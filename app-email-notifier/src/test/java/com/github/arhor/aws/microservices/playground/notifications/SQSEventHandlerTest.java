package com.github.arhor.aws.microservices.playground.notifications;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse.BatchItemFailure;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.arhor.aws.microservices.playground.notifications.TestObjectFactory.createSQSEvent;
import static com.github.arhor.aws.microservices.playground.notifications.TestObjectFactory.createSqsMessage;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class SQSEventHandlerTest {

    private final Context context = mock(Context.class);
    private final SQSMessageProcessorService sqsMessageProcessorService = mock(SQSMessageProcessorService.class);

    private final SQSEventHandler sqsEventHandler = new SQSEventHandler(
        sqsMessageProcessorService
    );

    @Test
    void should_process_sqs_message_without_failures_and_do_not_interact_with_context_object()
        throws Exception {

        // Given
        final var expectedMessageId = "test-message-id-1";
        final var expectedMessageBody = """
            {
                "user": "1",
                "text": "test message text 1"
            }
            """;
        final var sqsMessage = createSqsMessage(expectedMessageId, expectedMessageBody);
        final var sqsEvent = createSQSEvent(sqsMessage);

        doNothing()
            .when(sqsMessageProcessorService)
            .process(any());

        // When
        var sqsBatchResponse = sqsEventHandler.handleRequest(sqsEvent, context);

        // Then
        then(sqsMessageProcessorService)
            .should()
            .process(sqsMessage);

        then(context)
            .shouldHaveNoInteractions();

        assertThat(sqsBatchResponse)
            .isNotNull()
            .returns(emptyList(), from(SQSBatchResponse::getBatchItemFailures));
    }

    @Test
    void should_process_sqs_messages_with_failure_on_second_message_and_do_not_interact_with_context_object()
        throws Exception {

        // Given
        final var messageId1 = "test-message-id-1";
        final var messageBody1 = """
            {
                "user": "1",
                "text": "test message text 1"
            }
            """;

        final var messageId2 = "test-message-id-2";
        final var messageBody2 = """
            {
                "user": "2",
                "text": "test message text 2"
            }
            """;

        final var sqsMessage1 = createSqsMessage(messageId1, messageBody1);
        final var sqsMessage2 = createSqsMessage(messageId2, messageBody2);

        final var sqsEvent = createSQSEvent(sqsMessage1, sqsMessage2);

        final var expectedFailures = List.of(new BatchItemFailure(messageId2));

        doNothing()
            .when(sqsMessageProcessorService)
            .process(sqsMessage1);

        doThrow(RuntimeException.class)
            .when(sqsMessageProcessorService)
            .process(sqsMessage2);

        // When
        var sqsBatchResponse = sqsEventHandler.handleRequest(sqsEvent, context);

        // Then
        then(sqsMessageProcessorService)
            .should()
            .process(sqsMessage1);

        then(sqsMessageProcessorService)
            .should()
            .process(sqsMessage2);

        then(context)
            .shouldHaveNoInteractions();

        assertThat(sqsBatchResponse)
            .isNotNull()
            .returns(expectedFailures, from(SQSBatchResponse::getBatchItemFailures));
    }
}
