package com.github.arhor.aws.microservices.playground.notifications.service.impl;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.github.arhor.aws.microservices.playground.notifications.service.StringInterpolator;
import com.github.arhor.aws.microservices.playground.notifications.service.UserEmailSender;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UserEmailSenderImpl implements UserEmailSender {

    private static final String SES_SENDER_EMAIL_ADDRESS = System.getenv("SES_SENDER_EMAIL_ADDRESS");

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final String SUBJECT = "Amazon SES test (AWS SDK for Java)";

    private static final String ARG_LIMIT = "limit";
    private static final String ARG_VALUE = "value";

    // language=HTML
    private static final String HTML_BODY_TEMPLATE =
        "<h1>\n" +
        "    Budget overrun detected!\n" +
        "</h1>\n" +
        "<p>\n" +
        "    Overrun limit: ${limit}<br/>\n" +
        "    Current value: ${value}<br/>\n" +
        "</p>\n";

    private static final String TEXT_BODY_TEMPLATE =
        "---------- Budget overrun detected! ----------\n" +
        "\n" +
        "            Overrun limit: ${limit}\n" +
        "            Current value: ${value}\n" +
        "\n" +
        "----------------------------------------------\n";

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final StringInterpolator interpolator;

    @Override
    public void sendOverrunNotification(final String email, final double limit, final double value) {
        final var mappings = Map.of(
            ARG_LIMIT, limit,
            ARG_VALUE, value
        );

        final var htmlBody = interpolator.interpolate(HTML_BODY_TEMPLATE, mappings);
        final var textBody = interpolator.interpolate(TEXT_BODY_TEMPLATE, mappings);

        final var request =
            new SendEmailRequest()
                .withSource(SES_SENDER_EMAIL_ADDRESS)
                .withDestination(new Destination(List.of(email)))
                .withMessage(
                    new Message()
                        .withSubject(content(SUBJECT))
                        .withBody(
                            new Body()
                                .withHtml(content(htmlBody))
                                .withText(content(textBody))
                        )
                );

        amazonSimpleEmailService.sendEmail(request);
    }

    private Content content(final String data) {
        return new Content().withCharset(DEFAULT_ENCODING).withData(data);
    }
}
