package com.github.arhor.aws.microservices.playground.notifications.config;

import com.github.arhor.aws.microservices.playground.notifications.service.SQSMessageProcessor;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(
    modules = {
        Module_Bindings.class,
        Module_Provider.class,
    }
)
public interface ServiceFactory {

    SQSMessageProcessor sqsMessageProcessorService();
}
