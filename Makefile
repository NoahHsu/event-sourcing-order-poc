kafka-up:
	docker-compose -f Docker/docker-compose.yml -p event-sourcing up -d

kafka-pause:
	docker-compose -f Docker/docker-compose.yml -p event-sourcing pause

kafka-down:
	docker-compose -f Docker/docker-compose.yml -p event-sourcing down