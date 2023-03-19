package com.github.arhor.aws.microservices.playground.users.service.dto

import com.github.arhor.aws.microservices.playground.users.DEFAULT_BUDGET_LIMIT

data class UserCreateRequestDto(
    val email: String,
    val password: String,
    val budgetLimit: Double = DEFAULT_BUDGET_LIMIT,
)
