//package com.github.arhor.aws.microservices.playground.notifications;
//
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.from;
//
//class NotificationMapperImplTest {
//
//    private final NotificationMapperImpl notificationMapper = new NotificationMapperImpl();
//
//    @Test
//    void should_pass() throws IOException {
//        // given
//        final var expectedUser = "1";
//        final var expectedText = "test text content";
//
//        // when
//        final var notification = notificationMapper.convert(
//            // @formatter:off
//            """
//            {
//                "user": "%s",
//                "text": "%s"
//            }
//            """.formatted(expectedUser, expectedText)
//            // @formatter:on
//        );
//
//        // then
//
//        assertThat(notification)
//            .returns(expectedUser, from(Notification::user))
//            .returns(expectedText, from(Notification::text));
//    }
//}
