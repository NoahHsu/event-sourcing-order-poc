# Quick Start
## How to run application
### With local code
```shell
# should run build first
./gradlew build -x test

# run all command, query side, and dependencies 
docker compose -f Docker/observe-docker-compose.yaml -f Docker/boot-run-order-apps-docker-compose.yml -f Docker/boot-run-payment-apps-docker-compose.yml -f Docker/boot-run-shipment-apps-docker-compose.yml -f Docker/kafka-docker-compose.yml --env-file Docker/config/.env.docker -p event-sourcing up

# run order command query and dependencies
docker compose -f Docker/observe-docker-compose.yaml -f Docker/boot-run-order-apps-docker-compose.yml -f Docker/kafka-docker-compose.yml --env-file Docker/config/.env.docker -p event-sourcing up -d --scale prometheus=0

# run payment command query and dependencies
docker compose -f Docker/observe-docker-compose.yaml -f Docker/boot-run-payment-apps-docker-compose.yml -f Docker/kafka-docker-compose.yml --env-file Docker/config/.env.docker -p event-sourcing up -d --scale prometheus=0

# run shipment command query and dependencies
docker compose -f Docker/observe-docker-compose.yaml -f Docker/boot-run-shipment-apps-docker-compose.yml -f Docker/kafka-docker-compose.yml --env-file Docker/config/.env.docker -p event-sourcing up -d --scale prometheus=0

```

#### Run applications
### With docker image on the Docker Hub
```shell
# all service with all observability components and kafka
docker compose -f Docker/observe-docker-compose.yaml -f Docker/boot-apps-docker-compose.yml -f Docker/kafka-docker-compose.yml --env-file Docker/config/.env.docker --profile all -p event-sourcing up
# all order service without observability
docker compose -f Docker/boot-apps-docker-compose.yml -f Docker/kafka-docker-compose.yml --env-file Docker/config/.env.docker --profile order -p event-sourcing up
# only order-command-side
docker compose -f Docker/boot-apps-docker-compose.yml -f Docker/kafka-docker-compose.yml --env-file Docker/config/.env.docker --profile order -p event-sourcing up --scale order-handler=0 --scale order-query=0
# only payment-command-side
docker compose -f Docker/boot-apps-docker-compose.yml -f Docker/kafka-docker-compose.yml --env-file Docker/config/.env.docker --profile payment -p event-sourcing up --scale payment-handler=0 --scale payment-query=0
# only shipment-command-side
docker compose -f Docker/boot-apps-docker-compose.yml -f Docker/kafka-docker-compose.yml --env-file Docker/config/.env.docker --profile shipment -p event-sourcing up --scale shipment-handler=0 --scale shipment-query=0
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