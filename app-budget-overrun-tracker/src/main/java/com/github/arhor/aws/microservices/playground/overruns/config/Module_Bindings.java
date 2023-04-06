package com.github.arhor.aws.microservices.playground.overruns.config;

import com.github.arhor.aws.microservices.playground.overruns.service.BudgetOverrunTrackerService;
import com.github.arhor.aws.microservices.playground.overruns.service.impl.BudgetOverrunTrackerServiceImpl;
import dagger.Binds;
import dagger.Module;

@Module
public interface Module_Bindings {

    @Binds
    BudgetOverrunTrackerService budgetOverrunTrackerService(BudgetOverrunTrackerServiceImpl impl);
}
