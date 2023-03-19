package com.github.arhor.aws.microservices.playground.notifications;

import java.io.IOException;

public interface NotificationMapper {

    Notification convert(String data) throws IOException;
}
