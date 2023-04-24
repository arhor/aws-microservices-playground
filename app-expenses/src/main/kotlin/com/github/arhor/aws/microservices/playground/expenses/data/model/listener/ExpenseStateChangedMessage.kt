package com.github.arhor.aws.microservices.playground.expenses.data.model.listener

import java.math.BigDecimal
import java.time.LocalDate

sealed interface ExpenseStateChangedMessage {

    data class Updated(val expenseId: Long, val date: LocalDate, val amount: BigDecimal, val userId: Long) :
        ExpenseStateChangedMessage

    data class Deleted(val expenseId: Long)
}
