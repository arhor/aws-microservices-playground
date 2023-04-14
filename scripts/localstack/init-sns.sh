#!/bin/bash
echo "$(awslocal sns create-topic --name user-deleted-events-topic)"
