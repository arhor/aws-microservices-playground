package com.github.arhor.aws.microservices.playground.users.service.event

interface UserEventEmitter {

    fun emit(event: UserEvent)
}
