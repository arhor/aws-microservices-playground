package com.github.arhor.aws.microservices.playground.notifications.service;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;

public interface SQSMessageProcessor {

    void process(SQSEvent.SQSMessage message);
}
