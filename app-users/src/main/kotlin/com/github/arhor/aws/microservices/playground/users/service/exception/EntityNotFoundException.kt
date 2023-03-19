package com.github.arhor.aws.microservices.playground.users.service.exception

class EntityNotFoundException(entity: String, condition: String) : EntityConditionException(entity, condition)
