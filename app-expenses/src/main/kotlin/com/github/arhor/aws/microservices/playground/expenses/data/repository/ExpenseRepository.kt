package com.github.arhor.aws.microservices.playground.expenses.data.repository

import com.github.arhor.aws.microservices.playground.expenses.data.model.Expense
import com.github.arhor.aws.microservices.playground.expenses.data.model.projection.BudgetOverrunDetails
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.math.BigDecimal
import java.time.LocalDate
import java.util.stream.Stream

interface ExpenseRepository : CrudRepository<Expense, Long> {

    /**
     * @param budgetLimit
     * @param startDate
     * @param endDate
     * @param userIdsToSkip
     */
    @Query(name = "Expense.findBudgetOverrunsWithinDateRange")
    fun findBudgetOverrunsWithinDateRange(
        budgetLimit: BigDecimal,
        startDate: LocalDate,
        endDate: LocalDate,
        userIdsToSkip: List<Long>,
    ): Stream<BudgetOverrunDetails>
}
