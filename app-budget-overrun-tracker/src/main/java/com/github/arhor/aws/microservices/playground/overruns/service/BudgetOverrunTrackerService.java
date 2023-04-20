package com.github.arhor.aws.microservices.playground.overruns.service;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;

public interface BudgetOverrunTrackerService {

    void findOverrunsAndSendNotifications();

    void processSqsEvent(SQSEvent event);
}
