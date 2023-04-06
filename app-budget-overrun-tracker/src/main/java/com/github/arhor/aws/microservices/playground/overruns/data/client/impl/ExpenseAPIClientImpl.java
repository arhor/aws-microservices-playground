package com.github.arhor.aws.microservices.playground.overruns.data.client.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arhor.aws.microservices.playground.overruns.data.client.ExpenseAPIClient;
import com.github.arhor.aws.microservices.playground.overruns.data.model.BudgetOverrunDetails;
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

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.util.stream.Collectors.joining;

@Singleton
public class ExpenseAPIClientImpl implements ExpenseAPIClient {

    private static final URI EXPENSE_API_BASE_URI = URI.create(System.getenv("EXPENSE_API_BASE_URI"));

    private static final String PARAM_DATE_FROM = "dateFrom";
    private static final String PARAM_DATE_TILL = "dateTill";
    private static final String PARAM_SKIP_USER_IDS = "skipUserIds";

    private static final String HEADER_ACCEPT = "Accept";
    private static final String MEDIA_TYPE_APPLICATION_JSON = "application/json";

    private static final String SEPARATOR = ",";

    private static final TypeReference<List<BudgetOverrunDetails>> BUDGETS_TYPE_REF = new TypeReference<>() {};

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Inject
    private ExpenseAPIClientImpl(
        final HttpClient httpClient,
        final ObjectMapper objectMapper
    ) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    @SneakyThrows
    public List<BudgetOverrunDetails> findBudgetOverrunDetailsInCurrentMonth(
        final List<? extends Long> userIdsToExclude
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

        return objectMapper.readValue(response.body(), BUDGETS_TYPE_REF);
    }

    private String convertToCommaSeparatedString(final List<?> values) {
        return values.stream()
            .filter(Objects::nonNull)
            .map(Object::toString)
            .collect(joining(SEPARATOR));
    }
}
