package com.github.arhor.aws.microservices.playground.users.service.exception

class EntityDuplicateException(entity: String, condition: String) : EntityConditionException(entity, condition)
