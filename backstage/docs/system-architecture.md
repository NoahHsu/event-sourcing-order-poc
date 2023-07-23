# System Architecture

```kroki-mermaid
C4Context
    title System Architecture of Event Sourcing POC

    System_Boundary(main, "App Boundary") {

        Boundary(Aggregate, "Aggregate") {
            System(SystemC, "Command Side")
            System(SystemQ, "Query Side")
            SystemDb(DBO, "Data Snapshot for Query Side")
        }

        SystemQueue(Kafka, "Event Source (Kafka)", "The single source of truth for each Data Snapshot")
    }

    System_Ext(Loki, "Loki", "Log Aggregation")
    System_Ext(Tempo, "Tempo", "Distributed Tracing Backend")
    System_Ext(Prometheus, "Prometheus", "Monitoring System &<br> Time Series Database")
    System_Ext(Grafana, "Grafana", "The open observability platform")

    BiRel(SystemQ, DBO, "Write/<br>Read")
    Rel(SystemC, Kafka, "Produce Event", "TCP")
    Rel(SystemQ, Kafka, "Consume Event", "TCP")
    Rel(SystemC, SystemQ, "Query", "RESTful")

```

### Dependency Services
- Kafka: Event Store & Event Bus
- Loki: Log aggregation system
- Tempo: Distributed tracing backend
- Prometheus: Monitoring system & time series database
- Grafana: The open observability platform
