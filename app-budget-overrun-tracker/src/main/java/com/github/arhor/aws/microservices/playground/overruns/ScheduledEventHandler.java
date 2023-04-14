package com.github.arhor.aws.microservices.playground.overruns;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.github.arhor.aws.microservices.playground.overruns.config.DaggerServiceFactory;
import com.github.arhor.aws.microservices.playground.overruns.service.BudgetOverrunTrackerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ScheduledEventHandler implements RequestHandler<ScheduledEvent, Void> {

    private final BudgetOverrunTrackerService budgetOverrunTrackerService;

    public ScheduledEventHandler() {
        this(DaggerServiceFactory.create().budgetOverrunTrackerService());
    }

    @Override
    public Void handleRequest(final ScheduledEvent input, final Context context) {
        try {
            budgetOverrunTrackerService.findOverrunsAndSendNotifications();
        } catch (final Exception exception) {
            log.error(
                "An error occurred handling scheduled event with id: {} - {}",
                input.getId(),
                exception
            );
            throw new RuntimeException(exception);
        }
        return null;
    }
}
