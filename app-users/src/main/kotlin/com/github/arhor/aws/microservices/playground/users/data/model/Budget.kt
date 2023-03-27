package com.github.arhor.aws.microservices.playground.users.data.model

import org.springframework.data.relational.core.mapping.Column
import java.math.BigDecimal

data class Budget(
    @Column("limit")
    val limit: BigDecimal,
)
