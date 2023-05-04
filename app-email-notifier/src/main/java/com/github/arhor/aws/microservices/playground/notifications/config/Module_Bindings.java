package com.github.arhor.aws.microservices.playground.notifications.config;

import com.github.arhor.aws.microservices.playground.notifications.repository.UserRepository;
import com.github.arhor.aws.microservices.playground.notifications.repository.impl.UserRepositoryImpl;
import com.github.arhor.aws.microservices.playground.notifications.service.MessageHandler;
import com.github.arhor.aws.microservices.playground.notifications.service.SQSMessageProcessor;
import com.github.arhor.aws.microservices.playground.notifications.service.StringInterpolator;
import com.github.arhor.aws.microservices.playground.notifications.service.UserEmailSender;
import com.github.arhor.aws.microservices.playground.notifications.service.impl.BudgetOverrunMessageHandler;
import com.github.arhor.aws.microservices.playground.notifications.service.impl.SQSMessageProcessorImpl;
import com.github.arhor.aws.microservices.playground.notifications.service.impl.StringInterpolatorImpl;
import com.github.arhor.aws.microservices.playground.notifications.service.impl.UserDeletedMessageHandler;
import com.github.arhor.aws.microservices.playground.notifications.service.impl.UserEmailSenderImpl;
import com.github.arhor.aws.microservices.playground.notifications.service.impl.UserUpdatedMessageHandler;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

/**
 * Declares bindings between interfaces/abstract classes and concrete implementations.
 */
@Module
interface Module_Bindings {

    @Binds
    SQSMessageProcessor sqsMessageProcessorService(SQSMessageProcessorImpl impl);

    @Binds
    StringInterpolator stringInterpolator(StringInterpolatorImpl impl);

    @Binds
    UserRepository userRepository(UserRepositoryImpl impl);

    @Binds
    UserEmailSender userEmailSender(UserEmailSenderImpl impl);

    @Binds
    @IntoMap
    @StringKey("UserStateChangedMessage.Updated")
    MessageHandler userUpdatedMessageHandler(UserUpdatedMessageHandler impl);

    @Binds
    @IntoMap
    @StringKey("UserStateChangedMessage.Deleted")
    MessageHandler userDeletedMessageHandler(UserDeletedMessageHandler impl);

    @Binds
    @IntoMap
    @StringKey("BudgetOverrunMessage")
    MessageHandler budgetOverrunMessageHandler(BudgetOverrunMessageHandler impl);
}
