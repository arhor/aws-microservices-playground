package com.github.arhor.aws.microservices.playground.overruns.data.client.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.spy;

@Slf4j
class JsonStreamParserTest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Element {
        private String name;
        private List<String> data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Attachment {
        private int code;
        private Map<String, String> params;
    }

    private static final String JSON = new StringJoiner("\n")
        .add("[")
        .add("    {")
        .add("        \"name\": \"test-object-1\",")
        .add("        \"data\": [\"line-1\"]")
        .add("    },")
        .add("    {")
        .add("        \"name\": \"test-object-2\",")
        .add("        \"data\": [\"line-2\"]")
        .add("    },")
        .add("    {")
        .add("        \"name\": \"test-object-3\",")
        .add("        \"data\": [\"line-3\"]")
        .add("    }")
        .add("]{")
        .add("     \"code\": 12345,")
        .add("     \"params\": {")
        .add("         \"param-1\": \"value-1\",")
        .add("         \"param-2\": \"value-2\"")
        .add("     }")
        .add("}")
        .toString();

    private final JsonStreamParser streamParser = new JsonStreamParser(
        JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build()
    );

    @Test
    @SuppressWarnings({ "Convert2Lambda" })
    void should_correctly_parse_streaming_json_accepting_each_object_once_its_ready() throws IOException {
        // Given
        final var inputStream = new PipedInputStream();
        final var outputStream = new PipedOutputStream(inputStream);

        final var elementConsumer = spy(new Consumer<Element>() {
            @Override
            public void accept(final Element element) {
                log.info("Consumed element: {}", element);
            }
        });
        final var attachmentConsumer = spy(new Consumer<Attachment>() {
            @Override
            public void accept(final Attachment attachment) {
                log.info("Consumed attachment: {}", attachment);
            }
        });

        // When
        CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS).execute(() -> {
            final var lines = JSON.lines().collect(toList());

            for (int i = 0, size = lines.size(); i < size; i++) {
                writeLineToStream(lines.get(i), outputStream);
                log.info("{} of {} has been written to output stream", i + 1, size);
                delay100Millis();
            }
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

    private void delay100Millis() {
        try {
            Thread.sleep(100);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
