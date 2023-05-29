package com.github.arhor.aws.microservices.playground.users.data

import java.math.BigDecimal

sealed interface UserStateChangedMessage {

    data class Updated(val userId: Long, val email: String, val budgetLimit: BigDecimal) : UserStateChangedMessage

    data class Deleted(val userId: Long) : UserStateChangedMessage
}
