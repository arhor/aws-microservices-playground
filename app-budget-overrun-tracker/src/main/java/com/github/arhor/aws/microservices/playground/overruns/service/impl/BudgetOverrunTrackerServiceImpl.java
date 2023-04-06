package com.github.arhor.aws.microservices.playground.overruns.service.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.sns.AmazonSNS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arhor.aws.microservices.playground.overruns.service.BudgetOverrunTrackerService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.http.HttpClient;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
@Singleton
public class BudgetOverrunTrackerServiceImpl implements BudgetOverrunTrackerService {

    private final AmazonDynamoDB amazonDynamoDB;
    private final AmazonSNS amazonSNS;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final DynamoDBMapper dynamoDBMapper;

    @Inject
    public BudgetOverrunTrackerServiceImpl(
        final AmazonDynamoDB amazonDynamoDB,
        final AmazonSNS amazonSNS,
        final HttpClient httpClient,
        final ObjectMapper objectMapper,
        final DynamoDBMapper dynamoDBMapper
    ) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.amazonSNS = amazonSNS;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public void findOverrunsAndSendNotifications() throws IOException, InterruptedException {
        // 1. remove outdated user records which were notified in previous months
        // 2. get the list of users which were already notified in this month
        // 3. ask expenses service to find users with budget overruns in the current month (excluding users from step 2)
        // 4. publish notification to the SNS
        // 5. save users from the step 3 to the database

        amazonDynamoDB.deleteItem(
            new DeleteItemRequest()
                .withTableName("<TABLE_NAME>")
                .withKey(Map.of("date", new AttributeValue().withS(LocalDate.now().toString())))
        );
    }
}
