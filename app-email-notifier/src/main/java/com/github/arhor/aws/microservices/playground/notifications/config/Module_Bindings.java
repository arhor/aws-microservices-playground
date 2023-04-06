package com.github.arhor.aws.microservices.playground.notifications.config;

import com.github.arhor.aws.microservices.playground.notifications.service.SQSMessageProcessorService;
import com.github.arhor.aws.microservices.playground.notifications.service.StringInterpolator;
import com.github.arhor.aws.microservices.playground.notifications.service.UserEmailSender;
import com.github.arhor.aws.microservices.playground.notifications.service.UsersApiClient;
import com.github.arhor.aws.microservices.playground.notifications.service.impl.SQSMessageProcessorServiceImpl;
import com.github.arhor.aws.microservices.playground.notifications.service.impl.StringInterpolatorImpl;
import com.github.arhor.aws.microservices.playground.notifications.service.impl.UserEmailSenderImpl;
import com.github.arhor.aws.microservices.playground.notifications.service.impl.UsersApiClientImpl;
import dagger.Binds;
import dagger.Module;

/**
 * Declares bindings between interfaces/abstract classes and concrete implementations.
 */
@Module
interface Module_Bindings {

    @Binds
    SQSMessageProcessorService sqsMessageProcessorService(SQSMessageProcessorServiceImpl impl);

    @Binds
    StringInterpolator stringInterpolator(StringInterpolatorImpl impl);

    @Binds
    UsersApiClient usersApiClient(UsersApiClientImpl impl);

    @Binds
    UserEmailSender userEmailSender(UserEmailSenderImpl impl);
}
