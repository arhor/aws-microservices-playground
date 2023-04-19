package com.github.arhor.aws.microservices.playground.expenses.service.exception

/**
 * @param entity    entity name
 * @param condition condition caused the exception
 * @param operation operation during which an exception occurred
 */
abstract class EntityConditionException(val entity: String, val condition: String, val operation: String?) :
    RuntimeException(
        if (operation != null) {
            "Cannot execute operation [$operation] over the entity [$entity] under the condition [$condition]"
        } else {
            "Cannot execute operation over the entity [$entity] under the condition [$condition]"
        }
    )
