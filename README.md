[![Video Label](http://img.youtube.com/vi/OSQTZimCY9M/0.jpg)](https://www.youtube.com/watch?v=OSQTZimCY9M?t=0s)


![screen.png](./docs/images/screen.png?raw=true)
![flow.png](./docs/images/flow.png?raw=true)

# Docker build and run
docker build -t hongsgo/blockchain-monitor:latest api/
docker run -p8080:8080  hongsgo/blockchain-monitor:latest
open "http://127.0.0.1:8080/?s=http://host.docker.internal:9000" //Mac

# You can connect between deploy/pod which located on K8S and your localhost
```
$ kubectl port-forward deploy/blockchain-monitor-dev :8080
Forwarding from 127.0.0.1:53287 -> 8080
Forwarding from [::1]:53287 -> 8080
Handling connection for 53287
Handling connection for 53287
$ open 'http://localhost:53287'
```
127.0.0.1:53287 
# Continuous Delivery to Kubernetes with Jib, Skaffold,Spring boot2,Kotlin 
```bash
skaffold dev
./gradlew jibDockerBuild && docker run --rm -p 8080:8080 api:0.0.1-SNAPSHOT
# docker run with specific node hosts
./gradlew jibDockerBuild --image=blockchain-monitor && docker run --rm -p 8080:8080 blockchain-monitor -e --chain.snodeHost=https://wallet.icon.foundation -e --chain.cnodeHost=https://wallet.icon.foundation
open "http://localhost:8080"
open "http://localhost:8080?s=https://wallet.icon.foundation&c=https://wallet.icon.foundation&e=3&b=10&t=10"
```
# You can change c/node hosts freely which want to monitoring by changing param.
```
http://localhost:8080/?s=https://wallet.icon.foundation&c=https://wallet.icon.foundation&e=3&b=10&t=10
sNode host --> c=blah
sNode host --> s=blah
```
# You can change the display items of each table by changing the param. 
```
tableErrorInfo --> e=1, 
tableLastBlock --> b=10
tableTx  --> t=10
```

# Setup
* Install gradle 
```bash
./gradlew build
```
# Recipe
* https://github.com/json-path/JsonPath
* https://github.com/kittinunf/fuel/blob/master/fuel/README.md
* Mustache
