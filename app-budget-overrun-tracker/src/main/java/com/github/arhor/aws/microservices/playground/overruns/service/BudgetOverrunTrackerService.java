package com.github.arhor.aws.microservices.playground.overruns.service;

import java.io.IOException;

public interface BudgetOverrunTrackerService {

    void findOverrunsAndSendNotifications() throws IOException, InterruptedException;
}
