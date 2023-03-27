package com.github.arhor.aws.microservices.playground.users.service.dto

import com.github.arhor.aws.microservices.playground.users.DEFAULT_BUDGET_LIMIT
import java.math.BigDecimal

data class UserCreateRequestDto(
    val email: String,
    val password: String,
    val budgetLimit: BigDecimal = DEFAULT_BUDGET_LIMIT,
)
