package com.github.arhor.aws.microservices.playground.notifications.service;

public interface UserEmailSender {

    void sendOverrunNotification(String email, double limit, double value);
}
