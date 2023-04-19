#!/bin/bash

########################################## all SNS topic declarations ##################################################

awslocal sns create-topic --name user-updated-events-topic --output table | cat
awslocal sns create-topic --name user-deleted-events-topic --output table | cat

awslocal sns create-topic --name expense-updated-events-topic --output table | cat
awslocal sns create-topic --name expense-deleted-events-topic --output table | cat

########################################## all SQS queue declarations ##################################################

# Supposed consumer: app-expenses
awslocal sqs create-queue --queue-name user-updated-events-queue-1 --output table | cat

# Supposed consumer: app-budget-overrun-tracker
awslocal sqs create-queue --queue-name user-updated-events-queue-2 --output table | cat

# Supposed consumer: app-expenses
awslocal sqs create-queue --queue-name user-deleted-events-queue-1 --output table | cat

# Supposed consumer: app-budget-overrun-tracker
awslocal sqs create-queue --queue-name user-deleted-events-queue-2 --output table | cat

###################################### user-updated-events-topic subscriptions #########################################

awslocal sns subscribe \
    --topic-arn "arn:aws:sns:us-east-1:000000000000:user-updated-events-topic" \
    --protocol sqs \
    --notification-endpoint "arn:aws:sqs:us-east-1:000000000000:user-updated-events-queue-1" \
    --output table | cat

awslocal sns subscribe \
    --topic-arn "arn:aws:sns:us-east-1:000000000000:user-updated-events-topic" \
    --protocol sqs \
    --notification-endpoint "arn:aws:sqs:us-east-1:000000000000:user-updated-events-queue-2" \
    --output table | cat

###################################### user-deleted-events-topic subscriptions #########################################

awslocal sns subscribe \
    --topic-arn "arn:aws:sns:us-east-1:000000000000:user-deleted-events-topic" \
    --protocol sqs \
    --notification-endpoint "arn:aws:sqs:us-east-1:000000000000:user-deleted-events-queue-1" \
    --output table | cat

awslocal sns subscribe \
    --topic-arn "arn:aws:sns:us-east-1:000000000000:user-deleted-events-topic" \
    --protocol sqs \
    --notification-endpoint "arn:aws:sqs:us-east-1:000000000000:user-deleted-events-queue-2" \
    --output table | cat
