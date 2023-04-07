package com.github.arhor.aws.microservices.playground.overruns.data.client.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

class JsonStreamParserTest {

    private record TestRecord_1(String name, LocalDate date) {}

    private record TestRecord_2(String name, List<String> data) {}

    private static final String JSON = """
        [
            {
                "name": "test-object-1",
                "data": [
                    "line-1",
                    "line-2",
                    "line-3"
                ]
            },
            {
                "name": "test-object-2",
                "data": [
                    "line-1",
                    "line-2",
                    "line-3"
                ]
            },
            {
                "name": "test-object-3",
                "data": [
                    "line-1",
                    "line-2",
                    "line-3"
                ]
            }
        ]{
             "code": "GEN-00000",
             "message": "Internal Server Error. Please, contact system administrator.",
             "details": [],
             "timestamp": "2023-04-07T12:32:34.572Z"
         }
        """;

    private final JsonStreamParser streamParser = new JsonStreamParser(
        JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .addModule(new JavaTimeModule())
            .build()
    );

    @Test
    void should_pass() {
        streamParser.parse(
            new JsonStreamDescriptor<>(
                new ByteArrayInputStream(JSON.getBytes()),
                TestRecord_2.class,
                System.out::println
            )
        );
    }
}
