package com.github.arhor.aws.microservices.playground.overruns.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.net.http.HttpClient;

@Module
public interface Module_Provider {

    @Provides
    @Singleton
    static ObjectMapper objectMapper() {
        return JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .addModule(new JavaTimeModule())
            .build();
    }

    @Provides
    @Singleton
    static AmazonSNS amazonSNS() {
        return AmazonSNSClientBuilder.defaultClient();
    }

    @Provides
    @Singleton
    static AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.defaultClient();
    }

    @Provides
    @Singleton
    static DynamoDBMapper dynamoDBMapper(final AmazonDynamoDB dynamoDBClient) {
        return new DynamoDBMapper(dynamoDBClient);
    }

    @Provides
    @Singleton
    static HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }
}
