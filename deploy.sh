#!/bin/bash
set -x
gradle clean
gradle assemble
docker rmi -f hongsgo/blockchain-monitor:latest
docker build -t hongsgo/blockchain-monitor:latest api/
docker push hongsgo/blockchain-monitor:latest
skaffold run
