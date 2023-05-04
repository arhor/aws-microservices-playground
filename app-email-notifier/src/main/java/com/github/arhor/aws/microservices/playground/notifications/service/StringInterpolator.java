package com.github.arhor.aws.microservices.playground.notifications.service;

import java.util.Map;

public interface StringInterpolator {
    String interpolate(String template, Map<String, ?> mappings);
}
