package com.github.arhor.aws.microservices.playground.expenses.web.listener

data class UserDeletedEvent(
    /**
     * Identifier of the deleted user.
     */
    val userId: Long,
)
