package com.github.arhor.aws.microservices.playground.expenses.data.model.projection

import java.math.BigDecimal

data class BudgetOverrunDetails(
    val userId: Long,
    val amount: BigDecimal,
)
