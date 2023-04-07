package com.github.arhor.aws.microservices.playground.overruns.data.client.impl;

import java.time.ZonedDateTime;
import java.util.List;

public record ErrorDetails(
    String code,
    String message,
    List<String> details,
    ZonedDateTime timestamp
) {}
