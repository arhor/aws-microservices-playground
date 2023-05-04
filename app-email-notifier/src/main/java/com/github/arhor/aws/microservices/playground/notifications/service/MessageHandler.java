package com.github.arhor.aws.microservices.playground.notifications.service;

public interface MessageHandler {

    void handle(String body);
}
