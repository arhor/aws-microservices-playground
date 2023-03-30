package com.github.arhor.aws.microservices.playground.expenses.service.dto

import java.math.BigDecimal
import java.time.LocalDate

data class ExpenseCreateDTO(
    val date: LocalDate,
    val userId: Long,
    val amount: BigDecimal,
)
