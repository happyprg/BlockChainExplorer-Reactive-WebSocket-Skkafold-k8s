#!/bin/bash
set -x
gradle clean
gradle assemble
docker rmi -f hongsgo/blockexplorer:latest
docker build -t hongsgo/blockexplorer:latest api/
docker push hongsgo/blockexplorer:latest
kubectl  delete -l run=blockexplorer
