package com.github.arhor.aws.microservices.playground.overruns.data.client;

import com.github.arhor.aws.microservices.playground.overruns.data.model.BudgetOverrunDetails;

import java.util.List;

public interface ExpenseAPIClient {

    List<BudgetOverrunDetails> findBudgetOverrunDetailsInCurrentMonth(List<? extends Long> userIdsToExclude);
}
