package com.github.arhor.aws.microservices.playground.notifications.service.impl;

import com.github.arhor.aws.microservices.playground.notifications.service.StringInterpolator;
import org.apache.commons.text.StringSubstitutor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class StringInterpolatorImpl implements StringInterpolator {

    @Inject
    public StringInterpolatorImpl() {}

    @Override
    public String interpolate(final String template, final Map<String, String> mappings) {
        return createSubstitutor(mappings).replace(template);
    }

    private StringSubstitutor createSubstitutor(final Map<String, String> mappings) {
        return new StringSubstitutor(mappings)
            .setEnableUndefinedVariableException(true)
            .setDisableSubstitutionInValues(true);
    }
}
