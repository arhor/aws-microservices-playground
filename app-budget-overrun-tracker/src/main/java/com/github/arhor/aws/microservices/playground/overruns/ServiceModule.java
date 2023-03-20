package com.github.arhor.aws.microservices.playground.overruns;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public interface ServiceModule {

    @Provides
    @Singleton
    static ObjectMapper objectMapper() {
        return JsonMapper.builder().build();
    }

    @Provides
    @Singleton
    static AmazonSNS amazonSNS() {
        return AmazonSNSClientBuilder.defaultClient();
    }

    @Provides
    @Singleton
    static AmazonDynamoDB AmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.defaultClient();
    }
}
