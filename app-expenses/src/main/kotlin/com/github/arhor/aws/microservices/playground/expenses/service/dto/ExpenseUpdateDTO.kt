package com.github.arhor.aws.microservices.playground.expenses.service.dto

import java.math.BigDecimal
import java.time.LocalDate

data class ExpenseUpdateDTO(
    val date: LocalDate?,
    val amount: BigDecimal?,
)
