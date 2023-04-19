package com.github.arhor.aws.microservices.playground.expenses.service.impl

import com.github.arhor.aws.microservices.playground.expenses.Operation
import com.github.arhor.aws.microservices.playground.expenses.data.repository.ExpenseRepository
import com.github.arhor.aws.microservices.playground.expenses.service.ExpenseService
import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseCreateDTO
import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseResultDTO
import com.github.arhor.aws.microservices.playground.expenses.service.dto.ExpenseUpdateDTO
import com.github.arhor.aws.microservices.playground.expenses.service.event.ExpenseEvent
import com.github.arhor.aws.microservices.playground.expenses.service.event.ExpenseEventEmitter
import com.github.arhor.aws.microservices.playground.expenses.service.exception.EntityNotFoundException
import com.github.arhor.aws.microservices.playground.expenses.service.mapper.ExpenseMapper
import org.slf4j.LoggerFactory
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.stream.Stream

@Service
class ExpenseServiceImpl(
    private val expenseMapper: ExpenseMapper,
    private val expenseRepository: ExpenseRepository,
    private val expenseEventEmitter: ExpenseEventEmitter,
) : ExpenseService {

    override fun getExpenseById(expenseId: Long): ExpenseResultDTO {
        return expenseRepository.findByIdOrNull(expenseId)?.let { expenseMapper.mapExpenseToResultDto(it) }
            ?: throw EntityNotFoundException(
                entity = "Expense",
                operation = Operation.READ,
                condition = "id = $expenseId"
            )
    }

    // @Transactional causes Could not write JSON: This ResultSet is closed.
    override fun getExpenses(
        skipUids: List<Long>?,
        dateFrom: LocalDate?,
        dateTill: LocalDate?
    ): Stream<ExpenseResultDTO> {

        return expenseRepository
            .findAllWithinDateRangeSkippingUserIds(
                skipUids = if (skipUids.isNullOrEmpty()) {
                    NO_USERS_TO_SKIP
                } else {
                    skipUids
                },
                dateFrom = dateFrom,
                dateTill = dateTill,
            )
            .map(expenseMapper::mapExpenseToResultDto)
    }

    @Transactional
    override fun createExpense(dto: ExpenseCreateDTO): ExpenseResultDTO {
        return expenseMapper.mapCreateExpenseDtoToEntity(dto)
            .let { expenseRepository.save(it) }
            .let { expenseMapper.mapExpenseToResultDto(it) }
    }

    @Retryable(
        include = [OptimisticLockingFailureException::class],
        maxAttemptsExpression = "\${application-props.retry-attempts:3}",
    )
    @Transactional
    override fun updateExpense(expenseId: Long, updateRequest: ExpenseUpdateDTO): ExpenseResultDTO {
        // emit event to the queue since budget overrun may be not relevant after expense update
        var expense = expenseRepository.findByIdOrNull(expenseId)
            ?: throw EntityNotFoundException(
                entity = "Expense",
                condition = "id = $expenseId",
                operation = Operation.UPDATE,
            )
        var hasChanges = false

        updateRequest.date?.let {
            if (expense.date != it) {
                expense = expense.copy(date = it)
                hasChanges = true
            }
        }
        updateRequest.amount?.let {
            if (expense.amount != it) {
                expense = expense.copy(amount = it)
                hasChanges = true
            }
        }

        if (hasChanges) {
            expense = expenseRepository.save(expense)
            expenseEventEmitter.emit(ExpenseEvent.Updated(expenseId))
        }

        return expenseMapper.mapExpenseToResultDto(expense)
    }

    @Transactional
    override fun deleteExpenseById(expenseId: Long) {
        if (expenseRepository.existsById(expenseId)) {
            expenseRepository.deleteById(expenseId)
            expenseEventEmitter.emit(ExpenseEvent.Deleted(expenseId))
        } else {
            throw EntityNotFoundException(
                entity = "Expense",
                operation = Operation.DELETE,
                condition = "id = $expenseId"
            )
        }
    }

    @Transactional
    override fun deleteUserExpenses(userId: Long) {
        logger.debug("Deleting expense records with user id: {}", userId)
        val deletedExpensesNumber = expenseRepository.deleteByUserIdReturningNumberRecordsAffected(userId)
        logger.debug("{} expense records deleted with user id: {}", deletedExpensesNumber, userId)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ExpenseServiceImpl::class.java)
        private val NO_USERS_TO_SKIP = listOf(-1L)
    }
}
