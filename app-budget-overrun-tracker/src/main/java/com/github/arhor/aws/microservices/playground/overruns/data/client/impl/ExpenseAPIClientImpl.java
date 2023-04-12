package com.github.arhor.aws.microservices.playground.overruns.data.client.impl;

import com.github.arhor.aws.microservices.playground.overruns.data.client.ExpenseAPIClient;
import com.github.arhor.aws.microservices.playground.overruns.data.model.BudgetOverrunDetails;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.util.stream.Collectors.joining;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ExpenseAPIClientImpl implements ExpenseAPIClient {

    private static final URI EXPENSE_API_BASE_URI = URI.create(System.getenv("EXPENSE_API_BASE_URI"));

    private static final String PARAM_DATE_FROM = "dateFrom";
    private static final String PARAM_DATE_TILL = "dateTill";
    private static final String PARAM_SKIP_USER_IDS = "skipUserIds";

    private static final String HEADER_ACCEPT = "Accept";
    private static final String MEDIA_TYPE_APPLICATION_JSON = "application/json";

    private static final String SEPARATOR = ",";

    private final HttpClient httpClient;
    private final JsonStreamParser streamParser;

    @Override
    @SneakyThrows
    public void findBudgetOverrunDetailsInCurrentMonth(
        final List<? extends String> userIdsToExclude,
        final Consumer<BudgetOverrunDetails> consumer
    ) {
        final var currentDate = LocalDate.now();
        final var monthStart = currentDate.with(firstDayOfMonth()).toString();
        final var monthEnd = currentDate.with(lastDayOfMonth()).toString();
        final var userIds = convertToCommaSeparatedString(userIdsToExclude);

        final var uri =
            new URIBuilder(EXPENSE_API_BASE_URI)
                .setParameter(PARAM_DATE_FROM, monthStart)
                .setParameter(PARAM_DATE_TILL, monthEnd)
                .setParameter(PARAM_SKIP_USER_IDS, userIds)
                .build();

        final var request =
            HttpRequest.newBuilder(uri)
                .header(HEADER_ACCEPT, MEDIA_TYPE_APPLICATION_JSON)
                .build();

        final var response = httpClient.send(request, BodyHandlers.ofInputStream());

        streamParser.parse(response.body(), new TypedConsumer<>(BudgetOverrunDetails.class, consumer));
    }

    private String convertToCommaSeparatedString(final List<?> values) {
        return values.stream()
            .filter(Objects::nonNull)
            .map(Object::toString)
            .collect(joining(SEPARATOR));
    }
}
