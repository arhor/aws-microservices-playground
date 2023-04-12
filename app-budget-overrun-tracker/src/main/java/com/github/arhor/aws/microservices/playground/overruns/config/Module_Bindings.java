package com.github.arhor.aws.microservices.playground.overruns.config;

import com.github.arhor.aws.microservices.playground.overruns.data.client.ExpenseAPIClient;
import com.github.arhor.aws.microservices.playground.overruns.data.client.impl.ExpenseAPIClientImpl;
import com.github.arhor.aws.microservices.playground.overruns.service.BudgetOverrunTrackerService;
import com.github.arhor.aws.microservices.playground.overruns.service.impl.BudgetOverrunTrackerServiceImpl;
import dagger.Binds;
import dagger.Module;

/**
 * Declares bindings between interfaces/abstract classes and concrete implementations.
 */
@Module
interface Module_Bindings {

    @Binds
    BudgetOverrunTrackerService budgetOverrunTrackerService(BudgetOverrunTrackerServiceImpl impl);

    @Binds
    ExpenseAPIClient expenseAPIClient(ExpenseAPIClientImpl impl);
}
