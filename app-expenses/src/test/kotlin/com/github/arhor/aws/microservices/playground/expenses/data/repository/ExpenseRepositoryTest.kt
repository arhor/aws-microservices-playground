package com.github.arhor.aws.microservices.playground.expenses.data.repository

import com.github.arhor.aws.microservices.playground.expenses.data.model.Expense
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate

internal class ExpenseRepositoryTest : RepositoryTestBase() {

    @Autowired
    protected lateinit var expenseRepository: ExpenseRepository

    @Test
    fun `should pass`() {
        // Given
        val monday = LocalDate.of(2023, 3, 27)

        val expenses = listOf(
            Expense(
                userId = 1L,
                amount = BigDecimal("10.00"),
                date = monday
            ),
            Expense(
                userId = 1L,
                amount = BigDecimal("10.00"),
                date = monday
            ),
            Expense(
                userId = 1L,
                amount = BigDecimal("10.00"),
                date = monday
            ),
            Expense(
                userId = 2L,
                amount = BigDecimal("30.00"),
                date = monday
            ),
            Expense(
                userId = 3L,
                amount = BigDecimal("16.00"),
                date = monday
            ),
            Expense(
                userId = 3L,
                amount = BigDecimal("16.00"),
                date = monday
            ),
        )

        // When
        expenseRepository.saveAll(expenses)

        // Then
        expenseRepository.findBudgetOverrunsWithinDateRange(
            budgetLimit = 29.0.toBigDecimal(),
            startDate = monday,
            endDate = monday,
            userIdsToSkip = emptyList(),
        ).use { it.forEach(System.out::println) }
    }
}
