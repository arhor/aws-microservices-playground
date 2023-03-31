package com.github.arhor.aws.microservices.playground.expenses.data.model.projection

import org.springframework.data.annotation.PersistenceCreator
import java.math.BigDecimal

data class BudgetOverrunDetails @PersistenceCreator constructor(
    val userId: Long,
    val amount: BigDecimal,
) {
    internal constructor(userId: Long, amount: String) : this(userId, BigDecimal(amount))
}
