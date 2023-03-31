package com.github.arhor.aws.microservices.playground.expenses.web.controller

import com.github.arhor.aws.microservices.playground.expenses.service.ExpenseService
import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseCreateDTO
import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseResultDTO
import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseUpdateDTO
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@Validated
@RestController
@RequestMapping("/expenses")
class ExpenseController(
    private val expenseService: ExpenseService,
) {

    @GetMapping("/{expenseId}")
    fun getExpenseById(@PathVariable expenseId: Long): ExpenseResultDTO {
        return expenseService.getExpenseById(expenseId)
    }

    @GetMapping
    fun getUserExpenses(
        @RequestParam userId: Long,
        @RequestParam(required = false) dateFrom: LocalDate?,
        @RequestParam(required = false) dateTill: LocalDate?,
    ): List<ExpenseResultDTO> {
        return expenseService.getUserExpensesWithinDateRange(userId, dateFrom, dateTill)
    }

    @PostMapping
    fun createExpense(@RequestBody dto: ExpenseCreateDTO): ExpenseResultDTO {
        return expenseService.createExpense(dto)
    }

    @PatchMapping("/{expenseId}")
    fun updateExpense(@PathVariable expenseId: Long, @RequestBody dto: ExpenseUpdateDTO): ExpenseResultDTO {
        return expenseService.updateExpense(expenseId, dto)
    }

    @DeleteMapping("/{expenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteExpense(@PathVariable expenseId: Long) {
        expenseService.deleteExpenseById(expenseId)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUserExpenses(@RequestParam userId: Long) {
        expenseService.deleteUserExpenses(userId)
    }
}
