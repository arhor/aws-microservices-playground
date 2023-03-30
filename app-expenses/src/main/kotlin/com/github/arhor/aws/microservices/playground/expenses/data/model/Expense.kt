package com.github.arhor.aws.microservices.playground.expenses.data.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Immutable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDate

@Immutable
@Table("expenses")
data class Expense(
    @Id
    @Column("id")
    val id: Long? = null,

    @Column("date")
    val date: LocalDate,

    @Column("amount")
    val amount: BigDecimal,

    @Column("user_id")
    val userId: Long,
)
