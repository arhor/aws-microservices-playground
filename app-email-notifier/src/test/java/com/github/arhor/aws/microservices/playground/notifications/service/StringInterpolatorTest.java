package com.github.arhor.aws.microservices.playground.notifications.service;

import com.github.arhor.aws.microservices.playground.notifications.service.impl.StringInterpolatorImpl;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringInterpolatorTest {

    private final StringInterpolator stringInterpolator = new StringInterpolatorImpl();

    @Test
    void should_inject_expected_value_into_template_string() {
        // Given
        final var template = "Test template with injection: ${slot}";
        final var argName = "slot";
        final var argValue = "injected value";
        final var args = Map.of(argName, argValue);

        // When
        final var result = stringInterpolator.interpolate(template, args);

        // Then
        assertThat(result)
            .isEqualTo("Test template with injection: " + argValue);
    }

    @Test
    void should_throw_an_exception_interpolating_template_with_arg_that_is_not_provided() {
        // Given
        final var template = "Test template with injection: ${slot}";
        final var args = Collections.<String, String>emptyMap();
        final var arg = "slot";

        // When
        final var action = (ThrowingCallable) () -> {
            stringInterpolator.interpolate(template, args);
        };

        // Then
        assertThatThrownBy(action)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("'" + arg + "'");
    }
}
