package com.github.arhor.aws.microservices.playground.overruns.data.client.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.spy;

class JsonStreamParserTest {

    private record Element(String name, List<String> data) {}

    private record Attachment(int code, Map<String, String> params) {}

    private static final String JSON = """
        [
            {
                "name": "test-object-1",
                "data": ["line-1"]
            },
            {
                "name": "test-object-2",
                "data": ["line-2"]
            },
            {
                "name": "test-object-3",
                "data": ["line-3"]
            }
        ]{
             "code": 12345,
             "params": {
                 "param-1": "value-1",
                 "param-2": "value-2"
             }
         }
        """;

    private final JsonStreamParser streamParser = new JsonStreamParser(
        JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .addModule(new JavaTimeModule())
            .build()
    );

    @Test
    void should_correctly_parse_streaming_json_accepting_each_object_once_its_ready() throws IOException {
        // Given
        final var inputStream = new PipedInputStream();
        final var outputStream = new PipedOutputStream(inputStream);

        final var elementConsumer = spy(new Consumer<Element>() {
            @Override
            public void accept(final Element object) {}
        });
        final var attachmentConsumer = spy(new Consumer<Attachment>() {
            @Override
            public void accept(final Attachment object) {}
        });

        // When
        CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS).execute(() -> {
            JSON.lines().forEach(line -> {
                writeLineToStream(line, outputStream);
                delay(100);
            });
        });

        streamParser.parse(
            inputStream,
            new TypedConsumer<>(Element.class, elementConsumer),
            new TypedConsumer<>(Attachment.class, attachmentConsumer)
        );

        // Then
        then(elementConsumer)
            .should()
            .accept(new Element("test-object-1", List.of("line-1")));

        then(elementConsumer)
            .should()
            .accept(new Element("test-object-2", List.of("line-2")));

        then(elementConsumer)
            .should()
            .accept(new Element("test-object-3", List.of("line-3")));

        then(elementConsumer)
            .shouldHaveNoMoreInteractions();

        then(attachmentConsumer)
            .should()
            .accept(new Attachment(12345, Map.of("param-1", "value-1", "param-2", "value-2")));

        then(attachmentConsumer)
            .shouldHaveNoMoreInteractions();
    }

    private void writeLineToStream(final String line, final OutputStream stream) {
        try {
            stream.write(line.getBytes());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void delay(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
