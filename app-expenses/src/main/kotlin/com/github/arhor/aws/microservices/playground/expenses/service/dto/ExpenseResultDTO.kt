package com.github.arhor.aws.microservices.playground.expenses.service.dto

import java.math.BigDecimal
import java.time.LocalDate

data class ExpenseResultDTO(
    val id: Long,
    val date: LocalDate,
    val userId: Long,
    val amount: BigDecimal,
)
