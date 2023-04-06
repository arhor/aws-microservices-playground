package com.github.arhor.aws.microservices.playground.overruns.config;

import com.github.arhor.aws.microservices.playground.overruns.service.BudgetOverrunTrackerService;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(
    modules = {
        Module_Bindings.class,
        Module_Provider.class,
    }
)
public interface ServiceFactory {

    BudgetOverrunTrackerService budgetOverrunTrackerService();
}
