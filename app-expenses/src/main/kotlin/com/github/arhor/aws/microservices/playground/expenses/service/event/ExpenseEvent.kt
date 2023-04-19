package com.github.arhor.aws.microservices.playground.expenses.service.event

sealed interface ExpenseEvent {

    data class Deleted(val expenseId: Long) : ExpenseEvent
    data class Updated(val expenseId: Long) : ExpenseEvent
}
