package com.github.arhor.aws.microservices.playground.expenses.service.impl

import com.github.arhor.aws.microservices.playground.expenses.data.repository.ExpenseRepository
import com.github.arhor.aws.microservices.playground.expenses.service.ExpenseService
import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseCreateDTO
import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseResultDTO
import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseUpdateDTO
import com.github.arhor.aws.microservices.playground.expenses.service.mapper.ExpenseMapper
import org.slf4j.LoggerFactory
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class ExpenseServiceImpl(
    private val expenseMapper: ExpenseMapper,
    private val expenseRepository: ExpenseRepository,
) : ExpenseService {


    override fun getExpenseById(expenseId: Long): ExpenseResultDTO {
        TODO("Not yet implemented")
    }

    override fun getUserExpensesWithinDateRange(
        userId: Long,
        dateFrom: LocalDate?,
        dateTill: LocalDate?
    ): List<ExpenseResultDTO> {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun createExpense(dto: ExpenseCreateDTO): ExpenseResultDTO {
        TODO("Not yet implemented")
    }

    @Transactional
    @Retryable(
        retryFor = [OptimisticLockingFailureException::class],
        maxAttemptsExpression = "\${application-props.retry-attempts}",
    )
    override fun updateExpense(expenseId: Long, dto: ExpenseUpdateDTO): ExpenseResultDTO {
        // emit event to the queue since budget overrun may be not relevant after expense update
        TODO("Not yet implemented")
    }

    @Transactional
    override fun deleteExpenseById(expenseId: Long) {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun deleteUserExpenses(userId: Long) {
        logger.debug("Deleting expense records with user id: {}", userId)
        val deletedExpensesNumber = expenseRepository.deleteByUserId(userId)
        logger.debug("{} expense records deleted with user id: {}", deletedExpensesNumber, userId)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ExpenseServiceImpl::class.java)
    }
}
