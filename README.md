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
####prepare dependency services by docker-compose
```shell
# start dependency container services
make kafka-up
# pause dependency container services
make kafka-pause
# stop dependency container services
make kafka-down
```
#### run applications 
// TODO add service docker compose

for now just run each project bt either following way
- run by IDE  
- ```./../gradlew bootRun```

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
