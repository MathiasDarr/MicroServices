#!/bin/bash
docker exec kafka bin/kafka-console-consumer.sh --topic $1 --from-beginning --property print.key=true --bootstrap-server kafka:9092
