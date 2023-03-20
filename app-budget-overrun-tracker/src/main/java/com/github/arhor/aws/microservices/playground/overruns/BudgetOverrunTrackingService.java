package com.github.arhor.aws.microservices.playground.overruns;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.sns.AmazonSNS;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BudgetOverrunTrackingService {

    private final AmazonDynamoDB amazonDynamoDB;
    private final AmazonSNS amazonSNS;
    private final NotificationWritingConverter notificationWritingConverter;

    @Inject
    public BudgetOverrunTrackingService(
        final AmazonDynamoDB amazonDynamoDB,
        final AmazonSNS amazonSNS,
        final NotificationWritingConverter notificationWritingConverter
    ) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.amazonSNS = amazonSNS;
        this.notificationWritingConverter = notificationWritingConverter;
    }

    public void findOverrunsAndSendNotifications() {
        // 1. remove outdated user records which were notified in previous months
        // 2. get the list of users which were already notified in this month
        // 3. ask expenses service to find users with budget overruns in the current month (excluding users from step 2)
        // 4. publish notification to the SNS
        // 5. save users from the step 3 to the database
    }
}
