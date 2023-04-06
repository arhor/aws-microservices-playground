package com.github.arhor.aws.microservices.playground.overruns;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.github.arhor.aws.microservices.playground.overruns.config.DaggerServiceFactory;
import com.github.arhor.aws.microservices.playground.overruns.service.BudgetOverrunTrackerService;

@SuppressWarnings("unused")
public class ScheduledEventHandler implements RequestHandler<ScheduledEvent, Void> {

    private final BudgetOverrunTrackerService budgetOverrunTrackerService;

    public ScheduledEventHandler() {
        this(DaggerServiceFactory.create().budgetOverrunTrackerService());
    }

    ScheduledEventHandler(final BudgetOverrunTrackerService budgetOverrunTrackerService) {
        this.budgetOverrunTrackerService = budgetOverrunTrackerService;
    }

    @Override
    public Void handleRequest(final ScheduledEvent input, final Context context) {
        final var logger = context.getLogger();
        try {
            budgetOverrunTrackerService.findOverrunsAndSendNotifications();
        } catch (final Exception exception) {
            logger.log(
                "[ERROR] An error occurred handling scheduled event with id: %s - %s".formatted(
                    input.getId(),
                    exception
                )
            );
            throw new RuntimeException(exception);
        }
        return null;
    }
}
