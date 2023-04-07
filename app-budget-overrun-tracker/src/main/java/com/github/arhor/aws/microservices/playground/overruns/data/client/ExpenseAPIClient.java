package com.github.arhor.aws.microservices.playground.overruns.data.client;

import com.github.arhor.aws.microservices.playground.overruns.data.model.BudgetOverrunDetails;

import java.util.List;
import java.util.function.Consumer;

public interface ExpenseAPIClient {

    void findBudgetOverrunDetailsInCurrentMonth(
        List<? extends String> userIdsToExclude,
        Consumer<BudgetOverrunDetails> consumer
    );
}
