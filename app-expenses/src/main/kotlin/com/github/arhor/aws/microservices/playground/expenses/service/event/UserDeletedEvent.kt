package com.github.arhor.aws.microservices.playground.expenses.service.event

data class UserDeletedEvent(
    /**
     * Identifier of the deleted user.
     */
    val userId: Long,
)
