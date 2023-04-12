package com.github.arhor.aws.microservices.playground.overruns.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetOverrunDetails {
    private Long userId;
    private BigDecimal amount;
}
