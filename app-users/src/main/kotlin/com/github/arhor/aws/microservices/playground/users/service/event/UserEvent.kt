package com.github.arhor.aws.microservices.playground.users.service.event

sealed interface UserEvent {

    data class Deleted(val userId: Long) : UserEvent
}
