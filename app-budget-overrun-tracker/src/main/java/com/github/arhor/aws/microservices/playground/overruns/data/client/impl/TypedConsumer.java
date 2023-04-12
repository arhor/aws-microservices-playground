package com.github.arhor.aws.microservices.playground.overruns.data.client.impl;

import lombok.NonNull;
import lombok.Value;

import java.util.function.Consumer;

@Value
@SuppressWarnings("NullableProblems")
public class TypedConsumer<T> implements Consumer<T> {

    @NonNull Class<T> type;
    @NonNull Consumer<T> delegate;

    @Override
    public void accept(final T value) {
        delegate.accept(value);
    }
}
