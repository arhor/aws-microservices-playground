package com.github.arhor.aws.microservices.playground.notifications;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = SQSMessageProcessorModule.class)
public interface SQSMessageProcessorFactory {

    SQSMessageProcessor sqsMessageProcessor();
}
