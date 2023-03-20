package com.github.arhor.aws.microservices.playground.overruns;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = ServiceModule.class)
public interface ServiceFactory {

    BudgetOverrunTrackingService budgetOverrunTrackingService();
}
