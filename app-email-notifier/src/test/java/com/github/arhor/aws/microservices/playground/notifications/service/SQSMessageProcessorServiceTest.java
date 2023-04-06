package com.github.arhor.aws.microservices.playground.notifications.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arhor.aws.microservices.playground.notifications.model.Notification;
import com.github.arhor.aws.microservices.playground.notifications.model.User;
import com.github.arhor.aws.microservices.playground.notifications.service.impl.SQSMessageProcessorServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import static com.github.arhor.aws.microservices.playground.notifications.TestObjectFactory.createSqsMessage;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class SQSMessageProcessorServiceTest {

    private final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private final UsersApiClient usersApiClient = mock(UsersApiClient.class);
    private final UserEmailSender userEmailSender = mock(UserEmailSender.class);

    private final SQSMessageProcessorService sqsMessageProcessorService = new SQSMessageProcessorServiceImpl(
        objectMapper,
        usersApiClient,
        userEmailSender
    );

    @Test
    void should_extract_notification_object_then_fetch_user_details_then_send_overrun_email_notification()
        throws Exception {

        // Given
        var sqsMessage = createSqsMessage("message-id", "message-body");

        var notification = new Notification("user", "text");
        var user = new User(notification.user(), "email", "limit");

        doReturn(notification)
            .when(objectMapper)
            .readValue(anyString(), ArgumentMatchers.<Class<Notification>>any());

        doReturn(user)
            .when(usersApiClient)
            .getUserById(anyString());

        doNothing()
            .when(userEmailSender)
            .sendOverrunNotification(anyString(), anyString(), anyString());

        // When
        sqsMessageProcessorService.process(sqsMessage);

        // Then
        then(objectMapper)
            .should()
            .readValue(sqsMessage.getBody(), Notification.class);

        then(usersApiClient)
            .should()
            .getUserById(notification.user());

        then(userEmailSender)
            .should()
            .sendOverrunNotification(user.email(), user.budgetLimit(), notification.text());
    }
}
