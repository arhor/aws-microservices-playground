package com.github.arhor.aws.microservices.playground.notifications.service.impl;

import com.github.arhor.aws.microservices.playground.notifications.service.StringInterpolator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class StringInterpolatorImpl implements StringInterpolator {

    @Override
    public String interpolate(final String template, final Map<String, ?> mappings) {
        final var substitutor =
            new StringSubstitutor(mappings)
                .setEnableUndefinedVariableException(true)
                .setDisableSubstitutionInValues(true);

        return substitutor.replace(template);
    }
}
