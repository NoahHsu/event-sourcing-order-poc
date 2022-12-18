#!/bin/sh
SECONDS=0
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.0.5.jdk/Contents/Home
./gradlew build
root=$(pwd)
JAR_BUILD=$SECONDS

# order
cd order/command-side
FIRST_BUILD=$SECONDS
docker build -t docker-order-command-side:0.0.2 .
cd $root

cd order/event-handler
docker build -t docker-order-event-handler:0.0.2 .
cd $root

cd order/query-side
docker build -t docker-order-query-side:0.0.2 .
cd $root

# payment
cd payment/command-side
docker build -t docker-payment-command-side:0.0.2 .
cd $root

cd payment/event-handler
docker build -t docker-payment-event-handler:0.0.2 .
cd $root

cd payment/query-side
docker build -t docker-payment-query-side:0.0.2 .
cd $root

# shipment
cd shipment/command-side
docker build -t docker-shipment-command-side:0.0.2 .
cd $root

cd shipment/event-handler
docker build -t docker-shipment-event-handler:0.0.2 .
cd $root

cd shipment/query-side
docker build -t docker-shipment-query-side:0.0.2 .
cd $root

echo "It takes $JAR_BUILD seconds to complete gradle build... fist image takes $FIRST_BUILD.."
echo "It takes $SECONDS seconds to complete all task"