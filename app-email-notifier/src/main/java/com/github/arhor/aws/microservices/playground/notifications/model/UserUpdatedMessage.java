package com.github.arhor.aws.microservices.playground.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatedMessage {
    private Long userId;
    private String email;
    private double budgetLimit;
}
