package com.github.arhor.aws.microservices.playground.notifications;

import org.apache.commons.text.StringSubstitutor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class StringInterpolator {

    @Inject
    public StringInterpolator() {}

    public String interpolate(final String template, final Map<String, String> mappings) {
        return StringSubstitutor.replace(template, mappings);
    }
}
