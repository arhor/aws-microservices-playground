package com.github.arhor.aws.microservices.playground.notifications.config;

import com.github.arhor.aws.microservices.playground.notifications.service.SQSMessageProcessorService;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(
    modules = {
        Module_Provider.class,
        Module_Bindings.class,
    }
)
public interface ServiceFactory {

    SQSMessageProcessorService sqsMessageProcessorService();
}
