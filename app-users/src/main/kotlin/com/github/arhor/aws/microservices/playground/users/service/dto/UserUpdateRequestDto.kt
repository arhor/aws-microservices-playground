package com.github.arhor.aws.microservices.playground.users.service.dto

import java.math.BigDecimal

data class UserUpdateRequestDto(
    val password: String?,
    val budgetLimit: BigDecimal?,
)
