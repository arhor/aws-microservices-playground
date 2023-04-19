package com.github.arhor.aws.microservices.playground.users.service.dto

import java.math.BigDecimal
import javax.validation.constraints.Digits
import javax.validation.constraints.Pattern
import javax.validation.constraints.PositiveOrZero

data class UserUpdateRequestDto(

    @Pattern(
        /* language=REGEXP */
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,20}\$"
    )
    val password: String?,

    @Digits(integer = 10, fraction = 2)
    @PositiveOrZero
    val budgetLimit: BigDecimal?,
)
