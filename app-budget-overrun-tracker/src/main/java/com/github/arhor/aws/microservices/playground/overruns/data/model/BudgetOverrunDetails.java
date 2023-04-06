package com.github.arhor.aws.microservices.playground.overruns.data.model;

import java.math.BigDecimal;

public record BudgetOverrunDetails(Long userId, BigDecimal amount) {}
