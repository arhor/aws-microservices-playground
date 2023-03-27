package com.github.arhor.aws.microservices.playground.expenses.data.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class ExpenseRepositoryTest : RepositoryTestBase() {

    @Autowired
    protected lateinit var expenseRepository: ExpenseRepository

    @Test
    fun `should pass`() {
        println(expenseRepository)
    }
}
