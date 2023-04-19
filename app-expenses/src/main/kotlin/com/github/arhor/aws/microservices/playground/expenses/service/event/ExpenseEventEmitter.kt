package com.github.arhor.aws.microservices.playground.expenses.service.event

interface ExpenseEventEmitter {

    fun emit(event: ExpenseEvent)
}
