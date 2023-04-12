package com.github.arhor.aws.microservices.playground.overruns.data.client.impl;

import java.util.Objects;
import java.util.function.Consumer;

public record TypedConsumer<T>(Class<T> type, Consumer<T> delegate) implements Consumer<T> {

    public TypedConsumer {
        Objects.requireNonNull(type, "'type' cannot be null");
        Objects.requireNonNull(delegate, "'delegate' cannot be null");
    }

    @Override
    public void accept(final T value) {
        delegate.accept(value);
    }
}
