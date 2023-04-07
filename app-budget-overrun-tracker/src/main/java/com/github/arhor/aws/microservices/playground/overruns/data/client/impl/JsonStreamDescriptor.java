package com.github.arhor.aws.microservices.playground.overruns.data.client.impl;

import java.io.InputStream;
import java.util.function.Consumer;

public record JsonStreamDescriptor<T, R>(
    InputStream source,
    Class<T> elementType,
    Consumer<T> elementConsumer,
    Class<R> attachmentType,
    Consumer<R> attachmentConsumer
) {

    public JsonStreamDescriptor(
        final InputStream source,
        final Class<T> elementType,
        final Consumer<T> elementConsumer
    ) {
        this(source, elementType, elementConsumer, null, null);
    }
}
