package com.github.arhor.aws.microservices.playground.overruns;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;

@SuppressWarnings("unused")
public class ScheduledEventHandler implements RequestHandler<ScheduledEvent, Void> {

    private final BudgetOverrunTrackingService budgetOverrunTrackingService;

    public ScheduledEventHandler() {
        this(DaggerServiceFactory.create().budgetOverrunTrackingService());
    }

    ScheduledEventHandler(final BudgetOverrunTrackingService budgetOverrunTrackingService) {
        this.budgetOverrunTrackingService = budgetOverrunTrackingService;
    }

    @Override
    public Void handleRequest(final ScheduledEvent input, final Context context) {
        budgetOverrunTrackingService.findOverrunsAndSendNotifications();
        return null;
    }
}
