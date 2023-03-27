package com.github.arhor.aws.microservices.playground.expenses.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/expenses")
class ExpenseController {

    @GetMapping
    fun getAllExpenses(): List<String> {
        return emptyList()
    }
}
