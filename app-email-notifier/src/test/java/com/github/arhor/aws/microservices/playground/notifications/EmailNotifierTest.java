//package com.github.arhor.aws.microservices.playground.notifications;
//
//import com.amazonaws.services.lambda.runtime.events.SQSEvent;
//import com.amazonaws.services.lambda.runtime.tests.annotations.Event;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class EmailNotifierTest {
//
//    @Mock
//    private NotificationMapper notificationMapper;
//
//    @InjectMocks
//    private EmailNotifier emailNotifier;
//
//    @ParameterizedTest
//    @Event(value = "sqs_event.json", type = SQSEvent.class)
//    void should_pass(final SQSEvent sqsEvent) {
//        // given
//        // when
//        // then
//    }
//}
