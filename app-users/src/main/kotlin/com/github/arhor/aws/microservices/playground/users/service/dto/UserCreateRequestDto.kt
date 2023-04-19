package com.github.arhor.aws.microservices.playground.users.service.dto

import com.github.arhor.aws.microservices.playground.users.DEFAULT_BUDGET_LIMIT
import java.math.BigDecimal
import javax.validation.constraints.Email
import javax.validation.constraints.Pattern
import javax.validation.constraints.PositiveOrZero

data class UserCreateRequestDto(


    @Email(
        /* language=REGEXP */
        regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
    )
    val email: String,

    @Pattern(
        /* language=REGEXP */
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,20}\$"
    )
    val password: String,

    @PositiveOrZero
    val budgetLimit: BigDecimal = DEFAULT_BUDGET_LIMIT,
)
