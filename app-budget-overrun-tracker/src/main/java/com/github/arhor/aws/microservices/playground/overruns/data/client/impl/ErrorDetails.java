package com.github.arhor.aws.microservices.playground.overruns.data.client.impl;

import lombok.Value;

import java.time.ZonedDateTime;
import java.util.List;

@Value
public class ErrorDetails {
    String code;
    String message;
    List<String> details;
    ZonedDateTime timestamp;
}
