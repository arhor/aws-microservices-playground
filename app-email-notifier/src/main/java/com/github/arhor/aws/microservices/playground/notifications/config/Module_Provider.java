package com.github.arhor.aws.microservices.playground.notifications.config;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.net.http.HttpClient;

/**
 * Declares provider methods, which return instantiated and preconfigured objects.
 */
@Module
interface Module_Provider {

    @Provides
    @Singleton
    static ObjectMapper objectMapper() {
        return JsonMapper.builder().build();
    }

    @Provides
    @Singleton
    static HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }

    @Provides
    @Singleton
    static AmazonSimpleEmailService amazonSimpleEmailService() {
        return AmazonSimpleEmailServiceClientBuilder.defaultClient();
    }
}
