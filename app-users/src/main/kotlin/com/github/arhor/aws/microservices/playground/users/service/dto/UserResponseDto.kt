package com.github.arhor.aws.microservices.playground.users.service.dto

import java.math.BigDecimal

data class UserResponseDto(
    val id: Long,
    val email: String,
    val budgetLimit: BigDecimal,
)
