#!/bin/bash
aws dynamodb scan --table-name $1 --endpoint-url http://localhost:8000
