package com.github.arhor.aws.microservices.playground.users.service.dto

data class UserUpdateRequestDto(
    val password: String?,
    val budgetLimit: Double?,
)
