package com.github.arhor.aws.microservices.playground.notifications.service;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import java.io.IOException;

public interface SQSMessageProcessorService {

    void process(SQSEvent.SQSMessage message) throws IOException, InterruptedException;
}
