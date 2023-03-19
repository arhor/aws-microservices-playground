package com.github.arhor.aws.microservices.playground.users.service.exception

abstract class EntityConditionException(entity: String, condition: String) : RuntimeException()
