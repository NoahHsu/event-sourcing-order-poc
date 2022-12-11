#!/bin/sh
SECONDS=0
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.0.5.jdk/Contents/Home
./gradlew build
root=$(pwd)

# order
cd order/command-side
docker build -t docker-order-command-side .
cd $root

cd order/event-handler
docker build -t docker-order-event-handler .
cd $root

cd order/query-side
docker build -t docker-order-query-side .
cd $root

# payment
cd payment/command-side
docker build -t docker-payment-command-side .
cd $root

cd payment/event-handler
docker build -t docker-payment-event-handler .
cd $root

cd payment/query-side
docker build -t docker-payment-query-side .
cd $root

# shipment
cd shipment/command-side
docker build -t docker-shipment-command-side .
cd $root

cd shipment/event-handler
docker build -t docker-shipment-event-handler .
cd $root

cd shipment/query-side
docker build -t docker-shipment-query-side .
cd $root

echo "It takes $SECONDS seconds to complete this task..."