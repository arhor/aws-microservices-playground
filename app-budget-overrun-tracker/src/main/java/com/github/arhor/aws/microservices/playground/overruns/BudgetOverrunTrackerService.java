package com.github.arhor.aws.microservices.playground.overruns;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.sns.AmazonSNS;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Singleton
public class BudgetOverrunTrackerService {

    private final AmazonDynamoDB amazonDynamoDB;
    private final AmazonSNS amazonSNS;
    private final HttpClient httpClient;
    private final NotificationWritingConverter notificationWritingConverter;

    @Inject
    public BudgetOverrunTrackerService(
        final AmazonDynamoDB amazonDynamoDB,
        final AmazonSNS amazonSNS,
        final HttpClient httpClient,
        final NotificationWritingConverter notificationWritingConverter
    ) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.amazonSNS = amazonSNS;
        this.httpClient = httpClient;
        this.notificationWritingConverter = notificationWritingConverter;
    }

    public void findOverrunsAndSendNotifications() throws IOException, InterruptedException {
        // 1. remove outdated user records which were notified in previous months
        // 2. get the list of users which were already notified in this month
        // 3. ask expenses service to find users with budget overruns in the current month (excluding users from step 2)
        // 4. publish notification to the SNS
        // 5. save users from the step 3 to the database


        httpClient.send(
            HttpRequest
                .newBuilder()
                .build(),
            HttpResponse.BodyHandlers.ofString()
        );
    }
}
