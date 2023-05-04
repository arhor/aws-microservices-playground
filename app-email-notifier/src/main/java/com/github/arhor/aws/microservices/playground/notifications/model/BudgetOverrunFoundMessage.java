package com.github.arhor.aws.microservices.playground.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetOverrunFoundMessage {
    private long userId;
    private LocalDate date;
    private double amount;
}
