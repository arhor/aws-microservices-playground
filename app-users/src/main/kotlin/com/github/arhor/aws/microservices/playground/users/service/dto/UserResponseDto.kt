package com.github.arhor.aws.microservices.playground.users.service.dto

data class UserResponseDto(
    val id: Long,
    val email: String,
    val budgetLimit: Double,
)
