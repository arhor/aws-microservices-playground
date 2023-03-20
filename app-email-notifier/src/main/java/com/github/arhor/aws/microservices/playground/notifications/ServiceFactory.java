package com.github.arhor.aws.microservices.playground.notifications;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = ServiceModule.class)
public interface ServiceFactory {

    SQSMessageProcessorService sqsMessageProcessorService();
}
