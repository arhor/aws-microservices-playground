package com.github.arhor.aws.microservices.playground.overruns;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.github.arhor.aws.microservices.playground.overruns.config.DaggerServiceFactory;
import com.github.arhor.aws.microservices.playground.overruns.service.BudgetOverrunTrackerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("unused")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BudgetOverrunTrackerRequestHandler implements RequestHandler<Object, Void> {

    private final BudgetOverrunTrackerService budgetOverrunTrackerService;

    public BudgetOverrunTrackerRequestHandler() {
        this(DaggerServiceFactory.create().budgetOverrunTrackerService());
    }

    @Override
    public Void handleRequest(final Object input, final Context context) {

        if (input instanceof ScheduledEvent) {
            var event = (ScheduledEvent) input;
            budgetOverrunTrackerService.findOverrunsAndSendNotifications();
        } else if (input instanceof SQSEvent) {
            var event = (SQSEvent) input;
            budgetOverrunTrackerService.processSqsEvent(event);
        }
        return null;
    }
}
