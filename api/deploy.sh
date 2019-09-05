#!/bin/bash
echo "Clean and Build"
gradle clean
gradle build
echo "Deploy to : {DockerHub}/$1"
docker build . -t {DockerHub}/$1
docker push  {DockerHub}/$1
kubectl set image {K8S_DeployName} {K8S_Container_Name}-alpha={DockerHub}/$1
kubectl set image {K8S_DeployName} {K8S_Container_Name}-beta={DockerHub}/$1
kubectl set image {K8S_DeployName} {K8S_Container_Name}-release={DockerHub}/$1
kubectl get po -w


