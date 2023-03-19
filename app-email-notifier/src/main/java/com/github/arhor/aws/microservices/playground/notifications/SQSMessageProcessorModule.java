package com.github.arhor.aws.microservices.playground.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public interface SQSMessageProcessorModule {

    @Provides
    @Singleton
    static ObjectMapper objectMapper() {
        return JsonMapper.builder().build();
    }
}
