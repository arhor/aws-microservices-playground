package com.github.arhor.aws.microservices.playground.expenses.service

import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseCreateDTO
import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseResultDTO
import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseUpdateDTO
import java.time.LocalDate

interface ExpenseService {
    fun getExpenseById(expenseId: Long): ExpenseResultDTO
    fun getUserExpensesWithinDateRange(userId: Long, dateFrom: LocalDate?, dateTill: LocalDate?): List<ExpenseResultDTO>
    fun createExpense(dto: ExpenseCreateDTO): ExpenseResultDTO
    fun updateExpense(expenseId: Long, dto: ExpenseUpdateDTO): ExpenseResultDTO
    fun deleteExpenseById(expenseId: Long)
    fun deleteUserExpenses(userId: Long)
}
