package com.github.arhor.aws.microservices.playground.users.service.exception

abstract class EntityConditionException(val entity: String, val condition: String) : RuntimeException()
