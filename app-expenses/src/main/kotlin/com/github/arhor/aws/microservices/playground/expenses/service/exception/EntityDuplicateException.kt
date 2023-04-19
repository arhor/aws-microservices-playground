package com.github.arhor.aws.microservices.playground.expenses.service.exception

import com.github.arhor.aws.microservices.playground.expenses.Operation

class EntityDuplicateException(entity: String, condition: String, operation: String? = null) : EntityConditionException(
    entity,
    condition,
    operation,
) {
    constructor(entity: String, condition: String, operation: Operation) : this(entity, condition, operation.name)
}
