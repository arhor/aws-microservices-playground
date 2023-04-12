package com.github.arhor.aws.microservices.playground.overruns.data.client.impl;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.InputStream;

@Slf4j
@Singleton
public class JsonStreamParser {

    private final ObjectMapper objectMapper;

    @Inject
    public JsonStreamParser(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <E> void parse(final InputStream stream, final TypedConsumer<E> elementConsumer) {
        parse(stream, elementConsumer, null);
    }

    @SneakyThrows
    public <E, A> void parse(
        final InputStream stream,
        final TypedConsumer<E> elementConsumer,
        final TypedConsumer<A> attachmentConsumer
    ) {
        log.debug("Parsing json data stream...");

        try (final var parser = objectMapper.getFactory().createParser(stream)) {

            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected content to be an array");
            }

            final var elementType = elementConsumer.type();

            while (parser.nextToken() != JsonToken.END_ARRAY) {
                final var currentElement = objectMapper.readValue(parser, elementType);
                elementConsumer.accept(currentElement);
            }

            if (parser.nextToken() == JsonToken.START_OBJECT) {
                if (attachmentConsumer != null) {
                    final var attachment = objectMapper.readValue(parser, attachmentConsumer.type());
                    attachmentConsumer.accept(attachment);
                } else {
                    log.debug("Attachment found at the stream end, but no consumer provided");
                }
            }
        }
        log.debug("Parsing json data stream finished successfully");
    }
}
