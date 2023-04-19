package com.github.arhor.aws.microservices.playground.expenses.data.repository

import com.github.arhor.aws.microservices.playground.expenses.data.model.Expense
import com.github.arhor.aws.microservices.playground.expenses.data.model.projection.BudgetOverrunDetails
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import java.math.BigDecimal
import java.time.LocalDate
import java.util.stream.Stream

interface ExpenseRepository : CrudRepository<Expense, Long> {

    /**
     * @param skipUserIds list of user ids to skip, cannot be empty
     */
    @Query(name = "Expense.findBudgetOverrunsWithinDateRange")
    fun findBudgetOverrunsWithinDateRange(
        threshold: BigDecimal,
        dateFrom: LocalDate,
        dateTill: LocalDate,
        skipUserIds: List<Long>,
    ): Stream<BudgetOverrunDetails>

    @Query(name = "Expense.findAllWithinDateRangeSkippingUserIds")
    fun findAllWithinDateRangeSkippingUserIds(
        skipUids: List<Long>,
        dateFrom: LocalDate?,
        dateTill: LocalDate?
    ): Stream<Expense>

    @Modifying
    @Query(value = "DELETE FROM expenses e WHERE e.user_id = :userId")
    fun deleteByUserIdReturningNumberRecordsAffected(userId: Long): Int
}
