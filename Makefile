kafka-up:
	docker-compose -f Docker/docker-compose.yml up -d

kafka-down:
	docker-compose -f Docker/docker-compose.yml down