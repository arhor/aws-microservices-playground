package com.github.arhor.aws.microservices.playground.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.net.http.HttpClient;

@Module
public interface ServiceModule {

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
}
