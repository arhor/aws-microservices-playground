package com.github.arhor.aws.microservices.playground.overruns.data.client.impl;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class JsonStreamParser {

    private final ObjectMapper objectMapper;

    @Inject
    public JsonStreamParser(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    public <T, R> void parse(final JsonStreamDescriptor<T, R> jsonStream) {
        log.debug("Parsing json data stream...");

        try (final var parser = objectMapper.getFactory().createParser(jsonStream.source())) {

            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected content to be an array");
            }

            final var elementType = jsonStream.elementType();
            final var elementConsumer = jsonStream.elementConsumer();

            while (parser.nextToken() != JsonToken.END_ARRAY) {
                final var currentElement = objectMapper.readValue(parser, elementType);
                elementConsumer.accept(currentElement);
            }

            if (parser.nextToken() == JsonToken.START_OBJECT) {
                final var attachmentType = jsonStream.attachmentType();
                final var attachmentConsumer = jsonStream.attachmentConsumer();

                if (attachmentType != null && attachmentConsumer != null) {
                    final var attachment = objectMapper.readValue(parser, attachmentType);
                    attachmentConsumer.accept(attachment);
                } else {
                    log.debug(
                        "Attachment found at the end of stream, but no type or consumer provided: type={}, consumer={}",
                        attachmentType,
                        attachmentConsumer
                    );
                }
            }
        }
        log.debug("Parsing json data stream finished successfully");
    }
}
