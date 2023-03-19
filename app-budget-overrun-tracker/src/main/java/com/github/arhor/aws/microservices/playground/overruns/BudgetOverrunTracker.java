package com.github.arhor.aws.microservices.playground.overruns;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

@SuppressWarnings("unused")
public class BudgetOverrunTracker implements RequestHandler<ScheduledEvent, Void> {

    @Override
    public Void handleRequest(final ScheduledEvent input, final Context context) {
        final var logger = context.getLogger();
        final var amazonSNS = AmazonSNSClientBuilder.defaultClient();
        final var amazonDDB = AmazonDynamoDBClientBuilder.defaultClient();

        // 1. remove outdated user records which were notified in previous months
        // 2. get the list of users which were already notified in this month
        // 3. ask expenses service to find users with budget overruns in the current month (excluding users from step 2)
        // 4. publish notification to the SNS
        // 5. save users from the step 3 to the database

        logger.log("Received a request to the AWS lambda function with the following payload: " + input);

        return null;
    }
}
