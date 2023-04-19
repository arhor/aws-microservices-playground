package com.github.arhor.aws.microservices.playground.expenses.service

import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseCreateDTO
import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseResultDTO
import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseUpdateDTO
import java.time.LocalDate
import java.util.stream.Stream

interface ExpenseService {
    fun getExpenseById(expenseId: Long): ExpenseResultDTO
    fun getExpenses(skipUids: List<Long>?, dateFrom: LocalDate?, dateTill: LocalDate?): Stream<ExpenseResultDTO>
    fun createExpense(dto: ExpenseCreateDTO): ExpenseResultDTO
    fun updateExpense(expenseId: Long, updateRequest: ExpenseUpdateDTO): ExpenseResultDTO
    fun deleteExpenseById(expenseId: Long)
    fun deleteUserExpenses(userId: Long)
}
