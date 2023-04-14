package com.github.arhor.aws.microservices.playground.overruns.data.client.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
    private String code;
    private String message;
    private List<String> details;
    private ZonedDateTime timestamp;
}
