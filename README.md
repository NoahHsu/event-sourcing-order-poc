# Event Sourcing Order P.O.C
## Event Stream
![event storm result](./doc-image/event_storming_result.jpg)
    
## Code Structure
#### Dependency Services
- Kafka: Event Store & Event Bus
#### Domain Aggregate
- Order
- Payment
- Shipment
#### Server Role
- Command-Side: 
  - handle command request 
  - write events to Kafka
- Query-Side: 
  - consume events from Kafka to build data view
  - return query request
- Event-Handler
  - consume events from Kafka to send command request to Command-side

## How to run application
#### Prepare dependency services by docker-compose for run application outside docker
```shell
# should rim build first
./gradlew -Dskip.tests build
# run order command query and others
docker compose -f Docker/observe-docker-compose.yaml -f Docker/boot-run-apps-docker-compose.yml -f Docker/kafka-docker-compose.yml --env-file Docker/config/.env.docker -p event-sourcing up -d --scale prometheus=0
```

```shell
# observability components for application 
docker compose -f Docker/observe-docker-compose.yaml -p event-sourcing-observe up
# kafka for application
docker compose -f Docker/kafka-docker-compose.yml -p event-sourcing-kafka up
```
#### Run applications 
- run by docker-compose
```shell
# all service with all observability components and kafka
docker compose -f Docker/observe-docker-compose.yaml -f Docker/boot-apps-docker-compose.yml -f Docker/kafka-docker-compose.yml --env-file Docker/config/.env.docker --profile order  -p event-sourcing up
# all order service without
docker compose -f Docker/boot-apps-docker-compose.yml -f Docker/kafka-docker-compose.yml --env-file Docker/config/.env.docker --profile order -p event-sourcing up
# only order-command-side
docker compose -f Docker/boot-apps-docker-compose.yml -f Docker/kafka-docker-compose.yml --env-file Docker/config/.env.docker --profile order -p event-sourcing up --scale order-handler=0 --scale order-query=0
# only payment-command-side
docker compose -f Docker/boot-apps-docker-compose.yml -f Docker/kafka-docker-compose.yml --env-file Docker/config/.env.docker --profile payment -p event-sourcing up --scale payment-handler=0 --scale payment-query=0
# only shipment-command-side
docker compose -f Docker/boot-apps-docker-compose.yml -f Docker/kafka-docker-compose.yml --env-file Docker/config/.env.docker --profile shipment -p event-sourcing up --scale shipment-handler=0 --scale shipment-query=0
``` 

- run by terminal:

```
./../../gradlew bootRun
```


- run by IDE 
  
  set the working directory as `{path-to-project-root}/event-sourcing-order-poc`

## Run E2E test in local

### prerequisite
- install nektos/act (https://github.com/nektos/act)
- run docker deamon
### command
- for mac with M2 chip
```
act -j e2e-test --container-architecture linux/amd64
```
- for normal linux based kernel
```
act -j e2e-test
```

## Endpoint

| server           | swagger url                                 |
|------------------|---------------------------------------------|
| Order Command    | http://localhost:8081/swagger-ui/index.html |
| Order Handler    | http://localhost:8082/swagger-ui/index.html |
| Order Query      | http://localhost:8083/swagger-ui/index.html |
| Payment Command  | http://localhost:8084/swagger-ui/index.html |
| Payment Handler  | http://localhost:8085/swagger-ui/index.html |
| Payment Query    | http://localhost:8086/swagger-ui/index.html |
| Shipment Command | http://localhost:8087/swagger-ui/index.html |
| Shipment Handler | http://localhost:8088/swagger-ui/index.html |
| Shipment Query   | http://localhost:8089/swagger-ui/index.html |
| Grafana          | http://localhost:3000                       |
| Prometheus       | http://localhost:9090                       |