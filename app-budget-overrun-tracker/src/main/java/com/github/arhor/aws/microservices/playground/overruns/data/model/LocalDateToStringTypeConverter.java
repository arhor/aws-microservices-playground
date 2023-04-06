package com.github.arhor.aws.microservices.playground.overruns.data.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDate;

public class LocalDateToStringTypeConverter implements DynamoDBTypeConverter<String, LocalDate> {

    @Override
    public String convert(final LocalDate date) {
        return date.toString();
    }

    @Override
    public LocalDate unconvert(final String data) {
        return LocalDate.parse(data);
    }
}
