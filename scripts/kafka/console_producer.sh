#!/bin/bash
docker exec -it kafka bash bin/kafka-console-producer.sh --topic $1 --broker-list kafka:9092
