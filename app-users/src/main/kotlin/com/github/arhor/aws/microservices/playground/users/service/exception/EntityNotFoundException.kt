package com.github.arhor.aws.microservices.playground.users.service.exception

import com.github.arhor.aws.microservices.playground.users.Operation

class EntityNotFoundException(entity: String, condition: String, operation: String? = null) : EntityConditionException(
    entity,
    condition,
    operation
) {
    constructor(entity: String, condition: String, operation: Operation) : this(entity, condition, operation.name)
}
